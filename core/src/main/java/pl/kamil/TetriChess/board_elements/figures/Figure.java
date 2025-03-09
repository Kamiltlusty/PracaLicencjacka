package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.board_elements.Team;

import java.util.Objects;
import java.util.Optional;

public abstract class Figure {
    public abstract boolean isMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board);
    public abstract Vector2 getPosition();
    public abstract String getFigureId();
    public abstract Team getTeam();
    public abstract Texture getFigureTexture();
    public abstract Vector2 setPosition(float x, float y);
    protected boolean isNotBlocked(Vector2 initialPosition, BoardManager board) {
        String fieldSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) initialPosition.x, (int) initialPosition.y);
        Field field = board.getFieldsMap().get(fieldSignature);
        return field.getBlockedState() != Field.BlockedState.BLOCKED;
    }
    protected Tuple2<Vector2, Boolean> isPathFigureFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        // check is path figure free
        // how figure moved up/left/diagonally
        System.out.println("finalPosition: " + finalPosition + ", initialPosition: " + initialPosition);
        boolean isFinalAgain = false;
        if (finalPosition.y != initialPosition.y && finalPosition.x != initialPosition.x) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            int j = finalPosition.x > initialPosition.x ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosY = initialPosition.y + i;
            float checkPosX = initialPosition.x + j;
            // loop needs to check final field once before closing
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY && finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent()) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosY += i;
                checkPosX += j;
            }
        } else if (finalPosition.y != initialPosition.y) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosY = initialPosition.y + i;
            // loop needs to check final field once before closing
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) initialPosition.x, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent()) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosY += i;
            }
        } else if (finalPosition.x != initialPosition.x) {
            int i = finalPosition.x > initialPosition.x ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosX = initialPosition.x + i;
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) initialPosition.y);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent()) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosX += i;
            }
        }
        // check is path blocks free
        return new Tuple2<>(new Vector2(finalPosition.x, finalPosition.y), true);
    }
    protected Tuple2<Vector2, Boolean> isPathBlocksFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        // check is path figure free
        // how figure moved up/left/diagonally
        System.out.println("finalPosition: " + finalPosition + ", initialPosition: " + initialPosition);
        boolean isFinalAgain = false;
        if (finalPosition.y != initialPosition.y && finalPosition.x != initialPosition.x) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            int j = finalPosition.x > initialPosition.x ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosY = initialPosition.y + i;
            float checkPosX = initialPosition.x + j;
            // loop needs to check final field once before closing
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY && finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Field field = board.getFieldsMap().get(foundSignature);
                    if (field.getBlockedState() == Field.BlockedState.BLOCKED) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosY += i;
                checkPosX += j;
            }
        } else if (finalPosition.y != initialPosition.y) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosY = initialPosition.y + i;
            // loop needs to check final field once before closing
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) initialPosition.x, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Field field = board.getFieldsMap().get(foundSignature);
                    if (field.getBlockedState() == Field.BlockedState.BLOCKED) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosY += i;
            }
        } else if (finalPosition.x != initialPosition.x) {
            int i = finalPosition.x > initialPosition.x ? 1 : -1;
            // don't check initial position start checking from second field but it may change when add tetris fields
            float checkPosX = initialPosition.x + i;
            while (!isFinalAgain) {
                // set that we checked final field
                isFinalAgain = (finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) initialPosition.y);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Field field = board.getFieldsMap().get(foundSignature);
                    if (field.getBlockedState() == Field.BlockedState.BLOCKED) {
                        return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
                    }
                }
                checkPosX += i;
            }
        }
        // check is path blocks free
        return new Tuple2<>(new Vector2(finalPosition.x, finalPosition.y), true);
    }
    protected void beat() {

    }


}
