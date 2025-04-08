package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.resources.Assets;
import pl.kamil.TetriChess.resources.GlobalVariables;

public class BoardUtils {
    private final Assets assets;

    // numbers textures
    private Texture one_white_texture;
    private Texture two_white_texture;
    private Texture three_white_texture;
    private Texture four_white_texture;
    private Texture five_white_texture;
    private Texture six_white_texture;
    private Texture seven_white_texture;
    private Texture eight_white_texture;
    private Texture one_black_texture;
    private Texture two_black_texture;
    private Texture three_black_texture;
    private Texture four_black_texture;
    private Texture five_black_texture;
    private Texture six_black_texture;
    private Texture seven_black_texture;
    private Texture eight_black_texture;

    // letters textures
    private Texture a_white_texture;
    private Texture b_white_texture;
    private Texture c_white_texture;
    private Texture d_white_texture;
    private Texture e_white_texture;
    private Texture f_white_texture;
    private Texture g_white_texture;
    private Texture h_white_texture;
    private Texture a_black_texture;
    private Texture b_black_texture;
    private Texture c_black_texture;
    private Texture d_black_texture;
    private Texture e_black_texture;
    private Texture f_black_texture;
    private Texture g_black_texture;
    private Texture h_black_texture;

    public BoardUtils(Assets assets) {
        this.assets = assets;

        // create numbers texture
        initializeNumbers();
        // create letters textures
        initializeLetters();
    }

    public void initializeNumbers() {
        one_white_texture = assets.manager.get(Assets.ONE_WHITE_TEXTURE);
        two_white_texture = assets.manager.get(Assets.TWO_WHITE_TEXTURE);
        three_white_texture = assets.manager.get(Assets.THREE_WHITE_TEXTURE);
        four_white_texture = assets.manager.get(Assets.FOUR_WHITE_TEXTURE);
        five_white_texture = assets.manager.get(Assets.FIVE_WHITE_TEXTURE);
        six_white_texture = assets.manager.get(Assets.SIX_WHITE_TEXTURE);
        seven_white_texture = assets.manager.get(Assets.SEVEN_WHITE_TEXTURE);
        eight_white_texture = assets.manager.get(Assets.EIGHT_WHITE_TEXTURE);
        one_black_texture = assets.manager.get(Assets.ONE_BlACK_TEXTURE);
        two_black_texture = assets.manager.get(Assets.TWO_BlACK_TEXTURE);
        three_black_texture = assets.manager.get(Assets.THREE_BlACK_TEXTURE);
        four_black_texture = assets.manager.get(Assets.FOUR_BlACK_TEXTURE);
        five_black_texture = assets.manager.get(Assets.FIVE_BlACK_TEXTURE);
        six_black_texture = assets.manager.get(Assets.SIX_BlACK_TEXTURE);
        seven_black_texture = assets.manager.get(Assets.SEVEN_BlACK_TEXTURE);
        eight_black_texture = assets.manager.get(Assets.EIGHT_BlACK_TEXTURE);
    }

    public void initializeLetters() {
        a_white_texture = assets.manager.get(Assets.A_WHITE_TEXTURE);
        b_white_texture = assets.manager.get(Assets.B_WHITE_TEXTURE);
        c_white_texture = assets.manager.get(Assets.C_WHITE_TEXTURE);
        d_white_texture = assets.manager.get(Assets.D_WHITE_TEXTURE);
        e_white_texture = assets.manager.get(Assets.E_WHITE_TEXTURE);
        f_white_texture = assets.manager.get(Assets.F_WHITE_TEXTURE);
        g_white_texture = assets.manager.get(Assets.G_WHITE_TEXTURE);
        h_white_texture = assets.manager.get(Assets.H_WHITE_TEXTURE);
        a_black_texture = assets.manager.get(Assets.A_BlACK_TEXTURE);
        b_black_texture = assets.manager.get(Assets.B_BlACK_TEXTURE);
        c_black_texture = assets.manager.get(Assets.C_BlACK_TEXTURE);
        d_black_texture = assets.manager.get(Assets.D_BlACK_TEXTURE);
        e_black_texture = assets.manager.get(Assets.E_BlACK_TEXTURE);
        f_black_texture = assets.manager.get(Assets.F_BlACK_TEXTURE);
        g_black_texture = assets.manager.get(Assets.G_BlACK_TEXTURE);
        h_black_texture = assets.manager.get(Assets.H_BlACK_TEXTURE);
    }

