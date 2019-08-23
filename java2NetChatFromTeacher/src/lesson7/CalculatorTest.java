package lesson7;

import org.junit.*;

public class CalculatorTest {
    private static Calculator calc;

    @BeforeSuite
    public static void init() {
        System.out.println("init calc");
        calc = new Calculator();
    }

    @MyTest(prior = 1)
    public static void testAdd() {

        System.out.println(calc.add(2,2)+"= 4");;
    }


    @MyTest(prior = 9)
    public static void testMult() {

        System.out.println(calc.mult(5,10)+"= 50");;
    }
    @AfterSuite
    public static void afterTest(){
        System.out.println("Test finished");
    }
}