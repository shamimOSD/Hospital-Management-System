package gui;

import db.DBConnection;
import java.sql. ResultSet;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterPage extends JFrame {
    private JTextField usernameField, nameField, emailField, phoneField, specializationField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton registerButton, cancelButton;

    public RegisterPage() {
        setTitle("Register Page");
        JLabel welcomelabel = new JLabel("<-------- CONFIRM YOUR REGISTRATION  -------> ");
        welcomelabel.setBounds(280, 40, 650, 30);
        welcomelabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomelabel.setForeground(Color.cyan);
        add(welcomelabel);

        // Code for Username

        JLabel Username = new JLabel("Username");
        Username.setBounds(140, 110, 250, 30);
        Username.setFont(new Font("Shamim", Font.BOLD, 18));
        Username.setForeground(Color.BLACK);
        add(Username);

        // Code for Password
        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(140, 170, 250, 30);
        passwordlabel.setFont(new Font("Shamim", Font.BOLD, 18));
        passwordlabel.setForeground(Color.BLACK);
        add(passwordlabel);

        // Code for Name
        JLabel namelabel = new JLabel("Name");
        namelabel.setBounds(140, 230, 250, 30);
        namelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        namelabel.setForeground(Color.BLACK);
        add(namelabel);

        // Code for Email
        JLabel Emaillabel = new JLabel("Email");
        Emaillabel.setBounds(140, 290, 250, 30);
        Emaillabel.setFont(new Font("Shamim", Font.BOLD, 18));
        Emaillabel.setForeground(Color.BLACK);
        add(Emaillabel);

        // Code for Phone
        JLabel Phonelabel = new JLabel("Phone");
        Phonelabel.setBounds(140, 350, 250, 30);
        Phonelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        Phonelabel.setForeground(Color.BLACK);
        add(Phonelabel);

        // Code for Role
        JLabel Rolelabel = new JLabel("Role");
        Rolelabel.setBounds(140, 410, 250, 30);
        Rolelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        Rolelabel.setForeground(Color.BLACK);
        add(Rolelabel);

        // Code for Specialization
        JLabel speciallabel = new JLabel("Specialization(For Doctor)");
        speciallabel.setBounds(140, 470, 280, 30);
        speciallabel.setFont(new Font("1234", Font.BOLD, 18));
        speciallabel.setForeground(Color.BLACK);
        add(speciallabel);


        // Code for username field
        usernameField = new JTextField();
        usernameField.setBounds(400, 110, 250, 30);
        usernameField.setFont(new Font("Shamim", Font.BOLD, 15));
        usernameField.setBackground(new Color(255, 179, 0));
        usernameField.setForeground(Color.white);
        add(usernameField);

        // Code for user password field
        passwordField = new JPasswordField();
        passwordField.setBounds(400, 170, 250, 30);
        passwordField.setFont(new Font("Shamim", Font.BOLD, 15));
        passwordField.setBackground(new Color(255, 179, 0));
        passwordField.setForeground(Color.white);
        add(passwordField);

        // Code for NameField
        nameField = new JTextField();
        nameField.setBounds(400, 230, 250, 30);
        nameField.setFont(new Font("Shamim", Font.BOLD, 15));
        nameField.setBackground(new Color(255, 179, 0));
        nameField.setForeground(Color.white);
        add(nameField);

        // Code for EmailField
        emailField = new JTextField();
        emailField.setBounds(400, 290, 250, 30);
        emailField.setFont(new Font("Shamim", Font.BOLD, 15));
        emailField.setBackground(new Color(255, 179, 0));
        emailField.setForeground(Color.white);
        add(emailField);

        // Code for PhoneField
        phoneField = new JTextField();
        phoneField.setBounds(400, 350, 250, 30);
        phoneField.setFont(new Font("Shamim", Font.BOLD, 15));
        phoneField.setBackground(new Color(255, 179, 0));
        phoneField.setForeground(Color.white);
        add(phoneField);

        roleComboBox = new JComboBox<>(new String[]{"patient", "doctor","admin"});
        roleComboBox.setBounds(400, 410, 250, 30);
        roleComboBox.setFont(new Font("Shamim", Font.BOLD, 15));
        roleComboBox.setBackground(new Color(255, 179, 0));
        roleComboBox.setForeground(Color.white);
        add(roleComboBox);

        // Code for SpecializationField
        specializationField = new JTextField();
        specializationField.setBounds(400, 480, 250, 30);
        specializationField.setFont(new Font("Shamim", Font.BOLD, 15));
        specializationField.setBackground(new Color(255, 179, 0));
        specializationField.setForeground(Color.white);
        add(specializationField);

        // Code for Icon interface
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/reg.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(i1);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(720, 100, 600, 400);
        add(label);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(400, 570, 120, 30);
        cancelButton.setFont(new Font("Shamim", Font.BOLD, 15));
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);

        // Cancel Action
        cancelButton.addActionListener(e -> {
            new LoginPage().setVisible(true); // Open LoginPage
            dispose(); // Close current window
        });
        add(cancelButton);

        // Code for SUBMIT
        registerButton = new JButton("SUBMIT");
        registerButton.setBounds(140, 570, 150, 30);
        registerButton.setFont(new Font("Shamim", Font.BOLD, 15));
        registerButton.setBackground( Color.BLACK);
        registerButton.setForeground(Color.WHITE);


       // registerButton.addActionListener(e -> {
            registerButton.addActionListener(e -> {
                handleRegister();

            // Redirect to registration page
           // new RegisterPage().setVisible(true);
            dispose();
        });
        add(registerButton);

        getContentPane().setBackground(new Color(109, 164, 170));
        setSize(1950, 1050);
        setLayout(null);
        setVisible(true);

    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String role = (String) roleComboBox.getSelectedItem();
        String specialization = specializationField.getText();

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            String userQuery = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement userStmt = connection.prepareStatement(userQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, password);
            userStmt.setString(3, role);
            userStmt.executeUpdate();

            ResultSet generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                if ("patient".equals(role)) {
                    String patientQuery = "INSERT INTO patients (id, name, email, phone) VALUES (?, ?, ?, ?)";
                    PreparedStatement patientStmt = connection.prepareStatement(patientQuery);
                    patientStmt.setInt(1, userId);
                    patientStmt.setString(2, name);
                    patientStmt.setString(3, email);
                    patientStmt.setString(4, phone);
                    patientStmt.executeUpdate();
                } else if ("doctor".equals(role)) {
                    String doctorQuery = "INSERT INTO doctors (id, name, email, phone, specialization) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement doctorStmt = connection.prepareStatement(doctorQuery);
                    doctorStmt.setInt(1, userId);
                    doctorStmt.setString(2, name);
                    doctorStmt.setString(3, email);
                    doctorStmt.setString(4, phone);
                    doctorStmt.setString(5, specialization);
                    doctorStmt.executeUpdate();
                }else if ("admin".equals(role)){
                    String adminQuery = "INSERT INTO admins (id, name, email, phone) VALUES (?, ?, ?, ?)";
                    PreparedStatement adminStmt = connection.prepareStatement(adminQuery);
                    adminStmt.setInt(1, userId);
                    adminStmt.setString(2, name);
                    adminStmt.setString(3, email);
                    adminStmt.setString(4, phone);
                    adminStmt.executeUpdate();

                }

            }
            connection.commit();
            JOptionPane.showMessageDialog(this, "Registration successful!");
            new LoginPage().setVisible(true);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
