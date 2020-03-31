package JMaths;

public class Variable {

    protected String name;
    protected Double value;

    public Variable(String name, double value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public double getValue() { return value; }
    public void setName(String name) { this.name = name; }
    public void setValue(double value) { this.value = value; }

}