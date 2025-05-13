package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Biblioteca implements Serializable {
    private ArrayList<Album> albuns;
    private ArrayList<Playlist> playlists;

    public Biblioteca () {
        albuns = new ArrayList<>();
        playlists = new ArrayList<>();
    }
    public Biblioteca (Biblioteca b) {
        albuns = b.getAlbuns();
        playlists = b.getPlaylists();
    }

    public ArrayList<Album> getAlbuns() {
        return albuns;
    }
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setAlbuns (ArrayList<Album> albuns) {
        this.albuns = albuns;
    }
    public void setPlaylists (ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public void adicionaAlbum (Album album) {this.albuns.add(album);}
    public void adicionaPlaylist (Playlist playlist) {this.playlists.add(playlist);}

    public void removeAlbum (Album album) {this.albuns.remove(album);}
    public void removePlaylist (Playlist playlist) {this.playlists.remove(playlist);}


    @Override
    public Biblioteca clone () {
        return new Biblioteca(this);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("** Biblioteca **\n");
        sb.append("Álbuns:");
        this.albuns.forEach(albuns -> {
            if (sb.length() > 0) sb.append("\n");
            sb.append(albuns.getNome());
        });
        sb.append("\nPlaylists:");
        this.playlists.forEach(playlist -> {
            if (sb.length() > 0) sb.append("\n");
            sb.append(playlist.getNome());
        });
        return sb.toString();
    }
}
