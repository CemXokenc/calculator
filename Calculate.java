import java.util.ArrayList;
import java.util.Arrays;

/**
 * Application to Calculator
 */
public class Calculate {

    /**
     * Left bracket name
     */
    protected static final String LEFT_BRACKET_NAME = "left";
    /**
     * Right bracket name
     */
    protected static final String RIGHT_BRACKET_NAME = "right";
    /**
     * Regex variables
     */
    protected static final String REGEX_VARIABLES = "[-+*/^]";
    /**
     * Regex operators
     */
    protected static final String REGEX_OPERATORS = "[^-+*/^]";
    /**
     * Allow variables symbols
     */
    protected static final String ALLOW_SYMBOLS = "[-a-zA-Z]*";
    /**
     * Allow symbols on first two symbols
     */
    protected static final String REGEX_DOUBLE_OPERATORS_ALLOW = "[-]{2}[0-9.].*";
    /**
     * Not allow symbols on first two symbols
     */
    protected static final String REGEX_DOUBLE_OPERATORS_NOT_ALLOW = "[-=*/^]{2,}.*";
    /**
     * Digital regex
     */
    protected static final String REGEX_DIGITAL = "[0-9]";
    /**
     * Last symbol
     */
    protected static final String REGEX_LAST_SYMBOL = "[-+*/^.]";
    /**
     * Formatter result regex
     */
    protected static final String REGEX_FORMATTER_RESULT = ".*\\.0+";
    /**
     * Entered formula
     */
    public static String formula;
    /**
     * The result that we modify after each operation
     */
    public static String result;

    /**
     * Calculate formula
     */
    protected static String calculateAll(String enteredFormula) {
        result = formula = enteredFormula;
        int leftBrackets = countBrackets(LEFT_BRACKET_NAME);
        int rightBrackets = countBrackets(RIGHT_BRACKET_NAME);
        String subFormula;
        int indexLeft, indexRight;

        //check conformity
        if (leftBrackets == rightBrackets) {
            //loop where we gradually calculate all the brackets
            while (leftBrackets > 0) {
                indexLeft = result.lastIndexOf("(");
                indexRight = result.indexOf(")", indexLeft);

                //check bracket position
                if (indexLeft != -1 && indexRight == -1) {
                    break;
                }
                //check brackets position, if more than one pair of parentheses was opened in one iteration
                if (indexLeft == -1 && indexRight == -1) {
                    break;
                }

                subFormula = result.substring(indexLeft + 1, indexRight);
                result = result.replace("(" + subFormula + ")", calculate(subFormula));
                leftBrackets--;
            }
        }

        result = calculate(result);
        return formatResult(result);
    }

