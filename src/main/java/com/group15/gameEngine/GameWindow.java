package com.group15.gameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Initializes the game window and the various window logic.
 */
public class GameWindow extends Canvas
{
    private static Game game = null;
    private static JFrame frame;

    public GameWindow(int width, int height, String title, Game game)
    {
        GameWindow.game = game;
        frame = new JFrame(title);

        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
