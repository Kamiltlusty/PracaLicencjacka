package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;

public class GreenShape extends Shape {
    public GreenShape(Texture texture, Character letter, Integer number) {
        super(texture, letter, number);
    }

    @Override
    protected void setShape() {
        square.getSquares().get(1).setTexture(texture);
        square.getSquares().get(5).setTexture(texture);
        square.getSquares().get(9).setTexture(texture);
    }
}
