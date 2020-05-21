package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField commandLine;
    @FXML
    public TableView<Function> fctList;
    @FXML
    public TableView<Variable> varList;
    @FXML
    public Button sendBtn, clearPromptBtn, clearFctBtn, clearVarBtn, clearPlotBtn;
    @FXML
    public TextArea printArea;
    @FXML
    public LineChart lineChart;
    @FXML
    public Label copyright, commandText, fctListName, varListName;
    @FXML
    public NumberAxis xAxis;


    // Define Analyser/Solver classes + all TableColumns we need
    Analyser analyser = new Analyser();

    TableColumn<Function, String> fctName = new TableColumn<>("Name");
    TableColumn<Function, String> fctVar = new TableColumn<>("Variable");
    TableColumn<Function, String> fctExp = new TableColumn<>("Expression");

    TableColumn<Variable, String> varName = new TableColumn<>("Name");
    TableColumn<Variable, String> varValue = new TableColumn<>("Value");

    ObservableList<String> historyList = FXCollections.observableArrayList();
    Integer historyId = -1;

    // The one that is creating the Table rows
    private void manageTab(){

        historyId = -1;
        historyList.add(0, commandLine.getText());
        Object objectTyped = analyser.setRawString(commandLine.getText());

        // If analyser.setRawString() returns a function
        if (analyser.isFunction){

            // Counting rows
            int i = 0;

            // Converts Object to Function
            Function fctTyped = (Function) objectTyped;

            // If it's the first time we add something to this TableView
            if(fctList.getItems().isEmpty()){

                // Adding the function, its variable and its expression typed by the user in the TableView
                fctName.setCellValueFactory(new PropertyValueFactory<>("name"));
                fctVar.setCellValueFactory(new PropertyValueFactory<>("variable"));
                fctExp.setCellValueFactory(new PropertyValueFactory<>("expression"));
                fctList.getItems().add(fctTyped);

            }

            // If not
            else if(!fctList.getItems().isEmpty()){

                // Checking if fctTyped is not already in TableView
                TableColumn<Function, String> columnToCheck = fctName;

                // Browsing our TableView
                for (Function item : fctList.getItems()) {

                    // If the function we typed is already in the TableView, replace its variable and expression
                    if(fctTyped.getName().equals(columnToCheck.getCellObservableValue(item).getValue())){

                        fctList.getItems().set(i, fctTyped);
                        commandLine.clear();
                        return;

                    }
                    i++;
                }

                // We didn't find the same function name, so create a new row
                fctName.setCellValueFactory(new PropertyValueFactory<>("name"));
                fctVar.setCellValueFactory(new PropertyValueFactory<>("variable"));
                fctExp.setCellValueFactory(new PropertyValueFactory<>("expression"));
                fctList.getItems().add(fctTyped);

            }
        }

        // If analyser.setRawString() returns a variable
        else if (analyser.isVariable){

            // Counting rows
            int i = 0;

            // Converts Object to Variable
            Variable varTyped = (Variable) objectTyped;

            // If it's the first time we add something to this TableView
            if(varList.getItems().isEmpty()){

                varName.setCellValueFactory(new PropertyValueFactory<>("name"));
                varValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                varList.getItems().add(varTyped);

            }

            // If not
            else if(!varList.getItems().isEmpty()){

                // Checking if varTyped is not already in TableView
                TableColumn<Variable, String> columnToCheck = varName;

                // Browsing our TableView
                for (Variable item : varList.getItems()) {

                    // If the variable we typed is already in the TableView, replace its value
                    if(varTyped.getName().equals(columnToCheck.getCellObservableValue(item).getValue())){

                        varList.getItems().set(i, varTyped);
                        commandLine.clear();
                        return;

                    }
                    i++;
                }

                // We didn't find the same variable name, so create a new row
                varName.setCellValueFactory(new PropertyValueFactory<>("name"));
                varValue.setCellValueFactory(new PropertyValueFactory<>("value"));
                varList.getItems().add(varTyped);

            }
        }

        // If analyser.setRawString() returns a printFct
        else if(analyser.isPrint){

            //Converts Object to PrintFct
            PrintFct printLine = (PrintFct) objectTyped;
            printArea.appendText(printLine.solve(fctName, fctVar, fctExp, fctList, varName, varValue, varList) + "\n");

        }

        // If analyser.setRawString() returns a plotFct
        else if(analyser.isPlot){

            //Converts Object to PlotFct
            PlotFct plotLine = (PlotFct) objectTyped;
            XYChart.Series<Double, Double> series = plotLine.getSerie(fctName, fctVar, fctExp, fctList, varName, varValue, varList, xAxis);
            lineChart.getData().add(series);

        }

        //TODO : Problem when returning a String, that is announcing an error in the typed text by the user.

        else { printArea.appendText(analyser.setRawString(commandLine.getText()).toString()); }

        commandLine.clear();
    }

    private void climbUpHistory(){
        if(historyId+1 < historyList.size()){
            historyId++;
            commandLine.setText(historyList.get(historyId));
        }
    }

    private void climbDownHistory(){
        if(historyId > 0){
            historyId--;
            commandLine.setText(historyList.get(historyId));
        }
    }

    private void setFlexibleProperties(){
        // Flex size (old : 1300x800)
        GraphicsDevice Gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        double w = Gd.getDisplayMode().getWidth();
        double h = Gd.getDisplayMode().getHeight();

        // Here you can uncomment and change the the resolution to test
        //w = 1600;
        //h = 800;

        // Flex position
        commandText.setLayoutX(w*(242.0/1920.0));
        commandText.setLayoutY(h*(195.0/1080.0));
        fctListName.setLayoutX(w*(1350.0/1920.0));
        fctListName.setLayoutY(h*(160.0/1080.0));
        varListName.setLayoutX(w*(1350.0/1920.0));
        varListName.setLayoutY(h*(535.0/1080.0));
        copyright.setLayoutX(w*(488.0/1300.0));
        copyright.setLayoutY(h*(700.0/800.0));

        fctList.setLayoutX(w*(1350.0/1920.0));
        fctList.setLayoutY(h*(243.0/1080.0));
        varList.setLayoutX(w*(1350.0/1920.0));
        varList.setLayoutY(h*(625.0/1080.0));
        clearFctBtn.setLayoutX(w*(1712.0/1920.0));
        clearFctBtn.setLayoutY(h*(181.0/1080.0));
        clearVarBtn.setLayoutX(w*(1712.0/1920.0));
        clearVarBtn.setLayoutY(h*(565.0/1080.0));

        commandLine.setLayoutX(w*(350/1920.0));
        commandLine.setLayoutY(h*(70/1080.0));
        sendBtn.setLayoutX(w*(1460.0/1920.0));
        sendBtn.setLayoutY(h*(70.0/1080.0));
        clearPromptBtn.setLayoutX(w*(1212/1920.0));
        clearPromptBtn.setLayoutY(h*(190.0/1080.0));
        clearPlotBtn.setLayoutX(w*(1212.0/1920.0));
        clearPlotBtn.setLayoutY(h*(410.0/1080.0));
        printArea.setLayoutX(w*(241.0/1920.0));
        printArea.setLayoutY(h*(243.0/1080.0));
        lineChart.setLayoutX(w*(225.0/1920.0));
        lineChart.setLayoutY(h*(450.0/1080.0));

        // Flex size
        commandText.setPrefWidth(w*(96.0/1920.0));
        commandText.setPrefHeight(h*(50.0/1080.0));
        fctListName.setPrefWidth(w*(500.0/1920.0));
        fctListName.setPrefHeight(h*(100.0/1080.0));
        varListName.setPrefWidth(w*(500.0/1920.0));
        varListName.setPrefHeight(h*(100.0/1080.0));
        copyright.setPrefWidth(w*(250.0/1920.0));
        copyright.setPrefHeight(h*(50.0/1080.0));

        commandLine.setPrefWidth(w*(1100/1920.0));
        commandLine.setPrefHeight(h*(50.0/1080.0));
        fctList.setPrefWidth(w*(475.0/1920.0));
        fctList.setPrefHeight(h*(300.0/1080.0));
        varList.setPrefWidth(w*(475.0/1920.0));
        varList.setPrefHeight(h*(300.0/1080.0));
        printArea.setPrefWidth(w*(1080.0/1920.0));
        printArea.setPrefHeight(h*(150.0/1080.0));
        lineChart.setPrefWidth(w*(1100.0/1900.0));
        lineChart.setPrefHeight(h*(470.0/1080.0));

        clearFctBtn.setPrefWidth(w*(110.0/1920.0));
        clearFctBtn.setPrefHeight(h*(40.0/1080.0));
        clearVarBtn.setPrefWidth(w*(110.0/1920.0));
        clearVarBtn.setPrefHeight(h*(40.0/1080.0));
        clearPlotBtn.setPrefWidth(w*(110.0/1920.0));
        clearPlotBtn.setPrefHeight(h*(40.0/1080.0));
        clearPromptBtn.setPrefWidth(w*(110.0/1920.0));
        clearPromptBtn.setPrefHeight(h*(40.0/1080.0));
        sendBtn.setPrefWidth(w*(127.0/1920.0));
        sendBtn.setPrefHeight(h*(50.0/1080.0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setFlexibleProperties();

        // Creating columns
        fctList.getColumns().add(fctName);
        fctList.getColumns().add(fctVar);
        fctList.getColumns().add(fctExp);
        varList.getColumns().add(varName);
        varList.getColumns().add(varValue);

        // Command line listeners
        commandLine.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER) { manageTab(); }
            else if(event.getCode() == KeyCode.UP) { climbUpHistory(); }
            else if(event.getCode() == KeyCode.DOWN) { climbDownHistory(); }
        });

        // When pressing send button
        sendBtn.setOnAction(actionEvent -> manageTab());

        // All clear buttons
        clearPromptBtn.setOnAction(actionEvent -> printArea.setText(""));
        clearFctBtn.setOnAction(actionEvent -> fctList.getItems().clear());
        clearVarBtn.setOnAction(actionEvent -> varList.getItems().clear());
        clearPlotBtn.setOnAction(actionEvent -> lineChart.getData().clear());
    }
}
