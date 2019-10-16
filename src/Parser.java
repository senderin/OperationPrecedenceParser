import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public enum Operator {
        MULTIPLICATION,
        ADDITION ,
        NONE
    }

    public enum PrecedenceOrder {
        Greater,
        Lower,
        Equal
    }

    public Stack<String> operatorStack;
    public String input;
    public Queue<String> outputQueue;

    private List<Character> inputTokens;
    private List<String> variables;

    public boolean Parse(String expression) {
        // remove whitespaces
        expression.replaceAll("\\s+", "");

        if(!CheckInputValidity(expression))
            return false;

        input = expression;

        // tokenize expression
        TokenizeInput();

        // initialize data structures
        operatorStack = new Stack<>();
        outputQueue = new LinkedList<>();
        variables = new ArrayList<>();

        // start to shunting-yard algorithm
        for (Character character : inputTokens) {
            if(Character.isLetter(character)) {
                // add it to queue
                outputQueue.add(character.toString());
                // add it to variable list
                if(!variables.contains(character.toString()))
                    variables.add(character.toString());
            }
            else if(IsOperator(character)) {
                while(operatorStack.size() > 0 && ComparePrecedences(character) == PrecedenceOrder.Greater)
                    outputQueue.add(operatorStack.pop());
                operatorStack.push(character.toString());
            }
            else if(IsParanthesis(character)) {
                if(character == '(')
                    operatorStack.push(character.toString());
                if(character == ')') {
                    while(!operatorStack.empty() && operatorStack.peek().compareTo("(") != 0) {
                        outputQueue.add(operatorStack.pop());
                    }
                    if(!operatorStack.isEmpty())
                        operatorStack.pop(); // '(' is removed from stack
                    else
                        return false;
                }
            }
            else {
                return false;
            }
        }

        while(operatorStack.size() > 0) {
            outputQueue.add(operatorStack.pop());
        }

        if(outputQueue.contains("("))
            return  false;

        if(outputQueue.isEmpty())
            return false;

        return true;
    }


    public List<String> GetVariables() {
        return variables;
    }

    public int GetResult(Map<String, Integer> variableValuePairs) {
        List<Integer> operands = new ArrayList<Integer>();
        List<Operator> operators = new ArrayList<>();

        boolean isLastTokenOperator = false;
        int result = 0;

        for (String token: outputQueue) {
            if(Character.isLetter(token.charAt(0))) {
                if(isLastTokenOperator)
                    operands.add(InnerCalculation(operands, operators));
                operands.add(variableValuePairs.get(token));
                isLastTokenOperator = false;
            }
            else {
                isLastTokenOperator = true;
                operators.add(GetOperatorType(token));
            }
        }

        result = InnerCalculation(operands, operators);


        return result;
    }

    private int InnerCalculation(List<Integer> operands, List<Operator> operators) {
        int result = 0;

        Collections.reverse(operands);

        if(operands.size() > 1) {
            result = Calculate(operands.remove(0), operands.remove(0), operators.remove(0));
            while(!operators.isEmpty() && !operands.isEmpty())
                result = Calculate(result, operands.remove(0), operators.remove(0));
        }

        return result;
    }

    private int Calculate(int leftOperand, int rightOperand, Operator operator) {
        if(operator == Operator.MULTIPLICATION)
            return leftOperand*rightOperand;
        else if(operator == Operator.ADDITION)
            return leftOperand+rightOperand;
        return 0;
    }

    private void TokenizeInput() {
        inputTokens = new ArrayList<Character>();
        for (char element : input.toCharArray())
            inputTokens.add(element);
    }

    private boolean IsOperator(Character character) {
        if(character == '+' || character == '*')
            return  true;
        return false;
    }

    private boolean IsParanthesis(Character character) {
        if(character == '(' || character == ')')
            return  true;
        return false;
    }

    private PrecedenceOrder ComparePrecedences(Character character) {
        Operator operator = GetOperatorType(character.toString());
        Operator topOperator = GetOperatorType(operatorStack.peek());

        if(operator == Operator.MULTIPLICATION && topOperator == Operator.ADDITION)
            return  PrecedenceOrder.Lower;
        else if(operator == Operator.ADDITION && topOperator == Operator.MULTIPLICATION)
            return PrecedenceOrder.Greater;

        return PrecedenceOrder.Equal;
    }

    private Operator GetOperatorType(String character) {
        if(character.compareTo("+") == 0)
            return Operator.ADDITION;
        else if(character.compareTo("*") == 0)
            return Operator.MULTIPLICATION;
        else
            return Operator.NONE;
    }

    private boolean CheckInputValidity(String expression) {
        boolean isValid = true;

        if(expression.isEmpty())
            isValid = false;

        Pattern pattern = Pattern.compile("([a-zA-Z]{2,})|([\\(][+*])|([+*][\\)])|([+*]{2,})|^([+*])|([+*\\(])$|([\\(][\\)])|([\\)][\\(])|([\\(][a-zA-Z][\\)])|([+*]$)");//. represents single character
        Matcher matcher = pattern.matcher(expression);
        boolean isMatched = matcher.matches();

        if(isMatched)
            isValid = false;

        return isValid;
    }

}
