package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public interface Figure {
    boolean isMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Field field);
    Vector2 getPosition();
    String getFigureId();
    Team getTeam();
    Texture getFigureTexture();
    Vector2 setPosition(float x, float y);

}
