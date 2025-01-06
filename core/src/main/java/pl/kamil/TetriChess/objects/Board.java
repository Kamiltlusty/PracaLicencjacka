package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.resources.Assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;

public class Board {
    public final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    private final Assets assets;


    // fields textures
    private Texture white_field_texture;
    private Texture black_field_texture;

    // figures textures
    private Texture pawn_texture;
    private Texture rook_texture;
    private Texture knight_texture;
    private Texture bishop_texture;
    private Texture king_texture;
    private Texture queen_texture;

    public Board(Assets assets) {
        figuresList = new ArrayList<>();
        fieldsMap = new HashMap<>();
        this.assets = assets;
        // crate textures
        initializeBoardFields();
        initializeFigures();

        // draw the chessboard
        setFieldsMap();
        setFiguresInitially();
    }

    private void initializeFigures() {
        pawn_texture = assets.manager.get(Assets.PAWN_TEXTURE);
        rook_texture = assets.manager.get(Assets.ROOK_TEXTURE);
        knight_texture = assets.manager.get(Assets.KNIGHT_TEXTURE);
        bishop_texture = assets.manager.get(Assets.BISHOP_TEXTURE);
        king_texture = assets.manager.get(Assets.KING_TEXTURE);
        queen_texture = assets.manager.get(Assets.QUEEN_TEXTURE);
    }


    public void initializeBoardFields() {
        black_field_texture = assets.manager.get(Assets.BLACK_FIELD_TEXTURE);
        white_field_texture = assets.manager.get(Assets.WHITE_FIELD_TEXTURE);
    }

    public void setFieldsMap() {
        for (int i = 0; i < BOARD_FIELD_NUM; i++) {
            for (int j = 0; j < BOARD_FIELD_NUM; j++) {
                String fieldSignature = findFieldSignature(i, j);
                if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                    fieldsMap.put(fieldSignature, new Field(white_field_texture, j, i, white_field_texture.getWidth(), white_field_texture.getHeight()));
                } else {
                    fieldsMap.put(fieldSignature, new Field(black_field_texture, j, i, black_field_texture.getWidth(), black_field_texture.getHeight()));
                }

            }
        }
    }

    public void setFiguresInitially() {
        setPlayerFigures();
        setOpponentFigures();

    }

    private void setOpponentFigures() {
        // opponent pawns
        setOpponentPawns();
        // opponent rooks
        setOpponentRooks();
        // opponent knights
        setOpponentKnights();
        // opponent bishops
        setOpponentBishops();
        // opponent king
        setOpponentKing();
        // setOpponentQueen
        setOpponentQueen();
    }

    private void setPlayerFigures() {
        // player pawns
        setPlayerPawns();
        // player rooks
        setPlayerRooks();
        // player knights
        setPlayerKnights();
        // player bishops
        setPlayerBishops();
        // player king
        setPlayerKing();
        // setPlayerQueen
        setPlayerQueen();
    }

    public Vector2 findFieldCoordinates(String fieldSignature) {
        Field foundField = fieldsMap.get(fieldSignature);
        float x = foundField.getPosition().x;
        float y = foundField.getPosition().y;
        return new Vector2(x, y);
    }

    public void findFigureByCoordinatesAndErase(float x, float y) {
        // find figure by coordinates
        for (int i = 0; i < figuresList.size(); i++) {
            if (x == figuresList.get(i).getPosition().x &&
                y == figuresList.get(i).getPosition().y) {
                figuresList.get(i).setPosition(0, 0);
            }
        }
    }


    private void setOpponentQueen() {
        int i = 7;
        int j = 3;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Queen(
                "Q" + idNum, // zle
                queen_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    private void setPlayerQueen() {
        int i = 0;
        int j = 3;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Queen(
                "Q" + idNum, // zle
                queen_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    private void setPlayerKing() {
        int i = 0;
        int j = 4;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new King(
                "K" + idNum, // zle
                king_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }
    private void setOpponentKing() {
        int i = 7;
        int j = 4;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new King(
                "K" + idNum, // zle
                king_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }


    private void setOpponentBishops() {
        int i = 7;
        int j = 2;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 5;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    private void setPlayerKnights() {
        int i = 0;
        int j = 2;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Knigth(
                "b" + idNum, // zle
                bishop_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 5;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Knigth(
                "b" + idNum, // zle
                bishop_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }


    private void setPlayerBishops() {
        int i = 0;
        int j = 1;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 6;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    private void setOpponentKnights() {
        int i = 7;
        int j = 1;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 6;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    private void setPlayerRooks() {
        int i = 0;
        int j = 0;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Rook(
                "r" + idNum, // zle
                rook_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 7;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Rook(
                "r" + idNum, // zle
                rook_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }
    private void setOpponentRooks() {
        int i = 7;
        int j = 0;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Rook(
                "r" + idNum,
                rook_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 7;
        fieldSignature = findFieldSignature(i, j);
        idNum = j + 1;
        figuresList.add(new Rook(
                "r" + idNum,
                rook_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    private void setOpponentPawns() {
        int i = 6;
        for (int j = 0; j < BOARD_FIELD_NUM; j++) {
            String fieldSignature = findFieldSignature(i, j);
            int idNum = j + 1;
            figuresList.add(new Pawn(
                "p" + idNum,
                pawn_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.BLACK
                )
            );
        }
    }

    private void setPlayerPawns() {
        int i = 1;
        for (int j = 0; j < BOARD_FIELD_NUM; j++) {
            String fieldSignature = findFieldSignature(i, j);
            int idNum = j + 1;
            figuresList.add(new Pawn(
                "p" + idNum,
                pawn_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
                )
            );
        }
    }

    /**
     * i oraz j nie sa w tej funkjci powiazane ze wspolrzednymi,
     * dlatego sa intami i nie sa ustawione w kolejnosci float j, float i
     * @param i
     * @param j
     * @return
     */
    private String findFieldSignature(int i, int j) {
        char letter = (char) ('A' + j);
        int idNum = 8 - i;
        return new StringBuilder().append(letter).append(idNum).toString();
    }
    public String findFieldSignatureByScreenCoordinates(int screenX, int screenY) {
        int i = screenX / black_field_texture.getHeight();
        int j = screenY / black_field_texture.getWidth();
        char letter = (char) ('A' + j);
        int idNum = i + 1; // tutaj i jest obliczane z ekranu dlatego nie musze odejmowac od 8
        String string = new StringBuilder().append(letter).append(idNum).toString();
        System.out.println(string);
        return string;
    }

    public Map<String, Field> getFieldsMap() {
        return fieldsMap;
    }

}
