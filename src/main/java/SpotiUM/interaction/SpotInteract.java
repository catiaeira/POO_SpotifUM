package SpotiUM.interaction;

import java.io.IOException;

public class SpotInteract {
    SpotController controlador;

    public SpotInteract(SpotController controlador) {
        this.controlador = controlador;
    }

    public void run() {
        NewMenu mainMenu = new NewMenu(new String[]{
                "Criar SpotiUM.Utilizador",
                "Login SpotiUM.Utilizador",
                "Login Administrador"
        }, "Menu Inicial");

        mainMenu.setHandler(1, () -> {
            System.out.println("Cria a tua conta");
            novoUtilizador();
            });
        mainMenu.setHandler(2, this::menuUtilizador);
        mainMenu.setHandler(3, this::menuAdmin);

        mainMenu.run(); // Corre o menu principal
    }

    public void novoUtilizador () {
        UserInput input = new UserInput();
        String nome = input.lerString("Nome: ");
        String email = input.lerString("Email: ","Email inválido", e -> e.matches(".+@.+\\..+"));
        String morada = input.lerString("Morada: ");
        int plano = input.lerInt("""
                                    Plano:
                                    - 1. Plano grátis
                                    - 2. Plano Premium
                                    - 3. Plano TOP Premium
                                    """, "Insira um número entre 1 e 3" ,i -> (i>0 && i<4));
        int id = this.controlador.novoUser(nome,email,morada,plano);
        System.out.println("SpotiUM.Utilizador adicionado: " + id);
    }







    public void menuUtilizador() {
        UserInput input = new UserInput();
        int id = input.lerInt("Id do utilizador: ", "O número tem de ser positivo", i -> i>=0);
        if (!this.controlador.utilizadorExiste(id)) {
            System.out.println("SpotiUM.Utilizador " + id + " não existe");
            return;
        }
        NewMenu menu = new NewMenu(new String[]{
                "Ouvir música",
                "Criar playlist"
        }, "SpotiUM.Utilizador " + this.controlador.getUtilizadorNome(id));

        menu.setHandler(1, () -> this.controlador.userOuveMusica(id));
        menu.setHandler(2, () ->this.controlador.userCriaPlaylist(id));

        menu.run();
    }

    public void menuAdmin() {
        NewMenu menu = new NewMenu(new String[]{
                "Adicionar música",
                "Adicionar album",
                "Importar dados",
                "Exportar dados"
        }, "Menu Admin");

        menu.setHandler(1,() -> {});
        menu.setHandler(2, () -> {});
        menu.setHandler(3, this::carregarEstado);
        menu.setHandler(4, this::guardarEstado);
        menu.run();
    }

    private void guardarEstado () {
        try {
            this.controlador.guardarEstado("dados.dat");
            System.out.println("Dados guardados!");
        } catch (IOException e) {
            System.out.println("Erro ao guardar: " + e.getMessage());
        }
    }

    private void carregarEstado () {
        try {
            SpotModel novoModelo = SpotModel.carregarEstado("dados.dat");
            this.controlador.setModelo(novoModelo);
            System.out.println("Dados carregados!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }

}