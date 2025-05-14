package SpotiUM.MVC;

import SpotiUM.*;

import java.util.ArrayList;

public class PlaybackController {
    private SpotView view;
    private ArrayList<Musica> musicas;
    private volatile int cur; // Current song index
    private volatile Thread playbackThread;
    private volatile boolean isPlaying = false;
    private volatile boolean avancar = true;

    public PlaybackController(SpotView view, ArrayList<Musica> musicas) {
        this.view = view;
        this.musicas = musicas;
        this.cur = 0;
    }

    public synchronized void play() {
        if (cur >= musicas.size()) {
            view.printMensagem("Fim da lista de reprodução.");
            return;
        }

        if (playbackThread != null && playbackThread.isAlive()) {
            stop(); // pára alguma reprodução que esteja a decorrer
        }

        Musica current = musicas.get(cur);
        view.ouvirMusica(current.getNome());

        isPlaying = true;
        playbackThread = new Thread(() -> {
            try {
                imprimirLetra(current);

                if (avancar) {
                    synchronized (this) {
                        if (isPlaying) {
                            cur++; // passa para a música seguinte no array dado
                            play();
                        }
                    }
                }
            } catch (Exception e) {
                view.mostraMensagemErro("Erro na reprodução: ", e);
            } finally {
                isPlaying = false;
            }
        });
        playbackThread.start();
    }

    public synchronized void stop() {
        avancar = false; // desativa reprodução automática
        if (playbackThread != null) {
            playbackThread.interrupt();
        }
        isPlaying = false;
    }

    public synchronized void forward() {
        avancar = true; // permite reprodução automática depois de um skip
        if (cur < musicas.size() - 1) {
            cur++;
            play();
        } else {
            view.printMensagem("Não há mais músicas para avançar.");
        }
    }

    public synchronized void back() {
        avancar = true; // permite reprodução automática depois de um rewind
        if (cur > 0) {
            cur--;
            play();
        } else {
            view.printMensagem("Não há músicas anteriores.");
        }
    }

    public void imprimirLetra(Musica m){
        String letra = m.getLetra();

        letra = letra.replace("\\n", "\n");
        String[] linhas = letra.split("\n");

        //idealmente:
        int durSegundos = m.getDuracao();
        int atraso = durSegundos * 1000 / linhas.length; // atraso em ms para o Thread.sleep()
        //int atraso = 30000 / linhas.length; //cada musica demora 30 segundos

        for (String l : linhas) {
            try {
                Thread.sleep(atraso);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                return;
            }
            this.view.printMensagem(l);
        }

        m.reproduzir();
    }
}
