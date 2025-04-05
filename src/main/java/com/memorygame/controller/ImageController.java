package com.memorygame.controller;

import com.memorygame.model.Image;
import com.memorygame.service.ImageService;

import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
// Remove the @CrossOrigin annotation here since we're handling CORS in WebConfig
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images")
    public ResponseEntity<List<Image>> getImagesByCategory(@RequestParam @NotBlank String category) {
        List<Image> images = imageService.getImagesByCategory(category);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = imageService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }
}

