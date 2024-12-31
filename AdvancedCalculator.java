import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings({"InfiniteLoopStatement", "ForLoopReplaceableByForEach"})
public class AdvancedCalculator {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);

        int historyLength = 10;
        int[] historyPlacement = {0};
        String[][] history = new String[historyLength][2];

        commands();

        while (true) {
            System.out.print("\n> Enter your Command: ");
            String calculationNotClean = console.nextLine();

            userChoice(calculationNotClean, history, historyLength, historyPlacement);
        }
    }

    public static void commands(){
        System.out.print("\n\t~ Commands:\n\t - Commands\n\t - History\n\t - Clear History\n\t - Bye\n\t");
    }

    public static void userChoice(String calculationNotClean, String[][] history,
                                  int historyLength, int[] historyPlacement){
        String calculation = removeSpaces(calculationNotClean);

        if (calculation.equalsIgnoreCase("bye")) {
            exit();
        }
        else if(calculation.equalsIgnoreCase("commands")){
            commands();
        }
        else if (calculation.equalsIgnoreCase("history")) {
            historyPrint(history, historyLength);
        }
        else if(calculation.equalsIgnoreCase("clearhistory")){
            historyClear(history, historyLength);
        }
        else if (!checkIfValid(calculation)) {
            System.out.println("\t\t!! Error : Improper Entry !!");
            historyFunction(calculationNotClean, "!! Error : Improper Entry !!",
                    history, historyLength, historyPlacement);
        }
        else {
            double result = calculate(calculation);
            String resultString = String.valueOf(result);
            resultString = errorFinder(resultString);

            if(resultString == null){
                System.out.println("\t\t~ Result: " + result);
                historyFunction(calculationNotClean, Double.toString(result), history, historyLength, historyPlacement);
            }
            else {
                System.out.println("\t\t" + resultString);
                historyFunction(calculationNotClean, resultString, history, historyLength, historyPlacement);
            }
        }
    }

    public static String removeSpaces(String expression) {
        return expression.toLowerCase().replaceAll(" ", "");
    }

    public static void exit() {
        System.out.print("\n\t\t\t~ Bye Bye ~");
        System.exit(0);
    }

    public static void historyPrint(String[][] history, int historyLength){
        System.out.println("\n\t~ History: ");
        for (int i = 0; i < historyLength; i++) {
            if (history[i][0] != null)
                System.out.println("\t\t" + Arrays.toString(history[i]));
        }
    }

    public static void historyClear(String[][] history, int historyLength){
        for (int column = 0; column < 2; column++) {
            for (int row = 0; row < historyLength; row++) {
                if(history[row][column] != null) history[row][column] = null;
                else break;
            }
        }
        System.out.print("\t\t!! History Cleared !!\n");
    }

    public static void historyFunction(String calculationNotClean, String resultAny,
                                       String[][] history, int historyLength, int[] historyPlacement){
        history[historyPlacement[0]][0] = calculationNotClean;
        history[historyPlacement[0]][1] = "= " + resultAny;
        historyPlacement[0]++;
        if(historyPlacement[0] == historyLength) historyPlacement[0] = 0;
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

    public static String errorFinder(String result){
        return switch (result) {
            case "-0.0" -> "!! Error : Incomplete Function / Power Operation !!";
            case "Infinity" -> "!! Error : Division By Zero !!";
            default -> null;
        };
    }

    public static double calculate(String calculation) {
        while (calculation.contains("(")) {
            int closeIndex = calculation.indexOf(")");
            int openIndex = calculation.lastIndexOf("(", closeIndex);
            String subExpression = calculation.substring(openIndex + 1, closeIndex);
            double subResult = calculate(subExpression);
            calculation = calculation.substring(0, openIndex)
                    + subResult + calculation.substring(closeIndex + 1);
        }

        calculation = calculateFunctions(calculation);
        if(errorCheck(calculation)) return -0.0;
        calculation = calculatePower(calculation);
        if(errorCheck(calculation)) return -0.0;
        return calculateSimple(calculation);
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

                if(calculation.substring(startIndex, endIndex).isEmpty()) return "function error";
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

    public static boolean errorCheck(String calculation){
        return calculation.equals("power error") || calculation.equals("function error");
    }

    public static String calculatePower(String calculation) {
        while (calculation.contains("^")) {
            int powerIndex = calculation.indexOf("^");
            int leftStart = powerIndex - 1;
            int rightEnd = powerIndex + 1;

            if(leftStart < 0 || !Character.isDigit(calculation.charAt(leftStart)) ||
                    !Character.isDigit(calculation.charAt(rightEnd)))
                return "power error";

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

        return applyOperation(result, currentNumber, lastOperator);
    }

    public static boolean operator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    public static double applyOperation(double left, double right, char operator) {
        if (operator == '/' && right == 0) {
            return (Double.POSITIVE_INFINITY);
        }

        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> right;
        };
    }
}
