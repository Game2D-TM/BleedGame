package fightinggame.entity.background.touchable;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.background.ObjectTouchable;
import fightinggame.entity.item.Item;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Chest extends ObjectTouchable {

    private List<Item> items;

    public Chest(BufferedImage imageAfterTouch, List<Item> items, BufferedImage image, String name,
            GamePosition position, Gameplay gameplay) {
        super(imageAfterTouch, image, name, position, gameplay);
        this.items = items;
    }

    public Chest(BufferedImage imageAfterTouch, BufferedImage image, String name,
            GamePosition position, Gameplay gameplay) {
        super(imageAfterTouch, image, name, position, gameplay);
        this.items = new ArrayList<>();
    }

    @Override
    public boolean checkHit(int attackX, int attackY, int attackHeight) {
        boolean result = super.checkHit(attackX, attackY, attackHeight);
        if (result) {
            if (items != null && items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (item == null) {
                        continue;
                    }
                    GamePosition itemPos = item.getPosition();
                    if (itemPos == null) {
                        itemPos = new GamePosition(position.getXPosition() + position.getWidth() / 2 - 20
                                , position.getMaxY() - itemPos.getHeight()
                                , Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
                        item.setPosition(itemPos);
                    }
                    gameplay.getItemsOnGround().add(item);
                    item.setSpawnForever(true);
                }
            }
            return true;
        }
        return false;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
