package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

import java.util.*;

public class BoardManager {
    private final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    private final Assets assets;
    private final BoardUtils boardUtils;
    private final GameFlow gameFlow;
    private Optional<Figure> selectedFigure;
    private Vector2 initialPointerPosition;
    private Vector2 pointerPosition;
    private Vector2 initialFieldPosition;
    private Vector2 finalFieldPosition;
    private FieldsSetup fieldsSetup;
    private FiguresSetup figuresSetup;


    public BoardManager(Assets assets, GameFlow gameFlow) {
        this.gameFlow = gameFlow;
        figuresList = new ArrayList<>();
        fieldsMap = new HashMap<>();
        this.assets = assets;

        // set selected figure as empty
        this.selectedFigure = Optional.empty();
        // initialize mouse pointer
        this.pointerPosition = new Vector2();
        this.initialPointerPosition = new Vector2();
        this.initialFieldPosition = new Vector2();
        this.finalFieldPosition = new Vector2();

        // initialize board utils
        this.boardUtils = new BoardUtils(assets);

        // initialize fields and figures setups
        initializeSetups();

        // draw the chessboard
        fieldsSetup.setFieldsMap();
        setFiguresInitially();
    }

    public Vector2 getInitialPointerPosition() {
        return initialPointerPosition;
    }

    public void setInitialPointerPosition(float x, float y) {
        this.initialPointerPosition.x = x;
        this.initialPointerPosition.y = y;
    }

    public Vector2 getInitialFieldPosition() {
        return initialFieldPosition;
    }

    public Vector2 getFinalFieldPosition() {
        return finalFieldPosition;
    }

    public void setInitialFieldPosition(float x, float y) {
        this.initialFieldPosition.x = x;
        this.initialFieldPosition.y = y;
    }

    public void setFinalFieldPosition(float x, float y) {
        this.finalFieldPosition.x = x;
        this.finalFieldPosition.y = y;
    }

    private void initializeSetups() {
        this.fieldsSetup = new FieldsSetup(this, assets);
        this.figuresSetup = new FiguresSetup(this, assets);
    }

    public void setFiguresInitially() {
        setPlayerFigures();
        setOpponentFigures();
    }

    private void setOpponentFigures() {
        // opponent pawns
        figuresSetup.setOpponentPawns();
        // opponent rooks
        figuresSetup.setOpponentRooks();
        // opponent knights
        figuresSetup.setOpponentKnights();
        // opponent bishops
        figuresSetup.setOpponentBishops();
        // opponent king
        figuresSetup.setOpponentKing();
        // setOpponentQueen
        figuresSetup.setOpponentQueen();
    }

    private void setPlayerFigures() {
        // player pawns
        figuresSetup.setPlayerPawns();
        // player rooks
        figuresSetup.setPlayerRooks();
        // player knights
        figuresSetup.setPlayerKnights();
        // player bishops
        figuresSetup.setPlayerBishops();
        // player king
        figuresSetup.setPlayerKing();
        // setPlayerQueen
        figuresSetup.setPlayerQueen();
    }

