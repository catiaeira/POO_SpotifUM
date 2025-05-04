package SpotiUM;

import java.io.Serializable;
public class Musica implements Serializable {

    private String nome;
    private String interprete;
    private String editora;
    private String letra;
    private String musica;
    private String genero;
    private int duracao;
    private int nReproducoes;

    public Musica(String nome, String interprete, String editora, String letra, String musica, String genero, int duracao) { //adicionar aqui nReproduções se quisermos que a música já comece com um certo número, manter assim se for para iniciar a 0
        this.nome = nome;
        this.interprete = interprete;
        this.editora = editora;
        this.letra = letra;
        this.musica = musica;
        this.genero = genero;
        this.duracao = duracao;
        this.nReproducoes = 0;
    }

    public void reproduzir() {
        nReproducoes++;
        System.out.println(letra);
    }


    public String getNome(){
        return nome;
    }

    public String getInterprete(){
        return interprete;
    }

    public String getEditora(){
        return editora;
    }

    public String getLetra(){
        return letra;
    }

    public String getMusica(){
        return musica;
    }

    public String getGenero(){
        return genero;
    }

    public int getDuracao(){
        return duracao;
    }

    public int getnReproducoes(){
        return nReproducoes;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setInterprete(String interprete){
        this.interprete = interprete;
    }

    public void setEditora(String editora){
        this.editora = editora;
    }

    public void setLetra(String letra){
        this.letra = letra;
    }

    public void setMusica(String musica){
        this.musica = musica;
    }

    public void setGenero(String genero){
        this.genero = genero;
    }

    public void setDuracao(int duracao){
        this.duracao = duracao;
    }

    public void setnReproducoes(int nReproducoes){
        this.nReproducoes = nReproducoes;
    }

    public String musicaToString() {
        return "Música: " + nome + "\n" +
                "Intérprete: " + interprete + "\n" +
                "Editora: " + editora + "\n" +
                "Género: " + genero + "\n" +
                "Duração: " + duracao + "s\n" +
                "Reproduções: " + nReproducoes;
    }

    public boolean isExplicita() {
        return false;
    }

}

