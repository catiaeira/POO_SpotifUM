package SpotiUM.MVC;
import SpotiUM.*;

import java.io.*;
import java.util.*;
import static SpotiUM.MapComNomeUtils.*;

/*
Esta classe vai "segurar" os dados do programa, como os users, álbuns e playlists, etc. 
Métodos de remover ou adicionar componentes pertencem aqui.
 */
public class SpotModel implements Serializable {
    private Map<String, Utilizador> utilizadores; // key string é sempre em lowercase
    private Map<String, List<Album>> albunsPorTitulo;
    private Map<String, List<Playlist>> playlistsPorTitulo;
    private Map<String, Musica> musicasPorNome;


    public SpotModel() {
        this.utilizadores = new HashMap<>();
        this.albunsPorTitulo = new HashMap<>();
        this.playlistsPorTitulo = new HashMap<>();
        this.musicasPorNome = new HashMap<>();
    }

    public Map <String, Utilizador> getUtilizadores()           { return new HashMap<> (this.utilizadores);}
    public Map <String, List<Album>> getAlbunsPorTitulo()       { return new HashMap<>(this.albunsPorTitulo);}
    public Map <String, List<Playlist>> getPlaylistsPorTitulo() { return new HashMap<>(this.playlistsPorTitulo);}
    public Map<String, Musica> getMusicasPorNome()              {return new HashMap<>(this.musicasPorNome);}

    public void setAlbunsPorTitulo(Map<String, List<Album>> albunsPorTitulo) {
        deepCopy(this.albunsPorTitulo, albunsPorTitulo);
    }
    public void setPlaylistsPorTitulo(Map<String, List<Playlist>> playlistsPorTitulo) {
        deepCopy(this.playlistsPorTitulo, playlistsPorTitulo);
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
        adicionaGrupoDeMusicas(albunsPorTitulo, album.clone());
        for (Musica m : album.clone().getMusicasList()) {
            musicasPorNome.putIfAbsent(m.getNome().toLowerCase(), m.clone());
        }
    }

    public void adicionarPlaylist (Playlist playlist) {
        adicionaGrupoDeMusicas(playlistsPorTitulo, playlist.clone());
    }

    public Utilizador getUtilizador (String nome) throws UtilizadorException {
        Utilizador user = this.utilizadores.get(nome.toLowerCase());
        if (user == null) throw new UtilizadorException("Utilizador " + nome + " não existe");
        return user;
    }
    public void removerUtilizador (Utilizador user) {
        removerPlaylist(user);
        this.utilizadores.remove(user.getNome());
    }

    public void removerPlaylist(Utilizador user) {
        for (List<Playlist> list : playlistsPorTitulo.values()) {
            list.removeIf(p -> p.getCriador().equals(user));
        }

        playlistsPorTitulo.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public boolean utilizadorExiste(String nome) {
        return this.utilizadores.containsKey(nome.toLowerCase());
    }

    public List <Album> getAlbum (String nome) throws AlbumException {
        List <Album> album = getGrupos(albunsPorTitulo, nome);
        if (album.isEmpty()) throw new AlbumException("Não existe nenhum álbum com esse nome");
        return album;
    }

    public List <Playlist> getPlaylist (String nome) throws PlaylistException {
        List <Playlist> playlist = this.playlistsPorTitulo.get(nome.toLowerCase());
        if (playlist == null || playlist.isEmpty()) throw new PlaylistException("Não existe nenhuma playlist com esse nome");
        return playlist;
    }

    public void adicionaMusica (Musica musica, Album album) {
        album.adicionarMusica(musica.clone());
        this.musicasPorNome.putIfAbsent(musica.clone().getNome().toLowerCase(), musica.clone());
    }

    public List<Musica> getMusicasPeloNome(String nome) throws MusicaException {
        List<Musica> musicas = getGrupo(this.albunsPorTitulo).stream()
                .flatMap(album -> album.getMusicasPeloNome(nome).stream())
                .toList();

        if (musicas.isEmpty()) throw new MusicaException("Não existe nenhuma música com esse nome");
        return musicas;
    }

}
