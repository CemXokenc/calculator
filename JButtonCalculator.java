import javax.swing.*;
import java.awt.*;

/**
 * My version JButton for Calculator
 */
public class JButtonCalculator extends JButton {

    /**
     * Construct
     *
     * @param bName   button name
     * @param x       x-coordinate
     * @param y       y-coordinate
     * @param bWidth  button width
     * @param bHeight button height
     */
    JButtonCalculator(String bName, int x, int y, int bWidth, int bHeight) {

        super(bName);

        setContentAreaFilled(Configuration.BUTTON_CONTENT_AREA_IS_FILLED);
        setBorderPainted(Configuration.BUTTON_BORDER_IS_PAINTED);
        setFocusPainted(Configuration.BUTTON_FOCUS_IS_PAINTED);
        setBounds(x, y, bWidth, bHeight);
        setFont(new Font(Configuration.BUTTON_FONT_STYLE, Configuration.BUTTON_FONT, Configuration.BUTTON_FONT_SIZE));
        setForeground(Configuration.BUTTON_FOREGROUND);

    }

}
