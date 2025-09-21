import java.util.Scanner;
import static java.lang.Character.toUpperCase;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char chalOrVil;
        boolean valid = false;
        while (!valid) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Would You Like To Play Against A Villain(v) Or A Challenge(c)");
            System.out.println("---------------------------------------------------------------------");
            chalOrVil = scanner.nextLine().charAt(0);
            chalOrVil = toUpperCase(chalOrVil);
            if (chalOrVil == 'V') {
                Villains villains = new Villains();
                valid = true;
            } else if (chalOrVil == 'C') {
                Challenge challenge = new Challenge();
                valid = true;
            }
            else{
                System.out.println("Invalid Input: Try Again");
            }
        }
        Heroes heroes = new Heroes();
    }
    }
