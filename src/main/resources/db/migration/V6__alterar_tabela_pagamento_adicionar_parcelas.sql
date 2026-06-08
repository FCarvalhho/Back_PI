-- Migração para o schema billing
-- Adiciona a coluna de parcelas e inclui restrições para garantir a consistência dos dados

ALTER TABLE billing.pagamento
    ADD COLUMN parcelas INT NOT NULL DEFAULT 1;

-- Comentário opcional para documentar os novos métodos aceitos na aplicação:
-- metodos: 'CREDIT_CARD', 'DEBIT_CARD', 'PIX', 'BOLETO'
-- status: 'PENDENTE', 'PAGO', 'FALHADO', 'CANCELADO'

-- Uma boa prática é adicionar uma constraint (validação no banco) para garantir que ninguém
-- insira uma quantidade inválida de parcelas (ex: número negativo ou zero)
ALTER TABLE billing.pagamento
    ADD CONSTRAINT chk_pagamento_parcelas CHECK (parcelas >= 1);