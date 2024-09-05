import cycling.CheckpointType;
import cycling.CyclingPortalImpl;
import cycling.IDNotRecognisedException;
import cycling.IllegalNameException;
import cycling.InvalidCheckpointTimesException;
import cycling.InvalidLengthException;
import cycling.InvalidLocationException;
import cycling.InvalidNameException;
import cycling.InvalidStageStateException;
import cycling.InvalidStageTypeException;
import cycling.StageType;
import cycling.DuplicatedResultException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CyclingPortalTest {
    private static CyclingPortalImpl portal = new CyclingPortalImpl();

    public static void main(String[] args) {
        System.out.println("The system compiled and started the execution...");
        testGetRaceIds();
        testCreateRace();
        testViewRaceDetails();
        testRemoveRaceById();
        testGetNumberOfStages();
        testAddStageToRace();
        testGetRaceStages();
        testGetStageLength();
        testRemoveStageById();
        testAddCategorizedclimbToStage();
        testAddIntermediateSprintToStage();
        testRemoveCheckpoint();
        testConcludeStagePreparation();
        testGetStageCheckpoints();
        testCreateTeam();
        testRemoveTeam();
        testGetTeams();
        testGetTeamRiders();
        testCreateRider();
        testRemoveRider();
        testRegisterRiderResultsInStage();
        testGetRiderResultsInStage();
        testGetRiderAdjustedElapsedTimeInStage();
        testDeleteRiderResultsInStage();
        testGetRidersRankInStage();
        testGetRankedAdjustedElapsedTimesInStage();
        testGetRidersPointsInStage();
        testGetRidersMountainPointsInStage();
        testEraseCyclingPortal();
        testSaveCyclingPortal();
        testLoadCyclingPortal();
    }

    private static void testGetRaceIds() {
        System.out.println("The system is testing the getRaceIds method...");
        // test getRaceIds when no races have been created
        assert (portal.getRaceIds().length == 0)
                : "Initial Portal not empty as required or not returning empty array";
    }

    private static void testCreateRace() {
        System.out.println("The system is testing the createRace method...");
        // test createRace with valid name and description
        try {
            int raceId = portal.createRace("RaceOne", "My first race");
            assert (portal.getRaceIds().length == 1)
                    : "Portal should have one race after creating a race";
            assert (portal.getRaceIds()[0] == raceId)
                    : "The created race ID should be in the race IDs array.";
        } catch (InvalidNameException | IllegalNameException e) {
            // print the stack trace if an exception is thrown
            e.printStackTrace();
        }

        // test createRace with invalid name(null)
        try {
            portal.createRace(null, "Invalid Race");
            assert (false)
                    : "InvalidNameException should have been thrown";
        } catch (InvalidNameException e) {
            // expected exception
        } catch (IllegalNameException e) {
            e.printStackTrace();
        }

        // test createRace with invalid name(empty)
        try {
            portal.createRace("", "InvalidRace");
            assert (false)
                    : "InvalidNameException should have been thrown";
        } catch (InvalidNameException e) {
            // expected exception
        } catch (IllegalNameException e) {
            e.printStackTrace();
        }

        // test createRace with invalid name (too long)
        try {
            portal.createRace("This race is exceeds the length of thirty characters so it is too long", "InvalidRace");
            assert (false) : "InvalidNameException should have been thrown";
        } catch (InvalidNameException f) {
            // expected exception
        } catch (IllegalNameException f) {
            f.printStackTrace();
        }

        // test createRace with duplicate name
        try {
            portal.createRace("RaceOne", "Duplicate");
            assert (false)
                    : "IllegalNameException should have been thrown";
        } catch (IllegalNameException g) {
        } catch (InvalidNameException g) {
            g.printStackTrace();
        }

    }

    private static void testViewRaceDetails() {
        // Create a race for testing

        System.out.println("The system is testing the viewRaceDetails method...");
        int raceId = 0;
        try {
            raceId = portal.createRace("RaceTwo", "My Second");
        } catch (IllegalNameException | InvalidNameException e) {
            // Print the stack trace if an exception is thrown
            e.printStackTrace();
        }

        // Test viewRaceDetails with valid race ID
        try {
            String details = portal.viewRaceDetails(raceId);
            assert details.contains("RaceOne") : "Race details should contain the race name.";
            assert details.contains("My first race") : "Race details should contain the race description.";
        } catch (IDNotRecognisedException e) {
            // Print the stack trace if an exception is thrown
            e.printStackTrace();
        }

        // Test viewRaceDetails with invalid race ID
        try {
            portal.viewRaceDetails(999);
            assert false : "Expected IDNotRecognisedException for invalid race ID";
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testRemoveRaceById() {
        System.out.println("The system is testing the removeRaceById method...");

        // test removeRaceById with valid race ID
        try {
            int raceId = portal.createRace("RaceThree", "My Third");
            portal.removeRaceById(raceId);
            assert (portal.getRaceIds().length == 0)
                    : "Portal should be empty after removing";
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        } catch (IllegalNameException e) {
            e.printStackTrace();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }

        // test removeRaceById with invalid race ID
        try {
            portal.removeRaceById(999);
            assert (false)
                    : "Expected IDNotRecognisedException for invalid";
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testGetNumberOfStages() {
        System.out.println("The system is testing the getNumberOfStages method...");
        // test getNumberOfStages with no stages

        try {
            int numStages = portal.getNumberOfStages(1);
            assert (numStages == 0)
                    : "Expected 0 stages";
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        }
    }

    private static void testAddStageToRace() {
        System.out.println("The system is testing the addStageToRace method...");

        int stageId = 0;
        try {
            stageId = portal.addStageToRace(1, "StageOne", "First Stage", 10.0, LocalDateTime.now(), StageType.FLAT);
            assert (portal.getNumberOfStages(1) == 1)
                    : "Expected 1 stage";
        } catch (IDNotRecognisedException e) {
            e.printStackTrace();
        } catch (InvalidNameException e) {
            e.printStackTrace();
        } catch (IllegalNameException e) {
            e.printStackTrace();
        } catch (InvalidLengthException e) {
            e.printStackTrace();
        }
    }

    private static void testGetRaceStages() {
        System.out.println("The system is testing the getRaceStages method...");

        // create a race and add stages to it
        try {
            int raceId = portal.createRace("RaceFour", "Race number four");
            portal.addStageToRace(raceId, "StageOne", "First stage", 10.0, LocalDateTime.now(), StageType.FLAT);
            portal.addStageToRace(raceId, "StageTwo", "Second stage", 20.0, LocalDateTime.now(),
                    StageType.MEDIUM_MOUNTAIN);
            // test getRaceStages with valid race ID
            int[] stageIds = portal.getRaceStages(raceId);
            assert (stageIds.length == 2)
                    : "Expected 2 stages";

            // test getRaceStages with invalid race ID

            try {
                portal.getRaceStages(999);
                assert (false)
                        : "Expected IDNotRecognisedException for invalid ID";
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }

        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown";
        }
    }

    private static void testGetStageLength() {
        System.out.println("The system is testing the getStageLength method...");
        try {
            // create a race and add a stage to it
            int raceId = portal.createRace("RaceFive", "Race number five");
            int stageId = portal.addStageToRace(raceId, "StageThree", "Third stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // call getStageLength with valid stage ID
            double length = portal.getStageLength(stageId);

            // check that the returned length is correct
            assert (length == 10.0)
                    : "Expected length of 10.0";

            // call getStageLength with invalid stage ID
            boolean exceptionThrown = false;
            try {
                portal.getStageLength(999);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testRemoveStageById() {
        System.out.println("The system is testing the removeStageById method...");
        try {
            // create a race amd add a stage to it
            int raceId = portal.createRace("RaceSix", "Race number six");
            int stageId = portal.addStageToRace(raceId, "StageFour", "Fourth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // call remove stage by ID with valid stage ID
            portal.removeStageById(stageId);

            // check that the stage has been removed
            boolean exceptionThrown = false;
            try {
                portal.getStageLength(stageId);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for removed stage";

            // call removeStageById with invalid stage ID
            exceptionThrown = false;
            try {
                portal.removeStageById(999);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";

        } catch (IllegalNameException | InvalidNameException | IDNotRecognisedException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testAddCategorizedclimbToStage() {
        System.out.println("The system is testing the addCategorizedclimbToStage method...");
        try {

            // Create a race and add a stage to it
            int raceId = portal.createRace("RaceSeven", "Race number seven");
            int stageId = portal.addStageToRace(raceId, "StageFive", "Fifth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // call addCategorizedClimbToStage with valid stage ID
            int checkpointId = portal.addCategorizedClimbToStage(stageId, 5.0, CheckpointType.C1, 0.1, 1.0);

            // check that the checkpoint has been added
            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            boolean checkpointAdded = Arrays.stream(checkpointIds).anyMatch(id -> id == checkpointId);
            assert (checkpointAdded)
                    : "Expected checkpoint to be added";

            // call addCategorizedClimbToStage with invalid stage ID
            boolean exceptionThrown = false;
            try {
                portal.addCategorizedClimbToStage(999, 5.0, CheckpointType.C1, 0.1, 1.0);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            } catch (InvalidStageStateException e) {
                exceptionThrown = true;
                e.printStackTrace();
            } catch (InvalidStageTypeException e) {
                exceptionThrown = true;
                e.printStackTrace();
            } catch (InvalidLocationException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IDNotRecognisedException | InvalidStageStateException | InvalidStageTypeException
                | InvalidLocationException | InvalidNameException | IllegalNameException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testAddIntermediateSprintToStage() {
        System.out.println("The system is testing the addIntermediateSprintToStage method...");
        try {

            // create a race and add a stage to it
            int raceId = portal.createRace("RaceEight", "Race number eight");
            int stageId = portal.addStageToRace(raceId, "StageSix", "Sixth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // call addIntermediateSprintToStage with valid stage ID
            int checkpointId = portal.addIntermediateSprintToStage(stageId, 5.0);

            // check that the checkpoint has been added
            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            boolean checkpointAdded = Arrays.stream(checkpointIds).anyMatch(id -> id == checkpointId);
            assert (checkpointAdded)
                    : "Expected checkpoint to be added";

            // call addIntermediateSprintToStage with invalid stage ID
            boolean exceptionThrown = false;
            try {
                portal.addIntermediateSprintToStage(999, checkpointId);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            } catch (InvalidStageStateException e) {
                exceptionThrown = true;
                e.printStackTrace();
            } catch (InvalidStageTypeException e) {
                e.printStackTrace();
                exceptionThrown = true;
            } catch (InvalidLocationException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IDNotRecognisedException | InvalidStageStateException | InvalidStageTypeException
                | InvalidLocationException | InvalidNameException | IllegalNameException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testRemoveCheckpoint() {
        System.out.println("The system is testing the removeCheckpoint method...");
        try {
            // create a race and add a stage to it and add a checkpoint to the stage
            int raceId = portal.createRace("RaceNine", "Race number nine");
            int stageId = portal.addStageToRace(raceId, "StageSeven", "Seventh stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);
            int checkpointId = portal.addIntermediateSprintToStage(stageId, 5.0);

            // call removeCheckpoint with valid checkpoint ID
            portal.removeCheckpoint(checkpointId);

            // check that the checkpoint has been removed
            int[] checkpointIds = portal.getStageCheckpoints(stageId);
            boolean checkpointRemoved = Arrays.stream(checkpointIds).noneMatch(id -> id == checkpointId);
            assert (checkpointRemoved)
                    : "Expected checkpoint to be removed";

            // call removeCheckpoint with invalid checkpoint ID
            boolean exceptionThrown = false;
            try {
                portal.removeCheckpoint(999);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";

        } catch (IDNotRecognisedException | InvalidStageStateException | InvalidStageTypeException
                | InvalidLocationException | InvalidNameException | IllegalNameException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testConcludeStagePreparation() {
        System.out.println("The system is testing the concludeStagePreparation method...");
        try {
            // create a race and a stage to it
            int raceId = portal.createRace("RaceTen", "Race number ten");
            int stageId = portal.addStageToRace(raceId, "StageEight", "Eighth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);
            // call concludeStagePreparation with valid stage ID
            portal.concludeStagePreparation(stageId);

            // call concludeStagePereparation again with the same stage ID
            // this should throw an InvalidStageStateException

            boolean exceptionThrown = false;
            try {
                portal.concludeStagePreparation(stageId);
            } catch (InvalidStageStateException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the InvalidStageStateException was thrown
            assert (exceptionThrown)
                    : "Expected InvalidStageStateException for concluded stage";
        } catch (IDNotRecognisedException | InvalidStageStateException | InvalidNameException | IllegalNameException
                | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetStageCheckpoints() {
        System.out.println("The system is testing the getStageCheckpoints method...");
        try {
            // create a race and add a stage to it
            int raceId = portal.createRace("RaceEleven", "Race number eleven");
            int stageId = portal.addStageToRace(raceId, "StageNine", "Ninth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add a checkpoint to the stage
            int checkpointId = portal.addIntermediateSprintToStage(stageId, 5.0);

            // call getStageCheckpoints with valid stage ID
            int[] checkpointIds = portal.getStageCheckpoints(stageId);

            // check that the returned array contains the checkpoint ID
            boolean checkpointFound = Arrays.stream(checkpointIds).anyMatch(id -> id == checkpointId);
            assert (checkpointFound)
                    : "Expected checkpoint to be in the array";

            // call getStageCheckpoints with invalid stage ID
            boolean exceptionThrown = false;
            try {
                portal.getStageCheckpoints(999);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IDNotRecognisedException | InvalidStageStateException | InvalidStageTypeException
                | InvalidLocationException | InvalidNameException | IllegalNameException | InvalidLengthException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testCreateTeam() {
        System.out.println("The system is testing the createTeam method...");
        try {
            // call createTeam with valid name and description
            int teamId = portal.createTeam("TeamOne", "My first team");

            // check that the teamID is valid
            assert (teamId > 0)
                    : "Expected valid team ID";

            // call createTeam with a name that already exists
            boolean exceptionThrown = false;
            try {
                portal.createTeam("TeamOne", "Duplicate");
            } catch (InvalidNameException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the InvalidNameException was thrown
            assert (exceptionThrown)
                    : "Expected InvalidNameException for duplicate name";

            // call createTeam with an invalid name (null, empty, too long)
            exceptionThrown = false;
            try {
                portal.createTeam(null, "Invalid Team");
            } catch (IllegalNameException e) {
                e.printStackTrace();
            }

            // check that the IllegalNameException was thrown
            assert (exceptionThrown)
                    : "Expected IllegalNameException for invalid name";
        } catch (InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testRemoveTeam() {
        System.out.println("The system is testing the removeTeam method...");

        try {
            // create a team
            int teamId = portal.createTeam("TeamTwo", "My second team");

            // call remove team with valid team ID
            portal.removeTeam(teamId);

            // call remove team with the same Id again
            // this should throw an IDNotRecognisedException
            boolean exceptionThrown = false;
            try {
                portal.removeTeam(teamId);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for removed team";
        } catch (IDNotRecognisedException | InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;

        }
    }

    private static void testGetTeams() {
        System.out.println("The system is testing the getTeams method...");
        try {

            // create a few teams
            int teamId1 = portal.createTeam("TeamThree", "My third team");
            int teamId2 = portal.createTeam("TeamFour", "My fourth team");

            // call getTeams
            int[] teamIds = portal.getTeams();

            // check that the returned array contains the team IDs
            boolean team1Found = Arrays.stream(teamIds).anyMatch(id -> id == teamId1);
            boolean team2Found = Arrays.stream(teamIds).anyMatch(id -> id == teamId2);
            assert (team1Found && team2Found)
                    : "Expected team IDs to be in the array";
        } catch (InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetTeamRiders() {
        System.out.println("The system is testing the getTeamRiders method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamFive", "My fifth team");

            // add a rider to the team
            int riderId = portal.createRider(teamId, "FirstRider", 2005);

            // call getTeamRiders with valid team ID
            int[] riderIds = portal.getTeamRiders(teamId);

            // check that the returned array contains the rider ID
            boolean riderFound = Arrays.stream(riderIds).anyMatch(id -> id == riderId);
            assert (riderFound)
                    : "Expected rider to be in the array";

            // call getTeamRiders with invalid team ID
            boolean exceptionThrown = false;
            try {
                portal.getTeamRiders(999);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IDNotRecognisedException | InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testCreateRider() {
        System.out.println("The system is testing the createRider method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamSix", "My sixth team");

            // call createRider with valid name and year
            int riderId = portal.createRider(teamId, "RiderTwo", 2005);

            // check that the rider ID is valid
            assert (riderId > 0)
                    : "Expected valid rider ID";

            // call createRider with a name that is null, empty or year of birth that is
            // invalid
            boolean exceptionThrown = false;
            try {
                portal.createRider(teamId, null, 1900);
            } catch (IllegalArgumentException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IllegalNameException was thrown
            assert (exceptionThrown)
                    : "Expected IllegalNameException for invalid name";

            // call createRider with an invalid team ID
            exceptionThrown = false;
            try {
                portal.createRider(999, "RiderThree", 2000);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for invalid ID";
        } catch (IDNotRecognisedException | InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testRemoveRider() {
        System.out.println("The system is testing the removeRider method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamSeven", "My seventh team");

            // add a rider to the team
            int riderId = portal.createRider(teamId, "RiderFour", 2005);

            // call removeRider with valid rider ID
            portal.removeRider(riderId);

            // call removeRider with the same ID again
            // this should throw an IDNotRecognisedException
            boolean exceptionThrown = false;
            try {
                portal.removeRider(riderId);
            } catch (IDNotRecognisedException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the IDNotRecognisedException was thrown
            assert (exceptionThrown)
                    : "Expected IDNotRecognisedException for removed rider";
        } catch (IDNotRecognisedException | InvalidNameException | IllegalNameException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testRegisterRiderResultsInStage() {
        System.out.println("The system is testing the registerRiderResultsInStage method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamEight", "My eighth team");

            // add a rider to the team
            int riderId = portal.createRider(teamId, "RiderFive", 2005);

            // create a race
            int raceId = portal.createRace("RaceTwelve", "Race number twelve");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageTen", "Tenth stage", 10.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 5.0);
            portal.addCategorizedClimbToStage(stageId, 5.0, CheckpointType.C1, 0.1, 1.0);
            portal.addCategorizedClimbToStage(stageId, 5.0, CheckpointType.C2, 0.1, 1.0);

            // cal registerRiderResultsInStage with valid rider ID and stage ID
            portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(10, 0), LocalTime.of(11, 0),
                    LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0));
            // call registerRiderResultsInStage with the same stage ID and rider ID again
            // this should throw an DuplicatedResultException
            boolean exceptionThrown = false;
            try {
                portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(10, 0), LocalTime.of(11, 0),
                        LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0));
            } catch (DuplicatedResultException e) {
                exceptionThrown = true;
                e.printStackTrace();
            }

            // check that the DuplicatedResultException was thrown
            assert (exceptionThrown)
                    : "Expected DuplicatedResultException for duplicate result";

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;

        }

    }

    private static void testGetRiderResultsInStage() {
        System.out.println("The system is testing the getRiderResultsInStage method...");
        try {
            // Create a team
            int teamId = portal.createTeam("TeamNine", "My ninth team");

            // Add a rider to the team
            int riderId = portal.createRider(teamId, "RiderSix", 2006);

            // Create a race
            int raceId = portal.createRace("RaceThirteen", "Race number thirteen");

            // Add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageEleven", "Eleventh stage", 11.0, LocalDateTime.now(),
                    StageType.FLAT);

            // Add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 5.5);
            portal.addCategorizedClimbToStage(stageId, 5.5, CheckpointType.C1, 0.1, 1.1);
            portal.addCategorizedClimbToStage(stageId, 5.5, CheckpointType.C2, 0.1, 1.1);

            // Register rider results in stage
            portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(10, 0), LocalTime.of(11, 0),
                    LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0));

            // Call getRiderResultsInStage and check the results
            LocalTime[] expectedTimes = { LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.of(12, 0),
                    LocalTime.of(13, 0), LocalTime.of(14, 0), LocalTime.of(4, 0) };
            LocalTime[] actualTimes = portal.getRiderResultsInStage(stageId, riderId);

            // Check that the lengths of the arrays are equal
            assert expectedTimes.length == actualTimes.length : "Expected and actual times have different lengths";

            // Check that the elements of the arrays are equal
            for (int i = 0; i < expectedTimes.length; i++) {
                assert expectedTimes[i].equals(actualTimes[i]) : "Expected and actual times do not match at index " + i;
            }

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetRiderAdjustedElapsedTimeInStage() {
        System.out.println("The System is testing the getRiderAdjustedElapsedTimeInStage method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamTen", "My tenth team");

            // add two riders to the team
            int riderId1 = portal.createRider(teamId, "RiderSeven", 2007);
            int riderId2 = portal.createRider(teamId, "RiderEight", 2008);

            // create a race
            int raceId = portal.createRace("RaceFourteen", "Race number fourteen");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageTwelve", "Twelfth stage", 12.0, LocalDateTime.now(),
                    StageType.FLAT);

            // `add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 6.0);
            portal.addCategorizedClimbToStage(stageId, 6.0, CheckpointType.C1, 0.1, 1.2);
            portal.addCategorizedClimbToStage(stageId, 6.0, CheckpointType.C2, 0.1, 1.2);

            // register rider results in stage for both riders
            portal.registerRiderResultsInStage(stageId, riderId1, LocalTime.of(10, 0), LocalTime.of(10, 30),
                    LocalTime.of(11, 0), LocalTime.of(11, 30));
            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(10, 1), LocalTime.of(10, 31),
                    LocalTime.of(11, 1), LocalTime.of(11, 31));

            // Call getRiderAdjustedElapsedTimeInStage and check the results
            LocalTime expectedTime = LocalTime.of(10, 0);
            LocalTime actualTime = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId2);

            // check that the expected time is equal to the actual time
            assert expectedTime.equals(actualTime) : "Expected and actual times do not match";

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testDeleteRiderResultsInStage() {
        System.out.println("The system is testing the deleteRiderResultsInStage method...");
        try {
            // Create a team
            int teamId = portal.createTeam("TeamEleven", "My eleventh team");

            // Add a rider to the team
            int riderId = portal.createRider(teamId, "RiderNine", 2009);

            // Create a race
            int raceId = portal.createRace("RaceFifteen", "Race number fifteen");

            // Add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageThirteen", "Thirteenth stage", 13.0, LocalDateTime.now(),
                    StageType.FLAT);

            // Add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 6.5);
            portal.addCategorizedClimbToStage(stageId, 6.5, CheckpointType.C1, 0.1, 1.3);
            portal.addCategorizedClimbToStage(stageId, 6.5, CheckpointType.C2, 0.1, 1.3);

            // Register rider results in stage
            portal.registerRiderResultsInStage(stageId, riderId, LocalTime.of(10, 0), LocalTime.of(11, 0),
                    LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(14, 0));

            // Delete rider results in stage
            portal.deleteRiderResultsInStage(stageId, riderId);

            // Try to get the deleted results
            LocalTime deletedResults = portal.getRiderAdjustedElapsedTimeInStage(stageId, riderId);

            // Check that the deleted results are null
            assert deletedResults == null : "Deleted results are not null";

            // test deleteRiderResultsInStage with invalid rider ID
            try {
                // Test deleteRiderResultsInStage with invalid rider ID
                portal.deleteRiderResultsInStage(stageId, -1);
                assert false : "Expected an IDNotRecognisedException to be thrown";
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetRidersRankInStage() {
        System.out.println("The system is testing the getRidersRankInStage method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamTwelve", "My twelfth team");

            // add riders to the team
            int riderId1 = portal.createRider(teamId, "RiderTen", 2010);
            int riderId2 = portal.createRider(teamId, "RiderEleven", 2011);

            // create a race
            int raceId = portal.createRace("RaceSixteen", "Race number sixteen");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageFourteen", "Fourteenth stage", 14.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 7.0);
            portal.addCategorizedClimbToStage(stageId, 7.0, CheckpointType.C1, 0.1, 1.4);
            portal.addCategorizedClimbToStage(stageId, 7.0, CheckpointType.C2, 0.1, 1.4);

            // register rider results in stage for both riders
            portal.registerRiderResultsInStage(stageId, riderId1, LocalTime.of(10, 0), LocalTime.of(10, 30),
                    LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0));
            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(10, 1), LocalTime.of(10, 31),
                    LocalTime.of(11, 1), LocalTime.of(11, 31), LocalTime.of(12, 1));

            // get riders rank in stage
            int[] riderIds = portal.getRidersRankInStage(stageId);

            // check if the ranks are correct
            assert riderIds[0] == riderId1 : "Expected riderId1 to be first";
            assert riderIds[1] == riderId2 : "Expected riderId2 to be second";
            try {
                // Test getRidersRankInStage with invalid stage ID
                portal.getRidersRankInStage(-1);
                assert false : "Expected an IDNotRecognisedException to be thrown";
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetRankedAdjustedElapsedTimesInStage() {
        System.out.println("The system is testing the getRankedAdjustedElapsedTimesInStage method...");
        try {

            // create a team
            int teamId = portal.createTeam("TeamThirteen", "My thirteenth team");

            // create riders to the team
            int riderId1 = portal.createRider(teamId, "RiderTwelve", 2012);
            int riderId2 = portal.createRider(teamId, "RiderThirteen", 2013);

            // create a race
            int raceId = portal.createRace("RaceSeventeen", "Race number seventeen");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageFifteen", "Fifteenth stage", 15.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 7.5);
            portal.addCategorizedClimbToStage(stageId, 7.5, CheckpointType.C1, 0.1, 1.5);
            portal.addCategorizedClimbToStage(stageId, 7.5, CheckpointType.C2, 0.1, 1.5);

            // get rider results in stage for both riders
            portal.registerRiderResultsInStage(stageId, riderId1, LocalTime.of(10, 0), LocalTime.of(10, 30),
                    LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0));
            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(10, 1), LocalTime.of(10, 31),
                    LocalTime.of(11, 1), LocalTime.of(11, 31), LocalTime.of(12, 1));

            // get ranked adjusted elapsed times in stage
            LocalTime[] times = portal.getRankedAdjustedElapsedTimesInStage(stageId);

            // check if the times are correct
            assert times[0].equals(LocalTime.of(10, 0)) : "Expected riderId1 to be first";
            assert times[1].equals(LocalTime.of(10, 1)) : "Expected riderId2 to be second";

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetRidersPointsInStage() {
        System.out.println("The system is testing the getRidersPointsInStage method...");

        try {
            // create a team
            int teamId = portal.createTeam("TeamFourteen", "My fourteenth team");

            // add riders to the team
            int riderId1 = portal.createRider(teamId, "RiderFourteen", 2014);
            int riderId2 = portal.createRider(teamId, "RiderFifteen", 2015);

            // create a race
            int raceId = portal.createRace("RaceEighteen", "Race number eighteen");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageSixteen", "Sixteenth stage", 16.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add three checkpoints to the stage
            portal.addCategorizedClimbToStage(stageId, 8.0, CheckpointType.C1, 0.1, 1.6);
            portal.addCategorizedClimbToStage(stageId, 8.0, CheckpointType.C1, 0.1, 1.6);
            portal.addCategorizedClimbToStage(stageId, 8.0, CheckpointType.C2, 0.1, 1.6);

            // register rider results in stage for both riders
            portal.registerRiderResultsInStage(stageId, riderId1, LocalTime.of(10, 0), LocalTime.of(10, 30),
                    LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0));
            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(10, 1), LocalTime.of(10, 31),
                    LocalTime.of(11, 1), LocalTime.of(11, 31), LocalTime.of(12, 1));

            // get riders points in stage
            int[] points = portal.getRidersPointsInStage(stageId);

            // check if the points are correct
            assert points[0] == 50 : "Expected riderId1 to have 3 points";
            assert points[1] == 30 : "Expected riderId2 to have 2 points";

            try {
                // test getRidersPointsInStage with invalid stage ID
                portal.getRidersPointsInStage(-1);
                assert false : "Expected an IDNotRecognisedException to be thrown";
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testGetRidersMountainPointsInStage() {
        System.out.println("The system is testing the getRidersMountainPointsInStage method...");

        try {
            // create a team
            int teamId = portal.createTeam("TeamFifteen", "My fifteenth team");

            // add riders to the team
            int riderId1 = portal.createRider(teamId, "RiderSixteen", 2016);
            int riderId2 = portal.createRider(teamId, "RiderSeventeen", 2017);

            // create a race
            int raceId = portal.createRace("RaceNineteen", "Race number nineteen");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageSeventeen", "Seventeenth stage", 17.0,
                    LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);

            // add three checkpoints to the stage
            portal.addCategorizedClimbToStage(stageId, 8.5, CheckpointType.C1, 0.1, 1.7);
            portal.addCategorizedClimbToStage(stageId, 8.5, CheckpointType.C1, 0.1, 1.7);
            portal.addCategorizedClimbToStage(stageId, 8.5, CheckpointType.C2, 0.1, 1.7);

            // register rider results in stage for both riders
            portal.registerRiderResultsInStage(stageId, riderId1, LocalTime.of(10, 0), LocalTime.of(10, 30),
                    LocalTime.of(11, 0), LocalTime.of(11, 30), LocalTime.of(12, 0));
            portal.registerRiderResultsInStage(stageId, riderId2, LocalTime.of(10, 1), LocalTime.of(10, 31),
                    LocalTime.of(11, 1), LocalTime.of(11, 31), LocalTime.of(12, 1));

            // get riders mountain points in stage
            int[] points = portal.getRidersMountainPointsInStage(stageId);

            // check if the points are correct
            assert points[0] == 30 : "Expected riderId1 to have 30 points";
            assert points[1] == 25 : "Expected riderId2 to have 20 points";

            try {
                // test getRidersMountainPointsInStage with invalid stage ID
                portal.getRidersMountainPointsInStage(-1);
                assert false : "Expected an IDNotRecognisedException to be thrown";
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }
        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException
                | InvalidCheckpointTimesException | InvalidStageStateException | DuplicatedResultException
                | InvalidLengthException | InvalidLocationException | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testEraseCyclingPortal() {
        System.out.println("The system is testing the eraseCyclingPortal method...");
        try {
            // create a team
            int teamId = portal.createTeam("TeamEighteen", "My eighteenth team");

            // add riders to the team
            portal.createRider(teamId, "RiderEighteen", 2018);
            portal.createRider(teamId, "RiderNineteen", 2019);

            // create a race
            int raceId = portal.createRace("RaceTwentyTwo", "Race number twenty two");

            // add a stage to the race
            int stageId = portal.addStageToRace(raceId, "StageTwenty", "Twentieth stage", 20.0, LocalDateTime.now(),
                    StageType.FLAT);

            // add three checkpoints to the stage
            portal.addIntermediateSprintToStage(stageId, 10.0);
            portal.addCategorizedClimbToStage(stageId, 10.0, CheckpointType.C1, 0.1, 2.0);
            portal.addCategorizedClimbToStage(stageId, 10.0, CheckpointType.C2, 0.1, 2.0);

            List<Integer> raceIds = Arrays.stream(portal.getRaceIds()).boxed().collect(Collectors.toList());
            assert raceIds.isEmpty() : "Expected raceIds to be empty";

            List<Integer> stageIds = Arrays.stream(portal.getRaceStages(raceId)).boxed().collect(Collectors.toList());
            assert stageIds.isEmpty() : "Expected stageIds to be empty";

            List<Integer> checkpointIds = Arrays.stream(portal.getStageCheckpoints(stageId)).boxed()
                    .collect(Collectors.toList());
            assert checkpointIds.isEmpty() : "Expected checkpointIds to be empty";

        } catch (IDNotRecognisedException | IllegalNameException | InvalidNameException | InvalidLengthException
                | IllegalArgumentException | InvalidLocationException | InvalidStageStateException
                | InvalidStageTypeException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testSaveCyclingPortal() {
        System.out.println("The system is testing the testSaveCyclingPortal method...");
        try {
            // create a temporary file
            File tempFile = File.createTempFile("portal", ".tmp");

            // save the portal to the file
            portal.saveCyclingPortal(tempFile.getAbsolutePath());

            // check if the file exists
            assert tempFile.exists() : "Expected the file to exist";

            // delete the temporary file
            tempFile.delete();

        } catch (IOException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }

    private static void testLoadCyclingPortal() {
        System.out.println("The system is testing the loadCyclingPortal method...");

        try {
            // create a temporary file
            File tempFile = File.createTempFile("portal", ".tmp");

            // save the portal to the file
            portal.saveCyclingPortal(tempFile.getAbsolutePath());

            // load the portal from the file
            portal.loadCyclingPortal(tempFile.getAbsolutePath());

            // check if the loaded portal is not null
            assert portal != null : "Expected the loaded portal to be not null";

            // delete the temporary file
            tempFile.delete();

        } catch (IOException | ClassNotFoundException e) {
            assert (false)
                    : "Unexpected exception thrown" + e;
        }
    }
}
