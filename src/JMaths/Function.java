package JMaths;

public class Function {

    protected String name;
    protected String abstractVariable;
    protected String expression;

    public Function(String name, String value, String expression){
        this.name = name;
        this.abstractVariable = value;
        this.expression = expression;
    }

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