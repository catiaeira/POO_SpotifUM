package SpotiUM.MVC;

import SpotiUM.*;
import SpotiUM.Entidades.*;
import SpotiUM.Entidades.Excecoes.AlbumException;
import SpotiUM.Entidades.Excecoes.MusicaException;
import SpotiUM.Entidades.Excecoes.PlaylistException;
import SpotiUM.Entidades.Excecoes.UtilizadorException;
import SpotiUM.Entidades.Musica.Musica;
import SpotiUM.Entidades.Musica.MusicaExplicita;
import SpotiUM.Entidades.Musica.MusicaExplicitaMultimedia;
import SpotiUM.Entidades.Musica.MusicaMultimedia;
import SpotiUM.Entidades.Planos.PlanoFree;
import SpotiUM.Entidades.Planos.PlanoPremiumBase;
import SpotiUM.Entidades.Planos.PlanoPremiumTop;
import SpotiUM.Entidades.Planos.PlanoSubscricao;

import java.io.IOException;
import java.util.*;
import java.util.List;

/*
Esta classe vai ser a responsável por chamar os métodos dos componentes do programa
(ex. gets, SpotiUM.Musica m = new SpotiUM.Musica()). Serve como ponte entre o Model e o Interact.
 */
public class SpotController {
    private SpotModel modelo;
    private final SpotView view;

    public SpotController() {
        this.modelo = new SpotModel();
        this.view = new SpotView();
    }

    public void run() {
        NewMenu mainMenu = new NewMenu(new String[]{
                "Criar Utilizador",
                "Login Utilizador",
                "Funções de Administrador"
        }, "Menu Inicial");

        mainMenu.setHandler(1, this::novoUtilizador);
        mainMenu.setHandler(2, this::menuUtilizador);
        mainMenu.setHandler(3, this::menuAdmin);

        mainMenu.run();
    }

    public void menuUtilizador() {
        String user = this.view.pedeNome("utilizador", true);
        Utilizador utilizador;
        try { utilizador = this.modelo.getUtilizador(user);
        } catch (UtilizadorException e) {
            this.view.mostraMensagemErro(e);
            return;
        }
        String[] opcoes = new String[] {
                "Pesquisar música",
                "Pesquisar álbum",
                "Pesquisar playlist",
                "Ver biblioteca",
                "Criar playlist",
                "Ver histórico",
                "Ver pontos",
                "Ver conta",
                "Apagar conta"
        };
        NewMenu menu = new NewMenu(opcoes, "Utilizador " + user);

        menu.setHandler(1, () -> musicaMenu(utilizador));
        menu.setHandler(2, () -> albumMenu(utilizador));
        menu.setHandler(3, () -> playlistMenu(utilizador));
        menu.setHandler(4, () -> bibliotecaMenu(utilizador));
        menu.setHandler(5, () -> menuCriaPlaylist(utilizador));
        menu.setHandler(6, () -> userVerHistorico(utilizador));
        menu.setHandler(7, () -> userVerPontos(utilizador));
        menu.setHandler(8, () -> this.view.printMensagem(utilizador.toString()));
        menu.setHandler(9, () -> userApagarConta(utilizador));

        for (int i = 0; i< opcoes.length; i++) {
            menu.setPreCondition(i+1, () -> this.modelo.utilizadorExiste(user));
        }
        menu.setPreCondition(4, () -> !utilizador.getBiblioteca().estaVazia());
        menu.setPreCondition(5, () -> this.modelo.utilizadorExiste(user) && utilizador.getPlanoSubscricao().podeCriarPlaylist());
        menu.setPreCondition(6, () -> !utilizador.getHistorico().isEmpty());
        menu.run();
    }

    public void menuAdmin() {
        NewMenu menu = new NewMenu(new String[]{
                "Novo album",
                "Importar dados",
                "Exportar dados",
                "Consultar Estatísticas"
        }, "Admin");

        menu.setHandler(1, this::adicionaAlbum);
        menu.setHandler(2, this::carregarEstado);
        menu.setHandler(3, this::guardarEstado);
        menu.setHandler(4, this::menuEstatisticas);
        menu.run();
    }

