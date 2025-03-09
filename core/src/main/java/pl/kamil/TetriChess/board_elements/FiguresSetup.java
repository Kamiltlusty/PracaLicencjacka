package pl.kamil.TetriChess.board_elements;

import com.badlogic.gdx.graphics.Texture;
import pl.kamil.TetriChess.board_elements.figures.*;
import pl.kamil.TetriChess.resources.Assets;

import static pl.kamil.TetriChess.resources.GlobalVariables.BOARD_FIELD_NUM;

public class FiguresSetup {
    private final BoardManager board;
    private final Assets assets;

    // figures textures
    private Texture pawn_texture_white;
    private Texture rook_texture_white;
    private Texture knight_texture_white;
    private Texture bishop_texture_white;
    private Texture king_texture_white;
    private Texture queen_texture_white;
    private Texture pawn_texture_black;
    private Texture rook_texture_black;
    private Texture knight_texture_black;
    private Texture bishop_texture_black;
    private Texture king_texture_black;
    private Texture queen_texture_black;

    public FiguresSetup(BoardManager board, Assets assets) {
        this.board = board;
        this.assets = assets;

        // initialize figures
        initializeFigures();
    }

    public void initializeFigures() {
        pawn_texture_white = assets.manager.get(Assets.PAWN_TEXTURE_WHITE);
        rook_texture_white = assets.manager.get(Assets.ROOK_TEXTURE_WHITE);
        knight_texture_white = assets.manager.get(Assets.KNIGHT_TEXTURE_WHITE);
        bishop_texture_white = assets.manager.get(Assets.BISHOP_TEXTURE_WHITE);
        king_texture_white = assets.manager.get(Assets.KING_TEXTURE_WHITE);
        queen_texture_white = assets.manager.get(Assets.QUEEN_TEXTURE_WHITE);
        pawn_texture_black = assets.manager.get(Assets.PAWN_TEXTURE_BLACK);
        rook_texture_black = assets.manager.get(Assets.ROOK_TEXTURE_BLACK);
        knight_texture_black = assets.manager.get(Assets.KNIGHT_TEXTURE_BLACK);
        bishop_texture_black = assets.manager.get(Assets.BISHOP_TEXTURE_BLACK);
        king_texture_black = assets.manager.get(Assets.KING_TEXTURE_BLACK);
        queen_texture_black = assets.manager.get(Assets.QUEEN_TEXTURE_BLACK);
    }


    public void setOpponentQueen() {
        int i = 7;
        int j = 3;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Queen(
                "Q" + idNum, // zle
                queen_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    public void setPlayerQueen() {
        int i = 0;
        int j = 3;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Queen(
                "Q" + idNum, // zle
                queen_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    public void setPlayerKing() {
        int i = 0;
        int j = 4;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new King(
                "K" + idNum, // zle
                king_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    public void setOpponentKing() {
        int i = 7;
        int j = 4;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new King(
                "K" + idNum, // zle
                king_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }


    public void setOpponentBishops() {
        int i = 7;
        int j = 2;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 5;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    public void setPlayerBishops() {
        int i = 0;
        int j = 2;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 5;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Bishop(
                "b" + idNum, // zle
                bishop_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }


    public void setPlayerKnights() {
        int i = 0;
        int j = 1;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 6;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    public void setOpponentKnights() {
        int i = 7;
        int j = 1;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 6;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Knigth(
                "k" + idNum, // zle
                knight_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    public void setPlayerRooks() {
        int i = 0;
        int j = 0;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Rook(
                "r" + idNum, // zle
                rook_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 7;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Rook(
                "r" + idNum, // zle
                rook_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
    }

    public void setOpponentRooks() {
        int i = 7;
        int j = 0;
        String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        int idNum = j + 1;
        board.figuresList.add(new Rook(
                "r" + idNum,
                rook_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 7;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Rook(
                "r" + idNum,
                rook_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
    }

    public void setOpponentPawns() {
        int i = 6;
        for (int j = 0; j < BOARD_FIELD_NUM; j++) {
            String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
            int idNum = j + 1;
            board.figuresList.add(new Pawn(
                    "p" + idNum,
                    pawn_texture_black,
                    board.getFieldsMap().get(fieldSignature).getPosition().x,
                    board.getFieldsMap().get(fieldSignature).getPosition().y,
                    Team.BLACK
                )
            );
        }
    }

    public void setPlayerPawns() {
        int i = 1;
        for (int j = 0; j < BOARD_FIELD_NUM; j++) {
            String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
            int idNum = j + 1;
            board.figuresList.add(new Pawn(
                    "p" + idNum,
                    pawn_texture_white,
                    board.getFieldsMap().get(fieldSignature).getPosition().x,
                    board.getFieldsMap().get(fieldSignature).getPosition().y,
                    Team.WHITE
                )
            );
        }
    }
}
