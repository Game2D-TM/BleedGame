package fightinggame.entity.ability.type.skill;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.Ability;

public abstract class PassiveSkill extends Ability{

    public PassiveSkill(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation animationLTR, Animation animationRTL, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, animationLTR, animationRTL, gameplay, character);
    }

    public PassiveSkill(int id, String name, long resetTime, int energyLost, SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(id, name, resetTime, energyLost, skillIcon, currAnimation, gameplay, character);
    }
    
}
