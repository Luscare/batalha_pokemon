package map;

import java.util.Random;


public class Map {
	private final static int LENGTH = 10;
	private final static int WIDTH = 10;
	
	private static int[][] map = new int[LENGTH][WIDTH];

	public static void generateMap() {
		Random random = new Random();
		int randomNumber = 0;
		for (int i = 0; i < Map.LENGTH; i++) {
			for (int j = 0; j < Map.WIDTH; j++) {
				randomNumber = random.nextInt(2);
				map[i][j] = randomNumber;
			}
		}
	}
	
	public static Direction randomDirection() {
		Random random = new Random();
		int randomNumber = random.nextInt(4);
		return Direction.values()[randomNumber];
	}
	
	public static Floor getFloor(int x, int y) {
		return Floor.values()[map[x][y]];
	}
	
	public static void printMap(int xPlayer, int yPlayer) {
		for (int i = 0; i < Map.LENGTH; i++) {
			for (int j = 0; j < Map.WIDTH; j++) {
				if (i == xPlayer && j == yPlayer) {
					System.out.print("P ");
				} else {
					System.out.print(map[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
	
	public static int getLength() {
		return LENGTH;
	}

	public static int getWidth() {
		return WIDTH;
	}
	
	public enum Direction {
		UP, RIGHT, DOWN, LEFT
	}

	public enum Floor {
		GROUND, GRASS
	}
}
