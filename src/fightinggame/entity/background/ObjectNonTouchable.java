package fightinggame.entity.background;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;

public class ObjectNonTouchable extends GameObject{
    
    public ObjectNonTouchable(BufferedImage image, String name, Platform platform, GamePosition position, Gameplay gameplay) {
        super(image, name, platform, position, gameplay);
    }
    
}
