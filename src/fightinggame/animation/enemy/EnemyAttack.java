package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class EnemyAttack extends Animation{
    
    public EnemyAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
