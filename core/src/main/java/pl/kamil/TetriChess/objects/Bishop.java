package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.*;

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
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               Field field) {
        boolean isLegal = true;
        // transition
        isLegal = isTransitionLegal(initialPosition, finalPosition);
        // beating

        return isLegal;
    }

    private float expecVal(int value) {
        return switch (value) {
            case 1 -> BigDecimal.valueOf(1.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 2 -> BigDecimal.valueOf(2.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 3 -> BigDecimal.valueOf(3.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 4 -> BigDecimal.valueOf(4.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 5 -> BigDecimal.valueOf(5.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 6 -> BigDecimal.valueOf(6.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case 7 -> BigDecimal.valueOf(7.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -1 -> BigDecimal.valueOf(-1.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -2 -> BigDecimal.valueOf(-2.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -3 -> BigDecimal.valueOf(-3.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -4 -> BigDecimal.valueOf(-4.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -5 -> BigDecimal.valueOf(-5.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -6 -> BigDecimal.valueOf(-6.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            case -7 -> BigDecimal.valueOf(-7.0 * sqrt(2)).setScale(2, RoundingMode.HALF_UP).floatValue();
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    private boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition) {
        boolean isLegal = true;

        float moveDistanceX = finalPosition.x - initialPosition.x;
        float moveDistanceY = finalPosition.y - initialPosition.y;
        float moveDistance = BigDecimal.valueOf(sqrt(pow(moveDistanceX, 2) + pow(moveDistanceY, 2)))
            .setScale(2, RoundingMode.HALF_UP).floatValue();
        float revMoveDistance = -moveDistance;
        System.out.println("moveDistance: " + moveDistance + " revMoveDistance: " + revMoveDistance + " last: " + expecVal(1));
        if (!(moveDistance ==  expecVal(1) || moveDistance == expecVal(2) || moveDistance == expecVal(3) ||
            moveDistance == expecVal(4) || moveDistance == expecVal(5) || moveDistance == expecVal(6) || moveDistance == expecVal(7) ||
            revMoveDistance == expecVal(-1) || revMoveDistance == expecVal(-2) || revMoveDistance == expecVal(-3) ||
            revMoveDistance == expecVal(-4) || revMoveDistance == expecVal(-5) || revMoveDistance == expecVal(-6) ||
            revMoveDistance == expecVal(-7))) {
            isLegal = false;
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
