package fightinggame.entity.background.touchable;

import fightinggame.Gameplay;
import fightinggame.entity.GamePosition;
import fightinggame.entity.background.GameObject;
import fightinggame.entity.background.ObjectTouchable;
import fightinggame.entity.item.Item;
import fightinggame.entity.platform.Platform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Chest extends ObjectTouchable {

    private List<Item> items;

    public Chest(BufferedImage imageAfterTouch, List<Item> items, BufferedImage image, String name, Platform platform,
            GamePosition position, Gameplay gameplay) {
        super(imageAfterTouch, image, name, platform, position, gameplay);
        this.items = items;
    }

    public Chest(BufferedImage imageAfterTouch, BufferedImage image, String name, Platform platform,
            GamePosition position, Gameplay gameplay) {
        super(imageAfterTouch, image, name, platform, position, gameplay);
        this.items = new ArrayList<>();
    }

    @Override
    public boolean checkHit(GamePosition attackHitBox) {
        boolean result = super.checkHit(attackHitBox);
        if (result) {
            gameplay.getAudioPlayer().startThread("chest_open", false, gameplay.getOptionHandler().getOptionMenu().getSfxVolume());
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
                    item.setSpawnForever(true);
                    gameplay.getItemsOnGround().add(item);
                    items.remove(item);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public GameObject clone() {
        return new Chest(imageAfterTouch, items, image, name, platform, position, gameplay);
    }
    
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
