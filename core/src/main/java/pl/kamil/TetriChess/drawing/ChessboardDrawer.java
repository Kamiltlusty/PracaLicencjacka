package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;

public class ChessboardDrawer {
    private final SpriteBatch batch;
    private final BoardManager board;
    private final DrawUtils drawUtils;

    public ChessboardDrawer(SpriteBatch batch, BoardManager board, DrawUtils drawUtils) {
        this.batch = batch;
        this.board = board;
        this.drawUtils = drawUtils;
    }

    public void drawChessBoard() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                String fieldSignature = drawUtils.findFieldSignature(i, j);
                Field field = board.getFieldsMap().get(fieldSignature);
                drawField(field, i, j);
            }
        }
    }
    private void drawField(Field field, int i, int j) {
        batch.draw(
            field.getFieldTexture(),
            field.getPosition().x * field.getFieldTexture().getWidth(),
            field.getPosition().y * field.getFieldTexture().getHeight(),
            field.getFieldTexture().getWidth(),
            field.getFieldTexture().getHeight()
        );
        if (field.isVisibleLetter()) {
            batch.draw(
                field.getLetterTexture(),
                field.getPosition().x * field.getFieldTexture().getWidth(),
                field.getPosition().y * field.getFieldTexture().getHeight(),
                field.getFieldTexture().getWidth(),
                field.getFieldTexture().getHeight()
            );
        }
        if (field.isVisibleNumber()) {
            batch.draw(
                field.getNumberTexture(),
                field.getPosition().x * field.getNumberTexture().getWidth(),
                field.getPosition().y * field.getNumberTexture().getHeight(),
                field.getNumberTexture().getWidth(),
                field.getNumberTexture().getHeight()
            );
        }
    }
}
