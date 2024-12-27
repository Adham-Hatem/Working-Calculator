import java.util.*;

@SuppressWarnings({"InfiniteLoopStatement", "ForLoopReplaceableByForEach"})
public class AdvancedCalculator {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        int historyLength = 10;
        int historyPlacement = 0;
        String[][] history = new String[historyLength][2];

        commands();
        while (true) {
            System.out.print("\n> Enter your Command: ");
            String calculationNotClean = console.nextLine();
            String calculation = removeSpaces(calculationNotClean);

            if (calculation.equalsIgnoreCase("bye")) {
                exit();
            }
            else if(calculation.equalsIgnoreCase("command")){
                commands();
            }
            else if (calculation.equalsIgnoreCase("history")) {
                System.out.print("\n~ History:\n");
                for (int i = 0; i < historyLength; i++) {
                    if (history[i][0] != null)
                        System.out.println("\t" + Arrays.toString(history[i]));
                }
            }
            else if(calculation.equalsIgnoreCase("clearhistory")){
                for (int column = 0; column < 2; column++) {
                    for (int row = 0; row < historyLength; row++) {
                        if(history[row][column] != null) history[row][column] = null;
                        else break;
                    }
                }
                System.out.print("\t\t!! History Cleared !!\n");
            }
            else if (!checkIfValid(calculation)) {
                System.out.println("\t!! Invalid calculation !!\n");
            }
            else {
                double result = calculate(calculation);
                System.out.println("\t~ Result: " + result);
                history[historyPlacement][0] = calculationNotClean;
                history[historyPlacement][1] = "= " + result;
                historyPlacement++;
                if(historyPlacement == historyLength) historyPlacement = 0;
            }
        }
    }

    public static boolean checkIfValid(String calculation) {
        String validChars = "0123456789+-*/()sincostanlogln^sqrt.";

        for (int i = 0; i < calculation.length(); i++) {
            char c = calculation.charAt(i);
            if (validChars.indexOf(c) == -1 || calculation.contains("(") && !calculation.contains(")"))
                return false;
        }
        return true;
    }

    public static double calculate(String calculation) {
        double result;

        while (calculation.contains("(")) {
            int closeIndex = calculation.indexOf(")");
            int openIndex = calculation.lastIndexOf("(", closeIndex);
            String subExpression = calculation.substring(openIndex + 1, closeIndex);
            double subResult = calculate(subExpression);
            calculation = calculation.substring(0, openIndex)
                    + subResult + calculation.substring(closeIndex + 1);
        }

        calculation = calculateFunctions(calculation);
        calculation = calculatePower(calculation);
        result = calculateSimple(calculation);
        return result;
    }

    public static String calculateFunctions(String calculation) {
        String[] trigFunctions = {"sin", "cos", "tan"};
        String[] logFunctions = {"log", "ln"};
        String[] sqrtFunctions = {"sqrt"};

        for (int i = 0; i < trigFunctions.length; i++) {
            String func = trigFunctions[i];
            while (calculation.contains(func)) {
                int funcIndex = calculation.indexOf(func);
                int startIndex = funcIndex + func.length();
                int endIndex = startIndex;

                while (endIndex < calculation.length() && (Character.isDigit(calculation.charAt(endIndex)) || calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                double angle = Double.parseDouble(calculation.substring(startIndex, endIndex));

                double trigResult = switch (func) {
                    case "sin" -> Math.sin(Math.toRadians(angle));
                    case "cos" -> Math.cos(Math.toRadians(angle));
                    case "tan" -> Math.tan(Math.toRadians(angle));
                    default -> 0;
                };

                calculation = calculation.substring(0, funcIndex) + trigResult + calculation.substring(endIndex);
            }
        }

        for (int i = 0; i < logFunctions.length; i++) {
            String func = logFunctions[i];
            while (calculation.contains(func)) {
                int funcIndex = calculation.indexOf(func);
                int startIndex = funcIndex + func.length();

                // Find the full number after the log or ln function
                int endIndex = startIndex;
                while (endIndex < calculation.length() && (Character.isDigit(calculation.charAt(endIndex)) ||
                        calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                double value = Double.parseDouble(calculation.substring(startIndex, endIndex));

                double logResult = switch (func) {
                    case "log" -> Math.log10(value);
                    case "ln" -> Math.log(value);
                    default -> 0;
                };

                calculation = calculation.substring(0, funcIndex) + logResult + calculation.substring(endIndex);
            }
        }

        for (int i = 0; i < sqrtFunctions.length; i++) {
            String func = sqrtFunctions[i];
            while (calculation.contains(func)) {
                int funcIndex = calculation.indexOf(func);
                int startIndex = funcIndex + func.length();

                int endIndex = startIndex;
                while (endIndex < calculation.length() && (Character.isDigit(calculation.charAt(endIndex)) ||
                        calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                double value = Double.parseDouble(calculation.substring(startIndex, endIndex));

                double sqrtResult = Math.sqrt(value);
                calculation = calculation.substring(0, funcIndex) + sqrtResult + calculation.substring(endIndex);
            }
        }

        return calculation;
    }

    public static String calculatePower(String calculation) {
        while (calculation.contains("^")) {
            int powerIndex = calculation.indexOf("^");
            int leftStart = powerIndex - 1;
            int rightEnd = powerIndex + 1;

            while (leftStart > 0 && (Character.isDigit(calculation.charAt(leftStart - 1)) ||
                    calculation.charAt(leftStart - 1) == '.')) {
                leftStart--;
            }

            while (rightEnd < calculation.length() && (Character.isDigit(calculation.charAt(rightEnd)) ||
                    calculation.charAt(rightEnd) == '.')) {
                rightEnd++;
            }

            double base = Double.parseDouble(calculation.substring(leftStart, powerIndex));
            double exponent = Double.parseDouble(calculation.substring(powerIndex + 1, rightEnd));

            double powerResult = Math.pow(base, exponent);

            calculation = calculation.substring(0, leftStart) + powerResult + calculation.substring(rightEnd);
        }

        return calculation;
    }

    public static double calculateSimple(String calculation) {
        double result = 0;
        char lastOperator = '+';
        double currentNumber = 0;
        boolean inDecimal = false;
        double decimalPlace = 0.1;

        for (int i = 0; i < calculation.length(); i++) {
            char c = calculation.charAt(i);

            if (Character.isDigit(c)) {
                if (inDecimal) {
                    currentNumber += (c - '0') * decimalPlace;
                    decimalPlace *= 0.1;
                }
                else {
                    currentNumber = currentNumber * 10 + (c - '0');
                }
            }
            else if (c == '.') {
                inDecimal = true;
                decimalPlace = 0.1;
            }
            else if (operator(c)) {
                result = applyOperation(result, currentNumber, lastOperator);
                currentNumber = 0;
                inDecimal = false;
                lastOperator = c;
            }
        }

        result = applyOperation(result, currentNumber, lastOperator);
        return result;
    }

    public static boolean operator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static double applyOperation(double left, double right, char operator) {
        if (operator == '/' && right == 0) {
            System.out.println("Error: Division by zero is not allowed.");
            return 0;
        }

        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> right;
        };
    }

    public static String removeSpaces(String expression) {
        return expression.replaceAll(" ", "");
    }

    public static void exit() {
        System.out.print("\n\t\t\t~ Bye Bye ~");
        System.exit(0);
    }

    public static void commands(){
        System.out.print("\n\t~ Commands:\n\t - Commands\n\t - History\n\t - Clear History\n\t - Bye\n\t");
    }
}
