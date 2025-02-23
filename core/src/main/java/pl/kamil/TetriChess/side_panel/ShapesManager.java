package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;
import io.vavr.Tuple4;
import pl.kamil.TetriChess.resources.Assets;

import java.util.LinkedList;

public class ShapesManager {
    private final LinkedList<Shape> shapes = new LinkedList<>();
    private Boolean isFirst;
    private final ShapeValuesGenerator shapeValuesGenerator;
    private final Assets assets;
    private Texture greenTexture;
    private Texture roseTexture;
    private Texture blueTexture;
    private Texture yellowTexture;
    private Texture redTexture;

    public ShapesManager(Assets assets) {
        this.assets = assets;
        isFirst = true;
        initializeFieldsTextures();
        shapeValuesGenerator = new ShapeValuesGenerator();
    }

    private void initializeFieldsTextures() {
        greenTexture = assets.manager.get(Assets.TETRIS_FIELD_GREEN);
        roseTexture = assets.manager.get(Assets.TETRIS_FIELD_ROSE);
        blueTexture = assets.manager.get(Assets.TETRIS_FIELD_BLUE);
        yellowTexture = assets.manager.get(Assets.TETRIS_FIELD_YELLOW);
        redTexture = assets.manager.get(Assets.TETRIS_FIELD_RED);
    }

    public void generateShapes() {
        if (isFirst) {
            for (int i = 0; i < 4; i++) {
                Tuple4<Integer, Character, Integer, Integer> values = shapeValuesGenerator.generate();
                shapes.add(createShape(values._1, values._2, values._3, values._4));
            }
            isFirst = false;
        } else {
            Tuple4<Integer, Character, Integer, Integer> values = shapeValuesGenerator.generate();
            shapes.add(createShape(values._1, values._2, values._3, values._4));
        }
    }

    public LinkedList<Shape> getShapes() {
        return shapes;
    }

    private Shape createShape(Integer shape, Character letter, Integer number, Integer rotation) {
        Shape resultShape = switch (shape) {
            case 1 -> new GreenShape(greenTexture, letter, number);
            case 2 -> new RoseShape(roseTexture, letter, number);
            case 3 -> new BlueShape(blueTexture, letter, number);
            case 4 -> new YellowShape(yellowTexture, letter, number);
            case 5 -> new RedShape(redTexture, letter, number);
            default -> throw new IllegalStateException("Unexpected value: " + shape + " generated shape value should be in range [1, 5]");
        };
        resultShape.setShape();
        return resultShape.rotate(rotation);
    }
}
