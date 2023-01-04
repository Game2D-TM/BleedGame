package fightinggame.entity.background;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import java.awt.image.BufferedImage;

public class ObjectNonTouchable extends GameObject{
    
    public ObjectNonTouchable(BufferedImage image, String name, GamePosition position, Gameplay gameplay) {
        super(image, name, position, gameplay);
    }
    
}
