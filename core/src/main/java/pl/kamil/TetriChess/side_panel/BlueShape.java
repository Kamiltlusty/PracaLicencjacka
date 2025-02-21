package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;

public class BlueShape extends Shape {

    public BlueShape(Texture texture, Character letter, Integer number) {
        super(texture, letter, number);
    }

    @Override
    protected void setShape() {
        square.getSquares().get(5).setTexture(texture);
        square.getSquares().get(7).setTexture(texture);
        square.getSquares().get(9).setTexture(texture);
    }
}
