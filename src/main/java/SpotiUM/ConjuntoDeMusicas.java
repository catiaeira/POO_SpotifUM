package SpotiUM;

import SpotiUM.Entidades.Musica.Musica;

import java.util.ArrayList;
import java.util.List;

public interface ConjuntoDeMusicas {
    String getNomeGrupo();
    void setNomeGrupo(String nome);

    ArrayList<Musica> getMusicasList();
    void setMusicasList(ArrayList <Musica> musicas);

    default String getNome() { return getNomeGrupo();}
    default void setNome(String nome) {setNomeGrupo(nome);}

    default ArrayList<Musica> getMusicas() {
        return new ArrayList<>(getMusicasList());
    }

    default void setMusicas(List<Musica> musicas) {
        ArrayList <Musica> list = new ArrayList<>();
        musicas.forEach(m -> list.add(m.clone()));
        setMusicasList(list);
    }

    default List<Musica> getMusicasPeloNome(String nome) {
        return getMusicasList().stream()
                .filter(m-> m.getNome().equalsIgnoreCase(nome))
                .toList();
    }

    default boolean temMusica (Musica musica) {
        return getMusicasList().stream().anyMatch(m -> m.equals(musica));
    }
    default String printTitulos() {
        ArrayList<Musica> m = getMusicasList();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Musica musica : m) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(i + ". " + musica.getNome());
            i++;
        }
        return sb.toString();
    }

    default void adicionarMusica(Musica musica) {getMusicasList().add(musica);}
    default void removerMusica (Musica musica) {getMusicasList().remove(musica);}

    ConjuntoDeMusicas copy();
}
