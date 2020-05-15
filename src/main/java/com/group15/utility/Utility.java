package com.group15.utility;

/**
 * Commonly used mathematical calculations.
 */
public class Utility
{
    /**
     * Calculates the straight line distance between two coordinates (x, y).
     *
     * @param x1 x position of object1
     * @param y1 y position of object1
     * @param x2 x position of object2
     * @param y2 y position of object2
     * @return shortest distance between the two points
     */
    public static int distance(int x1, int y1, int x2, int y2)
    {
        int xComp = (int) Math.pow(Math.abs(x2 - x1), 2);
        int yComp = (int) Math.pow(Math.abs(y2 - y1), 2);

        return (int) Math.sqrt(xComp + yComp);
    }

    /**
     * Given an interval and an initial time, is it pass interval + initial?
     *
     * @param previousTime initial time (units of ms)
     * @param interval     interval time (units of s)
     * @return true if elapsed is greater or equal to interval
     */
    public static boolean haveElapsed(long previousTime, float interval)
    {
        float elapsed = (System.currentTimeMillis() - previousTime) / 1000f;
        return (elapsed >= interval);
    }

    /**
     * Calculates the distance between two points measured along axes at right angles.
     *
     * @param x1 x position of object1
     * @param y1 y position of object1
     * @param x2 x position of object2
     * @param y2 y position of object2
     * @return distance between the two point
     */
    public static int manhattanDistance(int x1, int y1, int x2, int y2)
    {
        int distanceX = Math.abs(x2 - x1);
        int distanceY = Math.abs(y2 - y1);
        return (distanceX + distanceY);
    }

    /**
     * Unit conversion from gain to decibel.
     *
     * @param gain gain value that is to be converted
     * @return decibel equivalent to the inputted gain
     */
    public static float gain2db(float gain)
    {
        return (float) (Math.log(gain) / Math.log(10f) * 20f);
    }

}
