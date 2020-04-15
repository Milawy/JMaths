package JMaths;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField commandLine;
    @FXML
    public TableView<Function> fctList;
    @FXML
    public TableView<Variable> varList;
    @FXML
    public Button sendBtn, clearBtn;
    @FXML
    public TextArea printArea;
    @FXML
    public LineChart lineChart;

    //Define Analyser/Solver classes + all TableColumns we need
    Analyser analyser = new Analyser();
    FunctionSolver fctSolver = new FunctionSolver();
    VariableSolver varSolver = new VariableSolver();

    TableColumn<Function, String> fctName = new TableColumn<>("Name");
    TableColumn<Function, String> fctVar = new TableColumn<>("Variable");
    TableColumn<Function, String> fctExp = new TableColumn<>("Expression");

    TableColumn<Variable, String> varName = new TableColumn<>("Name");
    TableColumn<Variable, String> varValue = new TableColumn<>("Value");

    //The one that is creating the Table rows
    private void manageTab(){
        Object objectTyped = analyser.setRawString(commandLine.getText());

        // If analyser.setRawString() returns a function
        if (analyser.isFunction){

            //Converts Object to Function
            Function fctTyped = (Function) objectTyped;
            fctSolver.solveFunctions(fctTyped.getExpression(), fctName, fctVar, fctExp, fctList, varName, varValue, varList);
            varSolver.solveVariable(fctSolver.getResult(), varName, varValue, varList);

            //Adding the function, its variable and its expression typed by the user in the TableView
            fctName.setCellValueFactory(new PropertyValueFactory<>("name"));
            fctVar.setCellValueFactory(new PropertyValueFactory<>("variable"));
            fctExp.setCellValueFactory(new PropertyValueFactory<>("expression"));

            fctList.getItems().add(fctTyped);
        }

        // If analyser.setRawString() returns a variable
        else if (analyser.isVariable){

            //Converts Object to Variable
            Variable varTyped = (Variable) objectTyped;
            varSolver.solveVariable(varTyped.getValue(), varName, varValue, varList);

            //Adding the variable and its value typed by the user in the TableView
            varName.setCellValueFactory(new PropertyValueFactory<>("name"));
            varValue.setCellValueFactory(new PropertyValueFactory<>("value"));

            varList.getItems().add(varTyped);
        }

        //TODO : Problem when returning a String, that is announcing an error in the typed text by the user.

        else { printArea.appendText(analyser.setRawString(commandLine.getText()).toString()); }

        commandLine.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Creating columns
        fctList.getColumns().add(fctName);
        fctList.getColumns().add(fctVar);
        fctList.getColumns().add(fctExp);
        varList.getColumns().add(varName);
        varList.getColumns().add(varValue);

        // When releasing ENTER key
        commandLine.setOnKeyReleased(event -> { if(event.getCode() == KeyCode.ENTER) { manageTab(); } });

        // When pressing send button
        sendBtn.setOnAction(actionEvent -> manageTab());

        // When pressing clear button
        clearBtn.setOnAction(actionEvent -> printArea.setText(""));
    }
}
