package SpotiUM.MVC;

import SpotiUM.Utilizador;
import SpotiUM.Reproducao;
import SpotiUM.Musica;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SpotView {
    private final UserInput input;

    public SpotView() {
        this.input = new UserInput();
    }
    public enum Mensagem {

        CARREGAR ("Dados carregados com sucesso!", "Erro ao carregar os dados"),
        GUARDAR ("Dados guardados!", "Dados não guardados"),
        ADICIONAR_MUSICA ("Música adicionada com sucesso!", "Adicionar música falhou"),
        ADICIONAR_ALBUM ("Álbum adicionado com sucesso!", "Álbum já existe"),
        ADICIONAR_PLAYLIST ("Playlist adicionada com sucesso!", "Playlist já existe");

        private final String mensagemSucesso;
        private final String mensagemErro;

        Mensagem (String msg) {
            this.mensagemSucesso = "";
            this.mensagemErro = "";
        }

        Mensagem (String mensagemSucesso, String mensagemErro) {
            this.mensagemSucesso = mensagemSucesso;
            this.mensagemErro = mensagemErro;
        }
        public void printMensagem (boolean foiSucesso) {
            System.out.println(foiSucesso ? mensagemSucesso : mensagemErro);
        }
    }
    public void printMensagem (String msg) {
        System.out.println(msg);
    }
    public void printMensagem (Mensagem msg, boolean b) {
        msg.printMensagem(b);
    }

    public String pedeNome (String objeto, boolean masculino) {
        if (masculino) return input.lerString("Nome do " + objeto + ": ");
        else return input.lerString("Nome da " + objeto + ": ");
    }

    public void printPontos (String user, int pontos) {
        System.out.println(user + " tem " + String.valueOf(pontos) + " pontos");
    }

    public void printMusicaMaisReproduzida(Musica musica) {
        System.out.println("Música mais reproduzida:\n" + musica.toString());
    }

    public void printSemDadosEstatistica() {
        System.out.println("Não existem dados de reprodução.");
    }

    public void printInterpreteMaisEscutado(String artista, int totalReproducoes) {
        System.out.println("Intérprete mais escutado: " + artista + " com " + totalReproducoes + " reproduções.");
    }

    public void printUtilizadorMaisMusicasOuvidas(String nome, int total) {
        System.out.println("Utilizador que ouviu mais músicas: " + nome + " com " + total + " músicas ouvidas");
    }

    public void printUtilizadorComMaisPontos(String nome, int pontos) {
        System.out.println("Utilizador com mais pontos: " + nome + " com " + pontos + " pontos.");
    }

    public void printGeneroMaisReproduzido(String genero, int totalReproducoes) {
        System.out.println("Género musical mais reproduzido: " + genero + " com " + totalReproducoes + " reproduções.");
    }

    public void printNumeroPlaylistsPublicas(int total) {
        System.out.println("Número de playlists públicas: " + total);
    }

    public void printUtilizadorComMaisPlaylists(String nome, int total) {
        System.out.println("Utilizador com mais playlists: " + nome + " com " + total + " playlists.");
    }

    public void printHistorico(Utilizador u) {
        List<Reproducao> historico = u.getHistorico();

        if (historico.isEmpty()) {
            System.out.println("Histórico vazio.");
            return;
        }

        System.out.println("Histórico de reproduções:");
        for (Reproducao r : historico) {
            System.out.println("- " + r.getMusica().getNome() + " por " + r.getMusica().getInterprete() + " em " + r.getData());
        }
    }

    public List<String> novoUtilizador(Predicate<String> validarNomeUser) {
        List<String> reply = new ArrayList<>();
        reply.add(input.lerString("Nome: ", "Username já existente", validarNomeUser));
        reply.add(input.lerString("Morada: "));
        reply.add(input.lerString("Email: ", "Email inválido", e -> e.matches(".+@.+\\..+")));
        int plano = input.lerInt("""
                Plano:
                - 1. Plano grátis
                - 2. Plano Premium
                - 3. Plano TOP Premium
                """, "Insira um número entre 1 e 3", i -> (i > 0 && i < 4));

        reply.add(Integer.toString(plano));
        return (reply);
    }

    public List <String> novaMusica () {
        List<String> reply = new ArrayList<>();
        reply.add(input.lerString("Nome da Música: "));
        reply.add(input.lerString("Nome do artista: "));
        reply.add(input.lerString("Nome da editora: "));
        reply.add(input.lerString("Letra: "));
        reply.add(input.lerString("Género: "));
        reply.add(input.lerString("Ficheiro de música: "));
        int duracao = input.lerInt("Duração (segundos): ", "Deve ser maior que 0", i -> i>0);
        boolean explicita = input.lerInt("É explícita?\n1. Sim\n2. Não ", "Insere 1 ou 2", i -> i == 1 || i == 2) == 1;
        boolean multimedia = input.lerInt("É multimédia?\n1. Sim\n2. Não ", "Insere 1 ou 2", i -> i == 1 || i == 2) == 1;

        reply.add (Integer.toString(duracao));
        reply.add(explicita ? "true" : "false");
        reply.add(multimedia ? "true" : "false");

        if (multimedia) reply.add(input.lerString("Link multimédia: "));
        else reply.add("");

        return reply;
    }

    public List <String> novoAlbum () {
        List<String> reply = new ArrayList<>();
        reply.add (input.lerString("Nome do Álbum: "));
        reply.add (input.lerString("Nome do artista: "));
        int musicasNum = (input.lerInt("Número de músicas: ", "Tem de ter pelo menos uma música", i -> i>0));

        reply.add (Integer.toString(musicasNum));
        return reply;
    }

    public List<String> novaPlaylist () {
        List<String> reply = new ArrayList<>();
        reply.add(input.lerString("Nome: "));
        int publicaVsPrivada = input.lerInt("1. Pública\n2. Privada ", "Insere ou 1 ou 2", i -> i == 1 || i == 2);
        if (publicaVsPrivada == 1) reply.add("publica");
        else reply.add("privada");
        int musicasNum = (input.lerInt("Número de músicas: ", "Tem de ter pelo menos uma música", i -> i>0));

        reply.add (Integer.toString(musicasNum));
        return (reply);
    }

    public void ouvirMusica(String nome) {
        System.out.println("A Tocar: " + nome);
    }

    public static <T> T escolheDeUmaLista(List<T> obj) {
        System.out.println("Qual pretendes?\n-----");
        for (int i = 0; i< obj.size(); i++) {
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

    public void mostraMensagemErro (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }

    public void mostraMensagemErro (String msg, Exception e) {
        System.out.println(msg + e.getMessage());
    }
}