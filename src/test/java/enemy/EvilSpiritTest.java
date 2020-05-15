package enemy;

import com.group15.enemy.EvilSpirit;
import com.group15.gameEngine.ID;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class EvilSpiritTest
{
    private EvilSpirit evilSpirit;
    private int timeOut = 10; // Units of seconds

    @BeforeEach
    public void init()
    {
        evilSpirit = new EvilSpirit(20, 30);
    }

    @Test
    public void testStat()
    {
        assertTrue(evilSpirit.getSpeedX() > 0);
        assertTrue(evilSpirit.getSpeedY() > 0);

        assertEquals(ID.EvilSpirit, evilSpirit.getID());

        assertTrue(evilSpirit.getHealth() >= 0);

        assertTrue(evilSpirit.getHeight() > 0);
        assertTrue(evilSpirit.getWidth() > 0);
        assertTrue(evilSpirit.getContactRadius() > 0);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SKILLS --------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testFrenzyMode()
    {
        RewardManager rm = new RewardManager();
        PlayerManager pm = new PlayerManager();

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();

        int currSpeedX = evilSpirit.getSpeedX();
        int currSpeedY = evilSpirit.getSpeedY();

        // Activates if player has collected all essence
        for (int i = 0; i < RewardManager.getMaxEssence(); i++)
        {
            RewardManager.newEssence(x, y);
            rm.tick();
            pm.tick();
        }

        // Test if speed increased
        evilSpirit.tick();
        assertTrue(evilSpirit.getSpeedX() > currSpeedX);
        assertTrue(evilSpirit.getSpeedY() > currSpeedY);

        rm.newGame();
        pm.newGame();
    }

    @Test
    public void testWallSkillOff()
    {
        // Right side is immediately a wall, test if the skill can be off
        int x = 710;
        int y = 255;

        evilSpirit.setX(x);
        evilSpirit.setY(y);

        evilSpirit.setVelX(evilSpirit.getSpeedX());
        evilSpirit.setVelY(0);

        for (int i = 0; i < timeOut; i++)
            evilSpirit.tick();

        // Should stay at still
        assertEquals(x, evilSpirit.getX());
        assertEquals(y, evilSpirit.getY());
    }

    @Test
    public void testWallSkillOn() throws InterruptedException
    {
        // -------------------------------- WALL TEST -------------------------------------

        // Right side is immediately a wall, test if it can pass through it
        int x = 710;
        int y = 255;

        evilSpirit.setX(x);
        evilSpirit.setY(y);

        evilSpirit.setVelX(evilSpirit.getSpeedX());
        evilSpirit.setVelY(0);

        int i;
        for (i = 0; i < 10; i++)
        {
            // Waiting for skill to activate
            TimeUnit.SECONDS.sleep(1);
            evilSpirit.tick();

            if (evilSpirit.getX() > x)
                break;
        }
        // If skill did not activate
        if (i == timeOut)
            fail();

        assertEquals(y, evilSpirit.getY());

        // ------------------------------- BOARDER TEST -----------------------------------

        // Top side is immediately the map boarder, test if we can exit the map
        x = 850;
        y = 40;

        evilSpirit.setX(x);
        evilSpirit.setY(y);

        evilSpirit.setVelX(0);
        evilSpirit.setVelY(-evilSpirit.getSpeedY());

        for (i = 0; i < 10; i++)
        {
            // Waiting for skill to activate
            TimeUnit.SECONDS.sleep(1);
            evilSpirit.tick();

            if (evilSpirit.getY() < y)
            {
                for (int j = 0; j < 2 * timeOut; j++)
                    evilSpirit.tick();

                break;
            }
        }
        // If skill did not activate
        if (i == timeOut)
            fail();

        assertEquals(x, evilSpirit.getX());
        assertTrue(evilSpirit.getY() >= 0);
    }
}


