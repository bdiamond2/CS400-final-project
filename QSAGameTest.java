// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class QSAGameTest {
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
//        System.out.println("calling tearDown()...");
    }

    @Test
    public void testGame() {
        try {
            QSAGame game = new QSAGame();
            assertEquals(5, game.getLevels().size());
            assertEquals(38, game.getLocations().size());
            assertEquals("Quarkslurp Alpha", game.getLocations().get("1").getName());
            assertEquals("Ohio", game.getLocations().get("29").getName());
        } catch (IOException e) {
            fail("Failed to create QSAGame object");
        }
    }

    @Test
    public void testLevelInit() {
        try {
            QSAGame game = new QSAGame();
            QSALevel level = game.getLevels().get(0);
            assertTrue(QSALevel.levelFileExists(1));
            assertNotEquals(null, level.getPlayerPath());
            assertNotEquals(null, level.getStart());
            assertNotEquals(null, level.getEnd());
            assertNotEquals(null, level.getGraph());
            assertEquals(1, level.getNum());
            assertEquals(level.getPlayerLocation(), level.getStart());

        } catch (IOException e) {
            fail("Failed to create QSALevel object");
        }
    }

    @Test
    public void testWinLose() {
        try {
            QSAGame game = new QSAGame();
            QSALevel level = game.getLevels().get(0);

            assertFalse(level.isLevelOver());
            assertFalse(level.levelWon());

            // win the game
            level.setPlayerPath(level.getGraph().new
                    Path(level.getPlayerPath(),
                    level.getGraph().getEdge(game.getLocations().get("1"), game.getLocations().get("2"))));
            level.setPlayerPath(level.getGraph().new
                    Path(level.getPlayerPath(),
                    level.getGraph().getEdge(game.getLocations().get("2"), game.getLocations().get("4"))));
            level.setPlayerPath(level.getGraph().new
                    Path(level.getPlayerPath(),
                    level.getGraph().getEdge(game.getLocations().get("4"), game.getLocations().get("5"))));

            assertTrue(level.isLevelOver() && level.levelWon());
            assertEquals(level.getOptimalPath().distance, level.getPlayerPath().distance);

            // lose the game
            level.setPlayerPath(level.getGraph().new Path(level.getGraph().vertices.get(game.getLocations().get("1"))));
            level.setPlayerPath(level.getGraph().new
                    Path(level.getPlayerPath(),
                    level.getGraph().getEdge(game.getLocations().get("1"), game.getLocations().get("3"))));
            assertFalse(level.isLevelOver());
            assertFalse(level.levelWon());
            level.setPlayerPath(level.getGraph().new
                    Path(level.getPlayerPath(),
                    level.getGraph().getEdge(game.getLocations().get("3"), game.getLocations().get("5"))));
            assertTrue(level.isLevelOver());
            assertFalse(level.levelWon());

        } catch (IOException e) {
            fail("Failed to create QSALevel object");
        }
    }

    @Test
    public void testLocation() {
        QSALocation loc;
        try {
            loc = new QSALocation("1|Earth|This is a valid location");
            assertEquals("1", loc.getID());
            assertEquals("Earth", loc.getName());
            assertEquals("This is a valid location", loc.getDesc());
        } catch (IOException e) {
            fail("Failed to initialize valid QSALocation");
        }
        try {
            new QSALocation("200|Mars|");
        } catch (IOException e) {
            fail("Failed to initialize valid QSALocation");
        }
        try {
            new QSALocation("EARTH|Earth|This technically a valid location");
        } catch (IOException e) {
            fail("Failed to initialize valid QSALocation");
        }
        try {
            new QSALocation("1|EarthThis is an invalid location");
            fail("Created location from invalid infoString");
        } catch (IOException e) {
            // good catch
        }
        try {
            new QSALocation("|Earth|This is an invalid location");
            fail("Created location from invalid infoString");
        } catch (IOException e) {
            // good catch
        }
        try {
            new QSALocation("1||This is an invalid location");
            fail("Created location from invalid infoString");
        } catch (IOException e) {
            // good catch
        }
        try {
            new QSALocation("||This is an invalid location");
            fail("Created location from invalid infoString");
        } catch (IOException e) {
            // good catch
        }
    }

    @Test
    public void testDataLoader() {
        assertEquals("\\|", QSADataLoader.DELIMITER);
        try {
            File f = new File("bdiamond2QSATestFile.txt");
            f.createNewFile();
            FileWriter fw = new FileWriter(f.getName());
            fw.write("Hello\nWorld");
            fw.close();

            ArrayList<String> output = QSADataLoader.loadFileData(f.getName());
            assertEquals(2, output.size());
            assertEquals("Hello", output.get(0));
            assertEquals("World", output.get(1));
            f.delete();
        } catch (IOException e) {
            fail("Failed to create mock file for testing");
        }
    }
}