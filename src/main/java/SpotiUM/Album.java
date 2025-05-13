package SpotiUM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Album implements Serializable {
    private String nome;
    private String artista;
    private ArrayList<Musica> musicas;

    public Album(String nome, String artista, ArrayList<Musica> musicas) {
        this.nome = nome;
        this.artista = artista;
        this.musicas = new ArrayList<>(musicas);
    }
    public Album (Album album) {
        this.nome = album.getNome();
        this.artista = album.getArtista();
        this.musicas = album.getMusicas();
    }

    public String getNome() { return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getArtista() {return artista;}
    public void setArtista(String artista) {this.artista = artista;}

    public ArrayList<Musica> getMusicas() {return new ArrayList<>(musicas);}

    public void setMusicas(ArrayList<Musica> musicas) {this.musicas = new ArrayList<>(musicas);}

    public List<Musica> getMusicasPeloNome(String nome) {
        return this.musicas.stream()
                .filter(m-> m.getNome().equalsIgnoreCase(nome))
                .toList();
    }
    public String printTitulos() {
        ArrayList<Musica> m = this.musicas;
        StringBuilder sb = new StringBuilder();
        m.forEach(musica -> {
            if (sb.length() > 0) sb.append("\n");
            sb.append(musica.getNome());
        });
        return sb.toString();
    }

    @Override
    public Album clone () {
        return new Album(this);
    }

    @Override
    public String toString(){
        return "Álbum: " + nome + "\nCriada por: " + artista + "\n" + printTitulos();
    }


    public void adicionarMusica(Musica musica) {this.musicas.add(musica);}
    public void removerMusica (Musica musica) {this.musicas.remove(musica);}

}
