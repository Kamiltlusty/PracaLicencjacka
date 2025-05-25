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
        board.figuresList.add(new Queen(
                "QB",
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
        board.figuresList.add(new Queen(
                "QW",
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
        board.figuresList.add(new King(
                "KW",
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
        board.figuresList.add(new King(
                "KB",
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
        board.figuresList.add(new Bishop(
                "b1B",
                bishop_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 5;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        board.figuresList.add(new Bishop(
                "b2B",
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
        board.figuresList.add(new Bishop(
                "b1W",
                bishop_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 5;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        board.figuresList.add(new Bishop(
                "b2W",
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
        board.figuresList.add(new Knight(
                "k1W",
                knight_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 6;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        idNum = j + 1;
        board.figuresList.add(new Knight(
                "k2W",
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
        board.figuresList.add(new Knight(
                "k1B",
                knight_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 6;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        board.figuresList.add(new Knight(
                "k2B",
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
        board.figuresList.add(new Rook(
                "r1W",
                rook_texture_white,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.WHITE
            )
        );
        j = 7;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        board.figuresList.add(new Rook(
                "r2W",
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
                "r1B",
                rook_texture_black,
                board.getFieldsMap().get(fieldSignature).getPosition().x,
                board.getFieldsMap().get(fieldSignature).getPosition().y,
                Team.BLACK
            )
        );
        j = 7;
        fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
        board.figuresList.add(new Rook(
                "r2B",
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
                    "p" + idNum + "B",
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
                    "p" + idNum + "W",
                    pawn_texture_white,
                    board.getFieldsMap().get(fieldSignature).getPosition().x,
                    board.getFieldsMap().get(fieldSignature).getPosition().y,
                    Team.WHITE
                )
            );
        }
    }

    public void setupFromFEN(String fen) {
        board.figuresList.clear();

        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/");

        for (int i = 0; i < 8; i++) {
            int j = 0;
            char[] charArray = rows[i].toCharArray();
            for (int k = 0; k < charArray.length; k++) {
                char symbol = charArray[k];
                if (Character.isDigit(symbol)) {
                    j += Character.getNumericValue(symbol);
                } else {
                    int start = rows[i].indexOf('{');
                    int end = rows[i].indexOf('}');
                    int len = end - start;

                    Team team = rows[i].charAt(end - 1) == 'W' ? Team.WHITE : Team.BLACK;
                    char pieceType = rows[i].charAt(start + 1);
                    // 4{KW}3 = 4 - 1 = 3

                    Texture texture = getTextureForPiece(pieceType, team);
                    String fieldSignature = board.getBoardUtils().findFieldSignature(i, j);
                    float x = board.getFieldsMap().get(fieldSignature).getPosition().x;
                    float y = board.getFieldsMap().get(fieldSignature).getPosition().y;

                    Figure piece = createFigure(pieceType, team, texture, x, y);
                    if (piece != null) {
                        board.figuresList.add(piece);
                    }
                    j++;
                    k += len;
                }
            }
        }
    }

    private Texture getTextureForPiece(char type, Team team) {
        switch (type) {
            case 'p': return team == Team.WHITE ? pawn_texture_white : pawn_texture_black;
            case 'r': return team == Team.WHITE ? rook_texture_white : rook_texture_black;
            case 'k': return team == Team.WHITE ? knight_texture_white : knight_texture_black;
            case 'b': return team == Team.WHITE ? bishop_texture_white : bishop_texture_black;
            case 'Q': return team == Team.WHITE ? queen_texture_white : queen_texture_black;
            case 'K': return team == Team.WHITE ? king_texture_white : king_texture_black;
            default: return null;
        }
    }

    private Figure createFigure(char type, Team team, Texture texture, float x, float y) {
        String id = type + (team == Team.WHITE ? "W" : "B");

        return switch (type) {
            case 'p' -> new Pawn(id, texture, x, y, team);
            case 'r' -> new Rook(id, texture, x, y, team);
            case 'k' -> new Knight(id, texture, x, y, team);
            case 'b' -> new Bishop(id, texture, x, y, team);
            case 'Q' -> new Queen(id, texture, x, y, team);
            case 'K' -> new King(id, texture, x, y, team);
            default -> null;
        };
    }
}
