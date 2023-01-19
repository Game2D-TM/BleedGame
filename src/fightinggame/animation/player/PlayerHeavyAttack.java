package fightinggame.animation.player;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class PlayerHeavyAttack extends Animation{
    
    public PlayerHeavyAttack(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public PlayerHeavyAttack(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
