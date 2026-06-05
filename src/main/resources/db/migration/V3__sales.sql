/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 29 de abr. de 2026
 */

CREATE SCHEMA IF NOT EXISTS sales;

CREATE TABLE sales.pedido (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cliente_id UUID NOT NULL,
    status VARCHAR(50),
    valor_total NUMERIC(10,2),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sales.item_pedido (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pedido_id UUID NOT NULL,
    variacao_id UUID NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario NUMERIC(10,2) NOT NULL,

    FOREIGN KEY (pedido_id) REFERENCES sales.pedido(id)
);

CREATE TABLE sales.carrinho (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cliente_id UUID UNIQUE NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sales.item_carrinho (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    carrinho_id UUID NOT NULL,
    variacao_id UUID NOT NULL,
    quantidade INT NOT NULL CHECK (quantidade > 0),

    FOREIGN KEY (carrinho_id) REFERENCES sales.carrinho(id) ON DELETE CASCADE,
    UNIQUE (carrinho_id, variacao_id)
);