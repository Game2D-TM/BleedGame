package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerKnockDown extends Animation{
    
    public PlayerKnockDown(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerKnockDown(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