    public Texture getWhiteNumTexture(int i) {
        return switch (i) {
            case 0 -> one_white_texture;
            case 1 -> two_white_texture;
            case 2 -> three_white_texture;
            case 3 -> four_white_texture;
            case 4 -> five_white_texture;
            case 5 -> six_white_texture;
            case 6 -> seven_white_texture;
            case 7 -> eight_white_texture;
            default -> null;
        };
    }
    public Texture getBlackNumTexture(int i) {
        return switch (i) {
            case 0 -> one_black_texture;
            case 1 -> two_black_texture;
            case 2 -> three_black_texture;
            case 3 -> four_black_texture;
            case 4 -> five_black_texture;
            case 5 -> six_black_texture;
            case 6 -> seven_black_texture;
            case 7 -> eight_black_texture;
            default -> null;
        };
    }
    public Texture getWhiteLetterTexture(int j) {
        return switch (j) {
            case 0 -> a_white_texture;
            case 1 -> b_white_texture;
            case 2 -> c_white_texture;
            case 3 -> d_white_texture;
            case 4 -> e_white_texture;
            case 5 -> f_white_texture;
            case 6 -> g_white_texture;
            case 7 -> h_white_texture;
            default -> null;
        };
    }
    public Texture getBlackLetterTexture(int j) {
        return switch (j) {
            case 0 -> a_black_texture;
            case 1 -> b_black_texture;
            case 2 -> c_black_texture;
            case 3 -> d_black_texture;
            case 4 -> e_black_texture;
            case 5 -> f_black_texture;
            case 6 -> g_black_texture;
            case 7 -> h_black_texture;
            default -> null;
        };
    }
    /**
     * i oraz j nie sa w tej funkjci powiazane ze wspolrzednymi,
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

    public float normalizeToGrid(float pos) {
        if (pos >= 0.0 && pos < 1.12) {
            return 0.0F;
        }
        else if (pos >= 1.12 && pos < 2.24) {
            return 1.0F;
        }
        else if (pos >= 2.24 && pos < 3.36) {
            return 2.0F;
        }
        else if (pos >= 3.36 && pos < 4.48) {
            return 3.0F;
        }
        else if (pos >= 4.48 && pos < 5.60) {
            return 4.0F;
        }
        else if (pos >= 5.60 && pos < 6.72) {
            return 5.0F;
        }
        else if (pos >= 6.72 && pos < 7.84) {
            return 6.0F;
        }
        else if (pos >= 7.84 && pos < 8.96) {
            return 7.0F;
        }
        return 0.0F;
    }

    public String findFieldSignatureByScreenCoordinates(int screenX, int screenY) {
        int j = screenX / GlobalVariables.BOARD_FIELD_SIDE_LENGTH;
        int i = screenY / GlobalVariables.BOARD_FIELD_SIDE_LENGTH;
        if (j >= 0 && j < 8 && i >= 0 && i < 8) {
            char letter = (char) ('a' + j);
            int idNum = i + 1; // tutaj i jest obliczane z ekranu dlatego nie musze odejmowac od 8
            return new StringBuilder().append(letter).append(idNum).toString();
        } else return "-1";
    }

    public String findFieldSignatureByCoordinates(int posX, int posY) {
        int j = posX;
        int i = posY;
        if (j >= 0 && j < 8 && i >= 0 && i < 8) {
            char letter = (char) ('a' + j);
            int idNum = i + 1; // tutaj i jest obliczane z ekranu dlatego nie musze odejmowac od 8
            return new StringBuilder().append(letter).append(idNum).toString();
        } else return "-1";
    }

    public Vector2 findPositionByFieldSignature(String signature) {
        char letter = signature.charAt(0);
        int posY = signature.charAt(1) - 1 - 48; // 48 = 0 ASCII
        int posX = changeLetterToPositionX(letter);
        return new Vector2(posX, posY);
    }

    public int changeLetterToPositionX(char letter) {
        return switch (letter) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> throw new IllegalStateException("Unexpected value: " + letter);
        };
    }

}
