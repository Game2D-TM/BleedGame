package fightinggame.entity;

import fightinggame.resource.SpriteSheet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public abstract class Animation {
    
    protected int id;
    protected CharacterState state;
    protected SpriteSheet sheet;
    protected int spriteCounter = 0;
    protected int spriteIndex = 0;
    protected int tickToExecute = 10;
    
    public Animation(int id, CharacterState state, SpriteSheet sheet) {
        this.id = id;
        this.state = state;
        this.sheet = sheet;
    }
    
    public void tick() {
        spriteCounter++;
        if (spriteCounter > tickToExecute) {
            if (spriteIndex < sheet.getImages().size() - 1) {
                spriteIndex++;
            } else {
                spriteIndex = 0;
            }
            spriteCounter = 0;
        }
    }
    
    public void render(Graphics g, int x, int y, int width, int height) {
        Image image = sheet.getImage(spriteIndex);
        if(image != null) {
            g.drawImage(image, x, y, width, height, null);
            g.setColor(Color.red);
            g.drawRect(x, y, width, height);
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public CharacterState getState() {
        return state;
    }
    
    public void setState(CharacterState state) {
        this.state = state;
    }
    
    public SpriteSheet getSheet() {
        return sheet;
    }
    
    public void setSheet(SpriteSheet sheet) {
        this.sheet = sheet;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setSpriteIndex(int spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    public int getTickToExecute() {
        return tickToExecute;
    }

    public void setTickToExecute(int tickToExecute) {
        this.tickToExecute = tickToExecute;
    }

    
    
}
