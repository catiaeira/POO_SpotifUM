package SpotiUM;

import SpotiUM.Entidades.Musica.Musica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe utils que guarda classes que contêm músicas pelo seu nome.
 * Funciona por agregação.
 * */
public class MapComNomeUtils {
    // Faz deep copy de um Map
    @SuppressWarnings("unchecked")
    public static <T extends ConjuntoDeMusicas> void deepCopy(Map<String, List<T>> novo, Map<String, List<T>> antigo) {
        novo.clear();

        antigo.forEach((key, valueList) -> {
            List<T> copiedList = valueList.stream()
                    .map(T::copy).map (o -> (T) o)
                    .collect(Collectors.toList());
            novo.put(key, copiedList);
        });
    }
    // retorna uma lista de todos os albuns/playlists com o nome dado
    public static <T extends ConjuntoDeMusicas> List<T> getGrupos(Map<String, List<T>> map, String name) {
        List<T> list = map.get(name.toLowerCase());
        return list != null ? new ArrayList<>(list) : new ArrayList<>();
    }

    // retorna uma lista de todos os albuns/playlists
    public static <T extends ConjuntoDeMusicas> List<T> getGrupo(Map<String, List<T>> map) {
        List<T> list = map.values().stream().flatMap(List :: stream).toList();
        return new ArrayList<>(list);
    }

    // adiciona um album/playlist
    @SuppressWarnings("unchecked")
    public static <T extends ConjuntoDeMusicas> void adicionaGrupoDeMusicas(Map<String, List<T>> map, T grupo) {
        map.computeIfAbsent(grupo.getNome().toLowerCase(), k -> new ArrayList<>()).add((T) grupo.copy());
    }

    // remove um album/playlist
    public static <T extends ConjuntoDeMusicas> void removeGrupoDeMusicas(Map<String, List<T>> map, T grupo) {
        String key = grupo.getNome().toLowerCase();
        List<T> gruposList = map.get(key);
        if (gruposList == null) return;

        gruposList.remove(grupo);
        if (gruposList.isEmpty()) map.remove(key);
    }
    // verifica se um determinado album/playlist já está guardado
    public static <T extends ConjuntoDeMusicas> boolean estaGuardado(Map<String, List<T>> map, T grupo) {
        List<T> gruposList = map.get(grupo.getNome().toLowerCase());
        return gruposList != null && gruposList.contains(grupo);
    }

    public static <T extends ConjuntoDeMusicas> String gruposToString(
            Map<String, List<T>> map) {
        return map.values().stream()
                .flatMap(List::stream)
                .map(T::toString)
                .collect(Collectors.joining("\n---\n"));
    }
}

