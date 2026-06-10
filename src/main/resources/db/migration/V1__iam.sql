/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Cansei2
 * Created: 29 de abr. de 2026
 */

CREATE SCHEMA IF NOT EXISTS iam;

CREATE TABLE iam.usuario (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha TEXT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE iam.cliente (
    id UUID PRIMARY KEY,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(255) UNIQUE NOT NULL,
    
    FOREIGN KEY (id) REFERENCES iam.usuario(id) ON DELETE CASCADE
);

CREATE TABLE iam.funcionario (
    id UUID PRIMARY KEY,
    matricula VARCHAR(255) UNIQUE NOT NULL,

    FOREIGN KEY (id) REFERENCES iam.usuario(id) ON DELETE CASCADE
);

CREATE TABLE iam.usuario_role (
    usuario_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,

    PRIMARY KEY (usuario_id, role),
    FOREIGN KEY (usuario_id) REFERENCES iam.usuario(id) ON DELETE CASCADE
);