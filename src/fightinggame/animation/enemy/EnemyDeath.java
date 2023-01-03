package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class EnemyDeath extends Animation{
    
    public EnemyDeath(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyDeath(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
