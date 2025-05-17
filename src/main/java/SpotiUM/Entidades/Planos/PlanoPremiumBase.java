package SpotiUM.Entidades.Planos;

import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Utilizador;

public class PlanoPremiumBase extends PlanoSubscricao {

    public PlanoPremiumBase() {
        super("Premium Base");
    }

    @Override
    public int calcularPontos(Utilizador utilizador, Musica musica) {
        return 10; //este plano dá 10 pontos quando se ouve uma musica
    }

    @Override
    public boolean podeCriarPlaylist() {
        return true;
    }

    @Override
    public boolean temListasFavoritas() {
        return true;
    }
}
