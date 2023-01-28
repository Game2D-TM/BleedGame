package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.skill.PassiveSkill;
import java.util.List;

public abstract class Benefit extends PassiveSkill {

    public Benefit(int id, String name, long resetTime, int energyLost,
            SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
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
