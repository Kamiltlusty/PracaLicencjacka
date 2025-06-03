package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pl.kamil.TetriChess.resources.Assets;

public class MenuDrawer {
    private final Assets assets;
    private final SpriteBatch batch;

    // background
    private Texture background;

    // buttons textures
    private Texture playButton;
    private Texture exitButton;
    private Texture settingsButton;

    // buttons rectangles
    private Rectangle pb;
    private Rectangle eb;
    private Rectangle sb;


    public MenuDrawer(Assets assets, SpriteBatch batch) {
        this.assets = assets;
        this.batch = batch;

        background = assets.manager.get(Assets.BACKGROUND_TEXTURE);
        playButton = assets.manager.get(Assets.PLAY_BUTTON_TEXTURE);
        exitButton = assets.manager.get(Assets.EXIT_BUTTON_TEXTURE);
        settingsButton = assets.manager.get(Assets.EXIT_BUTTON_TEXTURE);

        this.pb = createPlayButton(playButton);
        this.eb = createExitButton(exitButton);
        this.sb = createSettingsButton(settingsButton);
    }

    private Rectangle createPlayButton(Texture pbTexture) {
        return new Rectangle(420, 300, pbTexture.getWidth(), pbTexture.getHeight());
    }

    private Rectangle createSettingsButton(Texture ebTexture) {
        return new Rectangle(640, 300, ebTexture.getWidth(), ebTexture.getHeight());
    }

    private Rectangle createExitButton(Texture ebTexture) {
        return new Rectangle(860, 300, ebTexture.getWidth(), ebTexture.getHeight());
    }


    public void drawBackground() {
        batch.draw(background,
            0, 0,
            background.getWidth(),
            background.getHeight());
    }

    public void drawPlayButton() {
        batch.draw(playButton,
            pb.x, pb.y,
            pb.getWidth(),
            pb.getHeight());
    }

    public void drawExitButton() {
        batch.draw(exitButton,
            eb.x, eb.y,
            eb.getWidth(),
            eb.getHeight());
    }

    public void drawSettingsButton() {
        batch.draw(exitButton,
            sb.x, sb.y,
            sb.getWidth(),
            sb.getHeight());
    }

    public Rectangle getPlayButton() {
        return pb;
    }

    public Rectangle getExitButton() {
        return eb;
    }

    public Rectangle getSettingsButton() {
        return sb;
    }
}
