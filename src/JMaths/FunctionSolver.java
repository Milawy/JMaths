package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionSolver {

    private static Pattern functionPattern, functionNamePattern, functionVariablePattern;
    private String result;

    // This bool will allow interlocked functions (example f(x)=g(x) and g(x)=h(x))
    public boolean isFinished;

    public FunctionSolver(){

        functionPattern = Pattern.compile("[a-zA-Z]+\\([a-zA-Z0-9.^\\/\\+\\-\\*]+\\)");
        functionNamePattern = Pattern.compile("^[a-zA-Z]+");
        functionVariablePattern = Pattern.compile("\\([a-zA-Z0-9.^\\/\\+\\-\\*]+");

        this.isFinished = false;
    }

    private Integer find(ObservableList<String> list, String val) {
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).equals(val)){
                return i;
            }
        }
        return -1;
    }

    public String getResult(){
        return this.result;
    }

    public void solveFunctions(String rawString, TableColumn<Function, String> fctNameCol, TableColumn<Function, String> fctVarCol, TableColumn<Function, String> fctExpCol, TableView<Function> tableView, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> varTableView){

        this.result = rawString;
        boolean b = this.solveFunctionsRecursive(this.result, fctNameCol, fctVarCol, fctExpCol, tableView, varNameCol, varValCol, varTableView);
        while(!b){
            b = this.solveFunctionsRecursive(this.result, fctNameCol, fctVarCol, fctExpCol, tableView, varNameCol, varValCol, varTableView);
        }
    }

    protected String purgeIt(String str, String toPurge){

        str = str.replace(toPurge, "");
        return str;
    }

    public boolean solveFunctionsRecursive(String rawString, TableColumn<Function, String> fctNameCol, TableColumn<Function, String> fctVarCol, TableColumn<Function, String> fctExpCol, TableView<Function> tableView, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> varTableView){

        Matcher functionMatcher;
        this.isFinished = true;

        functionMatcher = functionPattern.matcher(rawString);

        ObservableList<String> nameFounded = FXCollections.observableArrayList();
        ObservableList<String> nameList = FXCollections.observableArrayList();
        ObservableList<String> variableList = FXCollections.observableArrayList();
        ObservableList<String> expressionList = FXCollections.observableArrayList();

        // getting the data of the table view
        for (Function item : tableView.getItems()) {
            nameList.add(fctNameCol.getCellObservableValue(item).getValue());
        }
        for (Function item : tableView.getItems()) {
            variableList.add(fctVarCol.getCellObservableValue(item).getValue());
        }
        for (Function item : tableView.getItems()) {
            expressionList.add(fctExpCol.getCellObservableValue(item).getValue());
        }

        // fill the list with the detected functions
        while(functionMatcher.find()){

            // test if the name is a trigonometric function and then replace by it's value
            nameFounded.add(functionMatcher.group());
        }

        if(nameFounded.size() != 0){
            this.isFinished = false;
        }

        nameFounded.forEach((elt) -> {

            // find the name of the function to compare it with existing ones
            Matcher functionNameMatcher = functionNamePattern.matcher(elt);
            Matcher functionVariableMatcher = functionVariablePattern.matcher(elt);

            String fctName;

            if(functionNameMatcher.find()){

                fctName = functionNameMatcher.group();
                Integer id = find(nameList, fctName);

                elt = elt.replaceAll("\\(", "\\\\(");
                elt = elt.replaceAll("\\)", "\\\\)");
                elt = elt.replaceAll("\\*", "\\\\*");
                elt = elt.replaceAll("\\+", "\\\\+");
                elt = elt.replaceAll("\\/", "\\\\/");
                elt = elt.replaceAll("\\-", "\\\\-");

                if(id != -1){ // In replaceFirst function you give a regex so we have to add backslash before parenthesis
                    this.result = result.replaceFirst(elt, expressionList.get(id));
                }
                else if(functionVariableMatcher.find() && !solveTrigoFunction(fctName, functionVariableMatcher.group(), elt, varNameCol, varValCol, varTableView)){ // if the function isn't declared it could be a trigo function
                    // TODO : cannot find this function and break the loop of solveFunctions
                }

            }
            else{
                System.out.println("Error in the function name");
            }
        });

        return this.isFinished;
    }

    protected Boolean solveTrigoFunction(String name, String var, String elt, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> tableView){

        // purge the '('
        var = purgeIt(var, "(");

        // TODO : solve the var for getting a numerical value
        VariableSolver varSolver = new VariableSolver();
        varSolver.solveVariable(var, varNameCol, varValCol, tableView);
        var = varSolver.getResult();

        String val = new RawExpressionSolver().solve(var);

        switch(name) {
            case "cos":
                val = Double.toString(Math.cos(Double.parseDouble(val)));
                break;
            case "sin":
                val = Double.toString(Math.sin(Double.parseDouble(val)));
                break;
            case "tan":
                val = Double.toString(Math.tan(Double.parseDouble(val)));
                break;
            case "arccos":
                val = Double.toString(Math.acos(Double.parseDouble(val)));
                break;
            case "arcsin":
                val = Double.toString(Math.asin(Double.parseDouble(val)));
                break;
            case "arctan":
                val = Double.toString(Math.atan(Double.parseDouble(val)));
            case "exp":
                val = Double.toString(Math.exp(Double.parseDouble(val)));
            case "log": // base 10
                val = Double.toString(Math.log10(Double.parseDouble(val)));
                break;
            case "ln": // base 2
                val = Double.toString(Math.log(Double.parseDouble(val)));
                break;
            default:
                return false;
        }

        this.result = result.replaceFirst(elt, val);
        return true;
    }
}
