package avaliacao;

import java.util.Map;

public class Boletim {
    public static void exibitBoletim(Map<String, Double> mediasPorSemestre, boolean mostrarDetalhes){
        System.out.println("Semestre: " + semestre + " - Média Final: " + mediasPorSemestre.get(semestre));
    }
}
