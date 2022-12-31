package fightinggame.entity;

import fightinggame.Gameplay;
import fightinggame.entity.ability.Ability;
import fightinggame.resource.SpriteSheet;
import fightinggame.animation.player.*;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class Player extends Character {

    private int isStunCounter = 0;
    private int score = 0;
    private boolean isAirAttack;
    private boolean isSpecialAttack;

    public Player(int id, String name, int health, GamePosition position,
            Map<CharacterState, Animation> animations, Map<String, BufferedImage> characterAssets,
            Gameplay gameplay, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, characterAssets, gameplay, true, inventorySheet);
        avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/avatar.png"));
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(25f, 10f, 100, 100));
        stats.setSpeed(2);
        stats.setHealth(health);
        stats.setAttackDamage(50);
        stats.setCritChange(0.5f);
        stats.setCritDamage(10);
        stats.setJumpSpeed(300);
        healthBar.getPositions().put("player_score",
                new GamePosition(healthBar.getNamePos().getXPosition(),
                        healthBar.getNamePos().getYPosition() + 30, 0, 0));
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        SpriteSheet healthBarSheet = new SpriteSheet();
        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
                1, 2, 126, 12));
        healthBar = new HealthBar(avatar, healthBarSheet, this,
                new GamePosition(120, 20, 550, 80), new GamePosition(15, 8, 100, 110),
                maxHealth);
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (!isDeath && isSpecialAttack) {
            if (currAnimation != null) {
                if (currAnimation instanceof PlayerAttackSpecial_LTR) {
                    if (currAnimation.getSheet().getSpriteIndex() == currAnimation.getSheet().getImages().size() - 1) {
                        isSpecialAttack = false;
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                    }
                } else if (currAnimation instanceof PlayerAttackSpecial_RTL) {
                    if (currAnimation.getSheet().getSpriteIndex() == currAnimation.getSheet().getImages().size() - 1) {
                        isSpecialAttack = false;
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                    }
                }
            }
        }
        if (isAttacked && !isDeath) {
            isStunCounter++;
            if (isStunCounter > 50) {
                isAttacked = false;
                isStunCounter = 0;
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.IDLE_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.IDLE_RTL);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        healthBar.render(g);
        g.drawString("Score: " + score, getPlayerScorePos().getXPosition(),
                getPlayerScorePos().getYPosition());
        g.setColor(Color.red);
        // hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        //attackhitbox
//        int attackX;
//        if (isLTR) {
//            attackX = position.getMaxX() - 12;
//        } else {
//            attackX = position.getXPosition() - 20 + 12;
//        }
//        int attackY = position.getYPosition() + position.getHeight() / 3 - 10;
//        int attackHeight = position.getHeight() / 2 - 50;
//        g.fillRect(attackX - gameplay.getCamera().getPosition().getXPosition(),
//                attackY - gameplay.getCamera().getPosition().getYPosition(), 20, attackHeight);
    }

    @Override
    public void healthBarTick() {

    }

    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerAttack) {
                return position.getXPosition() + 110;
            } else if (currAnimation instanceof PlayerRun_LTR) {
                return position.getXPosition() + 140;
            } else if (currAnimation instanceof PlayerRun_RTL) {
                return position.getXPosition() + 95;
            } else if (currAnimation instanceof PlayerJump_LTR) {
                return position.getXPosition() + 152;
            } else if (currAnimation instanceof PlayerJump_RTL) {
                return position.getXPosition() + 102;
            } else if (currAnimation instanceof PlayerFallDown_LTR) {
                return position.getXPosition() + 162;
            } else if (currAnimation instanceof PlayerFallDown_RTL) {
                return position.getXPosition() + 108;
            } else if (currAnimation instanceof PlayerSlide_LTR) {
                return position.getXPosition() + 105;
            } else if (currAnimation instanceof PlayerSlide_RTL) {
                return position.getXPosition() + 75;
            }
        }
        return position.getXPosition() + 115;
    }

    public int getWidthHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerRun_LTR) {
                return position.getWidth() / 3 - 5;
            } else if (currAnimation instanceof PlayerRun_RTL) {
                return position.getWidth() / 3 - 3;
            } else if (currAnimation instanceof PlayerAttack) {
                return position.getWidth() / 3 + 30;
            } else if (currAnimation instanceof PlayerCrouch) {
                return position.getWidth() / 3 + 10;
            } else if (currAnimation instanceof PlayerJump_LTR) {
                return position.getWidth() / 3 - 25;
            } else if (currAnimation instanceof PlayerJump_RTL) {
                return position.getWidth() / 3 - 25;
            } else if (currAnimation instanceof PlayerFallDown_LTR) {
                return position.getWidth() / 3 - 35;
            } else if (currAnimation instanceof PlayerFallDown_RTL) {
                return position.getWidth() / 3 - 35;
            } else if (currAnimation instanceof PlayerSlide_LTR) {
                return position.getWidth() / 3 + 55;
            } else if (currAnimation instanceof PlayerSlide_RTL) {
                return position.getWidth() / 3 + 55;
            }
        }
        return position.getWidth() / 3;
    }

    public int getHeightHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerCrouch) {
                return position.getHeight() - 105;
            } else if (currAnimation instanceof PlayerJump_LTR) {
                return position.getHeight() - 100;
            } else if (currAnimation instanceof PlayerJump_RTL) {
                return position.getHeight() - 100;
            } else if (currAnimation instanceof PlayerFallDown_LTR) {
                return position.getHeight() - 75;
            } else if (currAnimation instanceof PlayerFallDown_RTL) {
                return position.getHeight() - 75;
            } else if (currAnimation instanceof PlayerSlide_LTR) {
                return position.getHeight() / 3 + 20;
            } else if (currAnimation instanceof PlayerSlide_RTL) {
                return position.getHeight() / 3 + 20;
            }
        }
        return position.getHeight() - 55;
    }

    public int getXMaxHitBox() {
        return getXHitBox() + getWidthHitBox();
    }

    public int getYHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerCrouch) {
                return position.getYPosition() + position.getHeight() / 3 + 15;
            } else if (currAnimation instanceof PlayerJump_LTR) {
                return position.getYPosition() + position.getHeight() / 3 - 46;
            } else if (currAnimation instanceof PlayerJump_RTL) {
                return position.getYPosition() + position.getHeight() / 3 - 46;
            } else if (currAnimation instanceof PlayerFallDown_LTR) {
                return position.getYPosition() + position.getHeight() / 3 - 46;
            } else if (currAnimation instanceof PlayerFallDown_RTL) {
                return position.getYPosition() + position.getHeight() / 3 - 46;
            } else if (currAnimation instanceof PlayerSlide_LTR) {
                return position.getYPosition() + position.getHeight() / 2 + 20;
            } else if (currAnimation instanceof PlayerSlide_RTL) {
                return position.getYPosition() + position.getHeight() / 2 + 20;
            }
        }
        return position.getYPosition() + position.getHeight() / 3 - 35;
    }

    public int getYMaxHitBox() {
        return getYHitBox() + getHeightHitBox();
    }

    @Override
    public boolean checkHit(int attackX, int attackY, int attackHeight, boolean isAttack,
            Character character, int attackDamage) {
        int attackMaxY = attackY + attackHeight;
        if (!isAttack && !isDeath) {
            if (attackX >= getXHitBox() && attackX <= getXMaxHitBox()
                    && ((attackY <= getYHitBox() && attackMaxY >= getYMaxHitBox()
                    || (attackY >= getYHitBox() && attackMaxY <= getYMaxHitBox())
                    || (attackY > getYHitBox() && attackY <= getYMaxHitBox()
                    && attackMaxY > getYMaxHitBox())
                    || (attackMaxY > getYHitBox() && attackMaxY <= getYMaxHitBox()
                    && attackY < getYHitBox())))) {
                if (this.isAttack) {
                    return false;
                }
                return true;
            }
        } else {
            if (isAttack && !isAttacked && !isDeath) {
                if (attackX >= getXHitBox() && attackX <= getXMaxHitBox()
                        && ((attackY <= getYHitBox() && attackMaxY >= getYMaxHitBox()
                        || (attackY >= getYHitBox() && attackMaxY <= getYMaxHitBox())
                        || (attackY > getYHitBox() && attackY <= getYMaxHitBox()
                        && attackMaxY > getYMaxHitBox())
                        || (attackMaxY > getYHitBox() && attackMaxY <= getYMaxHitBox()
                        && attackY < getYHitBox())))) {
                    if (isLTR) {
                        currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                    } else {
                        currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                    }
                    isAttacked = true;
                    if (attackDamage == -1) {
                        receiveDamage = stats.getHit(character.getStats());
                    } else {
                        receiveDamage = stats.getHit(character.getStats(), attackDamage);
                    }
                    if (character.isLTR) {
                        position.setXPosition(position.getXPosition() + character.getStats().getBounceRange());
                    } else {
                        position.setXPosition(position.getXPosition() - character.getStats().getBounceRange());
                    }
                    if (stats.getHealth() <= 0) {
                        isDeath = true;
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.DEATH_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.DEATH_RTL);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSpecialAttack() {
        return isSpecialAttack;
    }

    public void setIsSpecialAttack(boolean isSpecialAttack) {
        this.isSpecialAttack = isSpecialAttack;
    }

    public boolean isAirAttack() {
        return isAirAttack;
    }

    public void setIsAirAttack(boolean isAirAttack) {
        this.isAirAttack = isAirAttack;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int point) {
        this.score += point;
    }

    public Ability getAbility(int index) {
        if (abilities.size() == 0) {
            return null;
        }
        if (index > abilities.size() - 1) {
            return null;
        }
        return abilities.get(index);
    }

    public GamePosition getPlayerScorePos() {
        return healthBar.getPositions().get("player_score");
    }
}
