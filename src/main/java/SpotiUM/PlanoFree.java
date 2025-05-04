package SpotiUM;

public class PlanoFree extends PlanoSubscricao {

    public PlanoFree() {
        super("Free");
    }

    @Override
    public int calcularPontos(Utilizador utilizador) {
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