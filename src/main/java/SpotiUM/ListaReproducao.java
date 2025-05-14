package SpotiUM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ListaReproducao {
    private ArrayList<Musica> listaReproducao;

    public ListaReproducao(ArrayList<Musica> musicas, Utilizador user) {
        this.listaReproducao = new ArrayList<>(musicas);

        if (user.getPlanoSubscricao() instanceof PlanoFree) {
            Collections.shuffle(listaReproducao);
        }
    }

    public ArrayList<Musica> getLista(){
        return new ArrayList<Musica>(listaReproducao);
    }
}
