CREATE TABLE IF NOT EXISTS usuario(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email  VARCHAR(100)  NOT NULL,
    senha VARCHAR(100) NOT NULL
);

