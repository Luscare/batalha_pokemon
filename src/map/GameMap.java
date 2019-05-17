package map;

import java.util.Random;

import map.Map.Direction;
import map.Map.Floor;
import pokemon_battle.Attack;
import pokemon_battle.PokemonType;
import pokemon_battle.Battle;
import pokemon_battle.Item;
import pokemon_battle.Player;
import pokemon_battle.Pokeball;
import pokemon_battle.Pokemon;
import Events.Controller;
import Events.Event;

public class GameMap extends Controller {
	private Player player, wildPokemon;
	private int stepsOnGrass;
	private int chanceWildBattle; // from 0 to 80
	private Battle currentWildBattle;
	
	private static final int DELAY_OF_EVENT = 1500;
	private static final int INITIAL_HP = 500;
	
	private class Walk extends Event {
		private boolean foundWildPokemon;
		private Floor whereWalked;
		private int actualX, actualY;
		
		public Walk(long eventTime) {
			super(eventTime);
			this.foundWildPokemon = false;
		}
		
		public void action() {
			int x = player.getX();
			int y = player.getY();
			actualX = x;
			actualY = y;
			whereWalked = Map.getFloor(x, y);
			switch (whereWalked) {
			case GRASS:
				if (stepsOnGrass < 5) {
					Random random = new Random();
					int randomNumber = random.nextInt(100) + 1;
					if (randomNumber <= chanceWildBattle) {
						stepsOnGrass = 0;
						chanceWildBattle = 0;
						foundWildPokemon = true;
						boolean wild = true;
						currentWildBattle = new Battle(player, wildPokemon, wild);					
					} else {
						stepsOnGrass++;
						chanceWildBattle = 20 * stepsOnGrass;
						walk();
						addEvent(new GameMap.Walk(
								System.currentTimeMillis() + DELAY_OF_EVENT));
					}
				}
				break;
				
			case GROUND:
				walk();
				addEvent(new GameMap.Walk(
						System.currentTimeMillis() + DELAY_OF_EVENT));
			}
		}
		
		private void walk() {
			int x = player.getX();
			int y = player.getY();
			Direction dir;
			do {
				dir = Map.randomDirection();
			} while (!possiblePath(dir, x, y));
			
			switch (dir) {
			case DOWN:
				player.setY(y + 1);
				break;
				
			case LEFT:
				player.setX(x - 1);
				break;
				
			case RIGHT:
				player.setX(x + 1);
				break;
				
			case UP:
				player.setY(y - 1);
			}
		}

		private boolean possiblePath(Direction dir, int x, int y) {
			if (x == 0 && dir == Direction.LEFT ||
				x == Map.getLength() - 1 && dir == Direction.RIGHT ||
				y == 0 && dir == Direction.UP ||
				y == Map.getWidth() - 1 && dir == Direction.DOWN) {
				return false;
			}
			return true;
		}

		public String description() {
			String msg = "";
			
			switch (whereWalked) {
			case GRASS:
				msg = player.getName() + " has walked on the grass..."
					  + (foundWildPokemon ? " And has found a Wild Pokemon!!\n" : "\n");
				break;
				
			case GROUND:
				msg = player.getName() + " has walked on the ground...\n";
			}
			Map.printMap(actualX, actualY);
			
			return msg;
		}
	}
	
