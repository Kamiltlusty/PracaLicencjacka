package pl.kamil.TetriChess.gameplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.ai.Bot;
import pl.kamil.TetriChess.board_elements.*;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.board_elements.figures.Queen;
import pl.kamil.TetriChess.board_elements.figures.Rook;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.screens.GameOverScreen;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.ShapesManager;

import java.util.*;
import java.util.stream.Collectors;

public class GameFlow {
    private final BoardManager boardManager;
    private final ShapesManager shapesManager;
    private final SpriteBatch batch;
    private final BoardUtils boardUtils;
    private final Assets assets;
    private Shape activeShape;

    private Team active = Team.WHITE;
    private CheckType checkType = CheckType.NONE;
    private boolean whiteInCheck = false;
    private boolean blackInCheck = false;
    private boolean isGameOver = false;
    private boolean touchDownOccurred;
    private boolean touchDraggedOccurred;
    private boolean touchUpOccurred;
    private final Deque<StateBeforeMoveRecord> stateBeforeMoveRecordDeque = new ArrayDeque<>();
    private final Bot bot;
    private static Integer totalMovesCounter;
    private Main main;
    private Team botTeam;
    private int botDepth;


    public GameFlow(Assets assets, SpriteBatch batch, Main main) {
        // crate new game/board
        this.main = main;
        this.boardManager = new BoardManager(assets, this);
        this.shapesManager = new ShapesManager(assets);
        this.batch = batch;
        this.boardUtils = boardManager.getBoardUtils();
        this.assets = assets;
        totalMovesCounter = 0;

        // generate shapes
        shapesManager.generateShapes();
        // set initially active shape
        this.activeShape = null;

        this.bot = new Bot(stateBeforeMoveRecordDeque, boardManager, this);

        // set initially mouse touch occurrences
        setInitialMouseTouchOccurrence();
    }

    public void onTouchDown(int screenX, int transformedY) {
        this.touchDownOccurred = true;
        Vector2 fieldCoordinates = boardManager.detectFigureOnClick(screenX, transformedY);
        // saves all pointer positions for usage in touchDragged and touchUp
        boardManager.savePointerPositionInBoardManager(screenX, transformedY, fieldCoordinates);
    }

    public void onTouchDragged(int screenX, float transformedY) {
        this.touchDownOccurred = true;
        boardManager.moveFigureOverBoard(screenX, transformedY);
    }

