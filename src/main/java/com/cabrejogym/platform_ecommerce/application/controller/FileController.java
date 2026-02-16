package com.cabrejogym.platform_ecommerce.application.controller;

import com.cabrejogym.platform_ecommerce.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{folder}/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String folder, @PathVariable String filename) {
        String fileUrl = "http://localhost:8080/api/files/" + folder + "/" + filename;
        byte[] fileContent = fileStorageService.loadFile(fileUrl);

        // Determinar content type basado en extensión
        String contentType = filename.endsWith(".png") ? "image/png" : "image/jpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(fileContent);
    }
}
