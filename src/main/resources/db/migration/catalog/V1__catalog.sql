/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 29 de abr. de 2026
 */

CREATE SCHEMA IF NOT EXISTS catalog;

CREATE TABLE catalog.produto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE catalog.atributo (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE catalog.produto_atributo (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL,
    atributo_id UUID NOT NULL,

    FOREIGN KEY (produto_id) REFERENCES catalog.produto(id),
    FOREIGN KEY (atributo_id) REFERENCES catalog.atributo(id)
);

CREATE TABLE catalog.produto_variacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    produto_id UUID NOT NULL,
    sku VARCHAR(100) UNIQUE,
    preco NUMERIC(10,2) NOT NULL,
    estoque INT DEFAULT 0,

    FOREIGN KEY (produto_id) REFERENCES catalog.produto(id) ON DELETE CASCADE
);

CREATE TABLE catalog.variacao_opcao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    variacao_id UUID NOT NULL,
    atributo_id UUID NOT NULL,
    valor VARCHAR(100) NOT NULL,

    FOREIGN KEY (variacao_id) REFERENCES catalog.produto_variacao(id) ON DELETE CASCADE,
    FOREIGN KEY (atributo_id) REFERENCES catalog.atributo(id) ON DELETE CASCADE,

    UNIQUE (variacao_id, atributo_id)
);

CREATE TABLE catalog.imagem_variacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    variacao_id UUID NOT NULL,
    url_imagem TEXT NOT NULL,
    ordem INT DEFAULT 0,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (variacao_id) REFERENCES catalog.produto_variacao(id) ON DELETE CASCADE
);