package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import pl.kamil.TetriChess.resources.Assets;

import java.util.HashMap;
import java.util.Map;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;

public class Board {
    public final Map<String, Field> fieldsMap;
    private final Assets assets;


    // field
    private Texture white_field_texture;
    private Texture black_field_texture;

    public Board(Assets assets) {
        fieldsMap = new HashMap<>();
        this.assets = assets; // tworze nowe za kazdym razem bo libgdx korzysta z wielowatkowosci
        // crate textures
        initializeBoardFields();
    }


    public void initializeBoardFields() {
        black_field_texture = assets.manager.get(Assets.BLACK_FIELD_TEXTURE);
        white_field_texture = assets.manager.get(Assets.WHITE_FIELD_TEXTURE);
    }

    public void setFieldsMap() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                char letter = (char) ('A' + j);
                String position = letter + "" + i + 1;
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    fieldsMap.put(position, new Field(white_field_texture, i, j, white_field_texture.getWidth(), white_field_texture.getHeight()));
                } else {
                    fieldsMap.put(position, new Field(black_field_texture, i, j, white_field_texture.getWidth(), white_field_texture.getHeight()));
                }

            }
        }
    }

    public Map<String, Field> getFieldsMap() {
        return fieldsMap;
    }

}
