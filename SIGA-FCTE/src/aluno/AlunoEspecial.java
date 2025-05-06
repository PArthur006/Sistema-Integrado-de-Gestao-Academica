package aluno;

public class AlunoEspecial extends Aluno {
    
    public AlunoEspecial(String nome, String matricula, String curso){
        super(nome, matricula, curso);
    }

    @Override
    public boolean podeMatricular(String disciplina){
        return disciplinasMatriculadas.size() < 2;
    }

    public boolean recebeNotas(){
        return false;
    }
}
