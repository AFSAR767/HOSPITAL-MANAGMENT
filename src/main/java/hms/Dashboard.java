package hms;

import javax.swing.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements ActionListener {

    public Dashboard() {
        setTitle("Hospital Management System - Dashboard");
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar mb = new JMenuBar();

        JMenu patientMenu = new JMenu("Patients");
        JMenuItem mniPatientRecord = new JMenuItem("Manage Patients");
        mniPatientRecord.addActionListener(this);
        patientMenu.add(mniPatientRecord);

        JMenu doctorMenu = new JMenu("Doctors");
        JMenuItem mniDoctorRecord = new JMenuItem("Manage Doctors");
        mniDoctorRecord.addActionListener(this);
        doctorMenu.add(mniDoctorRecord);

        JMenu appointmentMenu = new JMenu("Appointments");
        JMenuItem mniBookAppointment = new JMenuItem("Book Appointment");
        mniBookAppointment.addActionListener(this);
        appointmentMenu.add(mniBookAppointment);

        JMenu medicalMenu = new JMenu("Medical History");
        JMenuItem mniMedicalHistory = new JMenuItem("Manage History");
        mniMedicalHistory.addActionListener(this);
        medicalMenu.add(mniMedicalHistory);

        JMenu billingMenu = new JMenu("Billing");
        JMenuItem mniBilling = new JMenuItem("Patient Billing");
        mniBilling.addActionListener(this);
        billingMenu.add(mniBilling);

        mb.add(patientMenu);
        mb.add(doctorMenu);
        mb.add(appointmentMenu);
        mb.add(medicalMenu);
        mb.add(billingMenu);

        setJMenuBar(mb);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String msg = ae.getActionCommand();
        if (msg.equals("Manage Patients")) {
            new PatientRecord();
        } else if (msg.equals("Manage Doctors")) {
            new DoctorRecord();
        } else if (msg.equals("Book Appointment")) {
            new Appointment();
        } else if (msg.equals("Manage History")) {
            new MedicalHistory();
        } else if (msg.equals("Patient Billing")) {
            new Billing();
        }
    }
}
