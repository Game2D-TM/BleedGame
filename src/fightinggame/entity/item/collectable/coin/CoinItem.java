package fightinggame.entity.item.collectable.coin;

import fightinggame.Gameplay;
import fightinggame.entity.Animation;
import fightinggame.entity.Character;
import fightinggame.entity.item.Item;
import fightinggame.entity.item.collectable.NonConsumeItem;

public class CoinItem extends NonConsumeItem {

    private int coinValue;

    public CoinItem(int id, String name, Animation animation,
            Character character, Gameplay gameplay, int amount, int coinValue) {
        super(id, name, animation, character, gameplay, amount);
        this.coinValue = coinValue;
        price = coinValue;
        description = "[" + name + "] Sell For " + coinValue + " AP.";
    }

    @Override
    public boolean use() {
        return false;
    }

    @Override
    public Item clone() {
        return new CoinItem(id, name, animation, character, gameplay, amount, coinValue);
    }

    @Override
    public int getXHitBox() {
        return position.getXPosition() + 20;
    }

    @Override
    public int getWidthHitBox() {
        return position.getWidth() - 40;
    }

    @Override
    public int getHeightHitBox() {
        return position.getHeight();
    }

    @Override
    public int getYHitBox() {
        return position.getYPosition();
    }

    public int getCoinValue() {
        return coinValue;
    }

    public void setCoinValue(int coinValue) {
        this.coinValue = coinValue;
    }

}
