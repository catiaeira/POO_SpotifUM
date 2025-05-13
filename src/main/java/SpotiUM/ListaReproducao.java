package SpotiUM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ListaReproducao {
    private ArrayList<Musica> listaReproducao;
    private Iterator<Musica> iterador;

    public ListaReproducao(ArrayList<Musica> musicas, Utilizador utilizador) {
        this.listaReproducao = new ArrayList<>();

        if (utilizador.getPlanoSubscricao() instanceof PlanoFree) {
            listaReproducao.addAll(musicas);
            Collections.shuffle(listaReproducao);
        } else {
            listaReproducao.addAll(musicas);
        }

        this.iterador = listaReproducao.iterator();
    }

    public void tocamusicas() {
        iterador.forEachRemaining(m -> {
            try {
                m.reproduzir();
            } catch (Exception e) {
                System.err.println("Failed to play song: " + e.getMessage()); // can I??
            }
        });
    }


}
