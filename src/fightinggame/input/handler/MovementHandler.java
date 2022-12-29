package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerFallDownLTR;
import fightinggame.animation.player.PlayerFallDownRTL;
import fightinggame.entity.Background;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.util.List;
import fightinggame.entity.Character;
import fightinggame.entity.CharacterState;

public abstract class MovementHandler extends Handler {

    protected Gameplay gameplay;

    public MovementHandler(Gameplay gameplay, String name) {
        super(name);
        this.gameplay = gameplay;
    }

    public void applyGravityCharacter(Character character) {
        if (character.getStandPlatform() != null) {
            if (!character.isAttack() && !character.isInAir()) {
                if (character.getVely() < gameplay.gravity) {
                    character.setVely(character.getVely() + 0.05f);
                }
                float vely = character.getVely();
                GamePosition position = character.getPosition();
                double nY = vely + character.getYMaxHitBox();
                if (character.getStandPlatform().isCanStand()) {
                    if (nY < character.getStandPlatform().getPosition().getYPosition()) {
                        position.setYPosition((int) vely + position.getYPosition());
                        if (character.getPosition().isMoveRight) {
                            canMoveCheck(MoveState.RIGHT, character);
                        } else if (character.getPosition().isMoveLeft) {
                            canMoveCheck(MoveState.LEFT, character);
                        }
                    } else {
                        int distance = character.getStandPlatform().getPosition().getYPosition() - character.getYMaxHitBox();
                        distance = Math.abs(distance);
                        position.setYPosition(position.getYPosition() - distance);
                        if (character.isFallDown()) {
                            if (character.getCurrAnimation() != null) {
                                if (character.getCurrAnimation() instanceof PlayerFallDownLTR || character.getCurrAnimation() instanceof PlayerFallDownRTL) {
                                    if (character.getPosition().isMoveRight) {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.RUNFORWARD));
                                    } else if (character.getPosition().isMoveLeft) {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.RUNBACK));
                                    } else {
                                        if (character.isLTR()) {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.IDLE_LTR));
                                        } else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.IDLE_RTL));
                                        }
                                    }
                                }
                            }
                        }
                        character.setVely(0);
                        character.setFallDown(false);
                    }
                } else {
                    if (character.getStandPlatform() instanceof BlankTile) {
                        position.setYPosition((int) vely + position.getYPosition());
                        if (character.getPosition().isMoveRight) {
                            canMoveCheck(MoveState.RIGHT, character);
                        } else if (character.getPosition().isMoveLeft) {
                            canMoveCheck(MoveState.LEFT, character);
                        }
                    }
                }
            }
        }
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
//        GamePosition position = character.getPosition();
        int speed = character.getStats().getSpeed();
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
//                        GamePosition checkPos = new GamePosition(
//                                position.getXPosition(),
//                                position.getYPosition(), // position.getYPosition() + position.getHeight() / 2
//                                position.getWidth(), position.getHeight()); // position.getHeight() / 2
                        GamePosition checkPos = new GamePosition(character.getXHitBox(), character.getYHitBox(),
                                character.getWidthHitBox(), character.getHeightHitBox());
                        switch (state) {
                            case RIGHT:
                                if (j >= Background.DEF_SURROUND_TILE) {
                                    checkPos.setXPosition(checkPos.getXPosition() + speed);
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
                            case JUMP:
                                isMove = true;
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
            if (canMove && isMove) {
                switch (state) {
                    case RIGHT:
                    try {
                        Platform insidePlatform = character.getInsidePlatform();
                        if (insidePlatform == null) {
                            break;
                        }
                        Platform nextPlatform = gameplay.getPlatforms().get(insidePlatform.getRow()).get(insidePlatform.getColumn() + 1);
                        if (nextPlatform != null) {
                            GamePosition checkPos = new GamePosition(character.getXHitBox(), character.getYHitBox(),
                                    character.getWidthHitBox(), character.getHeightHitBox());
                            checkPos.setXPosition(checkPos.getXPosition() + speed);
                            if (nextPlatform instanceof Tile || nextPlatform instanceof WallTile) {
                                if (nextPlatform.checkValidPosition(checkPos)) {
                                    return false;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }
                    character.moveRight();
                    break;
                    case LEFT:
                        try {
                        Platform insidePlatform = character.getInsidePlatform();
                        if (insidePlatform == null) {
                            break;
                        }
                        Platform nextPlatform = gameplay.getPlatforms().get(insidePlatform.getRow()).get(insidePlatform.getColumn() - 1);
                        if (nextPlatform != null) {
                            GamePosition checkPos = new GamePosition(character.getXHitBox(), character.getYHitBox(),
                                    character.getWidthHitBox(), character.getHeightHitBox());
                            checkPos.setXPosition(checkPos.getXPosition() - speed);
                            if (nextPlatform instanceof Tile || nextPlatform instanceof WallTile) {
                                if (nextPlatform.checkValidPosition(checkPos)) {
                                    return false;
                                }
                            }
                        }
                    } catch (Exception ex) {

                    }
                    character.moveLeft();
                    break;
                    case JUMP:
                    character.jump();
                    break;
                }
            }
        }
        return canMove && isMove;
    }

}
