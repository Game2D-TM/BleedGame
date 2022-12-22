package fightinggame.entity.item;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.input.handler.Handler;
import fightinggame.entity.Character;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Item {

    protected int id;
    protected String name;
    protected Animation animation;
    protected Character character;
    protected GamePosition position;
    protected int amount;
    protected final List<Handler> handlers = new ArrayList<>();
    protected final List<Ability> abilities = new ArrayList<>();
    protected Gameplay gameplay;
    protected boolean spawnDrop;
    protected int dropExpireCounter = 0;
    protected int timeExpire = 1500;

    public Item(int id, String name, Animation animation, Character character, GamePosition position,
            Gameplay gameplay, int amount) {
        this.id = id;
        this.name = name;
        this.animation = animation;
        this.character = character;
        this.position = position;
        this.amount = amount;
        this.gameplay = gameplay;
    }

    public void tick() {
        if (handlers.size() > 0) {
            for (int i = 0; i < handlers.size(); i++) {
                Handler handler = handlers.get(i);
                handler.tick();
            }
        }
        if (animation != null) {
            animation.tick();
        }
        if (abilities.size() > 0) {
            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                ability.tick();
            }
        }
        if(spawnDrop) {
            dropExpireCounter++;
            if(dropExpireCounter > timeExpire) {
                gameplay.getItemsOnGround().remove(this);
                spawnDrop = false;
                dropExpireCounter = 0;
            }
        }
    }

    public void render(Graphics g) {
        if (animation != null && spawnDrop) {
            animation.render(g, position.getXPosition() , position.getYPosition(),
                     position.getWidth(), position.getHeight());
        }
        if (abilities.size() > 0) {
            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                ability.render(g);
            }
        }
    }
    
    public abstract boolean use();

    public Animation getAnimation() {
        return animation;
    }
    
    public BufferedImage getIcon() {
        if(animation != null) {
            if(animation.getSheet().getImages().size() > 0) 
                return animation.getSheet().getImage(0);
            else return null;
        }
        return null;
    }
    
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }
    
    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public boolean isSpawnDrop() {
        return spawnDrop;
    }

    public void setSpawnDrop(boolean spawnDrop) {
        this.spawnDrop = spawnDrop;
    }
    
    
}
