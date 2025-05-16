package SpotiUM;

public class MusicaExplicitaMultimedia extends Musica implements IMusicaMultimedia, IMusicaExplicita {
    private String videoLink;

    public MusicaExplicitaMultimedia(){
        super();
        this.videoLink = "";
    }

    public MusicaExplicitaMultimedia(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes, String link) {
        super(nome, interprete, editora, letra, genero, musica, duracaoSegs, nReproducoes);
        this.videoLink = link;
    }

    public MusicaExplicitaMultimedia(MusicaExplicitaMultimedia m){
        super(m);
        this.videoLink = getVideoLink();
    }

    public String getVideoLink(){
        return videoLink;
    }
    public void setVideoLink(String link){
        this.videoLink = link;
    }

    @Override
    public boolean isMultimedia() {
        return true;
    }

    @Override
    public boolean isExplicita() {
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
