package SpotiUM;

public class PlanoPremiumBase extends PlanoSubscricao {

    public PlanoPremiumBase() {
        super("Premium Base");
    }

    @Override
    public int calcularPontos(Utilizador utilizador) {
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
