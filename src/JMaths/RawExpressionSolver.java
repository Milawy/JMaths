package JMaths;

import java.util.*;
import java.text.*;
import java.math.*;

// Stolen, then modified to fit the project

/****************************************
 * @Author: Mark Alexander Edwards Jr.
 *
 * Utility class for solving equations.
 ****************************************/

public final class RawExpressionSolver {	// Utility classes don't need extensions!
    private RawExpressionSolver(){}	// Utility classes don't need to be instantiated!

    // Constants
    private static final Character POW = '^';
    private static final Character MUL = '*';
    private static final Character DIV = '/';
    private static final Character MOD = '%';
    private static final Character ADD = '+';
    private static final Character[] firstSet = {POW}, secondSet = {MUL, DIV, MOD}, thirdSet = {ADD};
    private static final DecimalFormat DF = new DecimalFormat();
    private static final StringBuffer SB = new StringBuffer();
    private static final MathContext MC = new MathContext(40);
    private enum Direction {
        L_TO_R,
        R_TO_L
    }

    /**
     * Performs an operation on the two numbers specified
     */

    private static String doOperation(String lhs, char operator, String rhs){
        BigDecimal bdLhs = new BigDecimal(lhs);
        BigDecimal bdRhs = new BigDecimal(rhs);
        switch(operator){
            case '^':
                return "" + Math.pow( bdLhs.doubleValue(), bdRhs.doubleValue() );
            case '*':
                return "" + bdLhs.multiply(bdRhs).toString();
            case '/':
                return "" + bdLhs.divide(bdRhs, MC).toString();
            case '+':
                return "" + bdLhs.add(bdRhs).toString();
            case '%':
                return "" + bdLhs.remainder(bdRhs).toString();
        }
        return "";
    }

    /**
     * Returns a corrected version of the String, which is one that
     * has its first and last parenthesis removed.
     */

    private static String correctedString(String arg){
        StringBuilder sb = new StringBuilder(); // A Mutable String
        boolean foundFirst = false;
        for(int i = 0; i < arg.length(); i++)
            if(!foundFirst && arg.charAt(i) == '(')
                foundFirst = true;
            else sb.append(arg.charAt(i));
        arg = String.valueOf(sb.reverse());
        sb.delete(0, sb.length());
        foundFirst = false;
        for(int i = 0; i < arg.length(); i++)
            if(!foundFirst && arg.charAt(i) == ')')
                foundFirst = true;
            else sb.append(arg.charAt(i));
        return arg = String.valueOf(sb.reverse());
    }
    /**
     * Provides a means of halting a process for a specified amount of time
     * without directly using the static Thread method sleep.
     *
     * Used for debugging purposes
     */
    private static void sleep(int mill){
        long t1 = System.currentTimeMillis();
        long t2 = t1 + mill;
        while((t1 = System.currentTimeMillis()) < t2);
    }
    /**
     * Returns the a String that all of the characters the parameter argu
     * has, minus the space characters in the String
     */
    private static String removeSpaces(String argu){
        String temp = "";
        for(int i = 0; i < argu.length(); i++)
            if(argu.charAt(i) != ' ')
                temp += "" + argu.charAt(i);
        return temp;
    }

    /**
     * Contains an expression that exists within parenthesis
     * i.e., (5+3), (6 + 2), etc.
     *
     * If a set of paranethesis exists within the expression, it must be resolved
     * by using another expression object to solve the inner expression.
     * i.e., (5 + 3 - 2 + (8 * 7) ) would first result in an ExpressionNode consisting of
     * a call to another expression to Solve the inner expression
     *
     * Returns the result of the calculated ExpressionNode.
     * If another expression is found within this one,
     * a new ExpressionNode will be created and will solve
     * the inner expression.
     */

