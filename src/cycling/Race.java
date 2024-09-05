package cycling;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a race in the cycling portal.
 * A race contains multiple stages and holds information about the race name and description.
 */
public class Race implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private List<Stage> stages;

    /**
     * Constructs a new Race.
     * 
     * @param id The unique identifier for the race.
     * @param name The name of the race.
     * @param description A description of the race.
     */
    public Race(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stages = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the race.
     * 
     * @return The race ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the race.
     * 
     * @return The race name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the race.
     * 
     * @return The race description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the list of stages in the race.
     * 
     * @return A list of stages.
     */
    public List<Stage> getStages() {
        return stages;
    }

    /**
     * Adds a stage to the race.
     * 
     * @param stage The stage to be added.
     */
    public void addStage(Stage stage) {
        stages.add(stage);
    }
}
