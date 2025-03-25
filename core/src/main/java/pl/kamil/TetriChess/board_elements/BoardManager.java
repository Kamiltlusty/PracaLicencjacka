package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.gameplay.StateRecord;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

import java.util.*;

public class BoardManager {
    private final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    private final Assets assets;
    private final BoardUtils boardUtils;
    private final GameFlow gameFlow;
    private Figure selectedFigure;
    private String capturedFigureId;
    private boolean isCapture;
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
        this.isCapture = false;

        // set selected figure as empty
        this.selectedFigure = null;
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

    public BoardManager(Assets assets,
                        GameFlow gameFlow,
                        Map<String, Field> fieldsMap,
                        List<Figure> figuresList,
                        BoardUtils boardUtils,
                        Figure selectedFigure,
                        String capturedFigureId,
                        boolean isCapture,
                        Vector2 initialPointerPosition,
                        Vector2 pointerPosition,
                        Vector2 initialFieldPosition,
                        Vector2 finalFieldPosition,
                        FieldsSetup fieldsSetup,
                        FiguresSetup figuresSetup
    ) {
        this.assets = assets;
        this.gameFlow = gameFlow;
        this.fieldsMap = fieldsMap;
        this.figuresList = figuresList;
        this.boardUtils = boardUtils;
        this.selectedFigure = selectedFigure;
        this.capturedFigureId = capturedFigureId;
        this.isCapture = isCapture;
        this.initialPointerPosition = initialPointerPosition;
        this.pointerPosition = pointerPosition;
        this.initialFieldPosition = initialFieldPosition;
        this.finalFieldPosition = finalFieldPosition;
        this.fieldsSetup = fieldsSetup;
        this.figuresSetup = figuresSetup;
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

//    public BoardManager copy() {
//        return new BoardManager(
//            this.assets,
//            this.gameFlow,
//            this.fieldsMap,
//            this.figuresList,
//            this.boardUtils,
//            this.selectedFigure,
//            this.capturedFigureId,
//            this.isCapture,
//            this.initialPointerPosition,
//            this.pointerPosition,
//            this.initialFieldPosition,
//            this.finalFieldPosition,
//            this.fieldsSetup,
//            this.figuresSetup
//        );
//    }

    public List<Figure> isExposingCheck(Team active) {
        List<Figure> threateningFigures;
        if (active == Team.WHITE) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.BLACK)
                .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this).getFigureId().equals("K"))
                .toList();
        } else {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.WHITE)
                .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this).getFigureId().equals("K"))
                .toList();
        }
        return threateningFigures;
    }

    public boolean validateMove(int screenX, int transformedY, StateRecord record) {

        // find figure
        String foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
        if (getSelectedFigure() == null) return false;
        Figure figure = getSelectedFigure();
        record.setSelectedFigure(figure);
        record.setInitialFieldPosition(figure.getPosition().x, figure.getPosition().y);
        // check if field was found
        if (Objects.equals(foundSignature, "-1")) return false;
        Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
        setFinalFieldPosition(fieldCoordinates.x, fieldCoordinates.y);
        record.setFinalFieldPosition(fieldCoordinates);

        // check whether selected figure belongs to active player
        if (!gameFlow.getActive().equals(figure.getTeam())) return false;

        // if figure stays at the same position do not count it as a move
        if ((getInitialFieldPosition().x == getFinalFieldPosition().x
            && getInitialFieldPosition().y == getFinalFieldPosition().y)) return false;

        // check whether move is legal for that specific figure
        if (!figure.isMoveLegal(
            getInitialFieldPosition(),
            getFinalFieldPosition(),
            figure,
            this
        )) return false;

        // check whether path is free or beating occurs
        Figure foundFigure = selectedFigure.isPathFigureFree(
            getInitialFieldPosition(), getFinalFieldPosition(), this.selectedFigure, this);

        // check whether beating is legal
        if (foundFigure != null) {
            if (!selectedFigure.isBeatingLegal(
                getInitialFieldPosition(),
                getFinalFieldPosition(),
                selectedFigure,
                foundFigure,
                this,
                record
            )) return false;
        }

        // check other rules specific to figure type
        if (!selectedFigure.isSpecificMoveLegal(
            getInitialFieldPosition(),
            getFinalFieldPosition(),
            selectedFigure,
            foundFigure,
            this
        )) return false;

        // check whether move is exposing king to check
        if (isExposingKingToCheck(
            getInitialFieldPosition(),
            getFinalFieldPosition(),
            selectedFigure,
            this
        )) return false;

        // check if already is check on active player and which one check is it and is move canceling check
        Team active = gameFlow.getActive();
        boolean isCheckOnActive = false;
        if (active == Team.WHITE && gameFlow.isWhiteInCheck()) isCheckOnActive = true;
        else if (active == Team.BLACK && gameFlow.isBlackInCheck()) isCheckOnActive = true;
        if (isCheckOnActive) {
            if (!isMoveCancelingCheck()) return false;
        }

        // set check if occurs after move (active player attacks opponent king with selected figure directly)
        boolean isSingleCheck = isSingleCheck();
        // set check if occurs after move (active player attacks opponent king by moving away selected figure)
        boolean isDiscoveredCheck = isDiscoveredCheck();

        // set type of check
        if (isSingleCheck && isDiscoveredCheck) {
            gameFlow.setCheckType(CheckType.DOUBLE_CHECK);
        } else if (isSingleCheck) {
            gameFlow.setCheckType(CheckType.SINGLE_CHECK);
        } else if (isDiscoveredCheck) {
            gameFlow.setCheckType(CheckType.DISCOVERED_CHECK);
        } else {
            gameFlow.setCheckType(CheckType.NONE);
        }

//        if (gameFlow.getActive() == Team.WHITE && gameFlow.isWhiteInCheck()) {
//            List<Figure> threateningFiguresList = isCheck(Team.BLACK);
//            if (threateningFiguresList.isEmpty() ||
//                threateningFiguresList.size() == 1 &&
//                    threateningFiguresList.getFirst().getFigureId().equals(capturedFigureId)) {
//                gameFlow.setWhiteInCheck(false);
//                return true;
//            } else return false;
//        } else if (gameFlow.getActive() == Team.BLACK && gameFlow.isBlackInCheck()) {
//            List<Figure> threateningFiguresList = isCheck(Team.WHITE);
//            if (threateningFiguresList.isEmpty() ||
//                threateningFiguresList.size() == 1 &&
//                    threateningFiguresList.getFirst().getFigureId().equals(capturedFigureId)) {
//                gameFlow.setBlackInCheck(false);
//                return true;
//            } else return false;
//        } else if (gameFlow.getActive() == Team.WHITE) {
//            List<Figure> whiteFiguresList = isCheck(Team.WHITE);
//            List<Figure> blackFiguresList = isCheck(Team.BLACK);
//            if (!whiteFiguresList.isEmpty()) {
//                gameFlow.setBlackInCheck(true);
//                return true;
//            } else if (!blackFiguresList.isEmpty()) {
//                gameFlow.setWhiteInCheck(true);
//                return true;
//            }
//        } else if (gameFlow.getActive() == Team.BLACK) {
//            List<Figure> whiteFiguresList = isCheck(Team.WHITE);
//            List<Figure> blackFiguresList = isCheck(Team.BLACK);
//
//            if (!blackFiguresList.isEmpty()) {
//                gameFlow.setWhiteInCheck(true);
//                return true;
//            } else if (!whiteFiguresList.isEmpty()) {
//                gameFlow.setBlackInCheck(true);
//                return true;
//            }
//        }

//        StateRecord record = stateRecordDeque.getFirst(); tylko w ramach przypomnienia
//        selectedFigure.setPosition(
//            record.getInitialFieldPosition().x,
//            record.getInitialFieldPosition().y
//        );
//        gameFlow.setActive(record.getActive());
//        stateRecordDeque.removeFirst();
            return true;
        }

    private boolean isMoveCancelingCheck() {
        boolean isCancelling = false;
        selectedFigure.setPosition(
            finalFieldPosition.x,
            finalFieldPosition.y
        );
        if (isCancellingCheck().isEmpty()) {
            isCancelling = true;
        }
        return isCancelling;
    }

    private List<Figure> isCancellingCheck() {
        List<Figure> threateningFigures;
        if (gameFlow.getActive() == Team.WHITE) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.BLACK)
                .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this).getFigureId().equals("K"))
                .toList();
        } else {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.WHITE)
                .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this).getFigureId().equals("K"))
                .toList();
        }
        return threateningFigures;
    }

    private boolean isDiscoveredCheck () {
            List<Figure> threateningFigures;
            if (gameFlow.getActive() == Team.WHITE) {
                Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
                threateningFigures = figuresList.stream()
                    .filter(f -> f.getTeam() == Team.WHITE)
                    .filter(f -> !selectedFigure.getFigureId().equals(f.getFigureId()))
                    .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                    .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this).getFigureId().equals("K"))
                    .toList();
            } else {
                Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
                threateningFigures = figuresList.stream()
                    .filter(f -> f.getTeam() == Team.BLACK)
                    .filter(f -> !selectedFigure.getFigureId().equals(f.getFigureId()))
                    .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                    .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this).getFigureId().equals("K"))
                    .toList();
            }
            return !threateningFigures.isEmpty();
        }

        private boolean isSingleCheck () {
            if (gameFlow.getActive() == Team.WHITE) {
                Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.BLACK).findFirst().get();
                boolean isMoveLegal = selectedFigure.isMoveLegal(
                    selectedFigure.getPosition(),
                    blackKing.getPosition(),
                    selectedFigure,
                    this);
                boolean isPathFigureFree = selectedFigure.isPathFigureFree(
                    selectedFigure.getPosition(),
                    blackKing.getPosition(),
                    selectedFigure,
                    this).getFigureId().equals("K");
                if (isMoveLegal && isPathFigureFree) {
                    gameFlow.setBlackInCheck(true);
                    return true;
                }
            } else if (gameFlow.getActive() == Team.BLACK) {
                Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("K") && f.getTeam() == Team.WHITE).findFirst().get();
                boolean isMoveLegal = selectedFigure.isMoveLegal(
                    selectedFigure.getPosition(),
                    whiteKing.getPosition(),
                    selectedFigure,
                    this);
                boolean isPathFigureFree = selectedFigure.isPathFigureFree(
                    selectedFigure.getPosition(),
                    whiteKing.getPosition(),
                    selectedFigure,
                    this).getFigureId().equals("K");
                if (isMoveLegal && isPathFigureFree) {
                    gameFlow.setWhiteInCheck(true);
                    return true;
                }
            }
            return false;
        }


        private boolean isExposingKingToCheck (Vector2 initialFieldPosition, Vector2 finalFieldPosition, Figure
        selectedFigure, BoardManager boardManager){
            boolean isExposing = false;
            selectedFigure.setPosition(
                finalFieldPosition.x,
                finalFieldPosition.y
            );
            if (!isExposingCheck(gameFlow.getActive()).isEmpty()) {
                isExposing = true;
            }
            return isExposing;
        }

        public void moveFigureOverBoard(int screenX, float transformedY){
            Figure selectedFigure = getSelectedFigure();
            if (selectedFigure == null) return;
            // counting difference between two pointer positions
            Vector2 newTouch = new Vector2(screenX, transformedY);
            Vector2 delta = newTouch.cpy().sub(getPointerPosition());
            // recalculating position
            selectedFigure.setPosition(
                selectedFigure.getPosition().x + delta.x / GlobalVariables.BOARD_FIELD_SIDE_LENGTH,
                selectedFigure.getPosition().y + delta.y / GlobalVariables.BOARD_FIELD_SIDE_LENGTH);
            setPointerPosition(newTouch.x, newTouch.y);
        }

        public void UndoFigurePlacement () {
            String foundSignature;
            Figure selectedFigure = getSelectedFigure();
            if (selectedFigure == null) return;
            // to make figure come back it needs to be set with field coordinates to become normalized and not go outside chessboard
            foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates((int) getInitialPointerPosition().x, (int) getInitialPointerPosition().y);
            Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
            selectedFigure.setPosition(
                fieldCoordinates.x,
                fieldCoordinates.y
            );
        }

        public Vector2 detectFigureOnClick ( int screenX, int transformedY){
            // mark/search for marked field
            String foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
            Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
            // finding figure
            findFigureByCoordinates(fieldCoordinates.x, fieldCoordinates.y);
            return fieldCoordinates;
        }

        public void savePointerPositionInBoardManager ( int screenX, int transformedY, Vector2 fieldCoordinates){
            // remembering pointer position
            setPointerPosition(screenX, transformedY);
            setInitialPointerPosition(getPointerPosition().x, getPointerPosition().y);
            setInitialFieldPosition(fieldCoordinates.x, fieldCoordinates.y);
        }


        public Vector2 findFieldCoordinates (String fieldSignature){
            Field foundField = fieldsMap.get(fieldSignature);
            float x = foundField.getPosition().x;
            float y = foundField.getPosition().y;
            return new Vector2(x, y);
        }

        public void findFigureByCoordinates ( float x, float y){
            // find figure by coordinates
            for (int i = 0; i < figuresList.size(); i++) {
                if (x == figuresList.get(i).getPosition().x &&
                    y == figuresList.get(i).getPosition().y) {
                    selectedFigure = figuresList.get(i);
                }
            }
        }

        public Optional<Figure> findFigureByCoordinatesAndReturn ( float x, float y){
            // find figure by coordinates
            for (int i = 0; i < figuresList.size(); i++) {
                if (x == figuresList.get(i).getPosition().x &&
                    y == figuresList.get(i).getPosition().y) {
                    return Optional.ofNullable(figuresList.get(i));
                }
            }
            return Optional.empty();
        }

        public List<Figure> findFigureByCoordinatesAndReturn ( float x, float y, boolean isMore){
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

        public void setAllFieldsFree () {
            fieldsMap.values().forEach(f -> f.setBlockedState(Field.BlockedState.FREE));
        }

        public void setSelectedFigureAsEmpty () {
            this.selectedFigure = null;
        }

        public Figure getSelectedFigure () {
            return selectedFigure;
        }

        public Map<String, Field> getFieldsMap () {
            return fieldsMap;
        }

        public Vector2 getPointerPosition () {
            return pointerPosition;
        }

        public void setPointerPosition ( float x, float y){
            this.pointerPosition.set(x, y);
        }

        public FieldsSetup getFieldsSetup () {
            return fieldsSetup;
        }

        public FiguresSetup getFiguresSetup () {
            return figuresSetup;
        }

        public BoardUtils getBoardUtils () {
            return boardUtils;
        }

        public GameFlow getGameFlow () {
            return gameFlow;
        }

        public String getCapturedFigureId () {
            return capturedFigureId;
        }

        public void setCapturedFigureId (String capturedFigureId){
            this.capturedFigureId = capturedFigureId;
        }

        public List<Figure> getFiguresList () {
            return figuresList;
        }

        public boolean isCapture () {
            return isCapture;
        }

        public void setCapture ( boolean capture){
            isCapture = capture;
        }


    }
