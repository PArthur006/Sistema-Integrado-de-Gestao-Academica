# Checklist de Finalização - Sistema Acadêmico OO

## 1. Módulo de Persistência
- [x] Criar arquivo `avaliacoes.txt`.
- [x] Criar arquivo `frequencias.txt`.
- [x] Implementar métodos para LER e ESCREVER `avaliacoes.txt`.
- [x] Implementar métodos para LER e ESCREVER `frequencias.txt`.
- [x] Testar o ciclo completo: Iniciar -> Cadastrar -> Salvar -> Fechar -> Iniciar -> Verificar Dados.

## 2. Módulo de Avaliação e Relatórios
- [x] Implementar função para gerar Relatório por Turma.
- [x] Implementar função para gerar Relatório por Disciplina.
- [x] Implementar função para gerar Relatório por Professor.
- [x] Validar a exibição dos dois tipos de Boletim (simples e completo).

## 3. Regras de Negócio e Validações
- [x] Testar limite de matrícula para `AlunoEspecial` (máx. 2 disciplinas).
- [x] Testar bloqueio de lançamento de notas para `AlunoEspecial`.
- [x] Implementar e testar a lógica de Trancamento de Disciplina.
- [x] Implementar e testar a lógica de Trancamento de Semestre.
- [x] Testar cenário de reprovação por nota.
- [x] Testar cenário de reprovação por frequência.
- [x] Testar cenário de aprovação limite (nota 5.0, frequência 75%).

## 4. Finalização
- [x] Realizar uma revisão final do código (code review).
- [x] Atualizar o `README.md` com detalhes das funcionalidades finais.
- [x] Garantir que o projeto compila e executa sem erros com os comandos fornecidos.