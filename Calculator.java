import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;

/**
 * Calculator
 */
public class Calculator extends JFrame implements ActionListener, KeyListener {

    //add input field
    protected JTextFieldCalculator t = new JTextFieldCalculator();

    //add icon and image
    protected ImageIcon icon = new ImageIcon(getClass().getResource(Configuration.IMAGE_PATH + Configuration.IMAGE_ICON));
    protected ImageIcon background = new ImageIcon(getClass().getResource(Configuration.IMAGE_PATH + Configuration.IMAGE_BACKGROUND));

    //create buttons
    protected JButtonCalculator[][] buttons = new JButtonCalculator[Configuration.BUTTON_NAME.length][Configuration.BUTTON_NAME[0].length];

    //inversion status
    protected boolean inversion = false;

    //save result after calculation for new formula
    protected boolean calculationIsDone = false;

    /**
     * Constructor
     */
    Calculator() {
        //add title
        super(Configuration.WINDOW_TITLE);

        //add icon and image
        setIconImage(icon.getImage());
        setLayout(new BorderLayout());
        setContentPane(new JLabel(background));
        setLayout(new FlowLayout());

        //create buttons
        createButtons();
        //add buttons
        addButtons();
        //add listeners
        addListeners();

        //some configuration
        setSize(Configuration.WINDOW_WIDTH, Configuration.WINDOW_HEIGHT);
        setResizable(Configuration.WINDOW_IS_RESIZABLE);
        setLocationRelativeTo(Configuration.WINDOW_LOCATION_RELATIVE_TO);
        setLayout(Configuration.WINDOW_LOCATION_LAYOUT);
        setVisible(Configuration.WINDOW_IS_VISIBLE);

    }

    /**
     * Create buttons
     */
    private void createButtons() {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButtonCalculator(
                        Configuration.BUTTON_NAME[i][j],
                        Configuration.BUTTON_HORIZONTAL_INDENTATION + Configuration.BUTTON_HORIZONTAL_DIFFERENT * j,
                        Configuration.BUTTON_VERTICAL_INDENTATION * i + Configuration.BUTTON_VERTICAL_DIFFERENT,
                        Configuration.BUTTON_WIDTH,
                        Configuration.BUTTON_HEIGHT
                );
            }
        }
    }

    /**
     * Add buttons on frame
     */
    private void addButtons() {
        //add text field
        add(t);
        //add buttons
        for (JButtonCalculator[] button : buttons) {
            for (JButtonCalculator jButtonCalculator : button) {
                add(jButtonCalculator);
            }
        }
    }

    /**
     * Add listeners on every buttons
     */
    private void addListeners() {
        t.addKeyListener(this);
        for (JButtonCalculator[] button : buttons) {
            for (JButtonCalculator jButtonCalculator : button) {
                jButtonCalculator.addActionListener(this);
                jButtonCalculator.addKeyListener(this);
            }
        }
    }

    /**
     * Calculate key
     *
     * @param formula calculate formula
     */
    private void calculate(String formula) {
        t.setText(Calculate.calculateAll(formula));
        //calculation is done
        calculationIsDone = true;
    }

    /**
     * Backspace key
     *
     * @throws BadLocationException ignored
     */
    private void backspace() throws BadLocationException {
        //check length
        if (t.getText().length() <= 1) {
            t.setText("0");
        } else {
            t.setText(t.getText(0, t.getText().length() - 1));
        }
    }

    /**
     * Action
     *
     * @param event action event
     */
    public void actionPerformed(ActionEvent event) {
        String formula = t.getText();

        switch (event.getActionCommand()) {
            //inversion buttons
            case "Inv" -> {
                for (int i = 0; i < buttons.length; i++) {
                    for (int j = 0; j < buttons[i].length; j++) {
                        if (!Configuration.BUTTON_NAME_INVERSE[i][j].equals("")) {
                            if (inversion) {
                                buttons[i][j].setText(Configuration.BUTTON_NAME[i][j]);
                            } else {
                                buttons[i][j].setText(Configuration.BUTTON_NAME_INVERSE[i][j]);
                            }
                        }
                    }
                }
                inversion = !inversion;
            }
            //calculate
            case "=" -> calculate(formula);
            case "AC" -> t.setText("0");
            case "<--" -> {
                try {
                    backspace();
                } catch (Exception ignored) {
                }
            }
            default -> {
                //check 0 and calculation done
                if (formula.equals("0") ||
                        //if calculation is done and next symbol is not operand
                        (calculationIsDone &&
                                !event.getActionCommand().equals("-") &&
                                !event.getActionCommand().equals("+") &&
                                !event.getActionCommand().equals("*") &&
                                !event.getActionCommand().equals("/")
                        )
                ) {
                    formula = "";
                }
                //new calculation
                calculationIsDone = false;
                //check constants
                if (Configuration.BUTTON_CONSTANTS.containsKey(event.getActionCommand())) {
                    t.setText(formula + Configuration.BUTTON_CONSTANTS.get(event.getActionCommand()));
                } else {
                    t.setText(formula + event.getActionCommand());
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String formula = t.getText();
        try {
            //is equal to
            if (e.getKeyChar() == Configuration.KEY_CODE_IS_EQUAL_TO) {
                calculate(formula);
            }
            //backspace
            else if (e.getKeyCode() == Configuration.KEY_CODE_BACKSPACE) {
                backspace();
            }
            //other
            else {
                //check 0
                if (formula.equals("0")) {
                    formula = "";
                }
                //check key codes range
                if (e.getKeyCode() >= Configuration.KEY_CODE_MIN && e.getKeyCode() <= Configuration.KEY_CODE_MAX) {
                    t.setText(formula + e.getKeyChar());
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Start point
     *
     * @param args array with arguments
     */
    public static void main(String[] args) {
        new Calculator();
    }

}