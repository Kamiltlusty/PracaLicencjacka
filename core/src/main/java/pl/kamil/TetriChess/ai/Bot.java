package pl.kamil.TetriChess.ai;

import com.badlogic.gdx.math.Vector2;
import io.vavr.Tuple2;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.board_elements.figures.Figure;
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
        } else throw new RuntimeException("No available moves found");
    }

    public Tuple2<String, Vector2> findBestMove(StateBeforeMoveRecord beforeMoveRecord, int depth) {
        int minEval = Integer.MAX_VALUE;
        Tuple2<String, Vector2> bestMove = null;

        Map<Figure, List<Vector2>> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
        for (var selectedFig : legalMovesForMinimax.keySet()) {
            List<Vector2> possibleMoves = legalMovesForMinimax.get(selectedFig);
            for (var move : possibleMoves) {
                prepareForMoveExecution(selectedFig, move);
                gameFlow.executeMove();
                gameFlow.prepare(false);
//                    resetFields(); is needed?
                // set new state as actual
                beforeMoveRecord = setNewStateBeforeMoveRecord();
                gameFlow.checkCheckmate(beforeMoveRecord);
                // reset fields
                resetFields();
                // count result of evaluation
                int eval = minimax(beforeMoveRecord, depth - 1, true);

                if (eval < minEval) {
                    minEval = eval;
                    bestMove = new Tuple2<>(selectedFig.getFigureId(), move);
                }
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
                gameFlow.setActive();
            }
        }

        return bestMove;
    }

    public int minimax(StateBeforeMoveRecord beforeMoveRecord, int depth, boolean isMaximizingPlayer) {
        // decide whether to go back in stack or go deeper
        if (depth == 0 || gameFlow.isCheckmate()) {
            return evaluatePosition(beforeMoveRecord);
        }
        // chose are we maximizing the evaluation score
        if (isMaximizingPlayer) {
            // take smallest value because we are searching for the biggest for maximizer
            int maxEval = Integer.MIN_VALUE;
            Map<Figure, List<Vector2>> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
            for (var selectedFig : legalMovesForMinimax.keySet()) {
                List<Vector2> possibleMoves = legalMovesForMinimax.get(selectedFig);
                for (var move : possibleMoves) {
                    prepareForMoveExecution(selectedFig, move);
                    gameFlow.executeMove();
                    gameFlow.prepare(false);
//                    resetFields(); is needed?
                    // set new state as actual
                    beforeMoveRecord = setNewStateBeforeMoveRecord();
                    gameFlow.checkCheckmate(beforeMoveRecord);
                    // reset fields
                    resetFields();
                    // count result of evaluation
                    int eval = minimax(beforeMoveRecord, depth - 1, false);
                    maxEval = Math.max(maxEval, eval);
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
                    gameFlow.setActive();
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            Map<Figure, List<Vector2>> legalMovesForMinimax = getLegalMovesForMinimax(beforeMoveRecord);
            for (var selectedFig : legalMovesForMinimax.keySet()) {
                List<Vector2> possibleMoves = legalMovesForMinimax.get(selectedFig);
                for (var move : possibleMoves) {
                    prepareForMoveExecution(selectedFig, move);
                    gameFlow.executeMove();
                    gameFlow.prepare(false);
//                    resetFields(); is needed?
                    // set new state as actual
                    beforeMoveRecord = setNewStateBeforeMoveRecord();
                    gameFlow.checkCheckmate(beforeMoveRecord);
                    // reset fields
                    resetFields();
                    // count result of evaluation
                    int eval = minimax(beforeMoveRecord, depth - 1, true);
                    minEval = Math.min(minEval, eval);
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
                    gameFlow.setActive();
                }
            }
            return minEval;
        }
    }

    private StateBeforeMoveRecord setNewStateBeforeMoveRecord() {
        // add it as a new state to state before move record
        StateBeforeMoveRecord newBeforeMoveRecord = new StateBeforeMoveRecord(
            gameFlow.getActive(),
            gameFlow.isWhiteInCheck(),
            gameFlow.isBlackInCheck(),
            gameFlow.isCheckmate(),
            boardManager,
            boardManager.getBoardUtils(),
            gameFlow.getActiveShape()
        );
        stateBeforeMoveRecordDeque.addFirst(newBeforeMoveRecord);
        return stateBeforeMoveRecordDeque.getFirst();
    }

    private Map<Figure, List<Vector2>> getLegalMovesForMinimax(StateBeforeMoveRecord beforeMoveRecord) {
        // create list for legal moves
        Map<Figure, List<Vector2>> validMoves = new HashMap<>();
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
        return validMoves;
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
            if (selectedFig.getPosition().x == 6.0 && selectedFig.getPosition().y == 0.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7.0f, 0.0f);
            } else if (selectedFig.getPosition().x == 2.0 && selectedFig.getPosition().y == 0.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3.0f, 0);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0.0f, 0.0f);
            } else if (selectedFig.getPosition().x == 6.0 && selectedFig.getPosition().y == 7.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5.0f, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7.0f, 7.0f);
            } else if (selectedFig.getPosition().x == 2.0 && selectedFig.getPosition().y == 7.0) {
                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3, 7);
                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0, 7.0f);
            }
        }
