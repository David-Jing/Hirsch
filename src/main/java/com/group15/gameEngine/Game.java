package com.group15.gameEngine;

import com.group15.map.Map;
import com.group15.overlay.Overlay;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Contains main(); the core of the game. Initializes the game and contains
 * frame and render logic.
 */
public class Game extends Canvas implements Runnable
{
    private Image hirschFront = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/hirsch_front.gif");

    private final Object lock = new Object();

    // FPS capping and tracking
    private static int currentFPS = 0;
    private static int maxFPS = 61;
    private static int targetTime = 1000000000 / maxFPS;
    private static boolean running = false;
    private Thread thread;

    private GameManager gm;
    private Map map;
    private Overlay overlay;

    /**
     * Initialize game components.
     */
    public Game()
    {
        // Get map
        map = Map.getInstance();

        // Initiates gameManager
        gm = new GameManager();

        // Initiate gameWindow
        new GameWindow(map.getWidth(), map.getHeight(), "Hirsch", this);

        // Initiates overlay
        overlay = new Overlay();
    }

    public static void main(String[] args)
    {
        new Game();
    }

    /**
     * Initiates the game, runs on a single thread.
     * Called by game.GameWindow()
     */
    public synchronized void start()
    {
        synchronized (lock)
        {
            thread = new Thread(this);
            thread.start();
            running = true;
        }
    }

    /**
     * Exits the game.
     * Called by game.GameWindow.
     */
    public synchronized void stop()
    {
        System.out.println("Exiting.");
        try
        {
            running = false;
            thread.join();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Implements frame to frame logic and fps.
     */
    public void run()
    {
        synchronized (lock)
        {
            long lastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;
            long timer = System.currentTimeMillis();
            int frames = 0;
            while (running)
            {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                while (delta >= 1)
                {
                    tick();
                    delta--;
                }
                if (running)
                    render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000)
                {
                    timer += 1000;
                    //System.out.println("FPS: " + frames);
                    currentFPS = frames;
                    frames = 0;
                }

                // FPS capping via sleeping
                long totalTime = System.nanoTime() - now;
                if (totalTime < targetTime)
                {
                    try
                    {
                        Thread.sleep((targetTime - totalTime) / 1000000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            stop();
        }
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Executes per frame game logic.
     * Root caller of tick().
     */
    private void tick()
    {
        if (GameManager.getGameStatus() == ID.Null)
        {
            gm.tick();
            overlay.tick();
        }
    }

    /**
     * Renders object on screen and switches from Game to Victory/Defeat screen.
     * Root caller of render().
     */
    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Render background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, map.getWidth(), map.getHeight());

        // Menu, Victory and Defeat screen management
        if (GameManager.getGameStatus() == ID.Null)
            gm.render(g);
        else if (GameManager.getGameStatus() == ID.Menu)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 20));
            g.drawString("SPACE to START", map.getWidth() / 2 - 100, map.getHeight() / 2 + 50);
            g.drawString("ESC to EXIT", map.getWidth() / 2 - 65, map.getHeight() / 2 + 70);

            g.setFont(new Font("Calibri", Font.BOLD, 90));
            g.setColor(Color.WHITE);
            g.drawString("HIRSCH", map.getWidth() / 2 - 250, map.getHeight() / 2 - 10);

            g.drawImage(hirschFront, map.getWidth() / 2 + 80, map.getHeight() / 2 - 70, 120, 135, null);
        }
        else
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.BOLD, 20));
            g.drawString("SPACE to CONTINUE", map.getWidth() / 2 - 80, map.getHeight() / 2 + 70);
            g.drawString("ESC to EXIT", map.getWidth() / 2 - 45, map.getHeight() / 2 + 90);

            g.setFont(new Font("Calibri", Font.BOLD, 90));
            if (GameManager.getGameStatus() == ID.Victory)
            {
                g.setColor(Color.YELLOW);
                g.drawString("VICTORY", map.getWidth() / 2 - 160, map.getHeight() / 2 - 30);
            }
            else if (GameManager.getGameStatus() == ID.Defeat)
            {
                g.setColor(Color.RED);
                g.drawString("YOU DIED", map.getWidth() / 2 - 164, map.getHeight() / 2 - 30);
            }
        }

        overlay.render(g);

        // Draw FPS label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.BOLD, 15));
        g.drawString(String.format("FPS: %d", currentFPS), 5, 11);

        g.dispose();
        bs.show();
    }
}
