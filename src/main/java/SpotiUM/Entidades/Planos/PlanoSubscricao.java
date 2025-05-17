package SpotiUM.Entidades.Planos;

import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Utilizador;

import java.io.Serializable;

public abstract class PlanoSubscricao implements Serializable {

    private String nome;

    public PlanoSubscricao(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract int calcularPontos(Utilizador utilizador, Musica musica);

    public abstract boolean podeCriarPlaylist();

    public abstract boolean temListasFavoritas();

    public String toString() {
        return nome;
    }
}
