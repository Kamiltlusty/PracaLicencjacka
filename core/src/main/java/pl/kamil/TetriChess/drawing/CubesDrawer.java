package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.Square1x1;

import java.util.List;

import static pl.kamil.TetriChess.resources.GlobalVariables.SQUARE_1X1_SIDE;

public class CubesDrawer {
    private final Assets assets;
    private final SpriteBatch batch;
    private final DrawUtils drawUtils;
    private final GameFlow gameFlow;

    // field
    private Texture major_field_texture;
    private Texture minor_field_texture;

    private static final int CUBE_FIELD_NUM = 3;

    public CubesDrawer(Assets assets, SpriteBatch batch, DrawUtils drawUtils, GameFlow gameFlow) {
        this.assets = assets;
        this.batch = batch;
        this.drawUtils = drawUtils;
        this.gameFlow = gameFlow;

        // create cubes
        initCubeFields();
    }

    public void initCubeFields() {
        major_field_texture = assets.manager.get(Assets.MAJOR_FIELD_TEXTURE);
        minor_field_texture = assets.manager.get(Assets.MINOR_FIELD_TEXTURE);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void drawMajorCube() {
        batch.draw(
            major_field_texture,
            1000,
            550,
            major_field_texture.getWidth(),
            major_field_texture.getHeight()
        );
        List<Square1x1> list = gameFlow.getShapesManager().getShapes().getFirst().getSquare3x3().getSquares().values().stream().filter(v -> v.getTexture() != null).toList();
        for (var el : list) {
            batch.draw(
                el.getTexture(),
                1000 + el.getRectangle().x,
                550 + el.getRectangle().y,
                el.getRectangle().getWidth(),
                el.getRectangle().getHeight()
            );
        }
        Shape first = gameFlow.getShapesManager().getShapes().getFirst();
        for (int i = 0; i < CUBE_FIELD_NUM; i++) {
            Texture t1 = drawUtils.getPanelNumTexture(first.getNumber() + i);
            batch.draw(
                t1,
                1000,
                550 + i * SQUARE_1X1_SIDE,
                SQUARE_1X1_SIDE,
                SQUARE_1X1_SIDE
            );
        }
        for (int j = 0; j < CUBE_FIELD_NUM; j++) {
            Texture t1 = drawUtils.getPanelLetterTexture((char)(first.getLetter() + j));
            batch.draw(
                t1,
                1000 + j * SQUARE_1X1_SIDE,
                550,
                SQUARE_1X1_SIDE,
                SQUARE_1X1_SIDE
            );
        }
    }
    public void drawMinor1Cube() {
        batch.draw(
            minor_field_texture,
            940,
            300,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
        List<Square1x1> list = gameFlow.getShapesManager().getShapes().get(1).getSquare3x3().getSquares().values().stream().filter(v -> v.getTexture() != null).toList();
        for (var el : list) {
            batch.draw(
                el.getTexture(),
                940 + el.getRectangle().x * 2 / 3,
                300 + el.getRectangle().y * 2 / 3,
                el.getRectangle().getWidth() * 2 / 3,
                el.getRectangle().getHeight() * 2 / 3
            );
        }
        Shape second = gameFlow.getShapesManager().getShapes().get(1);
        for (int i = 0; i < CUBE_FIELD_NUM; i++) {
            Texture t1 = drawUtils.getPanelNumTexture(second.getNumber() + i);
            batch.draw(
                t1,
                940,
                300 + i * SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
        for (int j = 0; j < CUBE_FIELD_NUM; j++) {
            Texture t1 = drawUtils.getPanelLetterTexture((char)(second.getLetter() + j));
            batch.draw(
                t1,
                940 + j * SQUARE_1X1_SIDE * 2 / 3,
                300,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
    }

    public void drawMinor2Cube() {
        batch.draw(
            minor_field_texture,
            1200,
            300,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
        List<Square1x1> list = gameFlow.getShapesManager().getShapes().get(2).getSquare3x3().getSquares().values().stream().filter(v -> v.getTexture() != null).toList();
        for (var el : list) {
            batch.draw(
                el.getTexture(),
                1200 + el.getRectangle().x * 2 / 3,
                300 + el.getRectangle().y * 2 / 3,
                el.getRectangle().getWidth() * 2 / 3,
                el.getRectangle().getHeight() * 2 / 3
            );
        }
        Shape third = gameFlow.getShapesManager().getShapes().get(2);
        for (int i = 0; i < CUBE_FIELD_NUM; i++) {
            Texture t1 = drawUtils.getPanelNumTexture(third.getNumber() + i);
            batch.draw(
                t1,
                1200,
                300 + i * SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
        for (int j = 0; j < CUBE_FIELD_NUM; j++) {
            Texture t1 = drawUtils.getPanelLetterTexture((char)(third.getLetter() + j));
            batch.draw(
                t1,
                1200 + j * SQUARE_1X1_SIDE * 2 / 3,
                300,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
    }

    public void drawMinor3Cube() {
        batch.draw(
            minor_field_texture,
            1075,
            50,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
        List<Square1x1> list = gameFlow.getShapesManager().getShapes().get(3).getSquare3x3().getSquares().values().stream().filter(v -> v.getTexture() != null).toList();
        for (var el : list) {
            batch.draw(
                el.getTexture(),
                1075 + el.getRectangle().x * 2 / 3,
                50 + el.getRectangle().y * 2 / 3,
                el.getRectangle().getWidth() * 2 / 3,
                el.getRectangle().getHeight() * 2 / 3
            );
        }
        Shape fourth = gameFlow.getShapesManager().getShapes().get(3);
        for (int i = 0; i < CUBE_FIELD_NUM; i++) {
            Texture t1 = drawUtils.getPanelNumTexture(fourth.getNumber() + i);
            batch.draw(
                t1,
                1075,
                50 + i * SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
        for (int j = 0; j < CUBE_FIELD_NUM; j++) {
            Texture t1 = drawUtils.getPanelLetterTexture((char)(fourth.getLetter() + j));
            batch.draw(
                t1,
                1075 + j * SQUARE_1X1_SIDE * 2 / 3,
                50,
                SQUARE_1X1_SIDE * 2 / 3,
                SQUARE_1X1_SIDE * 2 / 3
            );
        }
    }
}
