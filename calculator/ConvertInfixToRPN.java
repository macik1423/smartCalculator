package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertInfixToRPN {
    static boolean isNumber(String str) {
        try{
            Double.valueOf(str);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    Queue<String> convertInfixToRPN(String infixNotation) throws InvalidExpression {
        List<String> dataRPN_Notation = extractToRPN_Form(infixNotation);
        Map<String, Integer> prededence = new HashMap<>();
        prededence.put("/", 5);
        prededence.put("*", 5);
        prededence.put("+", 4);
        prededence.put("-", 4);
        prededence.put("(", 0);

        Queue<String> Q = new LinkedList<>();
        Stack<String> S = new Stack<>();

        for (String token : dataRPN_Notation) {
            if ("(".equals(token)) {
                S.push(token);
                continue;
            }

            if (")".equals(token)) {
                while (!"(".equals(S.peek())) {
                    Q.add(S.pop());
                }
                S.pop();
                continue;
            }
            // an operator
            if (prededence.containsKey(token)) {
                while (!S.empty() && prededence.get(token) <= prededence.get(S.peek())) {
                    Q.add(S.pop());
                }
                S.push(token);
                continue;
            }

            if (isNumber(token)) {
                Q.add(token);
                continue;
            }

            throw new InvalidExpression();
        }
        // at the end, pop all the elements in S to Q
        while (!S.isEmpty()) {
            Q.add(S.pop());
        }

        return Q;
    }

    private static List<String> extractToRPN_Form(String expr) {
        String regex = "([\\d\\.]+)|([+-\\/\\*])+|(\\()|(\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expr);
        List<String> result = new ArrayList<>();
        while(matcher.find()) {
            if (matcher.group().contains("-")) {
                if (matcher.group().length() % 2 == 0) {
                    result.add("+");
                } else {
                    result.add("-");
                }
            } else if (matcher.group().contains("+")) {
                result.add("+");
            }else {
                result.add(matcher.group());
            }
        }
        return result;
    }
}
