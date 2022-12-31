package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerFallDown_LTR;
import fightinggame.animation.player.PlayerFallDown_RTL;
import fightinggame.entity.Background;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import java.util.List;
import fightinggame.entity.Character;
import fightinggame.entity.CharacterState;
import fightinggame.entity.Player;

public abstract class MovementHandler extends Handler {

    protected Gameplay gameplay;

    public MovementHandler(Gameplay gameplay, String name) {
        super(name);
        this.gameplay = gameplay;
    }

    public void applyGravityCharacter(Character character) {
        if (character.getStandPlatform() != null) {
            if (!character.isAttack() && !character.isInAir()) {
                if (character.getStats().getVely() < gameplay.gravity) {
                    character.getStats().setVely(character.getStats().getVely() + 0.05f);
                }
                float vely = character.getStats().getVely();
                GamePosition position = character.getPosition();
                double nY = vely + character.getYMaxHitBox();
                if (character.getStandPlatform().isCanStand()) {
                    if (nY < character.getStandPlatform().getPosition().getYPosition()) {
                        position.setYPosition((int) vely + position.getYPosition());
                        falldownMove(character);
                    } else {
                        if (character.getStandPlatform() != null) {
                            int distance = character.getStandPlatform().getPosition().getYPosition() - character.getYMaxHitBox();
                            distance = Math.abs(distance) - 10;
                            position.setYPosition(position.getYPosition() - distance);
                        }
                        if (character.isFallDown()) {
                            if (character.getCurrAnimation() != null) {
                                if (character.getCurrAnimation() instanceof PlayerFallDown_LTR || character.getCurrAnimation() instanceof PlayerFallDown_RTL) {
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
                        if(character instanceof Player) {
                            ((Player)character).setIsAirAttack(false);
                        }
                        character.getStats().setVely(0);
                        character.setFallDown(false);
                    }
                } else {
                    if (character.getStandPlatform() instanceof BlankTile) {
                        position.setYPosition((int) vely + position.getYPosition());
                        falldownMove(character);
                        character.setFallDown(true);
                        if (character.isLTR()) {
                            if (character.getCurrAnimation() instanceof PlayerFallDown_LTR); else {
                                character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_LTR));
                            }
                        } else {
                            if (character.getCurrAnimation() instanceof PlayerFallDown_RTL); else {
                                character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_RTL));
                            }
                        }
                    }
                }
            }
        }
    }

    public void falldownMove(Character character) {
        if (character.getPosition().isMoveRight) {
            if (canMoveCheck(MoveState.RIGHT, character)) {
                if(character.isInAir()) return;
                character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_LTR));
            }
        } else if (character.getPosition().isMoveLeft) {
            if (canMoveCheck(MoveState.LEFT, character)) {
                if(character.isInAir()) return;
                character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_RTL));
            }
        }
    }

    public boolean canMoveCheck(MoveState state, Character character) {
        if (character == null) {
            return false;
        }
        if (character.getInsidePlatform() == null) {
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
            case SLIDE:
                if (!character.getPosition().isSlide) {
                    return false;
                }
                break;
        }
        boolean canMove = true;
        boolean isMove = false;
        List<List<Platform>> surroundPlatforms;
        surroundPlatforms = gameplay.getSurroundPlatform(character.getInsidePlatform().getRow(),
                character.getInsidePlatform().getColumn());
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
                            case SLIDE:
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
                        if (!checkRightTile(character, speed)) {
                            return false;
                        }
                        return character.moveRight();
                    case LEFT:
                        if (!checkLeftTile(character, speed)) {
                            return false;
                        }
                        return character.moveLeft();
                    case JUMP:
                        return character.jump();
                    case SLIDE:
                        if (character.isLTR()) {
                            if (!checkRightTile(character, speed * 2)) {
                                return false;
                            }
                            return character.slideRight();
                        } else {
                            if (!checkLeftTile(character, speed * 2)) {
                                return false;
                            }
                            return character.slideLeft();
                        }
                }
            }
        }
        return canMove && isMove;
    }

    public boolean checkRightTile(Character character, int speed) {
        try {
            Platform insidePlatform = character.getInsidePlatform();
            if (insidePlatform == null) {
                return false;
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
        return true;
    }

    public boolean checkLeftTile(Character character, int speed) {
        try {
            Platform insidePlatform = character.getInsidePlatform();
            if (insidePlatform == null) {
                return false;
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
        return true;
    }

}
