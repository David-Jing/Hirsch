package com.group15.player;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.reward.RewardManager;
import com.group15.utility.Utility;

import java.awt.*;

/**
 * Manages and stores the stats of a player character game entities.
 */
public class Hirsch extends GameObject
{
    private Map map = Map.getInstance();

    private Image hirschFront = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/hirsch_front.gif");
    private Image hirschBack = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/hirsch_back.gif");
    private Image hirschRight = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/hirsch_right.gif");
    private Image hirschLeft = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/hirsch_left.gif");

    private int essence, elusive;
    private int maxHealth;
    private float damageCoolDown;
    private long lastDamagedTime = System.currentTimeMillis();

    private boolean faceFront = true;
    private boolean faceRight = false;
    private boolean side = false;

    public Hirsch(int x, int y)
    {
        super(x, y);
        id = ID.Hirsch;

        essence = 0;
        elusive = 0;

        speedX = 5;
        speedY = 5;
        health = 2;

        width = 50;
        height = 56;

        contactRadius = width / 2 + 30;
        maxHealth = 8;

        damageCoolDown = 0.5f; // Units in seconds

        new KeyInput(this); // Initialize keyboard control
    }
    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public int getCollectedEssence()
    {
        return essence;
    }

    public int getCollectedElusive()
    {
        return elusive;
    }

    public int getMaxHealth() { return maxHealth; }

    public float getDamageCoolDown() { return damageCoolDown; }

    // When Hirsch gets damaged, he cannot receive damage once again within a short while.
    public boolean canBeDamaged() { return Utility.haveElapsed(lastDamagedTime, damageCoolDown); }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public void setDeath()
    {
        health = 0;
        lastDamagedTime = System.currentTimeMillis();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- ADDERS --------------------------------------
    // --------------------------------------------------------------------------------

    public void addCollectedEssence() { this.essence++; }

    public void addCollectedElusive() { this.elusive++; }

    public void addHealth(int amount)
    {
        health += amount;
        if (health > maxHealth)
            this.health = maxHealth;
    }

    public void subHealth(int damage)
    {
        health -= damage;
        lastDamagedTime = System.currentTimeMillis();
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- MOVEMENT ------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Movement logic
     */
    private void move()
    {
        // Cannot pass through entrance
        if (essence < RewardManager.getMaxEssence())
        {
            if (velX != 0)
                if (!map.isWall(x + velX, y, width, height) && !map.isEntrance(x + velX, y, width, height))
                    x += velX;

            if (velY != 0)
                if (!map.isWall(x, y + velY, width, height) && !map.isEntrance(x, y + velY, width, height))
                    y += velY;
        }
        // Can pass through entrance
        else
        {
            if (velX != 0)
                if (!map.isWall(x + velX, y, width, height))
                    x += velX;

            if (velY != 0)
                if (!map.isWall(x, y + velY, width, height))
                    y += velY;
        }
    }

    // -------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @Override
    public void tick() { move(); }

    @Override
    public void render(Graphics g)
    {
        // Determining which direction Hirsch should be rendered
        if (velY != 0 || velX != 0)
        {
            if (velX != 0)
            {
                side = true;

                if (velX > 0)
                {
                    faceRight = true;
                    g.drawImage(hirschRight, x, y, width, height, null);
                }
                else
                {
                    faceRight = false;
                    g.drawImage(hirschLeft, x, y, width, height, null);
                }
            }
            else
            {
                side = false;

                if (velY > 0)
                {
                    faceFront = true;
                    g.drawImage(hirschFront, x, y, width, height, null);
                }
                else
                {
                    faceFront = false;
                    g.drawImage(hirschBack, x, y, width, height, null);
                }
            }

        }
        else
        {
            if (side)
            {
                if (faceRight)
                    g.drawImage(hirschRight, x, y, width, height, null);
                else
                    g.drawImage(hirschLeft, x, y, width, height, null);
            }
            else if (faceFront)
                g.drawImage(hirschFront, x, y, width, height, null);
            else
                g.drawImage(hirschBack, x, y, width, height, null);
        }
    }
}
