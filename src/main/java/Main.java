public class Main {
    public static void main(String[] args) {
        Musica musica1 = new Musica("Seu Moço essa casa tem Axé", "Mestre Careca", "CCCB", "Axé que vem lá do pelourinho", "dim dam dam", "capoeira", 130);

        Utilizador utilizador1 = new Utilizador("Andreia", "batata@gmail.com", "Batatolandia", new PlanoPremiumTop());
        utilizador1.ouvirMusica(musica1);


        // musica1.reproduzir();
        System.out.println(musica1.musicaToString());
        //System.out.println(utilizador1);
        //System.out.println("Nome da música: " + musica1.getNome());
        //System.out.println("Intérprete: " + musica1.getInterprete());
        //System.out.println("Editora: " + musica1.getEditora());
    }
}