    public void musicaMenu (Utilizador utilizador) {
        Musica musica = obterMusicaValidaDoUserInput();
        if (musica == null) return;
        NewMenu menu = new NewMenu(new String[] {
                "Ouvir música",
                "Adicionar à playlist"
        }, musica.getNome());

        menu.setHandler(1, () -> {
            ArrayList <Musica> list = new ArrayList<>();
            list.add(musica);
            utilizadorOuveMusica(utilizador, list);
        });
        menu.setHandler(2, () -> adicionaMusicaPlaylist(musica, utilizador));

        menu.run();
    }

    public void albumMenu(Utilizador utilizador) {
        Album album = obterAlbumValidoDoUserInput();
        if (album == null) return;

        NewMenu menu = new NewMenu(new String[] {
                "Ouvir álbum",
                "Adicionar à biblioteca"
        }, album.getNome());

        menu.setHandler(1, () -> {
            if(utilizador.getPlanoSubscricao() instanceof PlanoFree){
                utilizadorOuveAlbum(utilizador, album, 1);
            } else {
                utilizadorEscolheModoAlbum(utilizador, album);
            }
        });
        menu.setHandler(2, () -> adicionaAlbumBiblioteca(utilizador, album));

        menu.run();
    }

    public void playlistMenu(Utilizador utilizador) {
        Playlist playlist = obterPlaylistValidaDoUserInput(utilizador);
        if (playlist == null){
            this.view.printMensagem("Playlist não existe");
            return;
        }

        NewMenu menu = new NewMenu(new String[] {
                "Ouvir playlist",
                "Adicionar à biblioteca",
                "Ver playlist"
        }, playlist.getNome());

        menu.setHandler(1, () -> {
            if(utilizador.getPlanoSubscricao() instanceof PlanoFree){
                utilizadorOuvePlaylist(utilizador, playlist, 1);
            } else {
                utilizadorEscolheModoPlaylist(utilizador, playlist);
            }
        });
        menu.setHandler(2, () -> adicionaPlaylistBiblioteca(utilizador, playlist));
        menu.setHandler(3, () -> this.view.printMensagem(playlist.toString()));

        menu.run();
    }


    public void bibliotecaMenu(Utilizador utilizador) {
        NewMenu menu = new NewMenu(new String[] {
                "Ver guardados na biblioteca",
                "Ouvir álbum",
                "Ouvir playlist",
                "Remover álbum",
                "Remover playlist"
        }, "Biblioteca");
        Biblioteca biblioteca = utilizador.getBiblioteca();
        menu.setHandler(1, () -> this.view.printMensagem(biblioteca.toString()));
        menu.setHandler(2, () -> {
            List <Album> albuns = biblioteca.getAlbuns();
            Album album = obterAlbumValidoDoUserInput (albuns);
            if (album == null) {
                this.view.printMensagem("Álbum não existe");
                return;
            }
            if(utilizador.getPlanoSubscricao() instanceof PlanoFree){
                utilizadorOuveAlbum(utilizador, album, 1);
            } else {
                utilizadorEscolheModoAlbum(utilizador, album);
            }
        });
        menu.setHandler(3, () -> {
            List<Playlist> playlists = biblioteca.getPlaylists();
            Playlist playlist = obterPlaylistValidaDoUserInput(playlists);
            if (playlist == null) {
                this.view.printMensagem("Playlist não existe");
                return;
            }
            if(utilizador.getPlanoSubscricao() instanceof PlanoFree){
                utilizadorOuvePlaylist(utilizador, playlist, 1);
            } else {
                utilizadorEscolheModoPlaylist(utilizador, playlist);
            }
        });
        menu.setHandler(4, () -> {
            List <Album> albuns = biblioteca.getAlbuns();
            Album album = obterAlbumValidoDoUserInput (albuns);
            biblioteca.removeAlbuns(album);
        });
        menu.setHandler(5, () -> {
            List<Playlist> playlists = biblioteca.getPlaylists();
            Playlist playlist = obterPlaylistValidaDoUserInput(playlists);
            biblioteca.removePlaylists(playlist);
        });

        menu.setPreCondition(2, () -> !biblioteca.getAlbuns()   .isEmpty());
        menu.setPreCondition(3, () -> !biblioteca.getPlaylists().isEmpty());
        menu.setPreCondition(4, () -> !biblioteca.getAlbuns()   .isEmpty());
        menu.setPreCondition(5, () -> !biblioteca.getPlaylists().isEmpty());

        menu.run();
    }

