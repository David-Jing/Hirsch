package player;

import com.group15.enemy.EnemyManager;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerManagerTest
{
    private PlayerManager pm;
    private RewardManager rm;
    private EnemyManager em;

    private int timeOut = 10; // Units of seconds

    @BeforeEach
    public void init()
    {
        pm = new PlayerManager();
        rm = new RewardManager();
        em = new EnemyManager();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetHealth()
    {
        assertTrue(PlayerManager.getHealth() >= 0);
    }

    @Test
    public void testGetCollection()
    {
        assertEquals(0, PlayerManager.getCollectedEssence());
        assertEquals(0, PlayerManager.getCollectedElusive());
    }

    @Test
    public void testGetPosition()
    {
        assertTrue(PlayerManager.getPosX() >= 0);
        assertTrue(PlayerManager.getPosY() >= 0);
    }

    @Test
    public void testCanBeDamaged() throws InterruptedException
    {
        if (PlayerManager.canBeDamaged())
            assertTrue(true);
        else // If damage cooldown timer is triggered on spawn
        {
            int i;
            for (i = 0; i < timeOut; i++)
            {
                TimeUnit.SECONDS.sleep(1);

                if (PlayerManager.canBeDamaged())
                    break;
            }

            if (i == timeOut)
                fail();
        }
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- CHECKERS --------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testRewardInteraction()
    {
        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int currHealth = PlayerManager.getHealth();
        int currElusive = PlayerManager.getCollectedElusive();
        int currEssence = PlayerManager.getCollectedEssence();

        // ---------------------------------- GENERAL -------------------------------------

        // Normal life essence should not be spawned on top of player
        pm.tick();
        assertEquals(currHealth, PlayerManager.getHealth());
        assertEquals(currEssence, PlayerManager.getCollectedEssence());

        // -------------------------------- LIFE ESSENCE ----------------------------------

        if (!RewardManager.newEssence(x, y))
            fail();

        pm.tick();

        // Should heal and update score
        assertEquals(currHealth + RewardManager.getEssenceHeal(), PlayerManager.getHealth());
        assertEquals(currEssence + 1, PlayerManager.getCollectedEssence());

        // ----------------------------- ELUSIVE LIFE ESSENCE -----------------------------

        pm.newGame();
        rm.newGame();

        if (!RewardManager.newElusive(x, y))
            fail();

        pm.tick();

        // Should heal and update score
        assertEquals(currHealth + RewardManager.getElusiveHeal(), PlayerManager.getHealth());
        assertEquals(currElusive + 1, PlayerManager.getCollectedElusive());
    }

    @Test
    public void testEnemyInteraction() throws InterruptedException
    {
        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int currHealth = PlayerManager.getHealth();

        // ---------------------------------- GENERAL -------------------------------------

        // Traps and enemies should not be spawned on top of player
        pm.tick();
        assertEquals(currHealth, PlayerManager.getHealth());

        // -------------------------------- EVIL SPIRIT -----------------------------------

        if (!EnemyManager.newEvilSpirit(x, y))
            fail();

        // Wait out the damage cooldown timer
        while (!PlayerManager.canBeDamaged())
            TimeUnit.SECONDS.sleep(1);

        pm.tick();

        // Should kill player (i.e. set HP to 0)
        assertEquals(0, PlayerManager.getHealth());

        // ---------------------------------- TRAPS ---------------------------------------

        pm.newGame();
        em.newGame();

        if (!EnemyManager.newTrap(x, y))
            fail();

        // Wait out the damage cooldown timer
        while (!PlayerManager.canBeDamaged())
            TimeUnit.SECONDS.sleep(1);

        pm.tick();

        // Should damage player by the appropriate amount
        assertEquals(currHealth - EnemyManager.getTrapDamage(), PlayerManager.getHealth());
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @AfterEach
    public void reset()
    {
        pm.newGame();
        em.newGame();
        rm.newGame();
    }
}
