package avaliacao;

public class Avaliacao {
    public static boolean aprovadoPorNota(double mediaFinal){
        return mediaFinal >= 5.0;
    }

    public static String resultadoFinal(double media, double frequencia){
        if(frequencia >= 75.0 && media >= 5.0){
            return "Aprovado";
        } else if(frequencia < 75.0 && media < 5.0){
            return "Reprovado por falta e nota";
        } else if(frequencia < 75.0){
            return "Reprovado por falta";
        } else {
            return "Reprovado por nota";
        }
    }
}
// 26250509017295