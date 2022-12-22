package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.Heal;
import java.util.List;

public class PotionHeal extends Heal{

    public PotionHeal(int healPoint, int id, String name, long resetTime, Animation currAnimation, GamePosition position, Gameplay gameplay, Character character) {
        super(healPoint, id, name, resetTime, currAnimation, position, gameplay, character);
    }

    @Override
    public boolean execute() {
        return super.healing();
    }

    @Override
    public boolean execute(Character character) {
        return super.healing(character);
    }

    @Override
    public boolean execute(List<Character> characters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
