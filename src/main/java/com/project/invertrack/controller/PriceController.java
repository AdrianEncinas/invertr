package com.project.invertrack.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/price")
public class PriceController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${twelvedata.api.key}")
    private String apiKey;
    
    @Value("${twelvedata.api.url}")
    private String apiUrl;

    public PriceController() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/test")
    public String getMethodName(@RequestParam String param) {
        String url = String.format("https://api.twelvedata.com/stocks?symbol=%s&apikey=%s", param, apiKey);
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(url);
        return response;
    }
    
    @GetMapping("/value/{symbol}")
    public ResponseEntity<Map<String, Object>> getAssetInfo(@PathVariable String symbol) {
        try {
            String url = String.format("%s/time_series?symbol=%s&interval=1day&apikey=%s", 
                apiUrl, symbol, apiKey);
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            if (root.has("status") && root.get("status").asText().equals("error")) {
                return ResponseEntity.badRequest().body(Map.of("error", root.get("message").asText()));
            }
            
            JsonNode values = root.path("values").get(0);
            System.out.println(values.get("close"));
            Map<String, Object> responseMap = Map.of(
                "price", new BigDecimal(values.path("close").asText())
            );
            
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/graphic/{symbol}")
    public ResponseEntity<List<Map<String, Object>>> getGraphicInterval(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1h") String interval) {  
        try {
            String url = String.format("https://api.twelvedata.com/time_series?symbol=%s&interval=%s&apikey=%s", 
                symbol, interval, apiKey);
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);        
            JsonNode values = root.path("values");
            List<Map<String, Object>> data = new ArrayList<>();
            for (JsonNode value : values) {
                Map<String, Object> valueMap = Map.of(
                    "date", value.path("datetime").asText(),
                    "price", new BigDecimal(value.path("close").asText())
                );
                data.add(valueMap);
            }
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}

