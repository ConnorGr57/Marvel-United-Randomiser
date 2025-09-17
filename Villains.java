import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Villains {

    public void Villains(){
        Scanner scanner = new Scanner(System.in);
        char randOrSel;
        int villainID = 0;
        String villainFile = "src/villains.csv";
        System.out.println("Would You Like A Random Villain(r) Or Would You Like To Choose One(s)");
        randOrSel = scanner.nextLine().charAt(0);
        String[][] villainlist = new String[32][3];
        loadfiles(villainlist, villainFile);
        if(randOrSel == 'r'){
            villainID = random(villainlist);
        }

        System.out.println("----------------------------------------");
        System.out.println("Your Chosen Villain Is");
        System.out.println(villainlist[villainID][1]);
        System.out.println("Times Previously Played: " + villainlist[villainID][2]);
    }

    public int random(String[][] villainlist){
        int randomNum = (int)(Math.random() * 31);
        String[] chosen = villainlist[randomNum];

        int villainID = Integer.parseInt(chosen[0]);

        return villainID;
    }

    //public void select{

    //}

    public void loadfiles(String[][] villainlist, String villainFile){
        int index = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(villainFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] splitString = line.split(",");
                    villainlist[index] = splitString;
                    index++;
                }
            } catch (FileNotFoundException e) {
                System.out.println("No player data found. Will create file on save.");
            } catch (IOException e) {
                System.out.println("Error loading players: " + e.getMessage());
            }
        }

    }

