package fightinggame.entity.ability.type;

import fightinggame.Gameplay;
import fightinggame.entity.Character;
import fightinggame.entity.SpriteSheet;
import fightinggame.entity.ability.type.skill.PassiveSkill;
import java.util.List;

public abstract class Harm extends PassiveSkill {

    protected int damage;
    
    public Harm(int damage, int id, String name , SpriteSheet skillIcon, Gameplay gameplay, Character character) {
        super(id, name, 0, 0, skillIcon, null, null, gameplay, character);
        this.damage = damage;
    }

    @Override
    public boolean execute(List<Character> characters) {
        return false;
    }
    
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    
}
