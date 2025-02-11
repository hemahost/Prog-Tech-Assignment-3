/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mehem
 */
import java.awt.Graphics;
import java.awt.Image;

public class Dark {
    private final int[][] maze;
    private final int rows;
    private final int cols;
    private final int vision;

    public Dark(int[][] maze, int vision) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.vision = vision;
    }

    public void draw(Graphics g, Image wallImg, Image floorImg, int size, int pX, int pY) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Image img = maze[y][x] == 1 ? wallImg : floorImg;

                if (Math.abs(x - pX) <= vision && Math.abs(y - pY) <= vision) {
                    g.drawImage(img, x * size, y * size, size, size, null);
                } else {
                    g.setColor(new java.awt.Color(0, 0, 0, 180));
                    g.fillRect(x * size, y * size, size, size);  
                }
            }
        }
    }
}