    public void onTouchUp(int screenX, int transformedY) throws InterruptedException {
        this.touchUpOccurred = true;
        StateBeforeMoveRecord beforeMoveRecord;

        Figure selectedFigure = boardManager.getSelectedFigure();
        if (selectedFigure == null) return;
        // very important! it lets me use selectedFigure.getPosition() in all the other methods
        selectedFigure.setPosition(
            boardManager.getInitialFieldPosition().x,
            boardManager.getInitialFieldPosition().y);

        // when I don't have check I have to set beforeMoveRecord at first but when check occurs I have to set it before next move
        // i set beforeMoveRecord only once before first move other times i set it after move was made
        if (totalMovesCounter == 0) {
            beforeMoveRecord = new StateBeforeMoveRecord(
                getActive(),
                getCheckType(),
                isWhiteInCheck(),
                isBlackInCheck(),
                isGameOver(),
                boardManager,
                boardUtils,
                activeShape
            );

            stateBeforeMoveRecordDeque.addFirst(beforeMoveRecord);
        } else beforeMoveRecord = stateBeforeMoveRecordDeque.getFirst();

        // check if move is legal on copy of game board
        boolean isValid = boardManager.validateMove(screenX, transformedY, beforeMoveRecord, false);
        // if move turned out to be legal execute it
        if (isValid) {
            executeMove();
            this.prepare();

            // set new state before next players move
            // when check occurs I have to set beforeMoveRecord before next move for checking checkmate
            beforeMoveRecord = new StateBeforeMoveRecord(
                getActive(),
                getCheckType(),
                isWhiteInCheck(),
                isBlackInCheck(),
                isGameOver(),
                boardManager,
                boardUtils,
                activeShape
            );
            stateBeforeMoveRecordDeque.addFirst(beforeMoveRecord);
            // check checkmate if is check or if was check in enemy move
            // or if attacking figure is blocked
//            if (!checkType.equals(CheckType.NONE) ||
//                !stateBeforeMoveRecordDeque.getFirst().getCheckType().equals(CheckType.NONE) ||
//                isBlockedFigureAttackingKing()
//            )
            checkGameOver(beforeMoveRecord);
            // as checkingCheckmate can change some fields I have to reset them again
            boardManager.setCastling(false);
            boardManager.setCapture(false);
            boardManager.setPromotion(false);
            boardManager.setCapturedFigureId(null);
            boardManager.setPromotedFigureId(null);
            boardManager.setSelectedFigureAsEmpty();
            isOver();
            long start = System.currentTimeMillis();
            // bot analysis
            bot.makeMoveAsBot(beforeMoveRecord, botDepth);
            // set new state before next players move
            long end = System.currentTimeMillis();
//            System.out.println("Czas: " + (end - start) + " ms");
            // when check occurs I have to set beforeMoveRecord before next move for checking checkmate
            beforeMoveRecord = new StateBeforeMoveRecord(
                getActive(),
                getCheckType(),
                isWhiteInCheck(),
                isBlackInCheck(),
                isGameOver(),
                boardManager,
                boardUtils,
                activeShape
            );
            stateBeforeMoveRecordDeque.addFirst(beforeMoveRecord);
            checkGameOver(beforeMoveRecord);
//            // as checkingCheckmate can change some fields I have to reset them again
            boardManager.setCastling(false);
            boardManager.setCapture(false);
            boardManager.setPromotion(false);
            boardManager.setCapturedFigureId(null);
            boardManager.setPromotedFigureId(null);
            boardManager.setSelectedFigureAsEmpty();
            isOver();
            totalMovesCounter++;
        } else {
            boardManager.UndoFigurePlacement();
            if (totalMovesCounter == 0) {// it is removing state only if first move is not validated
                // in other case state is set at the end so we just do not add next if there was no valid state
                stateBeforeMoveRecordDeque.removeFirst();
            }
            boardManager.setCastling(false);
            boardManager.setCapture(false);
            boardManager.setPromotion(false);
            boardManager.setCapturedFigureId(null);
            boardManager.setPromotedFigureId(null);
            boardManager.setSelectedFigureAsEmpty();
        }

        touchDownOccurred = false;
        touchDraggedOccurred = false;
        touchUpOccurred = false;
    }

    private boolean isBlockedFigureAttackingKing() {
        List<Field> blockedFields = boardManager.getFieldsMap().values().stream()
            .filter(f -> f.getBlockedState().equals(Field.BlockedState.BLOCKED))
            .toList();
        Set<Vector2> blockedPositions = blockedFields.stream()
            .map(f -> new Vector2(f.getPosition().x, f.getPosition().y))
            .collect(Collectors.toSet());

        List<Figure> blockedFigures = boardManager.getFiguresList().stream()
            .filter(f -> !f.getTeam().equals(getActive()))
            .filter(f -> blockedPositions.contains(f.getPosition()))
            .toList();

        Figure myKing = boardManager.getFiguresList().stream().filter(
                f -> f.getFigureId().charAt(0) == 'K'
            ).filter(f -> f.getTeam().equals(getActive()))
            .findFirst().get();

        Optional<Figure> attackingFigure = blockedFigures.stream()
            .filter(f -> f.isPathFigureFree(f.getPosition(), myKing.getPosition(), f, boardManager).isPresent())
            .findFirst();

        return attackingFigure.isPresent();
    }

    public void updateBotFirstMoveIfNeeded() throws InterruptedException {
        if (totalMovesCounter != 0 || botTeam.equals(Team.BLACK)) return;

        StateBeforeMoveRecord beforeMoveRecord = new StateBeforeMoveRecord(
            getActive(),
            getCheckType(),
            isWhiteInCheck(),
            isBlackInCheck(),
            isGameOver(),
            boardManager,
            boardUtils,
            activeShape
        );

        stateBeforeMoveRecordDeque.addFirst(beforeMoveRecord);
        bot.makeMoveAsBot(beforeMoveRecord, botDepth);

        beforeMoveRecord = new StateBeforeMoveRecord(
            getActive(),
            getCheckType(),
            isWhiteInCheck(),
            isBlackInCheck(),
            isGameOver(),
            boardManager,
            boardUtils,
            activeShape
        );
        stateBeforeMoveRecordDeque.addFirst(beforeMoveRecord);

        checkGameOver(beforeMoveRecord);

        boardManager.setCastling(false);
        boardManager.setCapture(false);
        boardManager.setPromotion(false);
        boardManager.setCapturedFigureId(null);
        boardManager.setPromotedFigureId(null);
        boardManager.setSelectedFigureAsEmpty();
        isOver();

        totalMovesCounter++;
    }


