package SpotiUM;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reproducao implements Serializable {

    private Musica musica;
    private LocalDateTime data;

    public Reproducao(Musica musica, LocalDateTime data) {
        this.musica = musica;
        this.data = data;
    }

    public Musica getMusica() {
        return musica;
    }

    public LocalDateTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Reproduziu a música: " + musica.getNome() + " em " + data.toString();
    }
}
