package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.resources.Assets;

import java.util.*;

public class BoardManager {
    private final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    private final Assets assets;
    private final BoardUtils boardUtils;
    private Optional<Figure> selectedFigure;
    private Vector2 initialPointerPosition;
    private Vector2 pointerPosition;
    private Vector2 initialFieldPosition;
    private Vector2 finalFieldPosition;
    private FieldsSetup fieldsSetup;
    private FiguresSetup figuresSetup;


    public BoardManager(Assets assets) {
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
}
