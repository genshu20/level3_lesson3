import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import testing.ArraysWithFourAndOne;

import java.util.Arrays;

public class SimpleTest {
    private static ArraysWithFourAndOne fourAndOneSimple;

    @BeforeClass
    public static void init(){
        System.out.println("init Arrrrrrr.....");
        fourAndOneSimple=new ArraysWithFourAndOne();
    }
    @Test
    public void justFail(){
        Assert.fail();
    }
    @Test
    public void testAfterLastFourNorm(){
        Assert.assertTrue(Arrays.equals(new int[]{5},
                fourAndOneSimple.afterLastFour(new int[]{1,2,3,4,5})));
    }
    @Test(expected = RuntimeException.class)
    public void testAfterLastFourEx(){
        fourAndOneSimple.afterLastFour(new int[]{1,2,3,5});
    }


}
