package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.resources.Assets;

public class NumbersDrawer {
    private final SpriteBatch batch;
    private final Assets assets;

    // panel numbers
    private Texture one_texture;
    private Texture two_texture;
    private Texture three_texture;
    private Texture four_texture;

    public NumbersDrawer(SpriteBatch batch, Assets assets) {
        this.batch = batch;
        this.assets = assets;

        // create numbers
        initPanelNumbers();
    }

    public void initPanelNumbers() {
        one_texture = assets.manager.get(Assets.ONE_TEXTURE);
        two_texture = assets.manager.get(Assets.TWO_TEXTURE);
        three_texture = assets.manager.get(Assets.THREE_TEXTURE);
        four_texture = assets.manager.get(Assets.FOUR_TEXTURE);
    }


    public void drawNumber1() {
        batch.draw(
            one_texture,
            920,
            800,
            one_texture.getWidth(),
            one_texture.getHeight()
        );
    }

    public void drawNumber2() {
        batch.draw(
            two_texture,
            870,
            445,
            two_texture.getWidth(),
            two_texture.getHeight()
        );
    }

    public void drawNumber3() {
        batch.draw(
            three_texture,
            1128,
            445,
            three_texture.getWidth(),
            three_texture.getHeight()
        );
    }

    public void drawNumber4() {
        batch.draw(
            four_texture,
            1000,
            190,
            four_texture.getWidth(),
            four_texture.getHeight()
        );
    }
}
