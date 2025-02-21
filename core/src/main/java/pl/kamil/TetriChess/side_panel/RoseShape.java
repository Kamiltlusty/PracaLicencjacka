package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;

public class RoseShape extends Shape {

    public RoseShape(Texture texture, Character letter, Integer number) {
        super(texture, letter, number);
    }

    @Override
    protected void setShape() {
        square.getSquares().get(1).setTexture(texture);
        square.getSquares().get(4).setTexture(texture);
        square.getSquares().get(8).setTexture(texture);
    }
}
