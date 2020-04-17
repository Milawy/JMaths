package JMaths;

import java.util.regex.*;

public class Analyser {

    private String rawString;

    public Boolean isFunction, isVariable, isPrint;

    private static Pattern functionPattern, functionNamePattern, functionVariablePattern, functionExpressionPattern;
    private static Pattern variablePattern, variableNamePattern, variableExpressionPattern;
    private static Pattern printPattern, printNamePattern, printVarPattern;
    private static Pattern plotPattern;

    // Constructor containing all regex expressions we need
    public Analyser(){

        functionPattern = Pattern.compile("^[a-zA-Z]+\\([a-zA-Z]+\\)=[a-zA-Z0-9\\(\\).^\\/+\\-*]+");
        functionNamePattern = Pattern.compile("^[a-zA-Z]+");
        functionVariablePattern = Pattern.compile("\\([a-zA-Z]+");
        functionExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\(\\)\\/+\\-*]+");

        variablePattern = Pattern.compile("^[a-zA-Z]+=[a-zA-Z0-9.^\\/+\\-*]+");
        variableNamePattern = Pattern.compile("^[a-zA-Z]+");
        variableExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\/+\\-*]+");
        printPattern = Pattern.compile("^print\\([a-zA-Z]+,[a-zA-Z0-9\\+\\-\\*\\^]+\\)");
        printNamePattern = Pattern.compile("\\([a-zA-Z]+");
        printVarPattern = Pattern.compile(",[a-zA-Z0-9\\+\\-\\*\\^]+");
        plotPattern = Pattern.compile("^plot\\([a-zA-Z]+,[a-zA-Z0-9]+,[a-zA-Z0-9]+\\)");
    }

    // Purge useless char in String
    protected String purgeIt(String str, String toPurge){

        str = str.replace(toPurge, "");
        return str;
    }

    //The main function of Analyser
    protected Object analyse(){

        Matcher functionMatcher, functionNameMatcher, functionVariableMatcher, functionExpressionMatcher;
        Matcher variableMatcher, variableNameMatcher, variableExpressionMatcher;
        Matcher printMatcher, printNameMatcher, printVarMatcher;
        Matcher plotMatcher;

        // Load the string to the matcher
        functionMatcher = functionPattern.matcher(rawString);
        functionNameMatcher  = functionNamePattern.matcher(rawString);
        functionVariableMatcher  = functionVariablePattern.matcher(rawString);
        functionExpressionMatcher  = functionExpressionPattern.matcher(rawString);

        variableMatcher = variablePattern.matcher(rawString);
        variableNameMatcher = variableNamePattern.matcher(rawString);
        variableExpressionMatcher = variableExpressionPattern.matcher(rawString);

        printMatcher = printPattern.matcher(rawString);
        printNameMatcher = printNamePattern.matcher(rawString);
        printVarMatcher= printVarPattern.matcher(rawString);
        plotMatcher = plotPattern.matcher(rawString);

        // Reset content indicator
        this.isFunction = false;
        this.isVariable = false;
        this.isPrint = false;

        // Test if the String is a function
        if(functionMatcher.find()) {

            if(functionNameMatcher.find() && functionVariableMatcher.find() && functionExpressionMatcher.find()){

                // Update content indicator
                this.isFunction = true;

                //return function because we know that the user wrote a function
                return new Function(functionNameMatcher.group(),
                        purgeIt(functionVariableMatcher.group(), "("),
                        purgeIt(functionExpressionMatcher.group(), "="));
            }

            else{ return "Wrong function syntax" + "\n" + "Try with the following syntax :" + "\n" + "functionName(variableName)=functionExpression" + "\n"; }
        }

        else if(variableMatcher.find()) {

            if(variableNameMatcher.find() && variableExpressionMatcher.find()) {

                // Update content indicator
                this.isVariable = true;

                //return variable because we know that the user wrote a variable
                return new Variable(variableNameMatcher.group(), purgeIt(variableExpressionMatcher.group(), "="));
            }

            else{ return "Wrong variable syntax" + "\n" + "Try with the following syntax :" + "\n" + "variableName=variableExpression" + "\n"; }
        }

        else if(printMatcher.find()) {

            // Update content indicator
            this.isPrint = true;

            if(printNameMatcher.find() && printVarMatcher.find()){
                return new PrintFct(printNameMatcher.group(), printVarMatcher.group());
            }
            else{ return "Wrong print syntax" + "\n" + "Try with the following syntax :" + "\n" + "print(functionName,value)" + "\n"; }
        }

        else if(plotMatcher.find()) { return plotMatcher.group(); }

        else{ return "No matches" + "\n" + "Please define a function, a variable or use a command" + "\n"; }
    }

    // Explicit
    protected Object setRawString(String raw){

        this.rawString = raw;
        return this.analyse();
    }
}