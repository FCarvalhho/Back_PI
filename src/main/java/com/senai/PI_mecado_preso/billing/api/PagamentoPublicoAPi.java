package com.senai.PI_mecado_preso.billing.api;

import com.senai.PI_mecado_preso.shared.dto.ResultadoPadrao;

import java.math.BigDecimal;
import java.util.UUID;

public interface PagamentoPublicoAPi {

    ResultadoPadrao<Boolean> pagamentoPublico(UUID variacaoId, BigDecimal valor);
}
