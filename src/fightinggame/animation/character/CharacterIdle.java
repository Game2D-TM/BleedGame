package fightinggame.animation.character;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public abstract class CharacterIdle extends Animation{
    
    public CharacterIdle(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public CharacterIdle(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
