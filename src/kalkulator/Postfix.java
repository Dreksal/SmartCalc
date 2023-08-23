package kalkulator;

import java.util.*;

public class Postfix {

    //algorithm to convert infix notation to the postfix. This static method returns a String List,
    //where components are arranged without parentheses and in the correct position to start calculations.
    static List<String> infixToPostfixConverter(List<String> listOfComponents, HashMap<String, String> kontener) {            //jesli zajdzie jakis blad, bedzie ta metoda zwracala pusta liste
        List<String> postfixList = new ArrayList<>();
        Deque<String> operatorStack = new ArrayDeque<>();

        for(int i = 0; i < listOfComponents.size(); i++) {
            String component = listOfComponents.get(i);

            if(!operatorChecker(component)) {
                postfixList.add(getVariables(kontener, component));
            } else {
                if(operatorStack.isEmpty() || precedence(component) == -5 || precedence(component) > precedence(operatorStack.getLast())) {
                    operatorStack.addLast(component);
                } else if(precedence(component) == -15) {
                     while(precedence(operatorStack.getLast()) != -5) {
                         postfixList.add(operatorStack.pollLast());
                     }
                     operatorStack.removeLast();
                } else if (precedence(component) == 30 && precedence(component) == precedence(operatorStack.getLast())) {
                    postfixList.add(component);
                } else {
                    while(!operatorStack.isEmpty() && precedence(component) <= precedence(operatorStack.getLast())) {
                        postfixList.add(operatorStack.pollLast());
                    }
                    operatorStack.addLast(component);
                }
            }
        }

        int operatorsToPoll = operatorStack.size();
        while(operatorsToPoll != 0) {
            postfixList.add(operatorStack.pollLast());
            operatorsToPoll--;
        }


        return postfixList;
    }

    //return true if the String has operators and false if there are no operators
    static boolean operatorChecker(String str) {
        switch (str) {
            case "+", "-", "*", "/", "^", "(", ")" :
                return true;
        }
        return false;
    }

    //if provided hashMap has a specific key, returns its value
    static String getVariables(HashMap<String, String> kontener, String key) {
        if(kontener.containsKey(key)) {
            return kontener.get(key);
        }
        return key;
    }

    //it returns a specific value of the provided operator,
    //to check their precedence in the Postfix notation expression
    static int precedence(String operator) {   //returning value of operators, to check theirs precedence
        switch (operator) {
            case "+", "-":
                return 10;
            case "*", "/":
                return 20;
            case "^":
                return 30;
            case ")":
                return -15;
            case "(":
                return -5;
        }
        return 0;
    }

}
