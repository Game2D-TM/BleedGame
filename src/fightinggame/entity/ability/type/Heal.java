package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.SpriteSheet;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Heal extends Ability {

    protected int healPoint;

    public Heal(int healPoint, int id, String name, long resetTime, SpriteSheet skillIcon, Animation currAnimation,
             GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, skillIcon, currAnimation, position, border, gameplay, character);
        this.healPoint = healPoint;
    }

    public boolean healing(Character character) {
        if (canUse) {
            int maxHealth = character.getHealthBar().getMaxHealth();
            int health = character.getStats().getHealth();
            if (health == maxHealth) {
                return false;
            }
            int afterHeal = health + healPoint;
            if (afterHeal > maxHealth) {
                afterHeal = maxHealth;
            }
            if (afterHeal > health) {
                character.getStats().setHealth(afterHeal);
                character.setHealingAmount(healPoint);
                gameplay.getAudioPlayer().startThread("heal_sound", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
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

    @Override
    public boolean execute() {
        return healing();
    }

    @Override
    public boolean execute(Character character) {
        return healing(character);
    }
    
    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    public int getHealPoint() {
        return healPoint;
    }

    public void setHealPoint(int healPoint) {
        this.healPoint = healPoint;
    }
}
