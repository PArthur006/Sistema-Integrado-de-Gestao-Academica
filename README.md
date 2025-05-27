# Sistema Acadêmico - FCTE

## Descrição do Projeto

Desenvolvimento de um sistema acadêmico para gerenciar alunos, disciplinas, professores, turmas, avaliações e frequência, utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

O enunciado do trabalho pode ser encontrado aqui:
- [Trabalho 1 - Sistema Acadêmico](https://github.com/lboaventura25/OO-T06_2025.1_UnB_FCTE/blob/main/trabalhos/ep1/README.md)

## Dados do Aluno

- **Nome completo:** Pedro Arthur Rodrigues Almeida
- **Matrícula:** 241012365
- **Curso:** Orientação a Objetos
- **Turma:** 06

---

## Instruções para Compilação e Execução

1. **Compilação:**  
   ```bash
   javac -d bin src/*/*.java src/Main.java

2. **Execução:**
   ```bash
   java -cp bin Main

3.  **Estrutura de Pastas:**  
      SIGA-FCTE/<br>
   ├── src/ <br>
   │   ├── aluno/          # Classes de Aluno (Normal/Especial)<br>
   │   ├── disciplina/     # Disciplinas e Turmas<br>
   │   ├── avaliacao/      # Notas, Frequência e Relatórios<br>
   │   ├── menus/          # Interfaces de usuário<br>
   │   ├── persistencia/   # Armazenamento em arquivos<br>
   │   └── Main.java       # Ponto de entrada<br>
   ├── bin/                # Arquivos compilados<br>
   ├── dados/              # Arquivos .txt gerados<br>
      ├── alunos.txt<br>
      ├── disciplinas.txt<br>
      └── turmas.txt<br>
   

3. **Versão do JAVA utilizada:**  
   23.0.1

---

## Principais Funcionalidades Implementadas

- [x] Cadastro, listagem, matrícula e trancamento de alunos (Normais e Especiais)
- [x] Cadastro de disciplinas e criação de turmas (presenciais e remotas)
- [x] Matrícula de alunos em turmas, respeitando vagas e pré-requisitos
- [x] Lançamento de notas e controle de presença
- [x] Cálculo de média final e verificação de aprovação/reprovação
- [x] Relatórios de desempenho acadêmico por aluno, turma e disciplina
- [x] Persistência de dados em arquivos (.txt ou .csv)
- [x] Tratamento de duplicidade de matrículas
- [x] Uso de herança, polimorfismo e encapsulamento

---

## Contato

- **Email:** parthur.rodrigues06@gmail.com
- **LinkedIn:** https://www.linkedin.com/in/parthurrod06
