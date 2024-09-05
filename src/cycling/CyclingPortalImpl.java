package cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CyclingPortalImpl implements MiniCyclingPortal {
	private static final long serialVersionUID = 1L;

	//Maps to store races, teams, riders, stages, checkpoints and results
	private Map<Integer, Race> races = new HashMap<>();
	private Map<Integer, Team> teams = new HashMap<>();
	private Map<Integer, Rider> riders = new HashMap<>();
	private Map<Integer, Stage> stages = new HashMap<>();
	private Map<Integer, Checkpoint> checkpoints = new HashMap<>();
	private Map<Integer, Map<Integer, Results>> results = new HashMap<>();
	private Map<Integer, Map<Integer, Integer>> points = new HashMap<>();
	private Map<Integer, Map<Integer, Integer>> mountainPoints = new HashMap<>();

	//counters for generating unique IDs
	private int raceIdCounter = 1;
	private int stageIdCounter = 1;
	private int teamIdCounter = 1;
	private int riderIdCounter = 1;
	private int checkpointIdCounter = 1;

	@Override
	public int[] getRaceIds() {
		return races.keySet().stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		if (name == null || name.isEmpty() || name.length() > 30 || name.contains(" ")) {
			throw new InvalidNameException("Invaldi race name");
		}

		// throws IllegalNameException if the name already exists in Race map
		if (races.values().stream().anyMatch(race -> race.getName().equals(name))) {
			throw new IllegalNameException();
		}
		Race newRace = new Race(raceIdCounter, name, description);
		races.put(raceIdCounter, newRace);
		return raceIdCounter++;
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {

		// retrieve the race from the map
		Race race = races.get(raceId);

		// check if the race exists
		if (race == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// calculate the number of stages and the total length of all stages
		int numberOfStages = race.getStages().size();
		double totalLength = race.getStages().stream().mapToDouble(Stage::getLength).sum();

		// return a formatted string with the race details
		return String.format("Race ID: %d\nName: %s\nDescription: %s\nNumber of Stages: %d\nTotal Length: %.2f km",
				raceId, race.getName(), race.getDescription(), numberOfStages, totalLength);
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {

		// check if the race exists
		if (!races.containsKey(raceId)) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// remove the race and its related info
		races.remove(raceId);

	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {

		// retrieve the race from the map
		Race race = races.get(raceId);

		// check if the race exists
		if (race == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// return the number of stages in the race
		return race.getStages().size();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {

		// retrieve the race from the map
		Race race = races.get(raceId);

		// check if the race exists
		if (race == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// Validate the stage name
		if (stageName == null || stageName.isEmpty() || stageName.length() > 30 || stageName.contains(" ")) {
			throw new InvalidNameException("Invalid Stage Name");
		}

		// Check if the stage name already exists in the race
		for (Stage stage : race.getStages()) {
			if (stage.getName().equals(stageName)) {
				throw new IllegalNameException("Stage name " + stageName + " already exists in the race.");
			}
		}

		// Validate the length
		if (length < 5) {
			throw new InvalidLengthException("stage length must be less than 5");
		}

		// Create a new stage
		Stage newStage = new Stage(stageIdCounter, stageName, description, length, startTime, type);

		// Add the stage to the race
		race.addStage(newStage);

		// Return the unique ID of the created stage
		return stageIdCounter++;
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// retrieve the race from the map
		Race race = races.get(raceId);

		// check if the race exists
		if (race == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// retrieve the list of stage IDs
		List<Integer> stageIds = race.getStages().stream().map(Stage::getId).collect(Collectors.toList());

		// Convert the list to an array and return
		return stageIds.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		// retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// return the length of the stage
		return stage.getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		// retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// remove the stage from all races that contain it
		for (Race race : races.values()) {
			race.getStages().removeIf(s -> s.getId() == stageId);
		}

		// remove the stage from the stages map
		stages.remove(stageId);

	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {

		// retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// check if the stage is a time trial
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain checkpoints0");
		}

		// check if the stage is "waiting for results"
		if (stage.isWaitingForResults()) {
			throw new InvalidStageStateException("Stage is waiting for results and cannot be modified");
		}

		// validate the location
		if (length < 0 || length > stage.getLength()) {
			throw new InvalidLocationException("Location is out of bounds of the stage length");

		}

		// create a new checkpoint
		Checkpoint newCheckpoint = new Checkpoint(checkpointIdCounter, length, type, averageGradient);

		// add the checkpoint to the stage
		stage.addCheckpoint(newCheckpoint);

		// store the checkpoint in the checkpoints map
		checkpoints.put(checkpointIdCounter, newCheckpoint);

		// return checkpointId and increment counter
		return checkpointIdCounter++;

	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {

		// retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// check if the stage is a time trial
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain checkpoints0");
		}

		// check if the stage is "waiting for results"
		if (stage.isWaitingForResults()) {
			throw new InvalidStageStateException("Stage is waiting for results and cannot be modified");

		}

		// validate the location
		if (location < 0 || location > stage.getLength()) {
			throw new InvalidLocationException("Location is out of bounds of the stage length");

		}

		// create a new checkpoint
		Checkpoint newCheckpoint = new Checkpoint(checkpointIdCounter, location, CheckpointType.SPRINT, 0);

		// add the checkpoint to the stage
		stage.addCheckpoint(newCheckpoint);

		// store the checkpoint in the checkpoints map
		checkpoints.put(checkpointIdCounter, newCheckpoint);

		// return checkpoint ID and increment the counter
		return checkpointIdCounter++;
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		// retrieve the checkpoint from the map
		Checkpoint checkpoint = checkpoints.get(checkpointId);

		// check if the checkpoint exists
		if (checkpoint == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// find the stage cotnaining the checkpoint
		Stage stageContainingCheckpoint = null;
		for (Stage stage : stages.values()) {
			if (stage.getCheckpoints().contains(checkpoint)) {
				stageContainingCheckpoint = stage;
				break;
			}
		}

		// if no stage contains the checkpoint, it is an error in the data integrity
		if (stageContainingCheckpoint == null) {
			throw new IDNotRecognisedException("Id not associated with a stage");
		}

		// check if the stage is waiting for results
		if (stageContainingCheckpoint.isWaitingForResults()) {
			throw new InvalidStageStateException("stage is waiting for resultsand cannot be modified");
		}

		// remove the checkpoint from the stage
		stageContainingCheckpoint.getCheckpoints().remove(checkpoint);

		// remove the checkpoint from the checkpoints map
		checkpoints.remove(checkpointId);

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {

		// retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exosts
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// check if the stage is already in "waiting for results" state
		if (stage.isWaitingForResults()) {
			throw new InvalidStageStateException("Stage is already waiting for results");
		}

		// conclude the preparation of the stage
		stage.setWaitingForResults(true);

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
		// retieve the stage from the map
		Stage stage = stages.get(stageId);

		// check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// retieve the list of checkpoint IDs ordered by their location
		List<Integer> checkpointIds = stage.getCheckpoints().stream()
				.sorted((c1, c2) -> Double.compare(c1.getLocation(), c2.getLocation()))
				.map(Checkpoint::getId)
				.collect(Collectors.toList());

		// convert the list to an array and return
		return checkpointIds.stream().mapToInt(Integer::intValue).toArray();

	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {

		// validate the team name
		if (name == null || name.isEmpty() || name.length() > 30 || name.contains(" ")) {
			throw new InvalidNameException("Invalid Team Nmae");
		}

		// Check if the team name already exists
		for (Team team : teams.values()) {
			if (team.getName().equals(name)) {
				throw new IllegalNameException("team name alredy exists");
			}
		}

		// create a new team
		Team newTeam = new Team(teamIdCounter, name, description);

		// add the team to the teams map
		teams.put(teamIdCounter, newTeam);

		// return the team Id and increment counter
		return teamIdCounter++;
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// retrieve the team from the map
		Team team = teams.get(teamId);

		// check if the team exists
		if (team == null) {
			throw new IDNotRecognisedException("id not recognised");
		}

		// remove the team from the teams map
		teams.remove(teamId);
	}

	@Override
	public int[] getTeams() {

		// Retrieve the list of team IDs
		List<Integer> teamIds = teams.keySet().stream().collect(Collectors.toList());

		// Convert the list to an array and return
		return teamIds.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {

		// Retrieve the team from the map
		Team team = teams.get(teamId);

		// Check if the team exists
		if (team == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// Retrieve the list of rider IDs
		List<Integer> riderIds = team.getRiders().stream()
				.map(Rider::getId)
				.collect(Collectors.toList());

		// Convert the list to an array and return
		return riderIds.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// Validate the rider's name and year of birth
		if (name == null || name.isEmpty() || yearOfBirth < 1900) {
			throw new IllegalArgumentException("Invalid rider name or year of birth.");
		}

		// Retrieve the team from the map
		Team team = teams.get(teamID);

		// Check if the team exists
		if (team == null) {
			throw new IDNotRecognisedException("Id not recognised");
		}

		// Create a new rider
		Rider newRider = new Rider(riderIdCounter, name, yearOfBirth);

		// Add the rider to the team
		team.addRider(newRider);

		// Store the rider in the riders map
		riders.put(riderIdCounter, newRider);

		// return riderId and increment the counter
		return riderIdCounter++;
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// Retrieve the rider from the map
		Rider rider = riders.get(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("ID not recognised");
		}

		// Remove the rider from the team
		for (Team team : teams.values()) {
			team.getRiders().removeIf(r -> r.getId() == riderId);
		}

		// Remove all results of the rider
		for (Map<Integer, Results> stageResults : results.values()) {
			stageResults.remove(riderId);
		}

		// Remove the rider from the riders map
		riders.remove(riderId);

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage Id not recognised");
		}

		// Retrieve the rider from the map
		Rider rider = riders.get(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider ID not recognised");
		}

		// Check if the stage is "waiting for results"
		if (!stage.isWaitingForResults()) {
			throw new InvalidStageStateException("Stage is not waiting for results.");
		}

		// Check if the rider already has a result for the stage
		Map<Integer, Results> stageResults = results.get(stageId);
		if (stageResults != null && stageResults.containsKey(riderId)) {
			throw new DuplicatedResultException("Rider ID already has result for stage");
		}

		// Validate the number of checkpoint times
		int expectedCheckpointTimes = stage.getCheckpoints().size() + 2;
		if (checkpoints.length != expectedCheckpointTimes) {
			throw new InvalidCheckpointTimesException("Invalid number of checkpoint times. Expected ");
		}

		// Store the result
		Results newResult = new Results(riderId, stageId, checkpoints);
		if (stageResults == null) {
			stageResults = new HashMap<>();
			results.put(stageId, stageResults);
		}
		stageResults.put(riderId, newResult);
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised.");
		}

		// Retrieve the rider from the map
		Rider rider = riders.get(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider ID not recognised.");
		}

		// Retrieve the rider's results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);
		if (stageResults == null || !stageResults.containsKey(riderId)) {
			return new LocalTime[0]; // Return an empty array if there is no result registered for the rider in the
										// stage
		}

		Results riderResults = stageResults.get(riderId);
		LocalTime[] checkpointTimes = riderResults.getCheckpointTimes();

		// Calculate the total elapsed time
		LocalTime startTime = checkpointTimes[0];
		LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
		LocalTime elapsedTime = finishTime.minusHours(startTime.getHour())
				.minusMinutes(startTime.getMinute())
				.minusSeconds(startTime.getSecond());

		// Create a new array including the elapsed time
		LocalTime[] resultWithElapsedTime = new LocalTime[checkpointTimes.length + 1];
		System.arraycopy(checkpointTimes, 0, resultWithElapsedTime, 0, checkpointTimes.length);
		resultWithElapsedTime[checkpointTimes.length] = elapsedTime;

		return resultWithElapsedTime;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised.");
		}

		// Retrieve the rider from the map
		Rider rider = riders.get(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider ID not recognised.");
		}

		// Retrieve the rider's results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);
		if (stageResults == null || !stageResults.containsKey(riderId)) {
			return null; // Return null if there is no result registered for the rider in the stage
		}

		Results riderResults = stageResults.get(riderId);
		LocalTime[] checkpointTimes = riderResults.getCheckpointTimes();

		// Calculate the real elapsed time
		LocalTime startTime = checkpointTimes[0];
		LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
		LocalTime realElapsedTime = finishTime.minusHours(startTime.getHour())
				.minusMinutes(startTime.getMinute())
				.minusSeconds(startTime.getSecond());

		// Check if the stage is a time-trial
		if (stage.getType() == StageType.TT) {
			return realElapsedTime; // No adjustments for time-trials
		}

		// Retrieve and sort all finish times for the stage
		List<LocalTime> finishTimes = new ArrayList<>();
		for (Results result : stageResults.values()) {
			LocalTime[] times = result.getCheckpointTimes();
			finishTimes.add(times[times.length - 1]);
		}
		finishTimes.sort(Comparator.naturalOrder());

		// Find the adjusted elapsed time
		LocalTime adjustedElapsedTime = realElapsedTime;
		for (int i = 0; i < finishTimes.size(); i++) {
			LocalTime currentFinishTime = finishTimes.get(i);
			LocalTime nextFinishTime = (i + 1 < finishTimes.size()) ? finishTimes.get(i + 1) : null;

			if (currentFinishTime.equals(finishTime)) {
				break;
			}

			if (nextFinishTime != null && nextFinishTime.isBefore(finishTime.plusSeconds(1))) {
				adjustedElapsedTime = nextFinishTime.minusHours(startTime.getHour())
						.minusMinutes(startTime.getMinute())
						.minusSeconds(startTime.getSecond());
			} else {
				break;
			}
		}

		return adjustedElapsedTime;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID  not recognised");
		}

		// Retrieve the rider from the map
		Rider rider = riders.get(riderId);

		// Check if the rider exists
		if (rider == null) {
			throw new IDNotRecognisedException("Rider ID not recognised");
		}

		// Retrieve the results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);

		// Check if there are results for the stage and if the rider has results for the
		// stage
		if (stageResults == null || !stageResults.containsKey(riderId)) {
			throw new IDNotRecognisedException(
					"No results found for Rider ID " + riderId + " in Stage ID " + stageId + ".");
		}

		// Remove the rider's results for the stage
		stageResults.remove(riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised");
		}

		// Retrieve the results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);

		// Check if there are results for the stage
		if (stageResults == null || stageResults.isEmpty()) {
			return new int[0]; // Return an empty array if there are no results for the stage
		}

		// Create a list of rider IDs sorted by their elapsed time
		List<Map.Entry<Integer, Results>> sortedResults = new ArrayList<>(stageResults.entrySet());
		sortedResults.sort(Comparator.comparing(entry -> {
			LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
			LocalTime startTime = checkpointTimes[0];
			LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
			return finishTime.toSecondOfDay() - startTime.toSecondOfDay();
		}));

		// Extract the sorted rider IDs
		List<Integer> sortedRiderIds = sortedResults.stream()
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		// Convert the list to an array and return
		return sortedRiderIds.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID  not recognised");
		}

		// Retrieve the results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);

		// Check if there are results for the stage
		if (stageResults == null || stageResults.isEmpty()) {
			return new LocalTime[0]; // Return an empty array if there are no results for the stage
		}

		// Create a list of rider results sorted by their finish time
		List<Map.Entry<Integer, Results>> sortedResults = new ArrayList<>(stageResults.entrySet());
		sortedResults.sort(Comparator.comparing(entry -> {
			LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
			LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
			return finishTime.toSecondOfDay();
		}));

		// Calculate adjusted elapsed times
		List<LocalTime> adjustedElapsedTimes = new ArrayList<>();
		LocalTime previousFinishTime = null;

		for (Map.Entry<Integer, Results> entry : sortedResults) {
			LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
			LocalTime startTime = checkpointTimes[0];
			LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
			LocalTime realElapsedTime = finishTime.minusHours(startTime.getHour())
					.minusMinutes(startTime.getMinute())
					.minusSeconds(startTime.getSecond());

			// Adjusted elapsed time logic
			LocalTime adjustedElapsedTime = realElapsedTime;
			if (previousFinishTime != null && finishTime.isBefore(previousFinishTime.plusSeconds(1))) {
				adjustedElapsedTime = previousFinishTime.minusHours(startTime.getHour())
						.minusMinutes(startTime.getMinute())
						.minusSeconds(startTime.getSecond());
			} else {
				previousFinishTime = finishTime;
			}

			adjustedElapsedTimes.add(adjustedElapsedTime);
		}

		// Convert the list to an array and return
		return adjustedElapsedTimes.toArray(new LocalTime[0]);
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised");
		}

		// Retrieve the results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);

		// Check if there are results for the stage
		if (stageResults == null || stageResults.isEmpty()) {
			return new int[0]; // Return an empty array if there are no results for the stage
		}

		// Retrieve the points for the stage
		Map<Integer, Integer> stagePoints = points.get(stageId);

		// Create a list of rider results sorted by their elapsed time
		List<Map.Entry<Integer, Results>> sortedResults = new ArrayList<>(stageResults.entrySet());
		sortedResults.sort(Comparator.comparing(entry -> {
			LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
			LocalTime startTime = checkpointTimes[0];
			LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
			return finishTime.toSecondOfDay() - startTime.toSecondOfDay();
		}));

		// Extract the points of the sorted riders
		List<Integer> sortedPoints = new ArrayList<>();
		for (Map.Entry<Integer, Results> entry : sortedResults) {
			int riderId = entry.getKey();
			int riderPoints = (stagePoints != null && stagePoints.containsKey(riderId)) ? stagePoints.get(riderId) : 0;
			sortedPoints.add(riderPoints);
		}

		// Convert the list to an array and return
		return sortedPoints.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// Retrieve the stage from the map
		Stage stage = stages.get(stageId);

		// Check if the stage exists
		if (stage == null) {
			throw new IDNotRecognisedException("Stage ID not recognised");
		}

		// Retrieve the results for the stage
		Map<Integer, Results> stageResults = results.get(stageId);

		// Check if there are results for the stage
		if (stageResults == null || stageResults.isEmpty()) {
			return new int[0]; // Return an empty array if there are no results for the stage
		}

		// Retrieve the mountain points for the stage
		Map<Integer, Integer> stageMountainPoints = mountainPoints.get(stageId);

		// Create a list of rider results sorted by their elapsed time
		List<Map.Entry<Integer, Results>> sortedResults = new ArrayList<>(stageResults.entrySet());
		sortedResults.sort(Comparator.comparing(entry -> {
			LocalTime[] checkpointTimes = entry.getValue().getCheckpointTimes();
			LocalTime startTime = checkpointTimes[0];
			LocalTime finishTime = checkpointTimes[checkpointTimes.length - 1];
			return finishTime.toSecondOfDay() - startTime.toSecondOfDay();
		}));

		// Extract the mountain points of the sorted riders
		List<Integer> sortedMountainPoints = new ArrayList<>();
		for (Map.Entry<Integer, Results> entry : sortedResults) {
			int riderId = entry.getKey();
			int riderMountainPoints = (stageMountainPoints != null && stageMountainPoints.containsKey(riderId))
					? stageMountainPoints.get(riderId)
					: 0;
			sortedMountainPoints.add(riderMountainPoints);
		}

		// Convert the list to an array and return
		return sortedMountainPoints.stream().mapToInt(Integer::intValue).toArray();
	}

	@Override
	public void eraseCyclingPortal() {
		// Clear all maps
		races.clear();
		teams.clear();
		riders.clear();
		stages.clear();
		results.clear();
		points.clear();
		mountainPoints.clear();

		// Reset all counters
		raceIdCounter = 1;
		teamIdCounter = 1;
		riderIdCounter = 1;
		stageIdCounter = 1;
		checkpointIdCounter = 1;

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// Create an object output stream to write the data to the file
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			// Write the current state of the CyclingPortal to the file
			oos.writeObject(this);
		} catch (IOException e) {
			// If an exception occurs, rethrow it
			throw new IOException("Error saving CyclingPortal to file: " + filename, e);
		}

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// Create an object input stream to read the data from the file
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			// Read the CyclingPortalImpl object from the file
			CyclingPortalImpl loadedPortal = (CyclingPortalImpl) ois.readObject();

			// Replace the current instance variables with those from the loaded portal
			this.races = loadedPortal.races;
			this.teams = loadedPortal.teams;
			this.riders = loadedPortal.riders;
			this.stages = loadedPortal.stages;
			this.results = loadedPortal.results;
			this.points = loadedPortal.points;
			this.mountainPoints = loadedPortal.mountainPoints;
			this.raceIdCounter = loadedPortal.raceIdCounter;
			this.teamIdCounter = loadedPortal.teamIdCounter;
			this.riderIdCounter = loadedPortal.riderIdCounter;
			this.stageIdCounter = loadedPortal.stageIdCounter;
			this.checkpointIdCounter = loadedPortal.checkpointIdCounter;
		} catch (IOException | ClassNotFoundException e) {
			// If an exception occurs, rethrow it
			throw e;
		}
	}

}
