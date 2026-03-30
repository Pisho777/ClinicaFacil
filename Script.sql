-- =============================================================
-- ClinicaFácil — Script FUNCIONAL (MySQL 8+)
-- =============================================================

DROP DATABASE IF EXISTS clinicafacil;
CREATE DATABASE clinicafacil
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE clinicafacil;

-- =============================================================
-- TABELAS
-- =============================================================

CREATE TABLE especialidades (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uq_especialidade_nome (nome)
) ENGINE=InnoDB;

CREATE TABLE medicos (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    crm VARCHAR(20) NOT NULL,
    uf_crm CHAR(2) NOT NULL,
    id_especialidade INT UNSIGNED NOT NULL,
    telefone VARCHAR(20),
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    UNIQUE KEY uq_crm_uf (crm, uf_crm),
    CONSTRAINT fk_medico_especialidade
        FOREIGN KEY (id_especialidade)
        REFERENCES especialidades (id)
) ENGINE=InnoDB;

CREATE TABLE pacientes (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cpf CHAR(11) NOT NULL,
    data_nasc DATE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(150),
    endereco VARCHAR(255),
    responsavel VARCHAR(150),
    PRIMARY KEY (id),
    UNIQUE KEY uq_paciente_cpf (cpf)
) ENGINE=InnoDB;

CREATE TABLE consultas (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_paciente INT UNSIGNED NOT NULL,
    id_medico INT UNSIGNED NOT NULL,
    data_hora DATETIME NOT NULL,
    status ENUM('agendada','realizada','cancelada') DEFAULT 'agendada',
    motivo_cancelamento VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_medico_horario (id_medico, data_hora),
    CONSTRAINT fk_consulta_paciente
        FOREIGN KEY (id_paciente) REFERENCES pacientes(id),
    CONSTRAINT fk_consulta_medico
        FOREIGN KEY (id_medico) REFERENCES medicos(id)
) ENGINE=InnoDB;

CREATE TABLE prontuarios (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_consulta INT UNSIGNED NOT NULL,
    queixa TEXT NOT NULL,
    diagnostico TEXT,
    prescricao TEXT,
    data_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_prontuario_consulta (id_consulta),
    CONSTRAINT fk_prontuario_consulta
        FOREIGN KEY (id_consulta) REFERENCES consultas(id)
) ENGINE=InnoDB;

CREATE TABLE usuarios (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    login VARCHAR(80) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    perfil ENUM('admin','recepcao','medico') DEFAULT 'recepcao',
    ativo TINYINT(1) DEFAULT 1,
    PRIMARY KEY (id),
    UNIQUE KEY uq_usuario_login (login)
) ENGINE=InnoDB;

-- =============================================================
-- TRIGGERS (REGRAS DE NEGÓCIO)
-- =============================================================

DELIMITER $$

CREATE TRIGGER trg_medico_ativo
BEFORE INSERT ON consultas
FOR EACH ROW
BEGIN
    DECLARE v_ativo INT;
    SELECT ativo INTO v_ativo FROM medicos WHERE id = NEW.id_medico;

    IF v_ativo = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Médico inativo não pode receber consultas';
    END IF;
END$$

CREATE TRIGGER trg_cancelamento
BEFORE INSERT ON consultas
FOR EACH ROW
BEGIN
    IF NEW.status = 'cancelada' AND 
       (NEW.motivo_cancelamento IS NULL OR NEW.motivo_cancelamento = '') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Motivo de cancelamento obrigatório';
    END IF;
END$$

DELIMITER ;

-- =============================================================
-- ÍNDICES
-- =============================================================

CREATE INDEX idx_paciente_nome ON pacientes(nome);
CREATE INDEX idx_consulta_data ON consultas(data_hora);

-- =============================================================
-- DADOS DE TESTE
-- =============================================================

INSERT INTO especialidades (nome) VALUES
('Clínica Geral'),
('Cardiologia');

INSERT INTO medicos (nome, crm, uf_crm, id_especialidade, ativo) VALUES
('Dr João', '12345', 'MG', 1, 1),
('Dra Maria', '67890', 'MG', 2, 1);

INSERT INTO pacientes (nome, cpf, data_nasc) VALUES
('Carlos Silva', '12345678901', '1990-01-01'),
('Ana Souza', '98765432100', '2000-05-10');

-- Senha: 123  →  SHA-256: a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3
INSERT INTO usuarios (nome, login, senha_hash, perfil) VALUES
('Admin',       'admin',   'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'admin'),
('Recepcionista','recepcao','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'recepcao'),
('Médico Teste', 'medico',  'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'medico');

INSERT INTO consultas (id_paciente, id_medico, data_hora) VALUES
(1, 1, '2026-04-10 10:00:00');

INSERT INTO prontuarios (id_consulta, queixa) VALUES
(1, 'Dor de cabeça');