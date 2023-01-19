package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;


public class PlayerHit extends Animation{
    
    public PlayerHit(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerHit(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
    
}
