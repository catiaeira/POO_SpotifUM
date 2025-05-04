package SpotiUM;

public class PlanoPremiumTop extends PlanoSubscricao {

    public PlanoPremiumTop() {
        super("Premium Top");
    }

    @Override
    public int calcularPontos(Utilizador utilizador) {
        return (int) (utilizador.getPontos() * 0.025); //os pontos dados por este plano são 2.5% dos pontos atuais
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
