/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 29 de abr. de 2026
 */

CREATE SCHEMA IF NOT EXISTS billing;

CREATE TABLE billing.pagamento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pedido_id UUID NOT NULL,
    status VARCHAR(50),
    valor NUMERIC(10,2),
    metodo VARCHAR(50),
    pago_em TIMESTAMP
);

