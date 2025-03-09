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
import pl.kamil.TetriChess.drawing.DrawManager;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.figures.Figure;
import pl.kamil.TetriChess.resources.Assets;

import java.util.Objects;
import java.util.Optional;

import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class GameScreen implements Screen, InputProcessor {
    private final GameFlow gameFlow;
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final DrawManager drawManager;

    // boards
    private BoardManager board;

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
        this.drawManager = new DrawManager(assets, board, batch, gameFlow);
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

        drawManager.getChessboardDrawer().drawChessBoard();
        // draw 3x3 cube side
        drawManager.getCubesDrawer().drawMajorCube();
        // draw scaled cubes
        drawCubes();
        // draw numbers
        drawNumbers();
        // draw figures
        drawManager.getFigureDrawer().drawFigures();
        drawManager.getActiveShapeDrawer().drawActiveShape();

        // end drawing
        batch.end();
    }

    private void drawNumbers() {
        drawManager.getNumbersDrawer().drawNumber1();
        drawManager.getNumbersDrawer().drawNumber2();
        drawManager.getNumbersDrawer().drawNumber3();
        drawManager.getNumbersDrawer().drawNumber4();
    }

    private void drawCubes() {
        drawManager.getCubesDrawer().drawMinor1Cube();
        drawManager.getCubesDrawer().drawMinor2Cube();
        drawManager.getCubesDrawer().drawMinor3Cube();
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
        String foundSignature = board.getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
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
        Texture fieldTexture = board.getFieldsMap().get("a1").getFieldTexture();

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
        String foundSignature = board.getBoardUtils().findFieldSignatureByScreenCoordinates(screenX, transformedY);
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
            foundSignature = board.getBoardUtils().findFieldSignatureByScreenCoordinates((int) board.getInitialPointerPosition().x, (int) board.getInitialPointerPosition().y);
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
