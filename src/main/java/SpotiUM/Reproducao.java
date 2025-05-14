package SpotiUM;

import java.io.Serializable;
import java.time.LocalDate;

public class Reproducao implements Serializable {

    private Musica musica;
    private LocalDate data;

    public Reproducao(Musica musica, LocalDate data) {
        this.musica = musica;
        this.data = data;
    }

    public Musica getMusica() {
        return musica;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Reproduziu a música: " + musica.getNome() + " em " + data.toString();
    }
}
