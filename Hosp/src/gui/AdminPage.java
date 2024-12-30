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

public class AdminPage extends JFrame {
    private int adminId; // Admin's ID
    private JTable appointmentsTable; // Table to display appointments
    private JButton approveButton, updateInfoButton,deleteAccountButton, backButton; // Buttons for actions

    public AdminPage(String username) {
        // Fetch the Admin ID using the username
       // this.adminId = getAdminId(username);

        // Set frame properties
        setTitle("Admin Dashboard");
        setSize(1950, 1050);
        setLayout(null);
        getContentPane().setBackground(new Color(109, 164, 170));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Welcome label
        JLabel welcomeLabel = new JLabel("<-------- ADMIN DASHBOARD ------->");
        welcomeLabel.setBounds(280, 40, 650, 30);
        welcomeLabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.CYAN);
        add(welcomeLabel);

      //   Icon display
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/Admin.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(i1);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBounds(780, 100, 600, 400);
        add(iconLabel);

        // Appointments table
        appointmentsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(appointmentsTable);
        tableScrollPane.setBounds(20, 350, 800, 300); // Adjust bounds to fit your layout
        add(tableScrollPane);

        // Load appointments into the table
        loadAppointments();

        // Approve Appointment Button
        approveButton = new JButton("Approve Appointment");
        approveButton.setBounds(120, 120, 250, 30);
        approveButton.setFont(new Font("Shamim", Font.BOLD, 15));
        approveButton.setBackground(new Color(255, 179, 0));
        approveButton.setForeground(Color.WHITE);
        approveButton.addActionListener(new ApproveAppointmentAction());
        add(approveButton);



        updateInfoButton = new JButton("Update Info");
        updateInfoButton.setBounds(120, 170, 250, 30);
        updateInfoButton.setFont(new Font("Shamim", Font.BOLD, 15));
        updateInfoButton.setBackground(new Color(255, 179, 0));
        updateInfoButton.setForeground(Color.WHITE);
        updateInfoButton.addActionListener(e -> new UpdateInfoPage(username).setVisible(true));
        add(updateInfoButton);

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

    // Load appointments into the table
    private void loadAppointments() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, patient_id, patient_username, doctor_id, appointment_date, appointment_time, doctor, status FROM appointments";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Appointment ID", "Patient ID", "Patient Username", "Doctor ID", "Appointment Date", "Appointment Time", "Doctor Name", "Status"}, 0
            );
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getString("patient_username"),
                        rs.getInt("doctor_id"),
                        rs.getString("appointment_date"),
                        rs.getString("appointment_time"),
                        rs.getString("doctor"),
                        rs.getString("status")
                });
            }
            appointmentsTable.setModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments.");
        }
    }

    // Action for approving selected appointment
    private class ApproveAppointmentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = appointmentsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AdminPage.this, "Select an appointment to approve.");
                return;
            }
            int appointmentId = (int) appointmentsTable.getValueAt(selectedRow, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String query = "UPDATE appointments SET status = 'Approved' WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, appointmentId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(AdminPage.this, "Appointment approved!");
                loadAppointments();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AdminPage.this, "Error approving appointment.");
            }
        }
    }

    // Action for deleting account
    private class DeleteAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirmation = JOptionPane.showConfirmDialog(AdminPage.this, "Are you sure you want to delete your account? This action cannot be undone.", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection connection = DBConnection.getConnection()) {
                    String deleteQuery = "DELETE FROM users WHERE username = ?";
                    PreparedStatement statement = connection.prepareStatement(deleteQuery);
                    statement.setInt(1, adminId);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(AdminPage.this, "Account deleted successfully.");
                    new LoginPage().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AdminPage.this, "Error deleting account.");
                }
            }
        }
    }
    public static void main(String[] args) {
        new AdminPage("admin");
    }

}
