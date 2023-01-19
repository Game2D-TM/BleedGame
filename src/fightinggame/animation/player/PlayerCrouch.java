package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerCrouch extends Animation{
    
    public PlayerCrouch(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerCrouch(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
    
}
