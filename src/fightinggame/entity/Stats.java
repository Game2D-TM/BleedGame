package fightinggame.entity;

import fightinggame.entity.Character;
import fightinggame.entity.enemy.Enemy;
import java.util.Random;

public class Stats {
    
    public static int NEXT_LEVEL_RATE = 100;

    private Character character;
    private int level = 1;
    private int levelUpCount = 0;
    private double levelExperience = 0;
    private double nextLevelExperience = 0;
    private int attackDamage;
    private int attackRange = 30;
    private int defenceDamage;
    private float defenceChange = 0.001f;
    private int bounceRange = 30;
    private int health;
    private int speed;
    private int levelUpPoint = 0;
    private int critDamage;
    private float critChange;
    private int jumpSpeed = 100;
    private float vely = 0;
    private int jumpFlySpeed = 4;
    private float dropSpeed = 0.08f; // 0.05

    public Stats(Character character, int level, double levelExperience, int attackDamage, int defenceDamage, 
            int health, int speed, int levelUpPoint, int critDamage, float critChange) {
        this.character = character;
        this.level = level;
        this.levelExperience = levelExperience;
        nextLevelExperience += level * NEXT_LEVEL_RATE;
        this.attackDamage = attackDamage;
        this.defenceDamage = defenceDamage;
        this.health = health;
        this.speed = speed;
        this.levelUpPoint = levelUpPoint;
        this.critDamage = critDamage;
        this.critChange = critChange;
    }

    public Stats(Character character, int attackDamage, int defenceDamage, int health, int speed, int critDamage, float critChange) {
        this.character = character;
        this.attackDamage = attackDamage;
        this.defenceDamage = defenceDamage;
        this.health = health;
        this.speed = speed;
        this.critDamage = critDamage;
        this.critChange = critChange;
    }
    
    public void addAttackDamage(int amount) {
        attackDamage += amount;
    }

    public int getHit(Stats attackerStats) {
        int receiveDamage = (attackerStats.getAttackDamageWithCrit());
        if (checkChangeSuccessDamage(defenceChange)) {
            receiveDamage -= defenceDamage;
        }
        int nHealth = this.health - receiveDamage;
        if (nHealth < 0) {
            nHealth = 0;
        }
        health = nHealth;
        return receiveDamage;
    }

    public int getHit(Stats attackerStats, int attackDamage) {
        int receiveDamage = (attackerStats.getAttackDamageWithCrit(attackDamage));
        if (checkChangeSuccessDamage(defenceChange)) {
            receiveDamage -= defenceDamage;
        }
        int nHealth = this.health - receiveDamage;
        if (nHealth < 0) {
            nHealth = 0;
        }
        health = nHealth;
        return receiveDamage;
    }

    public boolean checkChangeSuccessDamage(float change) {
        Random rand = new Random();
        float randNum = rand.nextFloat();
        if (randNum <= change) {
            return true;
        }
        return false;
    }

    public int getAttackDamageWithCrit(int attackDamage) {
        int attackWithCrit = attackDamage;
        if (checkChangeSuccessDamage(critChange)) {
            attackWithCrit += critDamage;
        }
        return attackWithCrit;
    }

    public int getAttackDamageWithCrit() {
        int attackWithCrit = attackDamage;
        if (checkChangeSuccessDamage(critChange)) {
            attackWithCrit += critDamage;
        }
        return attackWithCrit;
    }

    // need to code
    public void addExperience(double experience) {
        double nLevelExperience = levelExperience + experience;
        if (nextLevelExperience <= nLevelExperience) {
            levelUp();
            levelExperience = nLevelExperience - nextLevelExperience;
            nextLevelExperience += level * NEXT_LEVEL_RATE;
            while(true) {
                if(levelExperience >= nextLevelExperience) {
                    levelUp();
                    levelExperience -= nextLevelExperience;
                    nextLevelExperience += level * NEXT_LEVEL_RATE;
                } else {
                    break;
                }
            }
        } else {
            levelExperience = nLevelExperience;
        }
    }

    // need to code
    public void increaseLevelUpPoint() {
        if (levelUpCount < 5) {
            levelUpCount++;
        } else {
            levelUpCount = 0;
            levelUpPoint++;
        }
    }

    // need to code
    public void levelUp() {
        increaseLevelUpPoint();
        level += 1;
        if (character != null) {
            if (character instanceof Player) {
                if (level <= 10) {
                    attackDamage += 1;
                    if (health < character.getHealthBar().getMaxHealth()) {
                        health = character.getHealthBar().getMaxHealth();
                    }
                    health += 5;
                    character.getHealthBar().setMaxHealth(health);
                    defenceDamage += 1;
                    defenceChange += 0.005f;
                }
            } else {
                if (level <= 10) {
                    attackDamage += 15;
                    if (health < character.getHealthBar().getMaxHealth()) {
                        health = character.getHealthBar().getMaxHealth();
                    }
                    health += 50;
                    character.getHealthBar().setMaxHealth(health);
                    defenceDamage += 5;
                    defenceChange += 0.01f;
                    Enemy enemy = ((Enemy)character);
                    enemy.addExperience(50);
                    enemy.addPoint(10);
                } else {
                    attackDamage += 30;
                    if (health < character.getHealthBar().getMaxHealth()) {
                        health = character.getHealthBar().getMaxHealth();
                    }
                    health += 100;
                    character.getHealthBar().setMaxHealth(health);
                    defenceDamage += 10;
                    defenceChange += 0.02f;
                    Enemy enemy = ((Enemy)character);
                    enemy.addExperience(100);
                    enemy.addPoint(20);
                }
            }
        }
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLevelExperience() {
        return levelExperience;
    }

    public void setLevelExperience(double levelExperience) {
        this.levelExperience = levelExperience;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getDefenceDamage() {
        return defenceDamage;
    }

    public void setDefenceDamage(int defenceDamage) {
        this.defenceDamage = defenceDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLevelUpPoint() {
        return levelUpPoint;
    }

    public void setLevelUpPoint(int levelUpPoint) {
        this.levelUpPoint = levelUpPoint;
    }

    public int getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(int critDamage) {
        this.critDamage = critDamage;
    }

    public float getCritChange() {
        return critChange;
    }

    public void setCritChange(float critChange) {
        this.critChange = critChange;
    }

    public int getLevelUpCount() {
        return levelUpCount;
    }

    public void setLevelUpCount(int levelUpCount) {
        this.levelUpCount = levelUpCount;
    }

    public double getNextLevelExperience() {
        return nextLevelExperience;
    }

    public void setNextLevelExperience(double nextLevelExperience) {
        this.nextLevelExperience = nextLevelExperience;
    }

    public float getDefenceChange() {
        return defenceChange;
    }

    public void setDefenceChange(float defenceChange) {
        this.defenceChange = defenceChange;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public float getVely() {
        return vely;
    }

    public void setVely(float vely) {
        this.vely = vely;
    }

    public int getJumpFlySpeed() {
        return jumpFlySpeed;
    }

    public void setJumpFlySpeed(int jumpFlySpeed) {
        this.jumpFlySpeed = jumpFlySpeed;
    }

    public int getBounceRange() {
        return bounceRange;
    }

    public void setBounceRange(int bounceRange) {
        this.bounceRange = bounceRange;
    }

    public float getDropSpeed() {
        return dropSpeed;
    }

    public void setDropSpeed(float dropSpeed) {
        this.dropSpeed = dropSpeed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
    
}
