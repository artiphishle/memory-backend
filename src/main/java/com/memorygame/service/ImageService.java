package com.memorygame.service;

import com.memorygame.model.Image;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            String url = "https://api.unsplash.com/search/photos?query=" + category +
                "&per_page=8&client_id=" + unsplashApiKey;

            ResponseEntity<UnsplashResponse> response =
                    restTemplate.getForEntity(url, UnsplashResponse.class);

            List<UnsplashResult> results = Optional
                .ofNullable(response.getBody())
                .map(UnsplashResponse::getResults)
                .orElse(null);

            if (results != null) {
                return results.stream()
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
        List<Image> fallbackImages = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            fallbackImages.add(new Image(
                    String.valueOf(i),
                    "https://via.placeholder.com/150x150?text=" + category + "+" + i,
                    category + " " + i,
                    category
            ));
        }
        return fallbackImages;
    }

    // --- Lombok-powered DTOs ---

    @Data
    private static class UnsplashResponse {
        private List<UnsplashResult> results;
    }

    @Data
    private static class UnsplashResult {
        private String id;
        private String alt_description;
        private UnsplashUrls urls;
    }

    @Data
    private static class UnsplashUrls {
        private String raw;
        private String regular;
    }
}
