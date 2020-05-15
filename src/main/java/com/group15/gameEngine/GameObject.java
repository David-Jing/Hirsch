package com.group15.gameEngine;

import java.awt.*;

/**
 * Base game entity object that is used to construct all non-map game entities:
 * Evilspirit, Trap, Hirsch, ElusiveLifeEssence, and LifeEssence
 */
public abstract class GameObject
{
    protected ID id;
    protected int x, y;
    protected int velX, velY;

    protected int health;
    protected int speedX, speedY;
    protected int width, height;

    protected int contactRadius;

    public GameObject(int x, int y)
    {
        this.x = x;
        this.y = y;

        velY = 0;
        velX = 0;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public int getX() { return x; }

    public int getY() { return y; }

    public ID getID() { return id; }

    public int getVelX() { return velX; }

    public int getVelY() { return velY; }

    public int getSpeedX() { return speedX; }

    public int getSpeedY() { return speedY; }

    public int getHealth()
    {
        return health;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public int getContactRadius() { return contactRadius; }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setID(ID id)
    {
        this.id = id;
    }

    public void setVelX(int velX)
    {
        this.velX = velX;
    }

    public void setVelY(int velY)
    {
        this.velY = velY;
    }

    public void setSpeedX(int speedX)
    {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY)
    {
        this.speedY = speedY;
    }

    public void setHealth(int health) { this.health = health; }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setContactRadius(int contactRadius) { this.contactRadius = contactRadius; }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Per frame logic
     */
    public abstract void tick();

    /**
     * On screen visuals
     *
     * @param g passed down from game.Game
     */
    public abstract void render(Graphics g);

}
