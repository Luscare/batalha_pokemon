package pokemon_battle;

import Events.Controller;
import Events.Event;

public class Battle extends Controller {
	private Player player1, player2;

	private static final int DELAY_OF_EVENT = 1500;
	private static final int INITIAL_HP = 500;
		
	public Battle(Player player1, Player player2) {
		this.player1 = player1;
		this.player2 = player2;
	}
		
	private class AttackWithCurrent extends Event {
		private Attack attackerChosenAttack;
		private Player attacker, attacked;
		private boolean dead;
		private double multiplier;
		
		public AttackWithCurrent(long eventTime, Attack attack, Player attacker, Player attacked) {
			super(eventTime);
			attackerChosenAttack = attack;
			this.attacker = attacker;
			this.attacked = attacked;
		}
		
		public void action() {
			Item attackedCurrentItem = attacked.getItemCurrent();
			Pokemon attackerCurrentPok = attacker.getPokCurrent();
			Pokemon attackedCurrentPok = attacked.getPokCurrent();
			Attack attakedCurrentAttack = attackedCurrentPok.getAttCurrent();
			
			int attackedCurrHp = attacked.getPokCurrent().getHp();
			multiplier = TypeChart.getMultiplier(attackerChosenAttack.getType(), attackedCurrentPok.getType());
			
			int damage = (int) (attackerChosenAttack.getDan() * multiplier);
			attacked.getPokCurrent().setHp(attackedCurrHp - damage);
			
			if (attacked.getPokCurrent().getHp() == 0)
				dead = true;
			
			int proxOrderAtt = (attackerCurrentPok.getAttOrder() + 1) % attackerCurrentPok.getAttacks().length;
			attackerCurrentPok.setAttOrder(proxOrderAtt);
			attackerCurrentPok.setAttCurrent(attackerCurrentPok.getAttacks()[proxOrderAtt]);
			
			if (attackedCurrentPok.isAlive()) {
				addEvent(new Battle.AttackWithCurrent(
						System.currentTimeMillis() + DELAY_OF_EVENT,
						attakedCurrentAttack, attacked, attacker));
				
			} else {
				if (attacked.hasItens()) {
					// use item
					addEvent(new Battle.UseItem(
							System.currentTimeMillis() + DELAY_OF_EVENT,
							attackedCurrentItem, attacked, attacker));
				} else {
					// try to change pokemon
					attacked.setPokOrder(attacked.getPokOrder() + 1);
					boolean pokemonsAreGone = (attacked.getPokOrder() == attacked.getPokemons().length);
					if (pokemonsAreGone) {
						addEvent(new Battle.RunAway(
								System.currentTimeMillis() + DELAY_OF_EVENT,
								pokemonsAreGone, false, attacked, attacker));
					} else {
						Pokemon newCurrent = attacked.getPokemons()[attacked.getPokOrder()];
						addEvent(new Battle.ChangeCurrentPokemon(
								System.currentTimeMillis() + DELAY_OF_EVENT,
								newCurrent, attacked, attacker));
					}
				}
			}
		}
		
		public String description() {
			String descr = null;
			descr = (attacker.getName() + "'s turn: " + "Pokemon " + attacker.getPokCurrent().getName() 
					+ " attacks (" + attackerChosenAttack.getName() + " with multiplier " + multiplier
					+ ")! " + attacked.getName() + "'s Pokemon " + attacked.getPokCurrent().getName()
					+ " actual HP: " + attacked.getPokCurrent().getHp() + "."
					+ (dead ? " Pokemon " + attacked.getPokCurrent().getName() + " is dead." : ""));
			return descr;
		}
	}
	
		private class ChangeCurrentPokemon extends Event {
			private Pokemon newCurrent, previous;
			private Player changer, next;
			
			public ChangeCurrentPokemon(long eventTime, Pokemon nc, Player changer, Player next) {
				super(eventTime);
				this.changer = changer;
				this.previous = changer.getPokCurrent();
				this.newCurrent = nc;
				this.next = next;
			}
			
			public void action() {
				changer.setPokCurrent(newCurrent);
				
				addEvent(new Battle.AttackWithCurrent(
						System.currentTimeMillis() + DELAY_OF_EVENT,
						next.getPokCurrent().getAttCurrent(),
						next, changer));
			}
			
			public String description() {
				return (changer.getName() + "'s turn: Pokemon " + previous.getName() + " changed to " + 
						newCurrent.getName());
			}
		}
		
		private class UseItem extends Event {
			private Item item;
			private Player user, next;
			
			public UseItem(long eventTime, Item item, Player user, Player next) {
				super(eventTime);
				this.item = item;
				this.user = user;
				this.next = next;
			}
			
			public void action() {
				int actualPokHP = user.getPokCurrent().getHp();
				user.getPokCurrent().setHp(actualPokHP + item.getHpCure());
				item.takeOff();

				if (item.getQuantity() == 0) {
					user.setItemOrder(user.getItemOrder() + 1);
					if (user.getItemOrder() != user.getItems().length) {
						user.setItemCurrent(user.getItems()[user.getItemOrder()]);
					}
				}
				
				addEvent(new Battle.AttackWithCurrent(
						System.currentTimeMillis() + DELAY_OF_EVENT,
						next.getPokCurrent().getAttCurrent(),
						next, user));
			}
			
