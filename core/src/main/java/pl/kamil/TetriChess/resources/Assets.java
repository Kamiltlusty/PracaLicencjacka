package pl.kamil.TetriChess.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    // asset manager
    public final AssetManager manager = new AssetManager();

    // figures
    public static final String KING_TEXTURE = "textures/king.png";
    public static final String PAWN_TEXTURE = "textures/pawn.png";
    public static final String KNIGHT_TEXTURE = "textures/knight.png";
    public static final String QUEEN_TEXTURE = "textures/queen.png";
    public static final String ROOK_TEXTURE = "textures/rook.png";
    public static final String BISHOP_TEXTURE = "textures/bishop.png";

    // fields
    public static final String WHITE_FIELD_TEXTURE = "textures/white_field.png";
    public static final String BLACK_FIELD_TEXTURE = "textures/gray_field.png";
    public static final String MAJOR_FIELD_TEXTURE = "textures/major_field.png";
    public static final String MINOR_FIELD_TEXTURE = "textures/minor_field.png";

    // numbers
    public static final String ONE_TEXTURE = "textures/one.png";
    public static final String TWO_TEXTURE = "textures/two.png";
    public static final String THREE_TEXTURE = "textures/three.png";
    public static final String FOUR_TEXTURE = "textures/four.png";




    public void load() {
        // load all assets
        loadGameplayAssets();
    }

    private void loadGameplayAssets() {
        manager.load(KING_TEXTURE, Texture.class);
        manager.load(PAWN_TEXTURE, Texture.class);
        manager.load(KNIGHT_TEXTURE, Texture.class);
        manager.load(QUEEN_TEXTURE, Texture.class);
        manager.load(ROOK_TEXTURE, Texture.class);
        manager.load(BISHOP_TEXTURE, Texture.class);
        manager.load(WHITE_FIELD_TEXTURE, Texture.class);
        manager.load(BLACK_FIELD_TEXTURE, Texture.class);
        manager.load(MAJOR_FIELD_TEXTURE, Texture.class);
        manager.load(MINOR_FIELD_TEXTURE, Texture.class);
        manager.load(ONE_TEXTURE, Texture.class);
        manager.load(TWO_TEXTURE, Texture.class);
        manager.load(THREE_TEXTURE, Texture.class);
        manager.load(FOUR_TEXTURE, Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }

}
