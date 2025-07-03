package org.example.datnbbook.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;


@Service
public class ImgBBService {

    private final Logger logger = LoggerFactory.getLogger(ImgBBService.class);
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl = "https://api.imgbb.com/1/upload";

    @Autowired
    public ImgBBService(@Value("${imgbb.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }



    /**
     * Uploads an image to ImgBB and returns the URL
     *
     * @param file the image file to upload
     * @return the URL of the uploaded image
     * @throws IOException if there's an error processing the file
     */
    public String uploadImage(MultipartFile file) throws IOException {
        logger.info("Uploading image to ImgBB: {}", file.getOriginalFilename());

        // Convert MultipartFile to Base64
        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

        // Prepare request parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("key", apiKey);
        map.add("image", base64Image);
        // Optional: add name parameter with original filename
        if (file.getOriginalFilename() != null) {
            map.add("name", file.getOriginalFilename());
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            // Make API call to ImgBB
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            String responseBody = response.getBody();

            if (responseBody == null) {
                throw new IOException("Empty response from ImgBB");
            }

            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Check if the upload was successful
            if (!rootNode.path("success").asBoolean()) {
                String errorMessage = rootNode.path("error").path("message").asText("Unknown error");
                throw new IOException("ImgBB upload failed: " + errorMessage);
            }

            // Extract and return the image URL
            String imageUrl = rootNode.path("data").path("url").asText();
            logger.info("Image successfully uploaded to ImgBB: {}", imageUrl);
            return imageUrl;

        } catch (RestClientException e) {
            logger.error("Error communicating with ImgBB API", e);
            throw new IOException("Failed to upload image to ImgBB: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing ImgBB response", e);
            throw new IOException("Failed to parse ImgBB response: " + e.getMessage(), e);
        }
    }

    public String uploadToImgBB(MultipartFile file) {
        try {
            return uploadImage(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to ImgBB", e);
        }
    }
}