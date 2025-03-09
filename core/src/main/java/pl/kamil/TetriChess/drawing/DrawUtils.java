package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.Texture;
import pl.kamil.TetriChess.resources.Assets;

public class DrawUtils {
    private final Assets assets;
    // panel on cubes numbers
    public Texture p_two_black_texture;
    public Texture p_three_black_texture;
    public Texture p_four_black_texture;
    public Texture p_five_black_texture;
    public Texture p_six_black_texture;
    public Texture p_seven_black_texture;
    public Texture p_eight_black_texture;

    // panel on cubes letters
    public Texture p_a_black_texture;
    public Texture p_b_black_texture;
    public Texture p_c_black_texture;
    public Texture p_d_black_texture;
    public Texture p_e_black_texture;
    public Texture p_f_black_texture;
    public Texture p_h_black_texture;
    public Texture p_g_black_texture;

    public DrawUtils(Assets assets) {
        this.assets = assets;

        // panel on cubes numbers
        initPOnCubesNums();
        // panel on cubes letters
        initPOnCubesLetters();
    }

    private void initPOnCubesNums() {
        p_two_black_texture = assets.manager.get(Assets.P_TWO_BLACK_TEXTURE);
        p_three_black_texture = assets.manager.get(Assets.P_THREE_BLACK_TEXTURE);
        p_four_black_texture = assets.manager.get(Assets.P_FOUR_BLACK_TEXTURE);
        p_five_black_texture = assets.manager.get(Assets.P_FIVE_BLACK_TEXTURE);
        p_six_black_texture = assets.manager.get(Assets.P_SIX_BLACK_TEXTURE);
        p_seven_black_texture = assets.manager.get(Assets.P_SEVEN_BLACK_TEXTURE);
        p_eight_black_texture = assets.manager.get(Assets.P_SEVEN_BLACK_TEXTURE);

    }
    private void initPOnCubesLetters() {
        p_a_black_texture = assets.manager.get(Assets.P_A_BLACK_TEXTURE);
        p_b_black_texture = assets.manager.get(Assets.P_B_BLACK_TEXTURE);
        p_c_black_texture = assets.manager.get(Assets.P_C_BLACK_TEXTURE);
        p_d_black_texture = assets.manager.get(Assets.P_D_BLACK_TEXTURE);
        p_e_black_texture = assets.manager.get(Assets.P_E_BLACK_TEXTURE);
        p_f_black_texture = assets.manager.get(Assets.P_F_BLACK_TEXTURE);
        p_g_black_texture = assets.manager.get(Assets.P_G_BLACK_TEXTURE);
        p_h_black_texture = assets.manager.get(Assets.P_H_BLACK_TEXTURE);
    }

    /**
     * i oraz j nie sa w tej funkcji powiazane ze wspolrzednymi,
     * dlatego sa intami i nie sa ustawione w kolejnosci float j, float i
     *
     * @param i
     * @param j
     * @return
     */
    public String findFieldSignature(int i, int j) {
        char letter = (char) ('a' + j);
        return new StringBuilder().append(letter).append(i + 1).toString();
    }

    public Texture getPanelLetterTexture(char letter) {
        return switch (letter) {
            case 'a' -> p_a_black_texture;
            case 'b' -> p_b_black_texture;
            case 'c' -> p_c_black_texture;
            case 'd' -> p_d_black_texture;
            case 'e' -> p_e_black_texture;
            case 'f' -> p_f_black_texture;
            case 'g' -> p_g_black_texture;
            case 'h' -> p_h_black_texture;
            default -> null;
        };
    }

    public Texture getPanelNumTexture(int number) {
        return switch (number) {
            case 2 -> p_two_black_texture;
            case 3 -> p_three_black_texture;
            case 4 -> p_four_black_texture;
            case 5 -> p_five_black_texture;
            case 6 -> p_six_black_texture;
            case 7 -> p_seven_black_texture;
            case 8 -> p_eight_black_texture;
            default -> null;
        };
    }
}
