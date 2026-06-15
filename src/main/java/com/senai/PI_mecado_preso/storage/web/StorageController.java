package com.senai.PI_mecado_preso.storage.web;

import java.util.Map;

import com.senai.PI_mecado_preso.storage.api.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/produto/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> upload(
            @RequestParam("file") MultipartFile file
    ) {

        String url = storageService.armazenar(file);

        return ResponseEntity.ok(Map.of("url", url));
    }
}
