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

    public FunctionSolver(){

        functionPattern = Pattern.compile("[a-zA-Z]+\\([a-zA-Z]+\\)");
        functionNamePattern = Pattern.compile("^[a-zA-Z]+");
        functionVariablePattern = Pattern.compile("\\([a-zA-Z]+");
    }

    private Integer find(ObservableList<String> list, String val) {
        for (int i = 0; i < list.size(); i++){
            if(list.get(i).equals(val)){
                return i;
            }
        }
        return -1;
    }

    public void solveFunctions(String rawString, TableColumn<Function, String> fctNameCol, TableColumn<Function, String> fctVarCol, TableColumn<Function, String> fctExpCol, TableView<Function> tableView){

        Matcher functionMatcher;

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
                System.out.println(functionMatcher.group());
                nameFounded.add(functionMatcher.group());
        }

        nameFounded.forEach((elt) -> {

            // find the name of the function to compare it with existing ones
            Matcher functionNameMatcher = functionNamePattern.matcher(elt);
            String fctName;

            if(functionNameMatcher.find()){

                fctName = functionNameMatcher.group();
                Integer id = find(nameList, fctName);

                if(id != -1){

                }

            }
            else{
                System.out.println("Error in the function name");
            }
        });
    }
}