//        if (boardManager.isCastling()) {
//            if (boardManager.getFinalFieldPosition().x == 6.0 && boardManager.getFinalFieldPosition().y == 0.0) {
//                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5, 0);
//                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7.0f, 0.0f);
//            } else if (boardManager.getFinalFieldPosition().x == 2.0 && boardManager.getFinalFieldPosition().y == 0.0) {
//                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3.0f, 0);
//                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0.0f, 0.0f);
//            } else if (boardManager.getFinalFieldPosition().x == 6.0 && boardManager.getFinalFieldPosition().y == 7.0) {
//                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(5.0f, 7);
//                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(7.0f, 7.0f);
//            } else if (boardManager.getFinalFieldPosition().x == 2.0 && boardManager.getFinalFieldPosition().y == 7.0) {
//                Optional<Figure> rook = boardManager.findFigureByCoordinatesAndReturn(3, 7);
//                if (rook.isPresent() && rook.get() instanceof Rook) rook.get().setPosition(0, 7.0f);
//            }
//        }

        // if was promotion then in figures list we would have queen with id like QW1 or QB2 or QW2 ...
        // start checking if there are promotedPawns and selectedFig is pawn
        if (!boardManager.getPromotedPawns().isEmpty() && selectedFig.getFigureId().charAt(0) == 'p') {
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
            promotedToTrash.ifPresentOrElse(
                f -> boardManager.getFiguresList().remove(f),
                () -> {
                    throw new RuntimeException("Should have found queen promoted from pawn");
                }
            );
            boardManager.getPromotedPawns().remove(selectedFig);
            boardManager.getFiguresList().add(selectedFig);
        }

        // delete promoted figure and get back pawn if was promotion
//        if (boardManager.isPromotion()) {
//            // delete promoted figure from figures list
//            boardManager.getFiguresList().stream()
//                .filter(f -> f.getFigureId().equals(selectedFig.getFigureId()))
//                .findFirst().ifPresentOrElse(
//                    f -> boardManager.getFiguresList().remove(f),
//                    () -> {
//                        throw new RuntimeException("Should have found promoted queen");
//                    }
//                );
//            // find pawn that was promoted and removed, before move record was hardly set so i can take value from there
//            if (selectedFig.getTeam().equals(Team.WHITE)) {
//                beforeMoveRecord.getBoardState().keySet().stream()
//                    .filter(f -> f.getTeam().equals(Team.WHITE))
//                    .filter(f -> f.getPosition().x == selectedFig.getPosition().x && f.getPosition().y == 7)
//                    .findFirst().ifPresentOrElse(
//                        f -> {
//                            boardManager.promotedPawns.remove(f);
//                            boardManager.figuresList.add(f);
//                            f.setMoveCounter(f.getMoveCounter() - 1);
//                            boardManager.setSelectedFigure(f);
//                        },
//                        () -> {
//                            throw new RuntimeException("Should have found promoted pawn");
//                        }
//                    );
//            } else {
//                beforeMoveRecord.getBoardState().keySet().stream()
//                    .filter(f -> f.getTeam().equals(Team.BLACK))
//                    .filter(f -> f.getPosition().x == selectedFig.getPosition().x && f.getPosition().y == 1)
//                    .findFirst().ifPresentOrElse(
//                        f -> {
//                            boardManager.promotedPawns.remove(f);
//                            boardManager.figuresList.add(f);
//                            f.setMoveCounter(f.getMoveCounter() - 1);
//                            boardManager.setSelectedFigure(f);
//                        },
//                        () -> {
//                            throw new RuntimeException("Should have found promoted pawn");
//                        }
//                    );
//            }
//        }
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

    // this method checks position before bot analysis to calculate the beginning position evaluation
    public void evaluateRootPosition() {
        StateBeforeMoveRecord actualPosition = stateBeforeMoveRecordDeque.getFirst();
        evaluationScoreRoot = evaluatePosition(actualPosition);
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
        if (selectedFig.getFigureId().charAt(0) == 'K') {
            boolean specificMoveLegal = selectedFig.isSpecificMoveLegal(selectedFig.getPosition(), move, selectedFig, null, boardManager);
            if (specificMoveLegal) {
                boardManager.setCastling(true);
            }
        }
        // if is promotion -> set promotion
        if (boardManager.isPawnPromotion()) boardManager.setPromotion(true);
    }

    public int evaluatePosition(StateBeforeMoveRecord beforeMoveRecord) {
        int whiteScore;
        int blackScore;

        whiteScore = beforeMoveRecord.getBoardState().keySet().stream()
            .filter(f -> f.getTeam().equals(Team.WHITE))
            .filter(f -> f.getFigureId().charAt(0) != 'K')
            .map(Figure::getValue)
            .reduce(0, (a, b) -> a + b);

        blackScore = beforeMoveRecord.getBoardState().keySet().stream()
            .filter(f -> f.getTeam().equals(Team.BLACK))
            .filter(f -> f.getFigureId().charAt(0) != 'K')
            .map(Figure::getValue)
            .reduce(0, (a, b) -> a + b);

        return whiteScore - blackScore;
    }

