package fightinggame.entity.inventory;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.animation.item.SlotAnimation;
import fightinggame.entity.Character;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.item.Item;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.Stats;
import fightinggame.entity.item.collectable.ConsumeItem;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.GameHandler;
import fightinggame.input.handler.game.player.PlayerKeyboardHandler;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Inventory {

    public static int ITEM_SLOT_WIDTH = 90;
    public static int ITEM_SLOT_HEIGHT = 90;
    public static int ITEM_SLOT_ANIMATION_TICK = -1;
    public static int DISTANCE_X_TO_CAMERA = 350;
    public static int DISTANCE_Y_TO_CAMERA = 270;
    private int row = 6;
    private int column = 7;
    private Character character;
    private SpriteSheet sheet;
    private List<List<SlotInventory>> inventorySlots = new ArrayList();
    private GamePosition position;
    private Gameplay gameplay;
    private boolean isOpen;

    //player stats
    private Map<String, BufferedImage> statsGuis;
    private int[] numberOfStats = {0, 0, 0, 0, 0};
    private int currStatIndex = 0;
    private int currArrowIndex = 0;
    private BufferedImage imageSelection;
    private boolean haveLevelUpPoint;

    public Inventory(Character character, SpriteSheet sheet, Gameplay gameplay) {
        this.character = character;
        this.sheet = sheet;
        this.position = new GamePosition(0, 0, 0, 0);
        this.gameplay = gameplay;
        init();
        if (character instanceof Player) {
            statsGuis = ImageManager.loadImagesFromFolderToMap("assets/res/gui/stats/player");
        }
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
                    position.setXPosition(gameplay.getCamera().getPosition().getXPosition() + DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 + 10);
                    position.setYPosition(gameplay.getCamera().getPosition().getYPosition() + DISTANCE_Y_TO_CAMERA + 5);
                    slotChangePosition();
                    Player player = (Player) character;
                    if (player != null) {
                        List<GameHandler> controllers = player.getController();
                        if (controllers != null && controllers.size() > 0) {
                            PlayerKeyboardHandler keyHandler = (PlayerKeyboardHandler) controllers.get(0);
                            if (keyHandler != null) {
                                if (keyHandler.isDownArrowPressed()) {
                                    if (currStatIndex < numberOfStats.length) {
                                        currStatIndex++;
                                    } else {
                                        currStatIndex = 0;
                                    }
                                    keyHandler.setDownArrowPressed(false);
                                }
                                if (keyHandler.isUpArrowPressed()) {
                                    if (currStatIndex > 0) {
                                        currStatIndex--;
                                    } else {
                                        currStatIndex = numberOfStats.length;
                                    }
                                    keyHandler.setUpArrowPressed(false);
                                }
                                if (keyHandler.isRightArrowPressed()) {
                                    if (currArrowIndex < 1) {
                                        currArrowIndex++;
                                    } else {
                                        currArrowIndex = 0;
                                    }
                                    keyHandler.setRightArrowPressed(false);
                                }
                                if (keyHandler.isLeftArrowPressed()) {
                                    if (currArrowIndex > 0) {
                                        currArrowIndex--;
                                    } else {
                                        currArrowIndex = 1;
                                    }
                                    keyHandler.setLeftArrowPressed(false);
                                }
                                if (keyHandler.isEnterPressed()) {
                                    if (numberOfStats.length > 0) {
                                        if (currStatIndex < numberOfStats.length) {
                                            int statPlus = numberOfStats[currStatIndex];
                                            switch (currArrowIndex) {
                                                case 0:
                                                    if (statPlus > 0) {
                                                        statPlus--;
                                                        player.getStats().setLevelUpPoint(player.getStats().getLevelUpPoint() + 1);
                                                    }
                                                    break;
                                                case 1:
                                                    if (player.getStats().getLevelUpPoint() > 0) {
                                                        statPlus++;
                                                        player.getStats().setLevelUpPoint(player.getStats().getLevelUpPoint() - 1);
                                                    }
                                                    break;
                                            }
                                            numberOfStats[currStatIndex] = statPlus;
                                        } else {
                                            switch (currArrowIndex) {
                                                case 0:
                                                    for (int i = 0; i < numberOfStats.length; i++) {
                                                        int statPlus = numberOfStats[i];
                                                        if (statPlus > 0) {
                                                            player.getStats().setLevelUpPoint(player.getStats().getLevelUpPoint() + statPlus);
                                                        }
                                                        numberOfStats[i] = 0;
                                                    }
                                                    break;
                                                case 1:
                                                    for (int i = 0; i < numberOfStats.length; i++) {
                                                        int statPlus = numberOfStats[i];
                                                        if (statPlus > 0) {
                                                            switch (i) {
                                                                case 0:
                                                                    player.getStatusBar().setMaxHealth(player.getStatusBar().getMaxHealth() + (statPlus * 2));
                                                                    break;
                                                                case 1:
                                                                    player.getStatusBar().setMaxEnergy(player.getStatusBar().getMaxEnergy() + (statPlus * 2));
                                                                    break;
                                                                case 2:
                                                                    player.getStats().addAttackDamage(statPlus);
                                                                    break;
                                                                case 3:
                                                                    player.getStats().setCritDamage(player.getStats().getCritDamage() + statPlus);
                                                                    break;
                                                                case 4:
                                                                    player.getStats().addCritChance(statPlus);
                                                                    break;
                                                            }
                                                        }
                                                        numberOfStats[i] = 0;
                                                    }
                                                    if (player.getStats().getLevelUpPoint() == 0) {
                                                        haveLevelUpPoint = false;
                                                    }
                                                    break;
                                            }
                                        }
                                        keyHandler.setEnterPressed(false);
                                    }
                                }
                            }
                        }
                    }
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
                    Stats stats = character.getStats();
                    Player player = gameplay.getPlayer();
                    if (statsGuis != null && statsGuis.size() > 0) {
                        int x = DISTANCE_X_TO_CAMERA, y = DISTANCE_Y_TO_CAMERA;
                        g.drawImage(statsGuis.get("background_stats"), x, y,
                                gameplay.getWidth() / 3, gameplay.getHeight() / 2 + 100, null);
                        x += 30;
                        y += 20;
                        g.drawImage(statsGuis.get("avatar_border"), x, y, 220, 150, null);
                        x += 35;
                        y += 15;
                        g.drawImage(player.getAvatar(), x, y, 150, 100, gameplay);
                        g.setFont(DataManager.getFont(30f));
                        g.setColor(new Color(133, 0, 0));
                        x = DISTANCE_X_TO_CAMERA + 280;
                        y = DISTANCE_Y_TO_CAMERA + 70;
                        g.drawString(player.getName(), x, y);
                        y += 40;
                        g.drawString("Level: " + stats.getLevel(), x, y);
                        y += 40;
                        g.drawString("AP: " + player.getScore(), x, y);
                        x = DISTANCE_X_TO_CAMERA + 45;
                        y = DISTANCE_Y_TO_CAMERA + 210;
                        g.drawString("HP: " + stats.getHealth() + "/" + player.getStatusBar().getMaxHealth(), x, y);
                        y += 40;
                        g.drawString("MP: " + stats.getEnergy() + "/" + player.getStatusBar().getMaxEnergy(), x, y);
                        y += 40;
                        g.drawString("Attack Damage: " + stats.getAttackDamage(), x, y);
                        y += 40;
                        g.drawString("Critical Damage: " + stats.getCritDamage(), x, y);
                        y += 40;
                        g.drawString("Critical Change: " + stats.getCritChangeString(), x, y);
                        y += 40;
                        g.drawString("Armor: " + stats.getDefence(), x, y);
                        y += 40;
                        g.drawString("Exp: " + (int) stats.getLevelExperience() + "/" + (int) stats.getNextLevelExperience(), x, y);

                        if (haveLevelUpPoint) {
                            y = DISTANCE_Y_TO_CAMERA + 185;
                            for (int i = 0; i < numberOfStats.length; i++) {
                                x = DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 110;
                                g.drawImage(statsGuis.get("arrow_left"), x, y, 30, 30, null);
                                g.drawString(numberOfStats[i] + "", x + 35, y + 27);
                                x += 55;
                                g.drawImage(statsGuis.get("arrow_right"), x, y, 30, 30, null);
                                y += 40;
                            }
                            y = DISTANCE_Y_TO_CAMERA;
                            g.drawString("Level Up Point: " + stats.getLevelUpPoint(), DISTANCE_X_TO_CAMERA + 45, (y + (gameplay.getHeight() / 2 + 100)) - 55);
                            g.drawImage(statsGuis.get("plus"), DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 60, (y + (gameplay.getHeight() / 2 + 100)) - 80, 30, 30, null);
                            g.drawImage(statsGuis.get("cancel"), DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 110, (y + (gameplay.getHeight() / 2 + 100)) - 80, 30, 30, null);
                            switch (currArrowIndex) {
                                case 0:
                                    if (currStatIndex == numberOfStats.length) {
                                        imageSelection = statsGuis.get("cancel_red");
                                        x = DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 110;
                                    } else {
                                        imageSelection = statsGuis.get("arrow_red_left");
                                        x = DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 110;
                                    }
                                    break;
                                case 1:

                                    if (currStatIndex == numberOfStats.length) {
                                        imageSelection = statsGuis.get("plus_red");
                                        x = DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 60;
                                    } else {
                                        imageSelection = statsGuis.get("arrow_red_right");
                                        x = DISTANCE_X_TO_CAMERA + gameplay.getWidth() / 3 - 55;
                                    }
                                    break;
                            }
                            if (currStatIndex > 0) {
                                if (currStatIndex == numberOfStats.length) {
                                    y = (DISTANCE_Y_TO_CAMERA + (gameplay.getHeight() / 2 + 100)) - 80;
                                } else {
                                    y = DISTANCE_Y_TO_CAMERA + 185 + currStatIndex * 40;
                                }
                            } else {
                                y = DISTANCE_Y_TO_CAMERA + 185;
                            }
                            g.drawImage(imageSelection, x, y, 30, 30, null);
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
    
    public Item getConsumeItemAscending() {
        if (inventorySlots.size() > 0) {
            for (int i = 0; i < inventorySlots.size(); i++) {
                if (inventorySlots.get(i) != null && inventorySlots.get(i).size() > 0) {
                    for (int j = 0; j < inventorySlots.get(i).size(); j++) {
                        SlotInventory slot = inventorySlots.get(i).get(j);
                        if (slot != null) {
                            Item item = slot.getItem();
                            if (item != null) {
                                if(item instanceof ConsumeItem) {
                                    return item;
                                }
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
            if (gameplay.getPlayer().getStats().getLevelUpPoint() > 0) {
                haveLevelUpPoint = true;
            }
            Game.STATE = GameState.PLAYER_STATE;
            isOpen = true;
        } else {
            currStatIndex = 0;
            currArrowIndex = 0;
            if (numberOfStats.length > 0) {
                for (int i = 0; i < numberOfStats.length; i++) {
                    int statPlus = numberOfStats[i];
                    if (statPlus > 0) {
                        gameplay.getPlayer().getStats().setLevelUpPoint(gameplay.getPlayer().getStats().getLevelUpPoint() + statPlus);
                    }
                    numberOfStats[i] = 0;
                }
            }
            isOpen = false;
            Game.STATE = GameState.GAME_STATE;
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

    public int getCurrStatIndex() {
        return currStatIndex;
    }

    public void setCurrStatIndex(int currStatIndex) {
        this.currStatIndex = currStatIndex;
    }

    public int getCurrArrowIndex() {
        return currArrowIndex;
    }

    public void setCurrArrowIndex(int currArrowIndex) {
        this.currArrowIndex = currArrowIndex;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

}
