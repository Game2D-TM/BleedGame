package fightinggame.input.handler.game.player;

import fightinggame.Gameplay;
import fightinggame.entity.Player;
import fightinggame.input.handler.GameHandler;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayerMouseHandler extends GameHandler implements MouseListener {

    private final Player player;
    private final Gameplay gameplay;

    public PlayerMouseHandler(Player player, String name, Gameplay gameplay) {
        super(name);
        this.player = player;
        this.gameplay = gameplay;
    }

    @Override
    public void tick() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
