package enemy;

import com.group15.enemy.EnemyManager;
import com.group15.enemy.EvilSpirit;
import com.group15.enemy.Trap;
import com.group15.gameEngine.ID;
import com.group15.player.PlayerManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyManagerTest
{
    private EnemyManager em;
    private PlayerManager pm;
    private int searchTimeLimit = 3; // Units of seconds

    @BeforeEach
    public void init()
    {
        pm = new PlayerManager();
        em = new EnemyManager();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetTrapDamage()
    {
        assertTrue(EnemyManager.getTrapDamage() >= 0);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- MOVEMENT -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testMahanMove() throws InterruptedException
    {
        EvilSpirit spirit = new EvilSpirit(0, 0);

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int spiritRadius = spirit.getContactRadius();

        // Enemy on corner
        em.newGame();
        if (!EnemyManager.newEvilSpirit(x + spiritRadius, y - spiritRadius / 3))
            fail();
        em.tick();
        assertEquals(0, EnemyManager.isEvilSpirit(x, y));
        TimeUnit.SECONDS.sleep(searchTimeLimit);
        em.tick();
        assertEquals(1, EnemyManager.isEvilSpirit(x, y));

        // Enemy on bottom side
        em.newGame();
        if (!EnemyManager.newEvilSpirit(x, y + spiritRadius + 1))
            fail();
        assertEquals(0, EnemyManager.isEvilSpirit(x, y));
        TimeUnit.SECONDS.sleep(searchTimeLimit);
        em.tick();
        assertEquals(1, EnemyManager.isEvilSpirit(x, y));
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- COLLISIONS -----------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testIsEnemy() throws InterruptedException
    {
        EvilSpirit spirit = new EvilSpirit(0, 0);
        Trap trap = new Trap(0, 0);

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int spiritRadius = spirit.getContactRadius();
        int trapRadius = trap.getContactRadius();

        while (!PlayerManager.canBeDamaged())
            Thread.sleep(500);

        // --------------------------------- EVIL SPIRIT ----------------------------------

        em.newGame();
        if (!EnemyManager.newEvilSpirit(x, y))
            fail();

        // On Location
        assertEquals(ID.EvilSpirit, EnemyManager.isEnemy(x, y));

        // On Contact Edge
        assertEquals(ID.EvilSpirit, EnemyManager.isEnemy(x + spiritRadius, y));

        // Outside Perimeter
        assertEquals(ID.Null, EnemyManager.isEnemy(x + spiritRadius + 1, y));

        // ----------------------------------- TRAP ---------------------------------------

        em.newGame();
        if (!EnemyManager.newTrap(x, y))
            fail();

        // On Location
        assertEquals(ID.Trap, EnemyManager.isEnemy(x, y));

        // On Contact Edge
        assertEquals(ID.Trap, EnemyManager.isEnemy(x, y + trapRadius));

        // Outside Perimeter
        assertEquals(ID.Null, EnemyManager.isEnemy(x, y + trapRadius + 1));
    }

    @Test
    public void testIsEvilSpirit()
    {
        EvilSpirit spirit = new EvilSpirit(0, 0);

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();
        int spiritRadius = spirit.getContactRadius();

        em.newGame();
        assertEquals(0, EnemyManager.isEvilSpirit(x, y));

        // On Location
        if (!EnemyManager.newEvilSpirit(x, y))
            fail();
        assertEquals(1, EnemyManager.isEvilSpirit(x, y));

        // On Contact Edge
        if (!EnemyManager.newEvilSpirit(x, y + spiritRadius))
            fail();
        assertEquals(2, EnemyManager.isEvilSpirit(x, y));

        // Outside Perimeter
        if (!EnemyManager.newEvilSpirit(x, y - spiritRadius - 1))
            fail();
        assertEquals(2, EnemyManager.isEvilSpirit(x, y));
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SPAWN ---------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testNewSpawn()
    {
        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();

        assertFalse(EnemyManager.newTrap(-1, 0));
        assertTrue(EnemyManager.newTrap(x, y));

        assertFalse(EnemyManager.newEvilSpirit(0, -1));
        assertTrue(EnemyManager.newEvilSpirit(x, y));
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @AfterEach
    public void reset()
    {
        pm.newGame();
        em.newGame();
    }
}
