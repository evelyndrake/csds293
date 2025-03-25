package tests;

import exceptions.NetworkOperationException;
import org.junit.Test;
import socialnetwork.SocialNetwork;

public class SocialNetworkTest {
    @Test
    public void testCreateAndAddPerson() throws NetworkOperationException { // Test T1
        SocialNetwork network = new SocialNetwork();
        // TC1
        // Good data
        assert network.createAndAddPerson(1, "Person1");
        try {
            network.createAndAddPerson(1, "Person1");
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        // Bad data (negative ID)
        try {
            network.createAndAddPerson(-1, "Person1");
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        // Bad data
        try {
            network.createAndAddPerson(1, null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testAddConnection() throws NetworkOperationException { // Test T2
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        network.createAndAddPerson(2, "Person2");
        // TC2
        assert network.addConnection(1, 2);
    }

    // Note: For the following two methods, I can't get complete branch coverage because the final statements short-circuit
    // as I discussed in our last session. These are the only two branches in the entire project that I can't cover.
    @Test
    public void testAddConnectionWithInvalidId() throws NetworkOperationException { // Test T3
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        // TC2
        // Branch 1: First person exists, second person doesn't (should throw a NetworkOperationException)
        try {
            network.addConnection(1, 2);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        network = new SocialNetwork();
        network.createAndAddPerson(2, "Person2");
        // TC2
        // Branch 2: First person doesn't exist, second person exists (should throw a NetworkOperationException)
        try {
            network.addConnection(1, 2);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        // Branch 3: Neither person exists (should throw a NetworkOperationException)
        try {
            network.addConnection(3, 4);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        // TC2 (self connection)
        network = new SocialNetwork();
        // Should throw a NetworkOperationException
        try {
            network.addConnection(1, 1);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
    }

    @Test
    public void testAddDuplicateConnection() throws NetworkOperationException { // Test T4
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        network.createAndAddPerson(2, "Person2");
        // TC2
        assert network.addConnection(1, 2);
        assert !network.addConnection(1, 2);
    }

    @Test
    public void testRemoveConnection() throws NetworkOperationException { // Test T5
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        network.createAndAddPerson(2, "Person2");
        network.addConnection(1, 2);
        // TC3
        assert network.removeConnection(1, 2);
        assert !network.getPerson(1).isConnectedTo(2);
        assert !network.getPerson(2).isConnectedTo(1);
    }

    @Test
    public void testRemoveConnectionWithInvalidId() throws NetworkOperationException { // Test T6
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        // TC3
        try {
            network.removeConnection(1, 2);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        try {
            network.removeConnection(2, 1);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
        try {
            network.removeConnection(3, 4);
            assert false;
        } catch (NetworkOperationException e) {
            assert true;
        }
    }

    @Test
    public void testRemoveNonexistentConnection() throws NetworkOperationException { // Test T7
        SocialNetwork network = new SocialNetwork();
        network.createAndAddPerson(1, "Person1");
        network.createAndAddPerson(2, "Person2");
        // TC3
        assert !network.removeConnection(1, 2);
        assert !network.removeConnection(2, 1);
    }

}
