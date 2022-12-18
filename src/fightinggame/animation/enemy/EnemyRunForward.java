package fightinggame.animation.enemy;

import fightinggame.entity.Animation;
import fightinggame.entity.CharacterState;
import fightinggame.resource.SpriteSheet;

public class EnemyRunForward extends Animation {

    public EnemyRunForward(int id, CharacterState state, SpriteSheet sheet) {
        super(id, state, sheet);
        tickToExecute = 40;
    }

}