    private void isOver() throws InterruptedException {
        if (main.gameScreen != null) {
            main.gameScreen.render(0);
            Thread.sleep(10000);
        }
        if (isGameOver && isBlackInCheck()) {
            main.setScreen(new GameOverScreen(batch, assets, this, 0));
            System.out.println("White wins ");
        } else if (isGameOver && isWhiteInCheck()) {
            main.setScreen(new GameOverScreen(batch, assets, this, 1));
            System.out.println("Black wins ");
        } else if (isGameOver) {
            main.setScreen(new GameOverScreen(batch, assets, this, 2));
            System.out.println("draw");
        }
    }

    public void checkGameOver(StateBeforeMoveRecord beforeMoveRecord) {
//         check if there is legal move on figures owned by active player
        Map<Figure, List<Vector2>> figuresWithPossibleMoves = boardManager.figuresList.stream()
            .filter(f -> f.getTeam().equals(active))
            .collect(Collectors.toMap(
                f -> f,
                Figure::writeDownPossibleMoves
            ));
        boolean foundValidMove = false;
        for (Map.Entry<Figure, List<Vector2>> figureListEntry : figuresWithPossibleMoves.entrySet()) {
            boardManager.setSelectedFigure(figureListEntry.getKey());
            boolean b = false;
            for (Vector2 move : figureListEntry.getValue()) {
                boardManager.setCastling(false);
                boardManager.setCapture(false);
                boardManager.setPromotion(false);
                boardManager.setCapturedFigureId(null);
                boardManager.setPromotedFigureId(null);
                if (boardManager.validateMove((int) move.x, (int) move.y, beforeMoveRecord, true)) {
                    b = true;
                    break;
                }
            }
            if (b) {
                foundValidMove = true;
                break;
            }
        }
        if (boardManager.figuresList.size() <= 4) {
            if (boardManager.figuresList.size() == 2) {
                isGameOver = true;
            } else if (boardManager.figuresList.size() == 3) {
                Optional<Figure> figureOptional = boardManager.figuresList.stream().filter(f -> f.getFigureId().charAt(0) == 'k'
                    || f.getFigureId().charAt(0) == 'b').findFirst();
                if (figureOptional.isPresent()) {
                    isGameOver = true;
                }
            } else if (boardManager.figuresList.size() == 4) {
                long bishopNumber = boardManager.figuresList.stream().filter(f ->  f.getFigureId().charAt(0) == 'b').count();
                if (bishopNumber == 2) {
                    isGameOver = true;
                }
            }
        }
        if (!foundValidMove) {
            isGameOver = true;
        }
    }

