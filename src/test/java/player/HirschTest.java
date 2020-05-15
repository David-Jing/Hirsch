package player;

import com.group15.gameEngine.ID;
import com.group15.player.Hirsch;
import com.group15.reward.RewardManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class HirschTest
{
    private Hirsch hirsch;
    private Random r = new Random();
    private int timeOut = 10; // Units of seconds
    private int frameLimit = 50;

    @BeforeEach
    public void init()
    {
        hirsch = new Hirsch(10, 10);
    }

    @Test
    public void testStat()
    {
        assertTrue(hirsch.getSpeedX() > 0);
        assertTrue(hirsch.getSpeedY() > 0);

        assertEquals(ID.Hirsch, hirsch.getID());

        assertEquals(0, hirsch.getCollectedEssence());
        assertEquals(0, hirsch.getCollectedElusive());

        assertTrue(hirsch.getHealth() >= 0);
        assertTrue(hirsch.getMaxHealth() > 0);

        assertTrue(hirsch.getWidth() > 0);
        assertTrue(hirsch.getHeight() > 0);
        assertTrue(hirsch.getContactRadius() > 0);

        assertTrue(hirsch.getDamageCoolDown() >= 0f);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testSetDeath()
    {
        hirsch.setHealth(r.nextInt(10) + 1);
        hirsch.setDeath();
        assertEquals(0, hirsch.getHealth());
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testCanBeDamaged() throws InterruptedException
    {
        hirsch.subHealth(1);
        assertFalse(hirsch.canBeDamaged());

        // Check if player can be damaged eventually
        int i;
        for (i = 0; i < timeOut; i++)
        {
            TimeUnit.SECONDS.sleep(1);

            if (hirsch.canBeDamaged())
                break;
        }
        // Fail to be re-damaged
        if (i == timeOut)
            fail();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- ADDERS --------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testAddEssence()
    {
        int iterations = r.nextInt(10) + 1;

        for (int i = 0; i < iterations; i++)
            hirsch.addCollectedEssence();

        assertEquals(iterations, hirsch.getCollectedEssence());
    }

    @Test
    public void testAddElusive()
    {
        int iterations = r.nextInt(10) + 1;

        for (int i = 0; i < iterations; i++)
            hirsch.addCollectedElusive();

        assertEquals(iterations, hirsch.getCollectedElusive());
    }

    @Test
    public void testAddHealth()
    {
        hirsch.setHealth(0);
        hirsch.addHealth(1);
        assertEquals(1, hirsch.getHealth());

        // Test max health limit
        hirsch.addHealth(hirsch.getMaxHealth());
        assertEquals(hirsch.getMaxHealth(), hirsch.getHealth());
    }

    @Test
    public void testSubHealth()
    {
        int currHealth = r.nextInt(10) + 1;
        int damage = r.nextInt(currHealth);

        hirsch.setHealth(currHealth);
        hirsch.subHealth(damage);

        assertEquals(currHealth - damage, hirsch.getHealth());
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- MOVEMENT ------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testMove()
    {
        // Checks for wall collision on all 4 sides.

        int i;

        // ---------------------------------- LEFT ----------------------------------------

        // left (cannot pass entrance)
        hirsch.setX(520);
        hirsch.setY(180);

        hirsch.setVelY(0);
        hirsch.setVelX((-hirsch.getSpeedX()));
        for (i = 0; i < frameLimit; i++)
        {
            int temp = hirsch.getX();
            hirsch.tick();

            if (hirsch.getX() == temp)
            {
                hirsch.tick();
                i++;
                if (hirsch.getX() == temp)
                    break;
            }
        }
        // Passed through walls
        if (i == frameLimit)
            fail();

        // --------------------------------- RIGHT ----------------------------------------

        // right (cannot pass entrance)
        hirsch.setX(520);
        hirsch.setY(180);

        hirsch.setVelY(0);
        hirsch.setVelX(hirsch.getSpeedX());
        for (i = 0; i < frameLimit; i++)
        {
            int temp = hirsch.getX();
            hirsch.tick();

            if (hirsch.getX() == temp)
            {
                hirsch.tick();
                i++;
                if (hirsch.getX() == temp)
                    break;
            }
        }
        // Passed through walls
        if (i == frameLimit)
            fail();

        // ----------------------------------- UP -----------------------------------------

        // up (cannot pass entrance)
        hirsch.setX(520);
        hirsch.setY(180);

        hirsch.setVelX(0);
        hirsch.setVelY(-hirsch.getSpeedY());
        for (i = 0; i < frameLimit; i++)
        {
            int temp = hirsch.getY();
            hirsch.tick();

            if (hirsch.getY() == temp)
            {
                hirsch.tick();
                i++;
                if (hirsch.getY() == temp)
                    break;
            }
        }
        // Passed through entrance
        if (i == frameLimit)
            fail();

        // ----------------------------------- UP -----------------------------------------

        // up (can pass entrance)
        while (hirsch.getCollectedEssence() < RewardManager.getMaxEssence())
            hirsch.addCollectedEssence();

        hirsch.setX(520);
        hirsch.setY(180);
        for (i = 0; i < frameLimit; i++)
        {
            hirsch.tick();

            if (hirsch.getY() <= 0)
                break;
        }
        // Did not pass through entrance
        if (i == frameLimit)
            fail();

        // ---------------------------------- DOWN ----------------------------------------

        // down (cannot pass entrance)
        hirsch.setX(520);
        hirsch.setY(180);

        hirsch.setVelX(0);
        hirsch.setVelY(hirsch.getSpeedY());
        for (i = 0; i < frameLimit; i++)
        {
            int temp = hirsch.getY();
            hirsch.tick();

            if (hirsch.getY() == temp)
            {
                hirsch.tick();
                i++;
                if (hirsch.getY() == temp)
                    break;
            }
        }
        // Passed through walls
        if (i == frameLimit)
            fail();
    }
}
