package persistencia;

import aluno.Aluno;
import aluno.AlunoNormal;
import aluno.AlunoEspecial;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoAluno {
    private static final String ARQUIVO = "SIGA-FCTE/dados/alunos.txt";

    public static List<Aluno> carregarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 4) {
                    String tipo = dados[3];
                    Aluno aluno;
                    if (tipo.equals("Normal")) {
                        aluno = new AlunoNormal(dados[0], dados[1], dados[2]);
                    } else {
                        aluno = new AlunoEspecial(dados[0], dados[1], dados[2]);
                    }
                    alunos.add(aluno);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar alunos: " + e.getMessage());
        }
        return alunos;
    }

    public static void salvarAlunos(List<Aluno> alunos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Aluno aluno : alunos) {
                bw.write(aluno.getNome() + ";" + aluno.getMatricula() + ";" + aluno.getCurso() + ";" + aluno.getTipo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar alunos: " + e.getMessage());
        }
    }
}