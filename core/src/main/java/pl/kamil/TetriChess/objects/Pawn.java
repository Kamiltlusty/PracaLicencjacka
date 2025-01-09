package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Pawn implements Figure {
    private String figureId;
    private Texture figureTexture;
    private Vector2 position = new Vector2();
    private Team team; // dla uproszczenia zbijania, przewidywania w algorytmie czy mamy sytuacje, gdy jest bicie
    private static final int value = 1;
    private boolean hasMoved;

    public Pawn(String pawnId, Texture pawnTexture, float positionX, float positionY, Team team) {
        this.figureId = pawnId;
        this.figureTexture = pawnTexture;
        this.position.set(positionX, positionY);
        this.team = team;
        this.hasMoved = false;
    }

    @Override
    public boolean isMoveLegal(Vector2 initialPosition,
                               Vector2 finalPosition,
                               Figure selectedFigure,
                               Field field
    ) {
        // transition
        boolean isLegal = true;
        isLegal = isTransitionLegal(initialPosition, finalPosition, selectedFigure);
        // beating

        return isLegal;
    }

    boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure) {
        boolean isLegal = true;
        if (selectedFigure.getTeam() == Team.WHITE) {
            float moveDistance = finalPosition.y - initialPosition.y;
            System.out.println("moveDistance: " + moveDistance);
            if (!hasMoved) {
                if (moveDistance == 2.0 ||
                moveDistance == 1.0) {
                    hasMoved = true;
                } else isLegal = false;
            } else {
                if (moveDistance != 1.0) {
                    isLegal = false;
                }
            }
        } else {
            float moveDistance = finalPosition.y - initialPosition.y;
            System.out.println("moveDistance: " + moveDistance);
            if (!hasMoved) {
                if (moveDistance == -2.0 ||
                    moveDistance == -1.0) {
                    hasMoved = true;
                } else isLegal = false;
            } else {
                if (moveDistance != -1.0) isLegal = false;
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
