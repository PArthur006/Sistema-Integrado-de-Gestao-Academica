package persistencia;

import avaliacao.Nota;
import avaliacao.Frequencia;
import java.io.*;
import java.util.*;

/**
 * Classe responsável pela persistência dos dados de Avaliações (Notas e Frequências).
 */
public class ArquivoAvaliacao {
    private static final String DIRETORIO_DADOS = "SIGA-FCTE/dados";
    private static final String ARQ_AVALIACOES = DIRETORIO_DADOS + "/avaliacoes.txt";
    private static final String ARQ_FREQUENCIAS = DIRETORIO_DADOS + "/frequencias.txt";

    public static List<Nota> lerAvaliacoes() {
        /**
         * Carrega a lista de notas do arquivo avaliacoes.txt.
         */
        List<Nota> avaliacoes = new ArrayList<>();
        new File(DIRETORIO_DADOS).mkdir();
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
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de avaliações: " + e.getMessage());
        }
        return avaliacoes;
    }

    public static void salvarAvaliacoes(List<Nota> novasAvaliacoes) {
        /**
         * Salva a lista de notas, mesclando com os registros existentes para permitir atualizações.
         */
        List<Nota> registrosAtuais = lerAvaliacoes();
        Map<String, Nota> mapaRegistros = new LinkedHashMap<>();
        for (Nota n : registrosAtuais) {
            mapaRegistros.put(n.getTurma() + ";" + n.getAluno(), n);
        }
        for (Nota n : novasAvaliacoes) {
            mapaRegistros.put(n.getTurma() + ";" + n.getAluno(), n);
        }
        List<Nota> listaFinal = new ArrayList<>(mapaRegistros.values());

        new File(DIRETORIO_DADOS).mkdir();
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_AVALIACOES))) {
            pw.println("# turma;aluno;nota1,nota2,...");
            for (Nota n : listaFinal) {
                pw.print(n.getTurma() + ";" + n.getAluno() + ";");
                double[] notas = n.getNotas();
                for (int i = 0; i < notas.length; i++) {
                    pw.print(notas[i]);
                    if (i < notas.length - 1) pw.print(",");
                }
                pw.println();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de avaliações: " + e.getMessage());
        }
    }

    public static List<Frequencia> lerFrequencias() {
        /**
         * Carrega a lista de frequências do arquivo frequencias.txt.
         */
        List<Frequencia> frequencias = new ArrayList<>();
        new File(DIRETORIO_DADOS).mkdir();
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
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de frequências: " + e.getMessage());
        }
        return frequencias;
    }

    public static void salvarFrequencias(List<Frequencia> novasFrequencias) {
        /**
         * Salva a lista de frequências, mesclando com os registros existentes.
         */
        List<Frequencia> registrosAtuais = lerFrequencias();
        Map<String, Frequencia> mapaRegistros = new LinkedHashMap<>();
        for (Frequencia f : registrosAtuais) {
            mapaRegistros.put(f.getTurma() + ";" + f.getAluno(), f);
        }
        for (Frequencia f : novasFrequencias) {
            mapaRegistros.put(f.getTurma() + ";" + f.getAluno(), f);
        }
        List<Frequencia> listaFinal = new ArrayList<>(mapaRegistros.values());

        new File(DIRETORIO_DADOS).mkdir();
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQ_FREQUENCIAS))) {
            pw.println("# turma;aluno;faltas");
            for (Frequencia f : listaFinal) {
                pw.println(f.getTurma() + ";" + f.getAluno() + ";" + f.getFaltas());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de frequências: " + e.getMessage());
        }
    }
}
