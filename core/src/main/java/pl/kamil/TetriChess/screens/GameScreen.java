package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.drawing.DrawManager;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;

import java.util.Optional;

import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class GameScreen implements Screen, InputProcessor {
    private final GameFlow gameFlow;
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final DrawManager drawManager;

    // boards
    private BoardManager boardManager;

    public GameScreen(GameFlow gameFlow, SpriteBatch batch, Assets assets) {
        this.gameFlow = gameFlow;
        this.batch = batch;
        this.assets = assets;

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        // create board
        boardManager = gameFlow.getBoardManager();
        this.drawManager = new DrawManager(assets, boardManager, batch, gameFlow);
    }

    @Override
    public void show() {
        // process user input
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

//        // set the sprite batch to use the camera
//        batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        batch.begin();

        drawManager.getChessboardDrawer().drawChessBoard();
        // draw 3x3 cube side
        drawManager.getCubesDrawer().drawMajorCube();
        // draw scaled cubes
        drawCubes();
        // draw numbers
        drawNumbers();
        // draw figures
        drawManager.getFigureDrawer().drawFigures();
        drawManager.getActiveShapeDrawer().drawActiveShape();

        // end drawing
        batch.end();
    }

    private void drawNumbers() {
        drawManager.getNumbersDrawer().drawNumber1();
        drawManager.getNumbersDrawer().drawNumber2();
        drawManager.getNumbersDrawer().drawNumber3();
        drawManager.getNumbersDrawer().drawNumber4();
    }

    private void drawCubes() {
        drawManager.getCubesDrawer().drawMinor1Cube();
        drawManager.getCubesDrawer().drawMinor2Cube();
        drawManager.getCubesDrawer().drawMinor3Cube();
    }

    @Override
    public void resize(int width, int height) {
        // update the viewport with the new screen size
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public int transformY(int screenY) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        return (int) (screenHeight - screenY);
    }

    public float transformY(int screenY, boolean isFloat) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        return screenHeight - screenY;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int transformedY = transformY(screenY);

        Vector2 fieldCoordinates = boardManager.detectFigureOnClick(screenX, transformedY);
        // saves all pointer position for usage in touchDragged and touchUp
        boardManager.savePointerPositionInBoardManager(screenX, transformedY, fieldCoordinates);
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float transformedY = transformY(screenY, true);

        boardManager.moveFigureOverBoard(screenX, transformedY);
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int transformedY = transformY(screenY);

        // if everything is ok puts figure on place else return false
        Optional<Figure> figure = boardManager.isFigurePlaceable(screenX, transformedY);

        if (figure.isEmpty()) {
            boardManager.UndoFigurePlacement();
            boardManager.setCapture(false);
            boardManager.setCapturedFigureId(null);
            boardManager.setSelectedFigureAsEmpty();
        }

        else {
            if (boardManager.isCapture()) {
                removeCapturedFigure();
            }
            figure.get().setMoveCounter(figure.get().getMoveCounter() + 1);
//            gameFlow.isCheck();
            // prepare for next move
            gameFlow.prepare();
        }
        return false;
    }

    private void removeCapturedFigure() {
        Optional<Figure> capturedFigure = boardManager.getFiguresList().stream()
            .filter(f -> f.getFigureId().equals(boardManager.getCapturedFigureId()))
            .filter(f -> !f.getTeam().equals(gameFlow.getActive()))
            .findFirst();
        if (capturedFigure.isPresent()) {
            boardManager.getFiguresList().remove(capturedFigure.get());
        } else throw new RuntimeException("Figure that should be in figuresList was not found");
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
