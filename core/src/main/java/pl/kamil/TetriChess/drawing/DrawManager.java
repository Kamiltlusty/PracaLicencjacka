package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;

public class DrawManager {
    private final Assets assets;
    private final SpriteBatch batch;
    private final BoardManager board;
    private final GameFlow gameFlow;
    private DrawUtils drawUtils;
    private FigureDrawer figureDrawer;
    private ChessboardDrawer chessboardDrawer;
    private CubesDrawer cubesDrawer;
    private NumbersDrawer numbersDrawer;
    private ActiveShapeDrawer activeShapeDrawer;

    public DrawManager(Assets assets, BoardManager board, SpriteBatch batch, GameFlow gameFlow) {
        this.assets = assets;
        this.board = board;
        this.batch = batch;
        this.gameFlow = gameFlow;

        // initialize draw utils
        initializeDrawUtils();
        // initialize Drawers
        initializeDrawers();
    }

    private void initializeDrawUtils() {
        this.drawUtils = new DrawUtils(assets);
    }

    private void initializeDrawers() {
        this.figureDrawer = new FigureDrawer(board, batch);
        this.chessboardDrawer = new ChessboardDrawer(batch, board, drawUtils);
        this.cubesDrawer = new CubesDrawer(assets, batch, drawUtils, gameFlow);
        this.numbersDrawer = new NumbersDrawer(batch, assets);
        this.activeShapeDrawer = new ActiveShapeDrawer(gameFlow, board, batch, drawUtils);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BoardManager getBoard() {
        return board;
    }

    public FigureDrawer getFigureDrawer() {
        return figureDrawer;
    }

    public ChessboardDrawer getChessboardDrawer() {
        return chessboardDrawer;
    }

    public DrawUtils getDrawUtils() {
        return drawUtils;
    }

    public CubesDrawer getCubesDrawer() {
        return cubesDrawer;
    }

    public NumbersDrawer getNumbersDrawer() {
        return numbersDrawer;
    }

    public ActiveShapeDrawer getActiveShapeDrawer() {
        return activeShapeDrawer;
    }
}
