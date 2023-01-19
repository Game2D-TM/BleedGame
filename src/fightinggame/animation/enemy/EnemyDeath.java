package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class EnemyDeath extends Animation{
    
    public EnemyDeath(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyDeath(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
