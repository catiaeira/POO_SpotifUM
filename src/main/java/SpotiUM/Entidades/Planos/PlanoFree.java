package SpotiUM.Entidades.Planos;

import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Utilizador;

public class PlanoFree extends PlanoSubscricao {

    public PlanoFree() {
        super("Free");
    }

    @Override
    public int calcularPontos(Utilizador utilizador, Musica musica) {
        return 5;  // o plano Free dá 5 pontos por musica
    }

    @Override
    public boolean podeCriarPlaylist() {
        return false;
    }

    @Override
    public boolean temListasFavoritas() {
        return false;
    }
}