package xyz.igorgee.mtaperfectfare;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ResultsActivityTest {

    @Test
    public void calcTest() {
        assertEquals(calculateTotal(1, 1, 1, 0), 2.75);
        assertEquals(calculateTotal(1, 1, 1, .01), 2.75);
        assertEquals(calculateTotal(1, 1, 1, 1), 1.75);
        assertEquals(calculateTotal(2, 1, 1, 0), 5.50);
        assertEquals(calculateTotal(2, 1, 1, .01), 5.50);
        assertEquals(calculateTotal(5, 1, 1, 8.26), 5.50);
        assertEquals(calculateTotal(2, 1, 1, 1), 4.50);
        assertEquals(calculateTotal(1, 2, 3, 4), 11.30);
    }

}