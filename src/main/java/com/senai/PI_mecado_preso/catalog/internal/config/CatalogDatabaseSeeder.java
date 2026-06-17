package com.senai.PI_mecado_preso.catalog.internal.config;

import com.senai.PI_mecado_preso.catalog.api.dtos.*;
import com.senai.PI_mecado_preso.catalog.internal.service.AtributoService;
import com.senai.PI_mecado_preso.catalog.internal.service.ProdutoService;
import com.senai.PI_mecado_preso.catalog.internal.service.ProdutoVariacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Configuration
class CatalogDatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(CatalogDatabaseSeeder.class);

    @Bean
    CommandLineRunner initCatalogDatabase(
            AtributoService atributoService,
            ProdutoService produtoService,
            ProdutoVariacaoService variacaoService) {

        return args -> {
            try {
                log.info("[Catalog Seeder] Verificando a necessidade de inicialização de dados...");

                if (!produtoService.listar().isEmpty()) {
                    log.info("[Catalog Seeder] Base de dados já possui registros. Pulando.");
                    return;
                }

                log.info("[Catalog Seeder] Iniciando população completa de 3 produtos com 2 variações cada...");

                // 1. Garantir ou Criar os Atributos Globais Dinamicamente
                UUID idAtributoCor = garantirAtributo(atributoService, "Cor");
                UUID idAtributoTamanho = garantirAtributo(atributoService, "Tamanho");
                List<UUID> atributosPadrao = List.of(idAtributoCor, idAtributoTamanho);

                // =========================================================================
                // PRODUTO 1: Camiseta Premium Streetwear Oversized
                // =========================================================================
                ProdutoRequestDTO p1Request = new ProdutoRequestDTO(
                        "Camiseta Premium Streetwear Oversized",
                        "Camiseta de alta qualidade confeccionada em 100% algodão egípcio, malha de 210g com toque macio e modelagem confortável.",
                        atributosPadrao
                );
                ProdutoResponseDTO p1Salvo = produtoService.salvar(p1Request);

                // Variação 1.1: Preta M
                variacaoService.salvar(p1Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-CAM-OVER-PRT-M",
                        new BigDecimal("129.90"),
                        80,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Preto"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "M")),
                        List.of(new ImagemVariacaoRequestDTO("/files/b6ef8698-e0fa-42c9-89c7-c72607373abf_camisaPreto.webp", 1), new ImagemVariacaoRequestDTO("/files/dc00e4bc-a22d-4cc6-a71c-b340bf6152e7_camisaPreto2.jpg", 2))
                ));

                // Variação 1.2: Preta G
                variacaoService.salvar(p1Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-CAM-OVER-PRT-G",
                        new BigDecimal("134.90"),
                        45,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Preto"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "G")),
                        List.of(new ImagemVariacaoRequestDTO("/files/b6ef8698-e0fa-42c9-89c7-c72607373abf_camisaPreto.webp", 1), new ImagemVariacaoRequestDTO("/files/dc00e4bc-a22d-4cc6-a71c-b340bf6152e7_camisaPreto2.jpg", 2))
                ));
                log.info("[Catalog Seeder] Produto 1 (Camiseta) e suas 2 variações cadastrados.");

                // =========================================================================
                // PRODUTO 2: Tênis Sport Run Performance
                // =========================================================================
                ProdutoRequestDTO p2Request = new ProdutoRequestDTO(
                        "Tênis Sport Run Performance",
                        "Tênis ideal para corrida de alta performance, com amortecimento responsivo tecnológico e cabedal em malha respirável.",
                        atributosPadrao
                );
                ProdutoResponseDTO p2Salvo = produtoService.salvar(p2Request);

                // Variação 2.1: Azul 40
                variacaoService.salvar(p2Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-TEN-RUN-AZL-40",
                        new BigDecimal("349.90"),
                        25,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Azul"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "40")),
                        List.of(new ImagemVariacaoRequestDTO("/files/4871b118-5199-41da-9107-b92bbbaa6238_tenisAzul.webp", 1))
                ));

                // Variação 2.2: Azul 41
                variacaoService.salvar(p2Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-TEN-RUN-AZL-41",
                        new BigDecimal("349.90"),
                        30,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Azul"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "41")),
                        List.of(new ImagemVariacaoRequestDTO("/files/4871b118-5199-41da-9107-b92bbbaa6238_tenisAzul.webp", 1))
                ));
                log.info("[Catalog Seeder] Produto 2 (Tênis) e suas 2 variações cadastrados.");

                // =========================================================================
                // PRODUTO 3: Boné Aba Curva Minimalist
                // =========================================================================
                ProdutoRequestDTO p3Request = new ProdutoRequestDTO(
                        "Boné Aba Curva Minimalist",
                        "Boné estilo de aba curva em tecido sarja robusto, fecho ajustável Strapback em metal e logo sutil bordado.",
                        atributosPadrao
                );
                ProdutoResponseDTO p3Salvo = produtoService.salvar(p3Request);

                // Variação 3.1: Grafite Único
                variacaoService.salvar(p3Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-BON-MIN-GRA-UN",
                        new BigDecimal("79.90"),
                        100,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Grafite"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "Único")),
                        List.of(new ImagemVariacaoRequestDTO("/files/18c01c4d-9e48-49a0-9d64-aa6d1f2b72f0_boneGrafite.webp", 1))
                ));

                // Variação 3.2: Branco Único
                variacaoService.salvar(p3Salvo.id(), new ProdutoVariacaoRequestDTO(
                        "SKU-BON-MIN-BRC-UN",
                        new BigDecimal("79.90"),
                        50,
                        List.of(new VariacaoOpcaoRequestDTO(idAtributoCor, "Branco"), new VariacaoOpcaoRequestDTO(idAtributoTamanho, "Único")),
                        List.of(new ImagemVariacaoRequestDTO("/files/c6ad396e-4bdb-46a8-8fcc-705808ef3a0e_boneBranco.webp", 1))
                ));
                log.info("[Catalog Seeder] Produto 3 (Boné) e suas 2 variações cadastrados.");

                log.info("[Catalog Seeder] Carga inicial completa do catálogo finalizada com sucesso!");

            } catch (Exception e) {
                log.error("=================================================================");
                log.error("[ERRO CRÍTICO NO SEEDER] Falha ao preencher tabelas do catálogo:", e);
                log.error("=================================================================");
                throw e;
            }
        };
    }

    private UUID garantirAtributo(AtributoService atributoService, String nomeAtributo) {
        return atributoService.listar().stream()
                .filter(a -> a.nome().equalsIgnoreCase(nomeAtributo))
                .map(AtributoResponseDTO::id)
                .findFirst()
                .orElseGet(() -> {
                    log.info("[Catalog Seeder] Atributo '{}' não encontrado. Criando dinamicamente...", nomeAtributo);
                    AtributoRequestDTO novoAtributo = new AtributoRequestDTO(nomeAtributo);
                    AtributoResponseDTO atributoSalvo = atributoService.salvar(novoAtributo);
                    return atributoSalvo.id();
                });
    }
}