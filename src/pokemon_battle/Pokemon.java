package pokemon_battle;

public class Pokemon {
	private String name;
	private PokemonType type;
	private int hp;
	private Attack[] attacks;
	
	public Pokemon(String name, int hp, Attack[] attacks, PokemonType type, int catchRate) {
		super();
		this.name = name;
		this.type = type;
		this.hp = hp;
		this.attacks = attacks;
	}
	
	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}
	
	public Attack[] getAttacks() {
		return attacks;
	}

	public void setAttacks(Attack[] attacks) {
		this.attacks = attacks;

	}
	
	public PokemonType getType() {
		return type;
	}
	
}
