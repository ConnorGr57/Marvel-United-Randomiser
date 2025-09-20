import java.io.*;
import java.util.Scanner;
import static java.lang.Character.toUpperCase;

public class Challenge extends Randomiser{

    int MAXLISTSIZE = 3;
    int ENTRIESSIZE = 2;
    public Challenge(){
        //Declare variables for use throughout class
        Scanner scanner = new Scanner(System.in);
        char randOrSel;
        char playedOrNo;
        int challengeID = 0;
        int tempUpdate = 0;
        boolean randSel = false;
        String challengeFile = "src/challenge.csv";
        String[][] challengeList = new String[MAXLISTSIZE][ENTRIESSIZE];

        //Initialise files
        loadFiles(challengeList, challengeFile);

        //Get whether user wants to select or get a random challenge
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Would You Like A Random Challenge(r) Or Would You Like To Choose One(s)");
        randOrSel = scanner.nextLine().charAt(0);
        randOrSel = toUpperCase(randOrSel);

        //Either randomize or select depending on user choice
        if(randOrSel == 'R'){
            while(!randSel) {

                //Get wether they want any or one they have not played
                System.out.println("---------------------------------------------------------------------");
                System.out.println("Would you like one you haven't played(y) or not(n)");
                System.out.println("---------------------------------------------------------------------");
                playedOrNo = scanner.nextLine().charAt(0);
                playedOrNo = toUpperCase(playedOrNo);

                //Select randomiser method depending on user choice
                if (playedOrNo == 'N') {
                    challengeID = random(challengeList);
                    randSel = true;
                } else if (playedOrNo == 'Y') {
                    challengeID = randomNoPlayed(challengeList);
                    randSel = true;
                }
                else{
                    System.out.println("INVALID INPUT");
                }
            }
        }
        else if(randOrSel == 'S'){
            challengeID = select(challengeList);
        }
        //Display the chosen challenge
        System.out.println("----------------------------------------");
        System.out.println("Your Chosen Challenge Is");
        System.out.println(challengeList[challengeID][1]);
        System.out.println("Times Previously Played: " + challengeList[challengeID][2]);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Updating Times Played.");
        System.out.println("---------------------------------------------------------------------");

        //Update the challenge's played stat by 1
        tempUpdate = Integer.parseInt(challengeList[challengeID][2]) + 1;
        challengeList[challengeID][2] = String.valueOf(tempUpdate);
        updateFile(challengeList, challengeFile);
    }

    public int random(String[][] challengeList){
        //Get random number and find corresponding challenge
        int randomNum = (int)(Math.random() * (MAXLISTSIZE));
        String[] chosen = challengeList[randomNum];
        int challengeID = Integer.parseInt(chosen[0])-1;
        return challengeID;
    }

    public int randomNoPlayed(String[][] challengeList){
        //Create separate list for unplayed challenges
        String[][] unplayedChallengeList = new String[MAXLISTSIZE][ENTRIESSIZE];
        int unplayedIndex = 0;
        int challengeID = 0;
        for(int i = 0; i<MAXLISTSIZE; i++){
            if(challengeList[i][2].equals("0")){
                unplayedChallengeList[unplayedIndex] = challengeList[i];
                unplayedIndex++;
            }
        }
        //If all challenges have been played then choose randomly
        if(unplayedIndex == 0){
            System.out.println("All Challenges Have Been PLayed. Selecting From All");
            challengeID = random(challengeList);
        }
        else{
            //Get random number and find corresponding challenge
            int randomNum = (int)(Math.random() * (unplayedIndex-1));
            String[] chosen = unplayedChallengeList[randomNum];
            challengeID = Integer.parseInt(chosen[0])-1;
        }
        return challengeID;
    }

    public int select(String[][] challengeList){
        //Initialise necessary variables
        Scanner scanner = new Scanner(System.in);
        int challengeID = 0;
        boolean validInput = false;
        boolean validID = false;

        //Loop to display all challenges and their stats
        for(int i =0; i<MAXLISTSIZE; i++){
            System.out.println("----------------------------------------");
            System.out.println("Challenge ID: " + challengeList[i][0]);
            System.out.println(challengeList[i][1]);
            System.out.println("Times Previously Played: " + challengeList[i][2]);
        }

        //Ask user to choose a challenge and verify is the input is correct
        while(!validID) {
            System.out.println("----------------------------------------");
            System.out.println("Please Enter The Challenge ID Of Your Chosen Challenge");
            try {
                challengeID = Integer.parseInt(scanner.nextLine()) - 1;
                if(challengeID >= 0 && challengeID <MAXLISTSIZE){
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
        return challengeID;
    }

    public void loadFiles(String[][] challengeList, String challengeFile){
        int index = 0;
        //Read file and load into challengeList 2D array
        try (BufferedReader reader = new BufferedReader(new FileReader(challengeFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitString = line.split(",");
                challengeList[index] = splitString;
                index++;
            }
            //Errors if file loading fails
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File Not Found");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void updateFile(String[][] challengeList, String challengeFile){
        //Write to file to save updated stats
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(challengeFile))) {
            for (int i =0; i<MAXLISTSIZE;i++) {
                writer.write(challengeList[i][0] + "," + challengeList[i][1] + "," + challengeList[i][2]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving challenges: " + e.getMessage());
        }
    }

}


