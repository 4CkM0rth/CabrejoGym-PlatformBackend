package com.cabrejogym.platform_ecommerce.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String folder);
    void deleteFile(String fileUrl);
    byte[] loadFile(String fileUrl);
}
