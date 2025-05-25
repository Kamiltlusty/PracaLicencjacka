package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.drawing.GameOverDrawer;
import pl.kamil.TetriChess.drawing.MenuDrawer;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;

import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class GameOverScreen implements Screen, InputProcessor {
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final GameFlow gameFlow;
    private final GameOverDrawer gameOverDrawer;
    // 0 - white is the winner 1 - black is a winner 2 - draw occurred
    private int whoWon;

    public GameOverScreen(SpriteBatch batch, Assets assets, GameFlow gameFlow, int whoWon) {
        this.batch = batch;
        this.assets = assets;
        this.gameFlow = gameFlow;
        this.gameOverDrawer = new GameOverDrawer(assets, batch);

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );
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

        gameOverDrawer.drawBackground();
//        menuDrawer.drawExitButton();

        // end drawing
        batch.end();
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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
