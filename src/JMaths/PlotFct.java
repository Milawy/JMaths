package JMaths;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PlotFct {

    private String functionName;
    private Double lowerBound;
    private Double higherBound;
    private Double n;

    public PlotFct(String functionName, String lowerBound, String higherBound, String n){

        functionName = functionName.replaceAll("\\(", "\\\\(");
        functionName = purgeIt(functionName, "\\(");
        lowerBound = lowerBound.replaceAll("\\-", "\\\\-");
        lowerBound = purgeIt(lowerBound, ",");
        lowerBound = purgeIt(lowerBound, "\\");
        higherBound = higherBound.replaceAll("\\-", "\\\\-");
        higherBound = purgeIt(higherBound, ",");
        higherBound = purgeIt(higherBound, "\\");
        n = n.replaceAll("\\-", "\\\\-");
        n = n.replaceAll("\\)", "\\\\)");
        n = purgeIt(n, ",");
        n = purgeIt(n, "\\)");

        this.functionName = functionName;
        this.lowerBound = Double.parseDouble(lowerBound);
        this.higherBound =  Double.parseDouble(higherBound);
        this.n = Double.parseDouble(n);
    }

    // This method will return the list of points of the expression
    public XYChart.Series<Double, Double> getSerie(TableColumn<Function, String> fctNameCol, TableColumn<Function, String> fctVarCol, TableColumn<Function, String> fctExpCol, TableView<Function> tableView, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol, TableView<Variable> varTableView){

        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();

        FunctionSolver fctSolver = new FunctionSolver();
        VariableSolver varSolver = new VariableSolver();

        this.functionName = purgeIt(this.functionName, "(");

        PrintFct solver;
        String res;

        for (double x = lowerBound; x <= higherBound; x = x + (higherBound-lowerBound)/n) {

            // TODO : Add parenthesis before the ^ sign to get the - into the power

            solver = new PrintFct(this.functionName, Double.toString(x));
            res = solver.solve(fctNameCol, fctVarCol, fctExpCol, tableView, varNameCol, varValCol, varTableView);
            System.out.println(x + " : " + res);
            series.getData().add(new XYChart.Data<Double, Double>(x,  Double.parseDouble(res)));


        }

        return series;
    }

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
}
