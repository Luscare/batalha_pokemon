package pokemon_battle;

import map.Map;

public class Player {
	private String name;
	private boolean isWildPokemon;
	
	private Pokemon[] pokemons;
	private Pokemon pokCurrent;
	private int pokOrder;
	
	private Item[] items;
	private Item itemCurrent;
	private int itemOrder;
	
	private Pokeball[] pokeballs;
	private Pokeball pokeballFree;
	private int pokeballFreeOrder;
	
	private int x,y;
	
	public Player(String name, Pokemon[] pokemons, Item[] items, Pokeball[] pokeballs,
			boolean isWildPokemon) {
		this.name = name;
		this.isWildPokemon = isWildPokemon;
		this.pokemons = pokemons;
		setInitialPokCurrent();
		this.items = items;
		setInitialItemCurrent();
		this.pokeballs = pokeballs;
		setNextPokeballFree();
		this.x = Map.getLength() / 2;
		this.y = Map.getWidth() / 2;
	}

	public String getName() {
		return name;
	}
	
	public boolean isWildPokemon() {
		return isWildPokemon;
	}

	public Pokemon[] getPokemons() {
		return pokemons;
	}

	public void setPokemons(Pokemon[] pokemons) {
		this.pokemons = pokemons;
	}
	
	public boolean hasPokemons() {
		boolean hasPokemons = false;
		
		for (Pokemon pokemon : this.pokemons) {
			if (pokemon != null) {
				hasPokemons = true;
				break;
			}
		}
		
		return hasPokemons;
	}

	public Pokemon getPokCurrent() {
		return pokCurrent;
	}

	public void setInitialPokCurrent() {
		if (hasPokemons()) {
			this.pokCurrent = this.pokemons[0];
			this.pokOrder = 0;
		} else {
			this.pokCurrent = null;
			this.pokOrder = -1;
		}
	}

	public void setPokCurrent(Pokemon pokCurrent) {
		this.pokCurrent = pokCurrent;
	}

	public int getPokOrder() {
		return pokOrder;
	}

	public void setPokOrder(int pokOrder) {
		this.pokOrder = pokOrder;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}
	
	public boolean hasItens() {
		boolean hasItens = false;
		
		for (Item item : this.items) {
			if (item.getQuantity() > 0) {
				hasItens = true;
				break;
			}
		}
		
		return hasItens;
	}

	public Item getItemCurrent() {
		return itemCurrent;
	}

	public void setInitialItemCurrent() {
		if (hasItens()) {
			this.itemCurrent = this.items[0];
			this.itemOrder = 0;
		} else {
			this.itemCurrent = null;
			this.itemOrder = -1;
		}
	}

	public void setItemCurrent(Item itemCurrent) {
		this.itemCurrent = itemCurrent;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}
	
	public Pokeball[] getPokeballs() {
		return pokeballs;
	}

	public void setPokeballs(Pokeball[] pokeballs) {
		this.pokeballs = pokeballs;
	}
	
	public boolean hasPokeballs() {
		boolean hasPokeballs = false;
		
		for (Pokeball pb : this.pokeballs) {
			if (pb != null) {
				hasPokeballs = true;
				break;
			}
		}
		
		return hasPokeballs;
	}

	public Pokeball getPokeballFree() {
		return pokeballFree;
	}
	
	public void setNextPokeballFree() {
		Pokeball pb = null;
		pokeballFree = null;
		pokeballFreeOrder = -1;
		boolean hasPokeballs = hasPokeballs();
		for (int i = 0; hasPokeballs && i < pokeballs.length; i++) {
			pb = pokeballs[i];
			if (!pb.hasWildStored()) {
				pokeballFree = pb;
				pokeballFreeOrder = i;
				break;
			}
		}
	}

	public void setPokeballFree(Pokeball pokeballFree) {
		this.pokeballFree = pokeballFree;
	}

	public int getPokeballFreeOrder() {
		return pokeballFreeOrder;
	}

	public void setPokeballFreeOrder(int pokeballFreeOrder) {
		this.pokeballFreeOrder = pokeballFreeOrder;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
