package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.objects.Board;
import pl.kamil.TetriChess.objects.Field;
import pl.kamil.TetriChess.objects.Figure;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.side_panel.Square1x1;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static pl.kamil.TetriChess.resources.GlobalVariables.*;

public class GameScreen implements Screen, InputProcessor {
    private final GameFlow gameFlow;
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;

    // field
    private Texture major_field_texture;
    private Texture minor_field_texture;

    // numbers
    private Texture one_texture;
    private Texture two_texture;
    private Texture three_texture;
    private Texture four_texture;
    private Texture one_white_texture;
    private Texture two_white_texture;
    private Texture three_white_texture;
    private Texture four_white_texture;
    private Texture five_white_texture;
    private Texture six_white_texture;
    private Texture seven_white_texture;
    private Texture eight_white_texture;
    private Texture one_black_texture;
    private Texture two_black_texture;
    private Texture three_black_texture;
    private Texture four_black_texture;
    private Texture five_black_texture;
    private Texture six_black_texture;
    private Texture seven_black_texture;
    private Texture eight_black_texture;


    // boards
    private Board board;
    private static final int CUBE_FIELD_NUM = 3;

    public GameScreen(GameFlow gameFlow, SpriteBatch batch, Assets assets) {
        this.gameFlow = gameFlow;
        this.batch = batch;
        this.assets = assets;

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        // create board
        board = gameFlow.getBoard();

        // create cubes
        initializeCubeFields();
        // create numbers
        initializeNumbers();
    }

    public void initializeCubeFields() {
        major_field_texture = assets.manager.get(Assets.MAJOR_FIELD_TEXTURE);
        minor_field_texture = assets.manager.get(Assets.MINOR_FIELD_TEXTURE);
    }

    public void initializeNumbers() {
        one_texture = assets.manager.get(Assets.ONE_TEXTURE);
        two_texture = assets.manager.get(Assets.TWO_TEXTURE);
        three_texture = assets.manager.get(Assets.THREE_TEXTURE);
        four_texture = assets.manager.get(Assets.FOUR_TEXTURE);
        one_white_texture = assets.manager.get(Assets.ONE_WHITE_TEXTURE);
        two_white_texture = assets.manager.get(Assets.TWO_WHITE_TEXTURE);
        three_white_texture = assets.manager.get(Assets.THREE_WHITE_TEXTURE);
        four_white_texture = assets.manager.get(Assets.FOUR_WHITE_TEXTURE);
        five_white_texture = assets.manager.get(Assets.FIVE_WHITE_TEXTURE);
        six_white_texture = assets.manager.get(Assets.SIX_WHITE_TEXTURE);
        seven_white_texture = assets.manager.get(Assets.SEVEN_WHITE_TEXTURE);
        eight_white_texture = assets.manager.get(Assets.EIGHT_WHITE_TEXTURE);
        one_black_texture = assets.manager.get(Assets.ONE_BlACK_TEXTURE);
        two_black_texture = assets.manager.get(Assets.TWO_BlACK_TEXTURE);
        three_black_texture = assets.manager.get(Assets.THREE_BlACK_TEXTURE);
        four_black_texture = assets.manager.get(Assets.FOUR_BlACK_TEXTURE);
        five_black_texture = assets.manager.get(Assets.FIVE_BlACK_TEXTURE);
        six_black_texture = assets.manager.get(Assets.SIX_BlACK_TEXTURE);
        seven_black_texture = assets.manager.get(Assets.SEVEN_BlACK_TEXTURE);
        eight_black_texture = assets.manager.get(Assets.EIGHT_BlACK_TEXTURE);
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
//        batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        batch.begin();

        drawChessBoard();
        // draw 3x3 cube side
        drawMajorCube();
        // draw scaled cubes
        drawCubes();
        // draw numbers
        drawNumbers();
        drawFigures();

        // end drawing
        batch.end();
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
    }

    private void drawMinor1Cube() {
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
                940 + el.getRectangle().x * 2/3,
                300 + el.getRectangle().y * 2/3,
                el.getRectangle().getWidth() * 2/3,
                el.getRectangle().getHeight() * 2/3
            );
        }
    }

    private void drawMinor2Cube() {
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
                1200 + el.getRectangle().x * 2/3,
                300 + el.getRectangle().y * 2/3,
                el.getRectangle().getWidth() * 2/3,
                el.getRectangle().getHeight() * 2/3
            );
        }
    }

    private void drawMinor3Cube() {
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
                1075 + el.getRectangle().x * 2/3,
                50 + el.getRectangle().y * 2/3,
                el.getRectangle().getWidth() * 2/3,
                el.getRectangle().getHeight() * 2/3
            );
        }
    }

    private void drawNumber1() {
        batch.draw(
            one_texture,
            920,
            800 ,
            one_texture.getWidth(),
            one_texture.getHeight()
        );
    }

    private void drawNumber2() {
        batch.draw(
            two_texture,
            870,
            445,
            two_texture.getWidth(),
            two_texture.getHeight()
        );
    }

    private void drawNumber3() {
        batch.draw(
            three_texture,
            1128,
            445,
            three_texture.getWidth(),
            three_texture.getHeight()
        );
    }

    private void drawNumber4() {
        batch.draw(
            four_texture,
            1000,
            190,
            four_texture.getWidth(),
            four_texture.getHeight()
        );
    }

    private void drawChessBoard() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                String fieldSignature = findFieldSignature(i, j);
                Field field = board.getFieldsMap().get(fieldSignature);
                drawField(field, i, j);
            }
        }
    }

    /**
     * i oraz j nie sa w tej funkcji powiazane ze wspolrzednymi,
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


    private void drawField(Field field, int i, int j) {
        batch.draw(
            field.getFieldTexture(),
            field.getPosition().x * field.getFieldTexture().getWidth(),
            field.getPosition().y * field.getFieldTexture().getHeight(),
            field.getFieldTexture().getWidth(),
            field.getFieldTexture().getHeight()
        );
        batch.draw(
            field.getFieldTexture(),
            field.getPosition().x * field.getFieldTexture().getWidth(),
            field.getPosition().y * field.getFieldTexture().getHeight(),
            field.getFieldTexture().getWidth(),
            field.getFieldTexture().getHeight()
        );
        batch.draw(
            field.getFieldTexture(),
            field.getPosition().x * field.getFieldTexture().getWidth(),
            field.getPosition().y * field.getFieldTexture().getHeight(),
            field.getFieldTexture().getWidth(),
            field.getFieldTexture().getHeight()
        );
    }

    private void drawFigure(Field field, Figure figure) {
        batch.draw(
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
                gameFlow.getActive().equals(selectedFigure.get().getTeam()) &&
                selectedFigure.get().isMoveLegal(
                    board.getInitialFieldPosition(),
                    board.getFinalFieldPosition(),
                    selectedFigure.get(),
                    board
                    )
            ) {
                // if figure stays at the same position do not count it as a move
                if (board.getInitialFieldPosition().x == board.getFinalFieldPosition().x
                && board.getInitialFieldPosition().y == board.getFinalFieldPosition().y) {
                    allValid = false;
                } else {
                    selectedFigure.get().setPosition(
                        fieldCoordinates.x,
                        fieldCoordinates.y
                    );
                }
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
        } else {
            // prepare for next move
            gameFlow.prepare();
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
