package SpotiUM;

import SpotiUM.Entidades.Album;
import SpotiUM.Entidades.Playlist;

import java.util.HashMap;
import java.util.List;

import static SpotiUM.MapComNomeUtils.*;

public class Biblioteca {
    private final HashMap<String, List<Album>> albuns;
    private final HashMap<String,  List<Playlist>> playlists;

    public Biblioteca () {
        albuns = new HashMap<>();
        playlists = new HashMap<>();
    }
    public Biblioteca (Biblioteca b) {
        albuns = b.getAlbunsMap();
        playlists = b.getPlaylistsMap();
    }
    public HashMap<String, List<Album>> getAlbunsMap() {
        return new HashMap<>(this.albuns);
    }
    public HashMap<String, List<Playlist>> getPlaylistsMap() {
        return new HashMap<>(this.playlists);
    }

    public List<Album> getAlbuns() {
        return getGrupo(this.albuns);
    }
    public List<Playlist> getPlaylists() {
        return getGrupo(this.playlists);
    }

    public void adicionaAlbum(Album album) {
        adicionaGrupoDeMusicas(this.albuns, album);
    }
    public void adicionaPlaylist(Playlist playlist) {
        adicionaGrupoDeMusicas(this.playlists, playlist);
    }

    public void removePlaylists(Playlist playlist) {
        removeGrupoDeMusicas(this.playlists, playlist);
    }

    public void removeAlbuns(Album album) {
        removeGrupoDeMusicas(this.albuns, album);
    }

    public boolean estaNaBiblioteca(Album album) {
        return (estaGuardado(this.albuns, album));
    }

    public boolean estaNaBiblioteca (Playlist playlist) {
        return estaGuardado(this.playlists, playlist);
    }

    public String albunsToString() {
        return gruposToString(albuns);
    }

    public String playlistToString() {
        return  gruposToString(playlists);
    }

    public boolean estaVazia () {return this.albuns.isEmpty() && this.playlists.isEmpty();}

    @Override
    public Biblioteca clone () {
        return new Biblioteca(this);
    }

    @Override
    public String toString(){
        if (albuns.isEmpty() && playlists.isEmpty()) return "Biblioteca está vazia!";
        StringBuilder sb = new StringBuilder();

        sb.append("** Biblioteca **");
        if (!albuns.isEmpty()) {
            sb.append("\n- Álbuns:\n");
            sb.append(albunsToString());
        }
        if (!playlists.isEmpty()) {
            sb.append("\n- Playlists:\n");
            sb.append(playlistToString());
        }

        return sb.toString();
    }
}
