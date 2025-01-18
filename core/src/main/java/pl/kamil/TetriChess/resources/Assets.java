package pl.kamil.TetriChess.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    // asset manager
    public final AssetManager manager = new AssetManager();

    // figures
    public static final String KING_TEXTURE_WHITE = "textures/figures/king_white.png";
    public static final String PAWN_TEXTURE_WHITE = "textures/figures/pawn_white.png";
    public static final String KNIGHT_TEXTURE_WHITE = "textures/figures/knight_white.png";
    public static final String QUEEN_TEXTURE_WHITE = "textures/figures/queen_white.png";
    public static final String ROOK_TEXTURE_WHITE = "textures/figures/rook_white.png";
    public static final String BISHOP_TEXTURE_WHITE = "textures/figures/bishop_white.png";
    public static final String KING_TEXTURE_BLACK = "textures/figures/king_black.png";
    public static final String PAWN_TEXTURE_BLACK = "textures/figures/pawn_black.png";
    public static final String KNIGHT_TEXTURE_BLACK = "textures/figures/knight_black.png";
    public static final String QUEEN_TEXTURE_BLACK = "textures/figures/queen_black.png";
    public static final String ROOK_TEXTURE_BLACK = "textures/figures/rook_black.png";
    public static final String BISHOP_TEXTURE_BLACK = "textures/figures/bishop_black.png";

    // fields
    public static final String FIELD_TEXTURE_WHITE = "textures/fields/field_white.png";
    public static final String FIELD_TEXTURE_BLACK = "textures/fields/field_black.png";
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
        manager.load(KING_TEXTURE_WHITE, Texture.class);
        manager.load(PAWN_TEXTURE_WHITE, Texture.class);
        manager.load(KNIGHT_TEXTURE_WHITE, Texture.class);
        manager.load(QUEEN_TEXTURE_WHITE, Texture.class);
        manager.load(ROOK_TEXTURE_WHITE, Texture.class);
        manager.load(BISHOP_TEXTURE_WHITE, Texture.class);
        manager.load(KING_TEXTURE_BLACK, Texture.class);
        manager.load(PAWN_TEXTURE_BLACK, Texture.class);
        manager.load(KNIGHT_TEXTURE_BLACK, Texture.class);
        manager.load(QUEEN_TEXTURE_BLACK, Texture.class);
        manager.load(ROOK_TEXTURE_BLACK, Texture.class);
        manager.load(BISHOP_TEXTURE_BLACK, Texture.class);
        manager.load(FIELD_TEXTURE_WHITE, Texture.class);
        manager.load(FIELD_TEXTURE_BLACK, Texture.class);
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
