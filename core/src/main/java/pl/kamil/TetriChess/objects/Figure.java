package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public interface Figure {
    void move();
    Vector2 getPosition();
    String getFigureId();
    Team getTeam();
    Texture getFigureTexture();
    Vector2 setPosition(float x, float y);

}
