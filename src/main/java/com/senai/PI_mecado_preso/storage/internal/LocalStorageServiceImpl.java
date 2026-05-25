/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.senai.PI_mecado_preso.storage.internal;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.senai.PI_mecado_preso.storage.api.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cansei2
 */
@Service
public class LocalStorageServiceImpl implements StorageService {

    // Pasta onde as imagens vão ficar no seu computador
    private final Path raiz = Paths.get("uploads");

    public LocalStorageServiceImpl() {
        try {
            Files.createDirectories(raiz);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads");
        }
    }

    @Override
    public String armazenar(MultipartFile arquivo) {
        try {
            // Gera um nome único para não sobrescrever imagens com o mesmo nome
            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path destino = this.raiz.resolve(nomeArquivo);
            Files.copy(arquivo.getInputStream(), destino);
            
            // Retorna a URL (vamos configurar o Spring para servir essa rota)
            return "http://localhost:8080/files/" + nomeArquivo;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao armazenar arquivo.", e);
        }
    }

    @Override
    public void remover(String urlArquivo) {
        // Lógica para deletar o arquivo do disco usando a URL
    }
}
