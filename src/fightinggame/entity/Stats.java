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
    private int defence;
    private int bounceRange = 30;
    private int health;
    private int energy = 0;
    private int speed;
    private int levelUpPoint = 0;
    private int critDamage;
    private float critChange;
    private int jumpSpeed = 100;
    private float vely = 0;
    private int jumpFlySpeed = 4;
    private float dropSpeed = 0.08f; // 0.05
    
    private int magicDamage;
    private int statusDef;
    private int evasionChance;
    

    public Stats(Character character, int level, double levelExperience, int attackDamage, int defence,
            int health, int energy, int speed, int levelUpPoint, int critDamage, float critChange) {
        this.character = character;
        this.level = level;
        this.levelExperience = levelExperience;
        nextLevelExperience += level * NEXT_LEVEL_RATE;
        this.attackDamage = attackDamage;
        this.defence = defence;
        this.health = health;
        this.energy = energy;
        this.speed = speed;
        this.levelUpPoint = levelUpPoint;
        this.critDamage = critDamage;
        this.critChange = critChange;
    }

    public Stats(Character character, int attackDamage, int defence,
            int health, int energy, int speed, int critDamage, float critChange) {
        this.character = character;
        this.attackDamage = attackDamage;
        this.defence = defence;
        this.health = health;
        this.energy = energy;
        this.speed = speed;
        this.critDamage = critDamage;
        this.critChange = critChange;
    }

    public void addAttackDamage(int amount) {
        attackDamage += amount;
        critDamage = attackDamage / 5;
    }
    public void minusAttackDamage(int amount) {
        attackDamage -= amount;
        critDamage = attackDamage / 5;
    }
    
    public void addCritChance(int amount) {
        critChange += (float)amount / 100;
    }

    public int getHit(Stats attackerStats) {
        int receiveDamage = (attackerStats.getAttackDamageWithCrit());
        receiveDamage -= defence;
        if(receiveDamage <= 0) {
            return 0;
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
        receiveDamage -= defence;
        if(receiveDamage <= 0) {
            return 0;
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
        if (critChange > 0) {
            if (checkChangeSuccessDamage(critChange)) {
                attackWithCrit += critDamage;
            }
        }
        return attackWithCrit;
    }

    public int getAttackDamageWithCrit() {
        int attackWithCrit = attackDamage;
        if (critChange > 0) {
            if (checkChangeSuccessDamage(critChange)) {
                attackWithCrit += critDamage;
            }
        }
        return attackWithCrit;
    }

    public boolean useEnergy(int energy) {
        if (energy <= 0) {
            return false;
        }
        int nEnergy = this.energy - energy;
        if (nEnergy > 0) {
            this.energy = nEnergy;
        } else {
            this.energy = 0;
        }
        return true;
    }

    public boolean addEnergy(int energy) {
        if (energy <= 0) {
            return false;
        }
        int nEnergy = this.energy + energy;
        if (nEnergy > character.getHealthBar().getMaxEnergy()) {
            this.energy = character.getHealthBar().getMaxEnergy();
        } else {
            this.energy = nEnergy;
        }
        return true;
    }

    // need to code
    public void addExperience(double experience) {
        if (character.isDeath()) {
            return;
        }
        double nLevelExperience = levelExperience + experience;
        if (nextLevelExperience <= nLevelExperience) {
            levelUp();
            levelExperience = nLevelExperience - nextLevelExperience;
            nextLevelExperience += level * NEXT_LEVEL_RATE;
            while (true) {
                if (levelExperience >= nextLevelExperience) {
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
        if (levelUpCount < 0) {
            levelUpCount++;
        } else {
            levelUpCount = 0;
            levelUpPoint += 5;
        }
    }

    // need to code
    public void levelUp() {
        if (character.isDeath()) {
            return;
        }
        increaseLevelUpPoint();
        level += 1;
        if (character != null) {
            if (character instanceof Player) {
                if (level <= 10) {
                    if (health < character.getHealthBar().getMaxHealth()) {
                        character.setHealingAmount(Math.abs(health - character.getHealthBar().getMaxHealth()));
                        health = character.getHealthBar().getMaxHealth();
                    }
                    attackDamage += 1;
                    defence += 1;
                }
            } else {
                if (level <= 10) {
                    attackDamage += 15;
                    if (health < character.getHealthBar().getMaxHealth()) {
                        character.setHealingAmount(Math.abs(health - character.getHealthBar().getMaxHealth()));
                        health = character.getHealthBar().getMaxHealth();
                    }
                    health += 50;
                    character.getHealthBar().setMaxHealth(health);
                    defence += 5;
                    Enemy enemy = ((Enemy) character);
                    enemy.addExperience(50);
                    enemy.addPoint(10);
                } else {
                    attackDamage += 30;
                    if (health < character.getHealthBar().getMaxHealth()) {
                        health = character.getHealthBar().getMaxHealth();
                    }
                    health += 100;
                    character.getHealthBar().setMaxHealth(health);
                    defence += 10;
                    Enemy enemy = ((Enemy) character);
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

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defenceDamage) {
        this.defence = defenceDamage;
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

    public String getCritChangeString() {
        return (int) (critChange * 100) + "%";
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

    public int getEnergy() {
        return energy;
    }

    public boolean haveEnergy() {
        if (energy <= 0) {
            return false;
        }
        return true;
    }

}