			public String description() {
				return (user.getName() + "'s turn: Pokemon " + user.getPokCurrent().getName() + " earned " + 
						item.getHpCure() + " HP points (" + user.getPokCurrent().getHp() + " total HP " +
						"points), using " + item.getName() + " item.");
			}
		}
		
		private class RunAway extends Event {
			private boolean pokemonsAreGone;
			private Player loser, winner;
			
			public RunAway(long eventTime, boolean pokemonsAreGone, boolean hasBeenCaught,
					Player loser, Player winner) {
				super(eventTime);
				this.loser = loser;
				this.winner = winner;
				this.pokemonsAreGone = pokemonsAreGone;
			}
			
			public void action() {
				// no events added anymore, the battle is finished!
			}
			
			public String description() {
				return (loser.getName() + "'s turn: He has fled of the battle... "
						+ (pokemonsAreGone ? "His/her pokemons are gone. " : "")
						+ "Player " + winner.getName() + " has won!!! :-)");
			}
		}
		
		public class StartBattle extends Event {
			private boolean everybodyHasPokemons;
			
			public StartBattle(long eventTime) {
				super(eventTime);
			}
			
			public void action() {
				prepareBattle();
				everybodyHasPokemons = (player1.getPokCurrent() != null && 
						player2.getPokCurrent() != null);
				if (everybodyHasPokemons) {
					Pokemon p1PokCurr = player1.getPokCurrent();
					Pokemon p2PokCurr = player2.getPokCurrent();
					Attack p1AttCurr = p1PokCurr.getAttCurrent();
					Attack p2AttCurr = p2PokCurr.getAttCurrent();
					
					// if the priorities are equals, the player1 is the first to attack
					if (p1AttCurr.getPriority() <= p2AttCurr.getPriority()) {
						addEvent(new Battle.AttackWithCurrent(
								System.currentTimeMillis() + DELAY_OF_EVENT,
								p1AttCurr, player1, player2));
					} else {
						addEvent(new Battle.AttackWithCurrent(
								System.currentTimeMillis() + DELAY_OF_EVENT,
								p2AttCurr, player2, player1));
					}
				}
			}
			
			public String description() {
				if (everybodyHasPokemons) {
					return "The battle has started!";
				} else {
					return "There are players without pokemons. The battle couldn't be started... :-(";
				}
			}
		}
		
