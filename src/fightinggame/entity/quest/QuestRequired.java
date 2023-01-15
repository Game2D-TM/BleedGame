package fightinggame.entity.quest;

public abstract class QuestRequired {

    protected String name;
    protected boolean finished;
    protected final int amount;
    protected int currentAmount = 0;
    protected final Quest quest;

    public QuestRequired(String name, int amount, Quest quest) {
        this.name = name;
        this.quest = quest;
        this.amount = amount;
    }
    
    public abstract void tick();

    public void increaseCurrentAmount() {
        if (currentAmount < amount) {
            currentAmount++;
        }
    }

    public void checkIsFinished() {
        if (currentAmount == amount) {
            finished = true;
        }
    }

    public boolean isFinished() {
        if (!finished) {
            checkIsFinished();
        }
        return finished;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Quest getQuest() {
        return quest;
    }

    public int getAmount() {
        return amount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    @Override
    public String toString() {
        return name + " " + currentAmount + "/" + amount;
    }

}
