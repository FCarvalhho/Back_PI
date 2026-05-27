package com.senai.PI_mecado_preso.storage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path caminhoUploads = Paths.get("uploads");
        String caminhoAbsoluto = caminhoUploads.toFile().getAbsolutePath();

        // Sempre que o navegador pedir /files/nome-da-imagem.png...
        registry.addResourceHandler("/files/**")
                // ...o Spring vai buscar dentro da pasta física uploads/
                .addResourceLocations("file:/" + caminhoAbsoluto + "/");
    }
}
