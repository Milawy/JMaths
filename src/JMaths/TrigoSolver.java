package JMaths;

import javafx.scene.control.TableColumn;

public class TrigoSolver {

    private String result;
    //private VariableSolver varSolver;

    protected String solve(String name, String var, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol){

        //first find the numerical value of the var string (if it's already a number the variableSolver will not change)
        //this.varSolver = new VariableSolver(String var, TableColumn<Variable, String> varNameCol, TableColumn<Variable, String> varValCol);

        // TODO : cast the result of the varSolver to double and call trigo function

        switch(name) {
            case "cos":
                // code block
                break;
            case "sin":
                // code block
                break;
            case "tan":
                // code block
                break;
            case "arccos":
                // code block
                break;
            case "arcsin":
                // code block
                break;
            case "arctan":
                // code block
            case "exp":
                // code block
            case "log":
                // code block
                break;
            default:
                // code block
        }

        return result;
    }
}
