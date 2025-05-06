package SpotiUM;

public class MusicaExplicita extends Musica{

    public MusicaExplicita(){
        super();
    }

    public MusicaExplicita(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes) {
        super(nome, interprete, editora, letra, musica, genero, duracaoSegs, nReproducoes);
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
        return super.toString() + "\n⚠️ Tipo: Música Explícita";
    }

    @Override
    public MusicaExplicita clone() {
        return new MusicaExplicita(this);
    }
}
