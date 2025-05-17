package SpotiUM.Entidades.Musica;

public interface IMusicaMultimedia {
    default boolean isMultimedia() {
        return true;
    }
    String getVideoLink();
    void setVideoLink(String link);
}
