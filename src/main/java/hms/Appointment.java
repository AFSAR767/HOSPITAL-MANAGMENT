package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Appointment extends JFrame implements ActionListener {
    JComboBox<String> cbPatientId, cbDoctorId;
    JTextField tfDate, tfTime;
    JButton btnBook, btnCancel;
    JTable table;
    DefaultTableModel model;

    public Appointment() {
        setTitle("Book Appointment");
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Patient ID (Name):"));
        cbPatientId = new JComboBox<>();
        inputPanel.add(cbPatientId);

        inputPanel.add(new JLabel("Doctor ID (Name):"));
        cbDoctorId = new JComboBox<>();
        inputPanel.add(cbDoctorId);

        inputPanel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        tfDate = new JTextField();
        inputPanel.add(tfDate);

        inputPanel.add(new JLabel("Appointment Time (HH:MM:SS):"));
        tfTime = new JTextField();
        inputPanel.add(tfTime);

        JPanel btnPanel = new JPanel();
        btnBook = new JButton("Book");
        btnBook.addActionListener(this);
        btnPanel.add(btnBook);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnPanel.add(btnCancel);

        inputPanel.add(btnPanel);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "Appt ID", "Patient ID", "Doctor ID", "Date", "Time" }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadComboBoxData();
        loadAppointments();

        setSize(700, 450);
        setLocation(250, 150);
        setVisible(true);
    }

    public void loadComboBoxData() {
        try {
            DBConnection c = new DBConnection();
            ResultSet rsP = c.s.executeQuery("SELECT id, name FROM patients");
            while (rsP.next()) {
                cbPatientId.addItem(rsP.getString("id") + " - " + rsP.getString("name"));
            }

            ResultSet rsD = c.s.executeQuery("SELECT id, name FROM doctors");
            while (rsD.next()) {
                cbDoctorId.addItem(rsD.getString("id") + " - " + rsD.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAppointments() {
        model.setRowCount(0);
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.s.executeQuery("SELECT * FROM appointments");
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnBook) {
            try {
                DBConnection c = new DBConnection();
                String pat = (String) cbPatientId.getSelectedItem();
                String doc = (String) cbDoctorId.getSelectedItem();

                if (pat == null || doc == null) {
                    JOptionPane.showMessageDialog(null, "Please select Patient and Doctor");
                    return;
                }

                String pId = pat.split(" - ")[0];
                String dId = doc.split(" - ")[0];
                String date = tfDate.getText();
                String time = tfTime.getText();

                String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES ('"
                        +
                        pId + "', '" + dId + "', '" + date + "', '" + time + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Appointment Booked Successfully");
                loadAppointments();
                tfDate.setText("");
                tfTime.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error booking appointment. Check date/time format.");
            }
        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }
}
