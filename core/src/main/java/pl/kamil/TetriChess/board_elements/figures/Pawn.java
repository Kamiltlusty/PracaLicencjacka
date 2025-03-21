package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Optional;

public class Pawn extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;
    private boolean hasBeat;

    public Pawn(String pawnId, Texture pawnTexture, float positionX, float positionY, Team team) {
        this.figureId = pawnId;
        this.figureTexture = pawnTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.moveCounter = 0;
        this.hasBeat = false;
    }

    @Override
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               BoardManager board,
                               boolean isCheckingExpose
    ) {
        try {
            if (!isNotBlocked(initialPosition, board)) return false;
//            if (!isCheckingExpose && isMoveExposingKingToCheck(board, initialPosition, finalPosition)) return false;
            // transition
            if (!isTransitionLegal(initialPosition, finalPosition, selectedFigure)) return false;
            // checking if smth is standing on path
            if (!(selectedFigure.isPathBlocksFree(initialPosition, finalPosition, this, board))._2) return false;

            Tuple2<Vector2, Boolean> isPathFigureFree = selectedFigure.isPathFigureFree(initialPosition, finalPosition, this, board);
            Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFigureFree._1().x, isPathFigureFree._1().y);
            // check if we found figure but we cant beat if final position is not equal figure position
            if (!isPathFigureFree._2()) {
                // check if found figure is same team
                if (figure.isEmpty() || figure.get().getTeam().equals(selectedFigure.getTeam())) return false;
                else {
                    // not letting pawn beat vertically
                    if (finalPosition.x == initialPosition.x) return false;
                        // don't let figure beat if it is not last chosen field
                    else if (finalPosition.x != isPathFigureFree._1().x || finalPosition.y != isPathFigureFree._1().y) return false;
                    else {
                        // beating if figure is not king
                        if (!figure.get().getFigureId().equals("K")) {
                            board.figuresList.remove(figure.get());
                            hasBeat = true;
                            return true;
                        }
                    }
                }
            }
            // not letting pawn go diagonally without beating
            if (!hasBeat && initialPosition.x != isPathFigureFree._1().x) return false;
        } finally {
            // switching back hasBeat so it can beat again and transition properly
            hasBeat = false;
        }
        return true;
    }

    boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        boolean isLegal = true;
        float moveDistanceY = finalPosition.y - initialPosition.y;
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        if (selectedFigure.getTeam() == Team.WHITE) {
            if (moveCounter == 0) {
                if (!(moveDistanceY == 2.0 && moveDistanceX == 0.0 ||
                    moveDistanceY == 1.0 && moveDistanceX <= 1.0)) return false;
            } else if (moveDistanceY != 1.0 || moveDistanceX > 1.0) return false;
        } else {
            if (moveCounter == 0) {
                if (!(moveDistanceY == -2.0 && moveDistanceX == 0.0 ||
                    moveDistanceY == -1.0 && moveDistanceX <= 1.0)) return false;
            } else if (moveDistanceY != -1.0 || moveDistanceX > 1.0) return false;
        }
        return isLegal;
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
