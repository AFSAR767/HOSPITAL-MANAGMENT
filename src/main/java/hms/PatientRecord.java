package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PatientRecord extends JFrame implements ActionListener {
    JTextField tfName, tfAge, tfGender, tfPhone, tfAddress;
    JButton btnAdd, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel model;
    String currentId = "";

    public PatientRecord() {
        setTitle("Manage Patients");
        setLayout(new BorderLayout());

        // Top Panel for Inputs
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        tfName = new JTextField();
        inputPanel.add(tfName);

        inputPanel.add(new JLabel("Age:"));
        tfAge = new JTextField();
        inputPanel.add(tfAge);

        inputPanel.add(new JLabel("Gender:"));
        tfGender = new JTextField();
        inputPanel.add(tfGender);

        inputPanel.add(new JLabel("Phone:"));
        tfPhone = new JTextField();
        inputPanel.add(tfPhone);

        inputPanel.add(new JLabel("Address:"));
        tfAddress = new JTextField();
        inputPanel.add(tfAddress);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(this);
        btnPanel.add(btnAdd);

        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(this);
        btnPanel.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this);
        btnPanel.add(btnDelete);

        btnClear = new JButton("Clear");
        btnClear.addActionListener(this);
        btnPanel.add(btnClear);

        inputPanel.add(btnPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Table for displaying records
        model = new DefaultTableModel(new String[] { "ID", "Name", "Age", "Gender", "Phone", "Address" }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                currentId = model.getValueAt(row, 0).toString();
                tfName.setText(model.getValueAt(row, 1).toString());
                tfAge.setText(model.getValueAt(row, 2).toString());
                tfGender.setText(model.getValueAt(row, 3).toString());
                tfPhone.setText(model.getValueAt(row, 4).toString());
                tfAddress.setText(model.getValueAt(row, 5).toString());
            }
        });

        loadData();

        setSize(800, 500);
        setLocation(200, 100);
        setVisible(true);
    }

    public void loadData() {
        model.setRowCount(0);
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.s.executeQuery("SELECT * FROM patients");
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("age"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("address")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnAdd) {
            try {
                DBConnection c = new DBConnection();
                String query = "INSERT INTO patients (name, age, gender, phone, address) VALUES ('" +
                        tfName.getText() + "', '" + tfAge.getText() + "', '" + tfGender.getText() + "', '"
                        + tfPhone.getText() + "', '" + tfAddress.getText() + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Patient Added Successfully");
                loadData();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnUpdate) {
            if (currentId.isEmpty())
                return;
            try {
                DBConnection c = new DBConnection();
                String query = "UPDATE patients SET name='" + tfName.getText() + "', age='" + tfAge.getText() +
                        "', gender='" + tfGender.getText() + "', phone='" + tfPhone.getText() + "', address='"
                        + tfAddress.getText() + "' WHERE id=" + currentId;
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Patient Updated Successfully");
                loadData();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnDelete) {
            if (currentId.isEmpty())
                return;
            try {
                DBConnection c = new DBConnection();
                String query = "DELETE FROM patients WHERE id=" + currentId;
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Patient Deleted Successfully");
                loadData();
                clearFields();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnClear) {
            clearFields();
        }
    }

    public void clearFields() {
        tfName.setText("");
        tfAge.setText("");
        tfGender.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        currentId = "";
    }
}
