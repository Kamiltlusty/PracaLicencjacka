package pl.kamil.TetriChess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.screens.GameScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public Assets assets;

    // screens
    public GameScreen gameScreen;



    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();

        // load all assets
        assets.load();
        assets.manager.finishLoading();

        // initialize game screen
        gameScreen = new GameScreen(this);
        setScreen(gameScreen);
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