    public void executeMove() {
        if (boardManager.isCapture()) {
            removeCapturedFigure();
        }

        if (boardManager.isCastling()) {
            // need to check it again to find proper rook but do not need to check whether king is moving exactly 2 fields
            if (boardManager.getFinalFieldPosition().x == 6.0 && boardManager.getFinalFieldPosition().y == 0.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(5.0f, 0.0f);
            } else if (boardManager.getFinalFieldPosition().x == 2.0 && boardManager.getFinalFieldPosition().y == 0.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(3.0f, 0.0f);
            } else if (boardManager.getFinalFieldPosition().x == 6.0 && boardManager.getFinalFieldPosition().y == 7.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(7, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(5.0f, 7.0f);
            } else if (boardManager.getFinalFieldPosition().x == 2.0 && boardManager.getFinalFieldPosition().y == 7.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(0, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(3.0f, 7.0f);
            }
        }

        if (boardManager.isPromotion()) {
            Vector2 pawnPositionBeforePromotion = getBoardManager().getSelectedFigure().getPosition();
            // check amount of promoted pawns in one team and increase number by 1 to make index for new figure
            boardManager.figuresList.remove(boardManager.getSelectedFigure());
            boardManager.promotedPawns.add(boardManager.getSelectedFigure());
            long newFigureIdNumber = FigureIdGenerator.generate();
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
            // increase move counter before selected figure becomes new figure (promoted)
            boardManager.getSelectedFigure().setMoveCounter(boardManager.getSelectedFigure().getMoveCounter() + 1);
            boardManager.getFiguresList().add(promotedQueen);
            boardManager.setSelectedFigure(promotedQueen);
        } else {
            // increase move counter for selected figure
            boardManager.getSelectedFigure().setMoveCounter(boardManager.getSelectedFigure().getMoveCounter() + 1);
        }

        boardManager.getSelectedFigure().setPosition(
            boardManager.getFinalFieldPosition().x,
            boardManager.getFinalFieldPosition().y
        );
    }

    private void removeCapturedFigure() {
        Optional<Figure> capturedFigure = boardManager.getFiguresList().stream()
            .filter(f -> f.getFigureId().equals(boardManager.getCapturedFigureId()))
            .filter(f -> !f.getTeam().equals(this.getActive()))
            .findFirst();
        if (capturedFigure.isPresent()) {
            boardManager.getCapturedFigures().add(capturedFigure.get());
            boardManager.getFiguresList().remove(capturedFigure.get());
        } else throw new RuntimeException("Figure that should be in figuresList was not found");
    }

    public void setInitialMouseTouchOccurrence() {
        touchDownOccurred = false;
        touchDraggedOccurred = false;
        touchUpOccurred = false;
    }

    public void setActive() {
        this.active = active.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    public Team getActive() {
        return active;
    }

    public ShapesManager getShapesManager() {
        return shapesManager;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public Shape getActiveShape() {
        return activeShape;
    }

    public void setBotDepth(int botDepth) {
        this.botDepth = botDepth;
    }

    public void prepare() {
        setActive();
        boardManager.setCastling(false);
        boardManager.setCapture(false);
        boardManager.setPromotion(false);
        boardManager.setCapturedFigureId(null);
        boardManager.setPromotedFigureId(null);
        boardManager.setSelectedFigureAsEmpty();
        boardManager.setAllFieldsFree();
        activeShape = shapesManager.getShapes().pollFirst();
        shapesManager.generateShapes();
        boardManager.blockFieldsWithNewShape();
    }

    public void prepare(boolean generateShapes) {
        setActive();
        boardManager.setCastling(false);
        boardManager.setCapture(false);
        boardManager.setPromotion(false);
        boardManager.setCapturedFigureId(null);
        boardManager.setPromotedFigureId(null);
        boardManager.setSelectedFigureAsEmpty();
        boardManager.setAllFieldsFree();
        activeShape = shapesManager.getShapes().pollFirst();
        boardManager.blockFieldsWithNewShape();
    }


    public boolean isWhiteInCheck() {
        return whiteInCheck;
    }

    public void setWhiteInCheck(boolean whiteInCheck) {
        this.whiteInCheck = whiteInCheck;
    }

    public boolean isBlackInCheck() {
        return blackInCheck;
    }

    public void setBlackInCheck(boolean blackInCheck) {
        this.blackInCheck = blackInCheck;
    }

    public boolean isTouchDownOccurred() {
        return touchDownOccurred;
    }

    public void setTouchDownOccurred(boolean touchDownOccurred) {
        this.touchDownOccurred = touchDownOccurred;
    }

    public boolean isTouchDraggedOccurred() {
        return touchDraggedOccurred;
    }

    public void setTouchDraggedOccurred(boolean touchDraggedOccurred) {
        this.touchDraggedOccurred = touchDraggedOccurred;
    }

    public boolean isTouchUpOccurred() {
        return touchUpOccurred;
    }

    public void setTouchUpOccurred(boolean touchUpOccurred) {
        this.touchUpOccurred = touchUpOccurred;
    }

    public void setActive(Team active) {
        this.active = active;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public void setActiveShape(Shape activeShape) {
        this.activeShape = activeShape;
    }

    public Deque<StateBeforeMoveRecord> getStateBeforeMoveRecordDeque() {
        return stateBeforeMoveRecordDeque;
    }

    public void setGameOver(boolean checkmate) {
        isGameOver = checkmate;
    }

    public void setBotTeam(Team botTeam) {
        this.botTeam = botTeam;
    }
}
