package SpotiUM.Entidades.Musica;

public interface IMusicaExplicita {
    default boolean isExplicita() {
        return true;
    }
}
