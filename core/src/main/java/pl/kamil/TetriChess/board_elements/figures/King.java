package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Optional;

public class King extends Figure {
    private final String figureId;
    private final Texture figureTexture;
    private final Vector2 position = new Vector2();
    private final Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;

    public King(String kingId, Texture kingTexture, float positionX, float positionY, Team team) {
        this.figureId = kingId;
        this.figureTexture = kingTexture;
        this.position.set(positionX, positionY);
        this.team = team;
    }

    public boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        if (moveCounter == 0) {
            return (moveDistanceX <= 1.0 && moveDistanceY <= 1.0 ||
                moveDistanceX <= 2.0 && moveDistanceY <= 2.0 &&
                    initialPosition.x == 4.0 && initialPosition.y == 0.0 &&
                    finalPosition.x == 6.0 && finalPosition.y == 0.0 ||
                moveDistanceX <= 2.0 && moveDistanceY <= 2.0 &&
                    initialPosition.x == 4.0 && initialPosition.y == 0.0 &&
                    finalPosition.x == 2.0 && finalPosition.y == 0.0 ||
                moveDistanceX <= 2.0 && moveDistanceY <= 2.0 &&
                    initialPosition.x == 4.0 && initialPosition.y == 7.0 &&
                    finalPosition.x == 6.0 && finalPosition.y == 7.0 ||
                moveDistanceX <= 2.0 && moveDistanceY <= 2.0 &&
                    initialPosition.x == 4.0 && initialPosition.y == 7.0 &&
                    finalPosition.x == 2.0 && finalPosition.y == 7.0);
        } else {
            return moveDistanceX <= 1.0 && moveDistanceY <= 1.0;
        }
    }

    @Override
    public boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager) {
        // castling
        if (moveCounter == 0 && finalPosition.x == 6.0 && finalPosition.y == 0.0) {
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 0);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) {
                rook.get().setPosition(5.0f, 0.0f);
                ((Rook) rook.get()).setHasMoved(true);
            } else return false;
        } else if (moveCounter == 0 && finalPosition.x == 2.0 && finalPosition.y == 0.0) {
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 0);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) {
                rook.get().setPosition(3.0f, 0.0f);
                ((Rook) rook.get()).setHasMoved(true);
            } else return false;
        } else if (moveCounter == 0 && finalPosition.x == 6.0 && finalPosition.y == 7.0) {
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) {
                rook.get().setPosition(5.0f, 7.0f);
                ((Rook) rook.get()).setHasMoved(true);
            } else return false;
        } else if (moveCounter == 0 && finalPosition.x == 2.0 && finalPosition.y == 7.0) {
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) {
                rook.get().setPosition(3.0f, 7.0f);
                ((Rook) rook.get()).setHasMoved(true);
            } else return false;
        }
        return true;
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
