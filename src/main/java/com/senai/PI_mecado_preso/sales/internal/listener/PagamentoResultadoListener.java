package com.senai.PI_mecado_preso.sales.internal.listener;

import com.senai.PI_mecado_preso.billing.api.PagamentoProcessadoEvent;
import com.senai.PI_mecado_preso.sales.internal.repository.PedidoRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
class PagamentoResultadoListener {

    private final PedidoRepository pedidoRepository;

    public PagamentoResultadoListener(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @ApplicationModuleListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void escutarResultadoPagamento(PagamentoProcessadoEvent event) {
        pedidoRepository.findById(event.pedidoId()).ifPresent(pedido -> {

            if ("APROVADO".equals(pedido.getStatus()) || "CANCELADO".equals(pedido.getStatus())) {
                return;
            }

            if ("PAGO".equals(event.statusSugerido())) {
                pedido.setStatus("APROVADO");
            } else if ("FALHADO".equals(event.statusSugerido())) {
                pedido.setStatus("CANCELADO");
            }

            pedidoRepository.save(pedido);
        });
    }
}
