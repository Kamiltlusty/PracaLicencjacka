package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kamil.TetriChess.resources.Assets;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BoardUtilsTest {

    @Test
    @DisplayName("This test works only if methods in BoardUtils constructor are disabled")
    void findPositionByFieldSignature() {
        // given
        String signature = "a1";
        Assets assetsMock = mock(Assets.class);
        BoardUtils boardUtils = new BoardUtils(assetsMock);

        // when
        Vector2 position = boardUtils.findPositionByFieldSignature(signature);

        // then
        assertEquals(0, position.x);
        assertEquals(0, position.y);
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Checking whether all figure signatures are read correctly")
    void findFieldSignatureTest(int x, int y, String expectedSignature) {
        // given
        Assets assetsMock = mock(Assets.class);
        BoardUtils boardUtils = new BoardUtils(assetsMock);

        // when
        String signature = boardUtils.findFieldSignature(x, y);

        // then
        assertEquals(expectedSignature, signature);
    }

    public static Stream<Arguments> findFieldSignatureTest() {
        return Stream.of(
            Arguments.of(0, 0, "a1"),
            Arguments.of(1, 0, "b1"),
            Arguments.of(2, 0, "c1")
        );
    }






}
