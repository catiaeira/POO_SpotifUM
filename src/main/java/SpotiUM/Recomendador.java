package SpotiUM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recomendador {
    private Map<String, Integer> generosMaisOuvidos;
    private Map<String, Integer> artistasMaisOuvidos;
    private List<Musica> musicasOrdenadas;
    private int ultimaMusicaIndex;

    private Recomendador() {
        generosMaisOuvidos = new HashMap<>();
        artistasMaisOuvidos = new HashMap<>();
        musicasOrdenadas = new ArrayList<>();
    }

    public static Playlist recomenda (Utilizador user, List <Musica> musicas, boolean isExplicita, int duracaoMaxima) {
        Recomendador r = user.getRecomendador();
        int indexAnterior = 0;
        if (r != null) {
            indexAnterior = r.ultimaMusicaIndex;
            r.populaDados(user, r.ultimaMusicaIndex);
        }
        else {
            r = new Recomendador();
            r.populaDados(user, 0);
        }
        // verifica se houve adições no historico
        if (indexAnterior != r.ultimaMusicaIndex) r.pontuaMusicas(musicas);

        List <Musica> ordenadas = r.musicasOrdenadas;
        if (isExplicita) ordenadas = r.musicasOrdenadas.stream().filter(m -> m instanceof MusicaExplicita).toList();

        List <Musica> topMusicas = new ArrayList<>();
        if (duracaoMaxima == -1) topMusicas = ordenadas.stream().limit(20).toList();
        else {
            int duracao = 0;
            for (Musica m : ordenadas) {
                if (duracao + m.getDuracao() > duracaoMaxima*60) continue;
                // continua a percorrer até encontrar uma música mais pequena para ter o máximo nº de músicas possível
                topMusicas.add(m);
                duracao += m.getDuracao();
            }
        }
        user.setRecomendador(r);
        String playlistNome = "Recomendada";
        if (isExplicita) playlistNome = playlistNome.concat(" explícita");
        if (duracaoMaxima != -1) playlistNome = playlistNome.concat(" com " + duracaoMaxima + " minutos");
        return new Playlist(playlistNome, Utilizador.SISTEMA, topMusicas, false);
    }

    private void populaDados(Utilizador user, int index) {
        List<Reproducao> historico = user.getHistorico();

        for (int i = index; i < historico.size(); i++) {
            Musica musica = historico.get(i).getMusica();
            adicionaOcorrencia(generosMaisOuvidos, musica.getGenero());
            adicionaOcorrencia(artistasMaisOuvidos, musica.getInterprete());
        }
        this.ultimaMusicaIndex = historico.size();
    }

    private void adicionaOcorrencia (Map<String, Integer> map, String string) {
        Integer ocorrencias = map.get(string.toLowerCase());
        if (ocorrencias != null) {
            map.replace(string, ocorrencias + 1);
        }
        else map.put(string, 1);
    }

    private void pontuaMusicas (List <Musica> musicas) {
        this.musicasOrdenadas.clear();
        HashMap<Musica, Integer> musicasPorPontos = new HashMap<>();
        for (Musica m : musicas) {
            String genero = m.getGenero();
            String artista = m.getInterprete();
            int pontos = this.artistasMaisOuvidos.getOrDefault(artista,0)
                       + this.generosMaisOuvidos.getOrDefault(genero, 0);
            musicasPorPontos.put(m, pontos);
        }
        this.musicasOrdenadas = musicasPorPontos.entrySet().stream().
                sorted(Map.Entry.<Musica, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey).toList();
    }
}
