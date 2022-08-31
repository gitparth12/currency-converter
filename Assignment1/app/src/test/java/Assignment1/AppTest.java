/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Assignment1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class AppTest {
    // The arrayList of the numbers we're using to test the formula methods
    private ArrayList<Double> testNums = new ArrayList<Double>(Arrays.asList(4.35, 3.1415, 1.16457, 42.89, 5.0, 6.73));
    App app = new App();
    // Each method has been tested to 6 d.p.
    @Test
    void testAverage(){
        double num = (double)Math.round(app.getAverage(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "10.546012");
    }
    @Test
    void testEvenMedian(){
        double num = (double)Math.round(app.getMedian(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "4.675");
    }
    @Test
    void testOddMedian(){
        testNums.add(5.6);
        double num = (double)Math.round(app.getMedian(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "5.0");
        testNums.remove(5.6);
    }
    @Test
    void testMax(){
        double num = (double)Math.round(app.getMaximum(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "42.89");
    }
    @Test
    void testMin(){
        double num = (double)Math.round(app.getMinimum(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "1.16457");
    }

    @Test
    void testStandardDeviation(){
        double num = (double)Math.round(app.getSD(testNums) * 1000000d) /1000000d;
        assertEquals(String.valueOf(num), "14.564126");
    }

}
