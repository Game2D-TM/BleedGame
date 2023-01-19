package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;


public class EnemyHit extends Animation{
    
    public EnemyHit(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyHit(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
