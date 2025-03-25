package tests;

import exceptions.IllegalLineException;
import org.junit.Before;
import org.junit.Test;
import socialnetwork.SocialNetwork;
import socialnetwork.SocialNetworkParser;

import java.io.ByteArrayInputStream;

public class SocialNetworkParserTest {
    private SocialNetworkParser parser;
    private SocialNetwork network;

    @Before
    public void setUp() { // Create a new network in between each test
        network = new SocialNetwork();
        parser = new SocialNetworkParser(network);
    }

    // Test the generalized line parsing with valid data
    @Test
    public void testValidLineType()  { // Test T1
        // TC1
        // Good data
        String input = "P 1 Alice\nP 2 Bob\n C 1 2\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 2;
        assert network.getPeople().get(1).getName().equals("Alice");
        assert network.getPeople().get(2).getName().equals("Bob");
        assert network.getPeople().get(1).isConnectedTo(network.getPeople().get(2));
    }

    // Test the generalized line parsing with invalid data
    @Test
    public void testInvalidLineType() throws IllegalLineException { // Test T2
        // TC1
        // Bad data (D is not a valid line type)
        String input = "P Alice\nP Bob\n C \n D 1 2\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 0;
    }

    // Test the specific person line parsing with valid data
    @Test
    public void testValidPersonLine() { // Test T3
        // TC2
        // Good data
        String input = "P 1 Alice\nP 2 Bob\n C 1 2\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 2;
        assert network.getPeople().get(1).getName().equals("Alice");
        assert network.getPeople().get(2).getName().equals("Bob");
        assert network.getPeople().get(1).isConnectedTo(network.getPeople().get(2));
    }

    // Test the specific person line parsing with invalid data
    @Test
    public void testInvalidPersonLine() { // Test T4
        // TC2
        // Bad data (person cannot be added twice)
        String input = "P 1 Alice\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        // Parse the string a second time
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 1; // Network should be of size 1
        // Bad data (ID is not an integer)
        input = "P 1.1 Alice\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 1;
    }

    // Test the specific connection line parsing with valid data
    @Test
    public void testValidConnectionLine() { // Test T5
        // TC3
        // Good data
        String input = "P 1 Alice\nP 2 Bob\n C 1 2\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 2;
        assert network.getPeople().get(1).getName().equals("Alice");
        assert network.getPeople().get(2).getName().equals("Bob");
        assert network.getPeople().get(1).isConnectedTo(network.getPeople().get(2));
    }

    // Test the specific connection line parsing with invalid data
    @Test
    public void testInvalidConnectionLine() { // Test T6
        // TC3
        // Bad data (connection cannot be added twice)
        String input = "P 1 Alice\nP 2 Bob\n C 1 2\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        // Parse the string a second time
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 2;
        assert network.getPeople().get(1).isConnectedTo(network.getPeople().get(2));
        // Bad data (IDs do not exist)
        input = "C 1 3\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 2;
        // Bad data (IDs are not integers)
        input = "P 3 Alice\nP 4 Bob\n C 3.1 4.1\n";
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert !network.getPeople().get(3).isConnectedTo(network.getPeople().get(4));
    }

    // Stress test
    @Test
    public void stressTest() { // Test T7
        // TC4
        // Stress test
        String input = "";
        for (int i = 1; i <= 1000; i++) { // String will add 1000 people
            input += "P " + i + " Alice\n";
        }
        for (int i = 1; i <= 1000; i++) { // String will connect 1000 people
            input += "C " + i + " " + (i + 1) + "\n";
        }
        parser.parse(new ByteArrayInputStream(input.getBytes()));
        assert network.getPeople().size() == 1000;
        for (int i = 1; i < 1000; i++) {
            assert network.getPeople().get(i).isConnectedTo(network.getPeople().get(i + 1));
        }
    }


}
