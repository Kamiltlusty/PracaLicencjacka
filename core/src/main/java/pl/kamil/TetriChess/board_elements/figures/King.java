package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.ArrayList;
import java.util.List;
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
        // castling first i have to check whether move is occurring by checking is king moving 2 fields if not we can return true because move != castling
        if (Math.abs(finalPosition.x - initialPosition.x) != 2) return true;
        // if yes then we have to check move counters
        if (finalPosition.x == 6.0 && finalPosition.y == 0.0) {
            if (moveCounter != 0) return false;
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 0);
            if (rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 2.0 && finalPosition.y == 0.0) {
            if (moveCounter != 0) return false;
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 0);
            if (rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 6.0 && finalPosition.y == 7.0) {
            if (moveCounter != 0) return false;
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 2.0 && finalPosition.y == 7.0) {
            if (moveCounter != 0) return false;
            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && rook.get().getMoveCounter() == 0) return false;
            else boardManager.setCastling(true);
        }
        return true;
    }

    @Override
    public List<Vector2> writeDownPossibleMoves() {
        List<Vector2> possibleMoves = new ArrayList<>();
        // check possible moves in every direction right down left up
        // to right needs to check 2 fields because of castling
        // if king didn't move can move 2 fields else one
        Vector2 initialPosition = new Vector2(position.x + 1, position.y);
        if (moveCounter == 0) {
            for (int i = 0; initialPosition.x < 8 && i < 2; i++) {
                possibleMoves.add(initialPosition);
                initialPosition = new Vector2(initialPosition.x + 1, initialPosition.y);
            }
        } else {
            if (initialPosition.x < 8){
                possibleMoves.add(initialPosition);
            }
        }
        // to left needs to check 2 fields because of castling
        // if king didn't move can move 2 fields else one
        initialPosition = new Vector2(position.x - 1, position.y);
        if (moveCounter == 0) {
            for (int i = 0; initialPosition.x >= 0 && i < 2; i++) {
                possibleMoves.add(initialPosition);
                initialPosition = new Vector2(initialPosition.x - 1, initialPosition.y);
            }
        } else {
            if (initialPosition.x >= 0) {
                possibleMoves.add(initialPosition);
            }
        }
        // to down
        initialPosition = new Vector2(position.x, position.y - 1);
        if (initialPosition.y >= 0) {
            possibleMoves.add(initialPosition);
        }
        // to up
        initialPosition = new Vector2(position.x, position.y + 1);
        if (initialPosition.y < 8) {
            possibleMoves.add(initialPosition);
        }
        // diagonally right up
        initialPosition = new Vector2(position.x + 1, position.y + 1);
        if (initialPosition.y < 8 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        // diagonally right down
        initialPosition = new Vector2(position.x + 1, position.y - 1);
        if (initialPosition.y >= 0 && initialPosition.x < 8) {
            possibleMoves.add(initialPosition);
        }
        // diagonally left down
        initialPosition = new Vector2(position.x - 1, position.y - 1);
        if (initialPosition.y >= 0 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        // diagonally left up
        initialPosition = new Vector2(position.x - 1, position.y + 1);
        if (initialPosition.y < 8 && initialPosition.x >= 0) {
            possibleMoves.add(initialPosition);
        }
        return possibleMoves;
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
