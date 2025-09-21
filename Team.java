import java.io.*;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Team extends Randomiser{

    //Global variable for max list size
    int MAXLISTSIZE = 14;

    //Global variable for list entry size
    int ENTRIESSIZE = 3;

    int teamID = 0;
    public Team(){
        //Declare variables for use throughout class
        Scanner scanner = new Scanner(System.in);
        char randOrSel = '#';
        char playedOrNo = '#';
        int tempUpdate = 0;
        boolean randSel = false;
        boolean validInput = false;
        String teamFile = "src/teams.csv";
        String[][] teamList = new String[MAXLISTSIZE][ENTRIESSIZE];

        //Initialise files
        loadFiles(teamList, teamFile);

        //Get whether user wants to select or get a random team
        while(!validInput) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Would You Like A Random Team(r) Or Would You Like To Select One(s)");
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
                    teamID = random(teamList);
                    randSel = true;
                } else if (playedOrNo == 'Y') {
                    teamID = randomNoPlayed(teamList);
                    randSel = true;
                }
                else{
                    System.out.println("INVALID INPUT");
                }
            }
        }
        else if(randOrSel == 'S'){
            teamID = select(teamList);
        }

        //Display the chosen team
        System.out.println("----------------------------------------");
        System.out.println("Your Chosen Team Is");
        System.out.println(teamList[teamID][1]);
        System.out.println("Times Previously Played: " + teamList[teamID][2]);
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Updating Times Played.");

        //Update the team's played stat by 1
        tempUpdate = Integer.parseInt(teamList[teamID][2]) + 1;
        teamList[teamID][2] = String.valueOf(tempUpdate);
        updateFile(teamList, teamFile);
    }

    public int random(String[][] teamList){
        //Get random number and find corresponding team
        int randomNum = (int)(Math.random() * (MAXLISTSIZE));
        String[] chosen = teamList[randomNum];
        int teamID = Integer.parseInt(chosen[0])-1;
        return teamID;
    }

    public int randomNoPlayed(String[][] teamList){
        //Create separate list for unplayed teams
        String[][] unplayedTeamList = new String[MAXLISTSIZE][ENTRIESSIZE];
        int unplayedIndex = 0;
        int teamID = 0;
        for(int i = 0; i<MAXLISTSIZE; i++){
            if(teamList[i][2].equals("0")){
                unplayedTeamList[unplayedIndex] = teamList[i];
                unplayedIndex++;
            }
        }
        //If all teams have been played then choose randomly
        if(unplayedIndex == 0){
            System.out.println("All Teams Have Been PLayed. Selecting From All");
            teamID = random(teamList);
        }
        else{
            //Get random number and find corresponding team
            int randomNum = (int)(Math.random() * (unplayedIndex-1));
            String[] chosen = unplayedTeamList[randomNum];
            teamID = Integer.parseInt(chosen[0])-1;
        }
        return teamID;
    }

    public int select(String[][] teamList){
        //Initialise necessary variables
        Scanner scanner = new Scanner(System.in);
        int teamID = 0;
        boolean validInput = false;
        boolean validID = false;

        //Loop to display all teams and their stats
        for(int i =0; i<MAXLISTSIZE; i++){
            System.out.println("----------------------------------------");
            System.out.println("Team ID: " + teamList[i][0]);
            System.out.println(teamList[i][1]);
            System.out.println("Times Previously Played: " + teamList[i][2]);
        }

        //Ask user to choose a team and verify is the input is correct
        while(!validID) {
            System.out.println("----------------------------------------");
            System.out.println("Please Enter The Team ID Of Your Chosen Team");
            try {
                teamID = Integer.parseInt(scanner.nextLine()) - 1;
                if(teamID >= 0 && teamID <MAXLISTSIZE){
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
        return teamID;
    }

    public void loadFiles(String[][] teamList, String teamFile){
        int index = 0;
        //Read file and load into teamList 2D array
        try (BufferedReader reader = new BufferedReader(new FileReader(teamFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitString = line.split(",");
                teamList[index] = splitString;
                index++;
            }
            //Errors if file loading fails
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File Not Found");
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void updateFile(String[][] teamList, String teamFile){
        //Write to file to save updated stats
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(teamFile))) {
            for (int i =0; i<MAXLISTSIZE;i++) {
                writer.write(teamList[i][0] + "," + teamList[i][1] + "," + teamList[i][2]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving teams: " + e.getMessage());
        }
    }

    public int getID(){
        return teamID;
    }
}
