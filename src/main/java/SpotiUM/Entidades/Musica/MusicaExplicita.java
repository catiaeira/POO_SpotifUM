package SpotiUM.Entidades.Musica;

public class MusicaExplicita extends Musica implements IMusicaExplicita {

    public MusicaExplicita(){
        super();
    }

    public MusicaExplicita(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes) {
        super(nome, interprete, editora, letra, genero, musica, duracaoSegs, nReproducoes);
    }

    public MusicaExplicita(MusicaExplicita m){
        super(m);
    }

    @Override
    public boolean isExplicita() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTipo: Música Explícita";
    }

    @Override
    public MusicaExplicita clone() {
        return new MusicaExplicita(this);
    }
}
