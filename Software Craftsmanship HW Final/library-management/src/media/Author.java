package media;

import java.util.Date;

public class Author {
    private String id;
    private final String name;
    private final String biography;
    private final Date birthDate;
    private final boolean active;

    public Author(String id, String name, String biography, Date birthDate) {
        // All information must be entered
        if (id == null | name == null | biography == null | birthDate == null) {
            throw new IllegalArgumentException("Author must have an ID, name, biography, and birth date.");
        }
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.birthDate = birthDate;
        // This could be extended to update when the author hasn't published anything in a specific amount of time
        this.active = true;
    }

    public String getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBiography() {
        return biography;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public boolean isActive() {
        return active;
    }
}
