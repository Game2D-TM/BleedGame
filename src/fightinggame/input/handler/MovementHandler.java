package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.entity.Background;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.util.List;
import fightinggame.entity.Character;

public abstract class MovementHandler extends Handler {

    protected Gameplay gameplay;

    public MovementHandler(Gameplay gameplay, String name) {
        super(name);
        this.gameplay = gameplay;
    }

    public boolean canMoveCheck(MoveState state, Character character) {
        if (character == null) {
            return false;
        }
        if (character.getCurPlatform() == null) {
            return false;
        }
        switch (state) {
            case RIGHT:
                if (!character.getPosition().isMoveRight) {
                    return false;
                }
                break;
            case LEFT:
                if (!character.getPosition().isMoveLeft) {
                    return false;
                }
                break;
        }
        boolean canMove = true;
        boolean isMove = false;
        List<List<Platform>> surroundPlatforms;
        surroundPlatforms = gameplay.getSurroundPlatform(character.getCurPlatform().getRow(),
                character.getCurPlatform().getColumn());
        GamePosition position = character.getPosition();
        int speed = character.getSpeed();
        if (surroundPlatforms != null && surroundPlatforms.size() > 0) {
            for (int i = 0; i < surroundPlatforms.size(); i++) {
                List<Platform> platforms = surroundPlatforms.get(i);
                if (platforms != null && platforms.size() > 0) {
                    for (int j = 0; j < platforms.size(); j++) {
                        Platform platform = platforms.get(j);
                        if (platform == null) {
                            break;
                        }
                        if (platform.getPosition() == null) {
                            break;
                        }
                        GamePosition checkPos = new GamePosition(
                                position.getXPosition(),
                                position.getYPosition(), // position.getYPosition() + position.getHeight() / 2
                                position.getWidth(), position.getHeight()); // position.getHeight() / 2
                        switch (state) {
                            case RIGHT:
                                if (j >= Background.DEF_SURROUND_TILE) {
                                    checkPos.setXPosition(checkPos.getXPosition() + speed + checkPos.getWidth());
                                    if (canMove) {
                                        if (platform instanceof WallTile || platform instanceof Tile) {
                                            if (platform.checkValidPosition(checkPos)) {
                                                canMove = false;
                                                isMove = true;
                                                break;
                                            }
                                        }
                                        if (canMove) {
                                            if (platform instanceof BlankTile) {
                                                if (platform.checkValidPosition(checkPos)) {
                                                    isMove = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case LEFT:
                                if (j <= Background.DEF_SURROUND_TILE) {
                                    checkPos.setXPosition(checkPos.getXPosition() - speed);
                                    if (canMove) {
                                        if (platform instanceof WallTile || platform instanceof Tile) {
                                            if (platform.checkValidPosition(checkPos)) {
                                                canMove = false;
                                                isMove = true;
                                                break;
                                            }
                                        }
                                        if (canMove) {
                                            if (platform instanceof BlankTile) {
                                                if (platform.checkValidPosition(checkPos)) {
                                                    isMove = true;
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case UP:
                                if (canMove) {
                                    checkPos.setYPosition(checkPos.getYPosition() - speed - checkPos.getHeight());
                                    if (platform instanceof WallTile || platform instanceof Tile) {
                                        if (platform.checkValidPosition(checkPos)) {
                                            canMove = false;
                                            isMove = true;
                                            break;
                                        }
                                    }
                                    if (platform instanceof BlankTile) {
                                        if (platform.checkValidPosition(checkPos)) {
                                            isMove = true;
                                        }
                                    }
                                }
                                break;
                            case DOWN:
                                if (canMove) {
                                    checkPos.setYPosition(checkPos.getYPosition() + speed + checkPos.getHeight());
                                    if (platform instanceof WallTile || platform instanceof Tile) {
                                        if (platform.checkValidPosition(checkPos)) {
                                            canMove = false;
                                            isMove = true;
                                            break;
                                        }
                                    }
                                    if (platform instanceof BlankTile) {
                                        if (platform.checkValidPosition(checkPos)) {
                                            isMove = true;
                                        }
                                    }
                                }
                                break;
                        }
                        if (isMove) {
                            break;
                        }
                    }
                    if (isMove) {
                        break;
                    }
                }
            }
            if(canMove && isMove) {
                switch(state) {
                    case RIGHT:
                        character.moveRight();
                        break;
                    case LEFT:
                        character.moveLeft();
                        break;
                    case UP:
                        character.moveUp();
                        break;
                    case DOWN:
                        character.moveDown();
                        break;
                }
            }
        }
        return canMove && isMove;
    }

}
