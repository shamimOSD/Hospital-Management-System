package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookAppointmentPage extends JFrame {
    private JTextField dateField;
    private JComboBox<String> timeComboBox;
    private JComboBox<String> doctorComboBox;
    private JButton bookButton, backButton;
    private String patientUsername;

    public BookAppointmentPage(String patientUsername) {
        this.patientUsername = patientUsername;
        //code for
        setTitle("Book Appointment");
        JLabel welcomelabel = new JLabel("<-------- CONFIRM YOUR APPOINTMENT  -------> ");
        welcomelabel.setBounds(280, 40, 650, 30);
        welcomelabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomelabel.setForeground(Color.cyan);
        add(welcomelabel);

        // Code for Icon interface
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/patient.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(i1);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(720, 100, 600, 400);
        add(label);

        // Doctor selection
        JLabel SelectDoctor = new JLabel("Select Doctor:");
        SelectDoctor.setBounds(120, 120, 350, 30);
        SelectDoctor.setFont(new Font("Shamim", Font.BOLD, 18));
        SelectDoctor.setForeground(Color.BLACK);
        add(SelectDoctor);

        // Initialize doctorComboBox before using it
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setBounds(480, 120, 300, 30);
        doctorComboBox.setFont(new Font("Shamim", Font.BOLD, 15));
        doctorComboBox.setBackground(new Color(255, 179, 0));
        doctorComboBox.setForeground(Color.white);
        populateDoctors(); // Populate doctors list
        add(doctorComboBox);

        // Appointment date
        JLabel AppointmentDate = new JLabel("Enter Appointment Date (yyyy/MM/dd):");
        AppointmentDate.setBounds(120, 180, 350, 30);
        AppointmentDate.setFont(new Font("Shamim", Font.BOLD, 18));
        AppointmentDate.setForeground(Color.BLACK);
        add(AppointmentDate);

        dateField = new JTextField();
        dateField.setBounds(480, 180, 300, 30);
        dateField.setFont(new Font("Shamim", Font.BOLD, 15));
        dateField.setBackground(new Color(255, 179, 0));
        dateField.setForeground(Color.white);
        add(dateField);

        // Appointment time
        JLabel SelectTime = new JLabel("Select Appointment Time:");
        SelectTime.setBounds(120, 240, 350, 30);
        SelectTime.setFont(new Font("Shamim", Font.BOLD, 18));
        SelectTime.setForeground(Color.BLACK);
        add(SelectTime);

        timeComboBox = new JComboBox<>(new String[]{
                "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM",
                "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM",
                "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM",
                "04:00 PM", "04:30 PM", "05:00 PM", "06:00 PM",
                "06:30 PM", "7:00 PM", "07:30 PM", "8:00 PM"
        });
        timeComboBox.setBounds(480, 240, 300, 30);
        timeComboBox.setFont(new Font("Shamim", Font.BOLD, 15));
        timeComboBox.setBackground(new Color(255, 179, 0));
        timeComboBox.setForeground(Color.white);
        add(timeComboBox);

        // Book appointment button
        bookButton = new JButton("Book Appointment");
        bookButton.setBounds(120, 320, 200, 30);
        bookButton.setFont(new Font("Shamim", Font.BOLD, 15));
        bookButton.setBackground(Color.BLACK);
        bookButton.setForeground(Color.WHITE);
        bookButton.addActionListener(new BookAction());
        add(bookButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setBounds(450, 320, 150, 30);
        backButton.setFont(new Font("Shamim", Font.BOLD, 15));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new PatientPage(patientUsername).setVisible(true);
            dispose();
        });
        add(backButton);

        getContentPane().setBackground(new Color(109, 164, 170));
        setSize(1950, 1050);
        setLayout(null);
        setVisible(true);
    }


    private void populateDoctors() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT name, specialization FROM doctors";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String doctor = resultSet.getString("name") + " - " + resultSet.getString("specialization");
                doctorComboBox.addItem(doctor);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching doctors.");
        }
    }

    private int getPatientIdByUsername(String username) throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT id FROM patients WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new Exception("Patient not found.");
            }
        }
    }

    private int getDoctorIdByName(String doctorName) throws Exception {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT id FROM doctors WHERE CONCAT(name, ' - ', specialization) = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new Exception("Doctor not found.");
            }
        }
    }


    private class BookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String dateInput = dateField.getText();
            String timeSlot = (String) timeComboBox.getSelectedItem();
            String selectedDoctor = (String) doctorComboBox.getSelectedItem();

            // Validate date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            dateFormat.setLenient(false);
            Date selectedDate;
            try {
                selectedDate = dateFormat.parse(dateInput);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please enter a valid date (yyyy/MM/dd).");
                return;
            }

            try (Connection connection = DBConnection.getConnection()) {
                // Fetch Patient ID
                int patientId = getPatientIdByUsername(patientUsername);
                System.out.println("Patient ID: " + patientId);

                // Fetch Doctor ID
                int doctorId = getDoctorIdByName(selectedDoctor);
                System.out.println("Doctor ID: " + doctorId);

                // Check for conflicting appointments
                String conflictQuery = "SELECT * FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
                PreparedStatement conflictStmt = connection.prepareStatement(conflictQuery);
                conflictStmt.setInt(1, doctorId);
                conflictStmt.setDate(2, new java.sql.Date(selectedDate.getTime()));
                conflictStmt.setString(3, timeSlot);

                ResultSet rs = conflictStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "This time slot is already booked for the selected doctor. Please choose another.");
                    return;
                }

                // Insert appointment into database
                String insertQuery = "INSERT INTO appointments (patient_id, patient_username, doctor_id, appointment_date, appointment_time, doctor, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setInt(1, patientId);
                insertStmt.setString(2, patientUsername);
                insertStmt.setInt(3, doctorId);
                insertStmt.setDate(4, new java.sql.Date(selectedDate.getTime()));
                insertStmt.setString(5, timeSlot);
                insertStmt.setString(6, selectedDoctor);
                insertStmt.setString(7, "Pending");

                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Appointment booked successfully!");
                    new PatientPage(patientUsername).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to book appointment.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }



    public static void main(String[] args) {
        new BookAppointmentPage("test_patient").setVisible(true);
    }
}
