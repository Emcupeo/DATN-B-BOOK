package org.example.datnbbook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageKitService {
    
    private static final String PRIVATE_KEY = "private_xIPOEb8sE7AREhsSpGDva56+gRU=";
    private static final String PUBLIC_KEY = "public_5XG5VTNIYTdeOVWhFzTxDgFz6po=";
    private static final String URL_ENDPOINT = "https://ik.imagekit.io/emcupeo";
    
    /**
     * Tạo signature cho ImageKit authentication
     */
    public String generateSignature(String token, String expire) {
        try {
            // Theo tài liệu ImageKit: signature = HMAC-SHA1(token + expire, privateKey)
            String data = token + expire;
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }
    
    /**
     * Tạo authentication token cho ImageKit
     */
    public Map<String, Object> getAuthenticationParameters() {
        try {
            // Sử dụng timestamp hiện tại làm token
            long currentTime = System.currentTimeMillis() / 1000;
            String token = String.valueOf(currentTime);
            String expire = String.valueOf(currentTime + 600); // 10 minutes from now
            
            // Tạo signature
            String signature = generateSignature(token, expire);
            
            System.out.println("[DEBUG] ImageKit Auth - Token: " + token);
            System.out.println("[DEBUG] ImageKit Auth - Expire: " + expire);
            System.out.println("[DEBUG] ImageKit Auth - Signature: " + signature);
            System.out.println("[DEBUG] ImageKit Auth - Data for signature: " + token + expire);
            
            Map<String, Object> authParams = new HashMap<>();
            authParams.put("token", token);
            authParams.put("expire", expire);
            authParams.put("signature", signature);
            authParams.put("publicKey", PUBLIC_KEY);
            authParams.put("urlEndpoint", URL_ENDPOINT);
            
            return authParams;
        } catch (Exception e) {
            throw new RuntimeException("Error generating authentication parameters", e);
        }
    }
    
    /**
     * Kiểm tra xem URL có phải là ImageKit URL không
     */
    public boolean isImageKitUrl(String url) {
        return url != null && url.contains("ik.imagekit.io");
    }
    
    /**
     * Tạo URL với transformation
     */
    public String getTransformedUrl(String originalUrl, int width, int height) {
        if (!isImageKitUrl(originalUrl)) {
            return originalUrl;
        }
        
        // Thêm transformation parameters vào URL
        String separator = originalUrl.contains("?") ? "&" : "?";
        return originalUrl + separator + "tr=w-" + width + ",h-" + height + ",c-at_max";
    }
} 