package socialnetwork;

import exceptions.ExceptionHandler;
import exceptions.NetworkOperationException;

import java.util.*;

public final class NetworkAnalyzer {
    private static SocialNetwork network;

    // Wrappers for the SocialNetwork methods, used for testing purposes only
    public static boolean addPerson(int id, String name) {
        try {
            return network.createAndAddPerson(id, name);
        } catch (NetworkOperationException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    public static boolean addConnection(int id1, int id2) {
        try {
            return network.addConnection(id1, id2);
        } catch (NetworkOperationException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    public static boolean removeConnection(int id1, int id2) {
        try {
            return network.removeConnection(id1, id2);
        } catch (NetworkOperationException e) {
            ExceptionHandler.handleException(e);
            return false;
        }
    }

    // Reset the network, useful for testing
    public static void reset() {
        network = new SocialNetwork();
    }

    // Set the NetworkAnalyzer's network to the given network
    public static void setNetwork(SocialNetwork network) {
        Objects.requireNonNull(network, "Network cannot be null!");
        // Implemented feedback from last time to check for null
        NetworkAnalyzer.network = network;
    }

    // Find the top k influencers in the network
    public static List<Person> findInfluencers(int k) throws IllegalArgumentException {
        // Ensure that k > 0
        if (k <= 0) {
            throw new IllegalArgumentException("k must be a positive integer!");
        }
        // List mapping people with their influence score
        List<Map.Entry<Person, Integer>> scores = new ArrayList<>();
        // Iterate over each person in the network
        for (Person person : network.getPeople().values()) {
            // Calculate the influence score for the person
            int score = calculateInfluenceScore(person);
            // Add the person and their score to the list
            scores.add(Map.entry(person, score));
        }
        // Sort the list based on the influence score in descending order
        scores.sort((a, b) -> b.getValue() - a.getValue());
        // List to store the top k influencers
        List<Person> influencers = new ArrayList<>();
        // Add the top k influencers to the list
        for (int i = 0; i < k && i < scores.size(); i++) {
            influencers.add(scores.get(i).getKey());
        }
        return influencers;
    }

    // Calculate the influence score for a person
    private static int calculateInfluenceScore(Person person) {
        // Implemented feedback from last time to check for null
        Objects.requireNonNull(person, "Person cannot be null!");
        // Get the connections for the person
        List<Integer> connections = person.getConnections();
        // Initialize the influence score
        int score = connections.size();
        // Iterate over each connection
        for (int connection : connections) {
            // Get the connection Person object
            Person connectedPerson = network.getPerson(connection);
            // Add the connection person's connections to the score
            score += connectedPerson.getConnections().size();
            // Remove the connection leading to the initial person
            score--;
        }
        // Total score is the number of direct connections plus the number of connections of each connection
        return score;
    }

    // Check base cases for breadth-first search, used to reduce complexity while maintaining pseudocode structure
    private static List<Person> checkBFSBaseCases(int id1, int id2) {
        // Default cases
        if (id1 == id2) { // If the ids are the same, return a list with just one person
            return List.of(network.getPerson(id1));
        }
        // This is a change I made to my pseudocode--I figured it makes more sense to return an empty list over null
        if (network.getPerson(id1) == null || network.getPerson(id2) == null) {
            return List.of();
        }
        return null;
    }

    // Explore the neighbors of the last node in the path, used to reduce complexity while maintaining pseudocode structure
    private static void exploreNeighbors(List<Person> currentPath, Queue<List<Person>> queue, Set<Person> visited) {
        Objects.requireNonNull(currentPath, "Current path cannot be null!");
        Objects.requireNonNull(queue, "Queue cannot be null!");
        Objects.requireNonNull(visited, "Visited set cannot be null!");
        Person lastNode = currentPath.get(currentPath.size() - 1); // Get the last node in the path
        for (Integer connection : lastNode.getConnections()) { // For each connection of the last node
            Person connectedPerson = network.getPerson(connection);
            if (!visited.contains(connectedPerson)) { // If the connected person has not been visited
                // Create a new path with the connected person and add it to the queue
                List<Person> newPath = new ArrayList<>(currentPath);
                newPath.add(connectedPerson);
                queue.add(newPath);
                visited.add(connectedPerson);
            }
        }
    }

    // Find the shortest path between two people
    public static List<Person> shortestPath(int id1, int id2) {
        // Split base cases into separate method to reduce complexity
        List<Person> baseCases = checkBFSBaseCases(id1, id2);
        if (baseCases != null) {
            return baseCases;
        }
        // Empty queue of lists of Person objects (paths)
        Queue<List<Person>> queue = new LinkedList<>();
        // Empty list of Person objects (visited people)
        Set<Person> visited = new HashSet<>();
        // Enqueue the path containing just the starting person
        queue.add(List.of(network.getPerson(id1)));
        // Add this person to the visited set
        visited.add(network.getPerson(id1));
        // Begin BFS loop
        while (!queue.isEmpty()) { // While there are still paths to explore
            // Dequeue the current path
            List<Person> currentPath = queue.poll();
            Person lastNode = currentPath.get(currentPath.size() - 1);
            // If the last node is the person with id2, we have found the shortest path
            if (lastNode.getId() == id2) {
                return currentPath;
            }
            // Otherwise, explore the neighbors of the last node
            exploreNeighbors(currentPath, queue, visited);
        }
        // No path found
        return List.of();
    }

    public static SocialNetwork getNetwork() {
        return network;
    }

}
