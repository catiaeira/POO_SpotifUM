package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private String nome;
    private Utilizador criador; //null se for playlist aleatória, criada por SpotiUM, senão o utilizador em si
    private ArrayList<Musica> musicas;
    private int duracao;
    private boolean isPublica;

    public Playlist() {
        this.nome = "";
        this.criador = null;
        this.musicas = new ArrayList<>();
        this.duracao = 0;
        this.isPublica = false;
    }

    public Playlist(String nome, Utilizador criador, ArrayList<Musica> musicas, boolean isPublica) {
        this.nome = nome;
        this.criador = criador;
        this.musicas = new ArrayList<>(musicas);
        int dur = 0;
        for(Musica m : musicas){
            dur += m.getDuracao();
        }
        this.duracao = dur;
        this.isPublica = isPublica;
    }

    public Playlist (Playlist playlist) {
        this.nome = playlist.getNome();
        this.criador = playlist.getCriador();
        this.musicas = playlist.getMusicas();
        this.duracao = playlist.getDuracao();
        this.isPublica = playlist.isPublica();
    }
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Utilizador getCriador() {
        return criador;
    }
    public void setCriador(Utilizador criador) {
        this.criador = criador;
    }

    public ArrayList<Musica> getMusicas() {
        return new ArrayList<>(musicas);
    }
    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = new ArrayList<>(musicas);
    }

    public int getDuracao() {
        return duracao;
    }
    public void setDuracao(int dur) {
        this.duracao = dur;
    }

    public boolean isPublica() {
        return this.isPublica;
    }

    @Override
    public String toString(){
        return "Playlist: " + nome + "\nCriada por: " + criador.getNome() + "\n" + this.printTitulos();
    }

    public String printTitulos() {
        ArrayList<Musica> m = musicas;
        StringBuilder sb = new StringBuilder();
        m.forEach(musica -> {
            if (sb.length() > 0) sb.append("\n");
            sb.append(musica.getNome());
        });
        return sb.toString();
    }

    @Override
    public Playlist clone () {
        return new Playlist(this);
    }

    public void adicionarMusica(Musica musica) {
        this.musicas.add(musica);
    }
    public void removerMusica(Musica musica) {
        this.musicas.remove(musica);
    }
}
