package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class EnemyAttack extends Animation{
    
    public EnemyAttack(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 25;
    }
    
}
