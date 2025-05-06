package SpotiUM;

import java.io.Serializable;

public class Utilizador implements Serializable {
    private String nome; // a assumir que é username (único)
    private String email;
    private String morada;
    private PlanoSubscricao planoSubscricao;
    private int pontos;

    public Utilizador(){
        this.nome = "";
        this.email = "";
        this.morada = "";
        this.planoSubscricao = null;
        this.pontos = 0;
    }

    public Utilizador(String nome, String email, String morada, PlanoSubscricao planoSubscricao, int pontos){
        this.nome = nome;
        this.email = email;
        this.morada = morada;
        this.planoSubscricao = planoSubscricao;
        this.pontos = pontos;

        if (planoSubscricao instanceof PlanoPremiumTop) {
            this.pontos = 100; //os subscritores do plano premium top recebem 100 pontos extra, manter assim se começar sempre em 0, se não modificar tbm
        }
    }

    public Utilizador(Utilizador u){
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.morada = u.getMorada();
        this.planoSubscricao = u.getPlanoSubscricao();
        this.pontos = u.getPontos();
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

    @Override
    public String toString() {
        return "Utilizador: " + nome + "\n" +
                "Email: " + email + "\n" +
                "Morada: " + morada + "\n" +
                "Plano de Subscrição: " + planoSubscricao + "\n" +
                "Pontos: " + pontos;
    }

    @Override
    public Utilizador clone() {
        return new Utilizador(this);
    }
}

