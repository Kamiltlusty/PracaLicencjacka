package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.drawing.MenuDrawer;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;

import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class MenuScreen implements Screen, InputProcessor {
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final GameFlow gameFlow;
    private MenuDrawer menuDrawer;
    private boolean touchDownPlay = false;
    private boolean touchDownExit = false;
    private final Main main;
    private GameScreen gameScreen;

    private Rectangle pb;
    private Rectangle eb;

    public MenuScreen(GameFlow gameFlow, SpriteBatch batch, Assets assets, Main main) {
        this.gameFlow = gameFlow;
        this.batch = batch;
        this.assets = assets;
        this.main = main;
        this.menuDrawer = new MenuDrawer(assets, batch);

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        this.pb = menuDrawer.getPlayButton();
        this.eb = menuDrawer.getExitButton();

    }
    @Override
    public void show() {
        // process user input
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BROWN);

//        // set the sprite batch to use the camera
//        batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        batch.begin();

        menuDrawer.drawBackground();
        menuDrawer.drawPlayButton();
        menuDrawer.drawExitButton();

        // end drawing
        batch.end();
    }

    public int transformY(int screenY) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        return (int) (screenHeight - screenY);
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
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        int transformedY = transformY(y);
        if (x >= pb.x && transformedY >= pb.y && x <= pb.x + pb.getWidth() && transformedY <= pb.y + pb.getHeight()) {
            touchDownPlay = true;
        }

        if (x >= eb.x && transformedY >= eb.y && x <= eb.x + eb.getWidth() && transformedY <= eb.y + eb.getHeight()) {
            touchDownExit = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (touchDownPlay) {
            dispose();
            // switch to another screen
            gameScreen = new GameScreen(gameFlow, batch, assets, main);
            main.setScreen(gameScreen);
        } else if (touchDownExit) {
            System.exit(0);
        } else {
            touchDownPlay = false;
            touchDownExit = false;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
