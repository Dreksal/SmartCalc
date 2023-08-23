package kalkulator;

import java.util.*;

public class Compute {

    private List<String> history = new ArrayList<>();                     //field to storing every input from the user
    private String userInput;                                             //stores actual User input data
    private List<String> components;                                      //stores in List components of the expression to calculate
    private HashMap<String, String> container = new HashMap<>();          //field to storing variables and their values

    //the executable method to run the program, it uses Scanner object to scan input from user and check if
    // there are commands in the input like “/help” (to show more information about the program,
    // “/exit” (to terminate the app), “/rules” (to see more about variable naming convention), etc. or
    // if there are only some calculations or assignment values to variables
    public void execute() {
        welcomePrinter();

        Scanner sc = new Scanner(System.in);
        setUserInput(sc.nextLine());

        while(!getUserInput().equals("/exit")) {
            if(getUserInput().equals("/help")) {
                helpCommand();
            } else if(getUserInput().equals("/rules")) {
                rulesCommand();
            } else if(getUserInput().equals("") || getUserInput() == null) {
                System.out.println("Nothing to calculate. Type \"/help\" to see more information about this app. ");
            } else if(getUserInput().equals("/history")) {
                historyViewer();
            } else if(getUserInput().equals("/variables")) {
                variablesViewer();
            } else if(Validation.isAssignment(getUserInput())) {
                assignment();
            } else {
                System.out.println(exeCalculations(getUserInput()));
            }
            setUserInput(sc.nextLine());
        }
    }

    //method to start checking if expressions from the user are valid and if they are,
    // to start making some calculations and return result
    private String exeCalculations(String expressionToCalculate) {
        if(Validation.isExpressionValid(expressionToCalculate)) {
            setComponents(expressionToCalculate);
            if(!isInContainer()) {
                System.out.println("Container doesn't have this variable. Try again.");
            } else {
                components = Postfix.infixToPostfixConverter(components, container);
                return calculating(components);
            }
        } else {
            System.out.println("Wrong expression. Try again: ");
        }
        return "";
    }

    //method to show previous users’ inputs.
    private void historyViewer() {
        if(history.isEmpty()) {
            System.out.println("Nothing to print out!");
            return;
        }
        for(int i = 1; i < history.size(); i++) {
            System.out.printf("%d) %s.%n", i, history.get(i));
        }
    }

    //this method is used for showing variables from the container
    private void variablesViewer() {
        if(container.isEmpty()) {
            System.out.println("There is no variables here :(");
        } else {
            for (Map.Entry<String, String> entry: container.entrySet()) {
                System.out.printf("[%s = %s]\n", entry.getKey(), entry.getValue());
            }
        }
    }

    //method to call StringOrganizer() method and set returned string from that method to the userInput field
    private void setUserInput(String string) {
        this.userInput = stringOrganizer(string);
        history.add(this.userInput);
    }

    public String getUserInput() {
        return this.userInput;
    }

    private void putToContainer(String key, String value) {
            this.container.put(key, value);
    }

    //if the expression from the user is valid, this method divides the string into single components,
    // like numbers, operators, name of a variable and saves them in the List field of that Class
    void setComponents(String str) {
        StringBuilder buffer = new StringBuilder();
        components = new ArrayList<>();
        String ch;

        for(int i = 0; i < str.length(); i++) {
            ch = String.valueOf(str.charAt(i));

            if(buffer.isEmpty() && ch.equals("-") ) {
                buffer.append(ch);
            } else if(Validation.isOperator(ch)) {
                if(!buffer.isEmpty()) {
                    components.add(buffer.toString());
                }
                components.add(ch);
                buffer = new StringBuilder();
            } else {
                buffer.append(ch);
            }
        }

        if(!buffer.isEmpty()) {
            components.add(buffer.toString());
        }
    }

    //it takes a String from the user and deletes from its spaces, multiple operators (like —-- or +++++)
    private String stringOrganizer(String strToClean) {
        String stringNoSpaces = new String(strToClean.replaceAll("\\s+", ""));
        StringBuilder stringToReturn = new StringBuilder();
        String operator = new String("");

        for (int i = 0; i < stringNoSpaces.length(); i++) {
            if (stringNoSpaces.charAt(i) == '+' || stringNoSpaces.charAt(i) == '-' ||
                    stringNoSpaces.charAt(i) == '/' || stringNoSpaces.charAt(i) == '*' ||
                    stringNoSpaces.charAt(i) == '^' || stringNoSpaces.charAt(i) == '=') {

                operator += stringNoSpaces.charAt(i);
            } else {
                if(!operator.equals("")) {
                    stringToReturn.append(operators(operator));
                    stringToReturn.append(stringNoSpaces.charAt(i));
                    operator = new String("");
                } else {
                    stringToReturn.append(stringNoSpaces.charAt(i));
                }
            }
        }
        if(!operator.equals("")) {
            stringToReturn.append(operators(operator));
        }
        return String.valueOf(stringToReturn);
    }

