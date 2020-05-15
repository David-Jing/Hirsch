package map;

import com.group15.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapTest
{
    private Map map;

    @BeforeEach
    public void init() { map = Map.getInstance(); }

    @Test
    public void testInstance() { assertNotNull(map); }

    // --------------------------------------------------------------------------------
    // ---------------------------------- GETTERS -------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testGetSize()
    {
        assertEquals(1080, map.getWidth());
        assertEquals(680, map.getHeight());
    }

    // --------------------------------------------------------------------------------
    // ---------------------------------- CHECKERS ------------------------------------
    // --------------------------------------------------------------------------------

    @Test
    public void testIsWall()
    {
        assertTrue(map.isWall(0, 0));
        assertTrue(map.isWall(2000, 2000));
        assertFalse(map.isWall(400, 200));
        assertFalse(map.isWall(50, 50));
    }

    @Test
    public void testIsEntrance()
    {
        assertTrue(map.isEntrance(540, 0));
        assertTrue(map.isEntrance(2000, 2000));
        assertFalse(map.isEntrance(0, 0));
        assertFalse(map.isEntrance(400, 200));
    }

    @Test
    public void testOverlap()
    {
        assertTrue(map.isWall(0, 0, 40, 40));
        assertFalse(map.isWall(50, 50, 40, 40));

        assertTrue(map.isEntrance(540, 0, 40, 40));
        assertFalse(map.isEntrance(400, 200, 40, 40));

    }
}
