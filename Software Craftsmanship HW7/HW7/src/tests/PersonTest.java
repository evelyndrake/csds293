package tests;

import exceptions.PersonOperationException;
import org.junit.Test;
import socialnetwork.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonTest {
    @Test
    public void testCreatePerson() {
        // Good data
        Person person = new Person(1, "Person1");
        assert person.getId() == 1;
        assert person.getName().equals("Person1");
        // Bad data (null name)
        try {
            new Person(1, null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
        // Bad data (negative ID)
        try {
            new Person(-1, "Person1");
            assert false;
        } catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testCreatePersonWithListOfConnections() {
        // Good data
        Person person = new Person(1, "Person1", new ArrayList<>());
        assert person.getId() == 1;
        assert person.getName().equals("Person1");
        assert person.getConnections().isEmpty();
        // Bad data
        try {
            new Person(1, null, null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }


    @Test
    public void testIsConnectedTo() { // Test T1
        // Good data
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        // TC 1
        assert !person1.isConnectedTo(person2);
        assert !person2.isConnectedTo(person1);
        Person person3 = new Person(3, "Person3", new ArrayList<>(List.of(4)));
        Person person4 = new Person(4, "Person4", new ArrayList<>(List.of(3)));
        assert person3.isConnectedTo(person4);
        assert person4.isConnectedTo(person3);
        // Bad data
        try {
            person1.isConnectedTo(null);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testIsConnectedToWithId() { // Test T2
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        // TC 2
        assert !person1.isConnectedTo(person2.getId());
        assert !person2.isConnectedTo(person1.getId());
    }


    @Test
    public void testAddConnection() throws PersonOperationException { // Test T3
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        // TC 3
        assert person1.addConnection(person2);
        assert person2.addConnection(person1);
        assert person1.isConnectedTo(person2);
        assert person2.isConnectedTo(person1);
        // Bad data (null person)
        try {
            person1.addConnection(null);
            assert false;
        } catch (PersonOperationException e) {
            assert true;
        }
    }

    @Test
    public void testAddDuplicateConnection() throws PersonOperationException { // Test T4
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        // TC 3
        person1.addConnection(person2);
        // Duplicate connection (should throw a PersonOperationException)
        try {
            person1.addConnection(person2);
            assert false;
        } catch (PersonOperationException e) {
            assert true;
        }
        // Self-connection (should throw a PersonOperationException)
        try {
            person1.addConnection(person1);
            assert false;
        } catch (PersonOperationException e) {
            assert true;
        }
    }

    @Test
    public void testRemoveConnection() throws PersonOperationException { // Test T5
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        person1.addConnection(person2);
        // TC5
        assert person1.removeConnection(person2);
        assert !person1.isConnectedTo(person2);
        assert !person2.isConnectedTo(person1);
    }

    @Test
    public void testRemoveNonexistentConnection() throws PersonOperationException { // Test T6
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        // TC5
        // Bad data (should throw PersonOperationException)
        try {
            person1.removeConnection(person2);
            assert false;
        } catch (PersonOperationException e) {
            assert true;
        }
    }

    @Test
    public void testRemoveConnectionTwice() throws PersonOperationException { // Test T7
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        person1.addConnection(person2);
        person1.removeConnection(person2);
        // TC5
        // Bad data (should throw PersonOperationException)
        try {
            person1.removeConnection(person2);
            assert false;
        } catch (PersonOperationException e) {
            assert true;
        }
    }

    @Test
    public void testGetId() { // Test T8
        Person person = new Person(1, "Person1");
        assert person.getId() == 1;
    }

    @Test
    public void testGetName() { // Test T9
        Person person = new Person(1, "Person1");
        assert person.getName().equals("Person1");
    }

    @Test
    public void testGetConnections() throws PersonOperationException { // Test T10
        Person person1 = new Person(1, "Person1");
        Person person2 = new Person(2, "Person2");
        Person person3 = new Person(3, "Person3");
        person1.addConnection(person2);
        // TC6
        assert person1.getConnections().contains(person2.getId());
        assert person3.getConnections().isEmpty();
    }
}
