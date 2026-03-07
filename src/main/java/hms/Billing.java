package hms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Billing extends JFrame implements ActionListener {
    JComboBox<String> cbPatientId;
    JTextField tfConsultationFee, tfOtherCharges, tfTotal;
    JButton btnCalculate, btnSave, btnCancel;
    JTable table;
    DefaultTableModel model;

    public Billing() {
        setTitle("Patient Billing");
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Patient ID (Name):"));
        cbPatientId = new JComboBox<>();
        inputPanel.add(cbPatientId);

        inputPanel.add(new JLabel("Consultation Fee:"));
        tfConsultationFee = new JTextField("0.0");
        inputPanel.add(tfConsultationFee);

        inputPanel.add(new JLabel("Other Charges:"));
        tfOtherCharges = new JTextField("0.0");
        inputPanel.add(tfOtherCharges);

        inputPanel.add(new JLabel("Total Amount:"));
        tfTotal = new JTextField("0.0");
        tfTotal.setEditable(false);
        inputPanel.add(tfTotal);

        JPanel btnPanel = new JPanel();
        btnCalculate = new JButton("Calculate");
        btnCalculate.addActionListener(this);
        btnPanel.add(btnCalculate);

        btnSave = new JButton("Save Bill");
        btnSave.addActionListener(this);
        btnPanel.add(btnSave);

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        btnPanel.add(btnCancel);

        inputPanel.add(btnPanel);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[] { "Bill ID", "Patient ID", "Consultation", "Other Charges", "Total" }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        loadComboBoxData();
        loadBilling();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBilling() {
        model.setRowCount(0);
        try {
            DBConnection c = new DBConnection();
            ResultSet rs = c.s.executeQuery("SELECT * FROM billing");
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"),
                        rs.getString("patient_id"),
                        rs.getString("consultation_fee"),
                        rs.getString("other_charges"),
                        rs.getString("total")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnCalculate) {
            try {
                double consultation = Double.parseDouble(tfConsultationFee.getText());
                double other = Double.parseDouble(tfOtherCharges.getText());
                double total = consultation + other;
                tfTotal.setText(String.valueOf(total));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric amounts.");
            }
        } else if (ae.getSource() == btnSave) {
            try {
                DBConnection c = new DBConnection();
                String pat = (String) cbPatientId.getSelectedItem();

                if (pat == null) {
                    JOptionPane.showMessageDialog(null, "Please select Patient");
                    return;
                }

                String pId = pat.split(" - ")[0];
                String fee = tfConsultationFee.getText();
                String other = tfOtherCharges.getText();
                String total = tfTotal.getText();

                // Recalculate just in case
                double calcTotal = Double.parseDouble(fee) + Double.parseDouble(other);

                String query = "INSERT INTO billing (patient_id, consultation_fee, other_charges, total) VALUES ('" +
                        pId + "', '" + fee + "', '" + other + "', '" + calcTotal + "')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Bill Saved Successfully");
                loadBilling();

                tfConsultationFee.setText("0.0");
                tfOtherCharges.setText("0.0");
                tfTotal.setText("0.0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == btnCancel) {
            setVisible(false);
        }
    }
}
