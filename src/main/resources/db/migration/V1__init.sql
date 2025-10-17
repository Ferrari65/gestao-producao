CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE produtos (
  id_produto     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  nome           text NOT NULL UNIQUE,
  unidade_venda  text,
  preco_venda    numeric(10,2) NOT NULL CHECK (preco_venda >= 0)
);

CREATE TABLE custos_producao (
  id_custo                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  id_produto              uuid NOT NULL REFERENCES produtos(id_produto) ON DELETE CASCADE,
  nome_insumo             text NOT NULL,
  custo_total_compra      numeric(12,2) NOT NULL CHECK (custo_total_compra >= 0),
  quantidade_total_compra numeric(14,6) NOT NULL CHECK (quantidade_total_compra > 0),
  unidade_base            text NOT NULL,
  consumo_por_unidade     numeric(14,6) NOT NULL CHECK (consumo_por_unidade >= 0),
  vigente_de              date DEFAULT CURRENT_DATE,
  vigente_ate             date
);
CREATE INDEX idx_custos_produto ON custos_producao (id_produto);

CREATE TABLE producao_diaria (
  id_lancamento  uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  id_produto     uuid NOT NULL REFERENCES produtos(id_produto) ON DELETE CASCADE,
  data           date NOT NULL,
  quantidade     integer NOT NULL CHECK (quantidade >= 0),
  observacao     text,
  CONSTRAINT unq_produto_data UNIQUE (id_produto, data)
);
CREATE INDEX idx_producao_data ON producao_diaria (data);
