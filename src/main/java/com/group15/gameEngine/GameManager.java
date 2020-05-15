package com.group15.gameEngine;

import com.group15.audio.Audio;
import com.group15.enemy.EnemyManager;
import com.group15.map.Map;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;

import java.awt.*;

/**
 * Initialize and updates the controller classes for each entity group:
 * Map, Enemy, Reward, and Player.
 * Manages bgm music selection and checks for win/lose conditions.
 */
public class GameManager
{
    private static EnemyManager em;
    private static RewardManager rm;
    private static PlayerManager pm;
    private static Audio audio;

    private Map map;

    private ID currMusic = ID.Null;
    private static ID gameStatus = ID.Menu;

    public GameManager()
    {
        // Initiate managers
        em = new EnemyManager();
        rm = new RewardManager();
        pm = new PlayerManager();
        map = Map.getInstance();

        // Initiate sound and bgm
        audio = new Audio();
    }

    // --------------------------------------------------------------------------------
    // ------------------------------------ BGM ---------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Attempt to change bgm base on health percentage of player.
     */
    private void changeBGM()
    {
        // Player is uninitialized
        if (PlayerManager.getHealth() == -1)
            return;

        if (PlayerManager.getHealth() <= 0)
        {
            if (currMusic != ID.IntenseBGM)
            {
                Audio.loopBGM(ID.IntenseBGM, 0.17f);
                currMusic = ID.IntenseBGM;
            }
        }
        else if (PlayerManager.getHealth() <= 1 || PlayerManager.getCollectedEssence() >= RewardManager.getMaxEssence())
        {
            if (currMusic != ID.MediumBGM)
            {
                Audio.loopBGM(ID.MediumBGM, 0.15f);
                currMusic = ID.MediumBGM;
            }
        }
        else
        {
            if (currMusic != ID.SlowBGM)
            {
                Audio.loopBGM(ID.SlowBGM, 0.15f);
                currMusic = ID.SlowBGM;
            }
        }
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- CHECKERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Check if the player character has collected all required essences
     * and has returned to the entrance.
     */
    private void victoryCond()
    {
        if (PlayerManager.getCollectedEssence() >= RewardManager.getMaxEssence())
        {
            int x = PlayerManager.getPosX();
            int y = PlayerManager.getPosY();

            if (map.isEntrance(x, y))
                gameStatus = ID.Victory;
        }
    }

    /**
     * Check if the player character's health has dropped below 1,
     * indicating that he died. If player character is dead, pause the game
     * for few milliseconds.
     */
    private void defeatCond()
    {
        if (PlayerManager.getHealth() < 1)
        {
            try { Thread.sleep(300); }
            catch (Exception e) { System.out.println("ERROR: Unable to pause.");}

            gameStatus = ID.Defeat;
        }

    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Exits menu and starts the game.
     */
    public static void playGame() { gameStatus = ID.Null; }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    public static ID getGameStatus() { return gameStatus; }

    // --------------------------------------------------------------------------------
    // -------------------------------- GAME STATES -----------------------------------
    // -------------------------------------------------------------------------------

    public static void exitGame()
    {
        if (audio != null)
            audio.shutDown();

        System.exit(1);
    }

    public static void resetGame()
    {
        if (rm != null)
            rm.newGame();
        else
            rm = new RewardManager();

        if (em != null)
            em.newGame();
        else
            em = new EnemyManager();

        if (pm != null)
            pm.newGame();
        else
            pm = new PlayerManager();

        gameStatus = ID.Null;
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Pass tick() onto each game entity group.
     * Called by game.Game.
     */
    public void tick()
    {
        em.tick();
        rm.tick();
        pm.tick();
        map.tick();

        victoryCond();
        defeatCond();
        changeBGM();
    }

    /**
     * Pass render() onto each game entity group.
     *
     * @param g passed down from game.Game
     */
    public void render(Graphics g)
    {
        // Rewards should be on top of enemies
        em.render(g);
        rm.render(g);
        pm.render(g);
        map.render(g);
    }
}
