package pl.kamil.TetriChess.ai;

import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.board_elements.figures.Pawn;
import pl.kamil.TetriChess.board_elements.figures.Rook;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.gameplay.StateBeforeMoveRecord;

import java.util.*;
import java.util.stream.Collectors;

public class Bot {
    Map<Figure, List<Vector2>> validMoves = new HashMap<>();
    private final Deque<StateBeforeMoveRecord> stateBeforeMoveRecordDeque;
    private final BoardManager boardManager;
    private final GameFlow gameFlow;
    private final Deque<Map<Integer, List<Tuple2<String, Vector2>>>> evaluationsDeque = new ArrayDeque<>(); // <level_of_deep<evaluation_score, List<Tuple2<FigureId, FinalPosition>>>> for one key i can have few results
    private Integer evaluationScore; // bot is trying to make score on minus and the lowest he can
    private Integer evaluationScoreRoot; // bot is trying to make score on minus and the lowest he can
    private final TranspositionTable transpositionTable = new TranspositionTable();

    public Bot(Deque<StateBeforeMoveRecord> beforeMoveRecordDeque, BoardManager boardManager, GameFlow gameFlow) {
        this.stateBeforeMoveRecordDeque = beforeMoveRecordDeque;
        this.boardManager = boardManager;
        this.gameFlow = gameFlow;
        this.evaluationScoreRoot = 0;
    }

    public void makeMoveAsBot(StateBeforeMoveRecord beforeMoveRecord, int depth) {
        Tuple2<String, Vector2> bestMove = findBestMove(beforeMoveRecord, depth);

        if (bestMove != null) {
            Optional<Figure> bestFigureOptional = boardManager.figuresList.stream()
                .filter(f -> f.getFigureId().equals(bestMove._1))
                .findFirst();
            Figure bestFigure;
            if (bestFigureOptional.isPresent()) {
                bestFigure = bestFigureOptional.get();
            } else throw new RuntimeException("Should have found a figure because found bestMove");

            prepareForMoveExecution(bestFigure, bestMove._2);
            gameFlow.executeMove();
            gameFlow.prepare();
        }
    }

    public Tuple2<String, Vector2> findBestMove(StateBeforeMoveRecord beforeMoveRecord, int depth) {
        int minEval = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Tuple2<String, Vector2> bestMove = null;
        List<MoveDetails> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
        if (legalMovesForMinimax.isEmpty()) {
            gameFlow.setGameOver(true);
        }
        legalMovesForMinimax.sort((m1, m2) -> Float.compare(m2.getScore(), m1.getScore()));

        for (var moveDetails : legalMovesForMinimax) {
            Figure selectedFig = moveDetails.getFigure();
            Vector2 move = moveDetails.getMove();
            prepareForMoveExecution(selectedFig, move);
            gameFlow.executeMove();
            gameFlow.prepare(false);
//                    resetFields(); is needed?
            // set new state as actual
            beforeMoveRecord = setNewStateBeforeMoveRecord();
            gameFlow.checkGameOver(beforeMoveRecord);
            // reset fields
            resetFields();
            // count result of evaluation
            int eval = minimax(beforeMoveRecord, depth - 1, true, alpha, beta);

            if (eval < minEval) {
                minEval = eval;
                bestMove = new Tuple2<>(selectedFig.getFigureId(), move);
            }

            beta = Math.min(beta, eval);

            // undo latest move to go back up a level at first i need to remove latest state
            // because we can regain wanted state from penultimate state not from latest state
            // before we have to re add shape because prepare is removing it
            gameFlow.getShapesManager().getShapes().addFirst(
                stateBeforeMoveRecordDeque
                    .removeFirst()
                    .getActiveShape());
            //
            beforeMoveRecord = stateBeforeMoveRecordDeque.getFirst();
            undoLastMove(selectedFig, beforeMoveRecord);

            if (beta <= alpha) {
                break;
            }
        }

        return bestMove;
    }

