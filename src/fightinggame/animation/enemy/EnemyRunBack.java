package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class EnemyRunBack extends Animation {

    public EnemyRunBack(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 40;
    }
}
