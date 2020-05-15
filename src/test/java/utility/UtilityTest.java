package utility;

import com.group15.utility.Utility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilityTest
{
    @Test
    public void testDistance()
    {
        assertEquals(5, Utility.distance(0, 0, 3, 4));
        assertEquals(10, Utility.distance(5, 4, 13, 10));
    }

    @Test
    public void testHaveElapsed() throws InterruptedException
    {
        long interval = 1; // Units of seconds
        long curr = System.currentTimeMillis();

        Thread.sleep(interval * 500);
        assertFalse(Utility.haveElapsed(curr, interval));

        Thread.sleep(interval * 500);
        assertTrue(Utility.haveElapsed(curr, interval));
    }

    @Test
    public void testManhattanDistance()
    {
        assertEquals(7, Utility.manhattanDistance(0, 0, 3, 4));
        assertEquals(5, Utility.manhattanDistance(2, 4, 5, 6));
    }

    @Test
    public void testGain2db()
    {
        assertEquals(-20, (int) Utility.gain2db(0.1f));
        assertEquals(-6, (int) Utility.gain2db(0.5f));
    }
}
