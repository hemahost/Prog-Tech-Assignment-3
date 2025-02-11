/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mehem
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthGUI {
    private ArrayList<String> mazeFiles;
    private int currLevel = 0; 
    private int cntSolved = 0; 
    private long startTime; 
    private JFrame frame; 
    private HighScores highScores; 
    private String playerName; 

    public LabyrinthGUI() {
        try {
            highScores = new HighScores(); 
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "Database initialization failed.", e);
            JOptionPane.showMessageDialog(null, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        mainMenu();
    }

    private void saveProgress() {
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                int timeTaken = (int) ((System.currentTimeMillis() - startTime) / 1000);
                highScores.saveScore(playerName, cntSolved, timeTaken);
                System.out.println("Progress saved: " + playerName + ", " + cntSolved + " labyrinths, " + timeTaken + " seconds.");
            } catch (SQLException e) {
                Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "Failed to save progress.", e);
            }
        }
    }

    private boolean showExitConfirmation(Component parent) {
        Object[] options = {"Yes", "No"};
        int choice = JOptionPane.showOptionDialog(
            parent,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1]
        );

        return choice == JOptionPane.YES_OPTION;
    }

    private void mainMenu() {
        if (cntSolved > 0) saveProgress(); 

        JFrame menuFrame = new JFrame("Labyrinth Game - Main Menu");
        menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menuFrame.setSize(400, 300);
        menuFrame.setLayout(new BorderLayout());

        menuFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent ae) {
                if (showExitConfirmation(menuFrame)) {
                    saveProgress();
                    System.exit(0);
                }
            }
        });

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(ae -> {
            if (playerName(menuFrame)) {
                menuFrame.dispose();
                try {
                    startGame();
                } catch (IOException ex) {
                    Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "An error occurred while initializing the game.", ex);
                }
            }
        });

        JButton highScoresButton = new JButton("View High Scores");
        highScoresButton.setFont(new Font("Arial", Font.BOLD, 16));
        highScoresButton.addActionListener(e -> showHighScores());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(highScoresButton);

        menuFrame.add(buttonPanel, BorderLayout.CENTER);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    private boolean playerName(JFrame parentFrame) {
        while (true) {
            playerName = JOptionPane.showInputDialog(parentFrame, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
            if (playerName == null) {
                if (showExitConfirmation(parentFrame)) {
                    System.exit(0);
                }
            } else if (!playerName.trim().isEmpty()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Name cannot be empty. Please enter a valid name.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startGame() throws IOException {
        cntSolved = 0; 
        startTime = System.currentTimeMillis(); 
        loadMazeFiles("data/levels/"); 
        loadNextLevel();
    }

    private void loadMazeFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        
        if (files == null || files.length == 0) {
            throw new RuntimeException("No files found in directory: " + directoryPath);
        }

        mazeFiles = new ArrayList<>();
        for (File file : files) {
            mazeFiles.add(file.getPath()); 
        }
    }

    private void loadNextLevel() throws IOException {
        if (currLevel >= mazeFiles.size()) {
            int choice = JOptionPane.showOptionDialog(
                frame,
                "Congratulations! You've completed all levels. What would you like to do next?",
                "Game Complete",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Restart", "Main Menu", "Exit"},
                "Restart"
            );

            if (choice == 0) { 
                currLevel = 0;
                cntSolved = 0;
                frame.dispose();
                startGame();
            } else if (choice == 1) { 
                frame.dispose();
                cntSolved = 0;
                mainMenu();
            } else if (choice == 2) {
                System.exit(0);
            } else {
                System.exit(0);
            }
            return;
        }

        String filePath = mazeFiles.get(currLevel);
        Labyrinth maze = new Labyrinth(filePath);
        int cellSize = 40;
        int visionRange = 3;

        if (frame != null) {
            frame.dispose();
        }

        frame = new JFrame("Labyrinth Game - Level " + (currLevel + 1));
        GameEngine game = new GameEngine(maze, cellSize, visionRange, this);
        frame.add(game);
        frame.setSize(maze.getCols() * cellSize, maze.getRows() * cellSize);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (showExitConfirmation(frame)) {
                    System.exit(0);
                }
            }
        });

        frame.setResizable(false);
        frame.setVisible(true);
        currLevel++;
    }

    public long getStartTime() {
        return startTime;
    }

    public void next() {
        cntSolved++;
        try {
            loadNextLevel();
        } catch (IOException e) {
            Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "An error while loading the next level.", e);
        }
    }

    public void gameOver() {
        saveProgress();

        int choice = JOptionPane.showOptionDialog(
            frame,
            "You lost! What would you like to do?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Restart", "Main Menu"},
            "Restart"
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                currLevel = 0;
                cntSolved = 0;
                frame.dispose();
                startGame();
            } catch (IOException e) {
                Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "Failed to restart game.", e);
            }
        } else {
            frame.dispose();
            cntSolved = 0;
            mainMenu();
        }
    }

    private void showHighScores() {
        try {
            ArrayList<String> scores = highScores.getTopScores(10);
            StringBuilder highScoresText = new StringBuilder("Top 10 High Scores:\n");
            for (String score : scores) {
                highScoresText.append(score).append("\n");
            }

            JOptionPane.showMessageDialog(frame, highScoresText.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, "Failed to load high scores.", e);
        }
    }
}
