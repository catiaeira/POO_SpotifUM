package SpotiUM;

public class MusicaExplicitaMultimedia extends MusicaExplicita{

    public MusicaExplicitaMultimedia(){
        super();
    }

    public MusicaExplicitaMultimedia(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes) {
        super(nome, interprete, editora, letra, musica, genero, duracaoSegs, nReproducoes);
    }

    public MusicaExplicitaMultimedia(MusicaExplicitaMultimedia m){
        super(m);
    }

    @Override
    public boolean isMultimedia() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTipo: Multimédia Explícita";
    }

    @Override
    public MusicaExplicitaMultimedia clone(){
        return new MusicaExplicitaMultimedia(this);
    }
}
