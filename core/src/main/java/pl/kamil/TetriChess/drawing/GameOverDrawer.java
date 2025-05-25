package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pl.kamil.TetriChess.resources.Assets;

public class GameOverDrawer {
    private final Assets assets;
    private final SpriteBatch batch;

    // background
    private Texture background;

    // buttons textures
    private Texture play_button;
    private Texture exit_button;

    // buttons rectangles
    private Rectangle pb;

    private Rectangle eb;

    public GameOverDrawer(Assets assets, SpriteBatch batch) {
        this.assets = assets;
        this.batch = batch;

        background = assets.manager.get(Assets.OVER_BACKGROUND_TEXTURE);
//        exit_button = assets.manager.get(Assets.EXIT_BUTTON_TEXTURE);

//        this.eb = createExitButton(exit_button);
    }

    private Rectangle createExitButton(Texture ebTexture) {
        return new Rectangle(760, 300, ebTexture.getWidth(), ebTexture.getHeight());
    }

    public void drawBackground() {
        batch.draw(background,
            0, 0,
            background.getWidth(),
            background.getHeight());
    }

    public void drawExitButton() {
        batch.draw(exit_button,
            eb.x, eb.y,
            eb.getWidth(),
            eb.getHeight());
    }

    public Rectangle getExit_button() {
        return eb;
    }
}
