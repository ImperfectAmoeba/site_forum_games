CREATE TABLE IF NOT EXISTS usuario(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    email  VARCHAR(100)  NOT NULL,
    senha VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS perfil (
    usuarioid UUID NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    CONSTRAINT fk_perfil_usuario FOREIGN KEY(usuarioid) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categoria (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);
                                    
CREATE TABLE IF NOT EXISTS post (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(200) NOT NULL,
    conteudo TEXT NOT NULL,
    categoria_id UUID REFERENCES categoria(id) ON DELETE CASCADE,
    autor_id UUID REFERENCES usuario(id) ON DELETE CASCADE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
