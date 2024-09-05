package cycling;

import java.io.Serializable;

/**
 * Represents a rider in the cycling portal.
 */
public class Rider implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private int yearOfBirth;

    /**
     * Constructs a new Rider.
     * 
     * @param id The unique ID of the rider.
     * @param name The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     */
    public Rider(int id, String name, int yearOfBirth) {
        this.id = id;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    /**
     * Gets the unique identifier of the rider.
     * 
     * @return The rider ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the rider.
     * 
     * @return The rider name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the year of birth of the rider.
     * 
     * @return The year of birth of the rider.
     */
    public int getYearOfBirth() {
        return yearOfBirth;
    }
}