    /**
     * Count brackets in formula
     *
     * @param bracket left ot right
     * @return count
     */
    private static int countBrackets(String bracket) {
        int count = 0;

        //count all brackets in formula
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '(' && bracket.equals(LEFT_BRACKET_NAME) ||
                    result.charAt(i) == ')' && bracket.equals(RIGHT_BRACKET_NAME)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Calculate row between brackets
     *
     * @param subFormula formula between brackets
     * @return new formula after calculate
     */
    protected static String calculate(String subFormula) {
        //check empty brackets
        if (subFormula.length() == 0) {
            return "0";
        }
        //check correct last symbol
        if (String.valueOf(subFormula.charAt(subFormula.length() - 1)).matches(REGEX_LAST_SYMBOL)) {
            return subFormula;
        } else {
            //fill variables and operators
            ArrayList<String> variables = getVariables(subFormula);
            ArrayList<String> operators = getOperators(subFormula);

            normalizeVariablesOperators(variables, 0);
            normalizeVariablesOperators(operators, 1);
            normalizeFirstOperator(variables, operators);
            normalizeDoubleOperators(variables, operators);

            //math operation
            functionOperations(variables, operators);
            exponentiation(variables, operators);
            multiplication(variables, operators);
            adding(variables, operators);

            //compose new formula
            return composeFormula(variables, operators);
        }
    }

    /**
     * Get variables in sub formula
     *
     * @param subFormula sub formula
     * @return array with variables
     */
    private static ArrayList<String> getVariables(String subFormula) {
        return new ArrayList<>(Arrays.asList(subFormula.replaceAll(REGEX_VARIABLES, " ").split(" ")));
    }

    /**
     * Get operators in sub formula
     *
     * @param subFormula sub formula
     * @return array with operators
     */
    private static ArrayList<String> getOperators(String subFormula) {
        return new ArrayList<>(Arrays.asList(subFormula.replaceAll(REGEX_OPERATORS, " ").split(" ")));
    }

    /**
     * Normalize variables and operators. Remove "", " "
     *
     * @param array   array with variables or operators
     * @param startId start id. 0 if variables, 1 if operators
     */
    private static void normalizeVariablesOperators(ArrayList<String> array, int startId) {
        String arrayElement;
        //go through the entire array
        for (int i = startId; i < array.size(); i++) {
            arrayElement = array.get(i);
            //check bad operators
            if (arrayElement == null || arrayElement.equals("") || arrayElement.equals(" ")) {
                array.remove(i);
                i--;
            }
        }
    }

    /**
     * Normalize first operator
     *
     * @param variables array with variables
     * @param operators array with operators
     */
    private static void normalizeFirstOperator(ArrayList<String> variables, ArrayList<String> operators) {
        //check first element
        if (operators.size() > 0) {
            if (operators.get(0).equals("-")) {
                operators.set(0, "");
                variables.set(0, "-" + variables.get(0));
            }
        }
    }

    /**
     * Check double operators and fix it
     *
     * @param variables array with variables
     * @param operators array with operators
     */
    private static void normalizeDoubleOperators(ArrayList<String> variables, ArrayList<String> operators) {
        String oldOperator, newOperator;
        for (int i = 0; i < operators.size(); i++) {
            //check many operators in row
            if (operators.get(i).length() > 2) {
                break;
            }
            //switch double operators
            if (operators.get(i).length() == 2) {
                oldOperator = operators.get(i);
                //replace operators
                switch (oldOperator) {
                    case "++", "--" -> newOperator = "+";
                    case "+-", "-+" -> newOperator = "-";
                    case "+*", "*+" -> newOperator = "*";
                    case "+/", "/+" -> newOperator = "/";
                    case "+^", "^+" -> newOperator = "^";
                    case "*-" -> {
                        newOperator = "*";
                        variables.set(i, "-" + variables.get(i));
                    }
                    case "/-" -> {
                        newOperator = "/";
                        variables.set(i, "-" + variables.get(i));
                    }
                    case "^-" -> {
                        newOperator = "^";
                        variables.set(i, "-" + variables.get(i));
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + oldOperator);
                }

                operators.set(i, newOperator);
            }
        }
    }

    /**
     * Calculate function operations
     *
     * @param variables array with variables
     * @param operators array with variables
     */
    private static void functionOperations(ArrayList<String> variables, ArrayList<String> operators) {
        String var, beginSymbol = "";
        //pass through the array
        for (int i = 0; i < variables.size(); i++) {
            String variable = variables.get(i);
            //check fist symbol if variables in array 1
            if (variable.charAt(0) == '-') {
                variable = variable.substring(1);
                //if not function
                if (variable.matches(REGEX_DIGITAL)) {
                    variable = "-" + variable;
                } else {
                    beginSymbol = "-";
                }
            }
            //cos
            if (variable.matches("cos.*")) {
                if (variable.length() <= 3) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(3);
                variables.set(i, beginSymbol + Math.cos(Double.parseDouble(var)));
                break;
            }
            //sin
            if (variable.matches("sin.*")) {
                if (variable.length() <= 3) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(3);
                variables.set(i, beginSymbol + Math.sin(Double.parseDouble(var)));
                break;
            }
            //tan
            if (variable.matches("tan.*")) {
                if (variable.length() <= 3) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(3);
                variables.set(i, beginSymbol + Math.tan(Double.parseDouble(var)));
                break;
            }
            //atan
            if (variable.matches("atan.*")) {
                if (variable.length() <= 4) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(4);
                variables.set(i, beginSymbol + Math.atan(Double.parseDouble(var)));
                break;
            }
            //sqrt
            if (variable.matches("sqrt.*")) {
                if (variable.length() <= 4) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(4);
                variables.set(i, beginSymbol + Math.sqrt(Double.parseDouble(var)));
                break;
            }
            //log2
            if (variable.matches("log2.*")) {
                if (variable.length() <= 4) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(4);
                variables.set(i, beginSymbol + Math.log10(Double.parseDouble(var)) / Math.log10(2));
                break;
            }
            //log10
            if (variable.matches("log10.*")) {
                if (variable.length() <= 5) {
                    variable = normalizeUnaryFunction(variable, i, variables, operators);
                }
                var = variable.substring(5);
                variables.set(i, beginSymbol + Math.log10(Double.parseDouble(var)));
                break;
            }
        }
    }

    /**
     * Normalization of functions of this type "cos-30"
     *
     * @param variable  function variable
     * @param i         index of variable in variables array
     * @param variables array with variables
     * @param operators array with operators
     * @return normalized variable
     */
    private static String normalizeUnaryFunction(String variable, int i, ArrayList<String> variables, ArrayList<String> operators) {
        variable = variable + operators.get(i + 1) + variables.get(i + 1);
        variables.remove(i + 1);
        operators.remove(i + 1);

        return variable;
    }

    /**
     * Calculate exponentiation
     *
     * @param variables array with variables
     * @param operators array with operators
     */
    private static void exponentiation(ArrayList<String> variables, ArrayList<String> operators) {
        double temp, firstVariable, secondVariable;
        //go through the entire array
        for (int i = operators.size() - 1; i > 0; i--) {
            if (operators.get(i).equals("^")) {
                //get variables
                firstVariable = getVariable(variables, i - 1);
                secondVariable = getVariable(variables, i);
                //calculate
                temp = Math.pow(firstVariable, secondVariable);
                //edit arrays
                variables.set(i - 1, String.valueOf(temp));
                variables.remove(i);
                operators.remove(i);
            }
        }
    }

    /**
     * Calculate multiplication
     *
     * @param variables array with variables
     * @param operators array with operators
     */
    private static void multiplication(ArrayList<String> variables, ArrayList<String> operators) {
        double temp = 0, firstVariable, secondVariable;
        //go through the entire array
        for (int i = 1; i < operators.size(); i++) {
            if (operators.get(i).equals("*") || operators.get(i).equals("/")) {
                //get variables
                firstVariable = getVariable(variables, i - 1);
                secondVariable = getVariable(variables, i);
                //calculate
                if (operators.get(i).equals("*")) {
                    temp = firstVariable * secondVariable;

                } else if (operators.get(i).equals("/")) {
                    temp = firstVariable / secondVariable;

                }
                //edit arrays
                variables.set(i - 1, String.valueOf(temp));
                variables.remove(i);
                operators.remove(i);
                i--;
            }
        }
    }

    /**
     * Calculate adding
     *
     * @param variables array with variables
     * @param operators array with operators
     */
    private static void adding(ArrayList<String> variables, ArrayList<String> operators) {
        double temp = 0, firstVariable, secondVariable;

        //go through the entire array
        for (int i = 1; i < operators.size(); i++) {
            if (operators.get(i).equals("+") || operators.get(i).equals("-")) {
                //get variables
                firstVariable = getVariable(variables, i - 1);
                secondVariable = getVariable(variables, i);

                //calculate
                if (operators.get(i).equals("+")) {
                    temp = firstVariable + secondVariable;
                } else if (operators.get(i).equals("-")) {
                    temp = firstVariable - secondVariable;
                }
                //edit arrays
                variables.set(i - 1, String.valueOf(temp));
                variables.remove(i);
                operators.remove(i);
                i--;
            }
        }
    }

    /**
     * Get variables. Check hash map and replace variables on it value
     *
     * @param variables array with variables
     * @param i         number of variable
     * @return variables value
     */
    private static double getVariable(ArrayList<String> variables, int i) {
        String variable = variables.get(i);
        StringBuilder temp = new StringBuilder();
        //check allowed symbols
        if (variable.matches(ALLOW_SYMBOLS)) {
            //check format "-x"
            while (variable.charAt(0) == '-') {
                variable = variable.substring(1);
                temp.append("-");
            }
            variable += temp;
        }

        //check double operators format "--", "+-", "++" ...
        if (variable.matches(REGEX_DOUBLE_OPERATORS_NOT_ALLOW)) {
            if (variable.matches(REGEX_DOUBLE_OPERATORS_ALLOW)) {
                variable = variable.substring(2);
            }
        }

        return Double.parseDouble(variable);
    }

    /**
     * Compose new formula after calculate
     *
     * @param variables array with variables
     * @param operators array with operators
     * @return new formula
     */
    private static String composeFormula(ArrayList<String> variables, ArrayList<String> operators) {
        StringBuilder formula = new StringBuilder();

        for (int i = 0; i < operators.size(); i++) {
            formula.append(operators.get(i)).append(variables.get(i));
        }
        //edge case
        if (operators.size() == 0) {
            formula.append(variables.get(0));
        }

        return String.valueOf(formula);
    }

    /**
     * Formatting result string
     *
     * @param result string
     * @return formatted string
     */
    private static String formatResult(String result) {
        if (result.matches(REGEX_FORMATTER_RESULT)) {
            return result.substring(0, result.lastIndexOf('.'));
        }

        return result;
    }

}
