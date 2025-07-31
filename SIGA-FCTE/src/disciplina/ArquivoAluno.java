package persistencia;

import aluno.Aluno;
import aluno.AlunoNormal;
import aluno.AlunoEspecial;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoAluno {
    private static final String DIRETORIO_DADOS = "SIGA-FCTE/dados";
    private static final String ARQUIVO = DIRETORIO_DADOS + "/alunos.txt";

    public static List<Aluno> carregarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        new File(DIRETORIO_DADOS).mkdir();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    String tipo = dados[3];
                    Aluno aluno;
                    if (tipo.equals("Normal")) {
                        aluno = new AlunoNormal(dados[0], dados[1], dados[2]);
                    } else {
                        aluno = new AlunoEspecial(dados[0], dados[1], dados[2]);
                    }

                    if (dados.length > 4 && !dados[4].isEmpty()) {
                        String[] concluidas = dados[4].split(",");
                        for (String cod : concluidas) {
                            aluno.concluirDisciplina(cod);
                        }
                    }
                    alunos.add(aluno);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println("Erro ao carregar alunos: " + e.getMessage());
        }
        return alunos;
    }

    public static void salvarAlunos(List<Aluno> listaAtual) {
        new File(DIRETORIO_DADOS).mkdir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Aluno aluno : listaAtual) {
                StringBuilder sb = new StringBuilder();
                sb.append(aluno.getNome()).append(";")
                  .append(aluno.getMatricula()).append(";")
                  .append(aluno.getCurso()).append(";")
                  .append(aluno.getTipo()).append(";");
                sb.append(String.join(",", aluno.getDisciplinasConcluidas()));
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar alunos: " + e.getMessage());
        }
    }
}