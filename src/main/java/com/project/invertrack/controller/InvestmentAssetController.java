package com.project.invertrack.controller;

import com.project.invertrack.dto.AssetDTO;
import com.project.invertrack.model.InvestmentAsset;
import com.project.invertrack.service.InvestmentAssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class InvestmentAssetController {

    private final InvestmentAssetService assetService;

    public InvestmentAssetController(InvestmentAssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody AssetDTO assetDTO) {
        try {
            return ResponseEntity.ok(assetService.createAsset(assetDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el activo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAssets() {
        try {
            return ResponseEntity.ok(assetService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los activos: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssetById(@PathVariable Long id) {
        try {
            return assetService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el activo: " + e.getMessage());
        }
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<?> getAssetBySymbol(@PathVariable String symbol) {
        try {
            return assetService.findBySymbol(symbol)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el activo: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @RequestBody AssetDTO assetDTO) {
        try {
            return ResponseEntity.ok(assetService.updateAsset(id, assetDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el activo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        try {
            assetService.deleteAsset(id);
            return ResponseEntity.ok("Activo eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el activo: " + e.getMessage());
        }
    }
} 