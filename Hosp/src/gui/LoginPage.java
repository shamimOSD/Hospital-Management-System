package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton,cancelButton;

    public LoginPage() {
        // Code for welcome
        setTitle("Login");
        JLabel welcomelabel = new JLabel("<-------- WELCOME TO OUR HOSPITAL -------> ");
        welcomelabel.setBounds(280, 40, 650, 30);
        welcomelabel.setFont(new Font("Shamim", Font.BOLD, 26));
        welcomelabel.setForeground(Color.white);
        add(welcomelabel);

        // Code for Username
        JLabel namelabel = new JLabel("Username  :");
        namelabel.setBounds(150, 130, 150, 30);
        namelabel.setFont(new Font("Shamim", Font.BOLD, 18));
        namelabel.setForeground(Color.BLACK);
        add(namelabel);

        // Code for Password
        JLabel passwordlabel = new JLabel("Password  :");
        passwordlabel.setBounds(150, 200, 150, 30);
        passwordlabel.setFont(new Font("1234", Font.BOLD, 18));
        passwordlabel.setForeground(Color.BLACK);
        add(passwordlabel);

        // Code for username field
        usernameField = new JTextField();
        usernameField.setBounds(380, 130, 250, 30);
        usernameField.setFont(new Font("Shamim", Font.BOLD, 15));
        usernameField.setBackground(new Color(255, 179, 0));
        usernameField.setForeground(Color.WHITE);
        add(usernameField);

        // Code for user password field
        passwordField = new JPasswordField();
        passwordField.setBounds(380, 200, 250, 30);
        passwordField.setFont(new Font("Shamim", Font.BOLD, 15));
        passwordField.setBackground(new Color(255, 179, 0));
        passwordField.setForeground(Color.WHITE);
        add(passwordField);

        // Code for Icon interface
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("Icon/Login.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(600, 500, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(i1);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(620, 120, 800, 500);
        add(label);

        // Code for Login and cancel button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 280, 120, 30);
        loginButton.setFont(new Font("Shamim", Font.BOLD, 15));
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginAction());
        add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(380, 280, 120, 30);
        cancelButton.setFont(new Font("Shamim", Font.BOLD, 15));
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);


        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        add(cancelButton);

        // Code for Registration
        registerButton = new JButton("Registration");
        registerButton.setBounds(250, 350, 150, 30);
        registerButton.setFont(new Font("Shamim", Font.BOLD, 15));
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);


        registerButton.addActionListener(e -> {

            // Redirect to registration page
            new RegisterPage().setVisible(true);
            dispose();
        });
        add(registerButton);

        getContentPane().setBackground(new Color(109, 164, 170));
        setSize(1950, 1050);
     //   setLocation(350, 270);
        setLayout(null);
        setVisible(true);
    }
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection connection = DBConnection.getConnection()) {
                String query = "SELECT role FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    JOptionPane.showMessageDialog(null, "Login Successful!");

                    // Navigate based on role
                    if (role.equalsIgnoreCase("admin")) {
                        new AdminPage(username).setVisible(true);
                    } else if (role.equalsIgnoreCase("doctor")) {
                        new DoctorPage(username).setVisible(true);
                    } else if (role.equalsIgnoreCase("patient")) {
                        new PatientPage(username).setVisible(true);
                    }
                   // dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error connecting to the database.");
            }
        }
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
