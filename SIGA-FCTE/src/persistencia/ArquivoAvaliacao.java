package persistencia;

import avaliacao.Nota;
import avaliacao.Frequencia;
import java.io.*;
import java.util.*;

public class ArquivoAvaliacao {
    private static final String ARQ_AVALIACOES = "SIGA-FCTE/avaliacoes.txt";
    private static final String ARQ_FREQUENCIAS = "SIGA-FCTE/frequencias.txt";

    public static List<Nota> lerAvaliacoes() {
        List<Nota> avaliacoes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_AVALIACOES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("#") || linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                if (partes.length < 3) continue;
                String turma = partes[0];
                String aluno = partes[1];
                String[] notasStr = partes[2].split(",");
                double[] notas = Arrays.stream(notasStr).mapToDouble(Double::parseDouble).toArray();
                avaliacoes.add(new Nota(turma, aluno, notas));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return avaliacoes;
    }

    public static void salvarAvaliacoes(List<Nota> avaliacoes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_AVALIACOES))) {
            pw.println("# turma;aluno;nota1,nota2,...");
            for (Nota n : avaliacoes) {
                pw.print(n.getTurma() + ";" + n.getAluno() + ";");
                double[] notas = n.getNotas();
                for (int i = 0; i < notas.length; i++) {
                    pw.print(notas[i]);
                    if (i < notas.length - 1) pw.print(",");
                }
                pw.println();
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<Frequencia> lerFrequencias() {
        List<Frequencia> frequencias = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQ_FREQUENCIAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.startsWith("#") || linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                if (partes.length < 3) continue;
                String turma = partes[0];
                String aluno = partes[1];
                int faltas = Integer.parseInt(partes[2]);
                frequencias.add(new Frequencia(turma, aluno, faltas));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return frequencias;
    }

    public static void salvarFrequencias(List<Frequencia> frequencias) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_FREQUENCIAS))) {
            pw.println("# turma;aluno;faltas");
            for (Frequencia f : frequencias) {
                pw.println(f.getTurma() + ";" + f.getAluno() + ";" + f.getFaltas());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}
