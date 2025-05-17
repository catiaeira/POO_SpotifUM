package SpotiUM.Entidades.Planos;

import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Utilizador;

public class PlanoPremiumTop extends PlanoSubscricao {

    public PlanoPremiumTop() {
        super("Premium Top");
    }

    @Override
    public int calcularPontos(Utilizador utilizador, Musica musica) {
        int pontos = 10;
        if (utilizador.getHistorico().stream()
                .noneMatch(r -> r.getMusica().equals(musica))) pontos += (int) Math.round (utilizador.getPontos() * 0.025);
        return pontos;
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
