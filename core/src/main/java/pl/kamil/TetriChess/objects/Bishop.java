package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bishop implements Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public Bishop(String bishopId, Texture bishopTexture, float positionX, float positionY, Team team) {
        this.figureId = bishopId;
        this.figureTexture = bishopTexture;
        this.position.set(positionX, positionY);
        this.team = team;
    }

    @Override
    public void move() {

    }

    public String getFigureId() {
        return figureId;
    }

    public Texture getFigureTexture() {
        return figureTexture;
    }

    @Override
    public Vector2 setPosition(float x, float y) {
        return position.set(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Team getTeam() {
        return team;
    }
}
