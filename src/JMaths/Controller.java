package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Analyser analyser = new Analyser();

        // Creating columns
        TableColumn<Function, String> fctName = new TableColumn<Function, String>("Name");
        TableColumn<Function, String> fctVar = new TableColumn<Function, String>("Variable");
        TableColumn<Function, String> fctExp = new TableColumn<Function, String>("Expression");

        TableColumn<Variable, String> varName = new TableColumn<Variable, String>("Name");
        TableColumn<Variable, Double> varValue = new TableColumn<Variable, Double>("Value");

        fctList.getColumns().add(fctName);
        fctList.getColumns().add(fctVar);
        fctList.getColumns().add(fctExp);
        varList.getColumns().add(varName);
        varList.getColumns().add(varValue);

        //TODO : Solve duplicate code in one method called by either ENTER key or SendBtn

        // When releasing ENTER key
        commandLine.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                analyser.setRawString(commandLine.getText());
                commandLine.clear();

                // If analyser.setRawString() returns a function
                if (analyser.isFunction){

                    //TODO : dynamic parameters

                    //Creating the line to add with function parameters
                    final ObservableList<Function> fctData = FXCollections.observableArrayList(
                            new Function("f", "x", "1 / x + 1")
                    );
                    fctName.setCellValueFactory(new PropertyValueFactory<Function, String>("name"));
                    fctVar.setCellValueFactory(new PropertyValueFactory<Function, String>("variable"));
                    fctExp.setCellValueFactory(new PropertyValueFactory<Function, String>("expression"));

                    fctList.setItems(fctData);
                }

                // If analyser.setRawString() returns a variable
                else if (analyser.isVariable){

                    //TODO : dynamic parameters

                    //Creating the line to add with variable parameters
                    final ObservableList<Variable> varData = FXCollections.observableArrayList(
                            new Variable("x", 10)
                    );
                    varName.setCellValueFactory(new PropertyValueFactory<Variable, String>("name"));
                    varValue.setCellValueFactory(new PropertyValueFactory<Variable, Double>("value"));

                    varList.setItems(varData);
                }

                else { System.out.println("Not a function nor a Variable"); }

            }
        });

        // When pressing send button

        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                analyser.setRawString(commandLine.getText());
                commandLine.clear();

                // If analyser.setRawString() returns a function
                if (analyser.isFunction){

                    //TODO : dynamic parameters

                    //Creating the line to add with function parameters
                    final ObservableList<Function> fctData = FXCollections.observableArrayList(
                            new Function("f", "x", "1 / x + 1")
                    );
                    fctName.setCellValueFactory(new PropertyValueFactory<Function, String>("name"));
                    fctVar.setCellValueFactory(new PropertyValueFactory<Function, String>("abstractVariable"));
                    fctExp.setCellValueFactory(new PropertyValueFactory<Function, String>("expression"));

                    fctList.setItems(fctData);
                }

                // If analyser.setRawString() returns a variable
                else if (analyser.isVariable){

                    //TODO : dynamic parameters

                    //Creating the line to add with variable parameters
                    final ObservableList<Variable> varData = FXCollections.observableArrayList(
                            new Variable("x", 10)
                    );
                    varName.setCellValueFactory(new PropertyValueFactory<Variable, String>("name"));
                    varValue.setCellValueFactory(new PropertyValueFactory<Variable, Double>("value"));

                    varList.setItems(varData);
                }

                else { System.out.println("Not a function nor a Variable"); }

            }
        });

        // When pressing clear button

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                printArea.setText("");
            }
        });
    }
}
