package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;

public class EnemyIdle extends Animation{
    
    public EnemyIdle(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyIdle(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
}
