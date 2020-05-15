package player;

import com.group15.player.Hirsch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyInputTest
{
    private JFrame frame;
    private Hirsch player;
    private Robot robot;

    private int absSpeed;

    @BeforeEach
    public void init()
    {
        // Randomize speed value
        Random r = new Random();
        absSpeed = r.nextInt();

        // Setup dummy player character
        player = new Hirsch(10, 10);
        player.setSpeedX(absSpeed);
        player.setSpeedY(absSpeed);

        // Create robot to emulate key presses/releases
        try { robot = new Robot(); }
        catch (Exception e) {System.out.println("ERROR: KeyInputTest failed.");}

        // Setup JFrame for keys to register
        frame = new JFrame("Test");
        frame.setVisible(true);
        frame.setSize(500, 500);
    }

    @Test
    public void testUp()
    {
        // Single Press
        robot.keyPress(KeyEvent.VK_UP);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(-absSpeed, player.getVelY());

        // Single Release
        robot.keyRelease(KeyEvent.VK_UP);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- UP RIGHT ---------------------------------------

        // Combo Press (Up Left)
        robot.keyPress(KeyEvent.VK_W);
        robot.keyPress(KeyEvent.VK_A);
        robot.waitForIdle();
        assertEquals(-absSpeed, player.getVelX());
        assertEquals(-absSpeed, player.getVelY());

        // Combo Release (Up Left)
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_A);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- UP DOWN ----------------------------------------

        // Combo Press (Up Down)
        robot.keyPress(KeyEvent.VK_W);
        robot.keyPress(KeyEvent.VK_S);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(absSpeed, player.getVelY());

        // Combo Release (Up Down)
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_S);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());
    }

    @Test
    public void testDown()
    {
        // Single Press
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(absSpeed, player.getVelY());

        // Single Release
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();

        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ----------------------------- DOWN RIGHT ---------------------------------------

        // Combo Press (Down Right)
        robot.keyPress(KeyEvent.VK_S);
        robot.keyPress(KeyEvent.VK_D);
        robot.waitForIdle();
        assertEquals(absSpeed, player.getVelX());
        assertEquals(absSpeed, player.getVelY());

        // Combo Release (Down Right)
        robot.keyRelease(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_D);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // -------------------------------- DOWN UP ---------------------------------------

        // Combo Press (Down Up)
        robot.keyPress(KeyEvent.VK_S);
        robot.keyPress(KeyEvent.VK_W);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(-absSpeed, player.getVelY());

        // Combo Release (Down Up)
        robot.keyRelease(KeyEvent.VK_W);
        robot.keyRelease(KeyEvent.VK_S);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());
    }

    @Test
    public void testLeft()
    {
        // Single Press
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.waitForIdle();
        assertEquals(-absSpeed, player.getVelX());
        assertEquals(0, player.getVelY());

        // Single Release
        robot.keyRelease(KeyEvent.VK_LEFT);
        robot.waitForIdle();

        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- LEFT DOWN --------------------------------------

        // Combo Press (Left Down)
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_S);
        robot.waitForIdle();
        assertEquals(-absSpeed, player.getVelX());
        assertEquals(absSpeed, player.getVelY());

        // Combo Release (Left Down)
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_S);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- LEFT RIGHT -------------------------------------

        // Combo Press (Left Right)
        robot.keyPress(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_D);
        robot.waitForIdle();
        assertEquals(absSpeed, player.getVelX());
        assertEquals(0, player.getVelY());

        // Combo Release (Down Up)
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_D);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());
    }

    @Test
    public void testRight()
    {
        // Single Press
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.waitForIdle();
        assertEquals(absSpeed, player.getVelX());
        assertEquals(0, player.getVelY());

        // Single Release
        robot.keyRelease(KeyEvent.VK_RIGHT);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- RIGHT UP ---------------------------------------

        // Combo Press (Right Up)
        robot.keyPress(KeyEvent.VK_D);
        robot.keyPress(KeyEvent.VK_W);
        robot.waitForIdle();
        assertEquals(absSpeed, player.getVelX());
        assertEquals(-absSpeed, player.getVelY());

        // Combo Release (Right Up)
        robot.keyRelease(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_W);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());

        // ------------------------------- RIGHT LEFT -------------------------------------

        // Combo Press (Right Left)
        robot.keyPress(KeyEvent.VK_D);
        robot.keyPress(KeyEvent.VK_A);
        robot.waitForIdle();
        assertEquals(-absSpeed, player.getVelX());
        assertEquals(0, player.getVelY());

        // Combo Release (Right Left)
        robot.keyRelease(KeyEvent.VK_D);
        robot.keyRelease(KeyEvent.VK_A);
        robot.waitForIdle();
        assertEquals(0, player.getVelX());
        assertEquals(0, player.getVelY());
    }

    @AfterEach
    public void close()
    {
        frame.dispose();
    }
}
