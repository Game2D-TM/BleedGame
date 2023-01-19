package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;


public class EnemyHit extends Animation{
    
    public EnemyHit(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyHit(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
