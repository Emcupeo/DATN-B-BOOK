package org.example.datnbbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/provinces")
@CrossOrigin(origins = "*")
public class ProvincesController {
    
    @Autowired
    private RestTemplate restTemplate;

    private static final String PROVINCES_API_BASE = "https://provinces.open-api.vn/api";

    @GetMapping("/p")
    public ResponseEntity<String> getProvinces() {
        try {
            String response = restTemplate.getForObject(PROVINCES_API_BASE + "/p/", String.class);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching provinces: " + e.getMessage());
        }
    }

    @GetMapping("/p/{provinceCode}")
    public ResponseEntity<String> getDistricts(@PathVariable String provinceCode, 
                                             @RequestParam(defaultValue = "2") int depth) {
        try {
            String response = restTemplate.getForObject(
                PROVINCES_API_BASE + "/p/" + provinceCode + "?depth=" + depth, 
                String.class
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching districts: " + e.getMessage());
        }
    }

    @GetMapping("/d/{districtCode}")
    public ResponseEntity<String> getWards(@PathVariable String districtCode,
                                         @RequestParam(defaultValue = "2") int depth) {
        try {
            String response = restTemplate.getForObject(
                PROVINCES_API_BASE + "/d/" + districtCode + "?depth=" + depth,
                String.class
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching wards: " + e.getMessage());
        }
    }
}