import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Scanner;

public class ExpEvaluator {

    private String s;
    private int currIndex;
    private char inputToken;
    private HashMap<String, Integer> hs = new HashMap<String, Integer>();

    void ExpEvaluator(String s) {
        this.s = s.replaceAll("\\s", "");
        currIndex = 0;
        nextToken();

    }

    void nextToken() {
        char c;
        if (!s.endsWith(";"))
            throw new RuntimeException("; Token exptected");
        c = s.charAt(currIndex++);

        inputToken = c;
    }

    void match(char token) {
        if (inputToken == token) {
            nextToken();
        } else {
            throw new RuntimeException("syntax error");
        }
    }

    int eval() {
        int x = exp();
        if (inputToken == ';') {
            return x;
        } else {
            throw new RuntimeException("syntax error");
        }
    }

    public void run(Scanner fs) {
        while (fs.hasNextLine()) {
            ExpEvaluator(fs.nextLine());
            assignment();
            s = "";
        }
    }

    void assignment() {

        String var = identifier();
        int operand = eval();
        hs.put(var, operand);
        System.out.println(var + " = " + operand);

        // System.out.println(hs.toString());

    }

    int exp() {
        int x = term();
        while (inputToken == '+' || inputToken == '-') {
            char op = inputToken;
            nextToken();
            int y = term();
            x = apply(op, x, y);
        }
        return x;
    }

    int term() {
        int x = factor();
        while (inputToken == '*' || inputToken == '/') {
            char op = inputToken;
            nextToken();
            int y = factor();
            x = apply(op, x, y);
        }
        return x;
    }

    int factor() {
        int x;
        String temp = String.valueOf(inputToken);

        if (hs.containsKey(temp)) {
            x = hs.get(temp).intValue();
            nextToken();
            return x;
        }

        switch (inputToken) {
            case '(':
                nextToken();
                x = exp();
                match(')');
                return x;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                x = Integer.parseInt(Character.toString(inputToken));
                nextToken();
                return x;
            default:
                throw new RuntimeException("syntax error");
        }
    }

    String identifier() {
        StringBuilder sb = new StringBuilder();

        if (Character.isLetter(inputToken))
            sb.append(inputToken);
        else
            throw new RuntimeException("Invalid variable name");
        nextToken();

        while (Character.isLetter(inputToken) || inputToken == '_' || Character.isDigit(inputToken)) {
            sb.append(inputToken);
            nextToken();
        }
        if (inputToken != '=')
            throw new RuntimeException("Not an assignment statement");
        nextToken();
        return sb.toString();
    }

    static int apply(char op, int x, int y) {
        int z = 0;
        switch (op) {
            case '+':
                z = x + y;
                break;
            case '-':
                z = x - y;
                break;
            case '*':
                z = x * y;
                break;
            case '/':
                z = x / y;
                break;
        }
        return z;
    }

    public static void main(String[] args) {

        try {
            Scanner fReader = new Scanner(new FileInputStream(args[0]));
            ExpEvaluator ee = new ExpEvaluator();
            ee.run(fReader);

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
    }
}