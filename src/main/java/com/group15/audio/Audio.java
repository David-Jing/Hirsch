package com.group15.audio;

import com.group15.gameEngine.ID;

import java.io.File;

/**
 * Provides interface to play various sound effects and bgm.
 */
public class Audio
{
    private static boolean initialized = false;
    private static AudioLayer slowBGM, mediumBGM, intenseBGM;
    private static AudioLayer trapHit, enemyHit, essenceHit, elusiveHit;

    private File trapHitPath = new File("src/main/resources/soundfx/traphit.wav");
    private File enemyHitPath = new File("src/main/resources/soundfx/enemyhit.wav");
    private File essenceHitPath = new File("src/main/resources/soundfx/essencehit.wav");
    private File elusiveHitPath = new File("src/main/resources/soundfx/elusivehit.wav");

    private File slowBGMPath = new File("src/main/resources/music/slow.wav");
    private File mediumBGMPath = new File("src/main/resources/music/medium.wav");
    private File intenseBGMPath = new File("src/main/resources/music/intense.wav");

    public Audio()
    {
        slowBGM = new AudioLayer(slowBGMPath, true);
        mediumBGM = new AudioLayer(mediumBGMPath, true);
        intenseBGM = new AudioLayer(intenseBGMPath, true);

        trapHit = new AudioLayer(trapHitPath, false);
        enemyHit = new AudioLayer(enemyHitPath, false);
        essenceHit = new AudioLayer(essenceHitPath, false);
        elusiveHit = new AudioLayer(elusiveHitPath, false);

        initialized = true;
    }

    /**
     * Plays a specific sound effect that corresponds to a gameObject.
     *
     * @param id   the gameObject ID the sound is associated with
     * @param gain the volume value the sound is to be played at (0 to 1)
     */
    public static void playEffect(ID id, float gain)
    {
        if (!initialized)
            return;

        switch (id)
        {
            case Hirsch:
                // ...
                break;
            case Trap:
                trapHit.play(gain);
                break;
            case EvilSpirit:
                enemyHit.play(gain);
                break;
            case ElusiveLifeEssence:
                elusiveHit.play(gain);
                break;
            case LifeEssence:
                essenceHit.play(gain);
                break;
            default:
                System.out.println("ERROR: Invalid object id argument.");
        }
    }

    /**
     * Plays select music based on BGM ID and at the volume determined by gain.
     *
     * @param id   the ID of the music that is to be played
     * @param gain the volume value the music is to be played at (0 to 1)
     */
    public static void loopBGM(ID id, float gain)
    {
        if (!initialized)
            return;

        slowBGM.stop();
        mediumBGM.stop();
        intenseBGM.stop();

        switch (id)
        {
            case SlowBGM:
                slowBGM.play(gain);
                break;
            case MediumBGM:
                mediumBGM.play(gain);
                break;
            case IntenseBGM:
                intenseBGM.play(gain);
                break;
            default:
                System.out.println("ERROR: Invalid object id argument for bgm.");
        }
    }

    /**
     * Called on quiting the game. Clears memory.
     */
    public void shutDown()
    {
        slowBGM.stop();
        mediumBGM.stop();
        intenseBGM.stop();

        trapHit.stop();
        enemyHit.stop();
        essenceHit.stop();
        elusiveHit.stop();
    }
}