	private void createPlayer() {
		// player
		Pokemon[] playerPokemons = new Pokemon[6];
		
		Attack[] attacksVenusaur = new Attack[4];
		attacksVenusaur[0] = new Attack("Vine Whip", PokemonType.GRASS, 35, 1);
		attacksVenusaur[1] = new Attack("Razor Leaf", PokemonType.GRASS, 55, 1);
		attacksVenusaur[2] = new Attack("Sweet Scent", PokemonType.NORMAL, 0, 0);
		attacksVenusaur[3] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
		playerPokemons[0] = new Pokemon("Venusaur", INITIAL_HP, attacksVenusaur, 
				PokemonType.GRASS, 45);
		
		Attack[] attacksCharizard = new Attack[4];
		attacksCharizard[0] = new Attack("Dragon Breath", PokemonType.DRAGON, 60, 0);
		attacksCharizard[1] = new Attack("Flame Thrower", PokemonType.FIRE, 95, 2);
		attacksCharizard[2] = new Attack("Steal Wing", PokemonType.STEEL, 70, 1);
		attacksCharizard[3] = new Attack("Iron Tail", PokemonType.STEEL, 1020, 3);
		playerPokemons[1] = new Pokemon("Charizard", INITIAL_HP, attacksCharizard, 
				PokemonType.FIRE, 45);
		
		Attack[] attacksButterfree = new Attack[3];
		attacksButterfree[0] = new Attack("Confusion", PokemonType.PSYCHIC, 50, 1);
		attacksButterfree[1] = new Attack("Gust", PokemonType.FLYING, 40, 0);
		attacksButterfree[2] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
		playerPokemons[2] = new Pokemon("Butterfree", INITIAL_HP, attacksButterfree, 
				PokemonType.BUG, 45);
		
		Attack[] attacksPidgeot = new Attack[3];
		attacksPidgeot[0] = new Attack("Fly", PokemonType.FLYING, 70, 2);
		attacksPidgeot[1] = new Attack("Peck", PokemonType.FLYING, 35, 1);
		attacksPidgeot[2] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
		playerPokemons[3] = new Pokemon("Pidgeot", INITIAL_HP, attacksPidgeot, 
				PokemonType.FLYING, 45);
		
		Attack[] attacksArbok = new Attack[4];
		attacksArbok[0] = new Attack("Wrap", PokemonType.NORMAL, 15, 0);
		attacksArbok[1] = new Attack("Acid", PokemonType.POISON, 40, 1);
		attacksArbok[2] = new Attack("Headbutt", PokemonType.NORMAL, 70, 2);
		attacksArbok[3] = new Attack("Poison Sting", PokemonType.POISON, 15, 0);
		playerPokemons[4] = new Pokemon("Arbok", INITIAL_HP, attacksArbok, 
				PokemonType.POISON, 90);
		
		Attack[] attacksJigglypuff = new Attack[4];
		attacksJigglypuff[0] = new Attack("Body Slum", PokemonType.NORMAL, 85, 2);
		attacksJigglypuff[1] = new Attack("Rollout", PokemonType.ROCK, 30, 1);
		attacksJigglypuff[2] = new Attack("Flamethrower", PokemonType.FIRE, 95, 2);
		attacksJigglypuff[3] = new Attack("Doubleslap", PokemonType.NORMAL, 15, 0);
		playerPokemons[5] = new Pokemon("Jigglypuff", INITIAL_HP, attacksJigglypuff, 
				PokemonType.FAIRY, 170);
		
		Item[] playerItems = new Item[2];
		playerItems[0] = new Item("HP Up", 100, 2);
		playerItems[1] = new Item("Health Wing", 150, 2);
		
		Pokeball[] player2Pokeballs = new Pokeball[3];
		player2Pokeballs[0] = new Pokeball("Poke ball", 1);
		player2Pokeballs[1] = new Pokeball("Great ball", 1.5);
		player2Pokeballs[2] = new Pokeball("Ultra ball", 2);
		
		player = new Player("Lucas Seiji", playerPokemons, playerItems, player2Pokeballs, false);
	}
	
	private void createWildPokemon() {
		// wild pokemon
		Pokemon[] wildPokemon = new Pokemon[1];
		
		Attack[] attacksPikachu = new Attack[4];
		attacksPikachu[0] = new Attack("Thunderbolt", PokemonType.ELECTRIC, 95, 1);
		attacksPikachu[1] = new Attack("Quick Attack", PokemonType.NORMAL, 40, 0);
		attacksPikachu[2] = new Attack("Iron Tail", PokemonType.STEEL, 100, 2);
		attacksPikachu[3] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
		wildPokemon[0] = new Pokemon("Pikachu", INITIAL_HP, attacksPikachu, 
				PokemonType.ELECTRIC, 190);
		
		Item[] wildPokemonItems = {};
		
		Pokeball[] wildPokPokeballs = {};
		
		this.wildPokemon = new Player("Wild Pokemon", wildPokemon, wildPokemonItems, 
				wildPokPokeballs, true);
	}
	
	public static void main(String[] args) {
		// find a wild battle in grass (exercise 2)
		GameMap gmap = new GameMap();
		gmap.createPlayer();
		gmap.createWildPokemon();
		Map.generateMap();
		long tm = System.currentTimeMillis();
		gmap.addEvent(gmap.new Walk(tm));
		gmap.run(); // starts walking
		// when finishes walking, finds a wild pokemon to fight 
		gmap.currentWildBattle.addEvent(gmap.currentWildBattle.
				new StartBattle(System.currentTimeMillis() + DELAY_OF_EVENT));
		gmap.currentWildBattle.run();
	}
}