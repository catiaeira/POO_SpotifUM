package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    String nome;
    String artista; // necessário?
    ArrayList<Musica> musicas;

    public Album(String nome, String artista, ArrayList<Musica> musicas) {
        this.nome = nome;
        this.artista = artista;
        this.musicas = new ArrayList<>(musicas);
    }
    public Album (Album album) {
        this.nome = album.getNome();
        this.artista = album.getArtista();
        this.musicas = album.getMusicas();
    }

    public String getNome() { return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getArtista() {return artista;}
    public void setArtista(String artista) {this.artista = artista;}

    public ArrayList<Musica> getMusicas() {return new ArrayList<>(musicas);}
    public void setMusicas(ArrayList<Musica> musicas) {this.musicas = new ArrayList<>(musicas);}

    @Override
    public Album clone () {
        return new Album(this);
    }

    public void adicionarMusica(Musica musica) {this.musicas.add(musica);}
    public void removerMusica (Musica musica) {this.musicas.remove(musica);}

}
