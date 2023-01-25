package fightinggame.entity;

import fightinggame.entity.state.CharacterState;
import fightinggame.Gameplay;
import fightinggame.animation.effect.Effect;
import fightinggame.animation.effect.player.PlayerHitEffect;
import fightinggame.entity.ability.Ability;
import fightinggame.animation.player.*;
import fightinggame.entity.enemy.Enemy;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player extends Character {

    private int isStunCounter = 0;
    private int stunTime = 50;
    private int score = 0;
    private boolean isAirAttack;
    private boolean isSpecialAttack;
    private boolean isDoubleJump;
    private boolean isUseItem;
    private Dialogue dialogue;
    private boolean isSpeak;
    private int speakCounter = 0;
    private int speakDialogueIndex = 1;
    private final List<Enemy> enemiesKilled = new ArrayList<>();
    private Effect hitEffect;

    public Player(int id, String name, int health, GamePosition position,
            Map<CharacterState, Animation> animations,
            Gameplay gameplay, SpriteSheet inventorySheet) {
        super(id, name, health, position, animations, gameplay, true, inventorySheet);
        avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/player.png"));
        healthBarInit(health);
        healthBar.setOvalImage(new java.awt.geom.Ellipse2D.Float(25f, 10f, 100, 100));
        stats.setSpeed(2);
        stats.setHealth(health);
        stats.addAttackDamage(40);
        stats.setJumpSpeed(300);
        stats.setAttackRange(20);
        healthBar.getPositions().put("player_score",
                new GamePosition(healthBar.getNamePos().getXPosition(),
                        healthBar.getNamePos().getYPosition() + 30, 0, 0));
        dialogue = new Dialogue(this, gameplay);
        SpriteSheet exploreFireBallSheet = new SpriteSheet(ImageManager.loadImage("assets/res/effect/Mini_Effect_2D/Effect9.png"),
                0, 0, 18, 18,
                0, 0, 18, 18, 4);
        hitEffect = new PlayerHitEffect(0, exploreFireBallSheet, stunTime);
    }

    @Override
    protected void healthBarInit(int maxHealth) {
        SpriteSheet healthBarSheet = new SpriteSheet();
        SpriteSheet energyBarSheet = new SpriteSheet();
//        healthBarSheet.setImages(ImageManager.loadImagesWithCutFromFolderToList("assets/res/healthbar",
//                1, 2, 126, 12));
        healthBarSheet.setImages(ImageManager.loadImagesFromFoldersToList("assets/res/status-bar/HealthBar"));
        energyBarSheet.setImages(ImageManager.loadImagesFromFoldersToList("assets/res/status-bar/EnergyBar"));
        healthBar = new StatusBar(avatar, healthBarSheet, energyBarSheet, this,
                new GamePosition(120, 20, 550, 80), new GamePosition(15, 8, 100, 110),
                maxHealth, 100);
    }

    @Override
    public void tick() {
        super.tick();
        healthBar.tick();
        if (isDeath) {
            if (isLTR) {
                if (currAnimation instanceof PlayerDeath); else {
                    currAnimation = animations.get(CharacterState.DEATH_LTR);
                }
            } else {
                if (currAnimation instanceof PlayerDeath); else {
                    currAnimation = animations.get(CharacterState.DEATH_RTL);
                }
            }
        }
        if (isAttacked && !isDeath) {
            if (hitEffect != null) {
                hitEffect.tick();
            }
            isStunCounter++;
            if (isStunCounter > stunTime) {
                isAttacked = false;
                isStunCounter = 0;
                hitEffect.resetEffectCounter();
                if (isLTR) {
                    currAnimation = animations.get(CharacterState.IDLE_LTR);
                } else {
                    currAnimation = animations.get(CharacterState.IDLE_RTL);
                }
                if (stunTime > 50) {
                    stunTime = 50;
                }
            }
        }
        if (isSpeak) {
            if (Enemy.ENEMY_HEALTHBAR_SHOW != null) {
                Enemy enemy = Enemy.ENEMY_HEALTHBAR_SHOW;
                if (enemy.isSpeak()) {
                    enemy.setIsSpeak(false);
                }
            }
            if (dialogue != null) {
                dialogue.tick();
            }
            if (dialogue.isEndDialogue()) {
                isSpeak = false;
            }
        } else {
            if (speakCounter <= 1500) {
                speakCounter++;
            }
            if (speakCounter > 1500) {
                boolean result = dialogue.loadDialogue(DataManager.getFile("starter_" + speakDialogueIndex));
                if (result) {
                    dialogue.setEndDialogue(false);
                    isSpeak = true;
                    speakCounter = 0;
                    speakDialogueIndex++;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (!gameplay.isHideGUI()) {
            healthBar.render(g);
            if (isSpeak) {
                if (dialogue != null) {
                    dialogue.render(g);
                }
            }
        }
        if (hitEffect != null) {
            if (hitEffect.isActive()) {
                hitEffect.render(g, getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
                        getYHitBox() + getHeightHitBox() / 3 - gameplay.getCamera().getPosition().getYPosition(),
                        getWidthHitBox(), getHeightHitBox() / 3);
            }
        }
//        g.setColor(Color.red);
        // hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        //attackhitbox
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition()
//                    , attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition()
//                    , attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    @Override
    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerLightAttack) {
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

    @Override
    public int getWidthHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerRun_LTR) {
                return position.getWidth() / 3 - 5;
            } else if (currAnimation instanceof PlayerRun_RTL) {
                return position.getWidth() / 3 - 3;
            } else if (currAnimation instanceof PlayerLightAttack) {
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

    @Override
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

    @Override
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

    @Override
    public boolean checkHit(GamePosition attackHitBox, boolean isAttack,
            Character character, int attackDamage) {
        if (!isAttack && !isDeath) {
            if (((attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox())
                    || (attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox()
                    && attackHitBox.getMaxX() > getXMaxHitBox())
                    || (attackHitBox.getMaxX() >= getXHitBox() && attackHitBox.getMaxX() <= getXMaxHitBox()
                    && attackHitBox.getXPosition() < getXHitBox())
                    || (attackHitBox.getXPosition() < getXHitBox() && attackHitBox.getMaxX() > getXMaxHitBox()))
                    && ((attackHitBox.getYPosition() <= getYHitBox() && attackHitBox.getYPosition() >= getYMaxHitBox()
                    || (attackHitBox.getYPosition() >= getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox())
                    || (attackHitBox.getYPosition() > getYHitBox() && attackHitBox.getYPosition() <= getYMaxHitBox()
                    && attackHitBox.getMaxY() > getYMaxHitBox())
                    || (attackHitBox.getMaxY() > getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox()
                    && attackHitBox.getYPosition() < getYHitBox())))) {
                if (this.isAttack) {
                    return false;
                }
                return true;
            }
        } else {
            if (isAttack && !isAttacked && !isDeath) {
                if (((attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox())
                        || (attackHitBox.getXPosition() >= getXHitBox() && attackHitBox.getXPosition() <= getXMaxHitBox()
                        && attackHitBox.getMaxX() > getXMaxHitBox())
                        || (attackHitBox.getMaxX() >= getXHitBox() && attackHitBox.getMaxX() <= getXMaxHitBox()
                        && attackHitBox.getXPosition() < getXHitBox())
                        || (attackHitBox.getXPosition() < getXHitBox() && attackHitBox.getMaxX() > getXMaxHitBox()))
                        && ((attackHitBox.getYPosition() <= getYHitBox() && attackHitBox.getYPosition() >= getYMaxHitBox()
                        || (attackHitBox.getYPosition() >= getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox())
                        || (attackHitBox.getYPosition() > getYHitBox() && attackHitBox.getYPosition() <= getYMaxHitBox()
                        && attackHitBox.getMaxY() > getYMaxHitBox())
                        || (attackHitBox.getMaxY() > getYHitBox() && attackHitBox.getMaxY() <= getYMaxHitBox()
                        && attackHitBox.getYPosition() < getYHitBox())))) {
                    isAttacked = true;
                    hitEffect.setActive(true);
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
                        if (hitEffect != null) {
                            hitEffect.resetEffectCounter();
                        }
                    } else {
                        if (character.getStats().getBounceRange() < 50) {
                            if (character.isLTR()) {
                                currAnimation = animations.get(CharacterState.GET_HIT_RTL);
                                if (!isSlide()) {
                                    isLTR = false;
                                }
                            } else {
                                currAnimation = animations.get(CharacterState.GET_HIT_LTR);
                                if (!isSlide()) {
                                    isLTR = true;
                                }
                            }
                        } else {
                            if (character.isLTR()) {
                                currAnimation = animations.get(CharacterState.KNOCKDOWN_RTL);
                                if (!isSlide()) {
                                    isLTR = false;
                                }
                            } else {
                                currAnimation = animations.get(CharacterState.KNOCKDOWN_LTR);
                                if (!isSlide()) {
                                    isLTR = true;
                                }
                            }
                            stunTime = 150;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void resetEnemiesKilled() {
        if (enemiesKilled.size() > 0) {
            enemiesKilled.clear();
        }
    }

    public List<Enemy> getEnemiesKilled() {
        return enemiesKilled;
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setIsSpeak(boolean isSpeak) {
        this.isSpeak = isSpeak;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public boolean isSlide() {
        return position.isSlide;
    }

    public boolean isSprint() {
        return (position.isMoveRight || position.isMoveLeft) && position.isSprint;
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

    public void setScore(int score) {
        this.score = score;
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

    public boolean isDoubleJump() {
        return isDoubleJump;
    }

    public void setIsDoubleJump(boolean isDoubleJump) {
        this.isDoubleJump = isDoubleJump;
    }

    public GamePosition getPlayerScorePos() {
        return healthBar.getPositions().get("player_score");
    }

    @Override
    public GamePosition attackHitBox() {
        int width;
        int x = position.getXPosition(), y;
        int attackX = 0, attackY, attackWidth, attackHeight;
        if (isLTR) {
            width = position.getWidth() + stats.getAttackRange(); // 120
        } else {
            x = position.getXPosition() - stats.getAttackRange();
            width = position.getWidth() + stats.getAttackRange();
        }
        if (isLTR) {
            attackX = (x + width - 2) - (width / 3);
        } else {
            attackX = x + 24;
        }
        attackWidth = width / 3 - 22;
        attackY = position.getYPosition() + position.getHeight() / 3 - 10;
        attackHeight = position.getHeight() / 2 - 10;
        if (currAnimation != null) {
            if (currAnimation instanceof PlayerHeavyAttack) {
                if (isLTR) {
                    attackX -= 16;
                } else {
                    attackX += 16;
                }
            } else if (currAnimation instanceof PlayerAirAttack_LTR) {
                attackX -= 12;
                attackHeight -= 10;
            } else if (currAnimation instanceof PlayerAirAttack_RTL) {
                attackX += 12;
                attackHeight -= 10;
            }
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

    public int getStunTime() {
        return stunTime;
    }

    public void setStunTime(int stunTime) {
        this.stunTime = stunTime;
    }

    public Effect getHitEffect() {
        return hitEffect;
    }

    public boolean isUseItem() {
        return isUseItem;
    }

    public void setIsUseItem(boolean isUseItem) {
        this.isUseItem = isUseItem;
    }
}