    public void novoUtilizador () {
        List <String> utilizadorDados = this.view.novoUtilizador(u -> !this.modelo.utilizadorExiste(u));
        String planoI = utilizadorDados.get(3);
        PlanoSubscricao plano = switch (Integer.parseInt (planoI)) {
            case 1 -> new PlanoFree();
            case 2 -> new PlanoPremiumBase();
            case 3 -> new PlanoPremiumTop();
            default -> throw new IllegalStateException("Valor inesperado: " + planoI);
        };
        Utilizador utilizador = new Utilizador(utilizadorDados.get(0), utilizadorDados.get(1), utilizadorDados.get(2), plano, 0);
        this.modelo.adicionarUtilizador(utilizador);
    }


    public Musica novaMusica() {
        List<String> dados = this.view.novaMusica();

        String nome = dados.get(0);
        String artista = dados.get(1);
        String editora = dados.get(2);
        String letra = dados.get(3);
        String genero = dados.get(4);
        String ficheiro = dados.get(5);
        int duracao = Integer.parseInt(dados.get(6));
        boolean explicita = Boolean.parseBoolean(dados.get(7));
        boolean multimedia = Boolean.parseBoolean(dados.get(8));
        String linkMultimedia = dados.get(9);

        if (explicita && multimedia) return new MusicaExplicitaMultimedia(nome, artista, editora, letra, genero, ficheiro, duracao, 0, linkMultimedia);
        else if (explicita) return new MusicaExplicita(nome, artista, editora, letra, genero, ficheiro, duracao, 0);
        else if (multimedia) return new MusicaMultimedia(nome, artista, editora, letra, genero, ficheiro, duracao, 0, linkMultimedia);
        else return new Musica (nome, artista, editora, letra, genero, ficheiro, duracao, 0);
    }

    public void adicionaAlbum() {
        List <String> albumDados = this.view.novoAlbum();

        int numeroMusicas = Integer.parseInt(albumDados.get(2));
        ArrayList<Musica> musicas = new ArrayList<>();
        for (int i = 0; i<numeroMusicas; i++) {
            musicas.add(novaMusica());
        }
        Album album = new Album(albumDados.get(0), albumDados.get(1), musicas);
        this.modelo.adicionaAlbum(album);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_ALBUM, true);
    }

    public void novaPlaylist (Utilizador utilizador) {
        List <String> playlistDados = this.view.novaPlaylist();

        int numeroMusicas = Integer.parseInt(playlistDados.get(2));
        ArrayList<Musica> musicas = new ArrayList<>();
        for (int i = 0; i<numeroMusicas; i++) {

            String nome = this.view.pedeNome("música " + (i+1), false);

            try {
                List <Musica> m = this.modelo.getMusicasPeloNome(nome);
                Musica musica = m.size() == 1 ? m.getFirst() : SpotView.escolheDeUmaLista(m);
                musicas.add(musica);
            } catch (MusicaException e) {
                this.view.mostraMensagemErro(e);
                return;
            }
        }
        boolean isPublica = playlistDados.get(1).equals("publica");

        Playlist playlist = new Playlist(playlistDados.getFirst(), utilizador, musicas, isPublica);

        this.modelo.adicionarPlaylist(playlist);
        utilizador.getBiblioteca().adicionaPlaylist(playlist);      // adiciona à biblioteca como default
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, true);
    }

    public void utilizadorEscolheModoAlbum (Utilizador user, Album album) {
        NewMenu menu = new NewMenu(new String[] {
                "Sim",
                "Não"
        }, "Ouvir em modo aleatório?");
        menu.setHandler(1, () -> utilizadorOuveAlbum(user, album,1));
        menu.setHandler(2, () -> utilizadorOuveAlbum(user, album,0));
        menu.runOnce();
    }

    public void utilizadorEscolheModoPlaylist (Utilizador user, Playlist playlist) {
        NewMenu menu = new NewMenu(new String[] {
                "Sim",
                "Não"
        }, "Ouvir em modo aleatório?");
        menu.setHandler(1, () -> utilizadorOuvePlaylist(user, playlist,1));
        menu.setHandler(2, () -> utilizadorOuvePlaylist(user, playlist,0));
        menu.runOnce();
    }

    public void utilizadorOuveAlbum (Utilizador user, Album album, int modo) {
        ArrayList <Musica> musicas = album.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, modo);
        utilizadorOuveMusica(user, lista.getLista());
    }

