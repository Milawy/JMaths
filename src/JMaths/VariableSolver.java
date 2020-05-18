package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableSolver {

    private static Pattern variablePattern;
    private String result;

    // This bool will allow interlocked variables (example x=3*y and y=2*z)
    public boolean isFinished;

    public VariableSolver(){

        variablePattern = Pattern.compile("[a-zA-Z]+");

        this.isFinished = false;
    }

    public void solveVariable(String rawString, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> tableView){

        this.result = rawString;
        boolean b = this.solveVariableRecursive(this.result, varNameCol, varValCol, tableView);
        while(!b){
            b = this.solveVariableRecursive(this.result, varNameCol, varValCol, tableView);
        }
    }

    public boolean solveVariableRecursive(String rawString, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> tableView){

        Matcher variableMatcher;
        this.isFinished = true;

        variableMatcher = variablePattern.matcher(rawString);

        ObservableList<String> varFounded = FXCollections.observableArrayList();
        ObservableList<String> nameList = FXCollections.observableArrayList();
        ObservableList<String> valueList = FXCollections.observableArrayList();

        // getting the data of the table view
        for (Variable item : tableView.getItems()) {
            nameList.add(varNameCol.getCellObservableValue(item).getValue());
        }
        for (Variable item : tableView.getItems()) {
            valueList.add(varValCol.getCellObservableValue(item).getValue());
        }

        // TODO : replace the value of declared variable
        // fill the list with the detected variables
        while(variableMatcher.find()){
            varFounded.add(variableMatcher.group());
        }

        varFounded.forEach((elt) -> {

            Integer id = find(nameList, elt);

            if(varFounded.size() != 0 && id != -1){
                this.isFinished = false;
            }

            if(id != -1){
                this.result = result.replaceFirst(elt, valueList.get(id));
            }
        });

        //System.out.println(this.result);
        return this.isFinished;
    }

    public String getResult(){
        return this.result;
    }

    private Integer find(ObservableList<String> list, String val) {
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).equals(val)){
                return i;
            }
        }
        return -1;
    }


}
