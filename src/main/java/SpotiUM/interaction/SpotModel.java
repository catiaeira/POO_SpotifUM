package SpotiUM.interaction;
import SpotiUM.Album;
import SpotiUM.Musica;
import SpotiUM.Utilizador;
import SpotiUM.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
Esta classe vai "segurar" os dados do programa, como os users, álbuns e playlists, etc. 
Métodos de remover ou adicionar componentes pertencem aqui.
 */
public class SpotModel implements Serializable {
    private Map<Integer, Album> albuns;
    private Map<Integer, Utilizador> utilizadores;

    private Integer albumProximoID;
    private Integer utilizadorProximoID;

    public SpotModel() {
        this.albuns = new HashMap<>();
        this.utilizadores = new HashMap<>();
        albumProximoID = 1;
        utilizadorProximoID = 1;
    }

    public Map <Integer, Utilizador> getUtilizadores () { return new HashMap<> (this.utilizadores);}
    public Map <Integer, Album> getAlbuns()             { return new HashMap<>(this.albuns);}

    public void setAlbuns (Map<Integer, Album> albuns) {
        this.albuns = new HashMap<>(albuns);
        this.albumProximoID = albuns.size() + 1;
    }
    public void setUtilizadores (Map < Integer, Utilizador> utilizadores) {
        this.utilizadores = new HashMap<>(utilizadores);
        this.utilizadorProximoID = utilizadores.size() + 1;
    }

    public int adicionarUtilizador (Utilizador user) {
        this.utilizadores.put(utilizadorProximoID, new Utilizador(user));
        return utilizadorProximoID++;
    }

    public Utilizador getUtilizador (int id) {
        return this.utilizadores.get(id);
    }
    public Album getAlbum (int id) {return this.albuns.get(id);}

    public int adicionaAlbum(Album album) {
        this.albuns.put(albumProximoID, album.clone());
        return albumProximoID++;
    }

    public void adicionaMusica (Musica musica, int id) {
        this.albuns.get(id).adicionarMusica(musica.clone());
    }
}
