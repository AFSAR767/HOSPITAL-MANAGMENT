package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorRecord extends JFrame implements ActionListener {
    JTextField tfName, tfSpec, tfAvail;
    JButton btnAdd, btnUpdate, btnDelete, btnClear;
    JTable table;
    DefaultTableModel model;
    String currentId = "";

    public DoctorRecord() {
        setTitle("Manage Doctors");
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        tfName = new JTextField();
        inputPanel.add(tfName);

        inputPanel.add(new JLabel("Specialization:"));
        tfSpec = new JTextField();
        inputPanel.add(tfSpec);

        inputPanel.add(new JLabel("Availability:"));
        tfAvail = new JTextField();
        inputPanel.add(tfAvail);

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

        model = new DefaultTableModel(new String[] { "ID", "Name", "Specialization", "Availability" }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int row = table.getSelectedRow();
                currentId = model.getValueAt(row, 0).toString();
                tfName.setText(model.getValueAt(row, 1).toString());
                tfSpec.setText(model.getValueAt(row, 2).toString());
                tfAvail.setText(model.getValueAt(row, 3).toString());
            }
        });

        loadData();

        setSize(700, 450);
        setLocation(250, 150);
        setVisible(true);
    }

    public void loadData() {
        model.setRowCount(0);
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.s.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("specialization"),
                        rs.getString("availability")
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
                String query = "INSERT INTO doctors (name, specialization, availability) VALUES ('" +
                        tfName.getText() + "', '" + tfSpec.getText() + "', '" + tfAvail.getText() + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Doctor Added Successfully");
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
                String query = "UPDATE doctors SET name='" + tfName.getText() + "', specialization='" + tfSpec.getText()
                        +
                        "', availability='" + tfAvail.getText() + "' WHERE id=" + currentId;
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Doctor Updated Successfully");
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
                String query = "DELETE FROM doctors WHERE id=" + currentId;
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Doctor Deleted Successfully");
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
        tfSpec.setText("");
        tfAvail.setText("");
        currentId = "";
    }
}