    public boolean isCheck(Team active) {
        List<Figure> threateningFigures;
        if (active == Team.WHITE) {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
            List<Figure> whiteFigures = figuresList.stream().filter(f -> f.getTeam() == Team.WHITE).toList();
            threateningFigures = whiteFigures.stream().filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this, true)).toList();
        } else {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
            List<Figure> whiteFigures = figuresList.stream().filter(f -> f.getTeam() == Team.BLACK).toList();
            threateningFigures = whiteFigures.stream().filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this, true)).toList();
        }
        return !threateningFigures.isEmpty();
    }
    public boolean isCheck(Team active, boolean isBeforeMove) {
        List<Figure> threateningFigures;
        if (active == Team.WHITE) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
            List<Figure> whiteFigures = figuresList.stream().filter(f -> f.getTeam() == Team.BLACK).toList();
            threateningFigures = whiteFigures.stream().filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this, true)).toList();
        } else {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
            List<Figure> whiteFigures = figuresList.stream().filter(f -> f.getTeam() == Team.WHITE).toList();
            threateningFigures = whiteFigures.stream().filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this, true)).toList();
        }
        return !threateningFigures.isEmpty();
    }

    public boolean isFigurePlaceable(int screenX, int transformedY) {
        boolean allValid = true;
        // search for field to drop figure
        String foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
        // check if field was found
        if (!Objects.equals(foundSignature, "-1")) {
            Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
            setFinalFieldPosition(fieldCoordinates.x, fieldCoordinates.y);

            Optional<Figure> selectedFigure = getSelectedFigure();
            if (selectedFigure.isPresent() &&
                gameFlow.getActive().equals(selectedFigure.get().getTeam()) &&
                selectedFigure.get().isMoveLegal(
                    getInitialFieldPosition(),
                    getFinalFieldPosition(),
                    selectedFigure.get(),
                    this,
                    false
                )
            ) {
                // if figure stays at the same position do not count it as a move
                if (getInitialFieldPosition().x == getFinalFieldPosition().x
                    && getInitialFieldPosition().y == getFinalFieldPosition().y) {
                    allValid = false;
                } else {
                    selectedFigure.get().setPosition(
                        fieldCoordinates.x,
                        fieldCoordinates.y
                    );
                }

            } else allValid = false;
        } else allValid = false;
        return allValid;
    }

    public void moveFigureOverBoard(int screenX, float transformedY) {
        Optional<Figure> selectedFigure = getSelectedFigure();
        // counting difference between two pointer positions
        Vector2 newTouch = new Vector2(screenX, transformedY);
        Vector2 delta = newTouch.cpy().sub(getPointerPosition());
        // recalculating position
        selectedFigure.ifPresent(figure -> figure.setPosition(
            figure.getPosition().x + delta.x / GlobalVariables.BOARD_FIELD_SIDE_LENGTH,
            figure.getPosition().y + delta.y / GlobalVariables.BOARD_FIELD_SIDE_LENGTH));
        setPointerPosition(newTouch.x, newTouch.y);
    }

    public void UndoFigurePlacement() {
        String foundSignature;
        Optional<Figure> selectedFigure = getSelectedFigure();
        // to make figure come back it needs to be set with field coordinates to become normalized and not go outside chessboard
        foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates((int) getInitialPointerPosition().x, (int) getInitialPointerPosition().y);
        Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
        selectedFigure.ifPresent(figure -> figure.setPosition(
            fieldCoordinates.x,
            fieldCoordinates.y
        ));
    }

    public Vector2 detectFigureOnClick(int screenX, int transformedY) {
        // mark/search for marked field
        String foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
        Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
        // finding figure
        findFigureByCoordinates(fieldCoordinates.x, fieldCoordinates.y);
        return fieldCoordinates;
    }

    public void savePointerPositionInBoardManager(int screenX, int transformedY, Vector2 fieldCoordinates) {
        // remembering pointer position
        setPointerPosition(screenX, transformedY);
        setInitialPointerPosition(getPointerPosition().x, getPointerPosition().y);
        setInitialFieldPosition(fieldCoordinates.x, fieldCoordinates.y);
    }


    public Vector2 findFieldCoordinates(String fieldSignature) {
        Field foundField = fieldsMap.get(fieldSignature);
        float x = foundField.getPosition().x;
        float y = foundField.getPosition().y;
        return new Vector2(x, y);
    }

    public void findFigureByCoordinates(float x, float y) {
        // find figure by coordinates
        for (int i = 0; i < figuresList.size(); i++) {
            if (x == figuresList.get(i).getPosition().x &&
                y == figuresList.get(i).getPosition().y) {
                selectedFigure = Optional.ofNullable(figuresList.get(i));
            }
        }
    }
    public Optional<Figure> findFigureByCoordinatesAndReturn(float x, float y) {
        // find figure by coordinates
        for (int i = 0; i < figuresList.size(); i++) {
            if (x == figuresList.get(i).getPosition().x &&
                y == figuresList.get(i).getPosition().y) {
                return Optional.ofNullable(figuresList.get(i));
            }
        }
        return Optional.empty();
    }
    public List<Figure> findFigureByCoordinatesAndReturn(float x, float y, boolean isMore) {
        // this method helps isMoveExposingKingToCheck
        // find figure by coordinates
        List<Figure> foundFigures = new ArrayList<>();
        for (int i = 0; i < figuresList.size(); i++) {
            if (x == figuresList.get(i).getPosition().x &&
                y == figuresList.get(i).getPosition().y) {
                foundFigures.add(figuresList.get(i));
            }
        }
        return foundFigures;
    }

    public void setAllFieldsFree() {
        fieldsMap.values().forEach(f -> f.setBlockedState(Field.BlockedState.FREE));
    }

    public void setSelectedFigureAsEmpty() {
        this.selectedFigure = Optional.empty();
    }

    public Optional<Figure> getSelectedFigure() {
        return selectedFigure;
    }

    public Map<String, Field> getFieldsMap() {
        return fieldsMap;
    }

    public Vector2 getPointerPosition() {
        return pointerPosition;
    }

    public void setPointerPosition(float x, float y) {
        this.pointerPosition.set(x, y);
    }

    public FieldsSetup getFieldsSetup() {
        return fieldsSetup;
    }

    public FiguresSetup getFiguresSetup() {
        return figuresSetup;
    }

    public BoardUtils getBoardUtils() {
        return boardUtils;
    }

    public GameFlow getGameFlow() {
        return gameFlow;
    }
}
