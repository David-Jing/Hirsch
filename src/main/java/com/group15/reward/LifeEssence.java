package com.group15.reward;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;

import java.awt.*;

/**
 * Manages and stores the stats of a life essence game entities.
 */
public class LifeEssence extends GameObject
{
    private final int heal = 1;
    private Image essence = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/essence.gif");

    public LifeEssence(int x, int y)
    {
        super(x, y);
        id = ID.LifeEssence;

        speedX = 0;
        speedY = 0;
        health = 1;

        width = 40;
        height = 41;

        contactRadius = width / 2 + 30;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

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
        g.drawImage(essence, x, y, width, height, null);
    }
}
