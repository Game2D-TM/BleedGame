package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerRun_RTL extends Animation{
    
    public PlayerRun_RTL(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }
    
    public PlayerRun_RTL(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
