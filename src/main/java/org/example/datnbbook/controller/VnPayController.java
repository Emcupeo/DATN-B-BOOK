package org.example.datnbbook.controller;

import org.example.datnbbook.service.VnPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/payment/vnpay")
public class VnPayController {
    private static final Logger logger = LoggerFactory.getLogger(VnPayController.class);

    @Autowired
    private VnPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        try {
            long amount = Long.parseLong(String.valueOf(payload.getOrDefault("amount", 0)));
            String orderCode = String.valueOf(payload.getOrDefault("orderCode", ""));
            String ip = request.getRemoteAddr();
            String paymentUrl = vnPayService.createPaymentUrl(amount, orderCode, ip);
            Map<String, Object> result = new HashMap<>();
            result.put("paymentUrl", paymentUrl);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating VNPAY payment", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/return")
    public ResponseEntity<Map<String, Object>> paymentReturn(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        parameterMap.forEach((k, v) -> params.put(k, v[0]));

        boolean isValid = vnPayService.verifyCallback(new HashMap<>(params));
        String responseCode = params.getOrDefault("vnp_ResponseCode", "99");

        Map<String, Object> result = new HashMap<>();
        result.put("isValid", isValid);
        result.put("vnp_ResponseCode", responseCode);
        result.put("orderCode", params.get("vnp_TxnRef"));
        return ResponseEntity.ok(result);
    }
}


