package com.senai.PI_mecado_preso;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithArchitectureTest {

    // Carrega as configurações baseadas na classe principal do seu Spring Boot
    ApplicationModules modules = ApplicationModules.of(PiMecadoPresoApplication.class);

    @Test
    void verificarConformidadeArquitetural() {
        // Esse método vai falhar o build se o módulo 'catalog', por exemplo,
        // tentar importar qualquer classe dentro de 'iam.internal'
        modules.verify();
    }

    @Test
    void gerarDocumentacaoDoDiagrama() {
        // Cria automaticamente documentações de texto e diagramas na pasta target/modulith-docs
        new Documenter(modules).writeModulesAsPlantUml();
    }

    @Test
    void verificarContratosExpostosDoIam() {
        // Esse teste garante que apenas o NamedInterface "api" do IAM possa ser importado por outros módulos.
        // Qualquer tentativa de outro módulo de importar o pacote "web.controller" fará o teste falhar.
        modules.getModuleByName("iam").ifPresent(iamModule -> {
            var namedInterfaces = iamModule.getNamedInterfaces();
            boolean possuiApenasApiExposta = namedInterfaces.stream()
                    .anyMatch(ni -> ni.getName().equals("api"));

            org.junit.jupiter.api.Assertions.assertTrue(possuiApenasApiExposta,
                    "O módulo IAM precisa expor a interface nomeada 'api'");
        });
    }

    @Test
    void gerarDocumentacaoDoMonolito() {
        // Correção: Métodos oficiais da API Documenter
        new Documenter(modules)
                .writeModulesAsPlantUml()           // Gera o diagrama macro do sistema
                .writeIndividualModulesAsPlantUml() // Gera diagramas isolados por módulo
                .writeModuleCanvases();             // Gera tabelas de metadados de cada módulo
    }
}