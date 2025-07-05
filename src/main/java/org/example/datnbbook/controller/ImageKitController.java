package org.example.datnbbook.controller;

import org.example.datnbbook.service.ImageKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/imagekit")
@CrossOrigin(origins = "*")
public class ImageKitController {

    @Autowired
    private ImageKitService imageKitService;

    /**
     * Lấy authentication parameters cho ImageKit
     */
    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> getAuthParameters() {
        try {
            Map<String, Object> authParams = imageKitService.getAuthenticationParameters();
            return ResponseEntity.ok(authParams);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Tạo URL với transformation
     */
    @PostMapping("/transform")
    public ResponseEntity<String> getTransformedUrl(
            @RequestParam String originalUrl,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) {
        try {
            String transformedUrl = imageKitService.getTransformedUrl(originalUrl, width, height);
            return ResponseEntity.ok(transformedUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 