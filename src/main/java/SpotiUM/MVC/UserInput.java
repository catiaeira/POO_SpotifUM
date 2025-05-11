package SpotiUM.MVC;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public class UserInput {
    private Scanner scanner;
    public UserInput() {
        this.scanner = new Scanner(System.in);
    }
    public Object ler(String                    prompt,
                       String erro,
                       Predicate<String>        validate,
                       Function<String, Object> convert) {

        String ret = null;
        do {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (validate.test(line)) ret = line;
            else System.out.println(erro);
        } while (ret == null);
        return convert.apply(ret);
    }

    public String lerString (String prompt) {
        return (String) this.ler(prompt,null, s -> true, s -> s);
    }

    public String lerString (String prompt, String erro, Predicate<String> validate) {
        return (String) this.ler(prompt, erro, validate, s -> s);
    }

    public int lerInt(String prompt, String erro, Predicate<Integer> validate) {
        return (Integer) this.ler(prompt, erro, s -> {
            try {
                int i = Integer.parseInt(s);
                return validate.test(i);
            } catch (NumberFormatException e) {
                return false;
            }
        }, Integer::parseInt);
    }

}
