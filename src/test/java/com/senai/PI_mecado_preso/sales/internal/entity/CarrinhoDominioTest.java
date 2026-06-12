package com.senai.PI_mecado_preso.sales.internal.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarrinhoDominioTest {

    @Test
    @DisplayName("Deve somar a quantidade caso o item adicionado já exista no carrinho")
    void deveAcumularQuantidadeAoAdicionarItemExistente() {
        // Arrange
        Carrinho carrinho = new Carrinho();
        carrinho.setId(UUID.randomUUID());
        carrinho.setClienteId(UUID.randomUUID());
        
        UUID variacaoId = UUID.randomUUID();

        // Act
        carrinho.adicionarItem(variacaoId, 2);
        carrinho.adicionarItem(variacaoId, 5);

        // Assert
        assertEquals(1, carrinho.getItens().size(), "O carrinho deve possuir apenas um registro de item para a variação");
        
        ItemCarrinho itemResultante = carrinho.getItens().iterator().next();
        assertEquals(7, itemResultante.getQuantidade(), "A quantidade final computada deve ser a soma exata (2 + 5 = 7)");
        assertNotNull(carrinho.getAtualizadoEm(), "A propriedade de controle temporal deve ser registrada");
    }
}