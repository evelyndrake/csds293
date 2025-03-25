package socialnetwork;

import exceptions.ExceptionHandler;
import exceptions.NetworkOperationException;
import exceptions.PersonOperationException;

import java.util.HashMap;
import java.util.Objects;

public class SocialNetwork {

    // HashMap to store all the people in the network
    private final HashMap<Integer, Person> people;

    public SocialNetwork() {
        people = new HashMap<>();
    }

    // Create new person and add them to the network
    public boolean createAndAddPerson(int id, String name) throws NetworkOperationException {
        // Implemented feedback from last time to check for null
        Objects.requireNonNull(name, "Name cannot be null!");
        if (id < 0) {
            throw new NetworkOperationException("ID must be a positive integer!");
        }
        if (people.containsKey(id)) {
            throw new NetworkOperationException("Person with ID " + id + " already exists!");
        }
        people.put(id, new Person(id, name));
        return true;
    }

    public boolean addConnection(int id1, int id2) throws NetworkOperationException {
        Person person1 = people.get(id1);
        Person person2 = people.get(id2);
        if (id1 == id2) {
            throw new NetworkOperationException("Cannot connect to self!");
        }
        if (person1 == null || person2 == null) {
            throw new NetworkOperationException("One or both IDs are invalid!");
        }
        try {
            return person1.addConnection(person2) & person2.addConnection(person1);
        } catch (PersonOperationException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    // Remove a connection from the network
    public boolean removeConnection(int id1, int id2) throws NetworkOperationException {
        Person person1 = people.get(id1);
        Person person2 = people.get(id2);
        if (person1 == null || person2 == null) {
            throw new NetworkOperationException("One or both IDs are invalid!");
        }
        try {
            return person1.removeConnection(person2) & person2.removeConnection(person1);
        } catch (PersonOperationException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    // Getters
    public Person getPerson(int id) {
        return people.get(id);
    }

    public HashMap<Integer, Person> getPeople() {
        return new HashMap<>(people); // Defensive copying
    }

}
