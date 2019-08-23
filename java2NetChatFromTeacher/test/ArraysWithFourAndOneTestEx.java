import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testing.ArraysWithFourAndOne;

public class ArraysWithFourAndOneTestEx {
    private ArraysWithFourAndOne fourAndOneEx;

    @Before
    public void init(){
        fourAndOneEx=new ArraysWithFourAndOne();
    }
    @Test(expected = RuntimeException.class)
    public void afterLastFourTestEx(){
        fourAndOneEx.afterLastFour(new int[]{1,2,3,5});
    }
    @Test
    public void afterLastFourTestEx2()throws RuntimeException{
        try {
            fourAndOneEx.afterLastFour(new int[]{1, 2, 3, 5});
        }catch (RuntimeException e){
            Assert.assertEquals("no four",e.getMessage());
        }
    }

}
