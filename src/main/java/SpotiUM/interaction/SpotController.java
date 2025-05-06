package SpotiUM.interaction;

import SpotiUM.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
Esta classe vai ser a responsável por chamar os métodos dos componentes do programa
(ex. gets, SpotiUM.Musica m = new SpotiUM.Musica()). Serve como ponte entre o Model e o Interact.
 */
public class SpotController {
    private SpotModel modelo;

    public SpotController(SpotModel modelo) {
        this.modelo = modelo;
    }

    public void setModelo (SpotModel modelo) {
        this.modelo = modelo;
    }

    public boolean utilizadorExiste (String id) {
        return this.modelo.getUtilizador(id) != null;
    }

    public void novoUser (String nome, String morada, String email, int planoI) {
        PlanoSubscricao plano = switch (planoI) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> throw new IllegalStateException("Valor inesperado: " + planoI);
        };
        Utilizador utilizador = new Utilizador(nome, email, morada, plano, 0);
        this.modelo.adicionarUtilizador(utilizador);
    }

    public boolean userOuveMusica(String user, String musicaNome, Album album) {
        Utilizador utilizador = this.modelo.getUtilizador(user);

        List<Musica> musicas = album.getMusicaPeloNome(musicaNome);
        if (musicas == null || musicas.isEmpty()) return false;
        Musica musica = musicas.size() == 1 ? musicas.getFirst() : SpotInteract.escolheDeUmaLista(musicas);
        utilizador.ouvirMusica(musica);
        return true;
    }

    public void userCriaPlaylist (String user) {
        Utilizador u = this.modelo.getUtilizador(user);
        if (u.getPlanoSubscricao().podeCriarPlaylist()) {
            System.out.println("A criar playlist");
        }
        else {
            System.out.println("Não pode criar a playlist");
        }
    }

    public int adicionaAlbum(String nome, String artista, ArrayList<Musica> musicas) {
        Album album = new Album(nome, artista, new ArrayList<>(musicas));
        return this.modelo.adicionaAlbum(album);
    }

    public boolean existeAlbum(String nome) {return this.modelo.getAlbum(nome) != null;}

    public void adicionaMusica(Musica m, String album) {
        List <Album> albuns = this.modelo.getAlbum(album);
        Album albumm = albuns.size() == 1 ? albuns.getFirst() : SpotInteract.escolheDeUmaLista(albuns);
        this.modelo.adicionaMusica(m, albumm);
    }

    public void adicionaMusica(Musica m, Album a) {
        if (m == null || a == null) throw new IllegalArgumentException("Música ou álbum inválidos.");
        this.modelo.adicionaMusica(m, a);
    }

    public List<Album> getAlbuns (String nome) {
        List<Album> albuns = this.modelo.getAlbum(nome);
        if (albuns == null || albuns.isEmpty()) {
            return null;
        }
        return albuns;
    }
    // Persistência

    public boolean guardarEstado(String tipo, String ficheiro) {
        Object objetoAGuardar = switch (tipo) {
            case "utilizadores" -> this.modelo.getUtilizadores();
            case "albuns"       -> this.modelo.getAlbunsPorID();
            case "total"        -> this.modelo;
            default             -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        };

        try {
            return Utils.guardarObjeto(objetoAGuardar, ficheiro);
        } catch (IOException e) {
            System.out.println("Erro ao guardar [" + tipo + "]: " + e.getMessage());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean carregarEstado(String tipo, String ficheiro) {
        Class<?> classe = switch (tipo) {
            case "utilizadores", "albuns"   -> Map.class;
            case "total"                    -> SpotModel.class;
            default                         -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        };
        Object obj;
        try {
            obj = Utils.carregarEstado(classe, ficheiro);
            if (obj == null) return false;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar estado: " + e.getMessage());
            return false;
        }

        switch  (tipo) {
            case "utilizadores" -> this.modelo.setUtilizadores((Map<String, Utilizador>) obj);
            case "albuns" -> this.modelo.setAlbunsPorID((Map<Integer, Album>) obj);
            case "total" -> this.setModelo((SpotModel) obj);
        }
        return true;
    }

}
