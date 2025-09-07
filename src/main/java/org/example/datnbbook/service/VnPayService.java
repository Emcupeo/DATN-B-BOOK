package org.example.datnbbook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class VnPayService {

    @Value("${vnpay.tmnCode}")
    private String vnpTmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnpHashSecret;

    @Value("${vnpay.payUrl}")
    private String vnpPayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnpReturnUrl;

    @Value("${vnpay.version}")
    private String vnpVersion;

    @Value("${vnpay.command}")
    private String vnpCommand;

    @Value("${vnpay.currCode}")
    private String vnpCurrCode;

    @Value("${vnpay.locale}")
    private String vnpLocale;

    public String createPaymentUrl(long amountVnd, String orderCode, String clientIp) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", vnpVersion != null ? vnpVersion : "2.1.0");
        params.put("vnp_Command", vnpCommand != null ? vnpCommand : "pay");
        params.put("vnp_TmnCode", vnpTmnCode);
        params.put("vnp_Amount", String.valueOf(amountVnd * 100));
        params.put("vnp_CurrCode", vnpCurrCode != null ? vnpCurrCode : "VND");
        params.put("vnp_TxnRef", orderCode);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + orderCode);
        params.put("vnp_OrderType", "billpayment");
        params.put("vnp_Locale", vnpLocale != null ? vnpLocale : "vn");
        params.put("vnp_ReturnUrl", vnpReturnUrl);
        params.put("vnp_IpAddr", clientIp != null ? clientIp : "127.0.0.1");
        params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        
        // Thêm các tham số bắt buộc khác
        params.put("vnp_ExpireDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis() + 15 * 60 * 1000))); // 15 phút

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            String value = params.get(name);
            if (value != null && value.length() > 0) {
                hashData.append(name);
                hashData.append('=');
                hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                if (i < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String secureHash = hmacSHA512(vnpHashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String finalUrl = vnpPayUrl + "?" + query;
        
        // Log để debug
        System.out.println("VNPAY Payment URL: " + finalUrl);
        System.out.println("Hash Data: " + hashData.toString());
        System.out.println("Secure Hash: " + secureHash);
        
        return finalUrl;
    }

    public boolean verifyCallback(Map<String, String> allParams) {
        String vnpSecureHash = allParams.remove("vnp_SecureHash");
        allParams.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(allParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            String value = allParams.get(name);
            if (value != null && value.length() > 0) {
                hashData.append(name).append("=").append(value);
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                }
            }
        }
        String calcHash = hmacSHA512(vnpHashSecret, hashData.toString());
        return calcHash.equalsIgnoreCase(vnpSecureHash);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hash.append('0');
                hash.append(hex);
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA512", e);
        }
    }
}



