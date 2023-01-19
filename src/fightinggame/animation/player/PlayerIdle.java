package fightinggame.animation.player;

import fightinggame.animation.character.CharacterIdle;
import fightinggame.entity.Entity;

public class PlayerIdle extends CharacterIdle{
    
    public PlayerIdle(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerIdle(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
