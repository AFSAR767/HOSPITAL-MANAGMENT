package hms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DBConnection {
    public Connection c;
    public Statement s;

    public DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to MySQL server first (no specific database)
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            s = c.createStatement();

            // Auto-create database and tables if they don't exist
            s.executeUpdate("CREATE DATABASE IF NOT EXISTS hospital_db");
            s.executeUpdate("USE hospital_db");
            createTables();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Error: \n" + e.getMessage() +
                    "\n\nPlease ensure your local MySQL server (XAMPP/WAMP/Workbench) is running on port 3306.",
                    "MySQL Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTables() throws Exception {
        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS admin (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, password VARCHAR(50) NOT NULL)");
        s.executeUpdate("INSERT IGNORE INTO admin (username, password) VALUES ('admin', 'admin123')");

        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS patients (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, age INT, gender VARCHAR(10), phone VARCHAR(20), address TEXT)");

        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS doctors (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100) NOT NULL, specialization VARCHAR(100), availability VARCHAR(100))");

        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS appointments (id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT, doctor_id INT, appointment_date DATE, appointment_time TIME, FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE, FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE)");

        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS medical_history (id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT, diagnosis VARCHAR(255), treatment TEXT, prescription TEXT, date_recorded TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE)");

        s.executeUpdate(
                "CREATE TABLE IF NOT EXISTS billing (id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT, consultation_fee DOUBLE(10,2) DEFAULT 0.0, other_charges DOUBLE(10,2) DEFAULT 0.0, total DOUBLE(10,2) DEFAULT 0.0, FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE)");
    }
}
