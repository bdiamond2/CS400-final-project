// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class representing one level of Quarkslurp Alpha game
 */
public class QSALevel {
    private final CS400Graph<QSALocation> graph; // graph object representing the map
    private final int num; // the number of this level
    private CS400Graph<QSALocation>.Path playerPath; // the path the player has built so far
    private final CS400Graph<QSALocation>.Path optimalPath; // a path of the shortest distance through this level
    private QSALocation start; // starting location of the level
    private QSALocation end; // destination location
    private final QSAGame game; // QSAGame object this level is a part of

    /**
     * Checks whether the given level number has an associated level file
     * @param levelNum level number being checked
     * @return true if "level_<levelNum>.txt" exists, false if not
     */
    public static boolean levelFileExists(int levelNum) {
        return new File(getDataFilepath(levelNum)).isFile();
    }

    /**
     * Returns what would be a level's filepath if it exists
     * @param levelNum level number
     * @return filepath where this level's data would be located
     */
    private static String getDataFilepath(int levelNum) {
        return "data/levels/level_" + levelNum + ".txt";
    }

    /**
     * Creates a new QSALevel object
     * @param num level number
     * @param game QSAGame object this level is a part of
     * @throws IOException if necessary game files cannot be found
     */
    public QSALevel(int num, QSAGame game) throws IOException {
        this.num = num;
        this.game = game;
        graph = new CS400Graph<>();
        initialize();
        //setPlayerLocation(this.start);
        setPlayerPath(this.getGraph().new Path(this.graph.vertices.get(this.start)));
        optimalPath = getGraph().dijkstrasShortestPath(this.start, this.end);
    }

    /**
     * Initializes this level from its data file
     * @throws IOException if an error is encountered loading the file
     */
    private void initialize() throws IOException {
        // raw data
        ArrayList<String> data = QSADataLoader.loadFileData(getDataFilepath(this.num));

        QSALocation loc;
        String[] edgeWeights;

        // index of the locations used in this level
        // e.g. the QSALocation at index 0 is the first row of the adjacency matrix
        ArrayList<QSALocation> levelLoc = new ArrayList<>();

        // index of the adjacency weights, indices line up with levelLoc
        ArrayList<String> adjMatrix = new ArrayList<>();

        // one string-split line of data from the level file
        // <location ID>|<adjacent edge weights>
        String[] dataLine;

        // throw this if there's a problem detected with the level file
        IOException ioExc = new IOException(getDataFilepath(this.num) + " is corrupted");

        // create vertices, build adjacency matrix
        for (int i = 0; i < data.size(); i++) {
            dataLine = data.get(i).split(QSADataLoader.DELIMITER);
            loc = game.getLocations().get(dataLine[0]);

            // must be delimited and contain valid location ID
            if (dataLine.length != 2 || loc == null) {
                throw ioExc;
            }

            graph.insertVertex(loc);
            levelLoc.add(loc);
            adjMatrix.add(dataLine[1]);

            // start is always the first location listed
            if (start == null) {
                start = loc;
            }
            // end is always the last location listed
            else if (i == data.size() - 1) {
                end = loc;
            }
        }

        // connect all the vertices
        for (int row = 0; row < adjMatrix.size(); row++) {
            edgeWeights = adjMatrix.get(row).split(",");
            if (edgeWeights.length != adjMatrix.size()) { // must be a square matrix
                throw ioExc;
            }
            int weight;
            for (int col = 0; col < edgeWeights.length; col++) {
                try {
                    weight = Integer.parseInt(edgeWeights[col]);
                }
                catch (NumberFormatException e) {
                    throw ioExc;
                }
                if (weight <= 0) { // ignore non-positive numbers
                    continue;
                }
                graph.insertEdge(levelLoc.get(row), levelLoc.get(col), weight);
            }
        }

    }

    /**
     * Checks if this level is over; i.e. player reached the end or was caught
     * @return true if player reached the destination or their path distance has exceeded the shortest path
     */
    public boolean isLevelOver() {
        return playerPath.end.data.equals(this.end) || playerPath.distance > optimalPath.distance;
    }

    /**
     * Checks if the level is over AND if the player won
     * @return true if the player won (reached end and their path is shortest), false if not
     */
    public boolean levelWon() {
        return isLevelOver() && playerPath.distance <= optimalPath.distance; // distance should never be < optimalDistance
    }

    /**
     * Returns this level's graph object
     * @return CS400Graph representing this level's map
     */
    public CS400Graph<QSALocation> getGraph() {
        return graph;
    }

    /**
     * Returns this level's number
     * @return this level's number
     */
    public int getNum() {
        return num;
    }

    /**
     * Returns the player's current location
     * @return player's current location in the level
     */
    public QSALocation getPlayerLocation() {
        return getPlayerPath().end.data;
    }

    /**
     * Returns the start for this level
     * @return start for this level
     */
    public QSALocation getStart() {
        return start;
    }

    /**
     * Returns the destination for this level
     * @return destination for this level
     */
    public QSALocation getEnd() {
        return end;
    }

    /**
     * Returns a path of shortest distance through the level. This solution is not necessarily unique.
     * @return path of shortest distance through the level. This solution is not necessarily unique.
     */
    public CS400Graph<QSALocation>.Path getOptimalPath() {
        return optimalPath;
    }

    /**
     * Returns the path the player has taken so far through this level
     * @return path the player has taken so far through this level
     */
    public CS400Graph<QSALocation>.Path getPlayerPath() {
        return playerPath;
    }

    /**
     * Setter for playerPath
     * @param playerPath new Path to replace playerPath with
     */
    public void setPlayerPath(CS400Graph<QSALocation>.Path playerPath) {
        this.playerPath = playerPath;
    }
}
