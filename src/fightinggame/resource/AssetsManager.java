package fightinggame.resource;

import fightinggame.entity.SpriteSheet;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class AssetsManager {

    public static List<BufferedImage> fireBallsLTR;
    public static List<BufferedImage> fireBallsRTL;
    public static Map<String, SpriteSheet> playerSpriteSheetMap;
    
    static {
        fireBallsLTR = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/LTR", 200, 365, 580, 200);
        fireBallsRTL = ImageManager.loadImagesWithCutFromFolderToList("assets/res/ability/Fire Ball/RTL", 30, 365, 580, 200);
        playerSpriteSheetMap = SpriteSheet.loadSpriteSheetFromFolder("assets/res/player");
    }
}
