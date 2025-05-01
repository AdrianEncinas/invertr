package com.project.invertrack.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceService {

    @Value("${twelvedata.api.key}")
    private String apiKey;

    public Map<String, Object> getAssetInfo(List<String> symbols) {
        try {
            String url = "https://api.twelvedata.com/price?symbol=" + String.join(",", symbols) + "&apikey=" + apiKey;
            System.out.println(url);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            
            if (root.has("code") && root.get("code").asInt() != 200) {
                throw new RuntimeException("Error en la API: " + root.get("message").asText());
            }
            
            Map<String, Object> responseMap = new HashMap<>();
            for (String symbol : symbols) {
                responseMap.put(symbol, root.path(symbol).path("price").asDouble());
            }
            
            return responseMap;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información del asset: " + e.getMessage());
        }
    }
} 