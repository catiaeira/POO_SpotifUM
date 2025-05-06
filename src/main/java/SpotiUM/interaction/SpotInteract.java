package SpotiUM.interaction;

import SpotiUM.Album;
import SpotiUM.Musica;

import java.util.ArrayList;
import java.util.List;

public class SpotInteract {
    private SpotController controlador;

    public SpotInteract(SpotController controlador) {
        this.controlador = controlador;
    }

    public void run() {
        NewMenu mainMenu = new NewMenu(new String[]{
                "Criar Utilizador",
                "Login Utilizador",
                "Funções de Administrador"
        }, "Menu Inicial");

        mainMenu.setHandler(1, () -> {
            System.out.println("Cria a tua conta");
            novoUtilizador();
            });
        mainMenu.setHandler(2, this::menuUtilizador);
        mainMenu.setHandler(3, this::menuAdmin);

        mainMenu.run(); // Corre o menu principal
    }

    public Album obterAlbumValidoDoUserInput () {
        UserInput input = new UserInput();
        String nomeAlbum = input.lerString("Nome do álbum: ");
        List<Album> albuns = controlador.getAlbuns(nomeAlbum);

        if (albuns == null || albuns.isEmpty()) return null;

        return albuns.size() == 1 ? albuns.getFirst() : escolheDeUmaLista(albuns);
    }

    public void adicionaMusica() {
        UserInput input = new UserInput();
        Album album = obterAlbumValidoDoUserInput();
        if (album == null) {
            System.out.println("Álbum não existe.");
            return;
        }
        Musica m = novaMusica();

        try {
            controlador.adicionaMusica(m, album);
            System.out.println("Música adicionada.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void utilizadorOuveMusica (String user) {
        UserInput input = new UserInput();
        Album album = obterAlbumValidoDoUserInput();
        if (album == null) {
            System.out.println("Álbum não existe.");
            return;
        }
        String musicaNome = input.lerString("Nome da música: ");

        if (!this.controlador.userOuveMusica(user, musicaNome, album)) System.out.println("Música não existe");
    }

    public static <T> T escolheDeUmaLista(List<T> obj) {
        System.out.println("Qual pretendes?");
        for (int i = 0; i< obj.size(); i++) {
            System.out.println("-----");
            System.out.println(i+1 + ".");
            System.out.println(obj.get(i).toString());
            System.out.println("-----");
        }
        UserInput input = new UserInput();
        int index = input.lerInt("Insira o número correspondente: ",
                                    "Insira um número válido",
                                   i -> i>0 && i < obj.size()+1);
        return obj.get(index-1);
    }

    // nova entidade

    public void novoUtilizador () {
        UserInput input = new UserInput();
        String nome = input.lerString("Nome: ", "Username já existente", u -> !this.controlador.utilizadorExiste(u));
        String email = input.lerString("Email: ","Email inválido", e -> e.matches(".+@.+\\..+"));
        String morada = input.lerString("Morada: ");
        int plano = input.lerInt("""
                                    Plano:
                                    - 1. Plano grátis
                                    - 2. Plano Premium
                                    - 3. Plano TOP Premium
                                    """, "Insira um número entre 1 e 3" ,i -> (i>0 && i<4));
        this.controlador.novoUser(nome,email,morada,plano);
        System.out.println("Utilizador adicionado!");
    }

    public Musica novaMusica () {
        UserInput input = new UserInput();
        String nome = input.lerString("Nome da Música: ");
        String artista = input.lerString("Nome do artista: ");
        String editora = input.lerString("Nome da editora: ");
        String letra = input.lerString("Letra: ");
        String musica = input.lerString("Musica: ");
        String genero = input.lerString("Género: ");
        int duracao = input.lerInt("Duração (segundos): ", "Deve ser maior que 0", i -> i>0);

        return new Musica(nome, artista, editora, letra, genero, musica, duracao, 0);
    }

    public void novoAlbum () {
        UserInput input = new UserInput();
        String nome = input.lerString("Nome do Álbum: ");
        String artista = input.lerString("Nome do artista: ");
        int numeroMusicas = input.lerInt("Número de músicas: ", "Tem de ter pelo menos uma música", i -> i>0);
        ArrayList <Musica> musicas = new ArrayList<>();
        for (int i = 0; i<numeroMusicas; i++) {
            musicas.add(novaMusica());
        }

        int id = this.controlador.adicionaAlbum (nome, artista, musicas);
        System.out.println("Álbum adicionado: " + id);
    }

    // menus

    public void menuUtilizador() {
        UserInput input = new UserInput();
        String user = input.lerString("Nome do utilizador: ");
        if (!this.controlador.utilizadorExiste(user)) {
            System.out.println("Utilizador " + user + " não existe");
            return;
        }
        NewMenu menu = new NewMenu(new String[]{
                "Ouvir música",
                "Criar playlist"
        }, "Utilizador " + user);

        menu.setHandler(1, () -> utilizadorOuveMusica(user));
        menu.setHandler(2, () ->this.controlador.userCriaPlaylist(user));

        menu.run();
    }

    public void menuAdmin() {
        NewMenu menu = new NewMenu(new String[]{
                "Adicionar música",
                "Novo album",
                "Importar dados",
                "Exportar dados"
        }, "Admin");

        menu.setHandler(1, this::adicionaMusica);
        menu.setHandler(2, this::novoAlbum);
        menu.setHandler(3, this::carregarEstado);
        menu.setHandler(4, this::guardarEstado);
        menu.run();
    }

// Persistência
    /***
     * Carrega um estado já existente do disco. */
    private void carregarEstado() {
        NewMenu menu = new NewMenu(new String[] {
                "Importar utilizadores",
                "Importar álbuns",
                "Importar todos os dados"
        }, "Importar");

        menu.setHandler(1, () -> {
            if (controlador.carregarEstado("utilizadores", "utilizadores.dat"))
                System.out.println("Dados carregados com sucesso!");
            else System.out.println("Erro ao carregar os dados");
        });
        menu.setHandler(2, () -> {
            if (this.controlador.carregarEstado("albuns", "albuns.dat"))
                System.out.println("Dados carregados com sucesso!");
            else System.out.println("Erro ao carregar os dados");
        });

        menu.setHandler(3, () -> {
            if (this.controlador.carregarEstado("total", "dados.dat"))
                System.out.println("Dados carregados com sucesso!");
            else System.out.println("Erro ao carregar os dados");
        });

        menu.run();
    }

    /***
     * Guarda um estado do programa para o disco. */
    private void guardarEstado () {
        NewMenu menu = new NewMenu(new String[] {
                "Exportar utilizadores",
                "Exportar albuns",
                "Exportar todos os dados"
        }, "Exportar");

        menu.setHandler(1, () -> {
            if (this.controlador.guardarEstado("utilizadores", "utilizadores.dat"))
                System.out.println("Dados dos utilizadores guardados!");
            else System.out.println("Dados não guardados");
        });

        menu.setHandler(2, () -> {
            if (this.controlador.guardarEstado("albuns", "albuns.dat"))
                System.out.println("Dados dos álbuns guardados!");
            else System.out.println("Dados não guardados");
        });

        menu.setHandler(3, () -> {
            if (this.controlador.guardarEstado("total", "dados.dat"))
                System.out.println("Dados guardados!");
            else System.out.println("Dados não guardados");
        });

        menu.run();
    }


}