package SpotiUM;

import SpotiUM.MVC.UserInput;

import java.io.*;

public class Utils {

    public static boolean confirmarSobrescritaSeNecessario(String ficheiro) {
        File f = new File(ficheiro);
        if (f.exists()) {
            System.out.println("Aviso: O ficheiro já existe e será sobrescrito. Continuar?");
            UserInput input = new UserInput();
            int overwrite = input.lerInt("1. Para sobrescrever, 2 Para voltar: "
                    , "1 ou 2 apenas", i -> i == 1 || i == 2);
            return overwrite != 2;
        }
        return true;
    }

    public static boolean guardarObjeto(Object obj, String ficheiro) throws IOException {
        if (!Utils.confirmarSobrescritaSeNecessario(ficheiro)) return false;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ficheiro))) {
            oos.writeObject(obj);
        }
        return true;
    }

    public static <T> T carregarEstado( Class<T> classe, String ficheiro) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ficheiro))) {
            Object obj = ois.readObject();
            if (!classe.isInstance(obj)) throw new IOException("Tipo inesperado no ficheiro: " + ficheiro);

            return classe.cast(obj);
        }
    }
}
