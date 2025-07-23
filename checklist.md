# Checklist de Finalização - Sistema Acadêmico OO

## 1. Módulo de Persistência
- [ ] Criar arquivo `avaliacoes.txt`.
- [ ] Criar arquivo `frequencias.txt`.
- [ ] Implementar métodos para LER e ESCREVER `avaliacoes.txt`.
- [ ] Implementar métodos para LER e ESCREVER `frequencias.txt`.
- [ ] Testar o ciclo completo: Iniciar -> Cadastrar -> Salvar -> Fechar -> Iniciar -> Verificar Dados.

## 2. Módulo de Avaliação e Relatórios
- [ ] Implementar função para gerar Relatório por Turma.
- [ ] Implementar função para gerar Relatório por Disciplina.
- [ ] Implementar função para gerar Relatório por Professor.
- [ ] Validar a exibição dos dois tipos de Boletim (simples e completo).

## 3. Regras de Negócio e Validações
- [ ] Testar limite de matrícula para `AlunoEspecial` (máx. 2 disciplinas).
- [ ] Testar bloqueio de lançamento de notas para `AlunoEspecial`.
- [ ] Implementar e testar a lógica de Trancamento de Disciplina.
- [ ] Implementar e testar a lógica de Trancamento de Semestre.
- [ ] Testar cenário de reprovação por nota.
- [ ] Testar cenário de reprovação por frequência.
- [ ] Testar cenário de aprovação limite (nota 5.0, frequência 75%).

## 4. Finalização
- [ ] Realizar uma revisão final do código (code review).
- [ ] Atualizar o `README.md` com detalhes das funcionalidades finais.
- [ ] Garantir que o projeto compila e executa sem erros com os comandos fornecidos.