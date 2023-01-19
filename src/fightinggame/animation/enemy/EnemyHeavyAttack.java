package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class EnemyHeavyAttack extends Animation{
    
    public EnemyHeavyAttack(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyHeavyAttack(int id, Entity sheet) {
        super(id, sheet);
    }
    
}
