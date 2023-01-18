package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.Heal;
import java.awt.image.BufferedImage;

public class TimeHeal extends Heal {

    private long timeLimit;
    private long timeCounter = 0;
    private int timeUseCounter = 0;
    private int timeUseLimit;
    private boolean isUse;

    public TimeHeal(long timeLimit, int timeUseLimit, int healPoint, int id,
            long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation,
            GamePosition position, BufferedImage border,
            Gameplay gameplay, Character character) {
        super(healPoint, id, "Time Heal", resetTime, energyLost, skillIcon, currAnimation, position, border, gameplay, character);
        this.timeLimit = timeLimit;
        this.timeUseLimit = timeUseLimit;
        timeCounter = timeLimit;
        if (resetTime > timeLimit) {
            this.timeLimit = resetTime;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (isUse) {
            if (timeCounter > 0) {
                timeCounter--;
                if (timeUseCounter <= timeUseLimit) {
                    timeUseCounter++;
                } else {
                    if (character != null) {
                        healing(character);
                    }
                }
            } else {
                isUse = false;
            }
        }
    }

    @Override
    public boolean healing(Character character) {
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
            if (character instanceof Player) {
                character.getStats().useEnergy(energyLost);
            }
            character.getStats().setHealth(afterHeal);
            character.setHealingAmount(healPoint);
            timeUseCounter = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean execute() {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
            return execute(character);
        }
        return false;
    }

    @Override
    public boolean execute(Character character) {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
            if (canUse) {
                timeCounter = timeLimit;
                canUse = false;
                isUse = true;
                return true;
            }
        }
        return false;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getTimeUseLimit() {
        return timeUseLimit;
    }

    public void setTimeUseLimit(int timeUseLimit) {
        this.timeUseLimit = timeUseLimit;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setIsUse(boolean isUse) {
        this.isUse = isUse;
    }

}
