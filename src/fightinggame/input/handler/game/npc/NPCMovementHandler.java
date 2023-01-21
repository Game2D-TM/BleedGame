package fightinggame.input.handler.game.npc;

import fightinggame.Gameplay;
import fightinggame.entity.npc.NPC;
import fightinggame.entity.state.MoveState;
import fightinggame.input.handler.game.MovementHandler;

public class NPCMovementHandler extends MovementHandler {

    private final NPC npc;

    public NPCMovementHandler(NPC npc, Gameplay gameplay, String name) {
        super(gameplay, name);
        this.npc = npc;
    }

    @Override
    public void tick() {
        if (canMoveCheck(MoveState.LEFT, npc)) {
            npc.getPosition().isMoveLeft = false;
        }
        if (canMoveCheck(MoveState.RIGHT, npc)) {
            npc.getPosition().isMoveRight = false;
        }
        applyGravityCharacter(npc);
    }

}
