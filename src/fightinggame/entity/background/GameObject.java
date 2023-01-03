package fightinggame.entity.background;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameObject {

    protected BufferedImage image;
    protected String name;
    protected final Gameplay gameplay;
    protected GamePosition position;

    public GameObject(BufferedImage image, String name, GamePosition position, Gameplay gameplay) {
        this.image = image;
        this.name = name;
        this.position = position;
        this.gameplay = gameplay;
    }
    
    public void tick() {
        
    }
    
    public void render(Graphics g) {
        if(position != null) {
            g.drawImage(image, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition()
                    , position.getYPosition() - gameplay.getCamera().getPosition().getYPosition()
                    , position.getWidth(), position.getHeight(), null);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

}
