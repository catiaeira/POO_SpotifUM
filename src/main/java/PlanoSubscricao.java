public abstract class PlanoSubscricao {

    private String nome;

    public PlanoSubscricao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract int calcularPontos(Utilizador utilizador);

    public abstract boolean podeCriarPlaylist();

    public abstract boolean temListasFavoritas();

//    @Override //usei para testar mas estou indecisa de mantenho ou não
//    public String toString() {
//        return nome;
//    }
}
