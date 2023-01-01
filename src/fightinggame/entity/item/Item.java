package fightinggame.entity.item;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.ability.Ability;
import fightinggame.input.handler.Handler;
import fightinggame.entity.Character;
import fightinggame.entity.inventory.Inventory;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Item {

    public static int ITEM_WIDTH = 50;
    public static int ITEM_HEIGHT = 50;

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
    protected boolean spawnForever;
    protected int dropExpireCounter = 0;
    protected int timeExpire = 2000;

    public Item(int id, String name, Animation animation, Character character,
            Gameplay gameplay, int amount) {
        this.id = id;
        this.name = name;
        this.animation = animation;
        this.character = character;
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
        if (spawnForever) {
            checkHit(gameplay.getPlayer());
        }
        if (spawnDrop && !spawnForever) {
            dropExpireCounter++;
            if (dropExpireCounter > timeExpire) {
                gameplay.getItemsOnGround().remove(this);
                spawnDrop = false;
                dropExpireCounter = 0;
            } else {
                checkHit(gameplay.getPlayer());
            }
        }
    }

    public void render(Graphics g) {
        if (animation != null && (spawnDrop || spawnForever)) {
            animation.render(g, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                    position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                    position.getWidth(), position.getHeight());
            //hitbox
//            g.setColor(Color.red);
//            g.drawRect(getXHitBox() - gameplay.getCamera().getPosition().getXPosition(),
//                    getYHitBox() - gameplay.getCamera().getPosition().getYPosition(),
//                    getWidthHitBox(), getHeightHitBox());
        }
        if (abilities.size() > 0) {
            for (int i = 0; i < abilities.size(); i++) {
                Ability ability = abilities.get(i);
                ability.render(g);
            }
        }
    }

    public void renderInventory(Graphics g) {
        if (animation != null) {
            if (position != null) {
                animation.render(g, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition(),
                        position.getYPosition() - gameplay.getCamera().getPosition().getYPosition(),
                        position.getWidth(), position.getHeight());
            }
        }
    }

    public boolean checkHit(Character character) {
        if (((spawnDrop && dropExpireCounter <= 1500) || spawnForever) && !character.isAttack()) {
            GamePosition charPostion = new GamePosition(character.getXHitBox(), character.getYHitBox(),
                    character.getWidthHitBox(), character.getHeightHitBox());
            if (((charPostion.getXPosition() < getXHitBox() && charPostion.getMaxX() > getXMaxHitBox())
                    || (charPostion.getXPosition() >= getXHitBox() && charPostion.getXPosition() <= getXMaxHitBox()
                    && charPostion.getMaxX() > getXMaxHitBox())
                    || (charPostion.getMaxX() >= getXHitBox() && charPostion.getMaxX() <= getXMaxHitBox()
                    && charPostion.getXPosition() < getXHitBox())
                    || (charPostion.getXPosition() >= getXHitBox() && charPostion.getMaxX() <= getXMaxHitBox()))
                    && ((charPostion.getYPosition() < getYHitBox() && charPostion.getMaxY() > getYMaxHitBox()
                    || (charPostion.getYPosition() >= getYHitBox() && charPostion.getYPosition() <= getYMaxHitBox()
                    && charPostion.getMaxY() > getYMaxHitBox())
                    || (charPostion.getMaxY() >= getYHitBox() && charPostion.getMaxY() <= getYMaxHitBox()
                    && charPostion.getYPosition() < getYHitBox()))
                    || (charPostion.getYPosition() >= getYHitBox() && charPostion.getMaxY() <= getYMaxHitBox()))) {
                List<Item> itemsOnGround = gameplay.getItemsOnGround();
                if (itemsOnGround.size() > 0) {
                    if (itemsOnGround.contains(this)) {
                        this.character = character;
                        if (abilities.size() > 0) {
                            for (int i = 0; i < abilities.size(); i++) {
                                Ability ability = abilities.get(i);
                                if (ability != null) {
                                    ability.setCharacter(character);
                                }
                            }
                        }
                        itemsOnGround.remove(this);
                        Inventory inventory = character.getInventory();
                        inventory.addItemToInventory(this);
                        spawnDrop = false;
                        dropExpireCounter = 0;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public abstract boolean use();

    public Animation getAnimation() {
        return animation;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equalsIgnoreCase(((Item) obj).getName());
    }

    public BufferedImage getIcon() {
        if (animation != null) {
            if (animation.getSheet().getImages().size() > 0) {
                return animation.getSheet().getImage(0);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public abstract Item clone();

    public abstract int getXHitBox();

    public abstract int getWidthHitBox();

    public abstract int getHeightHitBox();

    public abstract int getXMaxHitBox();

    public abstract int getYHitBox();

    public abstract int getYMaxHitBox();

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

    public boolean isSpawnForever() {
        return spawnForever;
    }

    public void setSpawnForever(boolean spawnForever) {
        this.spawnForever = spawnForever;
    }

}
