package com.memorygame.service;

import com.memorygame.model.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final RestTemplate restTemplate;
    
    @Value("${unsplash.api.key}")
    private String unsplashApiKey;
    
    public ImageService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Image> getImagesByCategory(String category) {
        try {
            // Using Unsplash API to get images by category
            String url = "https://api.unsplash.com/search/photos?query=" + category + 
                        "/&per_page=8&client_id=" + unsplashApiKey;
            
            ResponseEntity<UnsplashResponse> response = 
                restTemplate.getForEntity(url, UnsplashResponse.class);
            
            if (response.getBody() != null && response.getBody().getResults() != null) {
                return response.getBody().getResults().stream()
                    .map(result -> new Image(
                        result.getId(),
                        result.getUrls().getRaw() + "&w=150&h=150&fit=crop",
                        result.getAlt_description() != null ? result.getAlt_description() : category,
                        category
                    ))
                    .collect(Collectors.toList());
            }
            
            return getFallbackImages(category);
        } catch (Exception e) {
            e.printStackTrace();
            return getFallbackImages(category);
        }
    }

    public List<String> getAvailableCategories() {
        return Arrays.asList("animals", "nature", "food", "travel", "technology");
    }

    private List<Image> getFallbackImages(String category) {
        // Fallback to placeholder images if API fails
        List<Image> fallbackImages = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            fallbackImages.add(new Image(
                String.valueOf(i),  // Convert int to String for id
                "https://via.placeholder.com/150x150?text=" + category + "+" + i,
                category + " " + i,
                category
            ));
        }
        return fallbackImages;
    }

    // Inner classes to map Unsplash API response
    private static class UnsplashResponse {
        private List<UnsplashResult> results;

        public List<UnsplashResult> getResults() {
            return results;
        }

        public void setResults(List<UnsplashResult> results) {
            this.results = results;
        }
    }

    private static class UnsplashResult {
        private String id;
        private String alt_description;
        private UnsplashUrls urls;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAlt_description() {
            return alt_description;
        }

        public void setAlt_description(String alt_description) {
            this.alt_description = alt_description;
        }

        public UnsplashUrls getUrls() {
            return urls;
        }

        public void setUrls(UnsplashUrls urls) {
            this.urls = urls;
        }
    }

    private static class UnsplashUrls {
        private String raw;
        private String regular;
    
        public String getRaw() {
            return raw;
        }
    
        public void setRaw(String raw) {
            this.raw = raw;
        }
    
        public String getRegular() {
            return regular;
        }
    
        public void setRegular(String regular) {
            this.regular = regular;
        }
    }
}

