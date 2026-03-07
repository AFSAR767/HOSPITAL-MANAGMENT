package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MedicalHistory extends JFrame implements ActionListener {
    JComboBox<String> cbPatientId;
    JTextField tfDiagnosis, tfTreatment, tfPrescription;
    JButton btnSave, btnCancel;
    JTable table;
    DefaultTableModel model;

    public MedicalHistory() {
        setTitle("Medical History");
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Patient ID (Name):"));
        cbPatientId = new JComboBox<>();
        inputPanel.add(cbPatientId);

        inputPanel.add(new JLabel("Diagnosis:"));
        tfDiagnosis = new JTextField();
        inputPanel.add(tfDiagnosis);

        inputPanel.add(new JLabel("Treatment:"));
        tfTreatment = new JTextField();
        inputPanel.add(tfTreatment);

        inputPanel.add(new JLabel("Prescription:"));
        tfPrescription = new JTextField();
        inputPanel.add(tfPrescription);

        JPanel btnPanel = new JPanel();
        btnSave = new JButton("Save History");
        btnSave.addActionListener(this);
        btnPanel.add(btnSave);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnPanel.add(btnCancel);

        inputPanel.add(btnPanel);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[] { "ID", "Patient ID", "Diagnosis", "Treatment", "Prescription", "Date" }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadComboBoxData();
        loadHistory();

        setSize(800, 500);
        setLocation(200, 100);
        setVisible(true);
    }

    public void loadComboBoxData() {
        try {
            DBConnection c = new DBConnection();
            ResultSet rsP = c.s.executeQuery("SELECT id, name FROM patients");
            while (rsP.next()) {
                cbPatientId.addItem(rsP.getString("id") + " - " + rsP.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHistory() {
        model.setRowCount(0);
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.s.executeQuery("SELECT * FROM medical_history");
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment"),
                        rs.getString("prescription"),
                        rs.getString("date_recorded")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnSave) {
            try {
                DBConnection c = new DBConnection();
                String pat = (String) cbPatientId.getSelectedItem();

                if (pat == null) {
                    JOptionPane.showMessageDialog(null, "Please select Patient");
                    return;
                }

                String pId = pat.split(" - ")[0];
                String diag = tfDiagnosis.getText();
                String treat = tfTreatment.getText();
                String presc = tfPrescription.getText();

                String query = "INSERT INTO medical_history (patient_id, diagnosis, treatment, prescription) VALUES ('"
                        +
                        pId + "', '" + diag + "', '" + treat + "', '" + presc + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Medical History Saved Successfully");
                loadHistory();

                tfDiagnosis.setText("");
                tfTreatment.setText("");
                tfPrescription.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }
}
