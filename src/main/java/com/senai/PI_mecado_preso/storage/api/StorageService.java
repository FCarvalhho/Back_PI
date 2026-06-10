/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.senai.PI_mecado_preso.storage.api;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Cansei2
 */
public interface StorageService {

    String armazenar(MultipartFile arquivo);

    void remover(String urlArquivo);
}
