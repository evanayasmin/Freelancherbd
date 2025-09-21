package com.evanadev.freelancherbd.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String UPLOAD_DIR = "uploads"; // base folder inside project root

    public String saveFile(MultipartFile file, String subDir) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty: " + file.getOriginalFilename());
            }

            // create directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR, subDir).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // Clean the filename (remove risky characters)
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            // Validate the filename (prevent path traversal)
            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Invalid file name: " + originalFilename);
            }
            // Generate a safe unique filename
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + originalFilename;

            //Save File
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE_NEW);

            return "/" + UPLOAD_DIR + "/" + subDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + file.getOriginalFilename(), e);
        }
    }
}

