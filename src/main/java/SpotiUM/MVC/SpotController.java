package SpotiUM.MVC;

import SpotiUM.*;

import java.io.IOException;
import java.util.*;

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
        NewMenu menu = new NewMenu(new String[]{
                "Ouvir música",
                "Ouvir álbum",
                "Ouvir playlist",
                "Ver biblioteca",
                "Criar playlist",
                "Ver histórico",
                "Ver pontos"
        }, "Utilizador " + user);

        menu.setHandler(1, () -> musicaMenu(utilizador));
        menu.setHandler(2, () -> albumMenu(utilizador));
        menu.setHandler(3, () -> playlistMenu(utilizador));
        menu.setHandler(4, () -> bibliotecaMenu(utilizador));
        menu.setHandler(5, () -> userCriaPlaylist(utilizador));
        // menu.setHandler(6, () -> );
        menu.setHandler(7, () -> userVerPontos(utilizador));

        menu.run();
    }

    public void menuAdmin() {
        NewMenu menu = new NewMenu(new String[]{
                "Adicionar música a álbum existente",
                "Novo album",
                "Importar dados",
                "Exportar dados"
        }, "Admin");

        menu.setHandler(1, this::adicionaMusicaComInputUsuario);
        menu.setHandler(2, this::adicionaAlbum);
        menu.setHandler(3, this::carregarEstado);
        menu.setHandler(4, this::guardarEstado);
        menu.run();
    }

    public void musicaMenu (Utilizador utilizador) {
        Musica musica = obterMusicaValidaDoUserInput();
        if (musica == null) return;
        NewMenu menu = new NewMenu(new String[] {
                "Ouvir música",
                "Adicionar a playlist"
        }, musica.getNome());
        menu.setHandler(1, () -> utilizadorOuveMusica(utilizador, new ArrayList<>((Collection) musica)));
        menu.setHandler(2, () -> adicionaMusicaPlaylist(musica));
        menu.run();
    }

    public void albumMenu(Utilizador utilizador) {
        Album album = obterAlbumValidoDoUserInput();
        if (album == null) return;

        NewMenu menu = new NewMenu(new String[] {
                "Ouvir álbum",
                "Adicionar à biblioteca"
        }, album.getNome());

        menu.setHandler(1, () -> utilizadorOuveAlbum(utilizador, album));
        menu.setHandler(2, () -> adicionaAlbumBiblioteca(utilizador, album));

        menu.run();
    }

    public void playlistMenu(Utilizador utilizador) {
        Playlist playlist = obterPlaylistValidaDoUserInput();
        if (playlist == null) return;

        NewMenu menu = new NewMenu(new String[] {
                "Ouvir playlist",
                "Adicionar à biblioteca"
        }, playlist.getNome());

        menu.setHandler(1, () -> utilizadorOuvePlaylist(utilizador, playlist));
        menu.setHandler(2, () -> adicionaPlaylistBiblioteca(utilizador, playlist));

        menu.run();
    }



    public void bibliotecaMenu(Utilizador utilizador) {
        NewMenu menu = new NewMenu(new String[] {
                "Ver biblioteca",
                "Ouvir álbum",
                "Ouvir playlist",
        }, "Biblioteca");
        Biblioteca biblioteca = utilizador.getBiblioteca();
        menu.setHandler(1, () -> this.view.printMensagem(biblioteca.toString()));
        menu.setHandler(2, () -> {
                                    List <Album> albuns = biblioteca.getAlbuns();
                                    if (albuns == null || albuns.isEmpty()) return;
                                    Album album = albuns.size() == 1 ? albuns.getFirst() : SpotView.escolheDeUmaLista(albuns);
                                    utilizadorOuveAlbum(utilizador, album);
                                    });
        menu.setHandler(2, () -> {
                                    ArrayList<Playlist> playlists = biblioteca.getPlaylists();
                                    if (playlists == null || playlists.isEmpty()) return;
                                    Playlist playlist = playlists.size() == 1 ? playlists.getFirst() : SpotView.escolheDeUmaLista(playlists);
                                    utilizadorOuvePlaylist(utilizador, playlist);
                                    });
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

    public void adicionaMusicaComInputUsuario() {
        Album album = obterAlbumValidoDoUserInput();
        if (album == null) return;
        adicionaMusica(album);
    }

    public void adicionaMusica(Album album) {
        if (album == null) {
            this.view.printMensagem(SpotView.Mensagem.ADICIONAR_MUSICA, false);
            return;
        }
        Musica musica = novaMusica();
        this.modelo.adicionaMusica(musica, album);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_MUSICA, true);
    }

    public Musica novaMusica () {
        List <String> musicaDados = this.view.novaMusica();
        return new Musica(
                musicaDados.get(0), musicaDados.get(1), musicaDados.get(2),
                musicaDados.get(3), musicaDados.get(4), musicaDados.get(5),
                Integer.parseInt(musicaDados.get(6)),
                0);
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

        int numeroMusicas = Integer.parseInt(playlistDados.get(1));
        ArrayList<Musica> musicas = new ArrayList<>();
        for (int i = 0; i<numeroMusicas; i++) {
            String nome = this.view.pedeNome("música "+ (i+1), false);
            try {
                List <Musica> m = this.modelo.getMusicasPeloNome(nome);
                Musica musica = m.size() == 1 ? m.getFirst() : SpotView.escolheDeUmaLista(m);
                musicas.add(musica);
            } catch (MusicaException e) {
                this.view.mostraMensagemErro(e);
                return;
            }
        }
        Playlist playlist = new Playlist(playlistDados.getFirst(), utilizador, musicas);
        this.modelo.adicionarPlaylist(playlist);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, true);
    }

