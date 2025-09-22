import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import static java.lang.Character.toUpperCase;
public class Heroes{

    //Global variables for max list sizes
    int MAXLISTSIZE = 66;

    //Global variables for list entry sizes
    int ENTRIESSIZE = 17;

    public Heroes(){
        //Declare variables for use throughout class
        Scanner scanner = new Scanner(System.in);
        char randOrSelOrTeam = '#';
        char teamRandOrSel = '#';
        char playedOrNo = '#';
        char teamPlayedOrNo = '#';
        int tempUpdate = 0;
        int teamID;
        int teamIndex = 0;
        boolean randSelandTeam = false;
        int numOfHeroes = 1;
        boolean validInput = false;
        String heroFile = "src/heroes.csv";
        String[][] heroList = new String[MAXLISTSIZE][ENTRIESSIZE];
        String[][] teamHeroList = new String[MAXLISTSIZE][ENTRIESSIZE];

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
        int[] randoms = new int[numOfHeroes];

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
                    heroID = random(heroList, numOfHeroes, MAXLISTSIZE);
                    randSelandTeam = true;
                } else if (playedOrNo == 'Y') {
                    heroID = randomNoPlayed(heroList, numOfHeroes, MAXLISTSIZE);
                    randSelandTeam = true;
                }
                else{
                    System.out.println("INVALID INPUT");
                }
            }
        }
        else if(randOrSelOrTeam == 'S'){
            heroID = select(heroList, numOfHeroes, MAXLISTSIZE);
        }
        else if(randOrSelOrTeam == 'T'){
            //Get team from team class
            Team team = new Team();
            teamID = team.getID();
            teamIndex = getTeamHeroes(heroList,teamID,teamHeroList);
            validInput=false;
            while(!validInput) {
                System.out.println("----------------------------------------");
                System.out.println("Would You Like To Choose Random Heroes From Your Team(r) Or Select Them(s)");
                teamRandOrSel = scanner.nextLine().charAt(0);
                teamRandOrSel = toUpperCase(teamRandOrSel);
                if(teamRandOrSel == 'R' || teamRandOrSel == 'S'){
                    validInput = true;
                }
                else{
                    System.out.println("Invalid Input: Try Again");
                }
            }
            //Get random heroes or let select depending on choice
            if(teamRandOrSel == 'R'){
                validInput = false;
                while(!validInput){
                    System.out.println("----------------------------------------");
                    System.out.println("Would You Like Heroes You Haven't Played(y) Or Not(n)");
                    teamPlayedOrNo = scanner.nextLine().charAt(0);
                    teamPlayedOrNo = toUpperCase(teamPlayedOrNo);
                    if(teamPlayedOrNo == 'Y' || teamPlayedOrNo == 'N'){
                        validInput = true;
                    }
                    else{
                        System.out.println("Invalid Input: Try Again");
                    }
                }
                if(teamPlayedOrNo == 'Y'){
                    heroID = randomNoPlayed(teamHeroList,numOfHeroes,(teamIndex+1));
                }
                else if(teamPlayedOrNo == 'N'){
                    heroID = random(teamHeroList,numOfHeroes,(teamIndex+1));
                }
            }
            else if(teamRandOrSel == 'S'){
                heroID = select(teamHeroList,numOfHeroes,teamIndex);
            }

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

        //Update the hero's played stat by 1
        for(int i=0; i<numOfHeroes; i++){
            tempUpdate = Integer.parseInt(heroList[heroID[i]][2]) + 1;
            heroList[heroID[i]][2] = String.valueOf(tempUpdate);
        }
        updateFile(heroList, heroFile);
    }

    public int[] random(String[][] heroList, int numOfHeroes, int listSize){
        int[] heroID = new int[numOfHeroes];
        boolean newRand = false;
        int randomNum = 0;
        ArrayList<Integer> possibleIDs = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            possibleIDs.add(i);
        }
        Collections.shuffle(possibleIDs);
        for (int i = 0; i < numOfHeroes; i++) {
            randomNum = possibleIDs.get(i);
            heroID[i] = Integer.parseInt(heroList[randomNum][0]) - 1;
        }
        return heroID;
    }


    public int getTeamHeroes(String[][] heroList, int teamID, String[][] teamHeroList){
        //Create new list for heroes of chosen team
        int teamIndex = 0;
        for(int i=0; i<MAXLISTSIZE; i++){
            if(heroList[i][teamID+3].equals("1")){
                teamHeroList[teamIndex] = heroList[i];
                teamIndex++;
            }
        }
        teamIndex--;
        return teamIndex;
    }

    public int[] randomNoPlayed(String[][] heroList, int numOfHeroes, int listSize){
        //Create separate list for unplayed heroes
        ArrayList<String[]> unplayedHeroList = new ArrayList<>();
        int[] heroID = new int[numOfHeroes];
        String[][] played = new String[listSize][];
        int[] heroID1;
        int count=0;
        int i2=0;

        for(int i = 0; i<listSize; i++){
            if(heroList[i][2].equals("0")){
                unplayedHeroList.add(heroList[i]);
            }
            else{
                played[i] = heroList[i];
            }
        }
        Collections.shuffle(unplayedHeroList);
        if(unplayedHeroList.size() > numOfHeroes){
            heroID1 = new int[numOfHeroes];
        }
        else{
            heroID1 = new int[unplayedHeroList.size()];
        }
        //If all heroes have been played then choose randomly
        if(unplayedHeroList.size() == 0){
            System.out.println("All Heroes Have Been Played. Selecting From All");
            heroID = random(heroList, numOfHeroes, listSize);
        }
        else{
            for(int i=0; i<numOfHeroes;i++) {
            if(i < unplayedHeroList.size()){
                heroID1[i] = (Integer.parseInt(unplayedHeroList.get(i)[0]))-1;
            }
            else{
                count++;
            }
            i2++;
            }
            String[][] played2 = new String[listSize-(unplayedHeroList.size())][];
            int j=0;
            for(int i=0;i<played.length;i++){
                if(played[i] != null){
                    played2[j] = played[i];
                    j++;
                }
            }
            int[] heroID2 = new int[listSize-(unplayedHeroList.size())];
            heroID2 = random(played2,count,(listSize-(unplayedHeroList.size())));
            System.arraycopy(heroID1,0,heroID,0,heroID1.length);
            System.arraycopy(heroID2,0,heroID,heroID1.length,heroID2.length);
        }
        return heroID;
    }

    public int[] select(String[][] heroList, int numOfHeroes, int listSize){
        //Initialise necessary variables
        Scanner scanner = new Scanner(System.in);
        int[] tempheroID = new int[numOfHeroes];
        int[] heroID = new int[numOfHeroes];
        boolean validInput = false;
        boolean validID = false;

        //Loop to display all heroes and their stats
        for(int i =0; i<listSize; i++){
            System.out.println("----------------------------------------");
            System.out.println("Hero ID: " + (i+1));
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
                    heroID[i] = Integer.parseInt(scanner.nextLine())-1;
                    if (heroID[i] >= 0 && heroID[i] <=listSize) {
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
        for(int i=0;i<numOfHeroes;i++){
            heroID[i] = Integer.parseInt(heroList[heroID[i]][0])-1;
        }
        return heroID;
    }

    public void loadFiles(String[][] heroList, String heroFile){
        int index = 0;
        //Read file and load into heroList 2D array
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
                writer.write(heroList[i][0] + "," + heroList[i][1] + "," + heroList[i][2] + "," + heroList[i][3] + "," + heroList[i][4] + "," + heroList[i][5] + "," + heroList[i][6] + "," + heroList[i][7] + "," + heroList[i][8] + "," + heroList[i][9] + "," + heroList[i][10] + "," + heroList[i][11] + "," + heroList[i][12] + "," + heroList[i][13] + "," + heroList[i][14] + "," + heroList[i][15] + "," + heroList[i][16]);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving heroes: " + e.getMessage());
        }
    }
}
