package fightinggame.input.handler.game;

import fightinggame.entity.state.MoveState;
import fightinggame.Gameplay;
import fightinggame.animation.player.PlayerFallDown_LTR;
import fightinggame.animation.player.PlayerFallDown_RTL;
import fightinggame.animation.player.PlayerWallAction_LTR;
import fightinggame.animation.player.PlayerWallAction_RTL;
import fightinggame.entity.Background;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import fightinggame.entity.platform.tile.BlankTile;
import fightinggame.entity.platform.tile.Tile;
import fightinggame.entity.platform.tile.WallTile;
import fightinggame.entity.Character;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.Player;
import fightinggame.input.handler.GameHandler;
import java.util.List;

public abstract class MovementHandler extends GameHandler {

    protected Gameplay gameplay;

    public MovementHandler(Gameplay gameplay, String name) {
        super(name);
        this.gameplay = gameplay;
    }

    public boolean checkNextToBarrier(Character character) {
        try {
            Platform insidePlatform = character.getInsidePlatform();
            if (insidePlatform == null) {
                return false;
            }
            int nextColumn = insidePlatform.getColumn();
            int row = insidePlatform.getRow();
            int speed = -1;
            if (character.isLTR()) {
                speed = character.getStats().getSpeed();
                nextColumn += 1;
            } else {
                speed = -character.getStats().getSpeed();
                nextColumn -= 1;
            }
            Platform nextPlatform = gameplay.getPlatforms().get(row).get(nextColumn);
            if (nextPlatform == null) {
                return false;
            }
            if (nextPlatform.getPosition() == null) {
                return false;
            }
            GamePosition position = new GamePosition(character.getXHitBox() + speed, character.getYHitBox(),
                    character.getWidthHitBox(), character.getHeightHitBox());
            if (nextPlatform instanceof WallTile || nextPlatform instanceof Tile) {
                if (nextPlatform.checkValidPosition(position)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }

    // error need fix
    public boolean checkCanGrapTile(Character character) {
        try {
            Platform insidePlatform = character.getInsidePlatform();
            if (insidePlatform == null) {
                return false;
            }
            int nextColumn = insidePlatform.getColumn();
            int row = insidePlatform.getRow();
            int speed = -1;
            if (character.isLTR()) {
                speed = character.getStats().getSpeed();
                nextColumn += 1;
            } else {
                speed = -character.getStats().getSpeed();
                nextColumn -= 1;
            }
            Platform nextPlatform = gameplay.getPlatforms().get(row).get(nextColumn);
            if (nextPlatform == null) {
                return false;
            }
            if (nextPlatform.getPosition() == null) {
                return false;
            }
            GamePosition position = new GamePosition(character.getXHitBox() + speed, character.getYHitBox(),
                    character.getWidthHitBox(), character.getHeightHitBox());
            if (nextPlatform instanceof WallTile || nextPlatform instanceof Tile) {
                if (nextPlatform.checkValidPosition(position)) {
                    int aboveRow = row - 1;
                    Platform aboveNextPlatform = gameplay.getPlatforms().get(aboveRow).get(nextColumn);
                    if (aboveNextPlatform == null) {
                        return false;
                    }
                    if (aboveNextPlatform.getPosition() == null) {
                        return false;
                    }
                    if (aboveNextPlatform instanceof BlankTile) {
                        if (aboveNextPlatform.checkValidPosition(position)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }

    public void applyGravityCharacter(Character character) {
        if (character.getStandPlatform() != null) {
            if (!character.isAttack() && !character.isInAir()) {
                float dropSpeed = character.getStats().getDropSpeed();
                if (character.isWallSlide()) {
                    dropSpeed /= 3;
                } else if (character.isGrapEdge()) {
                    dropSpeed = 0;
                }
                if (character.getStats().getVely() < gameplay.GRAVITY) {
                    character.getStats().setVely(character.getStats().getVely() + dropSpeed);
                }
                float vely = character.getStats().getVely();
                GamePosition position = character.getPosition();
                double nY = vely + character.getYMaxHitBox();
                if (character.getStandPlatform().isCanStand()) {
                    if (nY < character.getStandPlatform().getPosition().getYPosition()) {
                        position.setYPosition((int) vely + position.getYPosition());
                        if (character instanceof Player) {
                            if (checkNextToBarrier(character)) {
                                character.setWallSlide(true);
                                if (character.isLTR()) {
                                    if (character.getCurrAnimation() instanceof PlayerWallAction_LTR); else {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.WALLSLIDE_LTR));
                                    }
                                    if (!character.isLTR()) {
                                        if (character.getCurrAnimation() instanceof PlayerFallDown_RTL); else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_RTL));
                                        }
                                    }
                                } else {
                                    if (character.getCurrAnimation() instanceof PlayerWallAction_RTL); else {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.WALLSLIDE_RTL));
                                    }
                                    if (character.isLTR()) {
                                        if (character.getCurrAnimation() instanceof PlayerFallDown_LTR); else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_LTR));
                                        }
                                    }
                                }
                            } else {
                                character.setWallSlide(false);
                                falldownMove(character);
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
                    } else {
                        if (character.getStandPlatform() != null) {
                            int distance = character.getStandPlatform().getPosition().getYPosition() - character.getYMaxHitBox();
                            distance = Math.abs(distance) - 10;
                            position.setYPosition(position.getYPosition() - distance);
                        }
                        if (character.isFallDown()) {
                            if (character.getCurrAnimation() != null) {
                                if (character instanceof Player) {
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
                                    } else if (character.isWallSlide()) {
                                        if (character.isLTR()) {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.IDLE_RTL));

                                        } else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.IDLE_LTR));
                                        }
                                        character.setWallSlide(false);
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
                        if (character instanceof Player) {
                            ((Player) character).setIsAirAttack(false);
                        }
                        character.getStats().setVely(0);
                        character.setFallDown(false);
                    }
                } else {
                    if (character.getStandPlatform() instanceof BlankTile) {
                        position.setYPosition((int) vely + position.getYPosition());
                        character.setFallDown(true);
                        if (character instanceof Player) {
                            if (checkNextToBarrier(character)) {
                                character.setWallSlide(true);
                                if (character.isLTR()) {
                                    if (character.getCurrAnimation() instanceof PlayerWallAction_LTR); else {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.WALLSLIDE_LTR));
                                    }
                                    if (!character.isLTR()) {
                                        if (character.getCurrAnimation() instanceof PlayerFallDown_RTL); else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_RTL));
                                        }
                                    }
                                } else {
                                    if (character.getCurrAnimation() instanceof PlayerWallAction_RTL); else {
                                        character.setCurrAnimation(character.getAnimations().get(CharacterState.WALLSLIDE_RTL));
                                    }
                                    if (character.isLTR()) {
                                        if (character.getCurrAnimation() instanceof PlayerFallDown_LTR); else {
                                            character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_LTR));
                                        }
                                    }
                                }
                            } else {
                                character.setWallSlide(false);
                                falldownMove(character);
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
        }
    }

    public void falldownMove(Character character) {
        if (character.getPosition().isMoveRight) {
            if (canMoveCheck(MoveState.RIGHT, character)) {
                if (character.isInAir()) {
                    return;
                }
                character.setCurrAnimation(character.getAnimations().get(CharacterState.FALLDOWN_LTR));
            }
        } else if (character.getPosition().isMoveLeft) {
            if (canMoveCheck(MoveState.LEFT, character)) {
                if (character.isInAir()) {
                    return;
                }
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

    // right tile return true mean: valid position
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

    // left tile return true mean: valid position
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
