package pl.kamil.TetriChess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import pl.kamil.TetriChess.Main;
import pl.kamil.TetriChess.board_elements.Team;
import pl.kamil.TetriChess.drawing.SettingsDrawer;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.resources.Assets;


import static pl.kamil.TetriChess.resources.GlobalVariables.MIN_WORLD_HEIGHT;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_WIDTH;

public class SettingsScreen implements Screen, InputProcessor {
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final Assets assets;
    private final GameFlow gameFlow;
    private SettingsDrawer settingsDrawer;
    private boolean touchDownExit = false;
    private final MenuScreen menuScreen;
    private final Main main;

    private Rectangle eb;

    // Scene2D UI
    private Stage stage;
    private Skin skin;
    private Slider slider;
    private CheckBox whitePlayerCheckBox;
    private Label sliderLabel;

    private int currentSliderValue = 3;
    private boolean checkbox = true;

    public SettingsScreen(GameFlow gameFlow, SpriteBatch batch, Assets assets, MenuScreen menuScreen, Main main) {
        this.gameFlow = gameFlow;
        this.batch = batch;
        this.assets = assets;
        this.menuScreen = menuScreen;
        this.main = main;
        this.settingsDrawer = new SettingsDrawer(assets, batch);

        // set up the viewport
        viewport = new ExtendViewport(
            WORLD_WIDTH,
            MIN_WORLD_HEIGHT,
            WORLD_WIDTH,
            0
        );

        this.eb = settingsDrawer.getExitButton();
    }

    @Override
    public void show() {
        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));

        slider = new Slider(1, 6, 1, false, skin);
        slider.setValue(currentSliderValue);

        sliderLabel = new Label("Bot depth: " + (int) slider.getValue(), skin);
        sliderLabel.setFontScale(0.9f);

        whitePlayerCheckBox = new CheckBox("Play White", skin);
        whitePlayerCheckBox.setChecked(checkbox);
        whitePlayerCheckBox.getLabel().setFontScale(0.9f);

        whitePlayerCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                checkbox = whitePlayerCheckBox.isChecked();
            }
        });

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                currentSliderValue = (int) slider.getValue();
                sliderLabel.setText("Bot depth: " + (int) slider.getValue());
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.padTop(-170);
        table.padLeft(435);
        table.defaults().space(5);

        table.add(sliderLabel).center();
        table.row();
        table.add(slider).width(80).center();
        table.row();
        table.add(whitePlayerCheckBox).center();


        stage.addActor(table);
        stage.getRoot().setScale(0.25f);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BROWN);

//        // set the sprite batch to use the camera
//        batch.setProjectionMatrix(viewport.getCamera().combined);

        // begin drawing
        batch.begin();

        settingsDrawer.drawBackground();
        settingsDrawer.drawExitButton();

        // end drawing
        batch.end();

        stage.act(v);
        stage.draw();
    }

    public int transformY(int screenY) {
        // Reverse of Y for screen counting from left top corner to left bottom
        float screenHeight = Gdx.graphics.getHeight();
        return (int) (screenHeight - screenY);
    }

    @Override
    public void resize(int width, int height) {
        // update the viewport with the new screen size
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);

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
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        int transformedY = transformY(y);

        if (x >= eb.x && transformedY >= eb.y && x <= eb.x + eb.getWidth() && transformedY <= eb.y + eb.getHeight()) {
            touchDownExit = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        try {
            if (touchDownExit) {
                main.setScreen(menuScreen);
            }
        } finally {
            touchDownExit = false;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public int getSelectedLevel() {
        if (slider == null) {
            return 3;
        } else {
            return (int) slider.getValue();
        }
    }

    public void setSelectedLevel() {
        slider.setValue(getSelectedLevel());
    }

    public Team isWhitePlayerSelected() {
        if (whitePlayerCheckBox == null || whitePlayerCheckBox.isChecked()) {
            return Team.BLACK;
        } else {
            return Team.WHITE;
        }
    }

}
