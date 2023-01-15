package fightinggame.entity.quest.type;

import fightinggame.entity.Player;
import fightinggame.entity.enemy.Enemy;
import fightinggame.entity.quest.Quest;
import fightinggame.entity.quest.QuestRequired;
import java.util.List;

public class EnemyRequired extends QuestRequired{
    
    private final String enemyName;
    
    public EnemyRequired(String enemyName, int amount, String name, Quest quest) {
        super(name, amount, quest);
        this.enemyName = enemyName;
    }
    
    public String getEnemyName() {
        return enemyName;
    }

    @Override
    public void tick() {
        Player player = quest.getPlayer();
        if(player != null) {
            List<Enemy> enemiesList = player.getEnemiesKilled();
            if(enemiesList != null && enemiesList.size() > 0) {
                for(int i = 0 ; i < enemiesList.size(); i++) {
                    Enemy enemyKilled = enemiesList.get(i);
                    if(enemyKilled != null) {
                        if(enemyKilled.getName().toLowerCase().contains(enemyName.toLowerCase())
                                || enemyKilled.getName().equalsIgnoreCase(enemyName)) {
                            player.getEnemiesKilled().remove(enemyKilled);
                            increaseCurrentAmount();
                        }
                    }
                }
            }
        }
    }
    
}
