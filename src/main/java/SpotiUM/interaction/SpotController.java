package SpotiUM.interaction;

import SpotiUM.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/*
Esta classe vai ser a responsável por chamar os métodos dos componentes do programa
(ex. gets, SpotiUM.Musica m = new SpotiUM.Musica()). Serve como ponte entre o Model e o Interact.
 */
public class SpotController {
    SpotModel modelo;

    public SpotController(SpotModel modelo) {
        this.modelo = modelo;
    }

    public void setModelo (SpotModel modelo) {
        this.modelo = modelo;
    }

    public boolean utilizadorExiste (int id) {
        return this.modelo.getUtilizador(id) != null;
    }
    public String getUtilizadorNome (int id) {
        Utilizador user = this.modelo.getUtilizador(id);
        if (user == null) return "";
        return user.getNome();
    }

    public int novoUser (String nome, String morada, String email, int planoI) {
        PlanoSubscricao plano = switch (planoI) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> throw new IllegalStateException("Valor inesperado: " + planoI);
        };
        Utilizador utilizador = new Utilizador(nome, email, morada, plano, 0);
        return this.modelo.adicionarUtilizador(utilizador);
    }

    public void userOuveMusica(int id) {
        System.out.println("tocar musica");
        // lookup musica pelo nome, se houver várias dá print e pede para especificar qual?
    }

    public void userCriaPlaylist (int id) {
        Utilizador u = this.modelo.getUtilizador(id);
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

    public boolean verificaAlbum (int id) {return this.modelo.getAlbum(id) != null;}

    public void adicionaMusica(Musica m, int albumId) { this.modelo.adicionaMusica(m, albumId);}

    // Persistência

    public boolean guardarEstado(String tipo, String ficheiro) {
        Object objetoAGuardar = switch (tipo) {
            case "utilizadores" -> this.modelo.getUtilizadores();
            case "albuns"       -> this.modelo.getAlbuns();
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
            case "utilizadores" -> this.modelo.setUtilizadores((Map<Integer, Utilizador>) obj);
            case "albuns" -> this.modelo.setAlbuns((Map<Integer, Album>) obj);
            case "total" -> this.setModelo((SpotModel) obj);
        }
        return true;
    }

}
