package fightinggame.entity.inventory;

import fightinggame.Gameplay;
import fightinggame.animation.item.SlotAnimation;
import fightinggame.entity.GamePosition;
import fightinggame.entity.item.Item;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class SlotInventory {

    private SlotAnimation animation;
    private GamePosition position;
    private Item item;
    private Gameplay gameplay;
    private int rowInventory;
    private int columnInventory;

    public SlotInventory(SlotAnimation animation, GamePosition position, Gameplay gameplay
        ,int rowInventory, int columnInventory) {
        this.animation = animation;
        this.position = position;
        this.gameplay = gameplay;
        this.rowInventory = rowInventory;
        this.columnInventory = columnInventory;
    }

    public SlotInventory(SlotAnimation animation, GamePosition position, Item item, Gameplay gameplay
        ,int rowInventory, int columnInventory) {
        this.animation = animation;
        this.position = position;
        this.item = item;
        this.gameplay = gameplay;
        this.rowInventory = rowInventory;
        this.columnInventory = columnInventory;
        setItemPosition();
    }

    public void tick() {
        if(animation != null) {
            animation.tick();
        }
        if(item != null) {
            item.tick();
            if(item.getPosition() == null) {
                setItemPosition();
            }
        }
    }

    public void render(Graphics g) {
        if(animation != null) {
            animation.render(g, position.getXPosition() - gameplay.getCamera().getPosition().getXPosition()
                    , position.getYPosition() - gameplay.getCamera().getPosition().getYPosition()
                    , position.getWidth(), position.getHeight());
            if(item != null) {
                if(item.getPosition() != null) {
                    item.renderInventory(g);
                    g.setColor(Color.white);
                    g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
                    g.drawString("x" + item.getAmount(), item.getPosition().getMaxX() - 24 - gameplay.getCamera().getPosition().getXPosition()
                            , item.getPosition().getMaxY() - gameplay.getCamera().getPosition().getYPosition());
                }
            }
        }
    }
    
    public void setItemPosition() {
        if(item != null) {
            GamePosition itemInSlotPos = 
                    new GamePosition(position.getXPosition() + 12, 
                                    position.getYPosition() + 18, Inventory.ITEM_WIDTH, Inventory.ITEM_HEIGHT);
            item.setPosition(itemInSlotPos);
        }
    }
    public void setItemPosition(GamePosition itemInSlotPos) {
        if(item != null) {
            item.setPosition(itemInSlotPos);
        }
    }
    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public SlotAnimation getAnimation() {
        return animation;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getRowInventory() {
        return rowInventory;
    }

    public void setRowInventory(int rowInventory) {
        this.rowInventory = rowInventory;
    }

    public int getColumnInventory() {
        return columnInventory;
    }

    public void setColumnInventory(int columnInventory) {
        this.columnInventory = columnInventory;
    }
    
    
    
}
