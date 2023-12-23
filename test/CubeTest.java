import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {
    private Cube testCube;
    private Player playerB,playerW;
    @BeforeEach
    void setUp() {
        testCube=new Cube();
        playerB = new Player("B", Player.Color.BLACK);
        playerW = new Player("W", Player.Color.WHITE);
    }

    @Test
    void doubleScore() {
        testCube.doubleScore(playerB);
        assertEquals(2,testCube.getDouble());
        assertEquals(playerB,testCube.getOwner());
    }

    @Test
    void setOwner() {
        testCube.setOwner(playerW);
        assertEquals(playerW,testCube.getOwner());
    }

    @Test
    void hasOwner() {
        assertFalse(testCube.hasOwner());
        testCube.setOwner(playerB);
        assertTrue(testCube.hasOwner());
    }

    @Test
    void isOwnedBy() {
        assertFalse(testCube.isOwnedBy(playerB));
        testCube.setOwner(playerW);
        assertTrue(testCube.isOwnedBy(playerW));
        assertFalse(testCube.isOwnedBy(playerB));
    }

    @Test
    void getOwner() {
        testCube.setOwner(playerB);
        assertEquals(playerB,testCube.getOwner());
    }

    @Test
    void getDouble() {
        testCube.doubleScore(playerB);
        testCube.doubleScore(playerW);
        assertEquals(4,testCube.getDouble());
    }

    @Test
    void doubleStatus() {
        assertEquals("Double: 1 no owner",testCube.doubleStatus());
        testCube.doubleScore(playerW);
        assertEquals("Double: 2 W in possession",testCube.doubleStatus());
        testCube.doubleScore(playerB);
        assertEquals("Double: 4 B in possession",testCube.doubleStatus());
    }
}