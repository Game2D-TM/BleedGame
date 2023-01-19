package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.entity.Entity;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class Benefit extends Ability {

    public Benefit(int id, String name, long resetTime, int energyLost,
            Entity skillIcon, GamePosition position,
             Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, gameplay, character);
    }

    public Benefit(int id, String name, long resetTime, int energyLost, Entity skillIcon,
            GamePosition position, Animation animationLTR, Animation animationRTL,
             BufferedImage border, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, position, animationLTR, animationRTL, border, gameplay, character);
    }

    @Override
    public boolean execute(List<Character> characters) {
        if (this.character == null) {
            return false;
        }
        if (character.getStats().haveEnergy()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean execute(Character character) {
        if (this.character == null) {
            return false;
        }
        if (this.character.getStats().haveEnergy()) {
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
            return true;
        }
        return false;
    }

}
