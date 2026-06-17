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
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceCartaoTest {

    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private FabricaEstrategiaPagamento fabricaEstrategia;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private TransactionTemplate transactionTemplate;
    @Mock private EstrategiaPagamento estrategiaPagamento;

    @InjectMocks private PagamentoService pagamentoService;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Deve processar cobrança de cartão de crédito com sucesso de forma síncrona")
    void deveProcessarCartaoCreditoComSucesso() throws InterruptedException, ExecutionException {
        UUID pedidoId = UUID.randomUUID();
        CobrancaRequestDTO request = new CobrancaRequestDTO(pedidoId, new BigDecimal("150.00"), "CREDITO_CARD", 1);
        
        Pagamento pagamentoSalvo = new Pagamento(pedidoId, request.valor(), MetodoPagamento.CREDITO_CARD, 1);
        pagamentoSalvo.setId(UUID.randomUUID());

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoSalvo).thenReturn(pagamentoSalvo);
        
        when(fabricaEstrategia.obterEstrategia(MetodoPagamento.CREDITO_CARD)).thenReturn(Optional.of(estrategiaPagamento));
        when(estrategiaPagamento.processar(any(Pagamento.class))).thenReturn(ResultadoPadrao.success("APROVADO"));
        when(pagamentoRepository.findById(pagamentoSalvo.getId())).thenReturn(Optional.of(pagamentoSalvo));

        when(transactionTemplate.execute(any(TransactionCallback.class))).thenAnswer(invocation -> {
            TransactionCallback<?> callback = invocation.getArgument(0);
            return callback.doInTransaction(null);
        });

        CompletableFuture<ResultadoPadrao<CobrancaResponseDTO>> future = pagamentoService.processarCobranca(request);
        ResultadoPadrao<CobrancaResponseDTO> resultado = future.get();

        assertNotNull(resultado);
        assertTrue(resultado.isValid());
        CobrancaResponseDTO dto = resultado.dado();
        assertTrue(dto.processadoSincronamente());
        assertEquals("PAGO", dto.statusSugerido());
        assertEquals("Cartão autorizado.", dto.mensagem());

        verify(pagamentoRepository, times(2)).save(any(Pagamento.class));
        verify(estrategiaPagamento, times(1)).processar(any(Pagamento.class));
    }
}