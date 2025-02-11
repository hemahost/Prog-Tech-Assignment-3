/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mehem
 */
import java.awt.Image;
import java.util.Random;

public class Dragon extends Sprite {
    private final Random random = new Random();
    private Direction currDirection; 
    private final Labyrinth labyrinth; 

    public enum Direction {
        NORTH(-1, 0), 
        EAST(0, 1),   
        SOUTH(1, 0), 
        WEST(0, -1);  

        private final int newx; 
        private final int newy; 

        Direction(int newy, int newx) {
            this.newx = newx;
            this.newy = newy;
        }

        public int getNewx() {
            return newx;
        }

        public int getNewy() {
            return newy;
        }
    }

    public Dragon(int x, int y, int width, int height, Image image, Labyrinth labyrinth) {
        super(x, y, width, height, image);
        this.labyrinth = labyrinth; 
        this.currDirection = getRandomDirection(); 
    }

    private Direction getRandomDirection() {
        Direction[] directions = Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    public void moveRandom() {
        int nextX = x + currDirection.getNewx();
        int nextY = y + currDirection.getNewy();

        if (!labyrinth.isWall(nextX, nextY)) {
            x = nextX;
            y = nextY;
        } else {
            currDirection = getRandomDirection();
        }
    }
}
