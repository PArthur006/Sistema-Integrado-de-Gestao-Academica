package disciplina;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma disciplina no sistema.
 */
public class Disciplina {
    private String nome;
    private String codigo;
    private int cargaHoraria;
    private List<String> preRequisitos;

    public Disciplina(String nome, String codigo, int cargaHoraria) {
        /**
         * Construtor
         */
        this.nome = nome;
        this.codigo = codigo;
        this.cargaHoraria = cargaHoraria;
        this.preRequisitos = new ArrayList<>();
    }

    public String getNome() { return nome; }

    public String getCodigo() { return codigo; }

    public int getCargaHoraria() { return cargaHoraria; }

    public List<String> getPreRequisitos() { return preRequisitos; }

    public void setNome(String nome) { this.nome = nome; }

    public void setCargaHoraria(int cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    public void addPreRequisito(String codigoDisciplina) {
        /**
         * Adiciona um pré-requisito à disciplina.
         */
        if (codigoDisciplina != null && !codigoDisciplina.trim().isEmpty() && !preRequisitos.contains(codigoDisciplina)) {
            preRequisitos.add(codigoDisciplina);
        }
    }
}