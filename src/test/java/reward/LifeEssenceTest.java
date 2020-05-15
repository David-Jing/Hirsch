package reward;

import com.group15.gameEngine.ID;
import com.group15.reward.LifeEssence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LifeEssenceTest
{
    private LifeEssence essence;

    @BeforeEach
    public void init()
    {
        essence = new LifeEssence(10, 10);
    }

    @Test
    public void testStat()
    {
        assertEquals(0, essence.getSpeedX());
        assertEquals(0, essence.getSpeedY());

        assertEquals(ID.LifeEssence, essence.getID());

        assertTrue(essence.getWidth() > 0);
        assertTrue(essence.getHeight() > 0);
        assertTrue(essence.getContactRadius() > 0);

        assertTrue(essence.getHealth() >= 0);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetHeal()
    {
        assertTrue(essence.getHeal() >= 0);
    }
}
