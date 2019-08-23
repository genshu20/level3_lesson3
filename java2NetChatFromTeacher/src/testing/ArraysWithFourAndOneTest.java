package testing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)

public class ArraysWithFourAndOneTest {
    private static ArraysWithFourAndOne fourAndOne=null;
    private int[]afterFour;
    private int[]afterFourRes;
    private int[]oneAndFour;
    private boolean oneAndFourRes;

    public ArraysWithFourAndOneTest(int[] afterFour, int[] afterFourRes, int[] oneAndFour, boolean oneAndFourRes) {
        this.afterFour = afterFour;
        this.afterFourRes = afterFourRes;
        this.oneAndFour = oneAndFour;
        this.oneAndFourRes = oneAndFourRes;
    }
    @Parameterized.Parameters
    public static Collection data(){
        return Arrays.asList(new Object[][]{
                {new int[]{2,3,4,5},new int[]{5},new int[]{1,1,1},false},
                {new int[]{4,2,3,5},new int[]{2,3,5},new int[]{4,4,4},false},
                {new int[]{2,3,5,4},new int[]{},new int[]{2,3,4},false},
                {new int[]{4,2,4,5},new int[]{5},new int[]{1,1,4},true}
        });
    }
    @Before
    public void init(){
        fourAndOne=new ArraysWithFourAndOne();
    }
    @Test
    public void testAfterLastFour(){
        Assert.assertTrue(Arrays.equals(afterFourRes,fourAndOne.afterLastFour(afterFour)));
    }

    @Test
    public void testOneAndFour(){
        Assert.assertEquals(oneAndFourRes,fourAndOne.oneAndFour(oneAndFour));
    }
}
