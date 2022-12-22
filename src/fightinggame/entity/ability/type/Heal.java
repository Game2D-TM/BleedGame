package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.resource.SpriteSheet;
import java.awt.image.BufferedImage;

public abstract class Heal extends Ability {

    protected int healPoint;

    public Heal(int healPoint, int id, String name, long resetTime,
            SpriteSheet skillIcon, GamePosition position, Animation animationLTR, Animation animationRTL,
            Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animationLTR, animationRTL, gameplay, character);
        this.healPoint = healPoint;
    }

    public Heal(int healPoint, int id, String name, long resetTime, SpriteSheet skillIcon,
            GamePosition position, Animation animationLTR, Animation animationRTL,
            BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
        this.healPoint = healPoint;
    }

    public Heal(int healPoint, int id, String name, long resetTime, Animation currAnimation, GamePosition position, Gameplay gameplay, Character character) {
        super(id, name, resetTime, currAnimation, position, gameplay, character);
        this.healPoint = healPoint;
    }

    public boolean healing(Character character) {
        if (canUse) {
            int maxHealth = character.getHealthBar().getMaxHealth();
            int health = character.getHealthBar().getHealth();
            if (health == maxHealth) {
                return false;
            }
            int afterHeal = health + healPoint;
            if (afterHeal > maxHealth) {
                afterHeal = maxHealth;
            }
            if (afterHeal > health) {
                character.getHealthBar().setHealth(afterHeal);
                gameplay.getAudioPlayer().startThread("heal_sound", false, 0.8f);
                canUse = false;
                return true;
            }
        }
        return false;
    }

    public boolean healing() {
        if (character == null) {
            return false;
        }
        return healing(character);
    }

    public int getHealPoint() {
        return healPoint;
    }

    public void setHealPoint(int healPoint) {
        this.healPoint = healPoint;
    }
}
