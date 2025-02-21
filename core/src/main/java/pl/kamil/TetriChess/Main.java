package pl.kamil.TetriChess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.screens.GameScreen;
import pl.kamil.TetriChess.side_panel.ShapesManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public Assets assets;

    // screens
    public GameScreen gameScreen;

    // game
    public GameFlow gameFlow;

    // shapes manager
    public ShapesManager shapesManager;


    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();

        // load all assets
        assets.load();
        assets.manager.finishLoading();

        // initialize game
        gameFlow = new GameFlow(assets);
        // initialize game screen
        gameScreen = new GameScreen(gameFlow, batch, assets);
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
