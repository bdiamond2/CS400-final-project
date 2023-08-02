// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Driver class forQ Quarkslurp Alpha that takes user input and loops through the game
 */
public class QSAGameDriver {
    // default amount of time for Thread.sleep() between printing each character
    private static final int DEFAULT_SCROLL_TIME = 10;
    // defines when a newline should be inserted when printing a long string of text to the screen
    private static final int SCREEN_WIDTH = 70;

    /**
     * Main entry point
     *
     * @param args args
     */
    public static void main(String[] args) {
        try {
            QSAGame game = new QSAGame();
            QSALevel level;

            // "cinematic" opening sequence
            prologue();

            // loop through the levels
            for (int i = 0; i < game.getLevels().size(); i++) {
                level = game.getLevels().get(i);
                if (i != 0) {
                    textScroll("\n\nWarping to the next system...", 80);
                }
                // if they lost the level
                if (!playLevel(level)) {
                    System.out.println("\nYou were caught! The shortest path was " +
                            level.getOptimalPath() + " (" + level.getOptimalPath().distance + "). " +
                            "\nYour path was " + level.getPlayerPath() + " (" + level.getPlayerPath().distance + ").");
                    System.out.println("GAME OVER");
                    return;
                }
                // if they completed the level
                else {
                    textScroll("\nYou've reached the warp gate at " + level.getPlayerLocation() + ".", DEFAULT_SCROLL_TIME);
                    textScroll(level.getPlayerLocation().getDesc(), 5, SCREEN_WIDTH);
                    textScroll("LEVEL " + level.getNum() + " COMPLETED", DEFAULT_SCROLL_TIME);
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                        // nothing
                    }
                }
            }
            System.out.println("\nYou safely made it home!\nYOU WIN");
        } catch (IOException e) {
            System.out.println("Fatal error: " + e.getMessage());
        }

    }

    /**
     * Reads prologue.txt and displays the text to the screen
     * Uses &-prefixed directives:
     * - &SLEEP t: calls Thread.sleep(t) one time
     * - &SCROLL t: calls Thread.sleep(t) between each character when printing
     * - &PETC: prints "Press enter to continue" and awaits user input
     *
     * @throws IOException if error is encountered reading file
     */
    private static void prologue() throws IOException {
        int scrollTime = DEFAULT_SCROLL_TIME;
        File f = new File("data/prologue.txt");
        Scanner s = new Scanner(f);
        Scanner petc = new Scanner(System.in);
        String nextLine;
        String[] nextLineSplit;

        while (s.hasNextLine()) {
            nextLine = s.nextLine();
            if (nextLine.equals("&PETC")) {
                textScroll("\nPress enter to continue.", scrollTime);
                petc.nextLine();
            }
            else if (nextLine.contains("&SLEEP")) {
                nextLineSplit = nextLine.split(" ");
                try {
                    Thread.sleep(Integer.parseInt(nextLineSplit[1]));
                } catch (Exception e) {
                    // nothing
                }
            }
            else if (nextLine.contains("&SCROLL")) {
                nextLineSplit = nextLine.split(" ");
                scrollTime = Integer.parseInt(nextLineSplit[1]);
            }
            else {
                textScroll(nextLine, scrollTime);
            }
        }
        s.close();
    }

    /**
     * Plays a level of the game and returns the result
     *
     * @param level QSALevel object to be played
     * @return true if player passed, false if failed
     */
    private static boolean playLevel(QSALevel level) {
        ArrayList<CS400Graph<QSALocation>.Edge> choices;
        CS400Graph<QSALocation>.Edge choice;
        Scanner s = new Scanner(System.in);

        textScroll("\n\nLEVEL " + level.getNum(), DEFAULT_SCROLL_TIME);
        textScroll("Objective: Reach the warp gate at " + level.getEnd() + ".", DEFAULT_SCROLL_TIME);
        textScroll("\nMap: ", DEFAULT_SCROLL_TIME);
        System.out.println(level.getGraph());

        while (!level.isLevelOver()) {
            textScroll("\nYour current location: " + level.getPlayerLocation(), DEFAULT_SCROLL_TIME);
            textScroll(level.getPlayerLocation().getDesc(), 5, SCREEN_WIDTH);

            choices = new ArrayList<>(level.getGraph().vertices.get(level.getPlayerLocation()).edgesLeaving);
            textScroll("\nChoose a location to travel to: ", DEFAULT_SCROLL_TIME);
            for (int i = 0; i < choices.size(); i++) {
                choice = choices.get(i);
                System.out.println(i + ". " + choice.target +
                        " (distance: " + choice.weight + ")");
            }
            // extend the current path by the player's selected edge
            choice = getPlayerChoice(s, choices);
            level.setPlayerPath(level.getGraph().new Path(level.getPlayerPath(), choice));
        }
        return level.levelWon();
    }

    /**
     * Prompts the player to choose the next leg of their route through the sector
     *
     * @param s       Scanner object to collect input
     * @param choices a list of CS400.Edge choices the player can pick
     * @return CS400.Edge object chosen by the player
     */
    private static CS400Graph<QSALocation>.Edge getPlayerChoice(Scanner s, ArrayList<CS400Graph<QSALocation>.Edge> choices) {
        String input;
        System.out.print("Enter a number: ");
        input = s.nextLine();
        CS400Graph<QSALocation>.Edge choice = parseEdgeFromInput(input, choices);
        while (choice == null) {
            System.out.println("Invalid input");
            System.out.print("Enter a number: ");
            input = s.nextLine();
            choice = parseEdgeFromInput(input, choices);
        }
        return choice;
    }

    /**
     * Parses string input from the user and checks if it's a valid choice
     *
     * @param input   user input collected from Scanner
     * @param choices list of valid Edge choices
     * @return Edge the user chose, or null if the choice was invalid
     */
    private static CS400Graph<QSALocation>.Edge parseEdgeFromInput(String input, ArrayList<CS400Graph<QSALocation>.Edge> choices) {
        try {
            input = input.trim();
            int choice = Integer.parseInt(input);
            return choices.get(choice);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prints out text to the screen in a scrolling animation that looks neat and science fiction-y
     *
     * @param text        text to print
     * @param scrollTime  the amount of milliseconds to pause between printing each character to create
     *                    the scrolling effect
     * @param screenWidth number of characters that will print, after which a newline will be inserted
     *                    at the next whitespace character. Value of <=0 will suspend this behavior
     */
    private static void textScroll(String text, int scrollTime, int screenWidth) {
        char nextChar;
        boolean newLine = false;
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep(scrollTime);
            } catch (Exception e) {
                // nothing
            }
            nextChar = text.charAt(i);
            j++;
            if (screenWidth > 0 && j > screenWidth) {
                newLine = true;
            }

            if (Character.isWhitespace(nextChar) && newLine) {
                System.out.println();
                newLine = false;
                j = 0;
            }
            else {
                System.out.print(nextChar);
            }
        }
        System.out.println();
    }

    /**
     * Prints out text to the screen in a scrolling animation that looks neat and science fiction-y
     *
     * @param text       text to print
     * @param scrollTime the amount of milliseconds to pause between printing each character to create
     *                   the scrolling effect
     */
    private static void textScroll(String text, int scrollTime) {
        textScroll(text, scrollTime, 0);
    }
}
