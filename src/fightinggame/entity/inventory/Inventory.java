package fightinggame.entity.inventory;

import fightinggame.Gameplay;
import fightinggame.animation.item.SlotAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.SpriteSheet;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public static int ITEM_SLOT_WIDTH = 90;
    public static int ITEM_SLOT_HEIGHT = 90;
    public static int ITEM_SLOT_ANIMATION_TICK = -1;
    public static int DISTANCE_X_TO_CAMERA = 650;
    public static int DISTANCE_Y_TO_CAMERA = 250;
    private int row = 6;
    private int column = 10;
    private Character character;
    private SpriteSheet sheet;
    private List<List<SlotInventory>> inventorySlots = new ArrayList();
    private GamePosition position;
    private Gameplay gameplay;
    private boolean isOpen;

    public Inventory(Character character, SpriteSheet sheet, Gameplay gameplay) {
        this.character = character;
        this.sheet = sheet;
        this.position = new GamePosition(0, 0, 0, 0);
        this.gameplay = gameplay;
        init();
    }

    private void init() {
        if (position == null) {
            return;
        }
        if (character != null && character instanceof Player) {
            if (sheet != null && sheet.getImages().size() > 2) {
                int tempY = position.getYPosition();
                int tempX = -1;
                for (int i = 1; i <= row; i++) {
                    List<SlotInventory> images = new ArrayList<>();
                    tempX = position.getXPosition();
                    for (int j = 1; j <= column; j++) {
                        SpriteSheet nSheet = new SpriteSheet();
                        nSheet.getImages().add(sheet.getImage(0));
                        nSheet.getImages().add(sheet.getImage(1));
                        nSheet.getImages().add(sheet.getImage(2));
                        SlotAnimation animation = new SlotAnimation((i + j), nSheet, ITEM_SLOT_ANIMATION_TICK);
                        images.add(new SlotInventory(animation,
                                new GamePosition(tempX, tempY, ITEM_SLOT_WIDTH, ITEM_SLOT_HEIGHT),
                                gameplay, i - 1, j - 1));
                        tempX += ITEM_SLOT_WIDTH;
                    }
                    inventorySlots.add(images);
                    tempY += ITEM_SLOT_HEIGHT;
                }
                position.setWidth(tempX);
                position.setHeight(tempY);
            }
        } else {
            for (int i = 1; i <= row; i++) {
                List<SlotInventory> images = new ArrayList<>();
                for (int j = 1; j <= column; j++) {
                    images.add(new SlotInventory(null, null, gameplay, i - 1, j - 1));
                }
                inventorySlots.add(images);
            }
        }
    }

    private void slotChangePosition() {
        if (position == null) {
            return;
        }
        if (inventorySlots.size() > 0) {
            int tempY = position.getYPosition();
            int tempX = -1;
            for (int i = 0; i < inventorySlots.size(); i++) {
                tempX = position.getXPosition();
                List<SlotInventory> slots = inventorySlots.get(i);
                if (slots != null && slots.size() > 0) {
                    for (int j = 0; j < slots.size(); j++) {
                        SlotInventory slot = slots.get(j);
                        if (slot != null) {
                            slot.getPosition().setXPosition(tempX);
                            slot.getPosition().setYPosition(tempY);
                            if (slot.getItem() != null) {
                                slot.setItemPosition();
                            }
                        }
                        tempX += ITEM_SLOT_WIDTH;
                    }
                    tempY += ITEM_SLOT_HEIGHT;
                }
            }
        }
    }

    public void tick() {
        if (character != null) {
            if (character instanceof Player) {
                if (isOpen) {
                    position.setXPosition(gameplay.getCamera().getPosition().getXPosition() + DISTANCE_X_TO_CAMERA);
                    position.setYPosition(gameplay.getCamera().getPosition().getYPosition() + DISTANCE_Y_TO_CAMERA);
                    slotChangePosition();
                }
                if (inventorySlots.size() > 0) {
                    for (int i = 0; i < inventorySlots.size(); i++) {
                        if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                            for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                                SlotInventory slot = inventorySlots.get(i).get(j);
                                if (slot != null) {
                                    slot.tick();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        if (character != null) {
            if (character instanceof Player) {
                if (isOpen) {
                    if (inventorySlots.size() > 0) {
                        for (int i = 0; i < inventorySlots.size(); i++) {
                            if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                                for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                                    SlotInventory slot = inventorySlots.get(i).get(j);
                                    if (slot != null) {
                                        slot.render(g);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public SlotInventory findSlotInventoryByItem(Item item) {
        if (inventorySlots.size() > 0) {
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                    for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                        SlotInventory slot = inventorySlots.get(i).get(j);
                        if (slot != null) {
                            if (slot.getItem() != null) {
                                if (slot.getItem().equals(item)) {
                                    return slot;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public SlotInventory findSlotInventory(int row, int column) {
        try {
            return inventorySlots.get(row).get(column);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public SlotInventory getSlotInventoryAscending() {
        if (inventorySlots.size() > 0) {
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                    for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                        SlotInventory slot = inventorySlots.get(i).get(j);
                        if (slot != null) {
                            if (slot.getItem() != null) {
                                return slot;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Item getItemAscending() {
        SlotInventory slot = getSlotInventoryAscending();
        if (slot == null) {
            return null;
        }
        return slot.getItem();
    }

    public Item getItemByName(String name) {
        try {
            if (inventorySlots.size() > 0) {
                for (int i = 0; i < inventorySlots.size(); i++) {
                    if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                        for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                            SlotInventory slot = inventorySlots.get(i).get(j);
                            if (slot != null) {
                                if (slot.getItem() != null) {
                                    if (slot.getItem().getName().equalsIgnoreCase(name)) {
                                        return slot.getItem();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    public SlotInventory getEmptySlotInventory() {
        if (inventorySlots.size() > 0) {
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                    for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                        SlotInventory slot = inventorySlots.get(i).get(j);
                        if (slot != null) {
                            if (slot.getItem() == null) {
                                return slot;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<Item> getAllItemsFromInventory() {
        if (inventorySlots.size() > 0) {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                    for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                        SlotInventory slot = inventorySlots.get(i).get(j);
                        if (slot != null) {
                            if (slot.getItem() != null) {
                                items.add(slot.getItem());
                            }
                        }
                    }
                }
            }
            return items;
        }
        return null;
    }

    // need to code
    public void open() {
        if (!isOpen) {
            isOpen = true;
        } else {
            isOpen = false;
        }
    }

    // need to code
    public void addItemToInventory(Item item) {
        try {
            SlotInventory slotInventory = findSlotInventoryByItem(item);
            if (slotInventory != null) {
                Item itemInInventory = slotInventory.getItem();
                if (itemInInventory == null) {
                    throw new Exception("Error at find slot inventory method");
                }
                int nAmount = itemInInventory.getAmount() + item.getAmount();
                itemInInventory.setAmount(nAmount);
            } else {
                slotInventory = getEmptySlotInventory();
                if (slotInventory == null) {
                    throw new Exception("Error at get empty slot inventory method");
                }
                slotInventory.setItem(item);
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void removeItemFromInventory(Item item) {
        try {
            SlotInventory slotInventory = findSlotInventoryByItem(item);
            if (slotInventory != null) {
                slotInventory.setItem(null);
            } else {
                throw new Exception("Error not found item in inventory");
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public SpriteSheet getSheet() {
        return sheet;
    }

    public void setSheet(SpriteSheet sheet) {
        this.sheet = sheet;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public GamePosition getPosition() {
        return position;
    }

    public void setPosition(GamePosition position) {
        this.position = position;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

}
