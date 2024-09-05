package cycling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team in the cycling portal.
 */
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private List<Rider> riders;

    /**
     * Constructs a new Team.
     * 
     * @param id The unique ID of the team.
     * @param name The name of the team.
     * @param description The description of the team.
     */
    public Team(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.riders = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the team.
     * 
     * @return The team ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the team.
     * 
     * @return The team name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the team.
     * 
     * @return The team description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the list of riders in the team.
     * 
     * @return A list of riders.
     */
    public List<Rider> getRiders() {
        return riders;
    }

    /**
     * Adds a rider to the team.
     * 
     * @param rider The rider to add.
     */
    public void addRider(Rider rider) {
        riders.add(rider);
    }
}
