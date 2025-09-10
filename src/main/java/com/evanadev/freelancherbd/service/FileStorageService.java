package com.evanadev.freelancherbd.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final String UPLOAD_DIR = "uploads"; // base folder inside project root

    public String saveFile(MultipartFile file, String subDir) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty: " + file.getOriginalFilename());
            }

            // create directory if not exists
            Path uploadPath = Paths.get(UPLOAD_DIR, subDir);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // build file path
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // save file
            Files.write(filePath, file.getBytes());
            return filePath.toString(); // return full path
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + file.getOriginalFilename(), e);
        }
    }
}

