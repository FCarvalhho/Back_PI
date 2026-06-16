package com.senai.PI_mecado_preso.storage.api;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String armazenar(MultipartFile arquivo);

    void remover(String urlArquivo);
}
