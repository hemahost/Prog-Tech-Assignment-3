/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mehem
 */
import java.sql.*;
import java.util.ArrayList;

public class HighScores {
    private final Connection connection;
    private final PreparedStatement insertStatement;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/highscores";
    private static final String USER = "root";
    private static final String PASSWORD = "Maho90287";

    public HighScores() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        String insertQuery = "INSERT INTO HighScores (Name, LabyrinthsSolved, TimeTaken) VALUES (?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
    }

    public void saveScore(String name, int labyrinthsSolved, int timeTaken) throws SQLException {
    insertStatement.setString(1, name);
    insertStatement.setInt(2, labyrinthsSolved);
    insertStatement.setInt(3, timeTaken); 
    insertStatement.executeUpdate();
    }


    public ArrayList<String> getTopScores(int limit) throws SQLException {
        ArrayList<String> highScores = new ArrayList<>();
        String query = "SELECT Name, LabyrinthsSolved, TimeTaken FROM HighScores ORDER BY LabyrinthsSolved DESC, TimeTaken ASC LIMIT ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("Name");
            int score = resultSet.getInt("LabyrinthsSolved");
            int timeTaken = resultSet.getInt("TimeTaken");
            highScores.add(name + ": " + score + " labyrinths, " + timeTaken + " seconds");
        }
        return highScores;
    }
}

