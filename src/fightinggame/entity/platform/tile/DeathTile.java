package fightinggame.entity.platform.tile;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;

public abstract class DeathTile extends Platform {
    
    public DeathTile(String name, BufferedImage image, GamePosition position, Gameplay gameplay
        ,int row, int column) {
        super(name, image, false, position, gameplay, row, column);
    }
    
    @Override
    public boolean checkValidPosition(GamePosition position) {
        return false;
    }
}
