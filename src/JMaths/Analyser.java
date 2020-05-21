package JMaths;

import java.util.regex.*;

public class Analyser {

    private String rawString;

    public Boolean isFunction, isVariable, isPrint, isPlot;

    private static Pattern functionPattern, functionNamePattern, functionVariablePattern, functionExpressionPattern;
    private static Pattern variablePattern, variableNamePattern, variableExpressionPattern;
    private static Pattern printPattern, printNamePattern, printVarPattern;
    private static Pattern plotPattern, plotNamePattern, plotLowBoundPattern, plotUpBoundPattern, plotNPattern;

    // Constructor containing all regex expressions we need
    public Analyser(){

        functionPattern = Pattern.compile("^[a-zA-Z ]+\\([a-zA-Z ]+\\)[ ]*=[a-zA-Z0-9\\(\\).^\\/+\\-* ]+");
        functionNamePattern = Pattern.compile("^[a-zA-Z ]+");
        functionVariablePattern = Pattern.compile("\\([a-zA-Z ]+");
        functionExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\(\\)\\/+\\-* ]+");

        variablePattern = Pattern.compile("^[a-zA-Z ]+[ ]*=[a-zA-Z0-9.^\\/+\\-* ]+");
        variableNamePattern = Pattern.compile("^[a-zA-Z ]+");
        variableExpressionPattern = Pattern.compile("=[a-zA-Z0-9.^\\/+\\-* ]+");

        printPattern = Pattern.compile("^print[ ]*\\([a-zA-Z ]+,[a-zA-Z0-9\\+\\-\\*\\^ ]+\\)");
        printNamePattern = Pattern.compile("\\([a-zA-Z ]+");
        printVarPattern = Pattern.compile(",[a-zA-Z0-9\\+\\-\\*\\^ ]+");

        plotPattern = Pattern.compile("^plot[ ]*\\([a-zA-Z ]+[ ]*,[0-9\\- ]+[ ]*,[0-9\\- ]+[ ]*,[0-9 ]+\\)");
        plotNamePattern = Pattern.compile("\\([a-zA-Z ]+");
        plotLowBoundPattern = Pattern.compile(",[0-9\\- ]+");
        plotUpBoundPattern = Pattern.compile(",[0-9\\- ]+");
        plotNPattern = Pattern.compile(",[0-9\\- ]+\\)");
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
        Matcher plotMatcher, plotNameMatcher, plotBoundMatcher, plotNMatcher;

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
        plotNameMatcher = plotNamePattern.matcher(rawString);
        plotBoundMatcher = plotLowBoundPattern.matcher(rawString);
        plotNMatcher = plotNPattern.matcher(rawString);

        // Reset content indicator
        this.isFunction = false;
        this.isVariable = false;
        this.isPrint = false;
        this.isPlot = false;

        // Test if the String is a function
        if(functionMatcher.find()) {

            // Update content indicator
            this.isFunction = true;

            if(functionNameMatcher.find() && functionVariableMatcher.find() && functionExpressionMatcher.find()){

                String purgedFctName = purgeIt(functionNameMatcher.group()," ");
                String purgedFctVariable = purgeIt(functionVariableMatcher.group()," ");
                String purgedFctExpression = purgeIt(functionExpressionMatcher.group()," ");

                //return function because we know that the user wrote a function
                return new Function(purgedFctName, purgeIt(purgedFctVariable, "("), purgeIt(purgedFctExpression, "="));
            }

            else{ return "Wrong function syntax" + "\n" + "Try with the following syntax :" + "\n" + "functionName(variableName)=functionExpression" + "\n"; }
        }

        else if(variableMatcher.find()) {

            // Update content indicator
            this.isVariable = true;

            if(variableNameMatcher.find() && variableExpressionMatcher.find()) {

                String purgedVarName = purgeIt(variableNameMatcher.group()," ");
                String purgedVarValue = purgeIt(variableExpressionMatcher.group()," ");

                //return variable because we know that the user wrote a variable
                return new Variable(purgedVarName, purgeIt(purgedVarValue, "="));
            }

            else{ return "Wrong variable syntax" + "\n" + "Try with the following syntax :" + "\n" + "variableName=variableExpression" + "\n"; }
        }

        else if(printMatcher.find()) {

            // Update content indicator
            this.isPrint = true;

            if(printNameMatcher.find() && printVarMatcher.find()){

                String purgedPrintName = purgeIt(printNameMatcher.group()," ");
                String purgedPrintVar = purgeIt(printVarMatcher.group()," ");

                return new PrintFct(purgedPrintName, purgedPrintVar);
            }

            else{ return "Wrong print syntax" + "\n" + "Try with the following syntax :" + "\n" + "print(functionName,value)" + "\n"; }
        }

        else if(plotMatcher.find()) {

            // Update content indicator
            this.isPlot = true;

            if(plotNameMatcher.find() && plotBoundMatcher.find() && plotNMatcher.find()){
                String low = plotBoundMatcher.group();
                plotBoundMatcher.find();
                String high = plotBoundMatcher.group();

                String purgedPlotName = purgeIt(plotNameMatcher.group()," ");
                String purgedPlotLow = purgeIt(low," ");
                String purgedPlotHigh = purgeIt(high," ");
                String purgedPlotN = purgeIt(plotNMatcher.group()," ");

                return new PlotFct(purgedPlotName, purgedPlotLow, purgedPlotHigh, purgedPlotN);
            }

            else{ return "Wrong plot syntax" + "\n" + "Try with the following syntax :" + "\n" + "plot(functionName,lowerBound,upperBound,numberOfPoints)" + "\n"; }
        }

        else{ return "No matches" + "\n" + "Please define a function, a variable or use a command" + "\n"; }
    }

    // Explicit
    protected Object setRawString(String raw){

        this.rawString = raw;
        return this.analyse();
    }
}