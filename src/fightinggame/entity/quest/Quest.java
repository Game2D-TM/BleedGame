package fightinggame.entity.quest;

import fightinggame.Gameplay;
import fightinggame.entity.Player;
import fightinggame.entity.reward.Reward;
import fightinggame.entity.state.QuestState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {

    private String id;
    private String name;
    private List<QuestRequired> requireds = new ArrayList<>();
    private Map<String, Reward> rewards = new HashMap<>();
    private QuestType type;
    private QuestState state = QuestState.NONE;
    private String description;
    private final Player player;
    private final Gameplay gameplay;

    public Quest(String id, String name, List<QuestRequired> requireds, Map<String, Reward> rewards,
            QuestType type, QuestState state, String description, Player player, Gameplay gameplay) {
        this.id = id;
        this.name = name;
        this.requireds = requireds;
        this.rewards = rewards;
        this.type = type;
        this.state = state;
        this.description = description;
        this.player = player;
        this.gameplay = gameplay;
    }

    public Quest(String id, String name, QuestType type, String description, Player player, Gameplay gameplay) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.player = player;
        this.gameplay = gameplay;
    }

    public Quest(String id, String name, QuestType type, QuestState state, String description, Player player, Gameplay gameplay) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.state = state;
        this.player = player;
        this.gameplay = gameplay;
    }

    public void tick() {
        if (!isComplete()) {
            if (requireds.size() > 0) {
                for (int i = 0; i < requireds.size(); i++) {
                    QuestRequired questRequired = requireds.get(i);
                    if (questRequired != null) {
                        if (!questRequired.isFinished()) {
                            questRequired.tick();
                        }
                    }
                }
            }
        }
    }

    public boolean isComplete() {
        if (requireds.isEmpty()) {
            return true;
        }
        for (QuestRequired questRequired : requireds) {
            if (!questRequired.isFinished()) {
                return false;
            }
        }
        state = QuestState.FINISHED;
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Map<String, Reward> rewards) {
        this.rewards = rewards;
    }

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
    }

    public QuestState getState() {
        return state;
    }

    public void setState(QuestState state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<QuestRequired> getRequireds() {
        return requireds;
    }

    public void setRequireds(List<QuestRequired> requireds) {
        this.requireds = requireds;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public Player getPlayer() {
        return player;
    }

}
