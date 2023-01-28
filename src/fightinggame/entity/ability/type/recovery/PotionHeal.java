package fightinggame.entity.ability.type.recovery;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.Heal;
import java.util.List;

public class PotionHeal extends Heal{

    public PotionHeal(int healPoint, int id, long resetTime
            , SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(healPoint, id, "Heal", resetTime, 0, skillIcon, currAnimation, gameplay, character);
    }
    
    
    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    @Override
    public boolean execute(Character character) {
        return healing(character);
    }

    @Override
    public boolean execute() {
        return healing();
    }
}
