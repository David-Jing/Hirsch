package com.group15.reward;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;
import com.group15.utility.Utility;

import java.awt.*;

/**
 * Manages and stores the stats of an elusive life essence game entities.
 */
public class ElusiveLifeEssence extends GameObject
{
    private final int heal = 1;
    private long spawnTime;
    private long spawnDuration;

    private Image elusive = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/elusive.gif");

    public ElusiveLifeEssence(int x, int y)
    {
        super(x, y);
        id = ID.ElusiveLifeEssence;

        speedX = 0;
        speedY = 0;
        health = 1;

        width = 40;
        height = 41;

        spawnDuration = 3; // Units in seconds
        spawnTime = System.currentTimeMillis();

        contactRadius = width / 2 + 30;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Check if the object has reached or exceeded its lifespan
     *
     * @return true if this object has exceeded set time, false otherwise
     */
    public boolean isDead() { return Utility.haveElapsed(spawnTime, spawnDuration); }

    /**
     * Gets the heal value that would be healed to the object that steps on this.
     *
     * @return positive heal value.
     */
    public int getHeal() { return heal; }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @Override
    public void tick()
    {
        // Rewards should not move
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(elusive, x, y, width, height, null);
    }
}
