package calculator;

import java.math.BigInteger;
import java.util.*;


public class CalculatorRPN {

    protected static BigInteger calcRPN(Queue<String> expr) throws InvalidExpression {
        Stack<BigInteger> stack = new Stack<>();
        for (String s: expr) {
            BigInteger a, b;
            try {
                a = new BigInteger(s);
                stack.push(a);
                continue;
            } catch (Exception e) {

            }

            try {
                b = stack.pop();
                a = stack.pop();
            } catch (Exception e) {
                throw new InvalidExpression();
            }

            switch(s) {
                case "+":
                    stack.push(a.add(b));
                    break;
                case "-":
                    stack.push(a.subtract(b));
                    break;
                case "*":
                    stack.push(a.multiply(b));
                    break;
                case "/":
                    stack.push(a.divide(b));
                    break;
            }
        }
        return stack.pop();
    }

}

