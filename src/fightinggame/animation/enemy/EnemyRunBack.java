package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class EnemyRunBack extends Animation {

    public EnemyRunBack(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyRunBack(int id, Entity sheet) {
        super(id, sheet);
    }
    
    
}
