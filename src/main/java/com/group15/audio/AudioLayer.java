package com.group15.audio;

import com.group15.utility.Utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

/**
 * The backend of the audio interface. Manages the loading and playing of
 * sound effects and bgm.
 */
public class AudioLayer
{
    private Clip clip;

    private boolean loop;
    private File audioFile;

    public AudioLayer(File file, boolean loop)
    {
        audioFile = file;
        this.loop = loop;

        try { clip = AudioSystem.getClip(); }
        catch (Exception e) { System.out.println("ERROR: AudioLayer initiation failed"); }
    }

    /**
     * Plays the loaded audio clip. Will automatically reset clip if required.
     *
     * @param gain the volume value the music is to be played at (0 to 1)
     */
    public void play(float gain)
    {
        // If the clip is still running in the background, don't reset
        if (!clip.isRunning())
        {
            stop();
            reset();
        }

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(Utility.gain2db(gain));

        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Stops the audio. Should call this on shutdown to clear memory.
     */
    public void stop()
    {
        clip.stop();
        clip.close();
    }

    /**
     * Audio needs to be reset before played again if the audio clips has ended.
     */
    private void reset()
    {
        if (loop)
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        try
        {
            AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
            clip.open(stream);
        }
        catch (Exception e) { System.out.println("ERROR: AudioLayer loading failed"); }
    }
}