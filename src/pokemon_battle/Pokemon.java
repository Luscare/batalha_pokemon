package pokemon_battle;

public class Pokemon {
	private String name;
	private PokemonType type;
	private int hp;
	
	
	
	private Attack[] attacks;
	private Attack attCurrent;
	private int attOrder;
	
	public Pokemon(String name, int hp, Attack[] attacks, PokemonType type, int catchRate) {
		super();
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.attacks = attacks;
		this.attCurrent = this.attacks[0];
		this.attOrder = 0;
	}
	
	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}
	
	public boolean isAlive() {
		return (hp > 0);
	}

	public void setHp(int hp) {
		if (hp < 0) {
			this.hp = 0;
		} else {
			this.hp = hp;
		}
	}

	public Attack[] getAttacks() {
		return attacks;
	}

	public Attack getAttCurrent() {
		return attCurrent;
	}

	public void setAttCurrent(Attack attCurrent) {
		this.attCurrent = attCurrent;
	}
	
	public int getAttOrder() {
		return attOrder;
	}

	public void setAttOrder(int attOrder) {
		this.attOrder = attOrder;
	}
	
	public PokemonType getType() {
		return type;
	}
	
}
