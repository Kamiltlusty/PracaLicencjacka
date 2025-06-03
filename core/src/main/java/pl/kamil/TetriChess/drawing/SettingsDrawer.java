package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pl.kamil.TetriChess.resources.Assets;

public class SettingsDrawer {
    private final Assets assets;
    private final SpriteBatch batch;

    // background
    private Texture background;

    // buttons textures
    private Texture exitButton;

    // buttons rectangles
    private Rectangle eb;


    public SettingsDrawer(Assets assets, SpriteBatch batch) {
        this.assets = assets;
        this.batch = batch;

        background = assets.manager.get(Assets.BACKGROUND_TEXTURE);
        exitButton = assets.manager.get(Assets.EXIT_BUTTON_TEXTURE);

        this.eb = createExitButton(exitButton);
    }

    private Rectangle createExitButton(Texture ebTexture) {
        return new Rectangle(625, 100, ebTexture.getWidth(), ebTexture.getHeight());
    }


    public void drawBackground() {
        batch.draw(background,
            0, 0,
            background.getWidth(),
            background.getHeight());
    }

    public void drawExitButton() {
        batch.draw(exitButton,
            eb.x, eb.y,
            eb.getWidth(),
            eb.getHeight());
    }

    public Rectangle getExitButton() {
        return eb;
    }
}
