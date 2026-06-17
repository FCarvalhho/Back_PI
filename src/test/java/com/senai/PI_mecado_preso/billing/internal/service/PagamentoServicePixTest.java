package com.senai.PI_mecado_preso.billing.internal.service;

import com.senai.PI_mecado_preso.billing.api.CobrancaRequestDTO;
import com.senai.PI_mecado_preso.billing.api.CobrancaResponseDTO;
import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.billing.internal.repository.PagamentoRepository;
import com.senai.PI_mecado_preso.billing.internal.strategy.EstrategiaPagamento;
import com.senai.PI_mecado_preso.billing.internal.strategy.FabricaEstrategiaPagamento;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServicePixTest {

    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private FabricaEstrategiaPagamento fabricaEstrategia;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private TransactionTemplate transactionTemplate; 
    @Mock private EstrategiaPagamento estrategiaPagamento;

    @InjectMocks private PagamentoService pagamentoService;

    @Test
    @DisplayName("Deve gerar cobrança PIX com status PENDENTE e código copia e cola preenchido")
    void deveProcessarPixAssincronamente() throws InterruptedException, ExecutionException {
        UUID pedidoId = UUID.randomUUID();
        CobrancaRequestDTO request = new CobrancaRequestDTO(pedidoId, new BigDecimal("250.00"), "PIX", 1);
        
        Pagamento pagamentoInicial = new Pagamento(pedidoId, request.valor(), MetodoPagamento.PIX, 1);
        String chavePixEsperada = "00020101021126580014br.gov.bcb.pix...Mercado-Preso";

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoInicial);
        when(fabricaEstrategia.obterEstrategia(MetodoPagamento.PIX)).thenReturn(Optional.of(estrategiaPagamento));
        when(estrategiaPagamento.processar(any(Pagamento.class))).thenReturn(ResultadoPadrao.success(chavePixEsperada));

        CompletableFuture<ResultadoPadrao<CobrancaResponseDTO>> future = pagamentoService.processarCobranca(request);
        ResultadoPadrao<CobrancaResponseDTO> resultado = future.get();

        assertNotNull(resultado);
        assertTrue(resultado.isValid());
        CobrancaResponseDTO responseDTO = resultado.dado();
        
        assertFalse(responseDTO.processadoSincronamente(), "PIX não deve ser processado totalmente síncrono");
        assertEquals("PENDENTE", responseDTO.statusSugerido());
        assertEquals(chavePixEsperada, responseDTO.pixCopiaECola());
        assertNull(responseDTO.linhaDigitavel());

        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
        verify(estrategiaPagamento, times(1)).processar(any(Pagamento.class));
    }
}