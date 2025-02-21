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
    public static final String TETRIS_FIELD_BLUE = "textures/fields/tetris_field_blue.png";
    public static final String TETRIS_FIELD_GREEN = "textures/fields/tetris_field_green.png";
    public static final String TETRIS_FIELD_ROSE = "textures/fields/tetris_field_rose.png";
    public static final String TETRIS_FIELD_YELLOW = "textures/fields/tetris_field_yellow.png";
    public static final String TETRIS_FIELD_RED = "textures/fields/tetris_field_red.png";


    // numbers
    public static final String ONE_WHITE_TEXTURE = "textures/numbers/one_white.png";
    public static final String TWO_WHITE_TEXTURE = "textures/numbers/two_white.png";
    public static final String THREE_WHITE_TEXTURE = "textures/numbers/three_white.png";
    public static final String FOUR_WHITE_TEXTURE = "textures/numbers/four_white.png";
    public static final String FIVE_WHITE_TEXTURE = "textures/numbers/five_white.png";
    public static final String SIX_WHITE_TEXTURE = "textures/numbers/six_white.png";
    public static final String SEVEN_WHITE_TEXTURE = "textures/numbers/seven_white.png";
    public static final String EIGHT_WHITE_TEXTURE = "textures/numbers/eight_white.png";
    public static final String ONE_BlACK_TEXTURE = "textures/numbers/one_black.png";
    public static final String TWO_BlACK_TEXTURE = "textures/numbers/two_black.png";
    public static final String THREE_BlACK_TEXTURE = "textures/numbers/three_black.png";
    public static final String FOUR_BlACK_TEXTURE = "textures/numbers/four_black.png";
    public static final String FIVE_BlACK_TEXTURE = "textures/numbers/five_black.png";
    public static final String SIX_BlACK_TEXTURE = "textures/numbers/six_black.png";
    public static final String SEVEN_BlACK_TEXTURE = "textures/numbers/seven_black.png";
    public static final String EIGHT_BlACK_TEXTURE = "textures/numbers/eight_black.png";
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
        manager.load(TETRIS_FIELD_BLUE, Texture.class);
        manager.load(TETRIS_FIELD_GREEN, Texture.class);
        manager.load(TETRIS_FIELD_ROSE, Texture.class);
        manager.load(TETRIS_FIELD_YELLOW, Texture.class);
        manager.load(TETRIS_FIELD_RED, Texture.class);
        manager.load(ONE_WHITE_TEXTURE, Texture.class);
        manager.load(TWO_WHITE_TEXTURE, Texture.class);
        manager.load(THREE_WHITE_TEXTURE, Texture.class);
        manager.load(FOUR_WHITE_TEXTURE, Texture.class);
        manager.load(FIVE_WHITE_TEXTURE, Texture.class);
        manager.load(SIX_WHITE_TEXTURE, Texture.class);
        manager.load(SEVEN_WHITE_TEXTURE, Texture.class);
        manager.load(EIGHT_WHITE_TEXTURE, Texture.class);
        manager.load(ONE_BlACK_TEXTURE, Texture.class);
        manager.load(TWO_BlACK_TEXTURE, Texture.class);
        manager.load(THREE_BlACK_TEXTURE, Texture.class);
        manager.load(FOUR_BlACK_TEXTURE, Texture.class);
        manager.load(FIVE_BlACK_TEXTURE, Texture.class);
        manager.load(SIX_BlACK_TEXTURE, Texture.class);
        manager.load(SEVEN_BlACK_TEXTURE, Texture.class);
        manager.load(EIGHT_BlACK_TEXTURE, Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }

}
