package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.objects.Board;
import pl.kamil.TetriChess.objects.Field;
import pl.kamil.TetriChess.objects.Figure;
import pl.kamil.TetriChess.resources.Assets;

import java.util.Objects;
import java.util.Optional;

import static pl.kamil.TetriChess.resources.GlobalVariables.*;

public class GameScreen implements Screen, InputProcessor {
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
        // process user input
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

//        // set the sprite batch to use the camera
//        game.batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        game.batch.begin();

        drawChessBoard();
        // draw 3x3 cube side
        drawMajorCube();
        // draw scaled cubes
        drawCubes();
        // draw numbers
        drawNumbers();
        drawFigures();

        // end drawing
        game.batch.end();
    }

    private void drawFigures() {
        for (int k = 0; k < board.figuresList.size(); k++) {
            Figure figure;
            Field field = board.getFieldsMap().get("A1");
            if (board.figuresList.get(k) != null) figure = board.figuresList.get(k);
            else continue;
            drawFigure(field, figure);
        }
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
                String fieldSignature = findFieldSignature(i, j);
                Field field = board.getFieldsMap().get(fieldSignature);
                drawField(field);
            }
        }
    }

    /**
     * i oraz j nie sa w tej funkjci powiazane ze wspolrzednymi,
     * dlatego sa intami i nie sa ustawione w kolejnosci float j, float i
     *
     * @param i
     * @param j
     * @return
     */
    private String findFieldSignature(int i, int j) {
        char letter = (char) ('A' + j);
        return new StringBuilder().append(letter).append(i + 1).toString();
    }


    private void drawField(Field field) {
        game.batch.draw(
            field.getFieldTexture(),
            field.getPosition().x * field.getFieldTexture().getWidth(),
            field.getPosition().y * field.getFieldTexture().getHeight(),
            field.getFieldTexture().getWidth(),
            field.getFieldTexture().getHeight()
        );
    }

    private void drawFigure(Field field, Figure figure) {
        game.batch.draw(
            figure.getFigureTexture(),
            figure.getPosition().x * field.getFieldTexture().getWidth(),
            figure.getPosition().y * field.getFieldTexture().getHeight(),
            figure.getFigureTexture().getWidth() * 0.95f,
            figure.getFigureTexture().getHeight() * 0.95f
        );
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
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        int transformedY = (int) (screenHeight - screenY);

        // mark/search for marked field
        String foundSignature = board.findFieldSignatureByScreenCoordinates(screenX, transformedY);
        Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
        // finding pawn
        board.findFigureByCoordinates(fieldCoordinates.x, fieldCoordinates.y);
        // remembering pointer position
        board.setPointerPosition(screenX, transformedY);
        board.setInitialPointerPosition(board.getPointerPosition().x, board.getPointerPosition().y);
        board.setInitialFieldPosition(fieldCoordinates.x, fieldCoordinates.y);
        System.out.println("initial before" + board.getInitialPointerPosition().x + ", " + board.getInitialPointerPosition().y);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        float transformedY = screenHeight - screenY;

        Optional<Figure> selectedFigure = board.getSelectedFigure();
        // counting difference between two pointer positions

        Vector2 newTouch = new Vector2(screenX, transformedY);
        System.out.println("aktualny: " + board.getPointerPosition().x + ", " + board.getPointerPosition().y);
        System.out.println("nowy: " + newTouch.x + ", " + newTouch.y);

        Vector2 delta = newTouch.cpy().sub(board.getPointerPosition());
        System.out.println("delta" + delta.x + ", " + delta.y);
        Texture fieldTexture = board.getFieldsMap().get("A1").getFieldTexture();

        selectedFigure.ifPresent(figure -> figure.setPosition(
            figure.getPosition().x + delta.x / fieldTexture.getWidth(),
            figure.getPosition().y + delta.y / fieldTexture.getHeight()));
        board.setPointerPosition(newTouch.x, newTouch.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Reverse of Y for screen counting from left top corner changed to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        int transformedY = (int) (screenHeight - screenY);


        boolean allValid = true;
        // search for field to drop figure
        String foundSignature = board.findFieldSignatureByScreenCoordinates(screenX, transformedY);
        // check if field was found
        if (!Objects.equals(foundSignature, "-1")) {
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            board.setFinalFieldPosition(fieldCoordinates.x, fieldCoordinates.y);

            Optional<Figure> selectedFigure = board.getSelectedFigure();
//            Field field = board.getFieldsMap().get("A1");
            if (selectedFigure.isPresent() &&
                selectedFigure.get().isMoveLegal(
                    board.getInitialFieldPosition(),
                    board.getFinalFieldPosition(),
                    selectedFigure.get(),
                    board
                    )
            ) {
                selectedFigure.get().setPosition(
                    fieldCoordinates.x,
                    fieldCoordinates.y
                );
            } else allValid = false;
        } else allValid = false;

        if (!allValid) {
            Optional<Figure> selectedFigure = board.getSelectedFigure();
            // to make figure come back it needs to be set with field coordinates to become normalized and not go outside chessboard
            foundSignature = board.findFieldSignatureByScreenCoordinates((int) board.getInitialPointerPosition().x, (int) board.getInitialPointerPosition().y);
            Vector2 fieldCoordinates = board.findFieldCoordinates(foundSignature);
            selectedFigure.ifPresent(figure -> figure.setPosition(
                fieldCoordinates.x,
                fieldCoordinates.y
            ));
        }

        board.setSelectedFigureAsEmpty();
        return false;
    }


    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
