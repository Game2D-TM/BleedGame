package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.Entity;

public class EnemyLightAttack extends Animation{
    
    public EnemyLightAttack(int id, Entity sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyLightAttack(int id, Entity sheet) {
        super(id, sheet);
    }

}
