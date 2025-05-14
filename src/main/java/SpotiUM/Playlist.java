package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable, ConjuntoDeMusicas {
    private String nome;
    private Utilizador criador; //null se for playlist aleatória, criada por SpotiUM, senão o utilizador em si
    private ArrayList<Musica> musicas;
    private int duracao;
    private boolean isPublica;

    public Playlist() {
        this.nome = "";
        this.criador = null;
        this.musicas = new ArrayList<>();
        this.duracao = 0;
        this.isPublica = false;
    }

    public Playlist(String nome, Utilizador criador, ArrayList<Musica> musicas, boolean isPublica) {
        setNome(nome);
        this.criador = criador;
        setMusicas(musicas);
        int dur = 0;
        for(Musica m : musicas){
            dur += m.getDuracao();
        }
        this.duracao = dur;
        this.isPublica = isPublica;
    }

    public Playlist (Playlist playlist) {
        setNome(playlist.getNome());
        this.criador = playlist.getCriador();
        setMusicas(playlist.getMusicas());
        this.duracao = playlist.getDuracao();
        this.isPublica = playlist.isPublica();
    }

    @Override
    public String getNomeGrupo() {
        return this.nome;
    }
    @Override
    public void setNomeGrupo(String nome) {
        this.nome = nome;
    }

    @Override
    public ArrayList<Musica> getMusicasList() {
        return this.musicas;
    }
    @Override
    public void setMusicasList(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }

    public Utilizador getCriador() {
        return criador;
    }
    public void setCriador(Utilizador criador) {
        this.criador = criador;
    }

    public int getDuracao() {
        return duracao;
    }
    public void setDuracao(int dur) {
        this.duracao = dur;
    }

    public boolean isPublica() {
        return this.isPublica;
    }

    @Override
    public String toString(){
        String estado = isPublica ? "Pública" : "Privada";
        return "Playlist: " + nome + "\nCriada por: " + criador.getNome() + "\n"+ estado + "\n" + this.printTitulos();
    }

    @Override
    public Playlist clone () {
        return new Playlist(this);
    }
    @Override
    public Playlist copy() {
        return clone();
    }
}
