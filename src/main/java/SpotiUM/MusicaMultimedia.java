package SpotiUM;

public class MusicaMultimedia extends Musica{

    public MusicaMultimedia(){
        super();
    }

    public MusicaMultimedia(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes) {
        super(nome, interprete, editora, letra, musica, genero, duracaoSegs, nReproducoes);
    }

    public MusicaMultimedia(MusicaMultimedia m){
        super(m);
    }

    @Override
    public boolean isMultimedia() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTipo: Multimédia";
    }

    @Override
    public MusicaMultimedia clone(){
        return new MusicaMultimedia(this);
    }
}
