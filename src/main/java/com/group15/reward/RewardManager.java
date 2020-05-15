package com.group15.reward;

import com.group15.audio.Audio;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.utility.Utility;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Manages and renders the normal and bonus rewards; deals with player collisions.
 */
public class RewardManager
{
    private final static int maxEssence = 6;

    private static LinkedList<LifeEssence> lifeEssence = new LinkedList<>();
    private static LinkedList<ElusiveLifeEssence> elusiveLifeEssence = new LinkedList<>();

    private final double elusiveRate = 0.01;
    private final int maxElusive = 1;

    private Random random = new Random();

    private static int elusiveWidth = -1, elusiveHeight = -1;
    private static int essenceWidth = -1, essenceHeight = -1;

    private static Map map = Map.getInstance();
    private int width = map.getWidth(), height = map.getHeight();

    private static ElusiveLifeEssence elusiveStatCheck = null;
    private static LifeEssence essenceStatCheck = null;

    public RewardManager()
    {
        elusiveStatCheck = new ElusiveLifeEssence(0, 0);
        elusiveWidth = elusiveStatCheck.getWidth();
        elusiveHeight = elusiveStatCheck.getHeight();

        essenceStatCheck = new LifeEssence(0, 0);
        essenceWidth = essenceStatCheck.getWidth();
        essenceHeight = essenceStatCheck.getHeight();

        newGame();
    }

    /**
     * Remove all previously spawned and then spawn new set of rewards.
     */
    public void newGame()
    {
        lifeEssence.clear();
        elusiveLifeEssence.clear();

        newEssence(960, 48);
        newEssence(980, 570);
        newEssence(980, 320);
        newEssence(50, 55);
        newEssence(357, 465);
        newEssence(410, 60);
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SPAWN ---------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Tries to spawns a life essence object at position (x, y)
     *
     * @param x x coordinate in game window
     * @param y y coordinate in game window
     * @return true if successfully spawned, false otherwise
     */
    public static boolean newEssence(int x, int y)
    {
        // If uninitialized
        if (essenceWidth == -1 || essenceHeight == -1)
            return false;

        if (!map.isWall(x, y, essenceWidth, essenceHeight))
        {
            lifeEssence.add(new LifeEssence(x, y));
            return true;
        }

        return false;
    }

    /**
     * Tries to spawn a elusive life essence object at position (x, y)
     *
     * @param x x coordinate in game window
     * @param y y coordinate in game window
     * @return true if successfully spawned, false otherwise
     */
    public static boolean newElusive(int x, int y)
    {
        // If uninitialized
        if (elusiveWidth == -1 || elusiveHeight == -1)
            return false;

        if (!map.isWall(x, y, elusiveWidth, elusiveHeight))
        {
            elusiveLifeEssence.add(new ElusiveLifeEssence(x, y));
            return true;
        }

        return false;
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- COLLISIONS ------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Checks if the coordinate (x, y) is in contact radius with any of the
     * reward objects. If a reward is in contact radius, the its ID is returned and
     * is immediately removed from the map. If no object is in contact, it returns ID.Null.
     *
     * @param x x position of reference
     * @param y y position of reference
     * @return ID.LifeEssence, ID.ElusiveLifeEssence, or ID.Null if in contact with
     * life essence, elusive life essence, or nothing, respectively.
     */
    public static ID isReward(int x, int y)
    {
        for (LifeEssence gameObject : lifeEssence)
        {
            int distance = Utility.distance(x, y, gameObject.getX(), gameObject.getY());

            if (distance <= gameObject.getContactRadius())
            {
                Audio.playEffect(ID.LifeEssence, 0.7f);
                lifeEssence.remove(gameObject);
                return ID.LifeEssence;
            }
        }

        for (ElusiveLifeEssence gameObject : elusiveLifeEssence)
        {
            int distance = Utility.distance(x, y, gameObject.getX(), gameObject.getY());

            if (distance <= gameObject.getContactRadius())
            {
                Audio.playEffect(ID.ElusiveLifeEssence, 0.5f);
                elusiveLifeEssence.remove(gameObject);
                return ID.ElusiveLifeEssence;
            }
        }

        return ID.Null;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * The total number of essences spawned on the map.
     *
     * @return a positive number.
     */
    public static int getMaxEssence() { return maxEssence; }

    /**
     * Returns the healing amount of elusive life essences when picked up.
     *
     * @return a positive heal value, default to 0 if uninitialized.
     */
    public static int getElusiveHeal()
    {
        if (elusiveStatCheck != null)
            return elusiveStatCheck.getHeal();
        return 0;
    }

    /**
     * Returns the healing amount of life essences when picked up.
     *
     * @return a positive heal value, default to 0 if uninitialized.
     */
    public static int getEssenceHeal()
    {
        if (essenceStatCheck != null)
            return essenceStatCheck.getHeal();
        return 0;
    }

    // --------------------------------------------------------------------------------
    // -------------------------------- ELUSIVE LOGIC ---------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Trying to RNG spawn an elusive life essence on a valid position on the map.
     */
    private void tryElusiveSpawn()
    {
        double randomGenerator = random.nextDouble();
        if (randomGenerator <= elusiveRate && elusiveLifeEssence.size() < maxElusive)
        {
            int randomX;
            int randomY;

            do
            {
                // -20 is needed due to 1:20 map conversion, or we get out of bound error.
                randomX = random.nextInt(width - 20);
                randomY = random.nextInt(height - 20);

            } while (!newElusive(randomX, randomY));
        }
    }

    /**
     * Checks each active elusive life essence object, delete them
     * from map if they have exceeded their lifespan.
     */
    private void checkElusiveLife()
    {
        elusiveLifeEssence.removeIf(ElusiveLifeEssence::isDead);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Per frame logic of life essences and elusive life essences interactions and themselves.
     * Called by game.GameManager.
     */
    public void tick()
    {
        tryElusiveSpawn();
        checkElusiveLife();

        for (LifeEssence gameObject : lifeEssence) gameObject.tick();
        for (ElusiveLifeEssence gameObject : elusiveLifeEssence) gameObject.tick();
    }

    /**
     * Renders life essences and elusive life essences on screen.
     * Called by game.GameManager.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        for (LifeEssence gameObject : lifeEssence) gameObject.render(g);
        for (ElusiveLifeEssence gameObject : elusiveLifeEssence) gameObject.render(g);
    }
}
