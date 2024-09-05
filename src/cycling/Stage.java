package cycling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stage in a race.
 */
public class Stage implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private double length;
    private LocalDateTime startTime;
    private StageType type;
    private List<Checkpoint> checkpoints;
    private boolean waitingForResults;

    /**
     * Constructs a new Stage.
     * 
     * @param id The unique ID of the stage.
     * @param name The name of the stage.
     * @param description The description of the stage.
     * @param length The length of the stage in kilometers.
     * @param startTime The start time of the stage.
     * @param type The type of the stage.
     */
    public Stage(int id, String name, String description, double length, LocalDateTime startTime, StageType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        this.checkpoints = new ArrayList<>();
        this.waitingForResults = false;
    }

    /**
     * Gets the unique identifier of the stage.
     * 
     * @return The stage ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the stage.
     * 
     * @return The stage name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the stage.
     * 
     * @return The stage description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the length of the stage in kilometers.
     * 
     * @return The stage length.
     */
    public double getLength() {
        return length;
    }

    /**
     * Gets the start time of the stage.
     * 
     * @return The stage start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the type of the stage.
     * 
     * @return The stage type.
     */
    public StageType getType() {
        return type;
    }

    /**
     * Checks if the stage is waiting for results.
     * 
     * @return True if the stage is waiting for results, false otherwise.
     */
    public boolean isWaitingForResults() {
        return waitingForResults;
    }

    /**
     * Gets the list of checkpoints in the stage.
     * 
     * @return A list of checkpoints.
     */
    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    /**
     * Sets the waiting for results status of the stage.
     * 
     * @param waitingForResults The new waiting for results status.
     */
    public void setWaitingForResults(boolean waitingForResults) {
        this.waitingForResults = waitingForResults;
    }

    /**
     * Adds a checkpoint to the stage.
     * 
     * @param checkpoint The checkpoint to be added.
     */
    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }
}
