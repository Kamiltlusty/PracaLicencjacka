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
    Texture numberTexture;
    Texture letterTexture;
    boolean isVisibleNumber;
    boolean isVisibleLetter;

    public Rectangle getPosition() {
        return position;
    }

    private final Rectangle position = new Rectangle();


    public Field(Texture fieldColor,
                 Texture numberTexture,
                 Texture letterTexture,
                 boolean isVisibleNumber,
                 boolean isVisibleLetter,
                 float positionX,
                 float positionY,
                 float width,
                 float height) {
        occupiedState = OccupiedState.EMPTY;
        blockedState = BlockedState.FREE;
        this.fieldTexture = fieldColor;
        this.numberTexture = numberTexture;
        this.letterTexture = letterTexture;
        this.isVisibleNumber = isVisibleNumber;
        this.isVisibleLetter = isVisibleLetter;
        this.position.x = positionX;
        this.position.y = positionY;
        this.position.width = width;
        this.position.height = height;
    }

    public Texture getFieldTexture() {
        return fieldTexture;
    }

    public void setOccupiedState(OccupiedState occupiedState) {
        this.occupiedState = occupiedState;
    }

    public void setBlockedState(BlockedState blockedState) {
        this.blockedState = blockedState;
    }

    public BlockedState getBlockedState() {
        return blockedState;
    }

    public OccupiedState getOccupiedState() {
        return occupiedState;
    }

    public Texture getNumberTexture() {
        return numberTexture;
    }

    public Texture getLetterTexture() {
        return letterTexture;
    }

    public boolean isVisibleNumber() {
        return isVisibleNumber;
    }

    public boolean isVisibleLetter() {
        return isVisibleLetter;
    }
}
