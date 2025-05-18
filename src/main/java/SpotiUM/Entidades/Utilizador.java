package SpotiUM.Entidades;

import SpotiUM.Biblioteca;
import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Planos.PlanoPremiumTop;
import SpotiUM.Entidades.Planos.PlanoSubscricao;
import SpotiUM.Recomendador;
import SpotiUM.Reproducao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utilizador implements Serializable {
    private String nome; // a assumir que é username (único)
    private String email;
    private String morada;
    private PlanoSubscricao planoSubscricao;
    private transient Biblioteca biblioteca;
    private int pontos;
    private final transient List<Reproducao> historico;
    private transient Recomendador recomendador;

    public static final Utilizador SISTEMA = new Utilizador("SpotiUM","", "", null, 0);

    public Utilizador(){
        this.nome = "";
        this.email = "";
        this.morada = "";
        this.planoSubscricao = null;
        this.biblioteca = new Biblioteca();
        this.pontos = 0;
        this.historico = new ArrayList<>();
    }

    public Utilizador(String nome, String email, String morada, PlanoSubscricao planoSubscricao, int pontos){
        this.nome = nome;
        this.email = email;
        this.morada = morada;
        this.planoSubscricao = planoSubscricao;
        this.biblioteca = new Biblioteca();
        this.pontos = pontos;
        this.historico = new ArrayList<>();

        if (planoSubscricao instanceof PlanoPremiumTop) {
            this.pontos = 100; //os subscritores do plano premium top recebem 100 pontos extra, manter assim se começar sempre em 0, se não modificar tbm
        }
    }

    public Utilizador(Utilizador u){
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.morada = u.getMorada();
        this.planoSubscricao = u.getPlanoSubscricao();
        this.biblioteca = u.getBiblioteca();
        this.pontos = u.getPontos();
        this.historico = u.getHistorico();
    }

    public void ouvirMusica(Musica musica){
        int pontosGanhos = planoSubscricao.calcularPontos(this, musica);
        this.pontos += pontosGanhos;
        registarReproducao(musica);
        // System.out.println(nome + " ouviu \"" + musica.getNome() + "\" e ganhou " + pontosGanhos + " pontos. Total: " + pontos);
    }

    public void registarReproducao(Musica musica) {
        this.historico.add(new Reproducao(musica, LocalDateTime.now()));
    }

    public List<Reproducao> getHistorico() {
        if (historico == null) return new ArrayList<>();
        return new ArrayList<>(historico);
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
    public Biblioteca getBiblioteca() {
        if (biblioteca == null) return new Biblioteca();
        return biblioteca;
    }
    public int getPontos(){
        return pontos;
    }
    public Recomendador getRecomendador() {return recomendador;}

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
    public void setBiblioteca (Biblioteca biblioteca) {
        this.biblioteca = biblioteca.clone();
    }
    public void setPontos(int pontos){
        this.pontos = pontos;
    }
    public void setRecomendador (Recomendador r) {this.recomendador = r;}

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

