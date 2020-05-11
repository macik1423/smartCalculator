package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Map<String, String> varNameValues = new HashMap<>();

    public static void main(String[] args) throws UnknownVariableException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            String validAssignmentExpression = "\\w+ *= *[+-]*\\w+";
            Pattern pattern = Pattern.compile(validAssignmentExpression);
            Matcher matcherAssignmentExpression = pattern.matcher(command);

            String validExpression = "[a-zA-Z0-9()]+.*[+-]*.*[a-zA-Z0-9()]*";
            String unknownCommand = "/\\w+";
            if ("/exit".equals(command)) {
                break;
            } else if (command.matches(unknownCommand)) {
                System.out.println("Unknown command");
            } else if (matcherAssignmentExpression.find()) {
                try {
                    extractDigitsWithVarName(command);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (command.matches(validExpression)) {
                compute(command);
            } else if (command.matches("\n*")) {
            } else if (command.matches("-?\\d+")) {
                System.out.println(command);
            }
            else {
                System.out.println("Invalid expression");
            }
        }
        System.out.println("Bye!");
    }

    private static void compute(String command) {
        try {
            command = substituteVar(command);
            ConvertInfixToRPN converter = new ConvertInfixToRPN();
            Queue<String> commandToRPN = converter.convertInfixToRPN(command);
            System.out.println(CalculatorRPN.calcRPN(commandToRPN));
        } catch (Exception e) {
            System.out.println("Invalid expression");
        }
    }

    private static String substituteVar(String command) {
        Pattern patternVar = Pattern.compile("[a-zA-Z]+");
        Matcher matcherVar = patternVar.matcher(command);
        while(matcherVar.find()) {
            String variable = matcherVar.group();
            command = command.replace(variable, String.valueOf(varNameValues.get(variable)));
        }
        return command;
    }

    private static void extractDigitsWithVarName(String expression) throws InvalidIdentifierException, InvalidAssignmentException, UnknownVariableException {
        String[] assignmentExpression = expression.split("=");
        String varName = assignmentExpression[0].trim();
        String value = assignmentExpression[1].trim();
        if (varName.matches("[a-zA-Z]+")) {
            if (value.matches("[0-9]+") && assignmentExpression.length == 2) {
                varNameValues.put(varName, value);
            } else if (varNameValues.containsKey(value) && assignmentExpression.length == 2) {
                varNameValues.put(varName, varNameValues.get(value));
            } else {
                throw new InvalidAssignmentException();
            }
        } else {
            throw new InvalidIdentifierException();
        }
    }

}
