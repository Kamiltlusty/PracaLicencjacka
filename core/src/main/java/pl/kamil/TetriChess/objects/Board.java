package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import pl.kamil.TetriChess.resources.Assets;

import java.util.*;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;
import static pl.kamil.TetriChess.resources.GlobalVariables.WORLD_SCALE;

public class Board {
    private final Map<String, Field> fieldsMap;
    public final List<Figure> figuresList;
    private final Assets assets;
    private Optional<Figure> selectedFigure;
    private Vector2 initialPointerPosition;
    private Vector2 pointerPosition;
    private Vector2 initialFieldPosition;
    private Vector2 finalFieldPosition;



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


    public Vector2 getInitialPointerPosition() {
        return initialPointerPosition;
    }

    public void setInitialPointerPosition(float x, float y) {
        this.initialPointerPosition.x = x;
        this.initialPointerPosition.y = y;
    }

    public Vector2 getInitialFieldPosition() {
        return initialFieldPosition;
    }

    public Vector2 getFinalFieldPosition() {
        return finalFieldPosition;
    }

    public void setInitialFieldPosition(float x, float y) {
        this.initialFieldPosition.x = x;
        this.initialFieldPosition.y = y;
    }

    public void setFinalFieldPosition(float x, float y) {
        this.finalFieldPosition.x = x;
        this.finalFieldPosition.y = y;
    }

    public Board(Assets assets) {
        figuresList = new ArrayList<>();
        fieldsMap = new HashMap<>();
        this.assets = assets;

        // set selected figure as empty
        this.selectedFigure = Optional.empty();
        // initialize mouse pointer
        this.pointerPosition = new Vector2();
        this.initialPointerPosition = new Vector2();
        this.initialFieldPosition = new Vector2();
        this.finalFieldPosition = new Vector2();
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


    public void findFigureByCoordinates(float x, float y) {
        // find figure by coordinates
        for (int i = 0; i < figuresList.size(); i++) {
            if (x == figuresList.get(i).getPosition().x &&
                y == figuresList.get(i).getPosition().y) {
                selectedFigure = Optional.ofNullable(figuresList.get(i));
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

    private void setPlayerBishops() {
        int i = 0;
        int j = 2;
        String fieldSignature = findFieldSignature(i, j);
        int idNum = j + 1;
        figuresList.add(new Bishop(
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
        figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture,
                fieldsMap.get(fieldSignature).getPosition().x,
                fieldsMap.get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }


    private void setPlayerKnights() {
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
        return new StringBuilder().append(letter).append(i + 1).toString();
    }
    public String findFieldSignatureByScreenCoordinates(int screenX, int screenY) {
        int j = screenX / black_field_texture.getWidth();
        int i = screenY / black_field_texture.getHeight();
        if (j >= 0 && j < 8 && i >= 0 && i < 8) {
            char letter = (char) ('A' + j);
            int idNum = i + 1; // tutaj i jest obliczane z ekranu dlatego nie musze odejmowac od 8
            return new StringBuilder().append(letter).append(idNum).toString();
        } else return "-1";
    }

    public void setSelectedFigureAsEmpty() {
        this.selectedFigure = Optional.empty();
    }

    public Optional<Figure> getSelectedFigure() {
        return selectedFigure;
    }

    public Map<String, Field> getFieldsMap() {
        return fieldsMap;
    }

    public Vector2 getPointerPosition() {
        return pointerPosition;
    }

    public void setPointerPosition(float x, float y) {
        this.pointerPosition.set(x, y);
    }
}
