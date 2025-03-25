package socialnetwork;

import exceptions.PersonOperationException;

import java.util.ArrayList;
import java.util.Objects;

public class Person {
    private final int id;
    private final String name;
    private final ArrayList<Integer> connections;

    // Constructor for a person with a list of connections, probably not used in practice
    public Person(int id, String name, ArrayList<Integer> connections) {
        // Implemented feedback from last time to check for null
        Objects.requireNonNull(connections, "Connections list cannot be null!");
        Objects.requireNonNull(name, "Name cannot be null!");
        // Ensure ID is positive
        if (id < 0) {
            throw new IllegalArgumentException("ID must be a positive integer!");
        }
        this.id = id;
        this.name = name;
        this.connections = new ArrayList<>(connections); // Defensive copying
    }

    // Constructor for a person with no connections
    public Person(int id, String name) {
        // Just call the other constructor with an empty list to avoid code duplication
        this(id, name, new ArrayList<>());
    }

    // Check if a person is connected to another person, return boolean
    public boolean isConnectedTo(Person person) {
        // Implemented feedback from last time to check for null
        Objects.requireNonNull(person);
        return connections.contains(person.id);
    }

    // Check if a person is connected to another person with id, return boolean
    public boolean isConnectedTo(int id) {
        return connections.contains(id);
    }

    // Add a connection to a person, return boolean indicating success
    public boolean addConnection(Person person) throws PersonOperationException {
        // Person cannot be null
        if (person == null) {
            throw new PersonOperationException("Cannot add a null person as a connection!");
        }
        if (person.id == id) { // Cannot connect to self, return false
            throw new PersonOperationException("Cannot connect to self!");
        }
        if (connections.contains(person.id)) { // If the connection already exists, return false
            throw new PersonOperationException("Connection already exists!");
        }
        // Otherwise, add the connection and return true
        connections.add(person.id);
        return true;
    }

    // Remove a connection to a person, return boolean indicating success
    public boolean removeConnection(Person person) throws PersonOperationException {
        Objects.requireNonNull(person);
        if (!connections.contains(person.id)) { // If the connection does not exist, return false
            throw new PersonOperationException("Connection does not exist!");
        }
        // Otherwise, remove the connection and return true
        connections.remove(Integer.valueOf(person.id));
        return true;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getConnections() {
        return new ArrayList<>(connections); // Defensive copying
    }

}
