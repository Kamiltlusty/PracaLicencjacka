package pl.kamil.TetriChess.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Field {
    public enum OccupiedState {
        EMPTY,
        TAKEN,
        FREE
    }
    public enum BlockedState {
        FREE,
        BLOCKED
    }

    private OccupiedState occupiedState;
    private BlockedState blockedState;
    Texture fieldTexture;

    public Rectangle getPosition() {
        return position;
    }

    private final Rectangle position = new Rectangle();


    public Field(Texture fieldColor, float positionX, float positionY, float width, float height) {
        occupiedState = OccupiedState.EMPTY;
        blockedState = BlockedState.FREE;
        this.fieldTexture = fieldColor;
        this.position.x = positionX;
        this.position.y = positionY;
        this.position.width = width;
        this.position.height = height;
    }

    public Texture getFieldTexture() {
        return fieldTexture;
    }
}
