package fightinggame.entity.ability.type.increase;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.type.IncreaseStat;
import fightinggame.entity.SpriteSheet;
import java.util.List;

public class AttackIncrease extends IncreaseStat{

    public AttackIncrease(int id, String name, int amountIncrease, 
            SpriteSheet skillIcon, GamePosition position, Gameplay gameplay, Character character) {
        super(id, name, amountIncrease, skillIcon, position, gameplay, character);
    }

    @Override
    public boolean execute() {
        return execute(character);
    }

    @Override
    public boolean execute(Character character) {
        if(!isIncrease) {
            character.getStats().addAttackDamage((int)amountIncrease);
            isIncrease = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean execute(List<Character> characters) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
