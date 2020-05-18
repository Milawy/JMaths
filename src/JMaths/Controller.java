package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
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
    public Button sendBtn, clearPromptBtn, clearFctBtn, clearVarBtn, clearPlotBtn;
    @FXML
    public TextArea printArea;
    @FXML
    public LineChart lineChart;
    @FXML
    public Text appName, fctListName, varListName;
    @FXML
    public VBox vBox;


    // Define Analyser/Solver classes + all TableColumns we need
    Analyser analyser = new Analyser();
    FunctionSolver fctSolver = new FunctionSolver();
    VariableSolver varSolver = new VariableSolver();

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

            //Converts Object to Function
            Function fctTyped = (Function) objectTyped;

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

        // If analyser.setRawString() returns a printFct
        else if(analyser.isPrint){

            //Converts Object to PrintFct
            PrintFct printLine = (PrintFct) objectTyped;
            printArea.appendText(printLine.solve(fctName, fctVar, fctExp, fctList, varName, varValue, varList) + "\n");
        }

        else if(analyser.isPlot){

            //Converts Object to PlotFct
            PlotFct plotLine = (PlotFct) objectTyped;
            System.out.println("sd");
            XYChart.Series<Double, Double> series = plotLine.getSerie(fctName, fctVar, fctExp, fctList, varName, varValue, varList);
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
        appName.setLayoutX(w*(58.0/1300.0));
        appName.setLayoutY(h*(55.0/800.0));

        fctListName.setLayoutX(w*(1042.0/1300.0));
        fctListName.setLayoutY(h*(55.0/800.0));
        varListName.setLayoutX(w*(1042.0/1300.0));
        varListName.setLayoutY(h*(395.0/800.0));
        fctList.setLayoutX(w*(921.0/1300.0));
        fctList.setLayoutY(h*(67.0/800.0));
        varList.setLayoutX(w*(921.0/1300.0));
        varList.setLayoutY(h*(407.0/800.0));
        clearFctBtn.setLayoutX(w*(1170.0/1300.0));
        clearFctBtn.setLayoutY(h*(356.0/800.0));
        clearVarBtn.setLayoutX(w*(1170.0/1300.0));
        clearVarBtn.setLayoutY(h*(690.0/800.0));

        commandLine.setLayoutX(w*(58.0/1300.0));
        commandLine.setLayoutY(h*(107.0/800.0));
        sendBtn.setLayoutX(w*(58.0/1300.0));
        sendBtn.setLayoutY(h*(157.0/800.0));
        clearPromptBtn.setLayoutX(w*(744.0/1300.0));
        clearPromptBtn.setLayoutY(h*(157.0/800.0));
        clearPlotBtn.setLayoutX(w*(744.0/1300.0));
        clearPlotBtn.setLayoutY(h*(690.0/800.0));
        printArea.setLayoutX(w*(58.0/1300.0));
        printArea.setLayoutY(h*(189.0/800.0));
        lineChart.setLayoutX(w*(60.0/1300.0));
        lineChart.setLayoutY(h*(340.0/800.0));

        // Flex size
        commandLine.setPrefWidth(w*(800.0/1300.0));
        commandLine.setPrefHeight(h*(42.0/800.0));
        fctList.setPrefWidth(w*(333.0/1300.0));
        fctList.setPrefHeight(h*(280.0/800.0));
        varList.setPrefWidth(w*(333.0/1300.0));
        varList.setPrefHeight(h*(280.0/800.0));
        printArea.setPrefWidth(w*(800.0/1300.0));
        printArea.setPrefHeight(h*(142.0/800.0));
        lineChart.setPrefWidth(w*(800.0/1300.0));
        lineChart.setPrefHeight(h*(350.0/800.0));

        clearFctBtn.setPrefWidth(w*(84.0/1300.0));
        clearFctBtn.setPrefHeight(h*(21.0/800.0));
        clearVarBtn.setPrefWidth(w*(84.0/1300.0));
        clearVarBtn.setPrefHeight(h*(21.0/800.0));
        clearVarBtn.setPrefWidth(w*(84.0/1300.0));
        clearVarBtn.setPrefHeight(h*(21.0/800.0));
        clearPromptBtn.setPrefWidth(w*(84.0/1300.0));
        clearPromptBtn.setPrefHeight(h*(21.0/800.0));
        sendBtn.setPrefWidth(w*(84.0/1300.0));
        sendBtn.setPrefHeight(h*(21.0/800.0));
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
