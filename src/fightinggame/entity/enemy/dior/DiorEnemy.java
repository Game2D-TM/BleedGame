package fightinggame.entity.enemy.dior;

import fightinggame.Gameplay;
import fightinggame.animation.enemy.EnemyRunBack;
import fightinggame.entity.Animation;
import fightinggame.entity.Dialogue;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.throwable.Fireball;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.Graphics;
import java.io.File;
import java.util.Map;

public class DiorEnemy extends Enemy {

    private DiorColor color;

    public DiorEnemy(DiorColor color, int id, String name, int health,
            GamePosition position, Map<CharacterState, Animation> animations,
            Gameplay gameplay, int rangeRandomSpeed) {
        super(id, name, health, position, animations, gameplay, rangeRandomSpeed);
        this.color = color;
        attackLimit = 100;
        dialogueFile = DataManager.getFile("dior_dialogue");
        avatar = ImageManager.loadImage(new File("assets/res/gui/avatar/dior_" + color.toString().toLowerCase() + ".png"));
        switch (color) {
            case Red:
                experience = 100;
                stats.setAttackDamage(stats.getAttackDamage() + 20);
                stats.setSpeed(stats.getSpeed() - 20);
                stats.setHealth(stats.getHealth() - 150);
                break;
            case Blue:
                experience = 90;
                stats.setAttackDamage(stats.getAttackDamage() + 5);
                stats.setSpeed(stats.getSpeed() + 10);
                stats.setHealth(stats.getHealth() + 50);
                break;
            case Green:
                experience = 70;
                stats.setAttackDamage(stats.getAttackDamage() + 10);
                stats.setSpeed(stats.getSpeed() - 10);
                stats.setHealth(stats.getHealth() - 50);
                break;
            case Orange:
                experience = 60;
                stats.setAttackDamage(stats.getAttackDamage() - 5);
                stats.setSpeed(stats.getSpeed() + 20);
                stats.setHealth(stats.getHealth() + 100);
                break;
            case Purple:
                experience = 50;
                stats.setSpeed(stats.getSpeed() - 5);
                stats.setHealth(stats.getHealth() + 200);
                break;
            case White:
                experience = 80;
                stats.setAttackDamage(stats.getAttackDamage() - 8);
                stats.setSpeed(stats.getSpeed() - 5);
                stats.setHealth(stats.getHealth() + 400);
                break;
            case Yellow:
                experience = 80;
                stats.setAttackDamage(stats.getAttackDamage() - 5);
                stats.setSpeed(stats.getSpeed() + 30);
                stats.setHealth(stats.getHealth() + 50);
                break;
        }
        healthBar.setMaxHealth(stats.getHealth());
        if (stats.getSpeed() <= 30) {
            stats.setSpeed(30);
        }
        dialogue = new Dialogue(this, gameplay);
    }

    @Override
    public void tick() {
        super.tick();
        if (color == DiorColor.Red) {
            if (!isAttack && !isDeath) {
                if (checkPlayerOnSight()) {
                    Fireball fireBallAbility = ((Fireball) abilities.get(0));
                    if (fireBallAbility.isCanUse()) {
                        int spawnX;
                        int xChange;
                        int skillDistance = 1400;
                        GamePosition endPos;
                        if (!isLTR) {
                            xChange = 215;
                            spawnX = position.getXPosition() - xChange;
                            endPos = new GamePosition(
                                    position.getXPosition() - skillDistance - xChange, 0,
                                    position.getMaxX() + skillDistance, 0);
                        } else {
                            xChange = 15;
                            spawnX = position.getMaxX() + xChange;
                            endPos = new GamePosition(
                                    position.getXPosition() - skillDistance,
                                    0, position.getMaxX() + skillDistance + xChange, 0);
                        }
                        GamePosition fireBallPos = new GamePosition(spawnX,
                                position.getYPosition() + position.getHeight() / 2 - 10, 200, 100);
                        boolean result = fireBallAbility.execute(fireBallPos, endPos);
                        if (result) {
                            if (isLTR) {
                                currAnimation = animations.get(CharacterState.ATTACK01_LTR);
                            } else {
                                currAnimation = animations.get(CharacterState.ATTACK01_RTL);
                            }
                            isAttack = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isSpeak) {
            if (dialogue != null) {
                dialogue.render(g);
            }
        }
        //hitbox
//        g.setColor(Color.red);
//        g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                 getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                getWidthHitBox(), getHeightHitBox());
        // attack hitbox
//        g.fillRect(attackHitBox().getXPosition() - gameplay.getCamera().getPosition().getXPosition()
//                , attackHitBox().getYPosition() - gameplay.getCamera().getPosition().getYPosition()
//                , attackHitBox().getWidth(), attackHitBox().getHeight());
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setIsSpeak(boolean isSpeak) {
        speakTimeDialogueCounter = 0;
        this.isSpeak = isSpeak;
    }

    @Override
    public int getXHitBox() {
        if (currAnimation != null) {
            if (currAnimation instanceof EnemyRunBack) {
                return position.getXPosition() + 30;
            }
        }
        return position.getXPosition();
    }

    @Override
    public int getXMaxHitBox() {
        return position.getMaxX();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    @Override
    public int getYMaxHitBox() {
        return position.getMaxY();
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 30;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    public DiorColor getColor() {
        return color;
    }

    public void setColor(DiorColor color) {
        this.color = color;
    }

    @Override
    public GamePosition attackHitBox() {
        int attackX, attackY, attackWidth, attackHeight;
        attackY = position.getYPosition() + position.getHeight() / 3 - 10;
        attackHeight = position.getHeight() / 2 - 10;
        attackWidth = position.getWidth() / 3;
        if (!isLTR) {
            attackX = position.getXPosition() - 30;
        } else {
            attackX = (position.getMaxX() + 30) - attackWidth;
        }
        return new GamePosition(attackX, attackY, attackWidth, attackHeight);
    }

}
