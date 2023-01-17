package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class EnemyHeavyAttack extends Animation{
    
    public EnemyHeavyAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyHeavyAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
