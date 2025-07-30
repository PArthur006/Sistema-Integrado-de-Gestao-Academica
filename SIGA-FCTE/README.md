# SIGA-FCTE: Sistema de Gestão Acadêmica

## 📝 Descrição

O **SIGA-FCTE** é um sistema de desktop para gerenciamento acadêmico, desenvolvido em Java com a biblioteca Swing para a interface gráfica. Originalmente um projeto para a disciplina de Orientação a Objetos, a aplicação evoluiu de uma interface de console para uma experiência de usuário totalmente gráfica e intuitiva.

O sistema permite o controle completo de alunos, disciplinas, turmas e avaliações, demonstrando conceitos de Programação Orientada a Objetos, arquitetura de software e persistência de dados em arquivos.

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

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    cd SIGA-FCTE
    ```

2.  **Crie o diretório de saída (se não existir):**
    ```bash
    mkdir bin
    ```

3.  **Compile o projeto:**
    Navegue até o diretório raiz do projeto e execute o comando abaixo. Ele compilará todos os arquivos `.java` da pasta `src` e colocará os arquivos `.class` na pasta `bin`.

    *No Linux ou macOS:*
    ```bash
    javac -d bin -cp src $(find src -name "*.java")
    ```
    *No Windows (PowerShell):*
    ```powershell
    javac -d bin -cp src (Get-ChildItem -Recurse -Filter *.java src).FullName
    ```

4.  **Execute a aplicação:**
    Após a compilação, execute a classe principal:
    ```bash
    java -cp bin Main
    ```

A janela principal do sistema deverá ser exibida.

## 📂 Estrutura do Projeto

O código-fonte está organizado nos seguintes pacotes dentro de `src/`:

-   `aluno/`: Contém as classes que modelam os Alunos (`Aluno`, `AlunoNormal`, `AlunoEspecial`).
-   `disciplina/`: Modela as Disciplinas e Turmas (`Disciplina`, `Turma`).
-   `avaliacao/`: Classes responsáveis pela lógica de avaliações, como `Nota`, `Frequencia` e `Boletim`.
-   `persistencia/`: Classes responsáveis por ler e escrever os dados da aplicação nos arquivos de texto.
-   `ui/`: Contém todas as classes da interface gráfica (GUI) construídas com Java Swing.
-   `Main.java`: Ponto de entrada da aplicação, responsável por iniciar a interface gráfica.

O diretório `dados/` é criado automaticamente na raiz do projeto para armazenar os arquivos de dados (`alunos.txt`, `disciplinas.txt`, `turmas.txt`, etc.).