package SpotiUM.interaction;

import SpotiUM.*;

import java.io.IOException;

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
        Utilizador utilizador = new Utilizador(nome, email, morada, plano);
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

    public void guardarEstado(String ficheiro) throws IOException {
        this.modelo.guardarEstado(ficheiro);
    }


}
