package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateInfoPage extends JFrame {
    private JTextField usernameField, nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JButton updateButton, backButton;

    public UpdateInfoPage(String username) {
        setTitle("Update User Information");
        getContentPane().setBackground(new Color(109, 164, 170));
        setLayout(null);

        JLabel headerLabel = new JLabel("Update User Information");
        headerLabel.setFont(new Font("Shamim", Font.BOLD, 24));
        headerLabel.setBounds(200, 20, 400, 30);
        add(headerLabel);

        //   Icon display
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/updated.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(i1);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBounds(720, 100, 600, 400);
        add(iconLabel);

        // Code for Username
        JLabel Username = new JLabel("Username");
        Username.setBounds(140, 110, 250, 30);
        Username.setFont(new Font("Shamim", Font.BOLD, 18));
        Username.setForeground(Color.BLACK);
        add(Username);

        // Code for username field
        usernameField = new JTextField();
        usernameField.setBounds(400, 110, 250, 30);
        usernameField.setFont(new Font("Shamim", Font.BOLD, 15));
        usernameField.setBackground(new Color(255, 179, 0));
        usernameField.setForeground(Color.white);
        add(usernameField);

        // Code for Password
        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(140, 170, 250, 30);
        passwordlabel.setFont(new Font("Shamim", Font.BOLD, 18));
        passwordlabel.setForeground(Color.BLACK);
        add(passwordlabel);

        // Code for user password field
        passwordField = new JPasswordField();
        passwordField.setBounds(400, 170, 250, 30);
        passwordField.setFont(new Font("Shamim", Font.BOLD, 15));
        passwordField.setBackground(new Color(255, 179, 0));
        passwordField.setForeground(Color.white);
        add(passwordField);

        // Code for Name
        JLabel namelabel = new JLabel("Name");
        namelabel.setBounds(140, 230, 250, 30);
        namelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        namelabel.setForeground(Color.BLACK);
        add(namelabel);

        // Code for NameField
        nameField = new JTextField();
        nameField.setBounds(400, 230, 250, 30);
        nameField.setFont(new Font("Shamim", Font.BOLD, 15));
        nameField.setBackground(new Color(255, 179, 0));
        nameField.setForeground(Color.white);
        add(nameField);

        // Code for Email
        JLabel Emaillabel = new JLabel("Email");
        Emaillabel.setBounds(140, 290, 250, 30);
        Emaillabel.setFont(new Font("Shamim", Font.BOLD, 18));
        Emaillabel.setForeground(Color.BLACK);
        add(Emaillabel);

        // Code for EmailField
        emailField = new JTextField();
        emailField.setBounds(400, 290, 250, 30);
        emailField.setFont(new Font("Shamim", Font.BOLD, 15));
        emailField.setBackground(new Color(255, 179, 0));
        emailField.setForeground(Color.white);
        add(emailField);

        // Code for Phone
        JLabel Phonelabel = new JLabel("Phone");
        Phonelabel.setBounds(140, 350, 250, 30);
        Phonelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        Phonelabel.setForeground(Color.BLACK);
        add(Phonelabel);

        // Code for PhoneField
        phoneField = new JTextField();
        phoneField.setBounds(400, 350, 250, 30);
        phoneField.setFont(new Font("Shamim", Font.BOLD, 15));
        phoneField.setBackground(new Color(255, 179, 0));
        phoneField.setForeground(Color.white);
        add(phoneField);

        // Update Button
        updateButton = new JButton("Update");
        updateButton.setBounds(140, 420, 150, 30);
        updateButton.setFont(new Font("Shamim", Font.BOLD, 15));
        updateButton.setBackground(new Color(255, 179, 0));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updateUser(username));
        add(updateButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(400, 420, 150, 30);
        backButton.setFont(new Font("Shamim", Font.BOLD, 15));
        backButton.setBackground(new Color(255, 179, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> dispose());
        add(backButton);

        loadUserData(username);

        setSize(1950, 1050);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadUserData(String username) {
        try (Connection connection = DBConnection.getConnection()) {
//            String query = """
//            SELECT u.username, u.password, u.role,
//                   COALESCE(a.name, d.name, p.name) AS name,
//                   COALESCE(a.email, d.email, p.email) AS email,
//                   COALESCE(a.phone, d.phone, p.phone) AS phone
//            FROM users u
//            LEFT JOIN admins a ON u.username = a.name
//            LEFT JOIN doctors d ON u.username = d.name
//            LEFT JOIN patients p ON u.username = p.name
//            WHERE u.username = ?
//        """;
            String query = """
            SELECT u.username, a.name AS admin_name
            FROM users u
            LEFT JOIN admins a ON u.username = a.name
            WHERE u.role = 'Admin';

            SELECT u.username, d.name AS doctor_name
            FROM users u
            LEFT JOIN doctors d ON u.username = d.name
            WHERE u.role = 'Doctor';

            SELECT u.username, p.name AS patient_name
            FROM users u
            LEFT JOIN patients p ON u.username = p.name
            WHERE u.role = 'Patient' """;


            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                usernameField.setText(resultSet.getString("username"));
                passwordField.setText(resultSet.getString("password"));
                nameField.setText(resultSet.getString("name"));
                emailField.setText(resultSet.getString("email"));
                phoneField.setText(resultSet.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "User not found!");
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data.");
        }
    }


    private void updateUser(String oldUsername) {
        String newUsername = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (newUsername.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {
            // Get the user's role
            String roleQuery = "SELECT role FROM users WHERE username = ?";
            PreparedStatement roleStmt = connection.prepareStatement(roleQuery);
            roleStmt.setString(1, oldUsername);
            ResultSet roleResult = roleStmt.executeQuery();

            if (!roleResult.next()) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }

            String role = roleResult.getString("role");

            // Update the users table
            String userUpdateQuery = "UPDATE users SET username = ?, password = ? WHERE username = ?";
            PreparedStatement userUpdateStmt = connection.prepareStatement(userUpdateQuery);
            userUpdateStmt.setString(1, newUsername);
            userUpdateStmt.setString(2, password);
            userUpdateStmt.setString(3, oldUsername);
            userUpdateStmt.executeUpdate();

            // Update the corresponding role table
            String roleUpdateQuery = switch (role) {
                case "Admin" -> "UPDATE admins SET name = ?, email = ?, phone = ? WHERE name = ?";
                case "Doctor" -> "UPDATE doctors SET name = ?, email = ?, phone = ? WHERE name = ?";
                case "Patient" -> "UPDATE patients SET name = ?, email = ?, phone = ? WHERE name = ?";
                default -> throw new IllegalStateException("Unexpected value: " + role);
            };

            PreparedStatement roleUpdateStmt = connection.prepareStatement(roleUpdateQuery);
            roleUpdateStmt.setString(1, name);
            roleUpdateStmt.setString(2, email);
            roleUpdateStmt.setString(3, phone);
            roleUpdateStmt.setString(4, oldUsername);
            int rowsAffected = roleUpdateStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "User update failed. Check if the user exists.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        new UpdateInfoPage("Amin"); // Example username
    }
}
