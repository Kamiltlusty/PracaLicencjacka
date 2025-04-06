package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.board_elements.figures.Queen;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.gameplay.StateBeforeMoveRecord;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

import java.util.*;

public class BoardManager {
    private final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    public final List<Figure> capturedFigures;
    public final List<Figure> promotedPawns;
    private final Assets assets;
    private final BoardUtils boardUtils;
    private final GameFlow gameFlow;
    private Figure selectedFigure;
    private String capturedFigureId;
    private String promotedFigureId;
    private boolean isCapture;
    private boolean isCastling;
    private boolean isPromotion;
    private Vector2 initialPointerPosition;
    private Vector2 pointerPosition;
    private Vector2 initialFieldPosition;
    private Vector2 finalFieldPosition;
    private FieldsSetup fieldsSetup;
    private FiguresSetup figuresSetup;


    public BoardManager(Assets assets, GameFlow gameFlow) {
        this.gameFlow = gameFlow;
        figuresList = new ArrayList<>();
        capturedFigures = new ArrayList<>();
        promotedPawns = new ArrayList<>();
        fieldsMap = new HashMap<>();
        this.assets = assets;
        this.isCapture = false;
        this.isCastling = false;
        this.isPromotion = false;

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

    public boolean validateMove(int screenX, int transformedY, StateBeforeMoveRecord beforeMoveRecord, boolean isCheckingCheckmate) {
        // find figure
        // if isCheckingCheckmate is false screenX/transformedY are in range 0-screen(Width/Height)
        String foundSignature;
        if (!isCheckingCheckmate)
            foundSignature = getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
        else {
            foundSignature = getBoardUtils().findFieldSignatureByCoordinates(screenX, transformedY);

            setInitialFieldPosition(
                selectedFigure.getPosition().x,
                selectedFigure.getPosition().y);
        }
        if (getSelectedFigure() == null) return false;
        Figure figure = getSelectedFigure();
        // check if field was found
        if (Objects.equals(foundSignature, "-1")) return false;
        Vector2 fieldCoordinates = findFieldCoordinates(foundSignature);
        setFinalFieldPosition(fieldCoordinates.x, fieldCoordinates.y);

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
        Figure foundFigure = null;
        Optional<Figure> foundFigureOptional = selectedFigure.isPathFigureFree(
            getInitialFieldPosition(), getFinalFieldPosition(), this.selectedFigure, this);

        if (foundFigureOptional.isPresent())
            foundFigure = foundFigureOptional.get();

        // check whether beating is legal
        if (foundFigure != null) {
            if (!selectedFigure.isBeatingLegal(
                getInitialFieldPosition(),
                getFinalFieldPosition(),
                selectedFigure,
                foundFigure,
                this,
                beforeMoveRecord
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

        // check whether move is promotion
        isPromotion = isPawnPromotion();

        // check whether move is exposing king to check
        if (isExposingKingToCheck(
            getInitialFieldPosition(),
            getFinalFieldPosition(),
            selectedFigure,
            this,
            beforeMoveRecord
        )) return false;

        // check if already is check on active player and is move canceling check
        Team active = gameFlow.getActive();
        boolean isCheckOnActive = false;
        if (active == Team.WHITE && gameFlow.isWhiteInCheck()) isCheckOnActive = true;
        else if (active == Team.BLACK && gameFlow.isBlackInCheck()) isCheckOnActive = true;
        if (isCheckOnActive) {
            if (!isMoveCancelingCheck(beforeMoveRecord)) return false;
        }

        // set check if occurs after move (active player attacks opponent king with selected figure directly)
        boolean isSingleCheck = isSingleCheck(beforeMoveRecord);
        // set check if occurs after move (active player attacks opponent king by moving away selected figure)
        boolean isDiscoveredCheck = isDiscoveredCheck(beforeMoveRecord);

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
        return true;
    }

    private boolean isPawnPromotion() {
        if (selectedFigure.getFigureId().charAt(0) != 'p') return false;
        if (selectedFigure.getTeam() == Team.BLACK && finalFieldPosition.y == 0) {
            promotedFigureId = selectedFigure.getFigureId();
            return true;
        }
        else if (selectedFigure.getTeam() == Team.WHITE && finalFieldPosition.y == 7) {
            promotedFigureId = selectedFigure.getFigureId();
            return true;
        }
        else return false;
    }

    public List<Figure> isExposingCheck(Team active) {
        List<Figure> threateningFigures;
        if (active == Team.WHITE) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("KW")).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.BLACK)
                .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KW"))
                    .orElse(false))
                .toList();
        } else {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("KB")).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.WHITE)
                .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KB"))
                    .orElse(false))
                .toList();
        }
        return threateningFigures;
    }

    private boolean isMoveCancelingCheck(StateBeforeMoveRecord beforeMoveRecord) {
        boolean isCancelling = false;
        simulateMove();
        if (isCancellingCheck().isEmpty()) {
            isCancelling = true;
        }
        undoSimulation(beforeMoveRecord);

        return isCancelling;
    }

    private void undoSimulation(StateBeforeMoveRecord beforeMoveRecord) {
        // restore position (before simulation)
        selectedFigure.setPosition(
            initialFieldPosition.x,
            initialFieldPosition.y
        );
        if (isCapture) {
            // find captured figure in captured figures list
            Optional<Figure> capturedFigure = capturedFigures.stream().filter(f -> f.getFigureId().equals(capturedFigureId)).findFirst();
            if (capturedFigure.isEmpty()) throw new RuntimeException("Can't find captured figure");
            // find captured figure in BeforeMoveRecord class
            Optional<Figure> capturedFigureFromRecord = beforeMoveRecord.getBoardState().keySet().stream()
                .filter(f -> f.getFigureId().equals(capturedFigureId))
                .findFirst();
            if (capturedFigureFromRecord.isEmpty())
                throw new RuntimeException("Can't find captured figure in BeforeMoveRecord class");
            // get initial position of captured figure
            String capturedFigurePositionSignature = beforeMoveRecord.getBoardState().get(capturedFigureFromRecord.get());
            Vector2 capturedFigureInitialPosition = boardUtils.findPositionByFieldSignature(capturedFigurePositionSignature);
            capturedFigure.get().setPosition(
                capturedFigureInitialPosition.x,
                capturedFigureInitialPosition.y
            );
            capturedFigures.remove(capturedFigure.get());
            figuresList.add(capturedFigure.get());
        }

        if (isPromotion) {
            figuresList.remove(selectedFigure);
            // find pawn which was promoted to queen
            Optional<Figure> promotedFigureFromRecord = beforeMoveRecord.getBoardState().keySet().stream()
                .filter(f -> f.getFigureId().equals(promotedFigureId))
                .findFirst();
            if (promotedFigureFromRecord.isEmpty())
                throw new RuntimeException("Can't find promoted pawn in BeforeMoveRecord class");
            // set it back to figures list and as selected figure
            promotedPawns.remove(promotedFigureFromRecord.get());
            figuresList.add(promotedFigureFromRecord.get());
            selectedFigure = promotedFigureFromRecord.get();
        }
    }

    private void simulateMove() {
        if (isPromotion) {
            Team active = gameFlow.getActive();
            Vector2 pawnPositionBeforePromotion = selectedFigure.getPosition();
            // check amount of promoted pawns in one team and increase number by 1 to make index for new figure
            figuresList.remove(selectedFigure);
            promotedPawns.add(selectedFigure);
            long newFigureIdNumber = promotedPawns.stream()
                .filter(f -> f.getTeam().equals(active))
                .count();
            Figure promotedQueen;
            if (active == Team.BLACK) {
                String figureId = "QB" + newFigureIdNumber;
                promotedQueen = new Queen(figureId,
                    assets.manager.get(Assets.QUEEN_TEXTURE_BLACK),
                    pawnPositionBeforePromotion.x,
                    pawnPositionBeforePromotion.y,
                    active);
            } else {
                String figureId = "QW" + newFigureIdNumber;
                promotedQueen = new Queen(figureId,
                    assets.manager.get(Assets.QUEEN_TEXTURE_WHITE),
                    pawnPositionBeforePromotion.x,
                    pawnPositionBeforePromotion.y,
                    active);
            }
            figuresList.add(promotedQueen);
            selectedFigure = promotedQueen;
        }
        // simulate new position
        selectedFigure.setPosition(
            finalFieldPosition.x,
            finalFieldPosition.y
        );
        // simulate beating
        if (isCapture) {
            Optional<Figure> figureToCapture = figuresList.stream().filter(f -> f.getFigureId().equals(capturedFigureId)).findFirst();
            if (figureToCapture.isPresent()) {
                capturedFigures.add(figureToCapture.get());
                figuresList.remove(figureToCapture.get());
            } else throw new RuntimeException("Can't find captured figure");
        }
    }

    private List<Figure> isCancellingCheck() {
        List<Figure> threateningFigures;
        if (gameFlow.getActive() == Team.WHITE) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("KW") && f.getTeam() == Team.WHITE).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.BLACK)
                .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KW"))
                    .orElse(false))
                .toList();
        } else {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("KB") && f.getTeam() == Team.BLACK).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.WHITE)
                .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KB"))
                    .orElse(false))
                .toList();
        }
        return threateningFigures;
    }

    private boolean isDiscoveredCheck(StateBeforeMoveRecord beforeMoveRecord) {
        simulateMove();
        List<Figure> threateningFigures;
        if (gameFlow.getActive() == Team.WHITE) {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("KB")).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.WHITE)
                .filter(f -> !selectedFigure.getFigureId().equals(f.getFigureId()))
                .filter(f -> f.isMoveLegal(f.getPosition(), blackKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), blackKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KB"))
                    .orElse(false))
                .toList();
        } else {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("KW") && f.getTeam() == Team.WHITE).findFirst().get();
            threateningFigures = figuresList.stream()
                .filter(f -> f.getTeam() == Team.BLACK)
                .filter(f -> !selectedFigure.getFigureId().equals(f.getFigureId()))
                .filter(f -> f.isMoveLegal(f.getPosition(), whiteKing.getPosition(), f, this))
                .filter(f -> f.isPathFigureFree(f.getPosition(), whiteKing.getPosition(), f, this)
                    .map(fig -> fig.getFigureId().equals("KW"))
                    .orElse(false))
                .toList();
        }
        undoSimulation(beforeMoveRecord);
        return !threateningFigures.isEmpty();
    }

    private boolean isSingleCheck(StateBeforeMoveRecord beforeMoveRecord) {
        // simulate new position
        simulateMove();
        if (gameFlow.getActive() == Team.WHITE) {
            Figure blackKing = figuresList.stream().filter(f -> f.getFigureId().equals("KB")).findFirst().get();
            // checking whether we can attack king in next move == is king in check by selected figure
            boolean isMoveLegal = selectedFigure.isMoveLegal(
                selectedFigure.getPosition(),
                blackKing.getPosition(),
                selectedFigure,
                this);
            boolean isPathToKingFree = false;
            Optional<Figure> foundFigure = selectedFigure.isPathFigureFree(
                selectedFigure.getPosition(),
                blackKing.getPosition(),
                selectedFigure,
                this);
            if (foundFigure.isPresent()) {
                isPathToKingFree = foundFigure.get().getFigureId().equals("KB");}
            if (isMoveLegal && isPathToKingFree) {
                // restore position before simulation i think it is better to be before setBlackInCheck to not change final state
                undoSimulation(beforeMoveRecord);
                gameFlow.setBlackInCheck(true);
                return true;
            }
        } else if (gameFlow.getActive() == Team.BLACK) {
            Figure whiteKing = figuresList.stream().filter(f -> f.getFigureId().equals("KW")).findFirst().get();
            // checking whether we can attack king in next move == is king in check by selected figure
            boolean isMoveLegal = selectedFigure.isMoveLegal(
                selectedFigure.getPosition(),
                whiteKing.getPosition(),
                selectedFigure,
                this);
            // "KW".equals() this reversed equality check is for purpose if method iPFF will return null the result will be false instead of NullPointerException
            boolean isPathToKingFree = false;
            Optional<Figure> foundFigure = selectedFigure.isPathFigureFree(
                selectedFigure.getPosition(),
                whiteKing.getPosition(),
                selectedFigure,
                this);
            if (foundFigure.isPresent()) {
                isPathToKingFree = foundFigure.get().getFigureId().equals("KB");}
            if (isMoveLegal && isPathToKingFree) {
                // restore position before simulation i think it is better to be before setBlackInCheck to not change final stated
                undoSimulation(beforeMoveRecord);
                gameFlow.setWhiteInCheck(true);
                return true;
            }
        }
        // restore position before simulation i think it is better to be before setBlackInCheck to not change final stated
        undoSimulation(beforeMoveRecord);
        return false;
    }

    private boolean isExposingKingToCheck(Vector2 initialFieldPosition, Vector2 finalFieldPosition, Figure
        selectedFigure, BoardManager boardManager, StateBeforeMoveRecord beforeMoveRecord) {
        boolean isExposing = false;
        simulateMove();
        if (!isExposingCheck(gameFlow.getActive()).isEmpty()) {
            isExposing = true;
        }
        undoSimulation(beforeMoveRecord);
        return isExposing;
    }

    public void moveFigureOverBoard(int screenX, float transformedY) {
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

    public void UndoFigurePlacement() {
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
                selectedFigure = figuresList.get(i);
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

    public boolean isCastling() {
        return isCastling;
    }

    public void setCastling(boolean castling) {
        isCastling = castling;
    }

    public void setAllFieldsFree() {
        fieldsMap.values().forEach(f -> f.setBlockedState(Field.BlockedState.FREE));
    }

    public void setSelectedFigureAsEmpty() {
        this.selectedFigure = null;
    }

    public Figure getSelectedFigure() {
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

    public String getCapturedFigureId() {
        return capturedFigureId;
    }

    public void setCapturedFigureId(String capturedFigureId) {
        this.capturedFigureId = capturedFigureId;
    }

    public List<Figure> getFiguresList() {
        return figuresList;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public void setSelectedFigure(Figure selectedFigure) {
        this.selectedFigure = selectedFigure;
    }

    public String getPromotedFigureId() {
        return promotedFigureId;
    }

    public void setPromotedFigureId(String promotedFigureId) {
        this.promotedFigureId = promotedFigureId;
    }
}
