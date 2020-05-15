package gameEngine;

import com.group15.enemy.EnemyManager;
import com.group15.gameEngine.GameManager;
import com.group15.gameEngine.ID;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest
{
    private GameManager gm;

    @BeforeEach
    public void init()
    {
        gm = new GameManager();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void checkGameStatus()
    {
        assertEquals(ID.Null, GameManager.getGameStatus());

        int x = PlayerManager.getPosX();
        int y = PlayerManager.getPosY();

        // Check initialization
        assertTrue(x >= 0);
        assertTrue(y >= 0);

        // ----------------------------------- DEFEAT -------------------------------------

        // Spawn enemy on top of player
        if (!EnemyManager.newEvilSpirit(x, y))
            fail();
        gm.tick();

        assertEquals(ID.Defeat, GameManager.getGameStatus());

        // ----------------------------------- VICTORY -------------------------------------

        GameManager.resetGame();
        assertEquals(ID.Null, GameManager.getGameStatus());

        // Spawn all required essence on top of player
        for (int i = 0; i < RewardManager.getMaxEssence(); i++)
        {
            if (!RewardManager.newEssence(x, y))
                fail();
            gm.tick();
        }
        // Teleport player to entrance
        if (!PlayerManager.setPos(505, 20))
            fail();
        gm.tick();

        assertEquals(ID.Victory, GameManager.getGameStatus());
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @AfterEach
    public void reset()
    {
        GameManager.resetGame();
    }
}
