package SpotiUM;

import SpotiUM.MVC.SpotModel;
import java.util.*;
import java.util.stream.Collectors;

public class Estatisticas {

    public static Musica getMusicaMaisReproduzida(SpotModel modelo) {
        Map<Musica, Long> contagem = new HashMap<>();

        for (Utilizador u : modelo.getUtilizadores().values()) {
            for (Reproducao r : u.getHistorico()) {
                Musica m = r.getMusica();
                contagem.put(m, contagem.getOrDefault(m, 0L) + 1);
            }
        }

        return contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static String getInterpreteMaisEscutado(SpotModel modelo) {
        Map<String, Integer> contagem = new HashMap<>();

        for (Utilizador u : modelo.getUtilizadores().values()) {
            for (Reproducao r : u.getHistorico()) {
                String artista = r.getMusica().getInterprete();
                contagem.put(artista, contagem.getOrDefault(artista, 0) + 1);
            }
        }

        return contagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static Map.Entry<String, Integer> getUtilizadorMaisMusicasOuvidas(SpotModel modelo) {
        Map<String, Utilizador> utilizadores = modelo.getUtilizadores();
        if (utilizadores.isEmpty()) return null;

        return utilizadores.entrySet().stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().getHistorico().size()))
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    public static Map.Entry<String, Integer> getUtilizadorComMaisPontos(SpotModel modelo) {
        return modelo.getUtilizadores().values().stream()
                .collect(Collectors.toMap(
                        Utilizador::getNome,
                        Utilizador::getPontos
                ))
                .entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
    }

    public static Map.Entry<String, Integer> getGeneroMaisReproduzido(SpotModel modelo) {
        Map<String, Integer> generoContagem = new HashMap<>();

        for (Utilizador u : modelo.getUtilizadores().values()) {
            for (Reproducao r : u.getHistorico()) {
                Musica m = r.getMusica();
                String genero = m.getGenero();
                int atuais = generoContagem.getOrDefault(genero, 0);
                generoContagem.put(genero, atuais + 1);
            }
        }

        if (generoContagem.isEmpty()) return null;

        return generoContagem.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
    }

    public static int getNumeroPlaylistsPublicas(SpotModel modelo) {
        int total = 0;
        for (List<Playlist> listas : modelo.getPlaylistsPorTitulo().values()) {
            for (Playlist p : listas) {
                if (p.isPublica()) {
                    total++;
                }
            }
        }
        return total;
    }

    public static Map.Entry<String, Integer> getUtilizadorComMaisPlaylists(SpotModel modelo) {
        Map<String, Integer> contagem = new HashMap<>();

        for (Utilizador u : modelo.getUtilizadores().values()) {
            contagem.put(u.getNome(), u.getBiblioteca().getPlaylists().size());
        }

        return contagem.entrySet().stream().max(Map.Entry.comparingByValue()).orElse(null);
    }

}