    public int minimax(StateBeforeMoveRecord beforeMoveRecord, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        String boardHash = beforeMoveRecord.getSimpleHash();

        // checking for eval in transposition table
        if (transpositionTable.contains(boardHash)) {
            TranspositionEntry entry = transpositionTable.get(boardHash);

            if (entry.getDepth() >= depth) {
                return entry.getEval();
            }
        }

        // decide whether to go back in stack or go deeper
        if (depth == 0 || gameFlow.isGameOver()) {
            return evaluatePosition(beforeMoveRecord, depth);
        }
        // choose are we maximizing the evaluation score
        if (isMaximizingPlayer) {
            // take smallest value because we are searching for the biggest for maximizer
            int maxEval = Integer.MIN_VALUE;
            List<MoveDetails> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
            if (legalMovesForMinimax.isEmpty()) {
                gameFlow.setGameOver(true);
            }
            legalMovesForMinimax.sort((m1, m2) -> Float.compare(m2.getScore(), m1.getScore()));

            for (var moveDetails : legalMovesForMinimax) {
                Figure selectedFig = moveDetails.getFigure();
                Vector2 move = moveDetails.getMove();
                prepareForMoveExecution(selectedFig, move);
                gameFlow.executeMove();
                gameFlow.prepare(false);
//                    resetFields(); is needed?
                // set new state as actual
                beforeMoveRecord = setNewStateBeforeMoveRecord();
                gameFlow.checkGameOver(beforeMoveRecord);
                // reset fields
                resetFields();
                // count result of evaluation
                int eval = minimax(beforeMoveRecord, depth - 1, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                // undo latest move to go back up a level at first i need to remove latest state
                // because we can regain wanted state from penultimate state not from latest state
                // before we have to re add shape because prepare is removing it
                gameFlow.getShapesManager().getShapes().addFirst(
                    stateBeforeMoveRecordDeque
                        .removeFirst()
                        .getActiveShape());
                // reference needs has to be changed
                beforeMoveRecord = stateBeforeMoveRecordDeque.getFirst();
                undoLastMove(selectedFig, beforeMoveRecord);

                if (beta <= alpha) {
                    return maxEval;
                }
            }

            transpositionTable.put(boardHash, maxEval, depth);
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            List<MoveDetails> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
            if (legalMovesForMinimax.isEmpty()) {
                gameFlow.setGameOver(true);
            }
            legalMovesForMinimax.sort((m1, m2) -> Float.compare(m2.getScore(), m1.getScore()));

            for (var moveDetails : legalMovesForMinimax) {
                Figure selectedFig = moveDetails.getFigure();
                Vector2 move = moveDetails.getMove();
                prepareForMoveExecution(selectedFig, move);
                gameFlow.executeMove();
                gameFlow.prepare(false);
//                    resetFields(); is needed?
                // set new state as actual
                beforeMoveRecord = setNewStateBeforeMoveRecord();
                gameFlow.checkGameOver(beforeMoveRecord);
                // reset fields
                resetFields();
                // count result of evaluation
                int eval = minimax(beforeMoveRecord, depth - 1, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                // undo latest move to go back up a level at first i need to remove latest state
                // because we can regain wanted state from penultimate state not from latest state
                // before we have to re add shape because prepare is removing it
                gameFlow.getShapesManager().getShapes().addFirst(
                    stateBeforeMoveRecordDeque
                        .removeFirst()
                        .getActiveShape());
                // reference needs has to be changed
                beforeMoveRecord = stateBeforeMoveRecordDeque.getFirst();
                undoLastMove(selectedFig, beforeMoveRecord);

                if (beta <= alpha) {
                    return minEval;
                }
            }

            transpositionTable.put(boardHash, minEval, depth);
            return minEval;
        }
    }

    private StateBeforeMoveRecord setNewStateBeforeMoveRecord() {
        // add it as a new state to state before move record
        StateBeforeMoveRecord newBeforeMoveRecord = new StateBeforeMoveRecord(
            gameFlow.getActive(),
            gameFlow.getCheckType(),
            gameFlow.isWhiteInCheck(),
            gameFlow.isBlackInCheck(),
            gameFlow.isGameOver(),
            boardManager,
            boardManager.getBoardUtils(),
            gameFlow.getActiveShape()
        );
        stateBeforeMoveRecordDeque.addFirst(newBeforeMoveRecord);
        return stateBeforeMoveRecordDeque.getFirst();
    }

    private List<MoveDetails> getLegalMovesForMinimax(StateBeforeMoveRecord beforeMoveRecord) {
        // create list for legal moves
        List<MoveDetails> validMoves = new ArrayList<>();
        // check if there is legal move on figures owned by active player
        Map<Figure, List<Vector2>> figuresWithPossibleMoves = boardManager.figuresList.stream()
            .filter(f -> f.getTeam().equals(beforeMoveRecord.getActive()))
            .collect(Collectors.toMap(
                f -> f,
                Figure::writeDownPossibleMoves
            ));
        for (Map.Entry<Figure, List<Vector2>> figureListEntry : figuresWithPossibleMoves.entrySet()) {
            boardManager.setSelectedFigure(figureListEntry.getKey());
            for (Vector2 move : figureListEntry.getValue()) {
                // validate move
                if (boardManager.validateMove((int) move.x, (int) move.y, beforeMoveRecord, true)) {
                    // evaluate move
                    int score = evaluateMove();

                    validMoves.add(new MoveDetails(
                        figureListEntry.getKey(),
                        move,
                        score
                    ));
                }
                boardManager.setCastling(false);
                boardManager.setCapture(false);
                boardManager.setPromotion(false);
                boardManager.setCapturedFigureId(null);
                boardManager.setPromotedFigureId(null);
            }
        }
        resetFields();
        return validMoves;
    }

    private int evaluateMove() {
        int score = 0;

        // 1. capturing enemy figure
        if (boardManager.isCapture()) {
            Optional<Figure> toCapture = boardManager.getFiguresList().stream()
                .filter(f -> boardManager.getCapturedFigureId().equals(f.getFigureId()))
                .findFirst();
            if (toCapture.isPresent()) {
                score += toCapture.get().getValue();
            } else throw new RuntimeException("Figure to capture should have been found");
        }

        // 2. castling
        if (boardManager.isCastling()) {
            score += 5;
        }

        // 3. pawn promotion
        if (boardManager.isPromotion()) {
            score += 90;
        }

        // 5. central fields
        Vector2 finalFieldPosition = boardManager.getFinalFieldPosition();
        if (isClassicCenter(finalFieldPosition)) {
            score += 2;
        } else if (isExtendedCenter(finalFieldPosition)) {
            score += 1;
        }

        // giving check
        boardManager.simulateMove();
        if (gameFlow.getActive() == Team.WHITE) {
            Figure blackKing = boardManager.getKing(false);
            // checking whether we can attack king in next move == is king in check by selected figure
            boolean isMoveLegal = boardManager.getSelectedFigure().isMoveLegal(
                boardManager.getSelectedFigure().getPosition(),
                blackKing.getPosition(),
                boardManager.getSelectedFigure(),
                boardManager);
            boolean isPathToKingFree = false;
            Optional<Figure> foundFigure = boardManager.getSelectedFigure().isPathFigureFree(
                boardManager.getSelectedFigure().getPosition(),
                blackKing.getPosition(),
                boardManager.getSelectedFigure(),
                boardManager);
            if (foundFigure.isPresent()) {
                isPathToKingFree = foundFigure.get().getFigureId().equals("KB");
            }
            boolean isBeatingLegal = false;
            if (boardManager.getSelectedFigure().isBeatingLegal(boardManager.getSelectedFigure().getPosition(), blackKing.getPosition(), boardManager.getSelectedFigure(), blackKing, boardManager, stateBeforeMoveRecordDeque.getFirst(), true)) {
                isBeatingLegal = true;
            }
            if (isMoveLegal && isPathToKingFree && isBeatingLegal) {
                // restore position before simulation i think it is better to be before setBlackInCheck to not change final state
                score += 5;
            }
        } else if (gameFlow.getActive() == Team.BLACK) {
            Figure whiteKing = boardManager.getKing(true);
            // checking whether we can attack king in next move == is king in check by selected figure
            boolean isMoveLegal = boardManager.getSelectedFigure().isMoveLegal(
                boardManager.getSelectedFigure().getPosition(),
                whiteKing.getPosition(),
                boardManager.getSelectedFigure(),
                boardManager);
            // "KW".equals() boardManager reversed equality check is for purpose if method iPFF will return null the result will be false instead of NullPointerException
            boolean isPathToKingFree = false;
            Optional<Figure> foundFigure = boardManager.getSelectedFigure().isPathFigureFree(
                boardManager.getSelectedFigure().getPosition(),
                whiteKing.getPosition(),
                boardManager.getSelectedFigure(),
                boardManager);
            if (foundFigure.isPresent()) {
                isPathToKingFree = foundFigure.get().getFigureId().equals("KW");
            }
            boolean isBeatingLegal = false;
            if (boardManager.getSelectedFigure().isBeatingLegal(boardManager.getSelectedFigure().getPosition(), whiteKing.getPosition(), boardManager.getSelectedFigure(), whiteKing, boardManager, stateBeforeMoveRecordDeque.getFirst(), true)) {
                isBeatingLegal = true;
            }
            if (isMoveLegal && isPathToKingFree && isBeatingLegal) {
                // restore position before simulation i think it is better to be before setBlackInCheck to not change final stated
                score += 5;
            }
        }
        boardManager.undoSimulation(stateBeforeMoveRecordDeque.getFirst());
        return score;
    }

    private boolean isExtendedCenter(Vector2 pos) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        return x >= 2 && x <= 5 && y >= 2 && y <= 5 && !isClassicCenter(pos);
    }

    private boolean isClassicCenter(Vector2 pos) {
        return (pos.equals(new Vector2(3, 3)) || pos.equals(new Vector2(3, 4)) ||
            pos.equals(new Vector2(4, 3)) || pos.equals(new Vector2(4, 4)));
    }

    private void undoLastMove(Figure selectedFig, StateBeforeMoveRecord beforeMoveRecord) {
        // check whether amount of figures in two collections is different
        // if it would be promotion instead of beating sizes would be the same
        if (beforeMoveRecord.getBoardState().size() != boardManager.getFiguresList().size()) {
            // check whether in captured list is a figure that is in beforeMoveRecord boardState on board
            Optional<Figure> capturedToRestore = boardManager.getCapturedFigures().stream()
                .filter(f -> !f.getTeam().equals(beforeMoveRecord.getActive()))
                .filter(f ->
                    beforeMoveRecord.getBoardState().keySet().stream()
                        .anyMatch(fig ->
                            !fig.getTeam().equals(beforeMoveRecord.getActive()) &&
                                fig.getFigureId().equals(f.getFigureId())
                        ))
                .findFirst();
            capturedToRestore.ifPresentOrElse(
                f -> {
                    boardManager.getFiguresList().add(f);
                    boardManager.getCapturedFigures().remove(f);
                },
                () -> {
                    throw new RuntimeException("Collections sizes are different but we have not found figure to restore");
                }
            );
        }

        // get back rook if was castling
        if (selectedFig.getFigureId().charAt(0) == 'K' && selectedFig.getMoveCounter() == 1) {
            if (selectedFig.getPosition().x == 6 && selectedFig.getPosition().y == 0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7, 0);
            } else if (selectedFig.getPosition().x == 2 && selectedFig.getPosition().y == 0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0, 0);
            } else if (selectedFig.getPosition().x == 6 && selectedFig.getPosition().y == 7) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7, 7);
            } else if (selectedFig.getPosition().x == 2 && selectedFig.getPosition().y == 7) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0, 7);
            }
        }

        // if was promotion then in figures list we would have queen with id like QW1 or QB2 or QW2 ...
        // start checking if there are promotedPawns and selectedFig is pawn
        Optional<Figure> isPawnInPromoted = boardManager.getPromotedPawns().stream()
            .filter(f -> selectedFig.getFigureId().equals(f.getFigureId()))
            .findFirst();
        if (isPawnInPromoted.isPresent()) {
            // check whether in figures list is a queen that is not in beforeMoveRecord boardState on board
            Optional<Figure> promotedToTrash = boardManager.getFiguresList().stream()
                .filter(f -> f.getTeam().equals(beforeMoveRecord.getActive()))
                .filter(f -> f.getFigureId().charAt(0) == 'Q')
                .filter(f ->
                    beforeMoveRecord.getBoardState().keySet().stream()
                        .noneMatch(fig ->
                            fig.getTeam().equals(beforeMoveRecord.getActive()) &&
                                fig.getFigureId().equals(f.getFigureId())
                        ))
                .findFirst();
            if (promotedToTrash.isPresent()) {
                Figure f = promotedToTrash.get();
                boardManager.getFiguresList().remove(f);
            } else {
                throw new RuntimeException("Should have found queen promoted from pawn");
            }
            boardManager.getPromotedPawns().remove(selectedFig);
            boardManager.getFiguresList().add(selectedFig);
        }

        // set game flow fields from before move state
        gameFlow.setWhiteInCheck(beforeMoveRecord.isWhiteInCheck());
        gameFlow.setBlackInCheck(beforeMoveRecord.isBlackInCheck());
        gameFlow.setCheckType(beforeMoveRecord.getCheckType());
        gameFlow.setGameOver(beforeMoveRecord.isGameOver());
        gameFlow.setActive(beforeMoveRecord.getActive());
        gameFlow.setActiveShape(beforeMoveRecord.getActiveShape());
        // set figure on initial position
        String fieldSignature = beforeMoveRecord.getBoardState().get(selectedFig);
        Vector2 initialPosition = boardManager.getBoardUtils().findPositionByFieldSignature(fieldSignature);
        selectedFig.setPosition(
            initialPosition.x,
            initialPosition.y
        );
        selectedFig.setMoveCounter(selectedFig.getMoveCounter() - 1);
        resetFields();
    }

    private void resetFields() {
        // reset fields
        boardManager.setCastling(false);
        boardManager.setCapture(false);
        boardManager.setPromotion(false);
        boardManager.setCapturedFigureId(null);
        boardManager.setPromotedFigureId(null);
        boardManager.setSelectedFigureAsEmpty();
    }


    private void getLegalMoves(StateBeforeMoveRecord beforeMoveRecord) {
        // check if there is legal move on figures owned by active player
        Map<Figure, List<Vector2>> figuresWithPossibleMoves = boardManager.figuresList.stream()
            .filter(f -> f.getTeam().equals(beforeMoveRecord.getActive()))
            .collect(Collectors.toMap(
                f -> f,
                Figure::writeDownPossibleMoves
            ));
        for (Map.Entry<Figure, List<Vector2>> figureListEntry : figuresWithPossibleMoves.entrySet()) {
            boardManager.setSelectedFigure(figureListEntry.getKey());
            for (Vector2 move : figureListEntry.getValue()) {
                boardManager.setCastling(false);
                boardManager.setCapture(false);
                boardManager.setPromotion(false);
                boardManager.setCapturedFigureId(null);
                boardManager.setPromotedFigureId(null);
                if (boardManager.validateMove((int) move.x, (int) move.y, beforeMoveRecord, true)) {
                    validMoves.compute(figureListEntry.getKey(), (key, list) -> {
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            list.add(move);
                            return list;
                        }
                    );
                }
            }
        }
    }

    private void prepareForMoveExecution(Figure selectedFig, Vector2 move) {
        boardManager.setSelectedFigure(selectedFig);
        boardManager.setFinalFieldPosition(move.x, move.y);
        // if is any figure on final position -> set to beat
        Optional<Figure> figureToBeat = boardManager.figuresList.stream()
            .filter(f -> move.equals(f.getPosition()))
            .findFirst();
        if (figureToBeat.isPresent()) {
            boardManager.setCapture(true);
            boardManager.setCapturedFigureId(figureToBeat.get().getFigureId());
        }
        // if is castling -> set castling
        if (selectedFig.getFigureId().charAt(0) == 'K' && Math.abs(selectedFig.getPosition().x - move.x) == 2) {
            boardManager.setCastling(true);
//            boolean specificMoveLegal = selectedFig.isSpecificMoveLegal(selectedFig.getPosition(), move, selectedFig, null, boardManager);
//            if (specificMoveLegal) {
//                boardManager.setCastling(true);
//            }
        }
        // if is promotion -> set promotion
        if (boardManager.isPawnPromotion()) boardManager.setPromotion(true);
    }

    public int evaluatePosition(StateBeforeMoveRecord beforeMoveRecord, int currentDepth) {
        int whiteScore = 0;
        int blackScore = 0;

        if (gameFlow.isWhiteInCheck() && gameFlow.isGameOver()) {
            blackScore += 10000;
            return calcPosDiff(whiteScore, blackScore);
        }
        if (gameFlow.isBlackInCheck() && gameFlow.isGameOver()) {
            whiteScore += 10000;
            return calcPosDiff(whiteScore, blackScore);
        }

        if (gameFlow.isWhiteInCheck()) {
            blackScore += 4;
        }
        if (gameFlow.isBlackInCheck()) {
            whiteScore += 4;
        }

        Map<Figure, String> board = beforeMoveRecord.getBoardState();
        int blackFiguresValueSum = board.keySet().stream()
            .filter(f -> f.getTeam() == Team.BLACK)
            .mapToInt(Figure::getValue)
            .sum();

        int whiteFiguresValueSum = board.keySet().stream()
            .filter(f -> f.getTeam() == Team.WHITE)
            .mapToInt(Figure::getValue)
            .sum();
        int diff = whiteFiguresValueSum - blackFiguresValueSum;

        for (Map.Entry<Figure, String> entry : board.entrySet()) {
            Figure figure = entry.getKey();
            Vector2 position = boardManager.getBoardUtils().findPositionByFieldSignature(entry.getValue());
            int value = figure.getValue();

            if (figure.getFigureId().charAt(0) != 'K') {
                // base material value
                if (figure.getTeam() == Team.WHITE) {
                    whiteScore += value;
                } else {
                    blackScore += value;
                }
            }

//            // bonus: figure is attacking fields around king
            boolean isEndgame = boardManager.figuresList.size() <= 10;
            if (isEndgame) {
                if (figure.getTeam() == Team.BLACK && figure.getFigureId().charAt(0) != 'K') {
                    Figure KW = boardManager.getKing(true);
                    if (isAttackOnKingArea(figure, KW, boardManager)) blackScore += 40;
                } else if (figure.getTeam() == Team.WHITE && figure.getFigureId().charAt(0) != 'K') {
                    Figure KB = boardManager.getKing(false);
                    if (isAttackOnKingArea(figure, KB, boardManager)) whiteScore += 40;
                }
            }


            // bonus: center control by pawn bishop or knight
            char fId = figure.getFigureId().charAt(0);
            if (isClassicCenter(position)) {
                if (figure.getTeam() == Team.WHITE && (fId == 'p' || fId == 'k' || fId == 'b')) whiteScore += 3;
                else if (figure.getTeam() == Team.BLACK && (fId == 'p' || fId == 'k' || fId == 'b')) blackScore += 3;
            } else if (isExtendedCenter(position)) {
                if (figure.getTeam() == Team.WHITE && (fId == 'p' || fId == 'k' || fId == 'b')) whiteScore += 1;
                else if (figure.getTeam() == Team.BLACK && (fId == 'p' || fId == 'k' || fId == 'b')) blackScore += 1;
            }

            // bonus: king safety
            boolean isEndgameWithKing = board.size() <= 4;
            if (figure.getFigureId().charAt(0) == 'K') {
                // keeping king on sides
                if (!isEndgameWithKing) {
                    if (position.x <= 1 || position.x >= 6) {
                        if (figure.getTeam() == Team.WHITE) whiteScore += 20;
                        else blackScore += 20;
                    }
                }
                // attacking with king
                if (isEndgameWithKing && diff < 0 && figure.getTeam() == Team.BLACK) {
                    Figure KW = boardManager.getKing(true);
                    int dist = manhattanDistance(figure.getPosition(), KW.getPosition());
                    blackScore += (5 - dist) * 10;

                } else if (isEndgameWithKing && diff > 0 && figure.getTeam() == Team.WHITE) {
                    Figure KB = boardManager.getKing(false);
                    int dist = manhattanDistance(figure.getPosition(), KB.getPosition());
                    whiteScore += (5 - dist) * 10;
                }
            }

            // bonus: pawn close to advancement
            if (figure instanceof Pawn) {
                int row = (int) position.y;
                if (figure.getTeam() == Team.WHITE && row == 7) whiteScore += 100;
                else if (figure.getTeam() == Team.BLACK && row == 0) blackScore += 100;
            }
        }

        return calcPosDiff(whiteScore, blackScore);
    }

    private int calcPosDiff(int whiteScore, int blackScore) {
        if (gameFlow.getBotTeam().equals(Team.BLACK)) {
            return whiteScore - blackScore;
        } else {
            return blackScore - whiteScore;
        }
    }

    public int manhattanDistance(Vector2 a, Vector2 b) {
        return (int) (Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }

    private boolean isAttackOnKingArea(Figure selected, Figure king, BoardManager boardManager) {
        Vector2 kingPos = king.getPosition();

        // kings neighborhood
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        List<Vector2> surrounding = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (int) kingPos.x + dx[i];
            int y = (int) kingPos.y + dy[i];

            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                surrounding.add(new Vector2(x, y));
            }
        }
        boolean underAttack = false;
        for (Vector2 target : surrounding) {
            if (selected.isMoveLegal(selected.getPosition(), target, selected, boardManager)) {
                underAttack = true;
                break;
            }
        }

        return underAttack;
    }
}
