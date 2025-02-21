package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

public class Square3x3 {

    private final Map<Integer, Square1x1> squares = new HashMap<>();
    public Square3x3(Texture texture) {
        initializeSquareMap(texture);
    }

    private void initializeSquareMap(Texture texture) {
        squares.put(1, new Square1x1(null, new Rectangle(0, 0, texture.getWidth(), texture.getHeight())));
        squares.put(2, new Square1x1(null, new Rectangle(texture.getWidth(), 0, texture.getWidth(), texture.getHeight())));
        squares.put(3, new Square1x1(null, new Rectangle(2 * texture.getWidth(), 0, texture.getWidth(), texture.getHeight())));
        squares.put(4, new Square1x1(null, new Rectangle(0, texture.getHeight(), texture.getWidth(), texture.getHeight())));
        squares.put(5, new Square1x1(null, new Rectangle(texture.getWidth(), texture.getHeight(), texture.getWidth(), texture.getHeight())));
        squares.put(6, new Square1x1(null, new Rectangle(2 * texture.getWidth(), texture.getHeight(), texture.getWidth(), texture.getHeight())));
        squares.put(7, new Square1x1(null, new Rectangle(0, 2 * texture.getHeight(), texture.getWidth(), texture.getHeight())));
        squares.put(8, new Square1x1(null, new Rectangle(texture.getWidth(), 2 * texture.getHeight(), texture.getWidth(), texture.getHeight())));
        squares.put(9, new Square1x1(null, new Rectangle(2 * texture.getWidth(), 2 * texture.getHeight(), texture.getWidth(), texture.getHeight())));
    }

    public Map<Integer, Square1x1> getSquares() {
        return squares;
    }
}
