import farmio.Farmio;
import farmio.exceptions.FarmioException;
import gameassets.Farmer;
import gameassets.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelTest {

    private static final String FARMER_JSON = "{\"gold\":826,\"farm_chicken\":{\"chicken\":916,"
            + "\"egg\":4296,\"fullChicken\":21},\"level\":1.1,\"farm_wheat\":{\"seed\""
            + ":991,\"wheat\":18,\"grain\":3,\"seedling\":1},\"name\":\"FAME\",\"l"
            + "ocation\":\"WheatFarm\",\"farm_cow\":{\"milk\":888,\"cow\":437,\"fullcow\":381},\"task_list\":"
            + "[{\"condition\":{\"condition_type\":\"BOOLEAN\",\"condition_boolean_type\":\"TRUE\"},\"action\""
            + ":\"gotoMarket\",\"type\":\"DO\"}],\"day\":903}";

    private static final String LEVEL_JSON = "{\"narratives\": [\"Hi there Farmer +, you only have enough gold to"
            + " start a small farm. Why not we head over to the market to get started.\",\"Take note that you cannot"
            + " enter any game commands during the narratives. If you wish to enter any commands, press [skip].\",\""
            + "On your top right hand corner, you can see the location you are at. Currently you are at your Wheatfar"
            + "m.\",\"On the bottom left hand corner of the screen, you can see the assets you hold. Currently you ha"
            + "ve 10 gold to start your farm.\",\"On the top left corner you can see your current level which is 1.1"
            + ". This tutorial ends at level 1.4\",\"Next to that, there is a summary of the objective of the current"
            + " level. For example, for this level, the objective is to travel to the Market\",\"On the left, the sect"
            + "ion on GOALS breaks down the objective into individual goals to be accomplished. For example, for this"
            + " level, the goal is to make your locaton the market\",\"A summary list of all the commands can be acce"
            + "ssed the in-game Menu by entering [menu].\",\"If you wish to quit the game, you can either type [quit "
            + "game] or [exit game]\",\"Let's get started!\",\"The goal for this level is to change your location to t"
            + "he Market.\",\"The first command you will be using is a 'Do' command.\\nThe 'Do' command instructs farm"
            + "er + to carry out an action.\",\"The type of tasks available and the action to be executed can be seen "
            + "in the middle of the screen.\",\"From the example above, 'Do' is the type of task given and 'goToMark"
            + "et' is the action to be carried out by the farmer.\",\"Enter [do gotoMarket] to create a task that te"
            + "lls Farmer + to drive to the market.\"],\"feedback\": [\"You have succesfully travelled to the market"
            + " by using the [do] command.\",\"Note that your goal changed from white to green when it is completed "
            + "(ubuntu only)\"],\"gold\": 0,\"seeds\": 0,\"seedlings\": 0,\"wheat\": 0,\"grain\": 0,\"file_path\": \""
            + "Level1.1\",\"deadline\" : 1,\"objective\": \"Travel to the Market\",\"hint\": \"Enter [do gotoMarket] "
            + "to create a task that tells Farmer + to drive to the market.\",\"modelAnswer\": \"|do gotoMarket|\"}";

    JSONParser parser = new JSONParser();
    JSONObject jsonFarmer = (JSONObject) parser.parse(FARMER_JSON);
    JSONObject jsonLevel = (JSONObject) parser.parse(LEVEL_JSON);
    Farmio farmio = new Farmio(false);

    private ArrayList<String> narratives;
    private String filePath;
    private String objective;
    private String hint;
    private int endSeeds;
    private int endSeedlings;
    private int endWheat;
    private int endGrain;
    private int endGold;
    private int deadline;
    public String modelAnswer;
    public List<String> successfulFeedback;

    private boolean detailedFeedbackProvided = true;
    private boolean incompleteObjectives = false;

    public Level level;

    /**
     * Intialises Level Test.
     * @throws ParseException thrown when given wrong value
     * @throws FarmioException if file cant be found
     */
    public LevelTest() throws ParseException, FarmioException {

        Farmer farmer = new Farmer().setJson(jsonFarmer);
        Farmio farmio = new Farmio(false);

        level = new Level(jsonLevel, farmer.getName());

        narratives = level.getNarratives();
        filePath = level.getPath();
        objective = level.getObjective();
        hint = level.getHint();
        endSeeds = level.getEndSeeds();
        endSeedlings = level.getEndSeedlings();
        endWheat = level.getEndWheat();
        endGrain = level.getEndGrain();
        endGold = level.getEndGold();
        deadline = level.getDeadline();
        modelAnswer = level.getModelAnswer();
        successfulFeedback = level.getSuccessfulFeedback();

    }

    @Test
    public void checkDeadlineExceededTest() {

        try {
            int currentDayExceeded = 5;
            boolean deadlineExceeded = level.checkDeadlineExceeded(currentDayExceeded);
            assertEquals(true, deadlineExceeded);
        } catch (Exception e) {
            assert false;
        }
        try {
            int currentDayNotExceeded = 1;
            boolean deadlineNotExceeded = level.checkDeadlineExceeded(currentDayNotExceeded);
            assertEquals(false, deadlineNotExceeded);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void getSuccessfulFeedbackTest() {
        List<String> succesfulFeedback = level.getSuccessfulFeedback();
        List<String> testFeedback = new ArrayList<>();
        testFeedback.add("You have succesfully travelled to the market by using the [do] command.");
        testFeedback.add("Note that your goal changed from white to green when it is completed (ubuntu only)");

        for (int i = 0; i < testFeedback.size(); i++) {
            assertEquals(succesfulFeedback.get(i), testFeedback.get(i));
        }
    }

    @Test
    public void convertStringtoListTest() {
        try {
            String modelAns = modelAnswer;
            List<String> expected = Arrays.asList("do gotoMarket");
            List<String> testList = level.convertStringToList(modelAns);
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), testList.get(i));
            }
        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    public void convertTaskListFormatTest() {
        try {
            List<String> taskList = new ArrayList<>();
            taskList.add("1.do buySeeds");
            taskList.add("2.do gotoMarket");

            List<String> testTaskList = level.convertTaskListFormat(taskList);

            List<String> compareList = new ArrayList<>();
            compareList.add("do buySeeds");
            compareList.add("do gotoMarket");

            for (int i = 0; i < compareList.size(); i++) {
                assertEquals(testTaskList.get(i), compareList.get(i));
            }
        } catch (Exception e) {
            assert false;
        }
        try {
            List<String> taskList = new ArrayList<>();
            List<String> testTaskList = level.convertTaskListFormat(taskList);
            List<String> compareList = new ArrayList<>();

            for (int i = 0; i < compareList.size(); i++) {
                assertEquals(testTaskList.get(i), compareList.get(i));
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    public void checkCorrectTaskTest() {
        try {
            List<String> emptyTaskList1 = new ArrayList<>();
            List<String> emptyTaskList2 = new ArrayList<>();

            boolean checkEmptyCondition = level.checkCorrectTasks(emptyTaskList1, emptyTaskList2);

            List<String> incorrectTaskList1 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            List<String> incorrectTaskList2 = new ArrayList<String>(Arrays.asList("do gotoMarket"));
            boolean checkIncorrect = level.checkCorrectTasks(incorrectTaskList1, incorrectTaskList2);

            List<String> correctTaskList1 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            List<String> correctTaskList2 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            boolean checkCorrect = level.checkCorrectTasks(correctTaskList1, correctTaskList2);

            assertEquals(true, checkEmptyCondition);
            assertEquals(false, checkIncorrect);
            assertEquals(true, checkCorrect);
        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    public void setCheckPercentageCorrectTasksTest() {

        try {
            List<String> taskList1 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            List<String> taskList2 = new ArrayList<String>(Arrays.asList("do gotoMarket"));
            int checkFifty = level.checkPercentageCorrectTasks(taskList1,taskList2);
            assertEquals(checkFifty, 50);

            List<String> taskList3 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            List<String> taskList4 = new ArrayList<String>(Arrays.asList("do gotoMarket", "do buySeeds"));
            int checkHundred = level.checkPercentageCorrectTasks(taskList3, taskList4);
            assertEquals(checkHundred, 100);

            List<String> taskList5 = new ArrayList<>();
            List<String> taskList6 = new ArrayList<>();
            int checkEmptyList = level.checkPercentageCorrectTasks(taskList5, taskList6);
            assertEquals(checkEmptyList, 0);

            List<String> taskList7 = new ArrayList<>();
            List<String> taskList8 = new ArrayList<>();
            taskList7.add("do gotoMarket");
            taskList8.add("do buySeeds");

            int checkEmpty = level.checkPercentageCorrectTasks(taskList7, taskList8);
            assertEquals(checkEmpty, 100);

        } catch (Exception e) {
            assert true;
        }
    }
}
