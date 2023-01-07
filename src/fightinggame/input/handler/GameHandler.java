package fightinggame.input.handler;

public abstract class GameHandler{
    protected String name;
    
    public GameHandler(String name) {
        this.name = name;
    }

    public abstract void tick();
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
