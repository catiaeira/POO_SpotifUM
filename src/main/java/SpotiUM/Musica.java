package SpotiUM;

import java.io.Serializable;
public class Musica implements Serializable {
    private String nome;
    private String interprete;
    private String editora;
    private String letra;
    private String genero;
    private String musica;
    private int duracaoSegs;
    private int nReproducoes;

    public Musica() {
        this.nome = "";
        this.interprete = "";
        this.editora = "";
        this.letra = "";
        this.genero = "";
        this.musica = "";
        this.duracaoSegs = 0;
        this.nReproducoes = 0;
    }

    public Musica(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes) {
        this.nome = nome;
        this.interprete = interprete;
        this.editora = editora;
        this.letra = letra;
        this.genero = genero;
        this.musica = musica;
        this.duracaoSegs = duracaoSegs;
        this.nReproducoes = nReproducoes;
    }

    public Musica(Musica m) {
        this.nome = m.getNome();
        this.interprete = m.getInterprete();
        this.editora = m.getEditora();
        this.letra = m.getLetra();
        this.genero = m.getGenero();
        this.musica = m.getMusica();
        this.duracaoSegs = m.getDuracao();
        this.nReproducoes = m.getnReproducoes();
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
    public String getGenero(){
        return genero;
    }
    public String getMusica(){
        return musica;
    }
    public int getDuracao(){
        return duracaoSegs;
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
    public void setGenero(String genero){
        this.genero = genero;
    }
    public void setMusica(String musica){
        this.musica = musica;
    }
    public void setDuracao(int duracao){
        this.duracaoSegs = duracao;
    }
    public void setnReproducoes(int nReproducoes){
        this.nReproducoes = nReproducoes;
    }

    @Override
    public String toString() {
        return "Música: " + nome + "\n" +
                "Intérprete: " + interprete + "\n" +
                "Editora: " + editora + "\n" +
                "Género: " + genero + "\n" +
                "Duração: " + duracaoSegs + "s\n" +
                "Reproduções: " + nReproducoes;
    }

    @Override
    public Musica clone() {
        return new Musica(this);
    }

    public boolean isExplicita() {
        return false;
    }

    public boolean isMultimedia() {
        return false;
    }

    public void reproduzir() {
        nReproducoes++;
        System.out.println(letra);
    }
}
