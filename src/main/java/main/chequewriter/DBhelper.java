package main.chequewriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class DBhelper {
    public static String DBPATH="CHEQUE.DB";
    public static void addCoordinatesToSQLite(Map<String, Object> coordinates) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+DBPATH)) {
            // Create the coordinates table with specifiic columns if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS coordinates (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "PROFILE_NAME TEXT, " +
                    "DATEX REAL, " +
                    "DATEY REAL, " +
                    "PAYEEX REAL, " +
                    "PAYEEY REAL, " +
                    "DIGITSX REAL, " +
                    "DIGITSY REAL, " +
                    "WORDX REAL, " +
                    "WORDSY REAL)";
            conn.createStatement().executeUpdate(createTableSQL);

            // Insert coordinates into the table
            String insertSQL = "INSERT INTO coordinates (PROFILE_NAME, DATEX, DATEY, PAYEEX, PAYEEY, DIGITSX, DIGITSY, WORDX, WORDSY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                // Extract coordinates from the map
                String profileName = (String) coordinates.get("name");

                // Extract coordinate values
                Map<String, Double> date = (Map<String, Double>) coordinates.get("date");
                Map<String, Double> payee = (Map<String, Double>) coordinates.get("payee");
                Map<String, Double> digits = (Map<String, Double>) coordinates.get("digits");
                Map<String, Double> words = (Map<String, Double>) coordinates.get("words");

                // Set values from the coordinate maps
                pstmt.setString(1, profileName);
                pstmt.setDouble(2, date.get("x"));
                pstmt.setDouble(3, date.get("y"));
                pstmt.setDouble(4, payee.get("x"));
                pstmt.setDouble(5, payee.get("y"));
                pstmt.setDouble(6, digits.get("x"));
                pstmt.setDouble(7, digits.get("y"));
                pstmt.setDouble(8, words.get("x"));
                pstmt.setDouble(9, words.get("y"));

                pstmt.executeUpdate();
                System.out.println("Coordinates inserted successfully.");
            } catch (SQLException e) {
                System.err.println("Error inserting coordinates: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    public static List<String> getProfileNamesFromSQLite() {
        List<String> profileNames = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+DBPATH)) {
            // SQL query to retrieve profile names from the database
            String selectSQL = "SELECT PROFILE_NAME FROM coordinates";

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
                while (rs.next()) {
                    // Retrieve profile name from the result set
                    String profileName = rs.getString("PROFILE_NAME");
                    profileNames.add(profileName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving profile names from the database: " + e.getMessage());

        }

        return profileNames;
    }

    public static Map<String, Object> getCoordinatesByProfileName(String profileName) {
        System.out.println(profileName);
        Map<String, Object> coordinates = new HashMap<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+DBPATH)) {
            // SQL query to retrieve coordinates based on profile name
            String selectSQL = "SELECT * FROM coordinates WHERE PROFILE_NAME = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setString(1, profileName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Retrieve coordinate values from the result set
                        double dateX = rs.getDouble("DATEX");
                        double dateY = rs.getDouble("DATEY");
                        double payeeX = rs.getDouble("PAYEEX");
                        double payeeY = rs.getDouble("PAYEEY");
                        double digitsX = rs.getDouble("DIGITSX");
                        double digitsY = rs.getDouble("DIGITSY");
                        double wordsX = rs.getDouble("WORDX");
                        double wordsY = rs.getDouble("WORDSY");

                        // Populate the coordinates map
                        coordinates.put("name", profileName);
                        coordinates.put("date", Map.of("x", dateX, "y", dateY));
                        coordinates.put("payee", Map.of("x", payeeX, "y", payeeY));
                        coordinates.put("digits", Map.of("x", digitsX, "y", digitsY));
                        coordinates.put("words", Map.of("x", wordsX, "y", wordsY));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving coordinates by profile name from the database: " + e.getMessage());
        }
        System.out.println(coordinates);
        return coordinates;
    }


    public static void addOtherSettings(Map<String, String> othersettings) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+DBPATH)) {
            // Create the coordinates table with specific columns if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS otherSettings (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "CURRENCY VARCHAR(100), CENTS VARCHAR(20), FONT VARCHAR(100), FONTSIZE VARCHAR(2), DIRECTORY TEXT);";
            conn.createStatement().executeUpdate(createTableSQL);

            // Check if record with ID = 1 exists
            String checkExistenceSQL = "SELECT 1 FROM otherSettings WHERE id = 1";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkExistenceSQL)) {
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    // Insert new data into the table when ID is not 1
                    String insertSQL = "INSERT INTO otherSettings (CURRENCY, CENTS, FONT, FONTSIZE, DIRECTORY) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                        insertStmt.setString(1, othersettings.get("CURRENCY"));
                        insertStmt.setString(2, othersettings.get("CENTS"));
                        insertStmt.setString(3, othersettings.get("FONTNAME"));
                        insertStmt.setString(4, othersettings.get("FONTSIZE"));
                        insertStmt.setString(5, othersettings.get("DIRECTORY"));
                        insertStmt.executeUpdate();
                    }
                } else {
                    // Modify existing data in the table when ID is 1
                    String updateSQL = "UPDATE otherSettings SET CURRENCY = ?, CENTS = ?, FONT = ?, FONTSIZE = ?, DIRECTORY = ? WHERE id = 1";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                        updateStmt.setString(1, othersettings.get("CURRENCY"));
                        updateStmt.setString(2, othersettings.get("CENTS"));
                        updateStmt.setString(3, othersettings.get("FONTNAME"));
                        updateStmt.setString(4, othersettings.get("FONTSIZE"));
                        updateStmt.setString(5, othersettings.get("DIRECTORY"));
                        updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Map<String, String> getOtherSettings() {
        Map<String, String> otherSettings = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:"+DBPATH)) {
            String selectSQL = "SELECT * FROM otherSettings WHERE id = 1";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        // Retrieve data from the ResultSet and put it into the map
                        otherSettings.put("CURRENCY", rs.getString("CURRENCY"));
                        otherSettings.put("CENTS", rs.getString("CENTS"));
                        otherSettings.put("FONTNAME", rs.getString("FONT"));
                        otherSettings.put("FONTSIZE", rs.getString("FONTSIZE"));
                        otherSettings.put("DIRECTORY", rs.getString("DIRECTORY"));
                    }
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("no such table")) {
                System.err.println("Table 'otherSettings' does not exist in the database.");
            } else {
                e.printStackTrace(); // Handle or log the exception
            }
        }
        return otherSettings;
    }


}
