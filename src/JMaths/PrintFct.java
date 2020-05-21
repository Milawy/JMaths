package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class PrintFct {

    private String functionName;
    private String var;

    // First, solve the function (FctSolver) to get the reduced expression. Then get the variable name of the function and find the variable value (VarSolver).
    // Finally replace the var by its value in the reduced expression and solve the expression again (RawSolver).

    public PrintFct(String functionName, String var){
        this.functionName = functionName;
        this.var = var;
    }

    public String getFunctionName() { return functionName; }
    public String getVar() { return var; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public void setVar(String var) { this.var = var; }

    @FXML
    public TextArea printArea;

    private Integer find(ObservableList<String> list, String val) {
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).equals(val)){
                return i;
            }
        }
        return -1;
    }

    protected String purgeIt(String str, String toPurge){

        str = str.replace(toPurge, "");
        return str;
    }

    public String solve(TableColumn<Function, String> fctNameCol, TableColumn<Function, String> fctVarCol, TableColumn<Function, String> fctExpCol, TableView<Function> tableView, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> varTableView){

        FunctionSolver fctSolver = new FunctionSolver();
        VariableSolver varSolver = new VariableSolver();

        //get data from the function table view
        ObservableList<String> nameList = FXCollections.observableArrayList();
        ObservableList<String> variableList = FXCollections.observableArrayList();
        ObservableList<String> expressionList = FXCollections.observableArrayList();

        for (Function item : tableView.getItems()) {
            nameList.add(fctNameCol.getCellObservableValue(item).getValue());
        }
        for (Function item : tableView.getItems()) {
            variableList.add(fctVarCol.getCellObservableValue(item).getValue());
        }
        for (Function item : tableView.getItems()) {
            expressionList.add(fctExpCol.getCellObservableValue(item).getValue());
        }

        this.functionName = purgeIt(this.functionName, "(");
        this.var = purgeIt(this.var, ",");

        Integer fctId = find(nameList, this.functionName);
        String fctExpression = expressionList.get(fctId);
        String varName = variableList.get(fctId);

        // reduce the expression
        fctSolver.solveFunctions(fctExpression, fctNameCol, fctVarCol, fctExpCol, tableView, varNameCol, varValCol, varTableView);
        fctExpression = fctSolver.getResult();

        // reduce the variable expression to get a numerical value
        varSolver.solveVariable(this.var ,varNameCol, varValCol, varTableView);
        this.var = varSolver.getResult();
        this.var = new RawExpressionSolver().solve(this.var);

        // replace the value of var into the function expression
        fctExpression = fctExpression.replaceAll(varName, this.var);

        // solve declared variables
        varSolver.solveVariable(fctExpression ,varNameCol, varValCol, varTableView);
        fctExpression = varSolver.getResult();

        // use again trigo solver to get numerical value of trigo fct
        fctSolver.solveFunctions(fctExpression, fctNameCol, fctVarCol, fctExpCol, tableView, varNameCol, varValCol, varTableView);
        fctExpression = fctSolver.getResult();

        // then get the numerical value
        try {
            return new RawExpressionSolver().solve(fctExpression);
        }
        catch(ArithmeticException e){
            System.out.println("Division by zero");

        }
        return "0";
    }
}
