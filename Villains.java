import java.io.*;
import java.util.Scanner;
import static java.lang.Character.toUpperCase;

public class Villains extends Randomiser{

    //Global variable for max list size
    int MAXLISTSIZE = 32;

    //Global variable for list entry size
    int ENTRIESSIZE = 3;
    public Villains(){
        //Declare variables for use throughout class
        Scanner scanner = new Scanner(System.in);
        char randOrSel = '#';
        char playedOrNo = '#';
        int villainID = 0;
        int tempUpdate = 0;
        boolean randSel = false;
        boolean validInput = false;
        String villainFile = "src/villains.csv";
        String[][] villainlist = new String[MAXLISTSIZE][ENTRIESSIZE];

        //Initialise files
        loadFiles(villainlist, villainFile);

        //Get whether user wants to select or get a random villain
        while(!validInput) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Would You Like A Random Villain(r) Or Would You Like To Select One(s)");
            randOrSel = scanner.nextLine().charAt(0);
            randOrSel = toUpperCase(randOrSel);
            if(randOrSel == 'R' || randOrSel == 'S'){
                validInput = true;
            }
            else{
                System.out.println("Invalid Input: Try Again");
            }
        }

        //Either randomize or select depending on user choice
        if(randOrSel == 'R'){
            while(!randSel) {

                //Get wether they want any or one they have not played
                validInput = false;
                while(!validInput) {
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Would you like one you haven't played(y) or not(n)");
                    System.out.println("---------------------------------------------------------------------");
                    playedOrNo = scanner.nextLine().charAt(0);
                    playedOrNo = toUpperCase(playedOrNo);
                    if(playedOrNo == 'Y' || playedOrNo == 'N'){
                        validInput = true;
                    }
                    else{
                        System.out.println("Invalid Input: Try Again");
                    }
                }

                //Select randomiser method depending on user choice
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

        //Display the chosen villain
        System.out.println("----------------------------------------");
        System.out.println("Your Chosen Villain Is");
        System.out.println(villainlist[villainID][1]);
        System.out.println("Times Previously Played: " + villainlist[villainID][2]);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Updating Times Played.");

        //Update the villain's played stat by 1
        tempUpdate = Integer.parseInt(villainlist[villainID][2]) + 1;
        villainlist[villainID][2] = String.valueOf(tempUpdate);
        updateFile(villainlist, villainFile);
    }

    public int random(String[][] villainlist){
        //Get random number and find corresponding villains
        int randomNum = (int)(Math.random() * (MAXLISTSIZE));
        String[] chosen = villainlist[randomNum];
        int villainID = Integer.parseInt(chosen[0])-1;
        return villainID;
    }

    public int randomNoPlayed(String[][] villainlist){
        //Create separate list for unplayed villains
        String[][] unplayedVillainList = new String[MAXLISTSIZE][ENTRIESSIZE];
        int unplayedIndex = 0;
        int villainID = 0;
        for(int i = 0; i<MAXLISTSIZE; i++){
            if(villainlist[i][2].equals("0")){
                unplayedVillainList[unplayedIndex] = villainlist[i];
                unplayedIndex++;
            }
        }
        //If all villains have been played then choose randomly
        if(unplayedIndex == 0){
            System.out.println("All Villains Have Been PLayed. Selecting From All");
            villainID = random(villainlist);
        }
        else{
            //Get random number and find corresponding villain
            int randomNum = (int)(Math.random() * (unplayedIndex-1));
            String[] chosen = unplayedVillainList[randomNum];
            villainID = Integer.parseInt(chosen[0])-1;
        }
        return villainID;
    }

    public int select(String[][] villainList){
        //Initialise necessary variables
        Scanner scanner = new Scanner(System.in);
        int villainID = 0;
        boolean validInput = false;
        boolean validID = false;

        //Loop to display all villains and their stats
        for(int i =0; i<MAXLISTSIZE; i++){
            System.out.println("----------------------------------------");
            System.out.println("Villain ID: " + villainList[i][0]);
            System.out.println(villainList[i][1]);
            System.out.println("Times Previously Played: " + villainList[i][2]);
        }

        //Ask user to choose a villain and verify is the input is correct
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

    public void loadFiles(String[][] villainlist, String villainFile){
        int index = 0;
        //Read file and load into villainList 2D array
            try (BufferedReader reader = new BufferedReader(new FileReader(villainFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] splitString = line.split(",");
                    villainlist[index] = splitString;
                    index++;
                }
                //Errors if file loading fails
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: File Not Found");
            } catch (IOException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        }

        public void updateFile(String[][] villainList, String villainFile){
        //Write to file to save updated stats
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(villainFile))) {
                for (int i =0; i<MAXLISTSIZE;i++) {
                    writer.write(villainList[i][0] + "," + villainList[i][1] + "," + villainList[i][2]);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving villains: " + e.getMessage());
            }
        }
    }
