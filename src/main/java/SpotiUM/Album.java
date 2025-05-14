package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable, ConjuntoDeMusicas {
    private String nome;
    private String artista;
    private ArrayList<Musica> musicas;

    public Album(String nome, String artista, ArrayList<Musica> musicas) {
        setNome   (nome);
        setArtista(artista);
        setMusicas(musicas);
    }
    public Album (Album album) {
        setNome   (album.getNome());
        setArtista(album.getArtista());
        setMusicas(album.getMusicas());
    }

    public String getArtista() {return artista;}
    public void setArtista(String artista) {this.artista = artista;}

    @Override
    public String getNomeGrupo() {
        return this.nome;
    }
    @Override
    public void setNomeGrupo(String nome) {
        this.nome = nome;
    }

    @Override
    public ArrayList<Musica> getMusicasList() {
        return this.musicas;
    }
    @Override
    public void setMusicasList(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }

    @Override
    public Album clone () {
        return new Album(this);
    }
    @Override
    public Album copy() {
        return clone();
    }
    @Override
    public String toString(){
        return "Álbum: " + nome + "\nCriado por: " + artista + "\nMúsicas:\n" + printTitulos();
    }

}
