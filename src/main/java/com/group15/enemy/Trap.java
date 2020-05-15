package com.group15.enemy;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;

import java.awt.*;

/**
 * Manages and stores the stats of a trap game entities.
 */
public class Trap extends GameObject
{
    private final int damage = 1;
    private Image trapPic = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/trap.gif");

    public Trap(int x, int y)
    {
        super(x, y);
        id = ID.Trap;

        speedX = 0;
        speedY = 0;
        health = 1;

        width = 60;
        height = 60;

        contactRadius = width / 2 + 30;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Gets the damaged value that would be inflected to the object that steps on this.
     *
     * @return positive damage value.
     */
    public int getDamage() { return damage; }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @Override
    public void tick()
    {
        // Traps should not move
    }

    @Override
    public void render(Graphics g)
    {
        g.drawImage(trapPic, x, y, width, height, null);
    }

}
