package socialnetwork;

import exceptions.ExceptionHandler;
import exceptions.IllegalLineException;
import exceptions.NetworkOperationException;

import java.io.InputStream;
import java.util.Scanner;

public class SocialNetworkParser {
    private final SocialNetwork network;

    // Constructor
    public SocialNetworkParser(SocialNetwork network) {
        this.network = network;
    }

    // Generalized parse method
    public void parse(InputStream input) {
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim(); // Trim leading and trailing whitespace
            String[] parts;
            try { // Attempt to split and validate the line
                parts = splitAndValidate(line);
            } catch (IllegalLineException e) { // If the line is invalid, display a warning and continue
                ExceptionHandler.handleException(e);
                continue;
            }
            // Parse the line based on the first character
            if (line.startsWith("P")) {
                parsePerson(parts);
            } else if (line.startsWith("C")) {
                parseConnection(parts);
            } else {
                ExceptionHandler.displayWarning("Invalid line type: " + line);
            }
        }
        scanner.close();
    }

    // Split and validate a line of input
    private String[] splitAndValidate(String line) throws IllegalLineException {
        String[] parts = line.split(" ");
        if (parts.length != 3) {
            throw new IllegalLineException("Invalid line: " + line); // Throw an exception for invalid lines
        }
        return parts;
    }

    // Parse a person line
    public void parsePerson(String[] parts) {
        try { // Attempt to parse the ID as an integer
            int id = Integer.parseInt(parts[1]);
            String name = parts[2];
            try {
                network.createAndAddPerson(id, name);
            } catch (NetworkOperationException e) { // Catch any exceptions thrown by createAndAddPerson
                ExceptionHandler.handleException(e);
            }
        } catch (NumberFormatException e) { // Ensure that the ID is a valid integer
            ExceptionHandler.displayWarning("Invalid ID: " + parts[1]);
            ExceptionHandler.handleException(e);
        }
    }

    // Parse a connection line
    public void parseConnection(String[] parts) {
        try { // Attempt to parse the IDs as integers
            int id1 = Integer.parseInt(parts[1]);
            int id2 = Integer.parseInt(parts[2]);
            try {
                network.addConnection(id1, id2);
                /*
                NOTE: This is a good example of how exceptions are handled. The parser class calls the addConnection
                method of the network class, which in turn calls the addConnection method of the person class. If a
                PersonOperationException is thrown when adding the connection in the person class, it is caught by
                the network class which throws a NetworkOperationException. This is then passed up to the parser class
                and given to the ExceptionHandler, which displays the error message.
                 */
            } catch (NetworkOperationException e) { // Catch any exceptions thrown by addConnection
                ExceptionHandler.handleException(e);
            }
        } catch (NumberFormatException e) { // Ensure that the IDs are valid integers
            ExceptionHandler.displayWarning("Invalid ID: " + parts[1] + " or " + parts[2]);
            ExceptionHandler.handleException(e);
        }
    }

}
