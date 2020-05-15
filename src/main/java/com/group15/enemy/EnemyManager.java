package com.group15.enemy;

import com.group15.audio.Audio;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.player.PlayerManager;
import com.group15.utility.Utility;

import java.awt.*;
import java.util.LinkedList;

/**
 * Manages and renders the traps and evil spirits; deals with player collisions.
 */
public class EnemyManager
{
    private static LinkedList<EvilSpirit> evilSpirit = new LinkedList<>();
    private static LinkedList<Trap> traps = new LinkedList<>();

    private float searchCooldown = 0.5f; // Units of seconds
    private long lastSearchTime = System.currentTimeMillis();

    private static int evilSpiritWidth = -1, evilSpiritHeight = -1;
    private static int trapWidth = -1, trapHeight = -1;

    private static Map map = Map.getInstance();

    public EnemyManager()
    {
        EvilSpirit spiritStatCheck = new EvilSpirit(0, 0);
        evilSpiritWidth = spiritStatCheck.getWidth();
        evilSpiritHeight = spiritStatCheck.getHeight();

        Trap trapStatCheck = new Trap(0, 0);
        trapWidth = trapStatCheck.getWidth();
        trapHeight = trapStatCheck.getHeight();

        newGame();
    }

    /**
     * Reset enemy positions on map.
     */
    public void newGame()
    {
        evilSpirit.clear();
        traps.clear();

        newEvilSpirit(150, 45);
        newEvilSpirit(45, 350);
        newTrap(50, 545);
        newTrap(760, 370);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SPAWN ---------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Tries to spawns a evil spirit object at position (x, y)
     *
     * @param x x coordinate in game window
     * @param y y coordinate in game window
     * @return true if successfully spawned, false otherwise
     */
    public static boolean newEvilSpirit(int x, int y)
    {
        // If uninitialized
        if (evilSpiritWidth == -1 || evilSpiritHeight == -1)
            return false;

        if (!map.isWall(x, y, evilSpiritWidth, evilSpiritHeight))
        {
            evilSpirit.add(new EvilSpirit(x, y));
            return true;
        }

        return false;
    }

    /**
     * Tries to spawn a trap object at position (x, y)
     *
     * @param x x coordinate in game window
     * @param y y coordinate in game window
     * @return true if successfully spawned, false otherwise
     */
    public static boolean newTrap(int x, int y)
    {
        // If uninitialized
        if (trapWidth == -1 || trapHeight == -1)
            return false;

        if (!map.isWall(x, y, trapWidth, trapHeight))
        {
            traps.add(new Trap(x, y));
            return true;
        }

        return false;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Returns the damage the traps should inflict.
     *
     * @return a positive damage value, default to 0 if traps are uninitialized.
     */
    public static int getTrapDamage()
    {
        if (!traps.isEmpty())
            return traps.getFirst().getDamage();
        return 0;
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- COLLISIONS -----------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Checks if the coordinate (x, y) is in contact radius with any of the
     * enemy objects. If an enemy is in contact radius, the its ID is returned.
     * If no object is in contact, it returns ID.Null.
     *
     * @param x x position of reference
     * @param y y position of reference
     * @return ID.LifeEssence, ID.ElusiveLifeEssence, or ID.Null if in contact with
     * life essence, elusive life essence, or nothing, respectively.
     */
    public static ID isEnemy(int x, int y)
    {
        for (EvilSpirit gameObject : evilSpirit)
        {
            int distance = Utility.distance(x, y, gameObject.getX(), gameObject.getY());

            if (distance <= gameObject.getContactRadius())
            {
                Audio.playEffect(ID.EvilSpirit, 0.5f);
                return ID.EvilSpirit;
            }
        }

        if (!PlayerManager.canBeDamaged())
            return ID.Null;

        for (Trap gameObject : traps)
        {
            int distance = Utility.distance(x, y, gameObject.getX(), gameObject.getY());

            if (distance <= gameObject.getContactRadius())
            {
                Audio.playEffect(ID.Trap, 0.5f);
                return ID.Trap;
            }
        }

        return ID.Null;
    }

    /**
     * Returns the number of evil spirits that is in contact radius
     * to coordinate (x, y)
     *
     * @param x top-left x coordinate of object
     * @param y top-left y coordinate of object
     * @return number of evil spirit in contact
     */
    public static int isEvilSpirit(int x, int y)
    {
        int count = 0;

        for (EvilSpirit gameObject : evilSpirit)
        {
            int distance = Utility.distance(x, y, gameObject.getX(), gameObject.getY());

            if (distance <= gameObject.getContactRadius())
                count++;
        }

        return count;
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- MOVEMENT -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Move of evil spirits via Manhattan distance heuristic
     */
    private void mahanMove()
    {
        if (!Utility.haveElapsed(lastSearchTime, searchCooldown))
            return;

        lastSearchTime = System.currentTimeMillis();

        int xRef = PlayerManager.getPosX();
        int yRef = PlayerManager.getPosY();

        // Player is uninitialized
        if (xRef == -1 || yRef == -1)
            return;

        for (EvilSpirit gameObject : evilSpirit)
        {
            int x = gameObject.getX();
            int y = gameObject.getY();
            int speedX = gameObject.getSpeedX();
            int speedY = gameObject.getSpeedY();

            int up = Utility.manhattanDistance(x, y - speedY, xRef, yRef);
            int down = Utility.manhattanDistance(x, y + speedY, xRef, yRef);
            int left = Utility.manhattanDistance(x - speedX, y, xRef, yRef);
            int right = Utility.manhattanDistance(x + speedX, y, xRef, yRef);

            if (up > down)
                gameObject.setVelY(gameObject.getSpeedY());
            else
                gameObject.setVelY(-gameObject.getSpeedY());

            if (left > right)
                gameObject.setVelX(gameObject.getSpeedX());
            else
                gameObject.setVelX(-gameObject.getSpeedX());
        }
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Per frame logic of evil spirit and traps interactions and themselves.
     * Called by game.GameManager.
     */
    public void tick()
    {
        mahanMove();

        for (Trap trap : traps) trap.tick();
        for (EvilSpirit gameObject : evilSpirit) gameObject.tick();
    }

    /**
     * Renders evil spirit and traps on screen.
     * Called by game.GameManager.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        // Enemies should render on top of traps
        for (Trap trap : traps) trap.render(g);
        for (EvilSpirit gameObject : evilSpirit) gameObject.render(g);
    }
}
