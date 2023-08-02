// --== CS400 File Header Information ==--
// Name: Benjamin Diamond
// Email: bdiamond2@wisc.edu
// Notes to Grader: <optional extra notes>

import java.io.IOException;

/**
 * Class representing a location in the Quarkslurp Alpha game
 */
public class QSALocation {
    private final String ID; // ID for lookup
    private final String name; // location name
    private final String desc; // description

    /**
     * Initializes a QSA location from an info string in format 'ID|Description'
     * @param infoString string of text in format 'ID|Description'
     * @throws IOException if error is encountered loading a location from file
     */
    public QSALocation(String infoString) throws IOException {
        String[] fields = infoString.split(QSADataLoader.DELIMITER, -1);
        if (fields.length != 3) {
            throw new IOException("QSALocation info string is in wrong format: " + infoString);
        }

        this.ID = fields[0];
        this.name = fields[1];
        this.desc = fields[2];

        if (ID.isEmpty() || ID.isBlank() || name.isBlank() || name.isEmpty()) {
            throw new IOException("QSALocation infoString must have name and ID");
        }
    }

    /**
     * Returns location ID
     * @return location ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Returns location name
     * @return location name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns location description
     * @return location description
     */
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return getName();
    }
}
