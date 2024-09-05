package cycling;

import java.io.Serializable;

/**
 * Represents a checkpoint within a stage.
 */
public class Checkpoint implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private double location;
    private CheckpointType type;
    private double averageGradient;

    /**
     * Constructor to initialize a checkpoint.
     * 
     * @param id The unique ID of the checkpoint.
     * @param location The kilometre location where the checkpoint is placed.
     * @param type The category of the checkpoint.
     * @param averageGradient The average gradient for the checkpoint.
     */
    public Checkpoint(int id, double location, CheckpointType type, double averageGradient) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
    }

    // Getters for checkpoint attributes
    public int getId() {
        return id;
    }

    public double getLocation() {
        return location;
    }

    public CheckpointType getType() {
        return type;
    }

    public double getAverageGradient() {
        return averageGradient;
    }
}
