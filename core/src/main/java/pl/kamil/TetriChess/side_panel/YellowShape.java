package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;

public class YellowShape extends Shape {

    public YellowShape(Texture texture, Character letter, Integer number) {
        super(texture, letter, number);
    }

    @Override
    protected void setShape() {
        square.getSquares().get(2).setTexture(texture);
        square.getSquares().get(5).setTexture(texture);
        square.getSquares().get(7).setTexture(texture);
    }
}
