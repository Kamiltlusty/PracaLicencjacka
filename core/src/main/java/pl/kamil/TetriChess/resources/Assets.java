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

    // panel numbers
    public static final String P_TWO_BLACK_TEXTURE = "textures/panel_numbers/p_two_black.png";
    public static final String P_THREE_BLACK_TEXTURE = "textures/panel_numbers/p_three_black.png";
    public static final String P_FOUR_BLACK_TEXTURE = "textures/panel_numbers/p_four_black.png";
    public static final String P_FIVE_BLACK_TEXTURE = "textures/panel_numbers/p_five_black.png";
    public static final String P_SIX_BLACK_TEXTURE = "textures/panel_numbers/p_six_black.png";
    public static final String P_SEVEN_BLACK_TEXTURE = "textures/panel_numbers/p_seven_black.png";
    public static final String P_EIGHT_BLACK_TEXTURE = "textures/panel_numbers/p_eight_black.png";

    // panel letters
    public static final String P_B_BLACK_TEXTURE = "textures/panel_letters/p_b_black.png";
    public static final String P_C_BLACK_TEXTURE = "textures/panel_letters/p_c_black.png";
    public static final String P_D_BLACK_TEXTURE = "textures/panel_letters/p_d_black.png";
    public static final String P_E_BLACK_TEXTURE = "textures/panel_letters/p_e_black.png";
    public static final String P_F_BLACK_TEXTURE = "textures/panel_letters/p_f_black.png";
    public static final String P_G_BLACK_TEXTURE = "textures/panel_letters/p_g_black.png";
    public static final String P_H_BLACK_TEXTURE = "textures/panel_letters/p_h_black.png";

    // letters
    public static final String A_WHITE_TEXTURE = "textures/letters/a_white.png";
    public static final String B_WHITE_TEXTURE = "textures/letters/b_white.png";
    public static final String C_WHITE_TEXTURE = "textures/letters/c_white.png";
    public static final String D_WHITE_TEXTURE = "textures/letters/d_white.png";
    public static final String E_WHITE_TEXTURE = "textures/letters/e_white.png";
    public static final String F_WHITE_TEXTURE = "textures/letters/f_white.png";
    public static final String G_WHITE_TEXTURE = "textures/letters/g_white.png";
    public static final String H_WHITE_TEXTURE = "textures/letters/h_white.png";
    public static final String A_BlACK_TEXTURE = "textures/letters/a_black.png";
    public static final String B_BlACK_TEXTURE = "textures/letters/b_black.png";
    public static final String C_BlACK_TEXTURE = "textures/letters/c_black.png";
    public static final String D_BlACK_TEXTURE = "textures/letters/d_black.png";
    public static final String E_BlACK_TEXTURE = "textures/letters/e_black.png";
    public static final String F_BlACK_TEXTURE = "textures/letters/f_black.png";
    public static final String G_BlACK_TEXTURE = "textures/letters/g_black.png";
    public static final String H_BlACK_TEXTURE = "textures/letters/h_black.png";

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
        manager.load(A_WHITE_TEXTURE, Texture.class);
        manager.load(B_WHITE_TEXTURE, Texture.class);
        manager.load(C_WHITE_TEXTURE, Texture.class);
        manager.load(D_WHITE_TEXTURE, Texture.class);
        manager.load(E_WHITE_TEXTURE, Texture.class);
        manager.load(F_WHITE_TEXTURE, Texture.class);
        manager.load(G_WHITE_TEXTURE, Texture.class);
        manager.load(H_WHITE_TEXTURE, Texture.class);
        manager.load(A_BlACK_TEXTURE, Texture.class);
        manager.load(B_BlACK_TEXTURE, Texture.class);
        manager.load(C_BlACK_TEXTURE, Texture.class);
        manager.load(D_BlACK_TEXTURE, Texture.class);
        manager.load(E_BlACK_TEXTURE, Texture.class);
        manager.load(F_BlACK_TEXTURE, Texture.class);
        manager.load(G_BlACK_TEXTURE, Texture.class);
        manager.load(H_BlACK_TEXTURE, Texture.class);
        manager.load(P_TWO_BLACK_TEXTURE, Texture.class);
        manager.load(P_THREE_BLACK_TEXTURE, Texture.class);
        manager.load(P_FOUR_BLACK_TEXTURE, Texture.class);
        manager.load(P_FIVE_BLACK_TEXTURE, Texture.class);
        manager.load(P_SIX_BLACK_TEXTURE, Texture.class);
        manager.load(P_SEVEN_BLACK_TEXTURE, Texture.class);
        manager.load(P_EIGHT_BLACK_TEXTURE, Texture.class);
        manager.load(P_B_BLACK_TEXTURE, Texture.class);
        manager.load(P_C_BLACK_TEXTURE, Texture.class);
        manager.load(P_D_BLACK_TEXTURE, Texture.class);
        manager.load(P_E_BLACK_TEXTURE, Texture.class);
        manager.load(P_F_BLACK_TEXTURE, Texture.class);
        manager.load(P_G_BLACK_TEXTURE, Texture.class);
        manager.load(P_H_BLACK_TEXTURE, Texture.class);
    }

    public void dispose() {
        manager.dispose();
    }

}
