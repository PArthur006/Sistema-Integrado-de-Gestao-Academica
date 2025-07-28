package persistencia;

import disciplina.Disciplina;
import disciplina.Turma;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import aluno.Aluno;

public class ArquivoDisciplina {
    private static final String ARQUIVO_DISCIPLINAS = "SIGA-FCTE/dados/disciplinas.txt";
    private static final String ARQUIVO_TURMAS = "SIGA-FCTE/dados/turmas.txt";

    public static List<Disciplina> carregarDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_DISCIPLINAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 3) {
                    Disciplina disciplina = new Disciplina(dados[0], dados[1], Integer.parseInt(dados[2]));
                    for (int i = 3; i < dados.length; i++) {
                        disciplina.addPreRequisito(dados[i]);
                    }
                    disciplinas.add(disciplina);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar disciplinas: " + e.getMessage());
        }
        return disciplinas;
    }

    public static List<Turma> carregarTurmas() {
        List<Turma> turmas = new ArrayList<>();
        List<Disciplina> disciplinas = carregarDisciplinas();
        List<Aluno> alunos = ArquivoAluno.carregarAlunos();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_TURMAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 8) {
                    Disciplina disciplina = disciplinas.stream()
                        .filter(d -> d.getCodigo().equals(dados[1]))
                        .findFirst()
                        .orElse(null);
                        
                    if (disciplina != null) {
                        Turma turma = new Turma(
                            dados[0], disciplina, dados[2], dados[3], dados[4], 
                            dados[5], Integer.parseInt(dados[6]), dados[7]);
                        
                        // Carrega matrículas
                        for (int i = 8; i < dados.length; i++) {
                            String matricula = dados[i];
                            alunos.stream()
                                .filter(a -> a.getMatricula().equals(matricula))
                                .findFirst()
                                .ifPresent(turma::matricularAluno);
                        }
                        
                        turmas.add(turma);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar turmas: " + e.getMessage());
        }
        return turmas;
    }

    public static void salvarDisciplinas(List<Disciplina> novasDisciplinas) {
        List<Disciplina> antigas = carregarDisciplinas();
        for (Disciplina nova : novasDisciplinas) {
            boolean existe = antigas.stream().anyMatch(d -> d.getCodigo().equals(nova.getCodigo()));
            if (!existe) {
                antigas.add(nova);
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_DISCIPLINAS))) {
            for (Disciplina disciplina : antigas) {
                StringBuilder sb = new StringBuilder();
                sb.append(disciplina.getNome()).append(";");
                sb.append(disciplina.getCodigo()).append(";");
                sb.append(disciplina.getCargaHoraria());
                for (String preReq : disciplina.getPreRequisitos()) {
                    sb.append(";").append(preReq);
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar disciplinas: " + e.getMessage());
        }
    }

    public static void salvarTurmas(List<Turma> turmas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_TURMAS))) {
            for (Turma turma : turmas) {
                StringBuilder sb = new StringBuilder();
                sb.append(turma.getCodigo()).append(";");
                sb.append(turma.getDisciplina().getCodigo()).append(";");
                sb.append(turma.getProfessor()).append(";");
                sb.append(turma.getSemestre()).append(";");
                sb.append(turma.getSala()).append(";");
                sb.append(turma.getHorario()).append(";");
                sb.append(turma.getCapacidade()).append(";");
                sb.append(turma.getTipoAvaliacao());
                
                // Salva matrículas
                for (Aluno aluno : turma.getAlunosMatriculados()) {
                    sb.append(";").append(aluno.getMatricula());
                }
                
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar turmas: " + e.getMessage());
        }
    }
}
