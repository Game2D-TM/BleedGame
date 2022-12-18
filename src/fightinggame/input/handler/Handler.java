package fightinggame.input.handler;

public abstract class Handler{
    private String name;
    
    public Handler(String name) {
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
