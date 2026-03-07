package hms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JTextField tfUsername;
    JPasswordField pfPassword;
    JButton btnLogin, btnCancel;

    public Login() {
        setTitle("Hospital Management System - Login");
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(40, 40, 100, 30);
        add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(150, 40, 150, 30);
        add(tfUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(40, 90, 100, 30);
        add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 90, 150, 30);
        add(pfPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(40, 150, 120, 30);
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        add(btnLogin);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(180, 150, 120, 30);
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.addActionListener(this);
        add(btnCancel);

        setSize(360, 250);
        setLocation(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnLogin) {
            try {
                DBConnection conn = new DBConnection();
                if (conn.s == null) {
                    return; // Prevent NullPointerException if DB connection failed
                }

                String username = tfUsername.getText();
                String password = new String(pfPassword.getPassword());

                String query = "SELECT * FROM admin WHERE username='" + username + "' AND password='" + password + "'";
                ResultSet rs = conn.s.executeQuery(query);

                if (rs.next()) {
                    new Dashboard();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnCancel) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