    private static String parse(String arg){
        String expression = removeSpaces(correctedString(arg));
        String finalExpression = "";
        boolean operatorEncountered = true;
        boolean initialValue = true;
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) == '('){
                String multiply = "";
                if(!operatorEncountered && !initialValue){
                    multiply += "*";
                }
                String placeHolder = "(";
                int valuesCounted = 1;
                operatorEncountered = false;
                for(int j = i + 1; valuesCounted != 0; j++){
                    if(expression.charAt(j) == '(')
                        valuesCounted++;
                    else if(expression.charAt(j) == ')')
                        valuesCounted--;
                    placeHolder += "" + expression.charAt(j);
                }
                String evaluatedString = parse(placeHolder);
                finalExpression += multiply + evaluatedString;
                i+= (placeHolder.length() - 1);
            }else{
                if(expression.charAt(i) == '-' && !operatorEncountered){
                    finalExpression += ((!initialValue) ? "+": "") + expression.charAt(i);
                }else if(expression.charAt(i) == '-' && operatorEncountered){
                    finalExpression += "-1*";
                }else finalExpression += expression.charAt(i);
                if((expression.charAt(i) == '+'
                        || expression.charAt(i) == '/'
                        || expression.charAt(i) == '^'
                        || expression.charAt(i) == '*'
                        || expression.charAt(i) == '%'
                        || expression.charAt(i) == '-'))
                    operatorEncountered = true;
                else if(expression.charAt(i) != ' ')
                    operatorEncountered = false;
            }
            initialValue = false;
        }
        finalExpression = removeSpaces(finalExpression);
        String perfectExpression = "";
        for(int i = 0; i < finalExpression.length(); i++){
            if((i + 1) < finalExpression.length())
                if(finalExpression.charAt(i) == '-' && finalExpression.charAt(i + 1) == '-')
                    i+=2;
            perfectExpression += "" + finalExpression.charAt(i);
        }
        finalExpression = perfectExpression;
        ArrayList<String> totalNumbers = new ArrayList<>(0);
        ArrayList<Character> totalOperations = new ArrayList<>(0);
        //System.out.println("finalExpression : " + finalExpression);
        for(int i = 0; i < finalExpression.length(); i++){
            if(finalExpression.charAt(i) >= '0' && finalExpression.charAt(i) <= '9'
                    || finalExpression.charAt(i) == '-' || finalExpression.charAt(i) == '.'
                    || finalExpression.charAt(i) == ','){
                String temp = "";
                for(int j = i; j < finalExpression.length(); j++){
                    if(finalExpression.charAt(j) >= '0' && finalExpression.charAt(j) <= '9'
                            || finalExpression.charAt(j) == '-' || finalExpression.charAt(j) == '.'
                            || finalExpression.charAt(j) == ','){
                        temp += "" + finalExpression.charAt(j);
                    }else break;
                }
                totalNumbers.add(temp);
                i += temp.length() == 0 ? 0 : (temp.length() - 1);
            }else if(finalExpression.charAt(i) == '*'
                    || finalExpression.charAt(i) == '/'
                    || finalExpression.charAt(i) == '^'
                    || finalExpression.charAt(i) == '+'
                    || finalExpression.charAt(i) == '%'
            ){
                totalOperations.add(finalExpression.charAt(i));
            }
        }
        calculate(totalNumbers, totalOperations, firstSet, Direction.R_TO_L);
        calculate(totalNumbers, totalOperations, secondSet, Direction.L_TO_R);
        calculate(totalNumbers, totalOperations, thirdSet, Direction.L_TO_R);
        return totalNumbers.get(0);
    }

    /**
     * Returns true if the target character exists in the set of Character operands, returns false otherwise.
     */

    private static boolean containsCharacter(Character anOperation, Character operands[]){
        for(Character item : operands){
            if(anOperation.equals(item)){
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to solve an equation that is separated into a set of numbers and operands.
     * (More to add)
     */

    private static void calculate(ArrayList<String> totalNumbers, ArrayList<Character> totalOperations, Character operands[], Direction dir){
        String result;
        if(dir == Direction.L_TO_R){
            for(int i = 0; i < totalOperations.size(); i++){
                if(containsCharacter(totalOperations.get(i), operands)){
                    result = doOperation(totalNumbers.get(i), /*(char)*/totalOperations.get(i), totalNumbers.get(i + 1));
                    totalNumbers.set(i, result);
                    totalOperations.remove(i);
                    totalNumbers.remove(i + 1);
                    i--;
                }
            }
        }else if(dir == Direction.R_TO_L){
            for(int i = totalOperations.size() - 1; i >= 0; i--){
                if(containsCharacter(totalOperations.get(i), operands)){
                    result = doOperation(totalNumbers.get(i), /*(char)*/totalOperations.get(i), totalNumbers.get(i + 1));
                    totalNumbers.set(i, result);
                    totalOperations.remove(i);
                    totalNumbers.remove(i + 1);
                }
            }
        }
    }

    /**
     * Checks to see if the expression is solvable or not.
     *
     * This method is actually a misnomer, because more restrictions
     * should be put in place on what a user can determine as solvable.
     */

    private static boolean isSolvable(String eq){
        int parenthesisCount = 0;	// assuming 0 parenthesis to begin with
        for(char element : eq.toCharArray()){	// for every char in the String eq
            if(element == '(')	// if the element is a left parenthesis
                parenthesisCount++;	// increment the parenthesisCount
            else if(element == ')')	// else if the element is a right parenthesis
                parenthesisCount--;	// decrement the parenthesisCount
            if(parenthesisCount < 0)	// if brackets aren't in correct order, return false
                return false;
        }
        return parenthesisCount == 0;	// return true if parenthesisCount is zero, otherwise return false
    }

    /**
     * Attempts to solve an equation
     */

    public static String solve(String eq){
        if(isSolvable(eq)){
            System.out.println("equation : " + eq);	// Prints out the equation before it is parsed
            String value = "(" + eq + ")";	// Appending parenthesis to the equation for accuracy
            return "result : " + parse(value); //	returning the final value of the expression
        }else return "";
    }

    /**
     * Attempts to solve an equation, with the precision factor taken into account.
     *
     * The maximum precision is 40, only because the max precision for the MathContext object is 40
     * though this is not required and can be changed in future versions.
     */

    public static String solve(String eq, int precision){
        SB.delete(0, SB.length());
        return DF.format( (double)Double.parseDouble(solve(eq)), SB, new FieldPosition(precision) ).toString();	// formatted answer
    }
}