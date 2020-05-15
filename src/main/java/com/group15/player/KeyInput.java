package com.group15.player;

import com.group15.gameEngine.GameManager;
import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Takes in user key inputs and executes movement commands on a GameObject entity.
 */
public class KeyInput extends KeyAdapter
{
    // Removes delay when pressing opposite keys together
    private boolean uP = false;
    private boolean dP = false;
    private boolean lP = false;
    private boolean rP = false;

    private GameObject character;

    public KeyInput(GameObject character)
    {
        this.character = character;

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (KeyEvent.KEY_PRESSED == e.getID())
                        keyPressed(e);

                    else if (KeyEvent.KEY_RELEASED == e.getID())
                        keyReleased(e);

                    return false;
                });
    }

    /**
     * Called by a key listener, checks key press and execute its respective function.
     *
     * @param e keyEvent
     */
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        // --------------------------------------------------------------------------------
        // -------------------------------- MOVEMENT --------------------------------------
        // --------------------------------------------------------------------------------

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
        {
            uP = true;
            character.setVelY(-character.getSpeedY());
        }
        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
        {
            dP = true;
            character.setVelY(character.getSpeedY());
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
        {
            lP = true;
            character.setVelX(-character.getSpeedX());
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
        {
            rP = true;
            character.setVelX(character.getSpeedX());
        }

        // --------------------------------------------------------------------------------
        // ----------------------------- GAME COMMAND -------------------------------------
        // --------------------------------------------------------------------------------

        // If you are not in game playing
        if (GameManager.getGameStatus() != ID.Null)
        {
            if (key == KeyEvent.VK_SPACE)
                GameManager.resetGame();
            else if (key == KeyEvent.VK_ESCAPE)
                GameManager.exitGame();
        }
    }

    /**
     * Called by a key listener, checks key release and execute its respective function.
     *
     * @param e keyEvent
     */
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();

        // --------------------------------------------------------------------------------
        // -------------------------------- MOVEMENT --------------------------------------
        // --------------------------------------------------------------------------------

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W)
        {
            uP = false;
            if (dP) character.setVelY(character.getSpeedY());
            else character.setVelY(0);
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)
        {
            dP = false;
            if (uP) character.setVelY(-character.getSpeedY());
            else character.setVelY(0);
        }

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
        {
            lP = false;
            if (rP) character.setVelX(character.getSpeedX());
            else character.setVelX(0);
        }

        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)
        {
            rP = false;
            if (lP) character.setVelX(-character.getSpeedX());
            else character.setVelX(0);
        }
    }
}
