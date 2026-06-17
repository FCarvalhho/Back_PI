package com.senai.PI_mecado_preso.storage.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.senai.PI_mecado_preso.storage.api.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
class LocalStorageServiceImpl implements StorageService {

    private final Path raiz = Paths.get("uploads").toAbsolutePath().normalize();

    public LocalStorageServiceImpl() {
        try {
            Files.createDirectories(raiz);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de uploads: " + raiz, e);
        }
    }

    @Override
    public String armazenar(MultipartFile arquivo) {
        try {
            if (arquivo.isEmpty()) {
                throw new IllegalArgumentException("Não é possível salvar um arquivo vazio.");
            }

            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path destino = this.raiz.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), destino);
            return "/files/" + nomeArquivo;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao armazenar arquivo.", e);
        }
    }

    @Override
    public void remover(String urlArquivo) {
        // Implementação futura de deleção para evitar acúmulo de lixo no disco
    }
}