import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player testWhite;
    Player testBlack;
    @BeforeEach
    void setUp(){
        testWhite = new Player("Test", Player.Color.WHITE);
        testBlack = new Player("Also Test", Player.Color.BLACK);
    }
    @Test
    void setScore() {
    }

    @Test
    void getScore() {
    }

    @Test
    void getName() {
        assertAll(()->assertEquals("Test",testWhite.getName()),
                ()->assertEquals("Also Test",testBlack.getName()));
    }

    @Test
    void getColor() {
        assertAll(()->assertEquals(Player.Color.WHITE,testWhite.getColor()),
                ()->assertEquals(Player.Color.BLACK,testBlack.getColor()));
    }

    @Test
    void getPiece() {
    }

    @Test
    void pipScore() {
        assertAll(()->assertEquals(167,testWhite.pipScore()),
                ()->assertEquals(167,testBlack.pipScore()));
    }
}