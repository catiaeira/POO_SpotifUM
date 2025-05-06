package SpotiUM.interaction;
import SpotiUM.Album;
import SpotiUM.Musica;
import SpotiUM.Utilizador;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Esta classe vai "segurar" os dados do programa, como os users, álbuns e playlists, etc. 
Métodos de remover ou adicionar componentes pertencem aqui.
 */
public class SpotModel implements Serializable {
    private Map<Integer, Album> albunsPorID;
    private Map<String, Utilizador> utilizadores;
    private Map<String, List<Album>> albunsPorTitulo = new HashMap<>();

    private Integer albumProximoID;

    public SpotModel() {
        this.albunsPorID = new HashMap<>();
        this.utilizadores = new HashMap<>();
        this.albunsPorTitulo = new HashMap<>();
        albumProximoID = 1;
    }

    public Map <String, Utilizador> getUtilizadores()     { return new HashMap<> (this.utilizadores);}
    public Map <Integer, Album> getAlbunsPorID()          { return new HashMap<>(this.albunsPorID);}
    
    public void setAlbunsPorID(Map<Integer, Album> albunsPorID) {
        this.albunsPorID = new HashMap<>(albunsPorID);
        this.albumProximoID = albunsPorID.size() + 1;
        this.albunsPorTitulo.clear();
        for (Album a : albunsPorID.values()) {
            albunsPorTitulo.computeIfAbsent(a.getNome(), t -> new ArrayList<>()).add(a);
        }
    }

    public void setUtilizadores(Map <String, Utilizador> utilizadores) {
        this.utilizadores = new HashMap<>(utilizadores);
    }

    public void adicionarUtilizador (Utilizador user) {
        if (utilizadores.get(user.getNome()) != null) return;
        Utilizador utilizador = user.clone();
        this.utilizadores.put(utilizador.getNome(), utilizador);
    }
    public int adicionaAlbum(Album album) {
        Album albumCopia = album.clone();
        this.albunsPorID.put(albumProximoID, albumCopia);
        this.albunsPorTitulo.computeIfAbsent(albumCopia.getNome().toLowerCase(), t-> new ArrayList<>()).add(albumCopia);
        return albumProximoID++;
    }

    public Utilizador getUtilizador (String nome) {
        return this.utilizadores.get(nome);
    }

    public Album getAlbum (int id) {return this.albunsPorID.get(id);}

    public List <Album> getAlbum (String nome) {
        return this.albunsPorTitulo.get(nome.toLowerCase());
    }

    public void adicionaMusica (Musica musica, Album album) {
        album.adicionarMusica(musica.clone());
    }
}
