package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.objects.Board;
import pl.kamil.TetriChess.objects.Field;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

import static pl.kamil.TetriChess.resources.Assets.MAJOR_FIELD_TEXTURE;
import static pl.kamil.TetriChess.resources.GlobalVariables.*;

public class GameScreen implements Screen {
    private final Main game;
    private final ExtendViewport viewport;

    // field
    private Texture major_field_texture;
    private Texture minor_field_texture;

    // numbers
    private Texture one_texture;
    private Texture two_texture;
    private Texture three_texture;
    private Texture four_texture;

    // boards
    private Board board;
    private static final int CUBE_FIELD_NUM = 3;

    public GameScreen(Main game) {
        this.game = game;

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        // create board
        board = new Board(game.assets);

        // create cubes
        initializeCubeFields();
        // create numbers
        initializeNumbers();
    }

    public void initializeCubeFields() {
        major_field_texture = game.assets.manager.get(Assets.MAJOR_FIELD_TEXTURE);
        minor_field_texture = game.assets.manager.get(Assets.MINOR_FIELD_TEXTURE);
    }

    public void initializeNumbers() {
        one_texture = game.assets.manager.get(Assets.ONE_TEXTURE);
        two_texture = game.assets.manager.get(Assets.TWO_TEXTURE);
        three_texture = game.assets.manager.get(Assets.THREE_TEXTURE);
        four_texture = game.assets.manager.get(Assets.FOUR_TEXTURE);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);


        // begin drawing
        game.batch.begin();

        // draw the chessboard
        board.setFieldsMap();
        drawChessBoard();
        // draw 3x3 cube side
        drawMajorCube();
        // draw scaled cubes
        drawCubes();
        // draw numbers
        drawNumbers();

        // end drawing
        game.batch.end();
    }

    private void drawNumbers() {
        drawNumber1();
        drawNumber2();
        drawNumber3();
        drawNumber4();
    }

    private void drawCubes() {
        drawMinor1Cube();
        drawMinor2Cube();
        drawMinor3Cube();
    }

    private void drawMajorCube() {
        game.batch.draw(
            major_field_texture,
            1000,
            550,
            major_field_texture.getWidth(),
            major_field_texture.getHeight()
        );
    }

    private void drawMinor1Cube() {
        game.batch.draw(
            minor_field_texture,
            940,
            300,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
    }

    private void drawMinor2Cube() {
        game.batch.draw(
            minor_field_texture,
            1200,
            300,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
    }

    private void drawMinor3Cube() {
        game.batch.draw(
            minor_field_texture,
            1075,
            50,
            minor_field_texture.getWidth(),
            minor_field_texture.getHeight()
        );
    }

    private void drawNumber1() {
        game.batch.draw(
            one_texture,
            960,
            840,
            one_texture.getWidth() * 0.3f,
            one_texture.getHeight() * 0.3f
        );
    }

    private void drawNumber2() {
        game.batch.draw(
            two_texture,
            900,
            476,
            two_texture.getWidth() * 0.3f,
            two_texture.getHeight() * 0.3f
        );
    }

    private void drawNumber3() {
        game.batch.draw(
            three_texture,
            1146,
            460,
            three_texture.getWidth() * 0.55f,
            three_texture.getHeight() * 0.55f
        );
    }

    private void drawNumber4() {
        game.batch.draw(
            four_texture,
            1020,
            210,
            four_texture.getWidth() * 0.55f,
            four_texture.getHeight() * 0.55f
        );
    }

    private void drawChessBoard() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                char letter = (char) ('A' + j);
                String position = letter + "" + i + 1;
                Field field = board.getFieldsMap().get(position);
                drawField(field.getPosition().x * field.getPosition().getWidth(), field.getPosition().y * field.getFieldTexture().getWidth(), field);
            }
        }

    }

    private void drawField(float i, float j, Field field) {
        game.batch.draw(
            field.getFieldTexture(),
            i,
            j,
            field.getPosition().getWidth(),
            field.getPosition().getHeight()
        );
    }


    @Override
    public void resize(int width, int height) {

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
}
