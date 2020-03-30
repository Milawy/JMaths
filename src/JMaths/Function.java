package JMaths;

public class Function {

    protected String name;
    protected String abstractVariable;
    protected String expression;

    public Function(String name, String abstractVariable, String expression){
        this.name = name;
        this.abstractVariable = abstractVariable;
        this.expression = expression;
    }

    public String getName(){ return name; }
    public String getVariable(){ return abstractVariable; }
    public String getExpression(){ return expression; }

    public void setName(String name){ this.name = name; }
    public void setVariable(String abstractVariable){ this.abstractVariable = abstractVariable; }
    public void setExpression(String expression){ this.expression = expression; }

    //Main operations to do quick maths
    private String add(int x, int y){
        return Integer.toString(x + y);
    }
    private String sub(int x, int y){
        return Integer.toString(x - y);
    }
    private String times(int x, int y){
        return Integer.toString(x * y);
    }
    private String divide(int x, int y){
        return Integer.toString(x / y);
    }
    private String pow(int x, int y){
        return Integer.toString(x ^ y);
    }
    
}