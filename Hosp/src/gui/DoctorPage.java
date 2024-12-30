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

public class DoctorPage extends JFrame {
    private int doctorId; // Doctor's ID
    private JTable appointmentsTable; // Table to display appointments
    private JButton viewAppointmentsButton, updateInfoButton, deleteAccountButton, backButton;

    public DoctorPage(String username) {
        // Fetch the doctor ID using the username
        this.doctorId = getDoctorId(username);

        // Set frame properties
        setTitle("Doctor Dashboard");
        setSize(1950, 1050);
        setLayout(null);
        getContentPane().setBackground(new Color(109, 164, 170));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Welcome label
        JLabel welcomeLabel = new JLabel("<-------- DOCTOR DASHBOARD -------> ");
        welcomeLabel.setBounds(280, 40, 650, 30);
        welcomeLabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.CYAN);
        add(welcomeLabel);

        // Icon display
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/dr.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(i1);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBounds(720, 80, 600, 400);
        add(iconLabel);

        // Appointments table
        appointmentsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentsTable);
        tableScrollPane.setBounds(100, 500, 1200, 300); // Adjust bounds to fit your layout
        add(tableScrollPane);

        // Populate appointments table
        populateAppointmentsTable();
//
//        // View Appointments Button
//        viewAppointmentsButton = new JButton("View Appointments");
//        viewAppointmentsButton.setBounds(120, 110, 250, 30);
//        viewAppointmentsButton.setFont(new Font("Shamim", Font.BOLD, 15));
//        viewAppointmentsButton.setBackground(new Color(255, 179, 0));
//        viewAppointmentsButton.setForeground(Color.WHITE);
//        viewAppointmentsButton.addActionListener(e -> populateAppointmentsTable());
//        add(viewAppointmentsButton);

        // Update Info Button
        updateInfoButton = new JButton("Update Info");
        updateInfoButton.setBounds(120, 170, 250, 30);
        updateInfoButton.setFont(new Font("Shamim", Font.BOLD, 15));
        updateInfoButton.setBackground(new Color(255, 179, 0));
        updateInfoButton.setForeground(Color.WHITE);
        updateInfoButton.addActionListener(e -> new UpdateInfoPage(username).setVisible(true));

        add(updateInfoButton);

        // Delete Account Button
        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setBounds(120, 230, 250, 30);
        deleteAccountButton.setFont(new Font("Shamim", Font.BOLD, 15));
        deleteAccountButton.setBackground(new Color(255, 179, 0));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.addActionListener(new DeleteAccountAction());
        add(deleteAccountButton);

        // Back Button
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
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Appointment ID", "Patient Name", "Date", "Time", "Status"}, 0);

        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT a.id, u.username AS patient_name, a.appointment_date, a.appointment_time, a.status " +
                    "FROM appointments a JOIN users u ON a.patient_username = u.username " +
                    "WHERE a.doctor_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("id");
                String patientName = resultSet.getString("patient_name");
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");
                String status = resultSet.getString("status");
                tableModel.addRow(new Object[]{appointmentId, patientName, date, time, status});
            }

            appointmentsTable.setModel(tableModel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching appointments: " + e.getMessage());
        }
    }

    // Fetch the doctor ID using the username
    private int getDoctorId(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching doctor ID.");
        }
        return -1; // Invalid ID
    }

    // Action for updating information
    private class UpdateInfoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JTextField phoneField = new JTextField();

            // Fetch current information
            try (Connection connection = DBConnection.getConnection()) {
                String query = "SELECT name, email, phone FROM users WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, doctorId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    emailField.setText(resultSet.getString("email"));
                    phoneField.setText(resultSet.getString("phone"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(DoctorPage.this, "Error fetching user information.");
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

            int result = JOptionPane.showConfirmDialog(DoctorPage.this, panel, "Update Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection connection = DBConnection.getConnection()) {
                    String updateQuery = "UPDATE users SET name = ?, email = ?, phone = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, nameField.getText());
                    updateStatement.setString(2, emailField.getText());
                    updateStatement.setString(3, phoneField.getText());
                    updateStatement.setInt(4, doctorId);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(DoctorPage.this, "Information updated successfully.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DoctorPage.this, "Error updating information.");
                }
            }
        }
    }

    // Action for deleting account
    private class DeleteAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmation = JOptionPane.showConfirmDialog(DoctorPage.this, "Are you sure you want to delete your account? This action cannot be undone.", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection connection = DBConnection.getConnection()) {
                    String deleteQuery = "DELETE FROM users WHERE id = ?";
                    PreparedStatement statement = connection.prepareStatement(deleteQuery);
                    statement.setInt(1, doctorId);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(DoctorPage.this, "Account deleted successfully.");
                    new LoginPage().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DoctorPage.this, "Error deleting account.");
                }
            }
        }
    }

    public static void main(String[] args) {
        new DoctorPage("test_doctor"); // Replace with the actual doctor's username
    }
}
