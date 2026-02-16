package com.cabrejogym.platform_ecommerce.application.service.impl;

import com.cabrejogym.platform_ecommerce.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080/api/files}")
    private String baseUrl;

    @Override
    public String storeFile(MultipartFile file, String folder) {
        try {
            // Validar archivo
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }

            // Validar tipo de archivo
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                throw new RuntimeException("Only JPG and PNG images are allowed");
            }

            // Obtener extensión
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // Generar nombre único
            String filename = UUID.randomUUID().toString() + extension;

            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir, folder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar archivo
            Path targetLocation = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Retornar URL
            return baseUrl + "/" + folder + "/" + filename;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            // Extraer path del archivo desde la URL
            String filePath = fileUrl.replace(baseUrl + "/", "");
            Path path = Paths.get(uploadDir, filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to delete file", ex);
        }
    }

    @Override
    public byte[] loadFile(String fileUrl) {
        try {
            String filePath = fileUrl.replace(baseUrl + "/", "");
            Path path = Paths.get(uploadDir, filePath);

            if (!Files.exists(path)) {
                throw new RuntimeException("File not found");
            }

            return Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load file", ex);
        }
    }
}
