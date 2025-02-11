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

public class Labyrinth {
    private int[][] maze;
    private int rows;
    private int cols;

    public Labyrinth(String filePath) throws java.io.IOException {
        loadMazeFromFile(filePath);
    }
    public boolean isWall(int x, int y) {

        if (x < 0 || x >= maze[0].length || y < 0 || y >= maze.length) {
            return true; 
        } 
        
        return maze[y][x] == 1;
    }

    private void loadMazeFromFile(String filePath) throws java.io.IOException {
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(filePath));
        String line;

        int tempRows = 0;
        int tempCols = 0;

        while ((line = reader.readLine()) != null) {
            tempRows++;
            tempCols = Math.max(tempCols, line.length());
        }
        reader.close();

        rows = tempRows;
        cols = tempCols;
        maze = new int[rows][cols];
        
        reader = new java.io.BufferedReader(new java.io.FileReader(filePath));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                maze[row][col] = (line.charAt(col) == '0') ? 1 : 0;
            }
            row++;
        }
        reader.close();
        maze[0][cols - 1] = 2;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void draw(Graphics g, Image wallImage, Image floorImage, Image doorImg, int size, int pX, int pY, int vision) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Image img = floorImage;

                if (maze[y][x] == 1) {
                    img = wallImage;
                } else if (maze[y][x] == 2) {
                    img = doorImg;
                }
                
                if (Math.abs(x - pX) > vision || Math.abs(y - pY) > vision) {
                    g.setColor(new java.awt.Color(0, 0, 0, 180)); 
                    g.fillRect(x * size, y * size, size, size);
                } else {
                    g.drawImage(img, x * size, y * size, size, size, null);
                }
            }
        }
    }
}
