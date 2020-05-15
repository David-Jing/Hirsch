package com.group15.player;

import com.group15.enemy.EnemyManager;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.reward.RewardManager;

import java.awt.*;

/**
 * Manages, renders, and distributes information regarding the player character.
 */
public class PlayerManager
{
    private static Hirsch player = null;
    private static Map map = Map.getInstance();

    public PlayerManager() { newGame(); }

    /**
     * Reset player position on map.
     */
    public void newGame()
    {
        player = new Hirsch(map.getWidth() / 2 + 45, map.getHeight() / 2 - 40);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Teleports player to coordinate (x, y), cannot be teleported to a wall. If not all
     * essences are collected, cannot be teleported to an entrance.
     *
     * @param x x coordinate on map
     * @param y y coordinate on map
     * @return true if successfully teleported, false otherwise
     */
    public static boolean setPos(int x, int y)
    {
        if (player != null)
        {
            if (!map.isWall(x, y, player.getWidth(), player.getHeight()))
            {
                if (getCollectedEssence() < RewardManager.getMaxEssence())
                {
                    if (!map.isEntrance(x, y, player.getWidth(), player.getHeight()))
                    {
                        player.setX(x);
                        player.setY(y);
                        return true;
                    }
                    else
                        return false;
                }
                else
                {
                    player.setX(x);
                    player.setY(y);
                    return true;
                }
            }
        }
        return false;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Returns the health of the player object.
     *
     * @return health of the player or -1 if uninitialized
     */
    public static int getHealth()
    {
        if (player != null)
            return player.getHealth();
        return -1;
    }

    /**
     * Returns the number of currently collected life essences of the player object.
     *
     * @return number of life essence collected by the player or -1 if player is uninitialized
     */
    public static int getCollectedEssence()
    {
        if (player != null)
            return player.getCollectedEssence();
        return -1;
    }

    /**
     * Returns the number of currently collected elusive life essences of the player object.
     *
     * @return number of elusive life essence collected by the player or -1 if player is uninitialized
     */
    public static int getCollectedElusive()
    {
        if (player != null)
            return player.getCollectedElusive();
        return -1;
    }

    /**
     * Returns x position of player object.
     *
     * @return x coordinate of the player or -1 if player is uninitialized
     */
    public static int getPosX()
    {
        if (player != null)
            return player.getX();
        return -1;
    }

    /**
     * Returns y position of player object.
     *
     * @return y coordinate of the player or -1 if player is uninitialized
     */
    public static int getPosY()
    {
        if (player != null)
            return player.getY();
        return -1;
    }

    /**
     * Returns true if the player can be damaged, false otherwise.
     *
     * @return boolean indicating if the player can be damaged, if player is uninitialized it returns false
     */
    public static boolean canBeDamaged()
    {
        if (player != null)
            return player.canBeDamaged();
        return false;
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- CHECKERS --------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Check if current player position overlaps with contact radius of a reward.
     * If in contact, +1 health and +1 collection is added.
     */
    private void checkReward()
    {
        ID object = RewardManager.isReward(player.getX(), player.getY());

        if (object != ID.Null)
            if (object == ID.LifeEssence)
            {
                player.addCollectedEssence();
                player.addHealth(RewardManager.getEssenceHeal());
            }
            else if (object == ID.ElusiveLifeEssence)
            {
                player.addCollectedElusive();
                player.addHealth(RewardManager.getElusiveHeal());
            }
    }

    /**
     * Check if current player position overlaps with contact radius of a reward.
     * If in contact, +1 health and +1 collection is added.
     */
    private void checkEnemy()
    {
        ID object = EnemyManager.isEnemy(player.getX(), player.getY());

        if (object != ID.Null)
            if (object == ID.Trap)
                player.subHealth(EnemyManager.getTrapDamage());
            else if (object == ID.EvilSpirit)
                player.setDeath();
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Per frame logic of player interactions and itself.
     * Called by game.GameManager.
     */
    public void tick()
    {
        checkReward();
        checkEnemy();

        player.tick();
    }

    /**
     * Render player on screen.
     * Called by game.GameManager.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        player.render(g);
    }
}

