import javax.swing.*;
import java.awt.*;

/**
 * My version JTextField for Calculator
 */
public class JTextFieldCalculator extends JTextField {

    /**
     * Construct
     */
    JTextFieldCalculator() {

        super(Configuration.START_VALUE);

        setHorizontalAlignment(Configuration.TEXT_FIELD_HORIZONTAL_ALIGNMENT);
        setFont(new Font(
                Configuration.TEXT_FIELD_FONT_STYLE,
                Configuration.TEXT_FIELD_FONT,
                Configuration.TEXT_FIELD_FONT_SIZE)
        );
        setBounds(
                Configuration.HORIZONTAL_INDENTATION,
                Configuration.VERTICAL_INDENTATION,
                Configuration.TEXT_FIELD_WIDTH,
                Configuration.TEXT_FIELD_HEIGHT
        );
        setSelectionStart(Configuration.TEXT_FIELD_SELECTION_START);
        setEditable(Configuration.TEXT_FIELD_IS_EDITABLE);

    }

}
