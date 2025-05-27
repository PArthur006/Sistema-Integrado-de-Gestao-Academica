package avaliacao;

import disciplina.Turma;

public class Boletim {
    private Turma turma;
    private Nota nota;
    private Frequencia frequencia;
    private double mediaFinal;

    public Boletim(Turma turma, Nota nota, Frequencia frequencia) {
        this.turma = turma;
        this.nota = nota;
        this.frequencia = frequencia;
        this.mediaFinal = calcularMedia();
    }

    public double calcularMedia() {
        if (turma.getTipoAvaliacao().equals("Simples")) {
            return (nota.getP1() + nota.getP2() + nota.getP3() + nota.getL() + nota.getS()) / 5;
        } else {
            return (nota.getP1() + nota.getP2() * 2 + nota.getP3() * 3 + nota.getL() + nota.getS()) / 8;
        }
    }

    public boolean isAprovado() {
        return mediaFinal >= 5 && frequencia.calcularFrequencia() >= 75;
    }

    public double getMediaFinal() {
        return mediaFinal;
    }
}