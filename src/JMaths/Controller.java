package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    //Define Analyser class + all TableColumns we need
    Analyser analyser = new Analyser();

    TableColumn<Function, String> fctName = new TableColumn<>("Name");
    TableColumn<Function, String> fctVar = new TableColumn<>("Variable");
    TableColumn<Function, String> fctExp = new TableColumn<>("Expression");

    TableColumn<Variable, String> varName = new TableColumn<>("Name");
    TableColumn<Variable, Double> varValue = new TableColumn<>("Value");

    //The one that is creating the Table rows
    private void manageTab(){
        Object objectTyped = analyser.setRawString(commandLine.getText());
        commandLine.clear();

        // If analyser.setRawString() returns a function
        if (analyser.isFunction){

            //Converts Object to Function
            Function fctTyped = (Function) objectTyped;

            //Creating the line to add with function parameters
            final ObservableList<Function> fctData = FXCollections.observableArrayList(
                    new Function(fctTyped.getName(), fctTyped.getVariable(), fctTyped.getExpression())
            );
            fctName.setCellValueFactory(new PropertyValueFactory<>("name"));
            fctVar.setCellValueFactory(new PropertyValueFactory<>("variable"));
            fctExp.setCellValueFactory(new PropertyValueFactory<>("expression"));

            fctList.setItems(fctData);
        }

        // If analyser.setRawString() returns a variable
        else if (analyser.isVariable){

            //Converts Object to Variable
            Variable varTyped = (Variable) objectTyped;

            //Creating the line to add with variable parameters
            final ObservableList<Variable> varData = FXCollections.observableArrayList(
                    new Variable(varTyped.getName(), varTyped.getValue())
            );
            varName.setCellValueFactory(new PropertyValueFactory<>("name"));
            varValue.setCellValueFactory(new PropertyValueFactory<>("value"));

            varList.setItems(varData);
        }

        else { System.out.println("Not a function nor a Variable"); }

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
