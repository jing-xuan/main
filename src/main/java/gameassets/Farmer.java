package gameassets;

import farmio.exceptions.FarmioException;
import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import frontend.GameConsole;
import gameassets.places.ChickenFarm;
import gameassets.places.CowFarm;
import gameassets.places.WheatFarm;
import logic.usercode.tasks.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Farmer {

    private static final String[] LOCATIONS = {"WheatFarm", "Market"};
    private static final String JSON_KEY_GOLD = "gold";
    private static final String JSON_KEY_LEVEL = "level";
    private static final String JSON_KEY_DAY = "day";
    private static final String JSON_KEY_LOCATION = "location";
    private static final String JSON_KEY_FARM_WHEAT = "farm_wheat";
    private static final String JSON_KEY_FARM_CHICKEN = "farm_chicken";
    private static final String JSON_KEY_FARM_COW = "farm_cow";
    private static final String JSON_KEY_TASK_LIST = "task_list";
    private static final String JSON_KEY_TASK_CURRENT = "task_current";
    private static final String JSON_KEY_TASK_STATUS_FAIL = "task_status_fail";
    private static final String JSON_KEY_NAME = "name";

    protected Log logTaskList;
    private int gold;
    private double level;
    private int day;
    private String name;
    private String location;
    protected WheatFarm wheatFarm;
    protected ChickenFarm chickenFarm;
    protected CowFarm cowFarm;
    protected TaskList tasks;
    private int currentTask;
    private boolean hasfailedCurrentTask;
    private ArrayList<Double> levelList = new ArrayList<Double>(Arrays.asList(1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 2.1));


    /**
     * Constructor for Farmer to intialize farmer object.
     */
    public Farmer() {
        this.gold = 10;
        this.level = 1.1;
        this.day = 1;
        this.location = "WheatFarm";
        this.wheatFarm = new WheatFarm();
        this.chickenFarm = new ChickenFarm();
        this.cowFarm = new CowFarm();
        this.tasks = new TaskList();
        this.currentTask = -1;
        this.hasfailedCurrentTask = false;
        this.name = "name";
        this.logTaskList = new Log();
    }

    /**
     * Checks whether the name that was loaded from the save file is a valid name.
     *
     * @param loadName as the name that is loaded from the save file.
     * @throws FarmioException if loadName does not meet the conditions of the name.
     */
    private void isValidName(String loadName) throws FarmioException {
        boolean hasError = false;
        if (loadName.equals("MENU") || !(loadName.length() <= 15 && loadName.length() > 0
                && (loadName.matches("[a-zA-Z0-9_]+")))) {
            hasError = true;
        }

        if (hasError) {
            throw new FarmioException("Invalid Name!");
        }
    }

    private void isValidTaskList(TaskList tasks) throws FarmioException {
        if (tasks.size() > GameConsole.FRAME_SECTION_HEIGHT) {
            throw new FarmioException("Too many tasks!");
        }
    }

    /**
     * Adds the user's name.
     *
     * @param username as the name the user inputs.
     */
    public void inputName(String username) {
        name = username;
    }

    /**
     * Gets user's name.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the amount of gold the farmer has.
     *
     * @return the amount of gold.
     */
    public int getGold() {
        return gold;
    }

    public boolean hasGold() {
        return gold > 0;
    }

    /**
     * Gets user level.
     *
     * @return the user level.
     */
    public double getLevel() {
        return level;
    }

    /**
     * Gets the day the farmer is at.
     *
     * @return the day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets user location.
     *
     * @return the user location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets user input.
     *
     * @param newLocation as the new location of the user.
     */
    public void changeLocation(String newLocation) {
        location = newLocation;
    }

    /**
     * Gets user assets based on level.
     */
    public Map<String, Integer> getAssets() {
        Map<String, Integer> assets = new HashMap<>();

        if (level >= 1.4) {
            assets.put("Wheat", wheatFarm.getWheat());
            assets.put("Grain", wheatFarm.getGrain());
        }
        if (level >= 1.3) {
            assets.put("Seedlings", wheatFarm.getSeedlings());
        }
        if (level >= 1.2) {
            assets.put("Seeds", wheatFarm.getSeeds());
        }
        if (level >= 1.1) {
            assets.put("Gold", gold);
        }
        return assets;
    }

    /**
     * Gets user wheatfarm.
     *
     * @return the user wheatfarm.
     */
    public WheatFarm getWheatFarm() {
        return wheatFarm;
    }

    /**
     * Gets user chickenfarm.
     *
     * @return the user chickenfarm.
     */
    public ChickenFarm getChickenFarm() {
        return chickenFarm;
    }

    /**
     * Gets user cowfarm.
     *
     * @return the user cowfarm.
     */
    public CowFarm getCowFarm() {
        return cowFarm;
    }

    /**
     * Gets user tasks to be executed.
     *
     * @return the user tasks.
     */
    public TaskList getTasks() {
        return tasks;
    }

    /**
     * Gets the log of tasks.
     *
     * @return log containing a list of tasks.
     */
    public Log getLogTaskList() {
        return logTaskList;
    }

    public void addTask(Task task) throws FarmioException {
        tasks.addTask(task);
    }

    public void insertTask(int taskID, Task task) throws FarmioException {
        tasks.insertTask(taskID, task);
    }

    public void editTask(int taskID, Task task) throws FarmioException {
        tasks.editTask(taskID, task);
    }

    public ArrayList<String> toStringArray() {
        return tasks.toStringArray();
    }

    public String deleteTask(int taskID) throws FarmioException {
        return tasks.deleteTask(taskID);
    }

    public void deleteAll() {
        tasks.deleteAll();
    }


    public void taskClear() {
        tasks.clear();
    }

    public int taskSize() {
        return tasks.size();
    }

    /**
     * Checks if curent task has failed and resets current task.
     *
     * @return true if current task has failed and false otherwise.
     */
    public boolean isHasfailedCurrentTask() {
        return hasfailedCurrentTask;
    }

    /**
     * Reverts task list execution failure.
     */
    public void resetTaskFailed() {
        hasfailedCurrentTask = false;
        currentTask = -1;
    }

    /**
     * Sets task list execution as failed.
     */
    public void setTaskFailed() {
        hasfailedCurrentTask = true;
    }

    /**
     * Decrements gold after buying an item.
     *
     * @param cost as the buying price of the item.
     */
    public void spendGold(int cost) {
        gold -= cost;
    }

    /**
     * Increments gold after selling an item.
     *
     * @param profit as the selling price of the item.
     */
    public void earnGold(int profit) {
        gold += profit;
    }

    /**
     * Gets the index of the current task in execution.
     *
     * @return the index of the current task.
     */
    public int getCurrentTask() {
        return this.currentTask;
    }

    /**
     * Increases the level.
     *
     * @return the next level number. 0 if current level is not registered or game has ended
     */
    public double nextLevel() {
        if (level < levelList.get(levelList.size() - 1)) {
            level = levelList.get(levelList.indexOf(level) + 1);
            return level;
        }
        return 0;
    }

    /**
     * Takes care of TaskList execution and handles task failure.
     *
     * @param farmio The game where the day should be started.
     * @throws FarmioFatalException if file from action's simulation cannot be found.
     */
    public void startDay(Farmio farmio) throws FarmioFatalException {
        try {
            for (int i = 0; i < tasks.size(); i++) {
                this.currentTask = i;

                logTaskList.add(tasks.get(i));
                tasks.get(i).execute(farmio);
            }
        } catch (FarmioException e) {
            farmio.setStage(Farmio.Stage.LEVEL_FAILED);
        } finally {
            this.currentTask = -1;
        }
    }

    /**
     * Proceeds to the next day.
     * Allows seeds to grow into wheat and increment day number.
     * Allows chickens to grow into fullchicken and increment day number.
     * Allows cow to grow into fullcow and increment day number.
     */
    public void nextDay() {
        if (level > 1) {
            wheatFarm.growSeedlings();
            day += 1;
        }
        if (level > 2) {
            chickenFarm.layEggs();
            day += 1;
        }
        if (level > 3) {
            cowFarm.growCow();
            day += 1;
        }
    }

    /**
     * Checks whether the wheatfarm currently has seeds.
     *
     * @return true if wheatfarm has seeds, false if wheatfarm has no seeds.
     */
    public boolean hasSeeds() {
        return wheatFarm.hasSeeds();
    }

    /**
     * Checks whether the wheatfarm currently has wheat.
     *
     * @return true if wheatfarm has wheat, false if wheatfarm has no wheat.
     */
    public boolean hasWheat() {
        return wheatFarm.hasWheat();
    }

    /**
     * Checks whether the wheatfarm currently has grain.
     *
     * @return true if wheatfarm has grain, false if wheatfarm has no grain.
     */
    public boolean hasGrain() {
        return wheatFarm.hasGrain();
    }

    /**
     * Gets number of seeds.
     *
     * @return seeds as the amount of seeds in the wheatfarm.
     */
    public int getSeeds() {
        return wheatFarm.getSeeds();
    }

    /**
     * Gets number of seedlings.
     *
     * @return seedlings as the amount of seedlings in the wheatfarm.
     */
    public int getSeedlings() {
        return wheatFarm.getSeedlings();
    }

    /**
     * Gets number of wheat.
     *
     * @return wheat as the amount of wheat in the wheatfarm.
     */
    public int getWheat() {
        return wheatFarm.getWheat();
    }

    /**
     * Gets number of grains.
     *
     * @return seeds as the amount of grain in the wheatfarm.
     */
    public int getGrain() {
        return wheatFarm.getGrain();
    }

    /**
     * Increases number of seeds.
     */
    public void buySeeds() {
        wheatFarm.buySeeds();
    }

    /**
     * Changes all wheat to grains.
     * Resets wheat to 0.
     */
    public void harvestWheat() {
        wheatFarm.harvestWheat();
    }

    /**
     * Changes all seeds to seedlings.
     * Resets seeds to 0.
     */
    public void plantSeeds() {
        wheatFarm.plantSeeds();
    }

    /**
     * Increases the amount of money user has.
     *
     * @return the amount of money earned.
     */
    public int sellGrain() {
        return wheatFarm.sell();
    }

    /**
     * Checks whether the chickenfarm currently has eggs.
     *
     * @return true if chickenfarm has eggs, false if chickenfarm has no eggs.
     */
    public boolean hasEgg() {
        return chickenFarm.hasEgg();
    }

    /**
     * Checks whether the chickenfarm currently has chickens.
     *
     * @return true if chickenfarm has chicken, false if chickenfarm has no chicken.
     */
    public boolean hasChicken() {
        return chickenFarm.hasChicken();
    }

    /**
     * Checks whether the chickenfarm currently has fullChicken.
     *
     * @return true if chickenfarm has fullChicken, false if chickenfarm has no fullChicken.
     */
    public boolean hasFullChicken() {
        return chickenFarm.hasFullChicken();
    }

    /**
     * Gets number of chicken.
     *
     * @return chicken as the amount of chickens in the chickenfarm.
     */
    public int getChicken() {
        return chickenFarm.getChicken();
    }

    /**
     * Gets number of eggs.
     *
     * @return eggs as the amount of eggs in the chickenfarm.
     */
    public int getEgg() {
        return chickenFarm.getEgg();
    }

    /**
     * Gets number of fullChicken.
     *
     * @return fullChicken as the amount of fullChicken in the chickenfarm.
     */
    public int getFullChicken() {
        return chickenFarm.getFullChicken();
    }

    /**
     * Increases number of chickens.
     */
    public void buyChicken() {
        chickenFarm.buyChicken();
    }

    /**
     * Changes all fullchicken to egg.
     * Resets wheat to 0.
     */
    public void collectEgg() {
        chickenFarm.collectEgg();
    }

    /**
     * Increases the amount of money user has.
     *
     * @return the amount of money earned.
     */
    public int sellEgg() {
        return chickenFarm.sell();
    }

    /**
     * Checks whether the cowfarm currently has Milk.
     *
     * @return true if milkfarm has milk, false if milkfarm has no milk.
     */
    public boolean hasMilk() {
        return cowFarm.hasMilk();
    }

    /**
     * Checks whether the cowfarm currently has cow.
     *
     * @return true if cowfarm has cow, false if cowfarm has no cow.
     */
    public boolean hasCow() {
        return cowFarm.hasCow();
    }

    /**
     * Checks whether the cowfarm currently has fullCow.
     *
     * @return true if cowfarm has fullCow, false if cowfarm has no fullCow.
     */
    public boolean hasFullCow() {
        return cowFarm.hasFullCow();
    }

    /**
     * Gets number of cows.
     *
     * @return cow as the amount of cows in the cowfarm.
     */
    public int getCow() {
        return cowFarm.getCow();
    }

    /**
     * Gets number units of milk.
     *
     * @return milk as the amount of milk in the cowfarm.
     */
    public int getMilk() {
        return cowFarm.getMilk();
    }

    /**
     * Gets number of fullCow.
     *
     * @return fullCow as the amount of fullCow in the cowfarm.
     */
    public int getFullCow() {
        return cowFarm.getFullCow();
    }

    /**
     * Increases number of cows.
     */
    public void buyCow() {
        cowFarm.buyCow();
    }

    public void milkCow() {
        cowFarm.milkCow();
    }

    /**
     * Increase amount of money user has and reset cow to 0.
     *
     * @return the amount of money earned.
     */
    public int sellCow() {
        return cowFarm.sellCow();
    }

    /**
     * Increases the amount of money user has and reset milk to 0.
     *
     * @return the amount of money earned.
     */
    public int sellMilk() {
        return cowFarm.sell();
    }

    /**
     * Change farmer variables with the data in argument JSONObject.
     *
     * @param jsonObject data representation of farmer.
     * @return this instance of Farmer.
     * @throws FarmioException error parsing json to farmer object.
     */
    public Farmer setJson(JSONObject jsonObject) throws FarmioException {
        try {
            this.level = (Double) jsonObject.get(JSON_KEY_LEVEL);
            this.gold = validateInt((int) (long) jsonObject.get(JSON_KEY_GOLD));
            this.day = validateInt((int) (long) jsonObject.get(JSON_KEY_DAY));
            this.location = validateLoaction((String) jsonObject.get(JSON_KEY_LOCATION));
            this.wheatFarm = new WheatFarm((JSONObject) jsonObject.get(JSON_KEY_FARM_WHEAT));
            this.chickenFarm = new ChickenFarm((JSONObject) jsonObject.get(JSON_KEY_FARM_CHICKEN));
            this.cowFarm = new CowFarm((JSONObject) jsonObject.get(JSON_KEY_FARM_COW));
            this.tasks = new TaskList((JSONArray) jsonObject.get(JSON_KEY_TASK_LIST));
            this.currentTask = -1;
            this.hasfailedCurrentTask = false;
            String savedName = (String) jsonObject.get(JSON_KEY_NAME);
            String loadName = savedName.toUpperCase();
            isValidName(loadName);
            isValidTaskList(this.tasks);
            this.name = loadName;
        } catch (Exception e) {
            throw new FarmioException("Game save corrupted!");
        }
        return this;
    }

    /**
     * Checks if the number of assets are valid.
     *
     * @param farmer the farmer to be validated
     * @throws FarmioException if assets are not valid
     */
    private void isValidAssets(Farmer farmer) throws FarmioException {
        if (farmer.getGold() < 0 || farmer.getSeeds() < 0 || farmer.getWheat() < 0 || farmer.getSeedlings() < 0
                || farmer.getGrain() < 0) {
            throw new FarmioException("Negative assets");
        }
    }

    /**
     * Generates a json representation of farmer to be stored into a file.
     *
     * @return JSONOBject with farmer data.
     */
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put(JSON_KEY_LEVEL, level);
        obj.put(JSON_KEY_GOLD, gold);
        obj.put(JSON_KEY_DAY, day);
        obj.put(JSON_KEY_LOCATION, location);
        obj.put(JSON_KEY_FARM_WHEAT, wheatFarm.toJson());
        obj.put(JSON_KEY_FARM_CHICKEN, chickenFarm.toJson());
        obj.put(JSON_KEY_FARM_COW, cowFarm.toJson());
        obj.put(JSON_KEY_TASK_LIST, tasks.toJson());
        obj.put(JSON_KEY_NAME, name);
        return obj;
    }

    /**
     * Updates TaskList in argument json data to match TaskList in farmer.
     *
     * @param object Json data to be updated.
     * @return Updated json data.
     */
    public JSONObject updateJson(JSONObject object) {
        object.replace(JSON_KEY_TASK_LIST, tasks.toJson());
        return object;
    }

    /**
     * Validates and format input location.
     *
     * @param jsonLocation string to be validated and formatted.
     * @return formatted string location.
     * @throws FarmioException invalid location.
     */
    private String validateLoaction(String jsonLocation) throws FarmioException {
        for (String location : LOCATIONS) {
            if (jsonLocation.equalsIgnoreCase(location)) {
                return location;
            }
        }
        throw new FarmioException("Game save is corrupted.");
    }

    /**
     * Validates asset integer value within limits.
     * @param number Value to be validated.
     * @return Valid value for game assets.
     * @throws FarmioException Invalid value for game assets.
     */
    public static int validateInt(int number) throws FarmioException {
        int limit = 3000000 + new Random().nextInt(500000);
        if (number < 0 || number > limit) {
            throw new FarmioException("Game is corrupted");
        }
        return number;
    }
}