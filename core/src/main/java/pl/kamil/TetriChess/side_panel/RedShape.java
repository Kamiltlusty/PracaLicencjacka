package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;

public class RedShape extends Shape {

    public RedShape(Texture texture, Character letter, Integer number) {
        super(texture, letter, number);
    }

    @Override
    protected void setShape() {
        square.getSquares().get(8).setTexture(texture);
        square.getSquares().get(5).setTexture(texture);
        square.getSquares().get(6).setTexture(texture);
    }
}
