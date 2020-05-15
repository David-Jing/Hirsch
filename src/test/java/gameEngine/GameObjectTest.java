package gameEngine;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameObjectTest
{
    private GameObject obj;
    private Random r = new Random();
    private int test;

    @BeforeEach
    public void init()
    {
        obj = new GameObject(10, 15)
        {
            @Override public void tick() { }

            @Override public void render(Graphics g) { }
        };
    }

    @Test
    public void testStat()
    {
        assertEquals(0, obj.getVelX());
        assertEquals(0, obj.getVelY());
    }

    @Test
    public void testPosition()
    {
        // Initial value
        assertEquals(10, obj.getX());
        assertEquals(15, obj.getY());

        // X
        test = r.nextInt();
        obj.setX(test);
        assertEquals(test, obj.getX());

        // Y
        test = r.nextInt();
        obj.setY(test);
        assertEquals(test, obj.getY());
    }

    @Test
    public void testID()
    {
        obj.setID(ID.Trap);
        assertEquals(ID.Trap, obj.getID());

        obj.setID(ID.Hirsch);
        assertEquals(ID.Hirsch, obj.getID());

        obj.setID(ID.LifeEssence);
        assertEquals(ID.LifeEssence, obj.getID());

        obj.setID(ID.ElusiveLifeEssence);
        assertEquals(ID.ElusiveLifeEssence, obj.getID());

        obj.setID(ID.EvilSpirit);
        assertEquals(ID.EvilSpirit, obj.getID());
    }

    @Test
    public void testVel()
    {
        // Initial value
        assertEquals(0, obj.getVelX());
        assertEquals(0, obj.getVelY());

        // X
        test = r.nextInt();
        obj.setVelX(test);
        assertEquals(test, obj.getVelX());

        // Y
        test = r.nextInt();
        obj.setVelY(test);
        assertEquals(test, obj.getVelY());
    }

    @Test
    public void testSpeed()
    {
        // X
        test = r.nextInt();
        obj.setSpeedX(test);
        assertEquals(test, obj.getSpeedX());

        // Y
        test = r.nextInt();
        obj.setSpeedY(test);
        assertEquals(test, obj.getSpeedY());
    }

    @Test
    public void testHealth()
    {
        test = r.nextInt();
        obj.setHealth(test);
        assertEquals(test, obj.getHealth());
    }

    @Test
    public void testSize()
    {
        // Width
        test = r.nextInt();
        obj.setWidth(test);
        assertEquals(test, obj.getWidth());

        // Height
        test = r.nextInt();
        obj.setHeight(test);
        assertEquals(test, obj.getHeight());
    }

    @Test
    public void testContactRadius()
    {
        test = r.nextInt();
        obj.setContactRadius(test);
        assertEquals(test, obj.getContactRadius());
    }
}
