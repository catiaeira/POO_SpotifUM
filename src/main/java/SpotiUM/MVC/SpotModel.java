package SpotiUM.MVC;
import SpotiUM.*;

import java.io.*;
import java.util.*;

/*
Esta classe vai "segurar" os dados do programa, como os users, álbuns e playlists, etc. 
Métodos de remover ou adicionar componentes pertencem aqui.
 */
public class SpotModel implements Serializable {
    private Map<Integer, Album> albunsPorID;
    private Map<Integer, Playlist> playlistsPorID;
    private Map<String, Utilizador> utilizadores; // key string é sempre em lowercase
    private Map<String, List<Album>> albunsPorTitulo; // key string é sempre em lowercase
    private Map<String, List<Playlist>> playlistsPorTitulo;

    private Integer albumProximoID;
    private Integer playlistProximoID;

    public SpotModel() {
        this.albunsPorID = new HashMap<>();
        this.playlistsPorID = new HashMap<>();
        this.utilizadores = new HashMap<>();
        this.albunsPorTitulo = new HashMap<>();
        this.playlistsPorTitulo = new HashMap<>();
        albumProximoID = 1;
        playlistProximoID = 1;
    }

    public Map <String, Utilizador> getUtilizadores()     { return new HashMap<> (this.utilizadores);}
    public Map <Integer, Album> getAlbunsPorID()          { return new HashMap<>(this.albunsPorID);}
    public Map <Integer, Playlist> getPlaylistsPorID()    { return new HashMap<>(this.playlistsPorID);}

    public void setAlbunsPorID(Map<Integer, Album> albunsPorID) {
        this.albunsPorID = new HashMap<>(albunsPorID);
        this.albumProximoID = albunsPorID.size() + 1;
        this.albunsPorTitulo.clear();
        for (Album a : albunsPorID.values()) {
            albunsPorTitulo.computeIfAbsent(a.getNome(), t -> new ArrayList<>()).add(a);
        }
    }
    public void setPlaylistsPorID(Map<Integer, Playlist> playlistsPorID) {
        this.playlistsPorID = new HashMap<>(playlistsPorID);
        this.playlistProximoID = playlistsPorID.size() + 1;
        this.playlistsPorTitulo.clear();
        for (Playlist p : playlistsPorID.values()) {
            playlistsPorTitulo.computeIfAbsent(p.getNome(), t -> new ArrayList<>()).add(p);
        }
    }

    public void setUtilizadores(Map <String, Utilizador> utilizadores) {
        this.utilizadores = new HashMap<>(utilizadores);
    }

    public void adicionarUtilizador (Utilizador user)  {
        if (utilizadorExiste(user.getNome())) return;
        Utilizador utilizador = user.clone();
        this.utilizadores.put(utilizador.getNome().toLowerCase(), utilizador);
    }
    public void adicionaAlbum(Album album) {
        Album albumCopia = album.clone();
        this.albunsPorID.put(albumProximoID++, albumCopia);
        this.albunsPorTitulo.computeIfAbsent(albumCopia.getNome().toLowerCase(), t-> new ArrayList<>()).add(albumCopia);
    }

    public void adicionarPlaylist (Playlist playlist) {
        Playlist playlistCopia = playlist.clone();
        this.playlistsPorID.put(playlistProximoID++, playlistCopia);
        this.playlistsPorTitulo.computeIfAbsent(playlistCopia.getNome().toLowerCase(), t-> new ArrayList<>()).add(playlistCopia);
    }

    public Utilizador getUtilizador (String nome) throws UtilizadorException {
        Utilizador user = this.utilizadores.get(nome.toLowerCase());
        if (user == null) throw new UtilizadorException("Utilizador " + nome + " não existe");
        return user;
    }

    public boolean utilizadorExiste(String nome) {
        return this.utilizadores.containsKey(nome.toLowerCase());
    }

    public Album getAlbum (int id) {return this.albunsPorID.get(id);}

    public List <Album> getAlbum (String nome) throws AlbumException {
        List <Album> album = this.albunsPorTitulo.get(nome.toLowerCase());
        if (album.isEmpty()) throw new AlbumException("Não existe nenhum álbum com esse nome");
        return album;
    }

    public List <Playlist> getPlaylist (String nome) throws PlaylistException {
        List <Playlist> playlist = this.playlistsPorTitulo.get(nome.toLowerCase());
        if (playlist.isEmpty()) throw new PlaylistException("Não existe nenhuma playlist com esse nome");
        return playlist;
    }

    public void adicionaMusica (Musica musica, Album album) {
        album.adicionarMusica(musica.clone());
    }

    public List<Musica> getMusicasPeloNome(String nome) throws MusicaException {
        List<Musica> musicas = this.albunsPorID.values().stream()
                .flatMap(album -> album.getMusicasPeloNome(nome).stream())
                .toList();

        if (musicas.isEmpty()) throw new MusicaException("Não existe nenhuma música com esse nome");
        return musicas;
    }

}
