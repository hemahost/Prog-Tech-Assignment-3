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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameEngine extends JPanel {
    private final int FPS = 60; 
    private final int FRAME_TIME = 1000 / FPS;
    private final int size;
    private Player player;
    private Dragon dragon;
    private final Labyrinth maze;
    private Image playerImg;
    private Image wallImg;
    private Image floorImg;
    private Image doorImg;
    private Image dragonImg;
    private boolean gameOver = false;
    private final int vision;
    private final LabyrinthGUI gui;
    private Timer frameTimer; 
    private Timer dragonTimer; 

    public GameEngine(Labyrinth maze, int size, int vision, LabyrinthGUI gui) {
        this.maze = maze;
        this.size = size;
        this.vision = vision;
        this.gui = gui;
        
        loadImages();
        initialize();
        startTimers();
        addKeyListener(new KeyInputHandler());
        setFocusable(true);
    }

    private void loadImages() {
        playerImg = new ImageIcon("data/images/player.png").getImage();
        wallImg = new ImageIcon("data/images/brick.png").getImage();
        floorImg = new ImageIcon("data/images/floor.png").getImage();
        doorImg = new ImageIcon("data/images/door.png").getImage();
        dragonImg = new ImageIcon("data/images/dragon.png").getImage();
    }

    private void initialize() {
        player = new Player(1, maze.getMaze().length - 2, size, size, playerImg, vision);
        Random random = new Random();
        int dragonX, dragonY;
        do {
            dragonX = random.nextInt(maze.getCols());
            dragonY = random.nextInt(maze.getRows());
        } while (maze.isWall(dragonX, dragonY) || (dragonX == player.getX() && dragonY == player.getY()));
        dragon = new Dragon(dragonX, dragonY, size, size, dragonImg, maze);
    }

    private void startTimers() {
        frameTimer = new Timer(FRAME_TIME, new FrameUpdateListener());
        frameTimer.start();

        dragonTimer = new Timer(200, new DragonMoveListener());
        dragonTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGameElements(g);

        g.setColor(java.awt.Color.WHITE);
        g.drawString("FPS: " + FPS, 10, 20);
        int elapsedTime = (int) ((System.currentTimeMillis() - gui.getStartTime()) / 1000); 
        g.drawString("Time: " + elapsedTime + "s", 10, 40);
    }

    private void drawGameElements(Graphics g) {
        maze.draw(g, wallImg, floorImg, doorImg, size, player.getX(), player.getY(), vision);
        player.draw(g);
        if (Math.abs(dragon.getX() - player.getX()) <= vision &&
            Math.abs(dragon.getY() - player.getY()) <= vision) {
            dragon.draw(g);
        }
    }

    private void checkEscape() {
        if (maze.getMaze()[player.getY()][player.getX()] == 2) {
            gameOver = true;
            gui.next();
        }
    }

    private void checkCollDragon() {
        int dx = Math.abs(player.getX() - dragon.getX());
        int dy = Math.abs(player.getY() - dragon.getY());

        if (!gameOver && ((dx == 1 && dy == 0) || (dx == 0 && dy == 1))) {
            gameOver = true;
            gui.gameOver();
        }
    }

    class FrameUpdateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!gameOver) {
                repaint(); 
            }
        }
    }

    class DragonMoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!gameOver) {
                dragon.moveRandom();
                checkCollDragon();
            }
        }
    }

    class KeyInputHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ae) {
            if (!gameOver) {
                int dx = 0, dy = 0;

                switch (ae.getKeyCode()) {
                    case KeyEvent.VK_W -> dy = -1;
                    case KeyEvent.VK_S -> dy = 1;
                    case KeyEvent.VK_A -> dx = -1;
                    case KeyEvent.VK_D -> dx = 1;
                }

                player.move(dx, dy, maze.getMaze());
                checkCollDragon();
                checkEscape();
            }
        }
    }
}
