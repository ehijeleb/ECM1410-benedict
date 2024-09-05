package cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Represents the results of a rider in a stage.
 */
public class Results implements Serializable {
    private static final long serialVersionUID = 1L;

    private int riderId;
    private int stageId;
    private LocalTime[] checkpointTimes;

    /**
     * Constructs a new Results object.
     * 
     * @param riderId The ID of the rider.
     * @param stageId The ID of the stage.
     * @param checkpointTimes The times at which the rider reached each checkpoint, including the start and finish times.
     */
    public Results(int riderId, int stageId, LocalTime[] checkpointTimes) {
        this.riderId = riderId;
        this.stageId = stageId;
        this.checkpointTimes = checkpointTimes;
    }

    /**
     * Gets the ID of the rider.
     * 
     * @return The rider ID.
     */
    public int getRiderId() {
        return riderId;
    }

    /**
     * Gets the ID of the stage.
     * 
     * @return The stage ID.
     */
    public int getStageId() {
        return stageId;
    }

    /**
     * Gets the times at which the rider reached each checkpoint.
     * 
     * @return An array of checkpoint times.
     */
    public LocalTime[] getCheckpointTimes() {
        return checkpointTimes;
    }

    /**
     * Returns a string representation of the Results object.
     * 
     * @return A string representation of the Results object.
     */
    @Override
    public String toString() {
        return "Results{" +
                "riderId=" + riderId +
                ", stageId=" + stageId +
                ", checkpointTimes=" + Arrays.toString(checkpointTimes) +
                '}';
    }
}
