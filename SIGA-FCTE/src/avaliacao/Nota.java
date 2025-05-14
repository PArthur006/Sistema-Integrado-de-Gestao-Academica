package avaliacao;

public class Nota {
    private double p1;
    private double p2;
    private double p3;
    private double listas;
    private double seminario;

    public Nota(double p1, double p2, double p3, double listas, double seminario){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.listas = listas;
        this.seminario = seminario;
    }

    public double calcularMediaSimples(){
        return (p1 + p2 + p3 + listas + seminario) / 5;
    }

    public double calcularMediaPonderada(){
        return (p1 + p2*2 + p3*3 + listas + seminario) / 8;
    }
}
