package JMaths;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField commandLine;
    @FXML
    public TableView fctList, varList;
    @FXML
    public Button sendBtn, clearBtn;
    @FXML
    public TextArea printArea;
    @FXML
    public LineChart lineChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Analyser analyser = new Analyser();

        TableColumn fctName = new TableColumn("Name");
        TableColumn fctVar = new TableColumn("Variable");
        TableColumn fctExp = new TableColumn("Expression");

        TableColumn varName = new TableColumn("Name");
        TableColumn varValue = new TableColumn("Value");

        fctList.getColumns().addAll(fctName, fctVar, fctExp);
        varList.getColumns().addAll(varName, varValue);

        commandLine.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                printArea.appendText(analyser.setRawString(commandLine.getText()) + "\n");
                commandLine.clear();
            }
        });
        sendBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                printArea.appendText(analyser.setRawString(commandLine.getText()) + "\n");
                commandLine.clear();
            }
        });
        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                printArea.setText("");
            }
        });
    }
}
