package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.graphics.Texture;
import pl.kamil.TetriChess.resources.Assets;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;

public class FieldsSetup {
    private final BoardManager board;
    private final Assets assets;

    // fields textures
    private Texture white_field_texture;
    private Texture black_field_texture;

    public FieldsSetup(BoardManager board, Assets assets) {
        this.board = board;
        this.assets = assets;

        // crate textures
        initializeBoardFields();
    }

    public void initializeBoardFields() {
        black_field_texture = assets.manager.get(Assets.FIELD_TEXTURE_BLACK);
        white_field_texture = assets.manager.get(Assets.FIELD_TEXTURE_WHITE);
    }

    public void setFieldsMap() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    if (j == 0 && i == 0) {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(white_field_texture,
                                board.getBoardUtils().getBlackNumTexture(i), // to be visible it has to be opposite to field color
                                board.getBoardUtils().getBlackLetterTexture(j),
                                true,
                                true,
                                j,
                                i,
                                white_field_texture.getWidth(),
                                white_field_texture.getHeight()));
                    } else if (i != 0 && j == 0) {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(white_field_texture,
                                board.getBoardUtils().getBlackNumTexture(i), // to be visible it has to be opposite to field color
                                board.getBoardUtils().getBlackLetterTexture(j),
                                true,
                                false,
                                j,
                                i,
                                white_field_texture.getWidth(),
                                white_field_texture.getHeight()));
                    } else if (i == 0) {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(white_field_texture,
                                board.getBoardUtils().getBlackNumTexture(i), // to be visible it has to be opposite to field color
                                board.getBoardUtils().getBlackLetterTexture(j),
                                false,
                                true,
                                j,
                                i,
                                white_field_texture.getWidth(),
                                white_field_texture.getHeight()));
                    }
                    else {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(white_field_texture,
                                board.getBoardUtils().getBlackNumTexture(i), // to be visible it has to be opposite to field color
                                board.getBoardUtils().getBlackLetterTexture(j),
                                false,
                                false,
                                j,
                                i,
                                white_field_texture.getWidth(),
                                white_field_texture.getHeight()));
                    }
                } else {
                    if (i != 0 && j == 0) {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(black_field_texture,
                                board.getBoardUtils().getWhiteNumTexture(i),
                                board.getBoardUtils().getWhiteLetterTexture(j),
                                true,
                                false,
                                j,
                                i,
                                black_field_texture.getWidth(),
                                black_field_texture.getHeight()));
                    }
                    else if (i == 0) {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(black_field_texture,
                                board.getBoardUtils().getWhiteNumTexture(i), // to be visible it has to be opposite to field color
                                board.getBoardUtils().getWhiteLetterTexture(j),
                                false,
                                true,
                                j,
                                i,
                                black_field_texture.getWidth(),
                                black_field_texture.getHeight()));
                    }
                    else {
                        board.getFieldsMap().put(fieldSignature,
                            new Field(black_field_texture,
                                board.getBoardUtils().getWhiteNumTexture(i),
                                board.getBoardUtils().getWhiteLetterTexture(j),
                                false,
                                false,
                                j,
                                i,
                                black_field_texture.getWidth(),
                                black_field_texture.getHeight()));
                    }
                }

            }
        }
    }
}
