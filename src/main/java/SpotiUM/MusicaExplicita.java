package SpotiUM;

public class MusicaExplicita extends Musica{

    public MusicaExplicita(String nome, String interprete, String editora, String letra, String musica, String genero, int duracao) {
        super(nome, interprete, editora, letra, musica, genero, duracao);
    }

    @Override
    public boolean isExplicita() {
        return true;
    }

    @Override
    public String musicaToString() {
        return super.musicaToString() + "\n⚠️ Tipo: Música Explícita";
    }

}
