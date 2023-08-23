package kalkulator;

public class Validation {

    //checks if there are some inappropriate characters, invalid positions of the operators,
    // an equal number of left and right parentheses
    static boolean isExpressionValid(String expression) {

        int parenthesesRightCounter = 0;
        int parenthesesLeftCounter = 0;

        if(!expression.matches("[\\w\\(\\)\\+\\-\\*\\/\\^\\=\\.]+")) {
            System.out.println("ERROR!\nYou can write only letters, digits and operands of:");
            System.out.println("-addition: +\n-subtraction: -\n-multiplication: *\n-division: /\n-power: ^\n-parentheses: ( and )");
            return false;
        }

        boolean operatorParenthesesRight = false;
        boolean operatorLeftParentheses = false;
        boolean operatorMinus = false;
        boolean otherOperators = false;

        for(int j = 0; j < expression.length(); j++) {

            switch(expression.charAt(j)) {
                case '(':
                    parenthesesRightCounter++;
                    if(operatorLeftParentheses) {
                        System.out.println("ERROR! Invalid expression. Wrong parentheses position.");
                        return false;
                    }
                    operatorParenthesesRight = true;
                    operatorLeftParentheses = false;
                    operatorMinus = false;
                    otherOperators = false;
                    break;
                case ')':
                    parenthesesLeftCounter++;
                    operatorLeftParentheses = true;
                    if(parenthesesLeftCounter > parenthesesRightCounter || operatorParenthesesRight) {
                        System.out.println("ERROR! Invalid expression.\nYou have to use previously \"(\" and then \")\"");
                        return false;
                    }
                    operatorLeftParentheses = true;
                    operatorParenthesesRight = false;
                    operatorMinus = false;
                    otherOperators = false;
                    break;
                case '+', '*', '/', '^':
                    if(otherOperators || j == 0 || j == expression.length() - 1 || operatorMinus || operatorParenthesesRight) {
                        System.out.println("ERROR! Invalid expression. Incorrect use of operators.");
                        return false;
                    }
                    otherOperators = true;
                    operatorParenthesesRight = false;
                    operatorLeftParentheses = false;
                    break;
                case '-':
                    if(j == expression.length() - 1 || otherOperators) {
                        System.out.println("ERROR! Invalid expression3");
                        return false;
                    }
                    operatorMinus = true;
                    operatorParenthesesRight = false;
                    operatorLeftParentheses = false;
                    break;
                default:
                    if(operatorLeftParentheses) {
                        System.out.println("ERROR! Invalid expression4");
                        return false;
                    }
                    operatorParenthesesRight = false;
                    operatorLeftParentheses = false;
                    operatorMinus = false;
                    otherOperators = false;
            }
        }

        if(parenthesesLeftCounter != parenthesesRightCounter) {                                   // if parenthesis left and right don't have the same number of occurrences outputs
            System.out.println("Invalid expression (Not equal number of parenthesis!)");          // the warning will be shown
            return false;
        }

        if(!expComponentsChecker(expression, false)) {
            System.out.println("CAUTION! Wrong type of data! In expression to calculate you can put only digit values with operands and variables name!");
            return false;
        }

        return true;
    }

    //take string and parameter that tells if this string is a variable name to check (true)
    // or expression components to check (false). If there are characters that are not allowed
    // or numbers with letters, it returns false, if everything is ok it returns true
    static boolean expComponentsChecker(String str, boolean isVariableNameToCheck) {
        boolean isNumber = false;
        boolean isAlphabetic = false;
        char ch;

        for(int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);

            if(!isVariableNameToCheck && isOperator(String.valueOf(ch))) {
                isNumber = false;
                isAlphabetic = false;
            }
            if(isVariableNameToCheck && ch == '_') {
                continue;
            }
            if(!isNumber && ch == '.') {
                return false;
            }

            if(Character.isDigit(ch)) {
                if(isAlphabetic) {
                    continue;
                } else {
                    isNumber = true;
                }
            } else if(Character.isLetter(ch)) {
                if(isNumber) {
                    return false;
                } else {
                    isAlphabetic = true;
                }
            }
        }

        if(isVariableNameToCheck && isNumber) {
            return false;
        }
        return true;
    }

    static boolean isOperator(String ch) {           //checks if given string is an operator
        switch (ch) {
            case "+", "-", "*", "/", "^", "(", ")":
                return true;
        }
        return false;
    }

    //checks if the provided string has “=” character
    static boolean isAssignment(String toCheck) {
        return toCheck.contains("=");
    }

}
