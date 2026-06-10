package com.senai.PI_mecado_preso.billing.internal.strategy;

import com.senai.PI_mecado_preso.billing.internal.entity.MetodoPagamento;
import com.senai.PI_mecado_preso.billing.internal.entity.Pagamento;
import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

public interface EstrategiaPagamento {
    ResultadoPadrao<String> processar(Pagamento pagamento);
    MetodoPagamento getMetodo();
}