package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.drawing.DrawManager;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class GameScreen implements Screen, InputProcessor {
    private final GameFlow gameFlow;
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final DrawManager drawManager;
    private final Main main;
    private Team botTeam;
    private boolean exitGame = false;
    private BitmapFont font;
    private FrameBuffer frameBuffer;
    private Texture frozenBoardTexture;
    private boolean hasFrozenFrame = false;

    // panel background
    private Texture panelBcg;

    // boards
    private BoardManager boardManager;

    public GameScreen(GameFlow gameFlow, SpriteBatch batch, Assets assets, Main main, Team botTeam) {
        this.gameFlow = gameFlow;
        this.batch = batch;
        this.assets = assets;
        this.main = main;
        this.botTeam = botTeam;
        font = new BitmapFont();
        font.getData().setScale(2f);

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        panelBcg = assets.manager.get(Assets.PANEL_TEXTURE);
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
        if (!exitGame) {
            try {
                gameFlow.updateBotFirstMoveIfNeeded();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            freezeFrame();
        }
        ScreenUtils.clear(Color.BLACK);

//        // set the sprite batch to use the camera
//        batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        batch.begin();

        if (exitGame) {
            batch.draw(frozenBoardTexture,
                0, Gdx.graphics.getHeight(),
                Gdx.graphics.getWidth(), -Gdx.graphics.getHeight()
            );
            drawWinnerMessage();
        } else {

            batch.draw(panelBcg,
                8 * GlobalVariables.BOARD_FIELD_SIDE_LENGTH, 0,
                panelBcg.getWidth(),
                panelBcg.getHeight()
            );

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
        }
        // end drawing
        batch.end();

        if (exitGame) {
            handleExitInput();
        }
    }

    private void drawWinnerMessage() {
        font.getData().setScale(3);

        String message = "\nClick ESC button, to end the game.";
        GlyphLayout layout = new GlyphLayout(font, message);

        float x = (Gdx.graphics.getWidth() - layout.width) / 2;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2;

        font.setColor(Color.BLACK);
        font.draw(batch, layout, x + 2, y - 2);

        font.setColor(Color.WHITE);
        font.draw(batch, layout, x, y);
    }

    private void freezeFrame() {
        if (hasFrozenFrame) return;

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        frameBuffer.begin();

        ScreenUtils.clear(Color.BLACK);

        batch.begin();

        batch.draw(panelBcg,
            8 * GlobalVariables.BOARD_FIELD_SIDE_LENGTH, 0,
            panelBcg.getWidth(),
            panelBcg.getHeight()
        );
        drawManager.getChessboardDrawer().drawChessBoard();
        drawManager.getCubesDrawer().drawMajorCube();
        drawCubes();
        drawNumbers();
        drawManager.getFigureDrawer().drawFigures();
        drawManager.getActiveShapeDrawer().drawActiveShape();

        batch.end();

        frameBuffer.end();

        frozenBoardTexture = frameBuffer.getColorBufferTexture();

        hasFrozenFrame = true;
    }

    private void handleExitInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
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
        gameFlow.onTouchDown(screenX, transformedY);
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float transformedY = transformY(screenY, true);
        gameFlow.onTouchDragged(screenX, transformedY);
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int transformedY = transformY(screenY);
        try {
            gameFlow.onTouchUp(screenX, transformedY);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
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

    public boolean isExitGame() {
        return exitGame;
    }

    public void setExitGame(boolean exitGame) {
        this.exitGame = exitGame;
    }
}
