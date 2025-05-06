package aluno;

public class AlunoNormal extends Aluno {
    public AlunoNormal(String nome, String matricula, String curso){
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(String disciplina){
        return true; // Preciso acrescentar os pré-requisitos!
    }

    public boolean recebeNotas(){
        return true;
    }
}
