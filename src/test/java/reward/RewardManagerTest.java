package reward;

import com.group15.gameEngine.ID;
import com.group15.player.PlayerManager;
import com.group15.reward.ElusiveLifeEssence;
import com.group15.reward.LifeEssence;
import com.group15.reward.RewardManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RewardManagerTest
{
    private RewardManager rm;
    private PlayerManager pm;

    @BeforeEach
    public void init()
    {
        pm = new PlayerManager();
        rm = new RewardManager();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetHeal()
    {
        assertTrue(RewardManager.getElusiveHeal() >= 0);
        assertTrue(RewardManager.getEssenceHeal() >= 0);
    }

    @Test
    public void testGetMaxEssence()
    {
        assertTrue(RewardManager.getMaxEssence() > 0);
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- ELUSIVE LOGIC ---------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testCheckElusiveLife() throws InterruptedException
    {
        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();

        if (!RewardManager.newElusive(x, y))
            fail();

        TimeUnit.SECONDS.sleep(10);
        rm.tick();

        // The elusive essence should die eventually
        assertEquals(ID.Null, RewardManager.isReward(x, y));
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- COLLISIONS ------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testIsReward()
    {
        ElusiveLifeEssence elusive = new ElusiveLifeEssence(0, 0);
        LifeEssence essence = new LifeEssence(0, 0);

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int elusiveRadius = elusive.getContactRadius();
        int essenceRadius = essence.getContactRadius();

        // ----------------------------- ELUSIVE LIFE ESSENCE -----------------------------

        rm.newGame();

        // On Location
        if (!RewardManager.newElusive(x, y))
            fail();
        assertEquals(ID.ElusiveLifeEssence, RewardManager.isReward(x, y));
        assertEquals(ID.Null, RewardManager.isReward(x, y));

        // On Contact Edge
        if (!RewardManager.newElusive(x, y))
            fail();
        assertEquals(ID.ElusiveLifeEssence, RewardManager.isReward(x + elusiveRadius, y));
        assertEquals(ID.Null, RewardManager.isReward(x, y));

        // Outside Perimeter
        if (!RewardManager.newElusive(x, y))
            fail();
        assertEquals(ID.Null, RewardManager.isReward(x + elusiveRadius + 1, y));

        // -------------------------------- LIFE ESSENCE ----------------------------------

        rm.newGame();

        // On Location
        if (!RewardManager.newEssence(x, y))
            fail();
        assertEquals(ID.LifeEssence, RewardManager.isReward(x, y));
        assertEquals(ID.Null, RewardManager.isReward(x, y));

        // On Contact Edge
        if (!RewardManager.newEssence(x, y))
            fail();
        assertEquals(ID.LifeEssence, RewardManager.isReward(x, y + essenceRadius));
        assertEquals(ID.Null, RewardManager.isReward(x, y));

        // Outside Perimeter
        if (!RewardManager.newEssence(x, y))
            fail();
        assertEquals(ID.Null, RewardManager.isReward(x, y + essenceRadius + 1));
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SPAWN ---------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testNewSpawn()
    {
        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();

        assertFalse(RewardManager.newEssence(-1, 0));
        assertTrue(RewardManager.newEssence(x, y));

        assertFalse(RewardManager.newElusive(0, -1));
        assertTrue(RewardManager.newElusive(x, y));
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @AfterEach
    public void reset()
    {
        pm.newGame();
        rm.newGame();
    }
}
