package com.jygoh.whoever.domain.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedExtension(originalFilename)) {
            throw new IllegalArgumentException(
                "Invalid file type. Only JPG, JPEG, PNG, and GIF are allowed.");
        }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());
        return "http://localhost:8080/images/" + fileName;
    }


    private boolean isAllowedExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (allowedExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
