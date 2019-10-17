package FrontEnd;

import Farmio.Farmio;
import javafx.util.Pair;

import java.util.ArrayList;

/*
 load new frame and new Farmio with delay
 */
public class GameConsole {
    private static final String TOP_BORDER = "._______________________________________________________________________________________________________.\n";
    private static final String BOTTOM_FULL_BORDER = "|_______________________________________________________________________________________________________|\n";
    private static final String MENU_PROMPT_AND_CODE_TITLE = "|-----------------"+ AsciiColours.CYAN + "<MENU>" + AsciiColours.SANE + " for instruction list or settings---------------|---------"+ AsciiColours.CYAN + "<USER CODE>" + AsciiColours.SANE + "-----------|\n";
    private static final String BOX_BOTTOM_BORDER = "|_______________________________________________________________________|_______________________________|\n";
    private static final String BOX_TOP_BORDER = "|_______________________________________________________________________|_______________________________|\n"; //"|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|\n";
    private static final String CODE_TITLE_FILLER = "|                               |\n";
    private static final String EMPTY_STAGE_LINE = "                                                       ";
    private static final String LEFT_PANEL_BOTTOM_BORDER = "|_______________";
    private static final String LEFT_PANEL_TOP_BORDER = "|_______________";//"|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾";
    private static final String ASSETS_TITLE = "|---" + AsciiColours.CYAN + "<ASSETS>" + AsciiColours.SANE + "----";
    private static final String ASSETS_BODY_FILLER = "|               ";
    private static final String BOTTOM_BORDER = "|_______________|_______________________________________________________|_______________________________|\n";
    private static String horizontalPanel(String title, String content, int totalSpace) {
        String blankspace = "";
        for (int i = 0; i < totalSpace - title.length() - content.length(); i ++) {
            blankspace += " ";
        }
        return title + content + blankspace;
    }

    private static ArrayList<String> formatAndHighlightCode(ArrayList<String> userCode, int currentTask) {
        ArrayList<String> userCodeOutput = new ArrayList<>();
        while (userCode.size() < 18){
            userCode.add("");
        }
        int i = 0;
        for (String s: userCode) {
            if (i == currentTask) {
                userCodeOutput.add(AsciiColours.RED + AsciiColours.HIGH_INTENSITY + horizontalPanel("", s, 31) + AsciiColours.SANE + "|");
            } else {
                userCodeOutput.add(horizontalPanel("", s, 31) + "|");
            }
            i ++;
        }
        return userCodeOutput;
    }
    static String content(ArrayList<String> stage, Farmio farmio) { //does not include story
        StringBuilder output = new StringBuilder();
        String objective = "";// farmio.getLevel().getNarratives().get(0);
        String location = farmio.getFarmer().getLocation();
        int level = farmio.getFarmer().getLevel();
        int day = farmio.getFarmer().getDay();
        int gold = farmio.getFarmer().getMoney();
        ArrayList<String> userCode = farmio.getFarmer().getTasks().toStringArray();
        ArrayList<Pair<String, Integer>> assets = farmio.getFarmer().getAssets();
        userCode = formatAndHighlightCode(userCode, farmio.getFarmer().getCurrentTask());
        output.append(AsciiColours.SANE + TOP_BORDER);
        output.append("|" + AsciiColours.RED + horizontalPanel("OBJECTIVE:", objective, 71) + AsciiColours.SANE).append(CODE_TITLE_FILLER);
        output.append(BOX_BOTTOM_BORDER);
        output.append(MENU_PROMPT_AND_CODE_TITLE);
        output.append(BOX_TOP_BORDER);
        output.append("|" + AsciiColours.BLUE + horizontalPanel("Level: ", Integer.toString(level), 15) + AsciiColours.SANE).append(stage.get(0)).append(userCode.get(0)).append("\n");
        output.append("|" + AsciiColours.MAGENTA + horizontalPanel("Day:   ", Integer.toString(day), 15) + AsciiColours.SANE).append(stage.get(1)).append(userCode.get(1)).append("\n");
        output.append("|" + AsciiColours.YELLOW + horizontalPanel("Gold:", Integer.toString(gold), 15) + AsciiColours.SANE).append(stage.get(2)).append(userCode.get(2)).append("\n");
        output.append(LEFT_PANEL_TOP_BORDER).append(stage.get(3)).append(userCode.get(3)).append("\n");
        output.append(AsciiColours.GREEN + horizontalPanel("|@", location, 16)+ AsciiColours.SANE).append(stage.get(4)).append(userCode.get(4)).append("\n");
        output.append(LEFT_PANEL_BOTTOM_BORDER).append(stage.get(5)).append(userCode.get(5)).append("\n");
        output.append(ASSETS_TITLE).append(stage.get(6)).append(userCode.get(6)).append("\n");
        for (int i = 7; i < 18; i ++) {
            if (assets.size() > i - 7) {
                output.append(horizontalPanel("|" + assets.get(i - 7).getKey() + ": ", assets.get(i - 7).getValue().toString(), 16)).append(stage.get(i)).append(userCode.get(i)).append("\n");
            } else {
                output.append(ASSETS_BODY_FILLER).append(stage.get(i)).append(userCode.get(i)).append("\n");
            }
        }
        output.append(BOTTOM_BORDER);
        StringBuilder output2 = new StringBuilder();
        for (int i = 0; i < output.length(); i ++) {
            if (output.charAt(i) == '\n') {
                output2.append(AsciiColours.WHITE + AsciiColours.BACKGROUND_BLACK + "\n" + AsciiColours.SANE);
            } else {
                output2.append(output.charAt(i));
            }
        }
        return output2.toString() + AsciiColours.WHITE + AsciiColours.BACKGROUND_BLACK;
    }
    static String blankConsole(ArrayList<String> stage) {
        StringBuilder output = new StringBuilder();
        output.append(AsciiColours.SANE + TOP_BORDER);
        for (int i = 0; i < 22; i ++) {
            output.append(horizontalPanel("", stage.get(i), 103)).append("\n");
        }
        output.append(BOTTOM_FULL_BORDER);
        StringBuilder output2 = new StringBuilder();
        for (int i = 0; i < output.length(); i ++) {
            if (output.charAt(i) == '\n') {
                output2.append(AsciiColours.WHITE + AsciiColours.BACKGROUND_BLACK + "\n" + AsciiColours.SANE);
            } else {
                output2.append(output.charAt(i));
            }
        }
        return output2.toString() + AsciiColours.WHITE + AsciiColours.BACKGROUND_BLACK;
    }
}