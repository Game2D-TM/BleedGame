package fightinggame.entity.ability.type.recovery;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.EnergyRecovery;
import java.util.List;

public class PotionEnergyRecovery extends EnergyRecovery {
    
    public PotionEnergyRecovery(int point, int id, long resetTime, SpriteSheet skillIcon, Animation currAnimation, Gameplay gameplay, Character character) {
        super(point, id, "Energy Recovery", resetTime, 0, skillIcon, currAnimation, gameplay, character);
    }

    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }

    @Override
    public boolean execute(Character character) {
        return recovery(character);
    }

    @Override
    public boolean execute() {
        return recovery();
    }
    
    
    
}
