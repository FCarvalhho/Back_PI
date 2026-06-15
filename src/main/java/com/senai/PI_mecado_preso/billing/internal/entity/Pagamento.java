package com.senai.PI_mecado_preso.billing.internal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento", schema = "billing")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pedido_id", nullable = false)
    private UUID pedidoId;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private StatusPagamento status;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private MetodoPagamento metodo;

    @Column(name = "parcelas", nullable = false)
    private Integer parcelas;

    @Column(name = "pago_em")
    private LocalDateTime pagoEm;

    public Pagamento() {
    }

    public Pagamento(UUID pedidoId, BigDecimal valor, MetodoPagamento metodo, Integer parcelas) {
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.metodo = metodo;
        this.parcelas = parcelas != null ? parcelas : 1;
        this.status = StatusPagamento.PENDENTE; // Todo pagamento nasce pendente
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPedidoId() { return pedidoId; }
    public void setPedidoId(UUID pedidoId) { this.pedidoId = pedidoId; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public MetodoPagamento getMetodo() { return metodo; }
    public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }

    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }

    public LocalDateTime getPagoEm() { return pagoEm; }
    public void setPagoEm(LocalDateTime pagoEm) { this.pagoEm = pagoEm; }

    // Regra de negócio interna da Entidade para confirmar o pagamento
    public void confirmar() {
        this.status = StatusPagamento.PAGO;
        this.pagoEm = LocalDateTime.now();
    }

    public void falhar() {
        this.status = StatusPagamento.FALHADO;
    }

}