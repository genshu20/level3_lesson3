package lesson7;

public class Calculator {
    int x; int y;

    public Calculator(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Calculator() {
    }

    public int add(int a, int b)
    {
        return a + b;
    }

    public int mult(int a, int b)
    {

        return a * b;
    }

}
