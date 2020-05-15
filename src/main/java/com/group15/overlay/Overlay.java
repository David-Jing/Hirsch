package com.group15.overlay;

import com.group15.gameEngine.GameManager;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;

import javax.swing.*;
import java.awt.*;

/**
 * Manages and renders the on screen overlay of stats regarding the player character.
 */
public class Overlay extends JPanel
{
    Image heartPic = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/heart.gif");
    Image essencePic = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/essence.gif");
    Image elusivePic = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/elusive.gif");

    private int health = 0;
    private int essence = 0;
    private int elusive = 0;

    private Map map = Map.getInstance();

    // --------------------------------------------------------------------------------
    // ------------------------------- OVERLAY TYPE -----------------------------------
    // --------------------------------------------------------------------------------

    /**
     * In game screen overlay to display current status of health and collected rewards.
     *
     * @param g graphics
     */
    private void gameOverlay(Graphics g)
    {
        for (int i = 0; i < health; i++)
            g.drawImage(heartPic, 1030 - (30 * i), 10, null);

        g.setFont(new Font("Calibri", Font.BOLD, 15));

        g.setColor(Color.GREEN);
        g.drawImage(essencePic, 730, 10, null);
        g.drawString(String.format("%d / %d", essence, RewardManager.getMaxEssence()), 765, 14);

        g.setColor(Color.YELLOW);
        g.drawImage(elusivePic, 675, 10, null);
        g.drawString(String.format("%d", elusive), 705, 14);
    }

    /**
     * Defeat or victory screen overlay to display score of collected rewards.
     *
     * @param g graphics
     */
    private void scoreOverlay(Graphics g)
    {
        g.setFont(new Font("Calibri", Font.BOLD, 30));
        int height = map.getHeight() / 2 + 200;

        g.setColor(Color.GREEN);
        g.drawImage(essencePic, map.getWidth() / 2 - 160, height, 54, 52, null);
        g.drawString(String.format("%d / %d", essence, RewardManager.getMaxEssence()), map.getWidth() / 2 - 95,
                height + 40);

        g.setColor(Color.YELLOW);
        g.drawImage(elusivePic, map.getWidth() / 2 + 100, height, 54, 52, null);
        g.drawString(String.format("%d", elusive), map.getWidth() / 2 + 170, height + 40);
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Refreshes value for the displayed stats.
     * Called by game.Game.
     */
    public void tick()
    {
        health = PlayerManager.getHealth();
        essence = PlayerManager.getCollectedEssence();
        elusive = PlayerManager.getCollectedElusive();
    }

    /**
     * Renders the stats on screen.
     * Called by game.Game.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        if (GameManager.getGameStatus() == ID.Null)
            gameOverlay(g);
        else if (GameManager.getGameStatus() == ID.Victory || GameManager.getGameStatus() == ID.Defeat)
            scoreOverlay(g);
    }
}
