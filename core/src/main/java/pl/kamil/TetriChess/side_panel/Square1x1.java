package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Square1x1 {
    Texture texture;
    Rectangle rectangle;
    public Square1x1(Texture texture, Rectangle rectangle) {
        this.texture = texture;
        this.rectangle = rectangle;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
