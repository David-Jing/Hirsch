package enemy;

import com.group15.enemy.Trap;
import com.group15.gameEngine.ID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrapTest
{
    private Trap trap;

    @BeforeEach
    public void init()
    {
        trap = new Trap(10, 10);
    }

    @Test
    public void testStat()
    {
        assertEquals(0, trap.getSpeedX());
        assertEquals(0, trap.getSpeedY());

        assertEquals(ID.Trap, trap.getID());

        assertTrue(trap.getWidth() > 0);
        assertTrue(trap.getHeight() > 0);
        assertTrue(trap.getContactRadius() > 0);

        assertTrue(trap.getHealth() >= 0);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetHeal()
    {
        assertTrue(trap.getDamage() >= 0);
    }
}
