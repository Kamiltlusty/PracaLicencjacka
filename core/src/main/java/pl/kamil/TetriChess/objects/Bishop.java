package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.lang.Math.*;

public class Bishop extends Figure {
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
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               Board board) {
        boolean isLegal = true;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        if (!isLegal) return false;
        // checking if smth is standing on path
        Tuple2<Vector2, Boolean> isPathFree = selectedFigure.isPathFree(initialPosition, finalPosition, this, board);
        Optional<Figure> figure = board.findFigureByCoordinatesAndReturn(isPathFree._1().x, isPathFree._1().y);
        // check if we found figure
        if (!isPathFree._2()) {
            // check if found figure is same team
            if (figure.get().getTeam().equals(selectedFigure.getTeam())) {
                isLegal = false;
            } else {
                if (finalPosition.x != isPathFree._1().x || finalPosition.y != isPathFree._1().y) {
                    isLegal = false;
                } else {
                    // beating
                    board.figuresList.remove(figure.get());
                }
            }
        }

        return isLegal;
    }

//    private float expecVal(int value) {
//        return switch (value) {
//            case 1 -> BigDecimal.valueOf(1.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 2 -> BigDecimal.valueOf(2.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 3 -> BigDecimal.valueOf(3.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 4 -> BigDecimal.valueOf(4.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 5 -> BigDecimal.valueOf(5.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 6 -> BigDecimal.valueOf(6.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case 7 -> BigDecimal.valueOf(7.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -1 -> BigDecimal.valueOf(-1.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -2 -> BigDecimal.valueOf(-2.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -3 -> BigDecimal.valueOf(-3.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -4 -> BigDecimal.valueOf(-4.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -5 -> BigDecimal.valueOf(-5.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -6 -> BigDecimal.valueOf(-6.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            case -7 -> BigDecimal.valueOf(-7.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
//            default -> throw new IllegalStateException("Unexpected value: " + value);
//        };
//    }

    private boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
        boolean isLegal = true;

        float moveDistanceY = Math.abs(finalPosition.y - initialPosition.y);
        float moveDistanceX = Math.abs(finalPosition.x - initialPosition.x);
        System.out.println("moveDistanceY: " + moveDistanceY);
        System.out.println("moveDistanceX: " + moveDistanceX);
        if (moveDistanceY == moveDistanceX) {
        } else isLegal = false;

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
