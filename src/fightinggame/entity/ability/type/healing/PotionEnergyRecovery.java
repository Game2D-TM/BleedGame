package fightinggame.entity.ability.type.healing;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Entity;
import fightinggame.entity.ability.type.EnergyRecovery;
import java.awt.image.BufferedImage;
import java.util.List;

public class PotionEnergyRecovery extends EnergyRecovery {
    
    public PotionEnergyRecovery(int point, int id, long resetTime, Entity skillIcon, Animation currAnimation, GamePosition position, BufferedImage border, Gameplay gameplay, Character character) {
        super(point, id, "Potion Energy Recovery", resetTime, 0, skillIcon, currAnimation, position, border, gameplay, character);
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
