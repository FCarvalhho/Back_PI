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

CREATE TABLE iam.vendedor (
    id UUID PRIMARY KEY,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    nome_responsavel VARCHAR(255) NOT NULL,
    email_responsavel VARCHAR(255) NOT NULL,
    telefone_responsavel VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) UNIQUE NOT NULL,

    FOREIGN KEY (id) REFERENCES iam.usuario(id) ON DELETE CASCADE
);

CREATE TABLE iam.admin (
    id UUID PRIMARY KEY,
    matricula VARCHAR(255) UNIQUE NOT NULL,

    FOREIGN KEY (id) REFERENCES iam.usuario(id) ON DELETE CASCADE
);

CREATE TABLE iam.role (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE iam.usuario_role (
    usuario_id UUID,
    role_id INT,
    PRIMARY KEY (usuario_id, role_id),
    FOREIGN KEY (usuario_id) REFERENCES iam.usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES iam.role(id) ON DELETE CASCADE
);