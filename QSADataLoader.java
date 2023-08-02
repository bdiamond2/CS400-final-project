// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.util.Scanner; // Import the Scanner class to read text files

/**
 * Loader class to fetch file data
 */
public class QSADataLoader {
    public static final String DELIMITER = "\\|";

    /**
     * Fetches the given file and returns the contents as an ArrayList, where each line of the
     * file is an element of the list.
     * @param filename file to retrieve
     * @return ArrayList with the contents of the file (one element per line)
     * @throws IOException if file is not found
     */
    public static ArrayList<String> loadFileData(String filename) throws IOException {
        ArrayList<String> ret = new ArrayList<>();
        File f = new File(filename);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            ret.add(s.nextLine());
        }
        s.close();
        return ret;
    }

}

