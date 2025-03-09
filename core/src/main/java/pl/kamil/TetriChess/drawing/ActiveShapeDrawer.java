package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.gameplay.GameFlow;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.side_panel.Shape;
import pl.kamil.TetriChess.side_panel.Square1x1;

import java.util.List;

import static pl.kamil.TetriChess.resources.GlobalVariables.SQUARE_1X1_SIDE;

public class ActiveShapeDrawer {
    private final GameFlow gameFlow;
    private final BoardManager board;
    private final SpriteBatch batch;
    private final DrawUtils drawUtils;

    public ActiveShapeDrawer(GameFlow gameFlow, BoardManager board, SpriteBatch batch, DrawUtils drawUtils) {
        this.gameFlow = gameFlow;
        this.board = board;
        this.batch = batch;
        this.drawUtils = drawUtils;
    }

    public void drawActiveShape() {
        Shape activeShape = gameFlow.getActiveShape();
        if (activeShape != null) {
            Character letter = activeShape.getLetter();
            Integer number = activeShape.getNumber();
            String fieldSignature = letter + String.valueOf(number);
            // to make fields blocked
            Field field = board.getFieldsMap().get(fieldSignature);
            List<Square1x1> list = activeShape.getSquare3x3().getSquares().values().stream().filter(v -> v.getTexture() != null).toList();
            for (var el : list) {
                batch.draw(
                    el.getTexture(),
                    field.getPosition().x * SQUARE_1X1_SIDE + el.getRectangle().x,
                    field.getPosition().y * SQUARE_1X1_SIDE + el.getRectangle().y,
                    el.getRectangle().getWidth(),
                    el.getRectangle().getHeight()
                );
                String signature = drawUtils.findFieldSignature(
                    (int) (field.getPosition().y + el.getRectangle().y/SQUARE_1X1_SIDE),
                    (int) (field.getPosition().x + el.getRectangle().x/SQUARE_1X1_SIDE));
                Field fieldToBlock = board.getFieldsMap().get(signature);
                fieldToBlock.setBlockedState(Field.BlockedState.BLOCKED);
            }
        }
    }
}
