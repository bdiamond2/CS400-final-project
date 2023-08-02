// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing the entire Quarkslurp Alpha game, containing all levels and locations.
 */
public class QSAGame {
    private final ArrayList<QSALevel> levels;
    private final HashMap<String, QSALocation> locations;
    private final int MAX_NUM_LEVELS = 50; // max level cap so levels can be skipped (mainly for testing purposes)

    /**
     * Creates a new QSAGame object
     * @throws IOException if there is an error with game file data
     */
    public QSAGame() throws IOException {
        locations = new HashMap<>();
        levels = new ArrayList<>();
        initLocations();
        initLevels();
    }

    /**
     * Loads locations from locations.txt
     * @throws IOException if there is an error with game file data
     */
    private void initLocations() throws IOException {
        ArrayList<String> data = QSADataLoader.loadFileData("data/locations.txt");
        QSALocation loc;

        for (String s : data) {
            if (s.equals("")) {
                continue; // ignore blank lines
            }
            loc = new QSALocation(s);
            locations.put(loc.getID(), loc);
        }
    }

    /**
     * Loads all the levels from "level_<num>.txt" files
     * @throws IOException if an error is encountered loading the data files
     */
    private void initLevels() throws IOException {
        QSALevel level;
        for (int levelNum = 1; levelNum <= MAX_NUM_LEVELS; levelNum++) {
            if (QSALevel.levelFileExists(levelNum)) {
                level = new QSALevel(levelNum, this);
                levels.add(level);
            }
        }
    }

    /**
     * Returns the list of QSALevel objects for this game
     * @return ArrayList of QSALevel objects
     */
    public ArrayList<QSALevel> getLevels() {
        return levels;
    }

    /**
     * Returns the HashMap lookup table for this game's locations
     * @return HashMap of QSALocations where the lookup key is the ID string
     */
    public HashMap<String, QSALocation> getLocations() {
        return locations;
    }
}
