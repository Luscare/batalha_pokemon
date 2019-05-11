package pokemon_battle;

public class Player {
	private String name;
	
	private Pokemon[] pokemons;
	private Pokemon pokCurrent;
	private int pokOrder;
	
	private Item[] items;
	private Item itemCurrent;
	private int itemOrder;
	
	public Player(String name, Pokemon[] pokemons, Item[] items) {
		this.name = name;
		this.pokemons = pokemons;
		setInitialPokCurrent();
		this.items = items;
		setInitialItemCurrent();
	}

	public String getName() {
		return name;
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
}
