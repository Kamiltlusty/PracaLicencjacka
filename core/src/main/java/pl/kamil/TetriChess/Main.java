package pl.kamil.TetriChess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.screens.GameOverScreen;
import pl.kamil.TetriChess.screens.GameScreen;
import pl.kamil.TetriChess.screens.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public Assets assets;

    // screens
    public MenuScreen menuScreen;
    public GameOverScreen gameOverScreen;

    // game
    public GameFlow gameFlow;
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assets = new Assets();

        // load all assets
        assets.load();
        assets.manager.finishLoading();

        // initialize game
        gameFlow = new GameFlow(assets, batch, this);
        // initialize game screen
//        gameScreen = new GameScreen(gameFlow, batch, assets);
        menuScreen = new MenuScreen(gameFlow, batch, assets, this);
        setScreen(menuScreen);
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }
}
