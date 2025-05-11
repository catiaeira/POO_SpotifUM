package SpotiUM.MVC;

import SpotiUM.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        UserInput input = new UserInput();
        String user = input.lerString("Nome do utilizador: ");
        Utilizador utilizador;
        try { utilizador = this.modelo.getUtilizador(user);
        } catch (UtilizadorException e) {
           this.view.mostraMensagemErro(e);
           return;
        }
        NewMenu menu = new NewMenu(new String[]{
                "Ouvir música",
                "Criar playlist"
        }, "Utilizador " + user);

        menu.setHandler(1, () -> utilizadorOuveMusica(utilizador));
        menu.setHandler(2, () -> userCriaPlaylist(utilizador));

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

    public void utilizadorOuveMusica(Utilizador user) {
        String musicaNome = this.view.pedeNomeMusica();
        List <Musica> musicas;
        try { musicas = this.modelo.getMusicasPeloNome(musicaNome);
        } catch (MusicaException e) {
            this.view.mostraMensagemErro (e);
            return;
        }

        Musica musica = musicas.size() == 1 ? musicas.getFirst() : SpotView.escolheDeUmaLista(musicas);
        user.ouvirMusica(musica);
        this.view.ouvirMusica(musica.getNome(), musica.getLetra(), musica.getMusica());
    }

    public void userCriaPlaylist (Utilizador user) {
        if (user.getPlanoSubscricao().podeCriarPlaylist()) {
            System.out.println("A criar playlist");
        }
        else {
            System.out.println("Não pode criar a playlist");
        }
    }

    public Album obterAlbumValidoDoUserInput () {
        String nomeAlbum = this.view.pedeNomeAlbum();
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


    // Persistência

    /***
     * Guarda um estado do programa para o disco. */
    private void guardarEstado () {
        NewMenu menu = new NewMenu(new String[] {
                "Exportar utilizadores",
                "Exportar albuns",
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
            case "utilizadores", "albuns"   -> Map.class;
            case "total"                    -> SpotModel.class;
            default                         -> throw new IllegalArgumentException("Tipo desconhecido: " + tipo);
        };
        Object obj;
        try {
            obj = Utils.carregarEstado(classe, ficheiro);
            if (obj == null) return false;
        } catch (IOException | ClassNotFoundException e) {
            this.view.mostraMensagemErro("Erro ao carregar estado: ", e);
            return false;
        }

        switch  (tipo) {
            case "utilizadores" -> this.modelo.setUtilizadores((Map<String, Utilizador>) obj);
            case "albuns" -> this.modelo.setAlbunsPorID((Map<Integer, Album>) obj);
            case "total" -> this.modelo = (SpotModel) obj;
        }
        return true;
    }

}
