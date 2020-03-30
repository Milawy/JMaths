package JMaths;

import java.util.regex.*;

public class Analyser {

    private String rawString;

    public Boolean isFunction;
    public Boolean isVariable;

    private static Pattern functionPattern;
    private static Pattern functionNamePattern;
    private static Pattern functionVariablePattern;
    private static Pattern functionExpressionPattern;
    private static Pattern variablePattern;
    private static Pattern variableNamePattern, variableExpressionPattern;
    private static Pattern printPattern, plotPattern;

    public Analyser(){

        // All regex expressions we need

        functionPattern = Pattern.compile("^[a-zA-Z]+\\([a-zA-Z]+\\)=[a-zA-Z0-9.^\\/+\\-*]+");
        functionNamePattern = Pattern.compile("^[a-zA-Z]+");
        functionVariablePattern = Pattern.compile("\\([a-zA-Z]+");
        functionExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\/+\\-*]+");

        variablePattern = Pattern.compile("^[a-zA-Z]+=[a-zA-Z0-9.^\\/+\\-*]+");
        variableNamePattern = Pattern.compile("^[a-zA-Z]+");
        variableExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\/+\\-*]+");
        printPattern = Pattern.compile("^print\\([a-zA-Z]+,[a-zA-Z0-9]+\\)");
        plotPattern = Pattern.compile("^plot\\([a-zA-Z]+,[a-zA-Z0-9]+,[a-zA-Z0-9]+\\)");

    }

    protected String purgeIt(String str, String toPurge){

        str = str.replace(toPurge, "");
        return str;

    }

    protected Object analyse(){

        Matcher functionMatcher;
        Matcher functionNameMatcher;
        Matcher functionVariableMatcher;
        Matcher functionExpressionMatcher;
        Matcher variableMatcher;
        Matcher variableNameMatcher;
        Matcher variableExpressionMatcher;
        Matcher printMatcher, plotMatcher;

        // Clear all attributes except rawString

        // Load the string to the matcher
        functionMatcher = functionPattern.matcher(rawString);
        functionNameMatcher  = functionNamePattern.matcher(rawString);
        functionVariableMatcher  = functionVariablePattern.matcher(rawString);
        functionExpressionMatcher  = functionExpressionPattern.matcher(rawString);

        variableMatcher = variablePattern.matcher(rawString);
        variableNameMatcher = variableNamePattern.matcher(rawString);
        variableExpressionMatcher = variableExpressionPattern.matcher(rawString);
        printMatcher = printPattern.matcher(rawString);
        plotMatcher = plotPattern.matcher(rawString);

        // Reset content indicator
        this.isFunction = false;
        this.isVariable = false;

        // Test if the String is a function
        if(functionMatcher.find()) {

            if(functionNameMatcher.find() && functionVariableMatcher.find() && functionExpressionMatcher.find()){

                // Update content indicator
                this.isFunction = true;

                Function function = new Function(functionNameMatcher.group(),
                        purgeIt(functionVariableMatcher.group(), "("),
                        purgeIt(functionExpressionMatcher.group(), "="));

                System.out.println(functionNameMatcher.group() + purgeIt(functionVariableMatcher.group(),"(") + purgeIt(functionExpressionMatcher.group(),"="));
                return function;
            }

            else{ return "Wrong function"; }
        }

        else if(variableMatcher.find()) {

            if(variableNameMatcher.find() && variableExpressionMatcher.find()) {

                // Update content indicator
                this.isVariable = true;

                Variable variable = new Variable(variableNameMatcher.group(),
                        Double.parseDouble(purgeIt(variableExpressionMatcher.group(), "=")));
                System.out.println(variableNameMatcher.group() + purgeIt(variableExpressionMatcher.group(),"="));
                return variable;
            }

            else{ return "Wrong variable"; }
        }

        else if(printMatcher.find()) { return printMatcher.group(); }

        else if(plotMatcher.find()) { return plotMatcher.group(); }

        else{ return "No Matches"; }
    }

    protected Object setRawString(String raw){

        this.rawString = raw;
        return this.analyse();

    }
}