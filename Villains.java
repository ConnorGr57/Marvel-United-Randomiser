import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import static java.lang.Character.toUpperCase;

public class Villains {
    int MAXLISTSIZE = 32;
    int ENTRIESSIZE = 3;
    public Villains(){
        Scanner scanner = new Scanner(System.in);
        char randOrSel;
        char playedOrNo;
        int villainID = 0;
        boolean randSel = false;
        String villainFile = "src/villains.csv";
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Would You Like A Random Villain(r) Or Would You Like To Choose One(s)");
        randOrSel = scanner.nextLine().charAt(0);
        randOrSel = toUpperCase(randOrSel);
        String[][] villainlist = new String[MAXLISTSIZE][ENTRIESSIZE];
        loadfiles(villainlist, villainFile);
        if(randOrSel == 'R'){
            while(!randSel) {
                System.out.println("---------------------------------------------------------------------");
                System.out.println("Would you like one you haven't played(y) or not(n)");
                System.out.println("---------------------------------------------------------------------");
                playedOrNo = scanner.nextLine().charAt(0);
                playedOrNo = toUpperCase(playedOrNo);
                if (playedOrNo == 'N') {
                    villainID = random(villainlist);
                    randSel = true;
                } else if (playedOrNo == 'Y') {
                    villainID = randomNoPlayed(villainlist);
                    randSel = true;
                }
                else{
                    System.out.println("INVALID INPUT");
                }
            }
        }
        else if(randOrSel == 'S'){
            villainID = select(villainlist);
        }
        System.out.println("----------------------------------------");
        System.out.println("Your Chosen Villain Is");
        System.out.println(villainlist[villainID][1]);
        System.out.println("Times Previously Played: " + villainlist[villainID][2]);
    }

    public int random(String[][] villainlist){
        int randomNum = (int)(Math.random() * (MAXLISTSIZE-1));
        String[] chosen = villainlist[randomNum];

        int villainID = Integer.parseInt(chosen[0])-1;

        return villainID;
    }

    public int randomNoPlayed(String[][] villainlist){
        String[][] unplayedVillainList = new String[MAXLISTSIZE][ENTRIESSIZE];
        int unplayedIndex = 0;
        int villainID = 0;
        for(int i = 0; i<MAXLISTSIZE; i++){
            if(villainlist[i][2].equals("0")){
                unplayedVillainList[unplayedIndex] = villainlist[i];
                unplayedIndex++;
            }
        }
        if(unplayedIndex == 0){
            System.out.println("All Villains Have Been PLayed. Selecting From All");
            villainID = random(villainlist);
        }
        else{
            int randomNum = (int)(Math.random() * (unplayedIndex-1));
            String[] chosen = unplayedVillainList[randomNum];
            villainID = Integer.parseInt(chosen[0])-1;
        }
        return villainID;
    }

    public int select(String[][] villainList){
        Scanner scanner = new Scanner(System.in);
        int villainID = 0;
        boolean validInput = false;
        boolean validID = false;
        for(int i =0; i<MAXLISTSIZE; i++){
            System.out.println("----------------------------------------");
            System.out.println("Villain ID: " + villainList[i][0]);
            System.out.println(villainList[i][1]);
            System.out.println("Times Previously Played: " + villainList[i][2]);
        }
        while(!validID) {
                System.out.println("----------------------------------------");
                System.out.println("Please Enter The Villain ID Of Your Chosen Villain");
                try {
                    villainID = Integer.parseInt(scanner.nextLine()) - 1;
            if(villainID >= 0 && villainID <MAXLISTSIZE){
                validID = true;
            }
            else {
                System.out.println("----------------------------------------");
                System.out.println("Invalid ID. Try Again");
            }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
        }
        return villainID;
    }

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
                System.out.println("ERROR: File Not Found");
            } catch (IOException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        }

    }
