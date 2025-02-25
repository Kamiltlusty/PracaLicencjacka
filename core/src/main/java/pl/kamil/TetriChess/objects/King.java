package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;

import java.util.Optional;

public class King extends Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private boolean hasMoved;
    private static final int value = 1;

    public King(String kingId, Texture kingTexture, float positionX, float positionY, Team team) {
        this.figureId = kingId;
        this.figureTexture = kingTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.hasMoved = false;
    }

    @Override
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               Board board
    ) {
        boolean isLegal = true;
        isLegal = isNotBlocked(initialPosition, board);
        if (!isLegal) return false;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        if (!isLegal) return false;
        // checking if smth is standing on path
        Tuple2<Vector2, Boolean> isPathBlocksFree = selectedFigure.isPathBlocksFree(initialPosition, finalPosition, this, board);
        if (!isPathBlocksFree._2) return false;
        Tuple2<Vector2, Boolean> isPathFigureFree = selectedFigure.isPathFigureFree(initialPosition, finalPosition, this, board);
        Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFigureFree._1().x, isPathFigureFree._1().y);
        // check if we found figure
        if (!isPathFigureFree._2()) {
            // check if found figure is same team
            if (figure.get().getTeam().equals(selectedFigure.getTeam())) {
                isLegal = false;
            } else {
                if (finalPosition.x != isPathFigureFree._1().x || finalPosition.y != isPathFigureFree._1().y) {
                    isLegal = false;
                } else {
                    // beating
                    board.figuresList.remove(figure.get());
                }
            }
        }
        // castling
        if (!hasMoved && finalPosition.x == 6.0 && finalPosition.y == 0.0) {
            Optional<Figure> rook = board.findFigureByCoordinatesAndReturn(7, 0);
            if (rook.isPresent() && rook.get() instanceof Rook && !((Rook) rook.get()).getHasMoved()) {
                rook.get().setPosition(5.0f, 0.0f);
                ((Rook) rook.get()).setHasMoved(true);
            }
        } else if (!hasMoved && finalPosition.x == 2.0 && finalPosition.y == 0.0) {
            Optional<Figure> rook = board.findFigureByCoordinatesAndReturn(0, 0);
            if (rook.isPresent() && rook.get() instanceof Rook && !((Rook) rook.get()).getHasMoved()) {
                rook.get().setPosition(3.0f, 0.0f);
                ((Rook) rook.get()).setHasMoved(true);
            }
        } else if (!hasMoved && finalPosition.x == 6.0 && finalPosition.y == 7.0) {
            Optional<Figure> rook = board.findFigureByCoordinatesAndReturn(7, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && !((Rook) rook.get()).getHasMoved()) {
                rook.get().setPosition(5.0f, 7.0f);
                ((Rook) rook.get()).setHasMoved(true);
            }
        } else if (!hasMoved && finalPosition.x == 2.0 && finalPosition.y == 7.0) {
            Optional<Figure> rook = board.findFigureByCoordinatesAndReturn(0, 7);
            if (rook.isPresent() && rook.get() instanceof Rook && !((Rook) rook.get()).getHasMoved()) {
                rook.get().setPosition(3.0f, 7.0f);
                ((Rook) rook.get()).setHasMoved(true);
            }
        }
        hasMoved = isLegal ? true : false;
        return isLegal;
    }

    boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
        boolean isLegal = true;

        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        System.out.println("moveDistanceY: " + moveDistanceY);
        System.out.println("moveDistanceX: " + moveDistanceX);
        if (!hasMoved) {
            if (!(moveDistanceX <= 1.0 && moveDistanceY <= 1.0 ||
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
                    finalPosition.x == 2.0 && finalPosition.y == 7.0)
            ) {
                isLegal = false;
            }
        } else {
            if (!(moveDistanceX <= 1.0 && moveDistanceY <= 1.0)) {
                isLegal = false;
            }
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
