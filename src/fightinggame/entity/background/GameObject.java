package fightinggame.entity.background;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.platform.Platform;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class GameObject {

    protected BufferedImage image;
    protected String name;
    protected final Gameplay gameplay;
    protected GamePosition position;
    protected Platform platform;

    public GameObject(BufferedImage image, String name, Platform platform, GamePosition position, Gameplay gameplay) {
        this.image = image;
        this.name = name;
        this.position = position;
        this.gameplay = gameplay;
        this.platform = platform;
    }
    
    public void tick() {
        
    }
    
    public void render(Graphics g) {
        if(position != null) {
            g.drawImage(image, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition()
                    , position.getYPosition() - gameplay.getCamera().getPosition().getYPosition()
                    , position.getWidth(), position.getHeight(), null);
//          hitbox
//            g.setColor(Color.red);
//            g.drawRect(position.getXPosition() - gameplay.getCamera().getPosition().getXPosition()
//                    , position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(), 
//                    position.getWidth(), position.getHeight());
        }
    }

    @Override
    public GameObject clone() {
        return new GameObject(image, name, platform, position, gameplay);
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

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

}
