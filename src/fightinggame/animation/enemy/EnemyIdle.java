package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class EnemyIdle extends Animation{
    
    public EnemyIdle(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
    }
    
}
