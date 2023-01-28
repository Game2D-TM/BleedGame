package fightinggame.entity.ability.type.harm;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.ability.type.Harm;

public class ArmorPenetration extends Harm {

    public ArmorPenetration(int damage, int id, String name, Gameplay gameplay, Character character) {
        super(damage, id, name, null, gameplay, character);
    }

    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean execute(Character character) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
