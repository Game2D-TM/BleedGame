package fightinggame.animation.item;

import fightinggame.entity.Animation;
import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;

public class SlotAnimation extends Animation{
    
    public SlotAnimation(int id, SpriteSheet sheet, int tickToExecute) {
        super(id, sheet, tickToExecute);
    }

    public SlotAnimation(int id, SpriteSheet sheet) {
        super(id, sheet);
    }

    
    @Override
    public void tick() {
       
    }
    
    @Override
    public void render(Graphics g, int x, int y, int width, int height) {
        if(sheet != null) {
            if(sheet.getImages().size() > 0) {
                for(int i = 0 ; i < sheet.getImages().size(); i++) {
                    g.drawImage(sheet.getImage(i), x, y, width, height, null);
                }
            }
        }
    }
    
    
    
}
