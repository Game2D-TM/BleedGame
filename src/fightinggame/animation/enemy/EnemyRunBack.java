package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class EnemyRunBack extends Animation {

    public EnemyRunBack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyRunBack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }
    
    
}
