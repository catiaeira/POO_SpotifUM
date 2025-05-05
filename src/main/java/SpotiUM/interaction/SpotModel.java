package SpotiUM.interaction;
import SpotiUM.Musica;
import SpotiUM.Utilizador;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*
Esta classe vai "segurar" os dados do programa, como os users, álbuns e playlists, etc. 
Métodos de remover ou adicionar componentes pertencem aqui.
 */
public class SpotModel implements Serializable {
    private Map<Integer, Musica> musicas; // álbuns na vdd, mudar dps
    private Map<Integer, Utilizador> utilizadores;

    private Integer musicaProximoID;
    private Integer utilizadorProximoID;

    public SpotModel() {
        this.musicas = new HashMap<>();
        this.utilizadores = new HashMap<>();
        musicaProximoID = 1;
        utilizadorProximoID = 1;
    }

    public int adicionarUtilizador (Utilizador user) {
        this.utilizadores.put(utilizadorProximoID, user);
        return utilizadorProximoID++;
    }

    public Utilizador getUtilizador (int id) {
        return this.utilizadores.get(id);
    }

    public void guardarEstado(String ficheiro) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(this);
        }
    }

    public static SpotModel carregarEstado(String ficheiro) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            return (SpotModel) ois.readObject();
        }
    }
}