//    public int findBestMove() {
//        StateBeforeMoveRecord beforeMoveRecord = stateBeforeMoveRecordDeque.getFirst();
//        getLegalMoves(beforeMoveRecord);
//        // as checkingCheckmate can change some fields I have to reset them again
//        resetFields();
//
//        // create map saving evaluation score at actual depth
//        Map<Integer, List<Tuple2<String, Vector2>>> evaluationsMap = new HashMap<>();
//        // loop through all valid moves and try them
//        // execute move from list of validated moves
//        for (var selectedFig : validMoves.keySet()) {
//            List<Vector2> possibleMoves = validMoves.get(selectedFig);
//            for (var move : possibleMoves) {
//                prepareForMoveExecution(selectedFig, move);
//
//                gameFlow.executeMove();
//                // count result of evaluation
//                int evaluationScore = evaluatePosition(beforeMoveRecord);
//
//                // save result to evaluation map
//                evaluationsMap.compute(evaluationScore, (key, list) -> {
//                    if (list == null) {
//                        list = new ArrayList<>();
//                    }
//                    list.add(new Tuple2<>(selectedFig.getFigureId(), move));
//                    return list;
//                });
//                // regain starting position from state before move record:
//                // get back captured figure if was captured
//                undoLastMove(selectedFig, beforeMoveRecord);
//            }
//        }
//        // add all evaluation score map to deque
//        evaluationsDeque.offerFirst(evaluationsMap);
//        // choose first best for bot
//        Map<Integer, List<Tuple2<String, Vector2>>> depthOne = evaluationsDeque.getFirst();
//        Set<Integer> evaluationScores = depthOne.keySet();
//        // find minimum evaluation score list if is Black and maximum if is white turn
//        Optional<Integer> minEvalScore = evaluationScores.stream().min((v1, v2) -> v1 < v2 ? v1 : v2);
//        Optional<Integer> maxEvalScore = evaluationScores.stream().max((v1, v2) -> v1 > v2 ? v1 : v2);
//        Tuple2<String, Vector2> bestEvalScoreTuple;
//        // take first value from list with moves with best evaluation score
//        int evaluationScore;
//        if (gameFlow.getActive().equals(Team.BLACK) && minEvalScore.isPresent()) {
//            List<Tuple2<String, Vector2>> bestEvalScoreList = depthOne.get(minEvalScore.get());
//            bestEvalScoreTuple = bestEvalScoreList.getFirst();
//            evaluationScore = minEvalScore.get();
//        } else if (gameFlow.getActive().equals(Team.WHITE) && maxEvalScore.isPresent()) {
//            List<Tuple2<String, Vector2>> bestEvalScoreList = depthOne.get(maxEvalScore.get());
//            bestEvalScoreTuple = bestEvalScoreList.getFirst();
//            evaluationScore = maxEvalScore.get();
//        } else {
//            throw new RuntimeException("Something went wrong");
//        }
//        if (bestEvalScoreTuple != null) { // this check will be deleted when i add checking if is checkmate (probably)
//            // get figure and position from found Tuple2
//            Figure selectedFig = boardManager.getFiguresList().stream()
//                .filter(f -> bestEvalScoreTuple._1.equals(f.getFigureId()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Should have found figure"));
//            Vector2 move = bestEvalScoreTuple._2;
//            // execute move as bot
//            prepareForMoveExecution(selectedFig, move);
//
//            gameFlow.executeMove();
//            gameFlow.prepare(false);
//        }
//        // add it as a new state to state before move record
//        setNewStateBeforeMoveRecord();
//        gameFlow.checkCheckmate(stateBeforeMoveRecordDeque.getFirst());
//        // reset fields
//        resetFields();
//        return evaluationScore;
//    }
}
