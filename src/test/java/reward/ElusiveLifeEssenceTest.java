package reward;

import com.group15.gameEngine.ID;
import com.group15.reward.ElusiveLifeEssence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ElusiveLifeEssenceTest
{
    private ElusiveLifeEssence elusive;
    private int timeOut = 10; // Units of seconds

    @BeforeEach
    public void init()
    {
        elusive = new ElusiveLifeEssence(10, 10);
    }

    @Test
    public void testStat()
    {
        assertEquals(0, elusive.getSpeedX());
        assertEquals(0, elusive.getSpeedY());

        assertEquals(ID.ElusiveLifeEssence, elusive.getID());

        assertTrue(elusive.getWidth() > 0);
        assertTrue(elusive.getHeight() > 0);
        assertTrue(elusive.getContactRadius() > 0);

        assertTrue(elusive.getHealth() >= 0);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testIsDead() throws InterruptedException
    {
        // Should die eventually
        int i;
        for (i = 0; i < timeOut; i++)
        {
            TimeUnit.SECONDS.sleep(1);

            if (elusive.isDead())
                break;
        }
        // Failed to die
        if (i == 10)
            fail();
    }

    @Test
    public void testGetHeal()
    {
        assertTrue(elusive.getHeal() >= 0);
    }
}
