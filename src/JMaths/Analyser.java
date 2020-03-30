package JMaths;

import java.util.regex.*;

public class Analyser {

    private String rawString;
    private Function function;
    private Variable variable;
    private String outputFunction, outputVariable, outputPrint, outputPlot;

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

    protected String analyse(){

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

                this.function = new Function(functionNameMatcher.group(),
                        purgeIt(functionVariableMatcher.group(),"("),
                        purgeIt(functionExpressionMatcher.group(),"="));

                this.outputFunction = functionNameMatcher.group() + purgeIt(functionVariableMatcher.group(),"(") + purgeIt(functionExpressionMatcher.group(),"=");
                return this.outputFunction;
            }

            else{

                return "Wrong function";

            }
        }

        else if(variableMatcher.find()) {

            if(variableNameMatcher.find() && variableExpressionMatcher.find()) {

                // Update content indicator
                this.isVariable = true;

                this.variable = new Variable(variableNameMatcher.group(),
                        Double.parseDouble(purgeIt(variableExpressionMatcher.group(), "=")));
            }

            this.outputVariable = variableNameMatcher.group() + purgeIt(variableExpressionMatcher.group(),"=");
            return this.outputVariable;

        }

        else if(printMatcher.find()) {

            this.outputPrint = printMatcher.group();
            return this.outputPrint;

        }

        else if(plotMatcher.find()) {

            this.outputPlot = plotMatcher.group();
            return this.outputPlot;

        }

        else{

            return "No Matches";

        }
    }

    protected String setRawString(String raw){

        this.rawString = raw;
        return this.analyse();

    }
}
