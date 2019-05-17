package pokemon_battle;

public class Pokeball {
	private String name;
	private double rate;
	private Pokemon wildPokemon;
	
	public Pokeball(String name, double rate) {
		this.name = name;
		this.rate = rate;
	}
	
	public String getName() {
		return name;
	}
	
	public double getRate() {
		return rate;
	}
	
	public Pokemon getWildPokemon() {
		return wildPokemon;
	}
	
	public void setWildPokemon(Pokemon wildPokemon) {
		this.wildPokemon = wildPokemon;
	}
	
	public boolean hasWildStored() {
		return (wildPokemon != null);
	}
	
}
