package fightinggame.entity;

import fightinggame.input.handler.Handler;
import java.util.ArrayList;
import java.util.List;

public abstract class Item {
    private int id;
    private String name;
    private List<Ability> abilities;
    private int amount;
    private List<Handler> handlers = new ArrayList<>();

    public Item(int id, String name, List<Ability> abilities, int amount, List<Handler> handlers) {
        this.id = id;
        this.name = name;
        this.abilities = abilities;
        this.amount = amount;
        this.handlers = handlers;
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

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
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

    public void setHandlers(List<Handler> handlers) {
        this.handlers = handlers;
    }
    
    
}
