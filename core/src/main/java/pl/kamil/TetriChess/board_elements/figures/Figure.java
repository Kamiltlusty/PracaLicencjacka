package pl.kamil.TetriChess.board_elements.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.gameplay.StateBeforeMoveRecord;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class Figure {
    public abstract Vector2 getPosition();

    public abstract String getFigureId();

    public abstract Team getTeam();

    public abstract Texture getFigureTexture();

    public abstract Vector2 setPosition(float x, float y);

    public abstract boolean isTransitionLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure);

    public abstract boolean isSpecificMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, Figure foundFigure, BoardManager boardManager);

    protected Integer moveCounter = 0;

//    protected List<Vector2> possibleMoves = new ArrayList<>();

    public abstract List<Vector2> writeDownPossibleMoves();

    public Integer getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(Integer moveCounter) {
        this.moveCounter = moveCounter;
    }

    public boolean isMoveLegal(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager boardManager) {
        // check whether figure is blocked
        if (!isNotBlocked(initialPosition, boardManager)) return false;
        // check whether transition is legal
        if (!isTransitionLegal(initialPosition, finalPosition, selectedFigure)) return false;
        // check whether path to final field is blockless
        return (selectedFigure.isPathBlocksFree(initialPosition, finalPosition, this, boardManager))._2;
    }

    public boolean isBeatingLegal(
        Vector2 initialPosition,
        Vector2 finalPosition,
        Figure selectedFigure,
        Figure foundFigure,
        BoardManager boardManager,
        StateBeforeMoveRecord record,
        boolean simulateWithoutBeating) {
        // check if we found figure but we cant beat if final position is not equal figure position
        // check if found figure is same team
        if (foundFigure.getTeam().equals(selectedFigure.getTeam())) return false;
        // if figure is pawn and is moving straight then beating is not legal
        if (selectedFigure.getFigureId().charAt(0) == 'p') {
            String initialPositionSignature = record.getBoardState().get(selectedFigure);
            Vector2 initPosition = boardManager.getBoardUtils().findPositionByFieldSignature(initialPositionSignature);
            if (initPosition.x == finalPosition.x && initPosition.y != finalPosition.y) {
                return false; // because pawn tries to beat vertically not diagonally
            }
        }

        // don't let figure beat if it is not last chosen field
        if (finalPosition.x != foundFigure.getPosition().x
            || finalPosition.y != foundFigure.getPosition().y)
            return false;
        if (!simulateWithoutBeating) {
            boardManager.setCapturedFigureId(foundFigure.getFigureId());
            boardManager.setCapture(true);
        }
        return true;
    }

    protected boolean isNotBlocked(Vector2 initialPosition, BoardManager board) {
        String fieldSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) initialPosition.x, (int) initialPosition.y);
        Field field = board.getFieldsMap().get(fieldSignature);
        return field.getBlockedState() != Field.BlockedState.BLOCKED;
    }

    public Optional<Figure> isPathFigureFree(Vector2 initialPosition, Vector2 finalPosition, Figure selectedFigure, BoardManager board) {
        // check whether path to final fields is figureless and if not save figure as to be captured
        // check is path figure free
        // how figure moved up/left/diagonally
        System.out.println("finalPosition: " + finalPosition + ", initialPosition: " + initialPosition);
        boolean isFinalAgain = false;
        if (finalPosition.y != initialPosition.y && finalPosition.x != initialPosition.x) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            int j = finalPosition.x > initialPosition.x ? 1 : -1;
            //  check initial position because we can simulate moving to another field to check whether in next move we can attack king
            float checkPosY = initialPosition.y;
            float checkPosX = initialPosition.x;
            // loop needs to check final field once before closing
            while (!isFinalAgain && checkPosY >= 0 && checkPosY < 8 && checkPosX >= 0 && checkPosX < 8) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY && finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent() && !foundFigure.get().equals(selectedFigure)) {
                        return foundFigure;
                    }
                }
                checkPosY += i;
                checkPosX += j;
            }
        } else if (finalPosition.y != initialPosition.y) {
            int i = finalPosition.y > initialPosition.y ? 1 : -1;
            //  check initial position because we can simulate moving to another field to check whether in next move we can attack king
            float checkPosY = initialPosition.y;
            // loop needs to check final field once before closing
            while (!isFinalAgain && checkPosY >= 0 && checkPosY < 8) {
                // set that we checked final field
                isFinalAgain = (finalPosition.y == checkPosY);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) initialPosition.x, (int) checkPosY);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent() && !foundFigure.get().equals(selectedFigure)) {
                        return foundFigure;
                    }
                }
                checkPosY += i;
            }
        } else if (finalPosition.x != initialPosition.x) {
            int i = finalPosition.x > initialPosition.x ? 1 : -1;
            //  check initial position because we can simulate moving to another field to check whether in next move we can attack king
            float checkPosX = initialPosition.x;
            while (!isFinalAgain && checkPosX >= 0 && checkPosX < 8) {
                // set that we checked final field
                isFinalAgain = (finalPosition.x == checkPosX);
                // seek for figure
                String foundSignature = board.getBoardUtils().findFieldSignatureByCoordinates((int) checkPosX, (int) initialPosition.y);
                if (!Objects.equals(foundSignature, "-1")) {
                    Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
                    Optional<Figure> foundFigure = board.findFigureByCoordinatesAndReturn(fieldCoordinates.x, fieldCoordinates.y);
                    if (foundFigure.isPresent() && !foundFigure.get().equals(selectedFigure)) {
                        return foundFigure;
                    }
                }
                checkPosX += i;
            }
        }
        // check is path figure free
        return Optional.empty();
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
                Tuple2<Vector2, Boolean> x = checkIsFieldBlocked(board, foundSignature);
                if (x != null) return x;
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
                Tuple2<Vector2, Boolean> x = checkIsFieldBlocked(board, foundSignature);
                if (x != null) return x;
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
                Tuple2<Vector2, Boolean> x = checkIsFieldBlocked(board, foundSignature);
                if (x != null) return x;
                checkPosX += i;
            }
        }
        // check is path blocks free
        return new Tuple2<>(new Vector2(finalPosition.x, finalPosition.y), true);
    }

    protected Tuple2<Vector2, Boolean> checkIsFieldBlocked(BoardManager board, String foundSignature) {
        if (!Objects.equals(foundSignature, "-1")) {
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            Field field = board.getFieldsMap().get(foundSignature);
            if (field.getBlockedState() == Field.BlockedState.BLOCKED) {
                return new Tuple2<>(new Vector2(fieldCoordinates.x, fieldCoordinates.y), false);
            }
        }
        return new Tuple2<>(null, true);
    }
}
