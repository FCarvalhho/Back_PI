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

                log.info("[Catalog Seeder] Iniciando população completa de dados estruturados...");

                UUID idAtributoCor = garantirAtributo(atributoService, "Cor");
                UUID idAtributoTamanho = garantirAtributo(atributoService, "Tamanho");
                List<UUID> atributosDoProduto = List.of(idAtributoCor, idAtributoTamanho);
                
                ProdutoRequestDTO produtoRequest = new ProdutoRequestDTO(
                        "Camiseta Premium Streetwear Oversized",
                        "Camiseta de alta qualidade confeccionada em 100% algodão egípcio, malha de 210g com toque macio, costuras reforçadas e modelagem confortável de alto padrão.",
                        atributosDoProduto
                );

                ProdutoResponseDTO produtoSalvo = produtoService.salvar(produtoRequest);
                log.info("[Catalog Seeder] Produto base cadastrado com sucesso: ID {}", produtoSalvo.id());

                List<VariacaoOpcaoRequestDTO> opcoesVariacao1 = List.of(
                        new VariacaoOpcaoRequestDTO(idAtributoCor, "Preto"),
                        new VariacaoOpcaoRequestDTO(idAtributoTamanho, "M")
                );
                
                List<ImagemVariacaoRequestDTO> imagensVariacao1 = List.of(
                        new ImagemVariacaoRequestDTO("https://imagens.vendas.com/produtos/camiseta-preta-m-frente.jpg", 1),
                        new ImagemVariacaoRequestDTO("https://imagens.vendas.com/produtos/camiseta-preta-m-verso.jpg", 2)
                );

                ProdutoVariacaoRequestDTO variacao1Request = new ProdutoVariacaoRequestDTO(
                        "SKU-CAM-OVER-PRT-M",
                        new BigDecimal("129.90"),
                        80,
                        opcoesVariacao1,
                        imagensVariacao1
                );

                variacaoService.salvar(produtoSalvo.id(), variacao1Request);
                log.info("[Catalog Seeder] Variação cadastrada: SKU-CAM-OVER-PRT-M (Preço: 129.90 | Estoque: 80)");

                List<VariacaoOpcaoRequestDTO> opcoesVariacao2 = List.of(
                        new VariacaoOpcaoRequestDTO(idAtributoCor, "Preto"),
                        new VariacaoOpcaoRequestDTO(idAtributoTamanho, "G")
                );
                
                List<ImagemVariacaoRequestDTO> imagensVariacao2 = List.of(
                        new ImagemVariacaoRequestDTO("https://imagens.vendas.com/produtos/camiseta-preta-g-frente.jpg", 1),
                        new ImagemVariacaoRequestDTO("https://imagens.vendas.com/produtos/camiseta-preta-g-verso.jpg", 2)
                );

                ProdutoVariacaoRequestDTO variacao2Request = new ProdutoVariacaoRequestDTO(
                        "SKU-CAM-OVER-PRT-G",
                        new BigDecimal("134.90"),
                        45,
                        opcoesVariacao2,
                        imagensVariacao2
                );

                variacaoService.salvar(produtoSalvo.id(), variacao2Request);
                log.info("[Catalog Seeder] Variação cadastrada: SKU-CAM-OVER-PRT-G (Preço: 134.90 | Estoque: 45)");
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