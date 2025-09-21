import java.io.*;
import java.util.Scanner;
import static java.lang.Character.toUpperCase;
public class Heroes{

    //Global variables for max list sizes
    int MAXLISTSIZE = 66;

    //Global variables for list entry sizes
    int ENTRIESSIZE = 16;

    public Heroes(){
        //Declare variables for use throughout class
        Scanner scanner = new Scanner(System.in);
        char randOrSelOrTeam = '#';
        char playedOrNo = '#';
        int tempUpdate = 0;
        boolean randSelandTeam = false;
        int numOfHeroes = 1;
        boolean validInput = false;
        String heroFile = "src/heroes.csv";
        String[][] heroList = new String[MAXLISTSIZE][ENTRIESSIZE];

        //Initialise files
        loadFiles(heroList, heroFile);

        //Get how many heroes the user wants
        while(!validInput) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("How many heroes would you like (1-4)");
            try {
                numOfHeroes = Integer.parseInt(scanner.nextLine());
                if (numOfHeroes > 0 && numOfHeroes < 5) {
                    validInput = true;
                } else {
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Invalid Number: Try Again");
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid Input: Please Enter A Number");
            }
        }
        int[] heroID = new int[numOfHeroes];

        //Get whether user wants to select a hero, get a random hero or use a team deck
        validInput = false;
        while(!validInput) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Would You Like A Random Hero(r), Would You Like To Select One(s) Or Would You Like To Use A Team Deck(t)");
            randOrSelOrTeam = scanner.nextLine().charAt(0);
            randOrSelOrTeam = toUpperCase(randOrSelOrTeam);
            if(randOrSelOrTeam == 'R' || randOrSelOrTeam == 'S' || randOrSelOrTeam == 'T'){
                validInput = true;
            }
            else{
                System.out.println("---------------------------------------------------------------------");
                System.out.println("Invalid Input: Try Again");
            }
        }

        //Either randomize, select or go to team decks depending on user choice
        if(randOrSelOrTeam == 'R'){
            while(!randSelandTeam) {

                //Get weather they want any or one they have not played
                validInput = false;
                while(!validInput) {
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Would you like some you haven't played(y) or not(n)");
                    System.out.println("---------------------------------------------------------------------");
                    playedOrNo = scanner.nextLine().charAt(0);
                    playedOrNo = toUpperCase(playedOrNo);
                    if(playedOrNo == 'Y' || playedOrNo == 'N'){
                        validInput = true;
                    }
                    else{
                        System.out.println("---------------------------------------------------------------------");
                        System.out.println("Invalid Input: Try Again");
                    }
                }

                //Select randomiser method depending on user choice
                if (playedOrNo == 'N') {
                    heroID = random(heroList, numOfHeroes);
                    randSelandTeam = true;
                } else if (playedOrNo == 'Y') {
                    heroID = randomNoPlayed(heroList, numOfHeroes);
                    randSelandTeam = true;
                }
                else{
                    System.out.println("INVALID INPUT");
                }
            }
        }
        else if(randOrSelOrTeam == 'S'){
            heroID = select(heroList, numOfHeroes);
        }
        else if(randOrSelOrTeam == 'T'){

        }
        //Display the chosen heroes
        for(int i=0; i<numOfHeroes; i++) {
            System.out.println("----------------------------------------");
            System.out.println("Your Chosen Hero Number "+ (i+1) +" Is");
            System.out.println(heroList[heroID[i]][1]);
            System.out.println("Times Previously Played: " + heroList[heroID[i]][2]);
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Updating Times Played.");
        System.out.println("---------------------------------------------------------------------");

        //Update the challenge's played stat by 1
        for(int i=0; i<numOfHeroes; i++){
            tempUpdate = Integer.parseInt(heroList[heroID[i]][2]) + 1;
            heroList[heroID[i]][2] = String.valueOf(tempUpdate);
        }
        updateFile(heroList, heroFile);
    }

    public int[] random(String[][] heroList, int numOfHeroes){
        int[] heroID = new int[numOfHeroes];
        for(int i=0; i<numOfHeroes;i++){
            //Get random number and find corresponding hero
            int randomNum = (int)(Math.random() * (MAXLISTSIZE));
            String[] chosen = heroList[randomNum];
            heroID[i] = Integer.parseInt(chosen[0])-1;
        }
        return heroID;
    }

    public int[] randomNoPlayed(String[][] heroList, int numOfHeroes){
        //Create separate list for unplayed heroes
        String[][] unplayedHeroList = new String[MAXLISTSIZE][ENTRIESSIZE];
        int unplayedIndex = 0;
        int[] heroID = new int[numOfHeroes];
        for(int i = 0; i<MAXLISTSIZE; i++){
            if(heroList[i][2].equals("0")){
                unplayedHeroList[unplayedIndex] = heroList[i];
                unplayedIndex++;
            }
        }
        //If all heroes have been played then choose randomly
        if(unplayedIndex == 0){
            System.out.println("All Heroes Have Been PLayed. Selecting From All");
            heroID = random(heroList, numOfHeroes);
        }
        else{
            for(int i=0; i<numOfHeroes;i++) {
                //Get random number and find corresponding hero
                int randomNum = (int) (Math.random() * (unplayedIndex - 1));
                String[] chosen = unplayedHeroList[randomNum];
                heroID[i] = Integer.parseInt(chosen[0]) - 1;
            }
        }
        return heroID;
    }

    public int[] select(String[][] heroList, int numOfHeroes){
        //Initialise necessary variables
        Scanner scanner = new Scanner(System.in);
        int[] heroID = new int[numOfHeroes];
        boolean validInput = false;
        boolean validID = false;

        //Loop to display all heroes and their stats
        for(int i =0; i<MAXLISTSIZE; i++){
            System.out.println("----------------------------------------");
            System.out.println("Hero ID: " + heroList[i][0]);
            System.out.println(heroList[i][1]);
            System.out.println("Times Previously Played: " + heroList[i][2]);
        }

        //Ask user to choose a hero and verify is the input is correct
        System.out.println("----------------------------------------");
        System.out.println("Please Enter " + numOfHeroes + "Hero ID's Of Your Choice");
        for (int i = 0; i < numOfHeroes; i++) {
            validID = false;
        while(!validID) {
                try {
                    heroID[i] = Integer.parseInt(scanner.nextLine()) - 1;
                    if (heroID[i] >= 0 && heroID[i] < MAXLISTSIZE) {
                        validID = true;
                    } else {
                        System.out.println("----------------------------------------");
                        System.out.println("Invalid ID. Try Again");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
        }
        return heroID;
    }

    public void loadFiles(String[][] heroList, String heroFile){
        int index = 0;
        //Read file and load into challengeList 2D array
        try (BufferedReader reader = new BufferedReader(new FileReader(heroFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitString = line.split(",");
                heroList[index] = splitString;
                index++;
            }
            //Errors if file loading fails
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File Not Found");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void updateFile(String[][] heroList, String heroFile){
        //Write to file to save updated stats
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(heroFile))) {
            for (int i =0; i<MAXLISTSIZE;i++) {
                writer.write(heroList[i][0] + "," + heroList[i][1] + "," + heroList[i][2] + "," + heroList[i][3] + "," + heroList[i][4] + "," + heroList[i][5] + "," + heroList[i][6] + "," + heroList[i][7] + "," + heroList[i][8] + "," + heroList[i][9] + "," + heroList[i][10] + "," + heroList[i][11] + "," + heroList[i][12] + "," + heroList[i][13] + "," + heroList[i][14] + "," + heroList[i][15]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving challenges: " + e.getMessage());
        }
    }
}
