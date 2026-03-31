# 🏥 ClinicaFácil

## 📌 Status do Projeto

🚧 Em desenvolvimento

---

## 🧠 Descrição

O **ClinicaFácil** é um sistema desktop desenvolvido em Java com o objetivo de auxiliar no gerenciamento de uma clínica médica.
O sistema permite o cadastro, organização e controle de pacientes, médicos, consultas e prontuários, proporcionando uma experiência simples e eficiente para o usuário.

---

## 🎯 Objetivo do Software

O sistema foi criado para:

* Facilitar o cadastro de pacientes e médicos
* Organizar agendamentos de consultas
* Garantir controle de dados clínicos
* Melhorar a experiência de uso com interface intuitiva
* Simular um sistema real de clínica para fins educacionais

---

## 🛠️ Tecnologias Utilizadas

As tecnologias utilizadas no projeto foram baseadas no conteúdo do curso:

* **Java (JDK 17+)**
* **Java Swing (Interface gráfica)**
* **MySQL (Banco de dados)**
* **JDBC (Conexão com banco de dados)**
* **Git & GitHub (Versionamento)**

---

## 👨‍💻 Time de Desenvolvedores

* Pisho (Desenvolvedor principal)

---

## ⚙️ Funcionalidades do Sistema

### 👤 Pacientes

* Cadastro de pacientes
* Edição de dados
* Exclusão de registros
* Busca por nome
* Validação de CPF
* Identificação de menor de idade com responsável obrigatório

---

### 🩺 Médicos

* Cadastro de médicos
* Associação com especialidade
* Controle de status (ativo/inativo)

---

### 📅 Consultas

* Agendamento de consultas
* Controle de conflitos de horário
* Cancelamento com justificativa obrigatória
* Status da consulta (agendada, realizada, cancelada)

---

### 📄 Prontuários

* Registro de atendimento
* Armazenamento de diagnóstico e prescrição
* Vinculação com consulta

---

### 🔐 Usuários

* Sistema de login
* Perfis de acesso:

  * Admin
  * Recepção
  * Médico

---

## 🎨 Interface (UI/UX)

* Interface desenvolvida com **Java Swing**
* Sistema com:

  * Sidebar de navegação
  * Uso de **CardLayout** (uma única janela)
  * Efeitos de hover corrigidos
  * Navegação fluida entre telas
* Design padronizado com um Design System (DS)

---

## 🗃️ Estrutura do Projeto

```
ClinicaFacil/
│
├── src/
│   ├── model/
│   ├── dao/
│   ├── view/
│   ├── controller/
│   └── util/
│
├── database/
│   └── script.sql
│
└── README.md
```

---

## 🔄 Versionamento

O projeto utiliza Git para controle de versões, com práticas como:

* Criação de branches para melhorias e correções
* Commits descritivos
* Integração com repositório remoto no GitHub
* Uso de comandos:

  * `git add`
  * `git commit`
  * `git push`
  * `git pull`

---

## 🚀 Como Executar o Projeto

1. Clonar o repositório:

```
git clone <URL_DO_REPOSITORIO>
```

2. Importar no NetBeans ou outra IDE Java

3. Configurar o banco de dados:

* Executar o script SQL fornecido
* Ajustar credenciais no arquivo de conexão

4. Executar o projeto

---

## ⚠️ Observações

* O sistema ainda está em desenvolvimento
* Algumas funcionalidades podem ser aprimoradas
* Melhorias futuras incluem:

  * Melhor tratamento de erros
  * Interface mais responsiva
  * Implementação de relatórios

---

## 📚 Projeto Integrador

Este projeto foi desenvolvido como parte do **Projeto Integrador do curso de Desenvolvimento de Sistemas (SENAC)**, aplicando conceitos de:

* Programação orientada a objetos
* Banco de dados
* Interface gráfica
* Versionamento de código

---

## 📌 Autor

Desenvolvido por **Pisho**
