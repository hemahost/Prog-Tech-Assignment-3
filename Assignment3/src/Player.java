/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *  
 * @author mehem
 */
import java.awt.Image;

public class Player extends Sprite {
    private final int vision;

    public Player(int x, int y, int width, int height, Image image, int vision) {
        super(x, y, width, height, image);
        this.vision = vision;
    }

    public int getRange() {
        return vision;
    }

    public void move(int dx, int dy, int[][] maze) {
        int newX = x + dx;
        int newY = y + dy;

        if (newX >= 0 && newX < maze[0].length && newY >= 0 && newY < maze.length) {
            if (maze[newY][newX] == 0 || maze[newY][newX] == 2 ) { 
                x = newX;
                y = newY;
            }
        }
    }
}