//    public void tocarListaReproducao (Iterator<Musica> iter, Utilizador user){
//        iter.forEachRemaining(m -> utilizadorOuveMusica(user, m));
//    }

    public void utilizadorOuveAlbum (Utilizador user, Album album) {
        ArrayList <Musica> musicas = album.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, user);
        utilizadorOuveMusica(user, lista.getLista());
    }

    public void utilizadorOuveAlbum (Utilizador user) {
        Album album = obterAlbumValidoDoUserInput();
        ArrayList <Musica> musicas = album.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, user);
        utilizadorOuveMusica(user, lista.getLista());
    }

    public void utilizadorOuvePlaylist (Utilizador user, Playlist playlist) {
        ArrayList <Musica> musicas = playlist.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, user);
        utilizadorOuveMusica(user, lista.getLista());
    }

    public void utilizadorOuvePlaylist (Utilizador user) {
        Playlist playlist = obterPlaylistValidaDoUserInput();
        ArrayList <Musica> musicas = playlist.getMusicas();
        ListaReproducao lista = new ListaReproducao(musicas, user);
        utilizadorOuveMusica(user, lista.getLista());
    }

    public void utilizadorOuveMusica(Utilizador user, ArrayList<Musica> musicas) {
        PlaybackController playback = new PlaybackController(view, musicas);
        playback.play(); // Start first song

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
                        view.printMensagem("Ação inválida!");
                    } else {
                        playback.back();
                    }
                    break;
                default:
                    view.printMensagem("Ação inválida!");
            }
        }
        user.atualizaPontos();
    }

    public void userVerPontos (Utilizador user) {
        int pontos = user.getPontos();
        this.view.printPontos (user.getNome(), pontos);
    }

    public void userCriaPlaylist (Utilizador user) {
        if (user.getPlanoSubscricao().podeCriarPlaylist()) {
            novaPlaylist (user);
        }
        else this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, false);
    }

    public Playlist obterPlaylistValidaDoUserInput () {
        String nomePlaylist = this.view.pedeNome ("Playlist", false);
        List<Playlist> playlists = getPlaylists(nomePlaylist);
        if (playlists == null || playlists.isEmpty()) return null;

        return playlists.size() == 1 ? playlists.getFirst() : SpotView.escolheDeUmaLista(playlists);
    }

    public Musica obterMusicaValidaDoUserInput () {
        String musicaNome = this.view.pedeNome("música", false);
        List <Musica> musicas;
        try { musicas = this.modelo.getMusicasPeloNome(musicaNome);
        } catch (MusicaException e) {
            this.view.mostraMensagemErro (e);
            return null;
        }
        return musicas.size() == 1 ? musicas.getFirst() : SpotView.escolheDeUmaLista(musicas);
    }

    public Album obterAlbumValidoDoUserInput () {
        String nomeAlbum = this.view.pedeNome("álbum", true);
        List<Album> albuns = getAlbuns(nomeAlbum);
        if (albuns == null || albuns.isEmpty()) return null;

        return albuns.size() == 1 ? albuns.getFirst() : SpotView.escolheDeUmaLista(albuns);
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

    public void adicionaMusicaPlaylist (Musica m) {
        Playlist p = obterPlaylistValidaDoUserInput();
        if (p == null) return;
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
        utilizador.getBiblioteca().adicionaAlbum(album);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_ALBUM, true);
    }

    public void adicionaPlaylistBiblioteca (Utilizador utilizador, Playlist playlist) {
        utilizador.getBiblioteca().adicionaPlaylist(playlist);
        this.view.printMensagem(SpotView.Mensagem.ADICIONAR_PLAYLIST, true);
    }

    // Persistência

    /***
     * Guarda um estado do programa para o disco. */
    private void guardarEstado () {
        NewMenu menu = new NewMenu(new String[] {
                "Exportar utilizadores",
                "Exportar albuns",
                "Exportar playlists",
                "Exportar todos os dados"
        }, "Exportar");

        menu.setHandler(1, () -> {
            if (guardarEstado("utilizadores", "utilizadores.dat"))
                this.view.printMensagem(SpotView.Mensagem.GUARDAR, true);
            else this.view.printMensagem(SpotView.Mensagem.GUARDAR, false);
        });

        menu.setHandler(2, () -> {
            if (guardarEstado("albuns", "albuns.dat"))
                this.view.printMensagem(SpotView.Mensagem.GUARDAR, true);
            else this.view.printMensagem(SpotView.Mensagem.GUARDAR, false);
        });
        menu.setHandler(3, () -> {
            if (guardarEstado("playlists", "playlists.dat"))
                this.view.printMensagem(SpotView.Mensagem.GUARDAR, true);
            else this.view.printMensagem(SpotView.Mensagem.GUARDAR, false);
        });
        menu.setHandler(4, () -> {
            if (guardarEstado("total", "dados.dat"))
                this.view.printMensagem(SpotView.Mensagem.GUARDAR, true);
            else this.view.printMensagem(SpotView.Mensagem.GUARDAR, false);
        });

        menu.run();
    }
    /** Função genérica para guardar um tipo de dados para um ficheiro binário */
    public boolean guardarEstado(String tipo, String ficheiro) {
        Object objetoAGuardar = switch (tipo) {
            case "utilizadores" -> this.modelo.getUtilizadores();
            case "albuns"       -> this.modelo.getAlbunsPorID();
            case "playlists"    -> this.modelo.getPlaylistsPorID();
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
    /***
     * Carrega um estado já existente do disco. */
    private void carregarEstado() {
        NewMenu menu = new NewMenu(new String[] {
                "Importar utilizadores",
                "Importar álbuns",
                "Importar playlists",
                "Importar todos os dados"
        }, "Importar");

        menu.setHandler(1, () -> {
            if (carregarEstado("utilizadores", "utilizadores.dat"))
                this.view.printMensagem(SpotView.Mensagem.CARREGAR, true);
            else this.view.printMensagem(SpotView.Mensagem.CARREGAR, false);
        });
        menu.setHandler(2, () -> {
            if (carregarEstado("albuns", "albuns.dat"))
                this.view.printMensagem(SpotView.Mensagem.CARREGAR, true);
            else this.view.printMensagem(SpotView.Mensagem.CARREGAR, false);
        });
        menu.setHandler(3, () -> {
            if (carregarEstado("playlists", "playlists.dat"))
                this.view.printMensagem(SpotView.Mensagem.CARREGAR, true);
            else this.view.printMensagem(SpotView.Mensagem.CARREGAR, false);
        });

        menu.setHandler(4, () -> {
            if (carregarEstado("total", "dados.dat"))
                this.view.printMensagem(SpotView.Mensagem.CARREGAR, true);
            else this.view.printMensagem(SpotView.Mensagem.CARREGAR, false);
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
            case "albuns" -> this.modelo.setAlbunsPorID((Map<Integer, Album>) obj);
            case "playlists" ->  this.modelo.setPlaylistsPorID((Map <Integer, Playlist>) obj);
            case "total" -> this.modelo = (SpotModel) obj;
        }
        return true;
    }

}
