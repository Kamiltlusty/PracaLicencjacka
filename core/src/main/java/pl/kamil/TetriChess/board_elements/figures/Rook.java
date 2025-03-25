package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Optional;

public class Rook extends Figure {
    private final String figureId;
    private final Texture figureTexture;
    private final Vector2 position = new Vector2();
    private final Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private boolean hasMoved;
    private static final int value = 1;

    public Rook(String rookId, Texture rookTexture, float positionX, float positionY, Team team) {
        this.figureId = rookId;
        this.figureTexture = rookTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.hasMoved = false;
    }

    public boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        float moveDistanceX = finalPosition.x - initialPosition.x;
        float moveDistanceY = finalPosition.y - initialPosition.y;
        //noinspection DuplicatedCode
        return moveDistanceX == 1.0 && moveDistanceY == 0.0 || moveDistanceX == -1.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 2.0 && moveDistanceY == 0.0 || moveDistanceX == -2.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 3.0 && moveDistanceY == 0.0 || moveDistanceX == -3.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 4.0 && moveDistanceY == 0.0 || moveDistanceX == -4.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 5.0 && moveDistanceY == 0.0 || moveDistanceX == -5.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 6.0 && moveDistanceY == 0.0 || moveDistanceX == -6.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 7.0 && moveDistanceY == 0.0 || moveDistanceX == -7.0 && moveDistanceY == 0.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 1.0 || moveDistanceX == 0.0 && moveDistanceY == -1.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 2.0 || moveDistanceX == 0.0 && moveDistanceY == -2.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 3.0 || moveDistanceX == 0.0 && moveDistanceY == -3.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 4.0 || moveDistanceX == 0.0 && moveDistanceY == -4.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 5.0 || moveDistanceX == 0.0 && moveDistanceY == -5.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 6.0 || moveDistanceX == 0.0 && moveDistanceY == -6.0 ||
            moveDistanceX == 0.0 && moveDistanceY == 7.0 || moveDistanceX == 0.0 && moveDistanceY == -7.0;
    }

    @Override
    public boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager) {
        return true;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public String getFigureId() {
        return figureId;
    }

    @Override
    public Texture getFigureTexture() {
        return figureTexture;
    }

    @Override
    public Vector2 setPosition(float x, float y) {
        return position.set(x, y);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Team getTeam() {
        return team;
    }

}
