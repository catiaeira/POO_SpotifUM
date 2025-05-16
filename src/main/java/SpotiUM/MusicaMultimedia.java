package SpotiUM;

public class MusicaMultimedia extends Musica implements IMusicaMultimedia {
    private String videoLink;

    public MusicaMultimedia(){
        super();
        this.videoLink = "";
    }

    public MusicaMultimedia(String nome, String interprete, String editora, String letra, String genero, String musica, int duracaoSegs, int nReproducoes, String link) {
        super(nome, interprete, editora, letra, genero, musica, duracaoSegs, nReproducoes);
        this.videoLink = link;
    }

    public MusicaMultimedia(MusicaMultimedia m){
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
    public String toString() {
        return super.toString() + "\nTipo: Multimédia";
    }

    @Override
    public MusicaMultimedia clone(){
        return new MusicaMultimedia(this);
    }
}
