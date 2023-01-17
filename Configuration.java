import java.awt.*;
import java.util.HashMap;

/**
 * Configuration
 */
public class Configuration {

    //window size
    protected static final int WINDOW_WIDTH = 564;
    protected static final int WINDOW_HEIGHT = 359;
    protected static final boolean WINDOW_IS_RESIZABLE = false;
    protected static final boolean WINDOW_IS_VISIBLE = true;
    protected static final Component WINDOW_LOCATION_RELATIVE_TO = null;
    protected static final LayoutManager WINDOW_LOCATION_LAYOUT = null;
    protected static final String WINDOW_TITLE = "Calculator for Kristi";

    //images
    protected static final String IMAGE_PATH = "/img/";
    protected static final String IMAGE_BACKGROUND = "background.jpg";
    protected static final String IMAGE_ICON = "icon.png";

    //text field sizes
    protected static final String START_VALUE = "0";
    protected static final int HORIZONTAL_INDENTATION = 5;
    protected static final int VERTICAL_INDENTATION = 5;
    protected static final int TEXT_FIELD_WIDTH = 540;
    protected static final int TEXT_FIELD_HEIGHT = 35;
    //text field
    protected static final int TEXT_FIELD_HORIZONTAL_ALIGNMENT = 4;
    protected static final String TEXT_FIELD_FONT_STYLE = "SansSerif";
    protected static final int TEXT_FIELD_FONT_SIZE = 32;
    protected static final int TEXT_FIELD_FONT = Font.BOLD;
    protected static final int TEXT_FIELD_SELECTION_START = 1;
    protected static final boolean TEXT_FIELD_IS_EDITABLE = false;

    //buttons
    protected static final String[][] BUTTON_NAME = new String[][]{
            {"Rad", "x!", "(", ")", "%", "AC", "<--"},
            {"Inv", "sin", "ln", "7", "8", "9", "/"},
            {"pi", "cos", "log", "4", "5", "6", "*"},
            {"e", "tan", "sqrt", "1", "2", "3", "-"},
            {"Ans", "EXP", "sqr", "0", ".", "=", "+"},
    };
    protected static final String[][] BUTTON_NAME_INVERSE = new String[][]{
            {"", "", "", "", "", "", ""},
            {"", "asin", "", "", "", "", ""},
            {"", "acos", "", "", "", "", ""},
            {"", "atan", "", "", "", "", ""},
            {"", "", "", "", "", "", ""},
    };
    protected static final HashMap<String, String> BUTTON_CONSTANTS = new HashMap<>() {{
        put("pi", "3.14");
        put("e", "2.71");
        put("E", "");
        put("Rad", "");
        put("Ans", "");
        put("Rnd", "");
    }};
    protected static final int BUTTON_WIDTH = 85;
    protected static final int BUTTON_HEIGHT = 35;
    protected static final int BUTTON_HORIZONTAL_INDENTATION = 5;
    protected static final int BUTTON_VERTICAL_INDENTATION = 55;
    protected static final int BUTTON_HORIZONTAL_DIFFERENT = 77;
    protected static final int BUTTON_VERTICAL_DIFFERENT = 55;
    protected static final String BUTTON_FONT_STYLE = "SansSerif";
    protected static final int BUTTON_FONT_SIZE = 20;
    protected static final int BUTTON_FONT = Font.BOLD;
    protected static final Color BUTTON_FOREGROUND = new Color(255, 255, 255);
    protected static final boolean BUTTON_CONTENT_AREA_IS_FILLED = false;
    protected static final boolean BUTTON_BORDER_IS_PAINTED = false;
    protected static final boolean BUTTON_FOCUS_IS_PAINTED = false;

    //key codes
    protected static final int KEY_CODE_MIN = 45;
    protected static final int KEY_CODE_MAX = 61;
    protected static final int KEY_CODE_BACKSPACE = 8;
    protected static final char KEY_CODE_IS_EQUAL_TO = '=';

}
