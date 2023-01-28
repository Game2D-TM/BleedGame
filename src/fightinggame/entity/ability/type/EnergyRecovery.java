package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.skill.ActiveSkill;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class EnergyRecovery extends ActiveSkill {

    private int point;

    public EnergyRecovery(int point, int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, currAnimation, gameplay, character);
        this.point = point;
    }
    
    public EnergyRecovery(int point, int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation,
            GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(border, position, id, name, resetTime, energyLost, skillIcon, currAnimation, gameplay, character);
        this.point = point;
    }

    public boolean recovery(Character character) {
        if (canUse) {
            int maxEnergy = character.getStatusBar().getMaxEnergy();
            int energy = character.getStats().getEnergy();
            if (energy == maxEnergy) {
                return false;
            }
            int afterRecover = energy + point;
            if (afterRecover > maxEnergy) {
                afterRecover = maxEnergy;
            }
            if (afterRecover > energy) {
                if (character instanceof Player) {
                    character.getStats().useEnergy(energyLost);
                }
                character.getStats().addEnergy(point);
                character.addEnergyAmount(point);
                canUse = false;
                return true;
            }
        }
        return false;
    }

    public boolean recovery() {
        if (character == null) {
            return false;
        }
        return recovery(character);
    }

    @Override
    public boolean execute() {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
            return recovery();
        }
        return false;
    }

    @Override
    public boolean execute(Character character) {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
            return recovery(character);
        }
        return false;
    }

    @Override
    public boolean execute(List<Character> characters) {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
            return true;
        }
        return false;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
