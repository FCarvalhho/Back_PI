/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 29 de abr. de 2026
 */

CREATE SCHEMA IF NOT EXISTS shipping;

CREATE TABLE shipping.endereco (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    usuario_id UUID NOT NULL,
    rua VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(100),
    cep VARCHAR(20)
);

CREATE TABLE shipping.entrega (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pedido_id UUID NOT NULL,
    endereco_id UUID NOT NULL,
    status VARCHAR(50),
    codigo_rastreio VARCHAR(100)
);