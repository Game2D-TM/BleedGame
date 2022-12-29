package fightinggame.input.handler;

import fightinggame.Gameplay;
import fightinggame.entity.enemy.Enemy;

public class EnemyMovementHandler extends MovementHandler{
    
    private final Enemy enemy;
    
    public EnemyMovementHandler(String name, Gameplay gameplay, Enemy enemy) {
        super(gameplay, name);
        this.enemy = enemy;
    }

    @Override
    public void tick() {
        if(canMoveCheck(MoveState.LEFT, enemy)) {
         enemy.getPosition().isMoveLeft = false;
        }
        if(canMoveCheck(MoveState.RIGHT, enemy)) {
            enemy.getPosition().isMoveRight = false;
        }
        applyGravityCharacter(enemy);
    }
    
}