    //manipulate UserInput field, splitting this string into two parts by “=” character, a
    // nd check if something is in the other part of that array. Then call the method to
    // check that the variable name is correct, and if it is true, put that variable to Container
    private void assignment() {                                                                   // method to assign given variable to their given value
        String[] statementsOfAssign = userInput.split("\\=", 2);                      //splitting String to the array by "=" character


        if(statementsOfAssign[1].equals("")) {
            System.out.println("Invalid expression!\nTry again: ");
            return;
        }

        if (Validation.expComponentsChecker(statementsOfAssign[0], true)) {
            putToContainer(statementsOfAssign[0], exeCalculations(statementsOfAssign[1]));
            System.out.println("Done!");
        } else {
            System.out.println("ERROR! If you want to assign value to variable, you must follow the rules of naming a variable.");
            System.out.println("Write \"/rules\" to learn more about variable naming rules.\n");
        }
    }

    // is used for taking a String of multiple operators and changing them to a single operator
    private static String operators(String s) {
        StringBuilder operatorString = new StringBuilder();
        char buffer = 0;

        for(int i = 0; i < s.length(); i++) {
            buffer = s.charAt(i);
            if(i == 0) {
                operatorString.append(buffer);
            } else if(operatorString.charAt(operatorString.length() - 1) == buffer) {
                continue;
            } else {
                operatorString.append(buffer);
            }
        }
        return String.valueOf(operatorString);
    }

    private boolean isInContainer() {

        for(int i = 0; i < this.components.size(); i++) {
            if(components.get(i).matches("[a-zA-Z]+\\w*")) {
                if(!container.containsKey(components.get(i))) {
                    System.out.println("ERROR! Given variable not found!");
                    return false;
                }
            }
        }

        return true;
    }

    //takes a List of Strings that are arranged in Postfix notation rules and makes calculations using stack
    //(and calling computing() method). In the end, returns the result of the calculations as a String value
    private static String calculating(List<String> elementsPostfix) {
        Deque<String> stackOfElements = new ArrayDeque<>();
        double result = 0;

        if(elementsPostfix.size() == 1) {
            return elementsPostfix.get(0);
        }

        for(int i = 0; i < elementsPostfix.size(); i++) {
            String element = elementsPostfix.get(i);

            if(!Postfix.operatorChecker(element)) {
                stackOfElements.addLast(element);
            } else {
                result = computing(stackOfElements.pollLast(), stackOfElements.pollLast(), element);
                stackOfElements.addLast(String.valueOf(result));
            }
        }
        return stackOfElements.getLast();
    }

    // it tries to parse String parameters to the double values and returns
    // the computing value of that two arguments and the operator
    private static double computing(String secondArgument, String firstArgument, String oper) {
        double fArg;
        double sArg;
        try {
            fArg = Double.parseDouble(firstArgument);
            sArg = Double.parseDouble(secondArgument);
        } catch(NumberFormatException exception) {
            System.out.println("Something went wrong. Make sure that you are typing valid expression.");
            return 0;
        }

        switch(oper) {
            case "+":
                return fArg + sArg;
            case "-":
                return fArg - sArg;
            case "*":
                return fArg * sArg;
            case "/":
                if(sArg == 0) {
                    System.out.println("Error! Divison by 0.");
                    return 0;
                }
                return fArg / sArg;
            case "^":
                return Math.pow(fArg, sArg);
            default:
                System.out.println("Something went wrong");
                return 0;
        }
    }

    //is used to create a Compute object
    public static Compute getComputeInstance(){
        return new Compute();
    }

    //prints info about app
    private static void helpCommand() {
        System.out.println("The program calculates the subtraction, addition, multiplication, division and power of numbers.");
        System.out.println("If you want to calculate some expression, just type it correctly (you can use parenthesis).");
        System.out.println("To write a fraction, use the division operator, to calculate power of number, use the \"^\" operator (e.g. 2^3 means two to the third power).");
        System.out.println("You can assign value to variable and save it. The naming rules are on the \"/rules\" command.");
        System.out.println("If you want to see your previous inputs, type \"/history\"");
        System.out.println("If you want to see variables that you saved in this app, type \"/variables\"");
    }

    //print info about naming variables rules
    private static void rulesCommand() {
        System.out.println("In this app there are some rules about naming a variable:");
        System.out.println("1) If you want to assign value to variable, type your variable name and next use \"=\" operator and your value to assign it.");
        System.out.println("2) Name of the variable have to start with letter, next you can use letters or digits or both");
        System.out.println("3) Variable name can't have other characters than letters and digits!");
        System.out.println("4) Variable's names are case-sensitive (a != A etc.)");
        System.out.println("5) The Java rules for variable identifiers are the same in this application.");
    }

    private static void welcomePrinter() {
        System.out.println("This program can perform some calculations and assign value to your variable.\nTry it yourself! :)");
        System.out.println("© 2023 Dariusz Jaskiewicz. All rights reserved.\n");
    }
}

