package fightinggame.entity.npc;

import fightinggame.Game;
import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Dialogue;
import fightinggame.entity.GamePosition;
import fightinggame.entity.Player;
import fightinggame.entity.inventory.Inventory;
import fightinggame.entity.item.Item;
import fightinggame.entity.state.CharacterState;
import fightinggame.entity.state.GameState;
import fightinggame.input.handler.game.player.PlayerKeyboardHandler;
import fightinggame.resource.DataManager;
import fightinggame.resource.ImageManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public abstract class MerchantNPC extends NPC {

    protected PlayerKeyboardHandler keyHandler;
    protected Dialogue dialogue;
    protected boolean isConversation;
    protected boolean openOption;
    protected final String[] dialogueOptions = {"Yes", "No"};
    protected final String[] shoppingOptions = {"Buy", "Sell", "Cancel"};
    protected final String[] shopOptions = {"Options", "Items"};
    protected int shoppingOptionsIndex = 0;
    protected int shopOptionsIndex = 0;
    protected int dialogueOptionsIndex = 0;
    protected boolean shopping;
    protected BufferedImage shopAvatar;
    protected int inventorySize;
    protected int itemIndex = 0;
    protected String shopDescription = "";

    public MerchantNPC(int id, String name, GamePosition position, Map<CharacterState, Animation> animations, Gameplay gameplay, boolean isLTR) {
        super(id, name, position, animations, gameplay, isLTR);
        avatar = ImageManager.flipImage(avatar);
        Player player = gameplay.getPlayer();
        if (player != null) {
            keyHandler = (PlayerKeyboardHandler) player.getController().get(0);
        }
        dialogue = new Dialogue(this, gameplay);
        initItems();
    }

    public MerchantNPC() {
    }

    public abstract void initItems();

    protected boolean buyItem(Item item) {
        if (item == null) {
            return false;
        }
        Item itemInInventory = inventory.getItemByName(item.getName());
        Player player = gameplay.getPlayer();
        if (player == null) {
            return false;
        }
        int apPoint = player.getScore();
        if (itemInInventory != null) {
            int amount = itemInInventory.getAmount();
            if (amount <= 0) {
                return false;
            }
            amount -= 1;
            if (apPoint < itemInInventory.getPrice()) {
                return false;
            }
            itemInInventory.setAmount(amount);
            apPoint -= itemInInventory.getPrice();
            player.setScore(apPoint);
            Item nItem = itemInInventory.clone();
            nItem.setAmount(1);
            nItem.setCharacter(player);
            if (nItem.getPrice() > 0) {
                int nPrice = nItem.getPrice() / 2;
                nItem.setPrice(nPrice);
            }
            player.getInventory().addItemToInventory(nItem);
            return true;
        }
        return false;
    }

    protected boolean sellItem(Item item) {
        if (item == null) {
            return false;
        }
        Player player = gameplay.getPlayer();
        if (player == null) {
            return false;
        }
        Inventory playerInventory = player.getInventory();
        if (playerInventory == null) {
            return false;
        }
        int amount = item.getAmount();
        if (amount > 0) {
            int apPoint = player.getScore();
            apPoint += item.getPrice();
            player.setScore(apPoint);
            amount -= 1;
            item.setAmount(amount);
            if (amount == 0) {
                playerInventory.removeItemFromInventory(item);
            }
            Item nItem = item.clone();
            nItem.setAmount(1);
            nItem.setCharacter(this);
            inventory.addItemToInventory(nItem);
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (keyHandler != null) {
            Player player = gameplay.getPlayer();
            if (checkHit(player.getHitBoxPosition(), player)) {
                if (isConversation) {
                    if (openOption) {
                        if (shopping) {
                            switch (shopOptionsIndex) {
                                case 0:
                                    if (keyHandler.isUpArrowPressed()) {
                                        if (shopOptionsIndex > 0) {
                                            shopOptionsIndex--;
                                        } else {
                                            shopOptionsIndex = shopOptions.length - 1;
                                        }
                                        keyHandler.setUpArrowPressed(false);
                                    }
                                    if (keyHandler.isDownArrowPressed()) {
                                        if (shopOptionsIndex < shopOptions.length - 1) {
                                            shopOptionsIndex++;
                                        } else {
                                            shopOptionsIndex = 0;
                                        }
                                        keyHandler.setDownArrowPressed(false);
                                    }
                                    if (keyHandler.isRightArrowPressed()) {
                                        if (shoppingOptionsIndex < shoppingOptions.length - 1) {
                                            shoppingOptionsIndex++;
                                        } else {
                                            shoppingOptionsIndex = 0;
                                        }
                                        keyHandler.setRightArrowPressed(false);
                                    }
                                    if (keyHandler.isLeftArrowPressed()) {
                                        if (shoppingOptionsIndex > 0) {
                                            shoppingOptionsIndex--;
                                        } else {
                                            shoppingOptionsIndex = shoppingOptions.length - 1;
                                        }
                                        keyHandler.setLeftArrowPressed(false);
                                    }
                                    if (keyHandler.isEnterPressed()) {
                                        switch (shoppingOptionsIndex) {
                                            case 2:
                                                shopDescription = "";
                                                itemIndex = 0;
                                                dialogueOptionsIndex = 0;
                                                shoppingOptionsIndex = 0;
                                                shopping = false;
                                                isConversation = false;
                                                openOption = false;
                                                gameplay.setHideGUI(false);
                                                player.setIsShopping(false);
                                                Game.STATE = GameState.GAME_STATE;
                                                break;
                                        }
                                        keyHandler.setEnterPressed(false);
                                    }
                                    break;
                                case 1:
                                    if (keyHandler.isEscPressed()) {
                                        shopOptionsIndex = 0;
                                        itemIndex = 0;
                                        keyHandler.setEscPressed(false);
                                    }
                                    List<Item> items = null;
                                    switch (shoppingOptionsIndex) {
                                        case 0:
                                            items = inventory.getAllItemsFromInventory();
                                            inventorySize = items.size();
                                            break;
                                        case 1:
                                            items = player.getInventory().getAllItemsFromInventory();
                                            inventorySize = items.size();
                                            break;
                                    }
                                    if (items == null || inventorySize <= 0) {
                                        shopOptionsIndex = 0;
                                        break;
                                    }
                                    if (keyHandler.isUpArrowPressed()) {
                                        if (itemIndex > 0) {
                                            itemIndex--;
                                        } else {
                                            itemIndex = inventorySize - 1;
                                        }
                                        keyHandler.setUpArrowPressed(false);
                                    }
                                    if (keyHandler.isDownArrowPressed()) {
                                        if (itemIndex < inventorySize - 1) {
                                            itemIndex++;
                                        } else {
                                            itemIndex = 0;
                                        }
                                        keyHandler.setDownArrowPressed(false);
                                    }
                                    if (keyHandler.isEnterPressed()) {
                                        switch (shoppingOptionsIndex) {
                                            case 0:
                                                if (items != null && items.size() > 0) {
                                                    Item item = items.get(itemIndex);
                                                    if (item != null) {
                                                        buyItem(item);
                                                    }
                                                }
                                                break;
                                            case 1:
                                                if (items != null && items.size() > 0) {
                                                    Item item = items.get(itemIndex);
                                                    if (item != null) {
                                                        sellItem(item);
                                                    }
                                                }
                                                break;
                                        }
                                        keyHandler.setEnterPressed(false);
                                    }
                                    break;
                            }
                        } else {
                            if (keyHandler.isDownArrowPressed()) {
                                if (dialogueOptionsIndex < dialogueOptions.length - 1) {
                                    dialogueOptionsIndex++;
                                } else {
                                    dialogueOptionsIndex = 0;
                                }
                                keyHandler.setDownArrowPressed(false);
                            }
                            if (keyHandler.isUpArrowPressed()) {
                                if (dialogueOptionsIndex > 0) {
                                    dialogueOptionsIndex--;
                                } else {
                                    dialogueOptionsIndex = dialogueOptions.length - 1;
                                }
                                keyHandler.setUpArrowPressed(false);
                            }
                            if (keyHandler.isEnterPressed()) {
                                switch (dialogueOptionsIndex) {
                                    case 0:
                                        shopping = true;
                                        gameplay.setHideGUI(true);
                                        dialogue.setEndDialogue(true);
                                        break;
                                    case 1:
                                        isConversation = false;
                                        dialogue.setEndDialogue(true);
                                        openOption = false;
                                        dialogueOptionsIndex = 0;
                                        player.setIsShopping(false);
                                        Game.STATE = GameState.GAME_STATE;
                                        break;
                                }
                                keyHandler.setEnterPressed(false);
                            }
                        }
                    } else {
                        if (keyHandler.isSpaceBtnPressed()) {
                            if (dialogue.haveNextText()) {
                                dialogue.nextLoop();
                                keyHandler.setSpaceBtnPressed(false);
                            } else {
                                openOption = true;
                                Game.STATE = GameState.DIALOGUE_STATE;
                            }
                        }
                    }
                } else {
                    if (keyHandler.eBtnPressed()) {
                        dialogue.setEndDialogue(false);
                        if (player.isLTR()) {
                            isLTR = false;
                        } else {
                            isLTR = true;
                        }
                        if (isLTR) {
                            currAnimation = animations.get(CharacterState.IDLE_LTR);
                        } else {
                            currAnimation = animations.get(CharacterState.IDLE_RTL);
                        }
                        isConversation = true;
                        player.setIsShopping(true);
                        if (player.isSpeak()) {
                            player.setIsSpeak(false);
                            int index = player.getSpeakDialogueIndex() - 1;
                            if (index >= 0) {
                                player.setSpeakDialogueIndex(index);
                            }
                        }
                    }
                }
            } else {
                dialogue.setIndex(0);
                isConversation = false;
                openOption = false;
                dialogueOptionsIndex = 0;
                player.setIsShopping(false);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        if (isConversation) {
            if (dialogue != null) {
                dialogue.render(g);
            }
            if (openOption) {
                if (shopping) {
                    Graphics2D g2 = (Graphics2D) g;
                    // Header
                    g2.setColor(new Color(0, 0, 0, 220));
                    g2.fillRoundRect(dialogue.getPosition().getXPosition(), 20,
                            gameplay.getWidth() / 2, 80, 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(dialogue.getPosition().getXPosition(), 20,
                            gameplay.getWidth() / 2, 80, 25, 25);
                    // Player Point
                    g2.setFont(DataManager.getFont(30f));
                    g2.setColor(new Color(0, 0, 0, 220));
                    g2.fillRoundRect(dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 + 100, 20,
                            400, 80, 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 + 100, 20,
                            400, 80, 25, 25);
                    g2.drawString("Your Point: " + gameplay.getPlayer().getScore() + " AP",
                            dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 + 150, 65);
                    // Header Titles
                    g2.setFont(DataManager.getFont(45f));
                    g2.drawString("Buy", dialogue.getPosition().getXPosition() + 50, 75);
                    g2.drawString("Sell", dialogue.getPosition().getXPosition() + 200, 75);
                    g2.drawString("Cancel", dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 - 150, 75);
                    int shoppOptionX = 0, shoppOptionY = 75;
                    switch (shoppingOptionsIndex) {
                        case 0:
                            shopDescription = "Items For Selling.";
                            shoppOptionX = dialogue.getPosition().getXPosition() + 50;
                            break;
                        case 1:
                            shopDescription = "Selling Your Items.";
                            shoppOptionX = dialogue.getPosition().getXPosition() + 200;
                            break;
                        case 2:
                            shopDescription = "Exit.";
                            shoppOptionX = dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 - 150;
                            break;
                    }
                    g2.setColor(Color.red);
                    g2.drawString(shoppingOptions[shoppingOptionsIndex], shoppOptionX, shoppOptionY);
                    //Body
                    g2.setColor(new Color(0, 0, 0, 220));
                    g2.fillRoundRect(dialogue.getPosition().getXPosition(), 130,
                            gameplay.getWidth() / 2, gameplay.getHeight() / 2 + 100, 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(dialogue.getPosition().getXPosition(), 130,
                            gameplay.getWidth() / 2, gameplay.getHeight() / 2 + 100, 25, 25);
                    g2.setFont(DataManager.getFont(40f));
                    g2.setColor(Color.white);
                    Inventory choosenInventory = null;
                    switch (shoppingOptionsIndex) {
                        case 0:
                            choosenInventory = inventory;
                            break;
                        case 1:
                            if (gameplay.getPlayer() == null) {
                                break;
                            }
                            Inventory playerInventory = gameplay.getPlayer().getInventory();
                            if (playerInventory != null) {
                                choosenInventory = playerInventory;
                            }
                            break;
                    }
                    if (choosenInventory != null) {
                        List<Item> items = choosenInventory.getAllItemsFromInventory();
                        if (items != null && items.size() > 0) {
                            int length;
                            if (items.size() > 10) {
                                length = 10;
                            } else {
                                length = items.size();
                            }
                            int itemX = dialogue.getPosition().getXPosition() + 80, itemY = 200;
                            for (int i = 0; i < length; i++) {
                                Item item = items.get(i);
                                if (item != null) {
                                    g2.drawImage(item.getIcon(), itemX - 60, itemY - 35, Item.ITEM_WIDTH, Item.ITEM_HEIGHT, null);
                                    g2.drawString(item.getName() + " (" + item.getAmount() + ")", itemX, itemY);
                                    g2.drawString(item.getPrice() + " AP", dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 - 150, itemY);
                                    itemY += 50;
                                }
                            }
                            if (shopOptionsIndex == 1) {
                                itemX = dialogue.getPosition().getXPosition() + 80;
                                itemY = 200;
                                Item currItem = items.get(itemIndex);
                                if (itemIndex > 0) {
                                    itemY += 50 * itemIndex;
                                }
                                g2.setColor(Color.red);
                                g2.drawImage(currItem.getIcon(), itemX - 60, itemY - 35, Item.ITEM_WIDTH, Item.ITEM_HEIGHT, null);
                                g2.drawString(currItem.getName() + " (" + currItem.getAmount() + ")", itemX, itemY);
                                g2.drawString(currItem.getPrice() + " AP", dialogue.getPosition().getXPosition() + gameplay.getWidth() / 2 - 150, itemY);
                                shopDescription = currItem.getDescription();
                            }
                        }
                    }
                    //Dialogue
                    g2.setColor(new Color(0, 0, 0, 90));
                    g2.fillRoundRect(dialogue.getPosition().getXPosition(), dialogue.getPosition().getYPosition(),
                            dialogue.getPosition().getWidth(), dialogue.getPosition().getHeight(), 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(dialogue.getPosition().getXPosition(), dialogue.getPosition().getYPosition(),
                            dialogue.getPosition().getWidth(), dialogue.getPosition().getHeight(), 25, 25);
                    //Dialogue Text
                    g2.setFont(DataManager.getFont(25f));
                    int yText = dialogue.getPosition().getYPosition() + dialogue.getPosition().getHeight() / 3 - 15;
                    g2.drawString(shopDescription, dialogue.getPosition().getXPosition() + 50, yText);
                    //Avatar
                    g2.drawImage(ImageManager.flipImage(shopAvatar), 100 + gameplay.getWidth() / 2 + 100, 100, 600, 700, null);
                } else {
                    GamePosition dialoguePos = dialogue.getPosition();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(0, 0, 0, 220));
                    g2.fillRoundRect(dialoguePos.getMaxX() - 100, dialoguePos.getYPosition() - 80,
                            150, 200, 35, 35);
                    g2.setColor(Color.white);
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(dialoguePos.getMaxX() - 100, dialoguePos.getYPosition() - 80,
                            150, 200, 25, 25);
                    g2.setFont(DataManager.getFont(40f));
                    g2.setColor(Color.white);
                    if (dialogueOptions != null && dialogueOptions.length > 0) {
                        int y = dialoguePos.getYPosition() - 10;
                        int x = dialoguePos.getMaxX() - 50;
                        for (String option : dialogueOptions) {
                            g2.drawString(option, x, y);
                            y += 65;
                            x += 5;
                        }
                        g.setColor(new Color(133, 0, 0));
                        switch (dialogueOptionsIndex) {
                            case 0:
                                y = dialoguePos.getYPosition() - 10;
                                x = dialoguePos.getMaxX() - 50;
                                break;
                            case 1:
                                y = dialoguePos.getYPosition() + 55;
                                x = dialoguePos.getMaxX() - 45;
                                break;
                        }
                        g2.drawString(dialogueOptions[dialogueOptionsIndex], x, y);
                    }
                }
            }
        }
    }

}
