package fightinggame.animation.trap;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class TrapAnimation extends Animation{
    
    public TrapAnimation(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public TrapAnimation(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
