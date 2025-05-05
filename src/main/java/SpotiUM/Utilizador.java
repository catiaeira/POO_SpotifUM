package SpotiUM;

import java.io.Serializable;

public class Utilizador implements Serializable {

    private String nome;
    private String email;
    private String morada;
    private PlanoSubscricao planoSubscricao;
    private int pontos;

    public Utilizador(String nome, String email, String morada, PlanoSubscricao planoSubscricao){
        this.nome = nome;
        this.email = email;
        this.morada = morada;
        this.planoSubscricao = planoSubscricao;
        this.pontos = 0; //deixar assim se for para o utilizador iniciar sempre com 0 pontos, talvez alterar para this.pontos = pontos

        if (planoSubscricao instanceof PlanoPremiumTop) {
            this.pontos = 100; //os subscritores do plano premium top recebem 100 pontos extra, manter assim se começar sempre em 0, se não modificar tbm
        }
    }

    public void ouvirMusica(Musica musica){
        musica.reproduzir();
        int pontosGanhos = planoSubscricao.calcularPontos(this);
        this.pontos += pontosGanhos;
        // System.out.println(nome + " ouviu \"" + musica.getNome() + "\" e ganhou " + pontosGanhos + " pontos. Total: " + pontos);
    }

    public String getNome(){
        return nome;
    }

    public String getEmail(){
        return email;
    }

    public String getMorada(){
        return morada;
    }

    public PlanoSubscricao getPlanoSubscricao(){
        return planoSubscricao;
    }

    public int getPontos(){
        return pontos;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setMorada(String morada){
        this.morada = morada;
    }

    public void setPlanoSubscricao(PlanoSubscricao planoSubscricao){
        this.planoSubscricao = planoSubscricao;
    }

    public void setPontos(int pontos){
        this.pontos = pontos;
    }

}

