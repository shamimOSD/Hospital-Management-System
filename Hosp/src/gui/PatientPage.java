package gui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientPage extends JFrame {
    private String patientUsername;
    private JTable appointmentsTable;
    private JButton bookAppointmentButton, updateInfoButton, deleteAccountButton, backButton;

    public PatientPage(String patientUsername) {
        this.patientUsername = patientUsername;

        // Set frame properties
        setTitle("Patient Dashboard");
        setSize(1950, 1050);
        setLayout(null);
        getContentPane().setBackground(new Color(109, 164, 170));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Welcome label
        JLabel welcomeLabel = new JLabel("<-------- PATIENT DASHBOARD -------> ");
        welcomeLabel.setBounds(280, 40, 650, 30);
        welcomeLabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.CYAN);
        add(welcomeLabel);

        // Icon display
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/Pat.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(i1);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBounds(720, 100, 600, 400);
        add(iconLabel);

        // Appointments table
        appointmentsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentsTable);
        tableScrollPane.setBounds(80, 400, 600, 300); // Adjust bounds to fit your layout
        add(tableScrollPane);

        // Populate appointments table
        populateAppointmentsTable();

        // Buttons
        bookAppointmentButton = new JButton("Book Appointment");
        bookAppointmentButton.setBounds(120, 110, 250, 30);
        bookAppointmentButton.setFont(new Font("Shamim", Font.BOLD, 15));
        bookAppointmentButton.setBackground(new Color(255, 179, 0));
        bookAppointmentButton.setForeground(Color.WHITE);
        bookAppointmentButton.addActionListener(new BookAppointmentAction());
        add(bookAppointmentButton);

        updateInfoButton = new JButton("Update Info");
        updateInfoButton.setBounds(120, 170, 250, 30);
        updateInfoButton.setFont(new Font("Shamim", Font.BOLD, 15));
        updateInfoButton.setBackground(new Color(255, 179, 0));
        updateInfoButton.setForeground(Color.WHITE);
        updateInfoButton.addActionListener(e -> new UpdateInfoPage(patientUsername).setVisible(true));
        add(updateInfoButton);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(120, 230, 250, 30);
        deleteAccountButton.setFont(new Font("Shamim", Font.BOLD, 15));
        deleteAccountButton.setBackground(new Color(255, 179, 0));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.addActionListener(new DeleteAccountAction());
        add(deleteAccountButton);

        backButton = new JButton("Back");
        backButton.setBounds(120, 300, 250, 30);
        backButton.setFont(new Font("Shamim", Font.BOLD, 15));
        backButton.setBackground(new Color(255, 179, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });
        add(backButton);

        setVisible(true);
    }

    // Populate the appointments table
    private void populateAppointmentsTable() {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Date", "Time", "Doctor", "Status"}, 0);

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT appointment_date, appointment_time, doctor, status FROM appointments WHERE patient_username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, patientUsername);
            ResultSet resultSet = statement.executeQuery();

            boolean hasData = false;
            while (resultSet.next()) {
                hasData = true;
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String doctor = resultSet.getString("doctor");
                String status = resultSet.getString("status");
                tableModel.addRow(new Object[]{date, time, doctor, status});
            }

            appointmentsTable.setModel(tableModel);

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No appointments found for the patient.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching appointments.");
        }
    }

    // Action for booking an appointment
    private class BookAppointmentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new BookAppointmentPage(patientUsername).setVisible(true);
            dispose();
        }
    }

    // Action for updating personal info
    private class UpdateInfoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();

            // Fetch current information
            try (Connection connection = DBConnection.getConnection()) {
                String query = "SELECT name, email, phone FROM users WHERE username = ?";

                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, patientUsername);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    emailField.setText(resultSet.getString("email"));
                    phoneField.setText(resultSet.getString("phone"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(PatientPage.this, "Error fetching user information.");
                return;
            }

            // Create update form
            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Phone:"));
            panel.add(phoneField);

            int result = JOptionPane.showConfirmDialog(PatientPage.this, panel, "Update Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection connection = DBConnection.getConnection()) {
                    String updateQuery = "UPDATE users SET name = ?, email = ?, phone = ? WHERE username = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, nameField.getText());
                    updateStatement.setString(2, emailField.getText());
                    updateStatement.setString(3, phoneField.getText());
                    updateStatement.setString(4, patientUsername);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(PatientPage.this, "Information updated successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PatientPage.this, "Error updating information.");
                }
            }
        }
    }

    // Action for deleting account
    private class DeleteAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmation = JOptionPane.showConfirmDialog(PatientPage.this, "Are you sure you want to delete your account? This action cannot be undone.", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection connection = DBConnection.getConnection()) {
                    String deleteQuery = "DELETE FROM users WHERE username = ?";
                    PreparedStatement statement = connection.prepareStatement(deleteQuery);
                    statement.setString(1, patientUsername);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(PatientPage.this, "Account deleted successfully.");
                    new LoginPage().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PatientPage.this, "Error deleting account.");
                }
            }
        }
    }

    public static void main(String[] args) {
        new PatientPage("test_patient"); // Replace with dynamic username
    }
}
