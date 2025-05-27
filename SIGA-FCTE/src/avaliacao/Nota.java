package avaliacao;

public class Nota {
    private double p1;
    private double p2;
    private double p3;
    private double l;
    private double s;

    public Nota(double p1, double p2, double p3, double l, double s) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.l = l;
        this.s = s;
    }

    public double getP1() {
        return p1;
    }

    public double getP2() {
        return p2;
    }

    public double getP3() {
        return p3;
    }

    public double getL() {
        return l;
    }

    public double getS() {
        return s;
    }
}