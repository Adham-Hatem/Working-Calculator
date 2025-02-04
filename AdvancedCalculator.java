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
        else if (!checkIfValid(calculation).equals("valid")) {
            System.out.print(calculation);
            System.out.println("\t\t" + errorFinder(checkIfValid(calculation)));

            historyFunction(calculationNotClean, errorFinder(checkIfValid(calculation)),
                    history, historyLength, historyPlacement);
        }
        else {
            String result = calculate(calculation);

            if(!errorChecker(result)){
                System.out.println("\t\t~ Result: " + result);
                historyFunction(calculationNotClean, errorFinder(result), history, historyLength, historyPlacement);
            }
            else {
                System.out.println("\t\t" + errorFinder(result));
                historyFunction(calculationNotClean, errorFinder(result), history, historyLength, historyPlacement);
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

    public static String checkIfValid(String calculation) {
        String validChars = "0123456789+-*/()^.sincostansqrtlnlog";

        for (int i = 0; i < calculation.length(); i++) {
            char c = calculation.charAt(i);
            if (calculation.contains("(") && !calculation.contains(")"))
                return "bracketErr";
            else if (validChars.indexOf(c) == -1) {
                return "improperEntry";
            }
        }

        return switch (calculation.charAt(0)) {
            case '/', '*', '^' -> "placementErr";
            default -> switch (calculation.charAt(calculation.length()-1)) {
                            case '/', '*', '^', 't' -> "placementErr";
                            default -> "valid";
            };
        };
    }

    public static String errorFinder(String result){
        return switch (result) {
            case "functionErr" -> "!! Error : Function Error !!";
            case "powerErr" ->  "!! Error : Power Error !!";
            case "multiplicationErr" -> "!! Error : Multiplication Error !!";
            case "zeroDivision" -> "!! Error : Zero Division !!";
            case "complexErr" -> "!! Error : Complex Error !!";
            case "placementErr" -> "!! Error : Placement Error !!";
            case "bracketErr" -> "!! Error : Bracket Error !!";
            case "sqrtErr" -> "!! Error : Square Root Error !!";
            case "improperEntry" -> "!! Error : Improper Entry !!";
            default -> null;
        };
    }

    public static boolean errorChecker(String result){
        return switch (result) {
            case "functionErr", "powerErr", "zeroDivision", "complexErr", "placementErr",
                 "multiplicationErr", "bracketErr" ,"sqrtErr", "improperEntry" -> true;
            default -> false;
        };
    }

    public static String calculate(String calculation) {
        while (calculation.contains("(")) {
            int closeIndex = calculation.indexOf(")");
            int openIndex = calculation.lastIndexOf("(", closeIndex);
            String subExpression = calculation.substring(openIndex + 1, closeIndex);
            double subResult = Double.parseDouble(calculate(subExpression));
            calculation = calculation.substring(0, openIndex)
                    + subResult + calculation.substring(closeIndex + 1);
        }

        calculation = calculateFunctions(calculation);

        if (errorChecker(calculation)) return calculation;

        calculation = calculatePower(calculation);

        if (errorChecker(calculation)) return calculation;

        return calculateSimple(calculation);
    }

    public static String calculateFunctions(String calculation) {
        String[] trigFunctions = {"sin", "cos", "tan"};
        String[] logFunctions = {"log", "ln"};
        String[] sqrtFunctions = {"sqrt"};
        String letters = "abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < trigFunctions.length; i++) {
            String func = trigFunctions[i];

            while (calculation.contains(func)) {
                int funcIndex = calculation.indexOf(func);
                int startIndex = funcIndex + func.length();
                int endIndex = startIndex;

                while (endIndex < calculation.length() && (Character.isDigit(calculation.charAt(endIndex))
                        || calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                if (calculation.substring(startIndex, endIndex).isEmpty()) return "functionErr";
                double angle = Double.parseDouble(calculation.substring(startIndex, endIndex));
                if (angle == 90 && func.equals("tan")) return "complexErr";

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
                int sqrtLeft = funcIndex + func.length();
                int sqrtRight = sqrtLeft;



                while (sqrtRight < calculation.length() && (Character.isDigit(calculation.charAt(sqrtRight)) ||
                        calculation.charAt(sqrtRight) == '.')) {
                    sqrtRight++;
                }

                if (!Character.isDigit(calculation.charAt(sqrtRight)) || calculation.charAt(sqrtLeft) == '-')
                    return "sqrtErr";

                double value = Double.parseDouble(calculation.substring(sqrtLeft, sqrtRight));

                double sqrtResult = Math.sqrt(value);
                calculation = calculation.substring(0, funcIndex) + sqrtResult + calculation.substring(sqrtRight);
            }
        }

        for (int index = 0; index < calculation.length(); index++) {
            char character = calculation.charAt(index);

            if (letters.indexOf(character) != -1)
                return "improperEntry";
        }

        return calculation;
    }

    public static String calculatePower(String calculation) {
        while (calculation.contains("^")) {
            int powerIndex = calculation.indexOf("^");
            int leftStart = powerIndex - 1;
            int rightEnd = powerIndex + 1;

            if (leftStart < 0 || !Character.isDigit(calculation.charAt(leftStart)) ||
                    !Character.isDigit(calculation.charAt(rightEnd)))
                return "powerErr";

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

    public static String calculateSimple(String calculation) {
        double result = 0;
        double currentNumber = 0;
        double decimalPlace = 0.1;
        char lastOperator = '+';
        boolean inDecimal = false;
        String divisionLeft;
        String divisionRight;


        for (int multiplicationIndex = 0; multiplicationIndex < calculation.length(); multiplicationIndex++) {
            char chosenChar = calculation.charAt(multiplicationIndex);

            if (chosenChar == '*'){
                int multiplicationLeft = multiplicationIndex-1;
                int multiplicationRight = multiplicationIndex+1;

                while (multiplicationLeft > 0 && (Character.isDigit(calculation.charAt(multiplicationLeft)) ||
                        calculation.charAt(multiplicationLeft - 1) == '.')) {
                    multiplicationLeft--;
                }

                while (multiplicationRight < calculation.length() && (Character.isDigit
                        (calculation.charAt(multiplicationRight)) || calculation.charAt(multiplicationRight) == '.')) {
                    multiplicationRight++;
                }

                if (multiplicationLeft < 0 || !Character.isDigit(calculation.charAt(multiplicationLeft)) ||
                        !Character.isDigit(calculation.charAt(multiplicationRight - 1)))
                    return "multiplicationErr";

                double leftHandSide = Double.parseDouble(calculation.substring(multiplicationLeft,
                        multiplicationIndex));
                double rightHandSide = Double.parseDouble(calculation.substring(multiplicationIndex+1,
                        multiplicationRight));

                double multiplicationResult = leftHandSide * rightHandSide;

                calculation = calculation.substring(0, multiplicationLeft) + multiplicationResult +
                        calculation.substring(multiplicationRight);
            }
        }

        for (int index = 0; index < calculation.length(); index++) {
            char chosenChar = calculation.charAt(index);

            if (Character.isDigit(chosenChar)) {
                if (inDecimal) {
                    currentNumber += (chosenChar - '0') * decimalPlace;
                    decimalPlace *= 0.1;
                }
                else {
                    currentNumber = currentNumber * 10 + (chosenChar - '0');
                }
            }
            else if (chosenChar == '.') {
                inDecimal = true;
                decimalPlace = 0.1;
            }
            else if (chosenChar == '/') {
                divisionLeft = calculation.substring(0, calculation.indexOf('/'));
                divisionRight = calculation.substring(calculation.indexOf('/')+1);

                divisionLeft = calculateSimple(divisionLeft);
                divisionRight = calculateSimple(divisionRight);

                if (divisionRight.equals("0.0"))
                    return "zeroDivision";

                return Double.toString(Double.parseDouble(divisionLeft)/Double.parseDouble(divisionRight));
            }
            else if (operator(chosenChar)) {
                String resultString = applyOperation(result, currentNumber, lastOperator);
                if (errorFinder(resultString) == null){
                    result = Double.parseDouble(applyOperation(result, currentNumber, lastOperator));
                    currentNumber = 0;
                    inDecimal = false;
                    lastOperator = chosenChar;
                }
                else return resultString;
            }
        }

        return applyOperation(result, currentNumber, lastOperator);
    }

    public static boolean operator(char c) {
        return c == '+' || c == '-' || c == '*';
    }

    public static String applyOperation(double left, double right, char operator) {

        return switch (operator) {
            case '+' -> Double.toString(left + right);
            case '-' -> Double.toString(left - right);
            default -> Double.toString(right);
        };
    }
}
