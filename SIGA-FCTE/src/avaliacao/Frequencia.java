package avaliacao;

public class Frequencia {
    private int totalAulas;
    private int presencas;

    public Frequencia(int totalAulas, int presencas) {
        this.totalAulas = totalAulas;
        this.presencas = presencas;
    }

    public double calcularFrequencia() {
        return (double) presencas / totalAulas * 100;
    }
}