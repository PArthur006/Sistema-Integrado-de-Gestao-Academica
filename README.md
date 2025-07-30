# SIGA-FCTE: Sistema de Gestão Acadêmica

## 📝 Descrição

O **SIGA-FCTE** é um sistema de desktop para gerenciamento acadêmico, desenvolvido em Java com a biblioteca Swing para a interface gráfica. O projeto nasceu como uma aplicação de console para a disciplina de Orientação a Objetos e evoluiu para uma experiência de usuário totalmente gráfica e intuitiva.

O sistema permite o controle completo de alunos, disciplinas, turmas e avaliações, demonstrando a aplicação prática de Programação Orientada a Objetos, arquitetura de software e persistência de dados em arquivos.

## ✨ Funcionalidades Principais

-   **Interface Gráfica Completa:** Todas as operações são realizadas através de uma interface amigável, eliminando a necessidade de comandos de terminal.
-   **Gestão de Alunos:**
    -   Cadastro, edição e exclusão de alunos (Normais e Especiais).
    -   Busca dinâmica por nome, matrícula ou curso.
    -   Validação para impedir matrículas duplicadas.
-   **Gestão de Disciplinas e Turmas:**
    -   CRUD completo para Disciplinas e Turmas.
    -   Validação para impedir códigos duplicados.
    -   Exclusão em cascata: ao remover uma disciplina, suas turmas são removidas.
-   **Matrícula e Avaliações:**
    -   Matrícula e desmatrícula de alunos em turmas.
    -   Visualização de alunos matriculados por turma.
    -   Lançamento de notas e faltas.
    -   Geração de boletins individuais.
-   **Relatórios Detalhados:**
    -   Geração de relatórios por Turma, Disciplina ou Professor.
-   **Persistência de Dados:**
    -   Todas as informações são salvas em arquivos de texto no diretório `dados/`, garantindo que os dados não sejam perdidos ao fechar o sistema.

## 🚀 Como Executar

### Pré-requisitos

-   Java Development Kit (JDK) versão 17 ou superior.

### Passos para Compilação e Execução

1.  **Clone o repositório e acesse a pasta do projeto:**
    ```bash
    git clone https://github.com/PArthur006/Sistema-Integrado-de-Gestao-Academica
    cd Trabalho1-OO-2025_1
    ```

2.  **Compile o projeto:**
    O comando abaixo compilará todos os arquivos `.java` da pasta `src` e colocará os arquivos `.class` na pasta `bin`.

    *No Linux ou macOS:*
    ```bash
    javac -d SIGA-FCTE/bin -cp SIGA-FCTE/src $(find SIGA-FCTE/src -name "*.java")
    ```
    *No Windows (PowerShell):*
    ```powershell
    javac -d SIGA-FCTE/bin -cp SIGA-FCTE/src (Get-ChildItem -Recurse -Filter *.java SIGA-FCTE/src).FullName
    ```

3.  **Execute a aplicação:**
    Após a compilação, execute a classe principal a partir da raiz do projeto:
    ```bash
    java -cp SIGA-FCTE/bin ui.TelaPrincipal
    ```

A janela principal do sistema deverá ser exibida.

## Dados do Aluno

- **Nome completo:** Pedro Arthur Rodrigues Almeida
- **Matrícula:** 241012365

---

## Origem do Projeto

Este sistema foi desenvolvido para a disciplina de Orientação a Objetos (Turma 06). O enunciado original, que descreve a versão base (via console), pode ser encontrado aqui:
- [Trabalho 1 - Sistema Acadêmico](https://github.com/lboaventura25/OO-T06_2025.1_UnB_FCTE/blob/main/trabalhos/ep1/README.md)

---

## Contato

- **Email:** parthur.rodrigues06@gmail.com
- **LinkedIn:** https://www.linkedin.com/in/parthurrod06
