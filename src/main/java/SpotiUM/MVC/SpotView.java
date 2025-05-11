package SpotiUM.MVC;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SpotView {
    UserInput input;

    public SpotView() {
        this.input = new UserInput();
    }
    public enum Mensagem {

        CARREGAR ("Dados carregados com sucesso!", "Erro ao carregar os dados"),
        GUARDAR ("Dados guardados!", "Dados não guardados"),
        ADICIONAR_MUSICA ("Música adicionada com sucesso!", "Adicionar música falhou"),
        ADICIONAR_ALBUM ("Álbum adicionado com sucesso!", "Adicionar álbum falhou");

        private final String successMessage;
        private final String errorMessage;

        Mensagem (String mensagemSucesso, String mensagemErro) {
            this.successMessage = mensagemSucesso;
            this.errorMessage = mensagemErro;
        }

        public void printMensagem (boolean wasSuccessful) {
            System.out.println(wasSuccessful ? successMessage : errorMessage);
        }
    }

    public String pedeNomeMusica() {
        return input.lerString("Nome da música: ");
    }

    public String pedeNomeAlbum() {
        return input.lerString("Nome do álbum: ");
    }


    public void printMensagem (Mensagem msg, boolean b) {
        msg.printMensagem(b);
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
        reply.add(input.lerString("Musica: "));
        int duracao = input.lerInt("Duração (segundos): ", "Deve ser maior que 0", i -> i>0);

        reply.add (Integer.toString(duracao));
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

    public void ouvirMusica(String nome, String letra, String musica) {
        System.out.println("A ouvir a música " + nome);
        System.out.println(letra);
        System.out.println(musica);
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

    public void mostraMensagemErro (Exception e) {
        System.out.println("Erro: " + e.getMessage());
    }

    public void mostraMensagemErro (String msg, Exception e) {
        System.out.println(msg + e.getMessage());
    }
}