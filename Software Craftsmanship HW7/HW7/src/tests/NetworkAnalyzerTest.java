package tests;

import org.junit.Before;
import org.junit.Test;
import socialnetwork.NetworkAnalyzer;
import socialnetwork.Person;
import socialnetwork.SocialNetwork;

import java.util.List;

public class NetworkAnalyzerTest {

    @Before
    public void setUp() { // Reset the network before each test
        NetworkAnalyzer.reset();
    }

    @Test
    public void testConstructor() {
        // This is a static class, so this test just gets the constructor coverage
        NetworkAnalyzer analyzer = new NetworkAnalyzer();
    }

    @Test
    public void testFindInfluencers() { // Test T1
        // Add 3 people to the network
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addPerson(3, "Person3");
        // Add connections between the people
        NetworkAnalyzer.addConnection(1, 2);
        NetworkAnalyzer.addConnection(1, 3);
        NetworkAnalyzer.addConnection(2, 3);
        // Find the top 1 influencer, which should be Person1
        // TC1
        assert NetworkAnalyzer.findInfluencers(1).size() == 1;
        assert NetworkAnalyzer.findInfluencers(1).get(0).getId() == 1;
        // Find the top 2 influencers, which should be Person1 and Person2
        assert NetworkAnalyzer.findInfluencers(2).size() == 2;
        assert NetworkAnalyzer.findInfluencers(2).get(0).getId() == 1;
        assert NetworkAnalyzer.findInfluencers(2).get(1).getId() == 2;
        // TC1 - Testing boundaries with good data (although the network has people, k is invalid)
        // Should throw an IllegalArgumentException with k = 0
        try {
            NetworkAnalyzer.findInfluencers(0);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        // Should throw an IllegalArgumentException with k = -1
        try {
            NetworkAnalyzer.findInfluencers(-1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testFindInfluencersWithNoPeople() { // Test T2
        // TC1 - Testing boundaries with bad data
        // Find the top 1 influencer with no people in the network, which should be empty
        assert NetworkAnalyzer.findInfluencers(1).isEmpty();
        // Find the top 1 influencer when k is -1, which should throw an IllegalArgumentException
        try {
            NetworkAnalyzer.findInfluencers(-1);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
        // Find the top 1 influencer when k is 0, which should throw an IllegalArgumentException
        try {
            NetworkAnalyzer.findInfluencers(0);
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testShortestPath() { // Test T3
        // Add 3 people to the network
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addPerson(3, "Person3");
        // Add connections between the people
        NetworkAnalyzer.addConnection(1, 2);
        NetworkAnalyzer.addConnection(1, 3);
        NetworkAnalyzer.addConnection(2, 3);
        // Find the shortest path between Person1 and Person3
        // TC3
        assert NetworkAnalyzer.shortestPath(1, 3).size() == 2;
        assert NetworkAnalyzer.shortestPath(1, 3).get(0).getId() == 1;
        assert NetworkAnalyzer.shortestPath(1, 3).get(1).getId() == 3;
    }

    @Test
    public void testShortestPathWithNoPath() { // Test T4
        // Add 3 people to the network
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addPerson(3, "Person3");
        // Add connections between the people
        NetworkAnalyzer.addConnection(1, 2);
        // TC3
        // Find the shortest path between Person1 and Person3, which should be empty
        assert NetworkAnalyzer.shortestPath(1, 3).isEmpty();
    }

    @Test
    public void testShortestPathWithNonexistentID() { // Test T5
        // Add 3 people to the network
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addPerson(3, "Person3");
        // Add connections between the people
        NetworkAnalyzer.addConnection(1, 2);
        // TC3
        // Find the shortest path between Person1 and Person4, which should be empty
        assert NetworkAnalyzer.shortestPath(1, 4).isEmpty();
        assert NetworkAnalyzer.shortestPath(4, 1).isEmpty();
    }
    @Test
    public void testShortestPathWithSameID() { // Test T6
        // Add 3 people to the network
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addPerson(3, "Person3");
        // Add connections between the people
        NetworkAnalyzer.addConnection(1, 2);
        // TC3
        // Find the shortest path between Person1 and Person1, which should just be Person1
        assert NetworkAnalyzer.shortestPath(1, 1).size() == 1;
        assert NetworkAnalyzer.shortestPath(1, 1).get(0).getId() == 1;
    }

    @Test
    public void testAddPerson() { // Test T7
        assert NetworkAnalyzer.addPerson(1, "Person1");
        // Bad data (invalid ID)
        assert !NetworkAnalyzer.addPerson(-1, "Person1");
    }

    @Test
    public void testAddConnection() { // Test T8
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        assert NetworkAnalyzer.addConnection(1, 2);
    }

    @Test
    public void testAddConnectionWithInvalidId() { // Test T9
        NetworkAnalyzer.addPerson(1, "Person1");
        assert !NetworkAnalyzer.addConnection(1, 2);
    }

    @Test
    public void testAddDuplicateConnection() { // Test T10
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addConnection(1, 2);
        assert !NetworkAnalyzer.addConnection(1, 2);
    }

    @Test
    public void testRemoveConnection() { // Test T11
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        NetworkAnalyzer.addConnection(1, 2);
        assert NetworkAnalyzer.removeConnection(1, 2);
    }

    @Test
    public void testRemoveConnectionWithInvalidId() { // Test T12
        NetworkAnalyzer.addPerson(1, "Person1");
        assert !NetworkAnalyzer.removeConnection(1, 2);
    }


    @Test
    public void testRemoveNonexistentConnection() { // Test T13
        NetworkAnalyzer.addPerson(1, "Person1");
        NetworkAnalyzer.addPerson(2, "Person2");
        assert !NetworkAnalyzer.removeConnection(1, 2);
    }

    @Test
    public void testSetNetwork() { // Test T14
        // Good data
        SocialNetwork network = new SocialNetwork();
        NetworkAnalyzer.setNetwork(network);
        // TC4
        assert NetworkAnalyzer.getNetwork() == network;
    }

    @Test
    public void testSetNetworkWithNull() { // Test T15
        // Bad data
        try {
            NetworkAnalyzer.setNetwork(null);
            assert false;
        } catch (NullPointerException e) {
            // TC4
            assert true;
        }
    }

    @Test
    public void stressTest() { // Test T16
        // Create 1000 people
        for (int i = 0; i < 1000; i++) {
            // This also invokes SocialNetwork.createAndAddPerson and the Person constructor
            NetworkAnalyzer.addPerson(i, "Person" + i);
        }
        // Connect each person to every other person
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (i != j) {
                    // This also invokes SocialNetwork.addConnection and Person.addConnection
                    NetworkAnalyzer.addConnection(i, j);
                }
            }
        }
        // Because each person is connected to every other person, the shortest path between any two people should be of size 2.
        List<Person> shortestPath = NetworkAnalyzer.shortestPath(0, 999);
        // TC5
        assert shortestPath.size() == 2;
        assert shortestPath.get(0).getId() == 0;
        assert shortestPath.get(1).getId() == 999;
        // Reset the network and create 1000 people
        NetworkAnalyzer.reset();
        for (int i = 0; i < 1000; i++) {
            NetworkAnalyzer.addPerson(i, "Person" + i);
        }
        // Connect each person to the next person only
        for (int i = 0; i < 999; i++) {
            NetworkAnalyzer.addConnection(i, i + 1);
        }
        // Find the path from person 0 to person 999, which should be of size 1000
        shortestPath = NetworkAnalyzer.shortestPath(0, 999);
        // TC5
        assert shortestPath.size() == 1000;
        // Find the top 100 influencers, which should be the last 100
        List<Person> influencers = NetworkAnalyzer.findInfluencers(100);
        // TC5
        assert influencers.size() == 100;
    }

}
