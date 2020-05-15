package com.group15.map;

import com.group15.gameEngine.ID;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Manages and renders the on screen wall and entrance tiles.
 */
public class Map
{
    // Window size
    public static final int WIDTH = 1080, HEIGHT = 680;
    private static Map instance = null;

    File mapDiagram = new File("src/main/resources/map/map1.txt");

    private ID[][] map = new ID[WIDTH / 20][HEIGHT / 20];

    private Map()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(mapDiagram));

            int value;

            // parse txt file
            // '0' = ground, '1' = wall, '2' = entrance
            for (int i = 0; i < WIDTH / 20; i++)
                for (int j = 0; j < HEIGHT / 20; j++)
                {
                    if ((value = br.read()) != -1)
                    {
                        if ((char) value == '0')
                        {
                            map[i][j] = ID.Ground;
                        }
                        else if ((char) value == '1')
                        {
                            map[i][j] = ID.Wall;
                        }
                        else if ((char) value == '2')
                        {
                            map[i][j] = ID.Entrance;
                        }
                    }
                }
        }
        catch (IOException e) { System.out.println("ERROR: Map data not found."); }
    }

    /**
     * Achieves single structure type
     *
     * @return instance this class, initiates if uninitiated
     */
    public static Map getInstance()
    {
        if (instance == null)
            instance = new Map();
        return instance;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public int getWidth() { return WIDTH; }

    public int getHeight() { return HEIGHT; }

    // --------------------------------------------------------------------------------
    // ---------------------------------- CHECKERS ------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Checks if coordinate (x, y) is a wall tile.
     *
     * @param x x coordinate of interest
     * @param y y coordinate of interest
     * @return true if is a wall tile or is out of bound, false otherwise
     */
    public boolean isWall(int x, int y)
    {
        // Bad input
        if (x / 20 >= WIDTH / 20 || y / 20 >= HEIGHT / 20)
            return true;

        return map[x / 20][y / 20] == ID.Wall;
    }

    /**
     * Checks if an object of width and height overlaps with a wall tile.
     * Check for 16 points of contact.
     *
     * @param x      top-left x coordinate of object
     * @param y      top-left y coordinate of object
     * @param width  width of object
     * @param height height of object
     * @return true if there is overlap at any of 16 points, false otherwise
     */
    public boolean isWall(int x, int y, int width, int height)
    {
        width--;
        height--;

        int[] xCord = {x, x + width / 3, x + 2 * width / 3, x + width};
        int[] yCord = {y, y + height / 3, y + 2 * height / 3, y + height};

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (isWall(xCord[i], yCord[j]))
                    return true;

        return false;
    }

    /**
     * Checks if coordinate (x, y) is an entrance tile.
     *
     * @param x x coordinate of interest
     * @param y y coordinate of interest
     * @return true if is a entrance tile or is out of bound, false otherwise
     */
    public boolean isEntrance(int x, int y)
    {
        // Bad input
        if (x / 20 >= WIDTH / 20 || y / 20 >= HEIGHT / 20)
            return true;

        return map[x / 20][y / 20] == ID.Entrance;
    }

    /**
     * Checks if an object of width and height overlaps with a entrance tile.
     * Check for 16 points of contact.
     *
     * @param x      top-left x coordinate of object
     * @param y      top-left y coordinate of object
     * @param width  width of object
     * @param height height of object
     * @return true if there is overlap at any of 16 points, false otherwise
     */
    public boolean isEntrance(int x, int y, int width, int height)
    {
        width--;
        height--;

        int[] xCord = {x, x + width / 3, x + 2 * width / 3, x + width};
        int[] yCord = {y, y + height / 3, y + 2 * height / 3, y + height};

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (isEntrance(xCord[i], yCord[j]))
                    return true;

        return false;
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
    }

    /**
     * Renders the map on screen. Does nothing if ID == Ground, background rendered in Game().
     * Called by game.GameManager.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        for (int i = 0; i < WIDTH; i += 20)
            for (int j = 0; j < HEIGHT; j += 20)
                if (isWall(i, j))
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(i, j, 20, 20);
                }
                else if (isEntrance(i, j))
                {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(i, j, 20, 20);
                }
    }

}
