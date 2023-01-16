package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.SpriteSheet;

public class EnemyLightAttack extends Animation{
    
    public EnemyLightAttack(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public EnemyLightAttack(int id, SpriteSheet sheet) {
        super(id, sheet);
    }

}
