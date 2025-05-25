package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class King extends Figure {
    private final String figureId;
    private final Texture figureTexture;
    private final Vector2 position = new Vector2();
    private final Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie;

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
        // I have to check is field near to rook is empty and not blocked
        // because king move is checking fields to his final destination and no further
        if (finalPosition.x == 6.0 && finalPosition.y == 0.0) {
            if (moveCounter != 0) return false;
            // check are fields {(5,0),(6,0)} attacked and it is valid by any opponent figure
            boolean isAnyFigureAttackingField50 = isAnyFigureAttackingField(boardManager, 5, 0);
            boolean isAnyFigureAttackingField60 = isAnyFigureAttackingField(boardManager, 6, 0);
            if (isAnyFigureAttackingField50 || isAnyFigureAttackingField60) return false;

            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 0);
            if (rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 2.0 && finalPosition.y == 0.0) {
            if (moveCounter != 0) return false;
            // check are fields {(1,0),(2,0),(3,0)} attacked and it is valid by any opponent figure
            boolean isAnyFigureAttackingField10 = isAnyFigureAttackingField(boardManager, 1, 0);
            boolean isAnyFigureAttackingField20 = isAnyFigureAttackingField(boardManager, 2, 0);
            boolean isAnyFigureAttackingField30 = isAnyFigureAttackingField(boardManager, 3, 0);
            if (isAnyFigureAttackingField10 || isAnyFigureAttackingField20 || isAnyFigureAttackingField30) return false;

            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 0);
            Field.BlockedState blockedState = boardManager.getFieldsMap().get("b1").getBlockedState();
            boolean isFigureOnFieldB1 = boardManager.figuresList.stream().anyMatch(f -> f.getPosition().equals(new Vector2(1, 0)));
            if (isFigureOnFieldB1 || blockedState.equals(Field.BlockedState.BLOCKED) ||
                rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 6.0 && finalPosition.y == 7.0) {
            if (moveCounter != 0) return false;
            // check are fields {(5,7),(6,7)} attacked and it is valid by any opponent figure
            boolean isAnyFigureAttackingField57 = isAnyFigureAttackingField(boardManager, 5, 7);
            boolean isAnyFigureAttackingField67 = isAnyFigureAttackingField(boardManager, 6, 7);
            if (isAnyFigureAttackingField57 || isAnyFigureAttackingField67) return false;

            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 7);
            if (rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        } else if (finalPosition.x == 2.0 && finalPosition.y == 7.0) {
            if (moveCounter != 0) return false;
            // check are fields {(1,7),(2,7),(3,7)} attacked and it is valid by any opponent figure
            boolean isAnyFigureAttackingField17 = isAnyFigureAttackingField(boardManager, 1, 7);
            boolean isAnyFigureAttackingField27 = isAnyFigureAttackingField(boardManager, 2, 7);
            boolean isAnyFigureAttackingField37 = isAnyFigureAttackingField(boardManager, 3, 7);
            if (isAnyFigureAttackingField17 || isAnyFigureAttackingField27 || isAnyFigureAttackingField37) return false;

            Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 7);
            Field.BlockedState blockedState = boardManager.getFieldsMap().get("b8").getBlockedState();
            boolean isFigureOnFieldB8 = boardManager.figuresList.stream().anyMatch(f -> f.getPosition().equals(new Vector2(1, 7)));
            if (isFigureOnFieldB8 || blockedState.equals(Field.BlockedState.BLOCKED) ||
                rook.isEmpty() || !(rook.get() instanceof Rook) || rook.get().getMoveCounter() != 0) return false;
            else boardManager.setCastling(true);
        }
        return true;
    }

    private boolean isAnyFigureAttackingField(BoardManager boardManager, int x, int y) {
        for (Figure f : boardManager.figuresList) {
            if (!f.getTeam().equals(team)) {
                if (f.isMoveLegal(f.getPosition(), new Vector2(x, y), f, boardManager)) {
                    if (f.isPathFigureFree(f.getPosition(), new Vector2(x, y), f, boardManager).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    @Override
    public int getValue() {
        return 0;
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
