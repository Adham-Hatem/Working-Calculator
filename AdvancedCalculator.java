import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings({"InfiniteLoopStatement", "DuplicatedCode", "TextBlockMigration", "ForLoopReplaceableByForEach"})
public class AdvancedCalculator {
    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        // History Specifications:
        File history = new File("history.txt");
        int historyLength = 5;
        historyFileCreation(history, historyLength);
        int[] historyPlacement = {getPlacement(history, historyLength)};

        commands("start");

        while (true) {
            System.out.print("\n> Enter your Command: ");
            String calculationNotClean = console.nextLine();

            userChoice(calculationNotClean, history, historyLength, historyPlacement);
        }
    }

    public static void commands(String instance){
        switch (instance) {
            case "start":
                System.out.print("\n\t~ Commands:\n\t - Commands\n\t - History" +
                        "\n\t - Clear History\n\t - Bye\n\t");
                break;
            case "implemented":
                System.out.print("\n\t~ Available Calculations:" +
                        "\n\t - Multiplication > * <" +
                        "\n\t - Division > / <" +
                        "\n\t - Subtraction > - <" +
                        "\n\t - Addition > + <" +
                        "\n\t - Power > ^ <" +
                        "\n\t - Root > {num}rt <" +
                        "\n\t - Modulus > % <" +
                        "\n\t - Logarithmic Operations > Log, ln <" +
                        "\n\t - Trigonometric Functions > sin, cos, tan <\n\t");
                break;
        }
    }

    public static void userChoice(String calculationNotClean, File history,
                                  int historyLength, int[] historyPlacement) throws IOException {
        String calculation = removeSpaces(calculationNotClean);

        if (calculation.equalsIgnoreCase("bye")) {
            exit();
        }
        else if(calculation.equalsIgnoreCase("commands")){
            commands("implemented");
        }
        else if (calculation.equalsIgnoreCase("history")) {
            historyPrint(history);
        }
        else if(calculation.equalsIgnoreCase("clearhistory") ||
                    calculation.equalsIgnoreCase("historyclear")){
            historyClear(history, historyLength);
        }
        else if (!checkIfValid(calculation).equals("valid")) {
            System.out.println("\t\t" + errorFinder(checkIfValid(calculation)));

            historyFunction(calculationNotClean, errorFinder(checkIfValid(calculation)),
                    history, historyPlacement, historyLength);
        }
        else {
            String result = calculate(calculation);

            if(!errorChecker(result)){
                System.out.println("\t\t~ Result: " + result);

                historyFunction(calculationNotClean, result, history,
                        historyPlacement, historyLength);
            }
            else {
                System.out.println("\t\t" + errorFinder(result));

                historyFunction(calculationNotClean, errorFinder(result), history,
                        historyPlacement, historyLength);
            }
        }
    }

    public static int getPlacement(File history, int historyLength)
            throws FileNotFoundException {
        Scanner historyReader = new Scanner(history);

        for (int count = 0; count <= historyLength - 1; count++) {
            historyReader.nextLine();
        }
        return  Integer.parseInt(historyReader.nextLine());
    }

    public static String removeSpaces(String expression) {
        return expression.toLowerCase().replaceAll(" ", "");
    }

    public static void exit() {
        System.out.print("\n\t\t\t~ Bye Bye ~");
        System.exit(0);
    }

    public static void historyFileCreation(File history, int historyLength) throws IOException {
        if (history.createNewFile()) {
            FileWriter historyWriter = new FileWriter(history);
            for (int line = 1; line <= historyLength; line++) {
                historyWriter.write(line + " = \n");
            }
            historyWriter.write("0");
            historyWriter.close();
            return;
        }

        Scanner historyReader = new Scanner(history);
        String lastLine = "";
        int count = 0;

        while (historyReader.hasNextLine()){
            lastLine = historyReader.nextLine();
            count++;
        }
        historyReader.close();

        if (lastLine.isEmpty() || lastLine.length() < String.valueOf(historyLength).length() ||
                lastLine.substring(0, String.valueOf(historyLength).length())
                .equals(Integer.toString(historyLength + 1)) ||
                !Character.isDigit(lastLine.charAt(0)) || count != (historyLength + 1)){
            FileWriter historyWriter = new FileWriter(history);


            for (int line = 1; line <= historyLength; line++) {
                historyWriter.write(line + " =  \n");
            }
            historyWriter.write("0");
            historyWriter.close();
        }
    }

    public static void historyClear(File history, int historyLength) throws IOException {
        FileWriter historyClearer = new FileWriter(history, false);
        historyClearer.close();

        FileWriter historyWriter = new FileWriter(history);

        for (int line = 1; line <= historyLength; line++) {
            historyWriter.write(line + " =  \n");
        }
        historyWriter.write("0");
        historyWriter.close();

        System.out.println("\n\t!! History Cleared Successfully !!");
    }

    public static void historyPrint(File history) throws FileNotFoundException {
        Scanner historyScanner = new Scanner(history);
        boolean historyEmpty = true;

        System.out.print("\n\t ~ History:");

        while (historyScanner.hasNextLine()) {
            String line = historyScanner.nextLine();
            String validChars = "0123456789+-*/()^.sincostansqrtlnlog!";

            int equalsIndex = line.indexOf('=');
            if (equalsIndex != -1 && equalsIndex + 2 < line.length()) {
                char c = line.charAt(equalsIndex + 2);

                if (validChars.indexOf(c) > -1) {
                    System.out.print("\n\t\t - " + line.substring(line.indexOf(" ")));
                    historyEmpty = false;
                }
            }
        }

        if (historyEmpty) {
            System.out.print("\t!! History Empty !!");
        }

        System.out.println();
    }

    public static void historyFunction(String calculationNotClean, String resultAny , File history,
                                       int[] historyPlacement, int historyLength) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        BufferedReader historyReader = new BufferedReader(new FileReader(history));
        String line;

        while ((line = historyReader.readLine()) != null) {
            lines.add(line);
        }
        historyReader.close();
        lines.set(historyPlacement[0], (historyPlacement[0]+1) + " " + calculationNotClean +
                " = " + resultAny);

        historyPlacement[0]++;
        lines.set(historyLength, String.valueOf(historyPlacement[0]));

        BufferedWriter historyWriter = new BufferedWriter(new FileWriter(history));
        for (int NewLine = 0; NewLine < lines.size(); NewLine++) {
            historyWriter.write(lines.get(NewLine));
            historyWriter.newLine();
        }

        historyWriter.close();

        if (historyPlacement[0] > historyLength){
            historyPlacement[0] = 0;
        }
    }

    public static String checkIfValid(String calculation) {
        String validChars = "0123456789+-*/()^%.sincostansqrtlnlog";
        for (int count = 0; count < calculation.length(); count++) {
            char c = calculation.charAt(count);

            if (calculation.contains("(") && !calculation.contains(")"))
                return "bracketErr";

            else if (validChars.indexOf(c) == -1) {
                return "improperEntry";
            }
        }

        if (calculation.isEmpty()){
            return "improperEntry";
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
            case "rtErr" -> "!! Error : Root Error !!";
            case "ModuloErr" -> "!! Error : Modulus Error !!";
            case "improperEntry" -> "!! Error : Improper Entry !!";
            default -> null;
        };
    }

    public static boolean errorChecker(String result){
        return switch (result) {
            case "functionErr", "powerErr", "zeroDivision", "complexErr", "placementErr",
                 "multiplicationErr", "ModuloErr", "bracketErr" ,"rtErr", "improperEntry" -> true;
            default -> false;
        };
    }

    public static String calculate(String calculation) {
        while (calculation.contains("(")) {
            int closeIndex = calculation.indexOf(")");
            int openIndex = calculation.lastIndexOf("(", closeIndex);
            String subExpression = calculation.substring(openIndex + 1, closeIndex);
            String subResult = calculate(subExpression);
            calculation = calculation.substring(0, openIndex)
                    + subResult + calculation.substring(closeIndex + 1);
        }

        calculation = calculateFunctions(calculation);

        if (errorChecker(calculation)) return calculation;

        calculation = calculatePower(calculation);

        if (errorChecker(calculation)) return calculation;

        calculation = calculateModulus(calculation);

        if (errorChecker(calculation)) return calculation;

        return calculateSimple(calculation);
    }

    public static String calculateFunctions(String calculation) {
        String[] trigFunctions = {"sin", "cos", "tan"};
        String[] logFunctions = {"log", "ln"};
        String[] rtFunctions = {"rt"};
        String letters = "abcdefghijklmnopqrstuvwxyz";

        for (String function : trigFunctions) {
            while (calculation.contains(function)) {
                int functionIndex = calculation.indexOf(function);
                int startIndex = functionIndex + function.length();
                int endIndex = startIndex;

                while (endIndex < calculation.length()
                        && (Character.isDigit(calculation.charAt(endIndex))
                        || calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                if (calculation.substring(startIndex, endIndex).isEmpty()) return "functionErr";
                double angle = Double.parseDouble(calculation.substring(startIndex, endIndex));
                if (angle == 90 && function.equals("tan")) return "complexErr";

                double trigResult = switch (function) {
                    case "sin" -> Math.sin(Math.toRadians(angle));
                    case "cos" -> Math.cos(Math.toRadians(angle));
                    case "tan" -> Math.tan(Math.toRadians(angle));
                    default -> 0;
                };

                calculation = calculation.substring(0, functionIndex)
                        + trigResult + calculation.substring(endIndex);

            }
        }

        for (String function : logFunctions) {
            while (calculation.contains(function)) {
                int functionIndex = calculation.indexOf(function);
                int startIndex = functionIndex + function.length();
                int endIndex = startIndex;

                while (endIndex < calculation.length()
                        && (Character.isDigit(calculation.charAt(endIndex)) ||
                        calculation.charAt(endIndex) == '.')) {
                    endIndex++;
                }

                double value = Double.parseDouble(calculation.substring(startIndex, endIndex));

                double logResult = switch (function) {
                    case "log" -> Math.log10(value);
                    case "ln" -> Math.log(value);
                    default -> 0;
                };

                calculation = calculation.substring(0, functionIndex)
                        + logResult + calculation.substring(endIndex);
            }
        }

        for (String function : rtFunctions) {
            while (calculation.contains(function)) {
                int functionIndex = calculation.indexOf(function);
                int degreeStart = functionIndex - 1;
                int degreeEnd = degreeStart + 1;
                int rootLeft = functionIndex + function.length();
                int rootRight = rootLeft;

                while (rootRight < calculation.length()
                        && (Character.isDigit(calculation.charAt(rootRight))
                        || calculation.charAt(rootRight) == '.')) {
                    rootRight++;
                }

                if (!Character.isDigit(calculation.charAt(rootRight - 1))
                        || calculation.charAt(rootLeft) == '-' || degreeStart < 0
                        || !Character.isDigit(calculation.charAt(degreeEnd - 1)))
                    return "rtErr";

                while (degreeStart < calculation.length() && (degreeStart != 0
                        && Character.isDigit(calculation.charAt(degreeStart - 1)))
                        && (Character.isDigit(calculation.charAt(degreeStart))
                        || calculation.charAt(degreeStart) == '.')) {
                    degreeStart--;
                }

                double degree = Double.parseDouble(calculation.substring(degreeStart, degreeEnd));
                double value = Double.parseDouble(calculation.substring(rootLeft, rootRight));
                double rtResult = Math.pow(value, 1/degree);

                calculation = calculation.substring(0, degreeStart)
                        + rtResult + calculation.substring(rootRight);
            }
        }

        for (int index = 0; index < calculation.length(); index++) {
            char character = calculation.charAt(index);

            if (letters.indexOf(character) != -1)
                return "improperEntry";
        }

        return calculation;
    }

    public static String calculateModulus(String calculation) {
        while (calculation.contains("%")) {
            int ModuloIndex = calculation.indexOf("%");
            int leftStart = ModuloIndex - 1;
            int rightEnd = ModuloIndex + 1;

            if (leftStart < 0 || !Character.isDigit(calculation.charAt(leftStart)) ||
                    !Character.isDigit(calculation.charAt(rightEnd)))
                return "ModuloErr";

            while (leftStart > 0 && (Character.isDigit(calculation.charAt(leftStart - 1)) ||
                    calculation.charAt(leftStart - 1) == '.')) {
                leftStart--;
            }

            while (rightEnd < calculation.length()
                    && (Character.isDigit(calculation.charAt(rightEnd))
                    || calculation.charAt(rightEnd) == '.')) {
                rightEnd++;
            }

            double LHS = Double.parseDouble(calculation.substring(leftStart, ModuloIndex));
            double RHS = Double.parseDouble(calculation.substring(ModuloIndex + 1, rightEnd));

            double ModuloResult = LHS % RHS;

            calculation = calculation.substring(0, leftStart)
                    + ModuloResult + calculation.substring(rightEnd);
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

            while (rightEnd < calculation.length()
                    && (Character.isDigit(calculation.charAt(rightEnd))
                    || calculation.charAt(rightEnd) == '.')) {
                rightEnd++;
            }

            double base = Double.parseDouble(calculation.substring(leftStart, powerIndex));
            double exponent = Double.parseDouble(calculation.substring(powerIndex + 1, rightEnd));

            double powerResult = Math.pow(base, exponent);

            calculation = calculation.substring(0, leftStart)
                    + powerResult + calculation.substring(rightEnd);
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

        for (int multiplicationIndex = 0;
             multiplicationIndex < calculation.length(); multiplicationIndex++) {
            char chosenChar = calculation.charAt(multiplicationIndex);

            if (chosenChar == '*'){
                int multiplicationLeft = multiplicationIndex - 1;
                int multiplicationRight = multiplicationIndex + 2;

                while (multiplicationLeft > 0
                        && (Character.isDigit(calculation.charAt(multiplicationLeft))
                        || calculation.charAt(multiplicationLeft) == '.')) {
                    multiplicationLeft--;
                }

                if (!Character.isDigit(calculation.charAt(multiplicationLeft)))
                    multiplicationLeft++;

                while (multiplicationRight < calculation.length()
                        && (Character.isDigit(calculation.charAt(multiplicationRight - 1))
                        || calculation.charAt(multiplicationRight - 1) == '.')
                        || calculation.charAt(multiplicationRight - 1) == '-') {
                    multiplicationRight++;
                }

                while (!Character.isDigit(calculation.charAt(multiplicationRight-1))){
                    multiplicationRight--;
                }

                if (multiplicationLeft < 0
                        || !Character.isDigit(calculation.charAt(multiplicationLeft))
                        || !Character.isDigit(calculation.charAt(multiplicationIndex)))
                    if (calculation.charAt(multiplicationIndex) == '-')
                        if (!Character.isDigit(calculation.charAt(multiplicationIndex + 1)))
                            return "multiplicationErr";

                double leftHandSide = Double.parseDouble(calculation.substring
                        (multiplicationLeft, multiplicationIndex));

                double rightHandSide = Double.parseDouble(calculation.substring
                        (multiplicationIndex + 1, multiplicationRight));

                double multiplicationResult = leftHandSide * rightHandSide;

                calculation = calculation.substring
                        (0, multiplicationLeft) + multiplicationResult
                        + calculation.substring(multiplicationRight);
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

                return Double.toString(Double.parseDouble
                        (divisionLeft)/Double.parseDouble(divisionRight));
            }
            else if (operator(chosenChar)) {
                String resultString = applyOperation(result, currentNumber, lastOperator);
                if (errorFinder(resultString) == null){
                    result = Double.parseDouble
                            (applyOperation(result, currentNumber, lastOperator));
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
