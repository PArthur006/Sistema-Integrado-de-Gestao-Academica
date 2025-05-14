package avaliacao;

public class Frequencia {
    private int totalAulas;
    private int presencas;

    public Frequecia(int totalAulas){
        this.totalAulas = totalAulas;
        this.presencas = 0;
    }

    public void registrarPresenca(){
        if(presencas < totalAulas){
            presencas++;
        } else {
            System.out.println("Número máximo de presenças atingido.");
        }
    }

    public double calcularPercentual(){
        if(totalAulas == 0) return 0;
        return (presencas * 100.0) / totalAulas;
    }

    public boolean aprovadoPorFrequencia(){
        return calcularPercentual() >= 75.0;
    }
}
