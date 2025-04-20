package pl.kamil.TetriChess.ai;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.figures.Figure;

public class MoveDetails {
    private final Figure figure;
    private final Vector2 move;
    private int score;

    public MoveDetails(Figure figure, Vector2 move, int score) {
        this.figure = figure;
        this.move = move;
        this.score = score;
    }

    public Vector2 getMove() {
        return move;
    }

    public float getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Figure getFigure() {
        return figure;
    }
}