//    public void utilizadorOuveAlbum (Utilizador user, int modo) {
//        Album album = obterAlbumValidoDoUserInput();
//        ArrayList <Musica> musicas = album.getMusicas();
//        ListaReproducao lista = new ListaReproducao(musicas, modo);
//        utilizadorOuveMusica(user, lista.getLista());
//    }

    public void utilizadorOuvePlaylist (Utilizador user, Playlist playlist, int modo) {
        ArrayList <Musica> musicas = playlist.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, modo);
        utilizadorOuveMusica(user, lista.getLista());
    }

//    public void utilizadorOuvePlaylist (Utilizador user, int modo) {
//        Playlist playlist = obterPlaylistValidaDoUserInput(user);
//        if (playlist == null){
//            this.view.printMensagem("Playlist não existe");
//            return;
//        }
//        ArrayList <Musica> musicas = playlist.getMusicas();
//        ListaReproducao lista = new ListaReproducao(musicas, modo);
//        utilizadorOuveMusica(user, lista.getLista());
//    }

    public void utilizadorOuveMusica(Utilizador user, ArrayList<Musica> musicas) {
        PlaybackController playback = new PlaybackController(view, musicas, user);
        view.printMensagem("F para avançar, R para voltar, S para parar a reprodução.");
        playback.play(); // Inicia a reprodução

        boolean running = true;
        while (running) {
            UserInput input = new UserInput();
            String cmd = input.lerString("").toUpperCase();

            switch (cmd) {
                case "S": // STOP
                    playback.stop();
                    view.printMensagem("Reprodução interrompida.");
                    running = false;
                    break;
                case "F": // FORWARD (SKIP)
                    playback.forward();
                    break;
                case "R": // REWIND (BACK)
                    if (user.getPlanoSubscricao() instanceof PlanoFree) {
                        view.printMensagem("Ação inválida em plano gratuito!");
                    } else {
                        playback.back();
                    }
                    break;
                default:
                    view.printMensagem("Ação inválida!");
            }
        }
    }

    public void userVerPontos (Utilizador user) {
        int pontos = user.getPontos();
        this.view.printPontos (user.getNome(), pontos);
    }
    public void userApagarConta (Utilizador user) {
        this.modelo.removerUtilizador(user);
        this.view.printMensagem("Utilizador " + user.getNome() + " apagado.");
    }

    public void userCriaPlaylist (Utilizador user) {
        if (user.getPlanoSubscricao().podeCriarPlaylist()) {
            novaPlaylist (user);
        }
        else this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, false);
    }

    public void userVerHistorico(Utilizador user) {
        this.view.printHistorico(user);
    }

    public Playlist obterPlaylistValidaDoUserInput (Utilizador user) {
        String nomePlaylist = this.view.pedeNome ("playlist", false);
        List<Playlist> playlists = getPlaylists(nomePlaylist);
        if (playlists == null || playlists.isEmpty()) return null;

        List <Playlist> filteredPlaylist = playlists.stream().
                filter(p -> p.isPublica() || p.getCriador().equals(user)).toList();
        if (filteredPlaylist.isEmpty()) return null;

        return filteredPlaylist.size() == 1 ? filteredPlaylist.getFirst() : SpotView.escolheDeUmaLista(filteredPlaylist);
    }

    public Musica obterMusicaValidaDoUserInput () {
        String musicaNome = this.view.pedeNome("música", false);
        List <Musica> musicas;
        try { musicas = this.modelo.getMusicasPeloNome(musicaNome);
        } catch (MusicaException e) {
            this.view.mostraMensagemErro (e);
            return null;
        }
        if (musicas.isEmpty()) return null;
        return musicas.size() == 1 ? musicas.getFirst() : SpotView.escolheDeUmaLista(musicas);
    }

    public Album obterAlbumValidoDoUserInput () {
        String nomeAlbum = this.view.pedeNome("álbum", true);
        List<Album> albuns = getAlbuns(nomeAlbum);
        if (albuns == null || albuns.isEmpty()) return null;

        return albuns.size() == 1 ? albuns.getFirst() : SpotView.escolheDeUmaLista(albuns);
    }

    public Album obterAlbumValidoDoUserInput (List <Album> albuns) {
        if (albuns == null || albuns.isEmpty()) return null;
        String nomeAlbum = this.view.pedeNome("álbum", true);
        List <Album> albunsFiltrados = albuns.stream().filter(a -> a.getNome().equalsIgnoreCase(nomeAlbum)).toList();
        if (albunsFiltrados.isEmpty()) return null;
        return albunsFiltrados.size() == 1 ? albunsFiltrados.getFirst() : SpotView.escolheDeUmaLista(albunsFiltrados);
    }

    public Playlist obterPlaylistValidaDoUserInput (List <Playlist> playlists) {
        if (playlists == null || playlists.isEmpty()) return null;
        String nomePlaylist = this.view.pedeNome("playlist", false);
        List <Playlist> playlistsFiltrados = playlists.stream().filter(a -> a.getNome().equalsIgnoreCase(nomePlaylist)).toList();
        if (playlistsFiltrados.isEmpty()) return null;
        return playlistsFiltrados.size() == 1 ? playlistsFiltrados.getFirst() : SpotView.escolheDeUmaLista(playlistsFiltrados);
    }


    public void adicionaMusica(Musica m, Album a) {
        if (m == null || a == null) throw new IllegalArgumentException("Música ou álbum inválidos.");
        this.modelo.adicionaMusica(m, a);
    }

    public List<Album> getAlbuns (String nome) {
        List<Album> albuns;
        try { albuns = this.modelo.getAlbum(nome);
        } catch (AlbumException e) {
            this.view.mostraMensagemErro(e);
            return null;
        }

        return albuns;
    }

    public void menuCriaPlaylist (Utilizador user) {
        NewMenu menu = new NewMenu(new String[]{
                "Criar playlist manualmente",
                "Criar playlist baseada no histórico",
                "Criar playlist explícita baseada no histórico",
                "Criar playlist baseada no histórico com duração máxima",
                "Criar playlist explícita baseada no histórico com duração máxima"
        }, "Criar Playlist");

        menu.setHandler(1, () -> userCriaPlaylist(user));
        menu.setHandler(2, () -> playlistBaseadaNoHistorico(user, false, -1));
        menu.setHandler(3, () -> playlistBaseadaNoHistorico (user, true, -1));
        menu.setHandler(4, () -> {
            UserInput input = new UserInput();
            int duracao = input.lerInt("Insira a duração máxima (em minutos) ",
                    "Deve ser maior que 0",
                    i -> i > 0);
            playlistBaseadaNoHistorico(user, false, duracao);
        });
        menu.setHandler(5, () -> {
            UserInput input = new UserInput();
            int duracao = input.lerInt("Insira a duração máxima (em minutos) ",
                    "Deve ser maior que 0",
                    i -> i > 0);
            playlistBaseadaNoHistorico(user, true, duracao);
        });
        menu.run();
    }

    public void playlistBaseadaNoHistorico (Utilizador user, Boolean isExplicita, int duracaoMaxima) {
        List <Musica> musicas= this.modelo.getTodasAsMusicas();
        Playlist playlist = Recomendador.recomenda(user, musicas, isExplicita, duracaoMaxima);
        adicionaPlaylistBiblioteca(user, playlist);
    }

    public void adicionaMusicaPlaylist (Musica m, Utilizador user) {
        Playlist p = obterPlaylistValidaDoUserInput(user);
        if (p == null){
            this.view.printMensagem("Playlist não existe");
            return;
        }
        p.adicionarMusica(m);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_MUSICA, true);
    }

    public List<Playlist> getPlaylists (String nome) {
        List<Playlist> playlists;
        try { playlists = this.modelo.getPlaylist(nome);
        } catch (PlaylistException e) {
            this.view.mostraMensagemErro(e);
            return null;
        }

        return playlists;
    }
    public void adicionaAlbumBiblioteca (Utilizador utilizador, Album album) {
        if (utilizador.getBiblioteca().estaNaBiblioteca(album)) {
            this.view.printMensagem(SpotView.Mensagem.ADICIONAR_ALBUM, false);
            return;
        }
        utilizador.getBiblioteca().adicionaAlbum(album);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_ALBUM, true);
    }

    public void adicionaPlaylistBiblioteca (Utilizador utilizador, Playlist playlist) {
        if (utilizador.getBiblioteca().estaNaBiblioteca(playlist)) {
            this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, false);
            return;
        }
        utilizador.getBiblioteca().adicionaPlaylist(playlist);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, true);
    }

    // Persistência

    private void exportar (String tipo, String ficheiro) {
        boolean sucesso = guardarEstado(tipo, ficheiro);
        this.view.printMensagem(SpotView.Mensagem.GUARDAR, sucesso);
    }


    public void menuEstatisticas() {
        NewMenu menu = new NewMenu(new String[]{
                "Música mais reproduzida",
                "Intérprete mais escutado",
                "Utilizador que mais músicas ouviu",
                "Utilizador com mais pontos",
                "Género musical mais reproduzido",
                "Número de playlists públicas",
                "Utilizador com mais playlists"
        }, "Estatísticas");

        menu.setHandler(1, this::queryMusicaMaisReproduzida);
        menu.setHandler(2, this::queryInterpreteMaisEscutado);
        menu.setHandler(3, this::menuUtilizadorMaisMusicasOuvidas);
        menu.setHandler(4, this::queryUtilizadorComMaisPontos);
        menu.setHandler(5, this::queryGeneroMaisReproduzido);
        menu.setHandler(6, this::queryNumeroPlaylistsPublicas);
        menu.setHandler(7, this::queryUtilizadorComMaisPlaylists);

        menu.run();
    }

    public void menuUtilizadorMaisMusicasOuvidas() {
        NewMenu menu = new NewMenu(new String[]{
                "Sim, quero filtrar por data",
                "Não, sem filtro"
        }, "Filtrar por data?");

        menu.setHandler(1, this::queryUtilizadorMaisMusicasOuvidasIntervalo);
        menu.setHandler(2, this::queryUtilizadorMaisMusicasOuvidas);
        menu.run();
    }

    public void queryMusicaMaisReproduzida() {
        Musica maisOuvida = Estatisticas.getMusicaMaisReproduzida(modelo);
        if (maisOuvida == null) {
            view.printSemDadosEstatistica();
        } else {
            view.printMusicaMaisReproduzida(maisOuvida);
        }
    }

    public void queryInterpreteMaisEscutado() {
        String artista = Estatisticas.getInterpreteMaisEscutado(modelo);
        if (artista == null) {
            view.printSemDadosEstatistica();
        } else {
            int totalReproducoes = (int) modelo.getUtilizadores().values().stream()
                    .flatMap(u -> u.getHistorico().stream())
                    .filter(r -> r.getMusica().getInterprete().equals(artista))
                    .count();
            view.printInterpreteMaisEscutado(artista, totalReproducoes);
        }
    }

    public void queryUtilizadorMaisMusicasOuvidas() {
        Map.Entry<String, Integer> resultado = Estatisticas.getUtilizadorMaisMusicasOuvidas(modelo);
        if (resultado == null) {
            view.printSemDadosEstatistica();
        } else {
            view.printUtilizadorMaisMusicasOuvidas(resultado.getKey(), resultado.getValue());
        }
    }

    public void queryUtilizadorMaisMusicasOuvidasIntervalo() {
        String dataInicial = view.pedeData("Data inicial (AAAA-MM-DD): ");
        String dataFinal = view.pedeData("Data final (AAAA-MM-DD): ");

        Map.Entry<String, Integer> resultado = Estatisticas.getUtilizadorMaisMusicasOuvidasIntervalo(modelo, dataInicial, dataFinal);

        if (resultado == null || resultado.getValue() == 0) {
            view.printSemDadosEstatistica();
        } else {
            view.printUtilizadorMaisMusicasOuvidas(resultado.getKey(), resultado.getValue());
        }
    }

    public void queryUtilizadorComMaisPontos() {
        Map.Entry<String, Integer> entry = Estatisticas.getUtilizadorComMaisPontos(modelo);
        if (entry == null) {
            view.printSemDadosEstatistica();
        } else {
            view.printUtilizadorComMaisPontos(entry.getKey(), entry.getValue());
        }
    }

    public void queryGeneroMaisReproduzido() {
        Map.Entry<String, Integer> generoMaisReproduzido = Estatisticas.getGeneroMaisReproduzido(modelo);
        if (generoMaisReproduzido == null) {
            view.printSemDadosEstatistica();
        } else {
            String genero = generoMaisReproduzido.getKey();
            int total = generoMaisReproduzido.getValue();
            view.printGeneroMaisReproduzido(genero, total);
        }
    }

    public void queryNumeroPlaylistsPublicas() {
        int total = Estatisticas.getNumeroPlaylistsPublicas(modelo);
        if (total == 0) {
            view.printSemDadosEstatistica();
        } else {
            view.printNumeroPlaylistsPublicas(total);
        }
    }

    public void queryUtilizadorComMaisPlaylists() {
        Map.Entry<String, Integer> nPlaylists = Estatisticas.getUtilizadorComMaisPlaylists(modelo);
        if (nPlaylists == null)
            view.printSemDadosEstatistica();
        else
            view.printUtilizadorComMaisPlaylists(nPlaylists.getKey(), nPlaylists.getValue());
    }

    /***
     * Guarda um estado do programa para o disco. */
    private void guardarEstado () {
        NewMenu menu = new NewMenu(new String[] {
                "Exportar utilizadores",
                "Exportar albuns",
                "Exportar playlists",
                "Exportar todos os dados"
        }, "Exportar");

        menu.setHandler(1, () -> exportar("utilizadores", "utilizadores.dat"));
        menu.setHandler(2, () -> exportar("albuns", "albuns.dat"));
        menu.setHandler(3, () -> exportar("playlists", "playlists.dat"));

        menu.setHandler(4, () -> {
            exportar("utilizadores", "utilizadores.dat");
            exportar("albuns", "albuns.dat");
            exportar("playlists", "playlists.dat");
        });

        menu.run();
    }
    /** Função genérica para guardar um tipo de dados para um ficheiro binário */
    public boolean guardarEstado(String tipo, String ficheiro) {
        Object objetoAGuardar = switch (tipo) {
            case "utilizadores" -> this.modelo.getUtilizadores();
            case "albuns"       -> this.modelo.getAlbunsPorTitulo();
            case "playlists"    -> this.modelo.getPlaylistsPorTitulo();
            case "total"        -> this.modelo;
            default             -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        };

        try {
            return Utils.guardarObjeto(objetoAGuardar, ficheiro);
        } catch (IOException e) {
            this.view.mostraMensagemErro("Erro ao guardar: ", e);
            return false;
        }
    }

    private void importar(String tipo, String ficheiro) {
        boolean sucesso = carregarEstado(tipo, ficheiro);
        this.view.printMensagem(SpotView.Mensagem.CARREGAR, sucesso);
    }

    /***
     * Carrega um estado já existente do disco. */
    private void carregarEstado() {
        NewMenu menu = new NewMenu(new String[] {
                "Importar utilizadores",
                "Importar álbuns",
                "Importar playlists",
                "Importar todos os dados"
        }, "Importar");

        menu.setHandler(1, () -> importar("utilizadores", "utilizadores.dat"));
        menu.setHandler(2, () -> importar("albuns", "albuns.dat"));
        menu.setHandler(3, () -> importar("playlists", "playlists.dat"));

        menu.setHandler(4, () -> {
            importar("utilizadores", "utilizadores.dat");
            importar("albuns", "albuns.dat");
            importar("playlists", "playlists.dat");
        });

        menu.run();
    }
    /** Função genérica para carregar um ficheiro binário do disco */
    @SuppressWarnings("unchecked")
    public boolean carregarEstado(String tipo, String ficheiro) {
        Class<?> classe = switch (tipo) {
            case "utilizadores", "albuns",
                 "playlists"                -> Map.class;
            case "total"                    -> SpotModel.class;
            default                         -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        };
        Object obj;
        try {
            obj = Utils.carregarEstado(classe, ficheiro);
            if (obj == null) return false;
        } catch (IOException | ClassNotFoundException e) {
            this.view.mostraMensagemErro("Erro: ", e);
            return false;
        }

        switch  (tipo) {
            case "utilizadores" -> this.modelo.setUtilizadores((Map<String, Utilizador>) obj);
            case "albuns" -> this.modelo.setAlbunsPorTitulo((Map<String, List<Album>>) obj);
            case "playlists" ->  this.modelo.setPlaylistsPorTitulo((Map <String, List <Playlist>>) obj);
            case "total" -> this.modelo = (SpotModel) obj;
        }
        return true;
    }

}