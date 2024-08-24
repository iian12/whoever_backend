package com.jygoh.whoever.domain.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestPart MultipartFile file) {
        try {
            String fileUrl = imageService.uploadImage(file);
            ImageUploadResponse response = ImageUploadResponse.builder()
                    .url(fileUrl)
                    .build();

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // 실제 이미지 형식에 맞게 설정 (예: MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}
