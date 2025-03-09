package pl.kamil.TetriChess.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.kamil.TetriChess.board_elements.BoardManager;
import pl.kamil.TetriChess.board_elements.Field;
import pl.kamil.TetriChess.board_elements.figures.Figure;

public class FigureDrawer {
    private final BoardManager board;
    private final SpriteBatch batch;

    public FigureDrawer(BoardManager board, SpriteBatch batch) {
        this.board = board;
        this.batch = batch;
    }

    public void drawFigures() {
        for (int k = 0; k < board.figuresList.size(); k++) {
            Figure figure;
            Field field = board.getFieldsMap().get("a1");
            if (board.figuresList.get(k) != null) figure = board.figuresList.get(k);
            else continue;
            drawFigure(field, figure);
        }
    }

    private void drawFigure(Field field, Figure figure) {
        batch.draw(
            figure.getFigureTexture(),
            figure.getPosition().x * field.getFieldTexture().getWidth(),
            figure.getPosition().y * field.getFieldTexture().getHeight(),
            figure.getFigureTexture().getWidth() * 0.95f,
            figure.getFigureTexture().getHeight() * 0.95f
        );
    }
}
