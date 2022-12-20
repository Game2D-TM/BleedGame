package fightinggame.entity;

import fightinggame.resource.SpriteSheet;
import java.awt.Graphics;

public abstract class Animation {
    
    protected int id;
    protected SpriteSheet sheet;
    protected int tickToExecute = 10;
    
    public Animation(int id, SpriteSheet sheet, int tickToExecute) {
        this.id = id;
        this.sheet = sheet;
        this.tickToExecute = tickToExecute;
    }

    public Animation(int id, SpriteSheet sheet) {
        this.id = id;
        this.sheet = sheet;
    }
    
    public void tick() {
        sheet.tick(tickToExecute);
    }
    
    public void render(Graphics g, int x, int y, int width, int height) {
        sheet.render(g, x, y, width, height);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public SpriteSheet getSheet() {
        return sheet;
    }
    
    public void setSheet(SpriteSheet sheet) {
        this.sheet = sheet;
    }

    public int getTickToExecute() {
        return tickToExecute;
    }

    public void setTickToExecute(int tickToExecute) {
        this.tickToExecute = tickToExecute;
    }

    
    
}
