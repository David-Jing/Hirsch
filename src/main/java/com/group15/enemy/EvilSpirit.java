package com.group15.enemy;

import com.group15.gameEngine.GameObject;
import com.group15.gameEngine.ID;
import com.group15.map.Map;
import com.group15.player.PlayerManager;
import com.group15.reward.RewardManager;
import com.group15.utility.Utility;

import java.awt.*;

/**
 * Manages and stores the stats of an evil spirit game entities.
 */
public class EvilSpirit extends GameObject
{
    private Image enemyPic = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/enemy.gif");
    private Image enemySkill = Toolkit.getDefaultToolkit().getImage("src/main/resources/animation/enemy_skill.gif");

    private Map map = Map.getInstance();

    private float wallSkillCooldown = 4f; // Units of seconds

    private long lastSkillActiveTime = System.currentTimeMillis();
    private boolean skillActivated = false;
    private boolean frenzyMode = false;

    public EvilSpirit(int x, int y)
    {
        super(x, y);
        id = ID.EvilSpirit;

        speedX = 3;
        speedY = 3;
        health = 5;

        width = 50;
        height = 50;

        contactRadius = width / 2 + 20;
    }

    // --------------------------------------------------------------------------------
    // --------------------------------- MOVEMENT -------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Logic for moving through walls. Cannot exit map.
     */
    private void skillOnMove()
    {
        if (velX != 0)
            if (x + velX >= 0 && x + velX + width < map.getWidth() &&
                    EnemyManager.isEvilSpirit(x + velX, y) < 2)
                x += velX;

        if (velY != 0)
            if (y + velY >= 0 && y + velY + height < map.getHeight() &&
                    EnemyManager.isEvilSpirit(x, y + velY) < 2)
                y += velY;
    }

    /**
     * Logic for normal movement. Cannot move through walls.
     */
    private void skillOffMove()
    {
        if (velX != 0)
        {
            if (!map.isWall(x + velX, y, width, height) && !map.isEntrance(x + velX, y, width, height) &&
                    EnemyManager.isEvilSpirit(x + velX, y) < 2)
                x += velX;
        }

        if (velY != 0)
            if (!map.isWall(x, y + velY, width, height) && !map.isEntrance(x, y + velY, width, height) &&
                    EnemyManager.isEvilSpirit(x, y + velY) < 2)
                y += velY;
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- SKILLS --------------------------------------
    // --------------------------------------------------------------------------------

    /**
     * Allows object to temporarily move through walls.
     */
    private void wallSkill()
    {
        // Just used skill
        if (skillActivated)
        {
            // Remain active through duration of wall, else turn it off
            if (map.isWall(x + velX, y + velY, width, height) && !map.isEntrance(x + velX, y + velY, width, height))
                skillOnMove();
            else
            {
                skillActivated = false;
                skillOffMove();
            }
        }
        // Can use skill
        else if (Utility.haveElapsed(lastSkillActiveTime, wallSkillCooldown) && !skillActivated)
        {
            // A wall to pass through, use skill now
            if (map.isWall(x + velX, y + velY, width, height))
            {
                lastSkillActiveTime = System.currentTimeMillis();
                skillActivated = true;
                skillOnMove();
            }
            // No walls to pass through, reserve skill for later
            else
                skillOffMove();
        }
        else
            skillOffMove();
    }

    /**
     * If all essences are collected, let object move faster.
     */
    private void frenzyMode()
    {
        if (!frenzyMode)
            if (PlayerManager.getCollectedEssence() >= RewardManager.getMaxEssence())
            {
                speedX++;
                speedY++;
                wallSkillCooldown--;
                frenzyMode = true;
            }
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------

    @Override
    public void tick()
    {
        frenzyMode();
        wallSkill();
    }

    @Override
    public void render(Graphics g)
    {
        if (!skillActivated)
            g.drawImage(enemyPic, x, y, width, height, null);
        else
        {
            g.drawImage(enemySkill, x, y, width, height, null);
        }
    }
}