		private void prepareBattle() {
			// player 1
			Pokemon[] player1Pokemons = new Pokemon[6];
			
			Attack[] attacksBulbasaur = new Attack[4];
			attacksBulbasaur[0] = new Attack("Solar Beam", PokemonType.GRASS, 120, 3);
			attacksBulbasaur[1] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
			attacksBulbasaur[2] = new Attack("Petal Dance", PokemonType.GRASS, 70, 2);
			attacksBulbasaur[3] = new Attack("Razor Leaf", PokemonType.GRASS, 55, 1);
			player1Pokemons[0] = new Pokemon("Bulbasaur", INITIAL_HP, attacksBulbasaur,
					PokemonType.GRASS, 45);
			
			Attack[] attacksCharmander = new Attack[4];
			attacksCharmander[0] = new Attack("Ember", PokemonType.FIRE, 40, 2);
			attacksCharmander[1] = new Attack("Flame Thrower", PokemonType.FIRE, 95, 2);
			attacksCharmander[2] = new Attack("Metal Claw", PokemonType.STEEL, 50, 1);
			attacksCharmander[3] = new Attack("Rage", PokemonType.NORMAL, 20, 0);
			player1Pokemons[1] = new Pokemon("Charmander", INITIAL_HP, attacksCharmander, 
					PokemonType.FIRE, 45);
			
			Attack[] attacksSandslash = new Attack[3];
			attacksSandslash[0] = new Attack("Swift", PokemonType.NORMAL, 60, 1);
			attacksSandslash[1] = new Attack("Fury Swipes", PokemonType.NORMAL, 35, 0);
			attacksSandslash[2] = new Attack("Slash", PokemonType.NORMAL, 70, 1);
			player1Pokemons[2] = new Pokemon("Sandslash", INITIAL_HP, attacksSandslash, 
					PokemonType.GROUND, 90);
			
			Attack[] attacksPidgey = new Attack[1];
			attacksPidgey[0] = new Attack("Gust", PokemonType.FLYING, 40, 0);
			player1Pokemons[3] = new Pokemon("Pidgey", INITIAL_HP, attacksPidgey, 
					PokemonType.FLYING, 255);
			
			Attack[] attacksPikachu = new Attack[4];
			attacksPikachu[0] = new Attack("Thunderbolt", PokemonType.ELECTRIC, 95, 1);
			attacksPikachu[1] = new Attack("Quick Attack", PokemonType.NORMAL, 40, 0);
			attacksPikachu[2] = new Attack("Iron Tail", PokemonType.STEEL, 100, 2);
			attacksPikachu[3] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
			player1Pokemons[4] = new Pokemon("Pikachu", INITIAL_HP, attacksPikachu, 
					PokemonType.ELECTRIC, 190);
			
			Attack[] attacksSquirtle = new Attack[4];
			attacksSquirtle[0] = new Attack("Bubble", PokemonType.WATER, 20, 0);
			attacksSquirtle[1] = new Attack("Hydro Pump", PokemonType.WATER, 120, 2);
			attacksSquirtle[2] = new Attack("Ice Beam", PokemonType.ICE, 95, 1);
			attacksSquirtle[3] = new Attack("Skull Bash", PokemonType.NORMAL, 100, 1);
			player1Pokemons[5] = new Pokemon("Squirtle", INITIAL_HP, attacksSquirtle, 
					PokemonType.WATER, 45);
			
			Item[] player1Items = new Item[2];
			player1Items[0] = new Item("HP Up", 100, 3);
			player1Items[1] = new Item("Health Wing", 150, 1);
			
			
			player1 = new Player("Frito", player1Pokemons, player1Items);
			
			// player 2
			Pokemon[] player2Pokemons = new Pokemon[6];
			
			Attack[] attacksVenusaur = new Attack[4];
			attacksVenusaur[0] = new Attack("Vine Whip", PokemonType.GRASS, 35, 1);
			attacksVenusaur[1] = new Attack("Razor Leaf", PokemonType.GRASS, 55, 1);
			attacksVenusaur[2] = new Attack("Sweet Scent", PokemonType.NORMAL, 0, 0);
			attacksVenusaur[3] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
			player2Pokemons[0] = new Pokemon("Venusaur", INITIAL_HP, attacksVenusaur, 
					PokemonType.GRASS, 45);
			
			Attack[] attacksCharizard = new Attack[4];
			attacksCharizard[0] = new Attack("Dragon Breath", PokemonType.DRAGON, 60, 0);
			attacksCharizard[1] = new Attack("Flame Thrower", PokemonType.FIRE, 95, 2);
			attacksCharizard[2] = new Attack("Steal Wing", PokemonType.STEEL, 70, 1);
			attacksCharizard[3] = new Attack("Iron Tail", PokemonType.STEEL, 1020, 3);
			player2Pokemons[1] = new Pokemon("Charizard", INITIAL_HP, attacksCharizard, 
					PokemonType.FIRE, 45);
			
			Attack[] attacksButterfree = new Attack[3];
			attacksButterfree[0] = new Attack("Confusion", PokemonType.PSYCHIC, 50, 1);
			attacksButterfree[1] = new Attack("Gust", PokemonType.FLYING, 40, 0);
			attacksButterfree[2] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
			player2Pokemons[2] = new Pokemon("Butterfree", INITIAL_HP, attacksButterfree, 
					PokemonType.BUG, 45);
			
			Attack[] attacksPidgeot = new Attack[3];
			attacksPidgeot[0] = new Attack("Fly", PokemonType.FLYING, 70, 2);
			attacksPidgeot[1] = new Attack("Peck", PokemonType.FLYING, 35, 1);
			attacksPidgeot[2] = new Attack("Tackle", PokemonType.NORMAL, 35, 0);
			player2Pokemons[3] = new Pokemon("Pidgeot", INITIAL_HP, attacksPidgeot, 
					PokemonType.FLYING, 45);
			
			Attack[] attacksArbok = new Attack[4];
			attacksArbok[0] = new Attack("Wrap", PokemonType.NORMAL, 15, 0);
			attacksArbok[1] = new Attack("Acid", PokemonType.POISON, 40, 1);
			attacksArbok[2] = new Attack("Headbutt", PokemonType.NORMAL, 70, 2);
			attacksArbok[3] = new Attack("Poison Sting", PokemonType.POISON, 15, 0);
			player2Pokemons[4] = new Pokemon("Arbok", INITIAL_HP, attacksArbok, 
					PokemonType.POISON, 90);
			
			Attack[] attacksJigglypuff = new Attack[4];
			attacksJigglypuff[0] = new Attack("Body Slum", PokemonType.NORMAL, 85, 2);
			attacksJigglypuff[1] = new Attack("Rollout", PokemonType.ROCK, 30, 1);
			attacksJigglypuff[2] = new Attack("Flamethrower", PokemonType.FIRE, 95, 2);
			attacksJigglypuff[3] = new Attack("Doubleslap", PokemonType.NORMAL, 15, 0);
			player2Pokemons[5] = new Pokemon("Jigglypuff", INITIAL_HP, attacksJigglypuff, 
					PokemonType.FAIRY, 170);
			
			Item[] player2Items = new Item[2];
			player2Items[0] = new Item("HP Up", 100, 2);
			player2Items[1] = new Item("Health Wing", 150, 2);
			
			
			player2 = new Player("Lucas Seiji", player2Pokemons, player2Items);
			
		}
		
		public static void main(String[] args) {
			// for testing a battle (exercise 1)
			Player player1 = null;
			Player player2 = null;
			Battle newBattle = new Battle(player1, player2);
			long tm = System.currentTimeMillis();
			newBattle.addEvent(newBattle.new StartBattle(tm));
			newBattle.run();
		}
	
}
