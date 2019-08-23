package lesson7;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StartTest {
    public static void start(Class testClass){
        try {
            for (Method m : testClass.getMethods()) {
                if (m.isAnnotationPresent(BeforeSuite.class)) {
                    m.invoke(testClass);
                }
            }
            for (int i = 10; i >=0 ; i--) {
                    for(Method m:testClass.getMethods()){
                    if(m.isAnnotationPresent(MyTest.class)){
                        if(m.getAnnotation(MyTest.class).prior()==i)m.invoke(testClass);
                    }
                }
            }
            for (Method m : testClass.getMethods()) {
                if (m.isAnnotationPresent(AfterSuite.class)) {
                    m.invoke(testClass);
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        StartTest.start(CalculatorTest.class);
    }
}

