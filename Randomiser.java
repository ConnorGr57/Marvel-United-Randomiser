abstract class Randomiser {
        abstract int random(String[][] list);
        abstract int randomNoPlayed(String[][] list);
        abstract int select(String[][] list);
        abstract void loadFiles(String[][] list, String filename);
        abstract void updateFile(String[][] list, String filename);
}
