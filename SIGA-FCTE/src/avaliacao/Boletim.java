package avaliacao;

import disciplina.Turma;

/**
 * Representa o boletim de um aluno em uma turma, calculando média, frequência e situação.
 */
public class Boletim {

    /**
     * Enum para representar a situação final do aluno na disciplina.
     */
    public enum Situacao {
        APROVADO("Aprovado"),
        REPROVADO_POR_NOTA("Reprovado por Nota"),
        REPROVADO_POR_FALTA("Reprovado por Falta"),
        REPROVADO_POR_NOTA_E_FALTA("Reprovado por Nota e Falta"),
        DADOS_INCOMPLETOS("Dados Incompletos");

        private final String descricao;

        Situacao(String descricao) { this.descricao = descricao; }

        public String getDescricao() { return descricao; }
    }

    private Turma turma;
    private String matricula;
    private Nota nota;
    private Frequencia frequencia;
    private double mediaFinal;
    private double percentualFrequencia;

    public Boletim(Turma turma, String matricula, java.util.List<Nota> notas, java.util.List<Frequencia> frequencias) {
        /**
         * Construtor que busca os dados de nota e frequência e calcula os resultados.
         */
        this.turma = turma;
        this.matricula = matricula;
        this.nota = notas.stream().filter(n -> n.getTurma().equals(turma.getCodigo()) && n.getAluno().equals(matricula)).findFirst().orElse(null);
        this.frequencia = frequencias.stream().filter(f -> f.getTurma().equals(turma.getCodigo()) && f.getAluno().equals(matricula)).findFirst().orElse(null);
        this.mediaFinal = calcularMedia();
        this.percentualFrequencia = calcularPercentualFrequencia();
    }

    public double calcularMedia() {
        /**
         * Calcula a média final do aluno com base no tipo de avaliação da turma.
         */
        if (nota == null) {
            return 0.0;
        }

        if (turma.getTipoAvaliacao().equals("Simples")) {
            return (nota.getP1() + nota.getP2() + nota.getP3() + nota.getL() + nota.getS()) / 5;
        } else {
            return (nota.getP1() + nota.getP2() * 2 + nota.getP3() * 3 + nota.getL() + nota.getS()) / 8;
        }
    }

    public double calcularPercentualFrequencia() {
        /**
         * Calcula o percentual de frequência do aluno.
         */
        if (frequencia == null || turma.getDisciplina().getCargaHoraria() <= 0) {
            return 100.0;
        }
        double cargaHoraria = turma.getDisciplina().getCargaHoraria();
        return 100.0 - (frequencia.getFaltas() * 100.0 / cargaHoraria);
    }

    /**
     * @deprecated Use getSituacao() para obter um status detalhado.
     */
    @Deprecated
    public boolean isAprovado() {
        return getSituacao() == Situacao.APROVADO;
    }

    public Situacao getSituacao() {
        /**
         * Retorna a situação detalhada do aluno (aprovado, reprovado por nota, etc.).
         */
        if (nota == null || frequencia == null) {
            return Situacao.DADOS_INCOMPLETOS;
        }

        boolean notaSuficiente = mediaFinal >= 5.0;
        boolean frequenciaSuficiente = percentualFrequencia >= 75.0;

        if (notaSuficiente && frequenciaSuficiente) return Situacao.APROVADO;
        if (!notaSuficiente && !frequenciaSuficiente) return Situacao.REPROVADO_POR_NOTA_E_FALTA;
        if (!notaSuficiente) return Situacao.REPROVADO_POR_NOTA;
        return Situacao.REPROVADO_POR_FALTA;
    }

    /**
     * Getters
     */
    public double getMediaFinal() {
        return mediaFinal;
    }

    public double getPercentualFrequencia() {
        return percentualFrequencia;
    }
}