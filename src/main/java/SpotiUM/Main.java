package SpotiUM;

import SpotiUM.interaction.SpotController;
import SpotiUM.interaction.SpotInteract;
import SpotiUM.interaction.SpotModel;

/*
TODO:
- Playlists e álbuns;
- Reprodução de músicas do utilizador (dependente do plano)
- "Análise dos hábitos de reprodução" para os users premium (artistas e músicas), listas de músicas automáticas baseado nessa análise
    Involve guardar o histórico de músicas do utilizador
- Interação com playlists e álbuns
- Queries
 */
public class Main {
    public static void main(String[] args) {

        //Musica musica1 = new Musica("Seu Moço essa casa tem Axé", "Mestre Careca", "CCCB", "Axé que vem lá do pelourinho", "dim dam dam", "capoeira", 130);
        //Utilizador utilizador1 = new Utilizador("Andreia", "batata@gmail.com", "Batatolandia", new PlanoPremiumTop());

        //utilizador1.ouvirMusica(musica1);

        SpotModel modelo = new SpotModel();
        SpotController controlador = new SpotController(modelo);
        SpotInteract interact = new SpotInteract(controlador);
        interact.run();

        // musica1.reproduzir();
        //System.out.println(musica1.musicaToString());
        //System.out.println(utilizador1);
        //System.out.println("Nome da música: " + musica1.getNome());
        //System.out.println("Intérprete: " + musica1.getInterprete());
        //System.out.println("Editora: " + musica1.getEditora());
    }
}