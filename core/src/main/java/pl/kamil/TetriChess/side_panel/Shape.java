package pl.kamil.TetriChess.side_panel;

import com.badlogic.gdx.graphics.Texture;
import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape {
    protected Square3x3 square;
    protected Texture texture;
    protected Character letter;
    protected Integer number;

    public Square3x3 getSquare3x3() {
        return square;
    }

    public Texture getTexture() {
        return texture;
    }

    public Shape(Texture texture, Character letter, Integer number) {
        this.square = new Square3x3(texture);
        this.texture = texture;
        this.letter = letter;
        this.number = number;
    }

    protected Shape rotate(Integer rotation) {
        for (int i = 0; i < rotation; i++) {
            List<Integer> toDel = new ArrayList<>();
            List<Tuple2<Integer, Texture>> toAdd = new ArrayList<>();
            for (int j = 1; j <= 9; j++) {
                if (j == 5) continue;
                Texture texture;
                var square1x1 = square.getSquares().get(j);
                if (square1x1.getTexture() != null) {
                    texture = square1x1.getTexture();
                } else continue;
                toDel.add(j);
                Integer nextPos = getNextPos(j);
                toAdd.add(new Tuple2<>(nextPos, texture));
            }
            // after gathering all new texture positions can delete
            toDel.forEach(l -> square.getSquares().get(l).setTexture(null));
            // add textures on new places
            toAdd.forEach(t -> square.getSquares().get(t._1).setTexture(t._2));
        }
        return this;
    }

    private Integer getNextPos(int i) {
        return switch (i) {
            case 1 -> 7;
            case 2  -> 4;
            case 3  -> 1;
            case 4  -> 8;
            case 6 -> 2;
            case 7 -> 9;
            case 8 -> 6;
            case 9 -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + i + " range of values should be [1,4] && [6,9]");
        };
    }

    protected abstract void setShape();

    public Character getLetter() {
        return letter;
    }

    public Integer getNumber() {
        return number;
    }
}
