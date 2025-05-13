package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private String nome;
    private Utilizador criador; //null se for playlist aleatória, criada por SpotiUM, senão o utilizador em si
    private ArrayList<Musica> musicas;

    public Playlist() {
        this.nome = "";
        this.criador = null;
        this.musicas = new ArrayList<>();
    }

    public Playlist(String nome, Utilizador criador, ArrayList<Musica> musicas) {
        this.nome = nome;
        this.criador = criador;
        this.musicas = new ArrayList<>(musicas);
    }

    public Playlist (Playlist playlist) {
        this.nome = playlist.getNome();
        this.criador = playlist.getCriador();
        this.musicas = playlist.getMusicas();
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

    @Override
    public String toString(){
        return "Playlist: " + nome + "\nCriada por: " + criador + "\n" + this.printTitulos();
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
