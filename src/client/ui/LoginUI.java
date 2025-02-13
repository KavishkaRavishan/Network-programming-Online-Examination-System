package client.ui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginUI extends JFrame {
    private Client client;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI(Client client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Exam System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(null);

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(250, 30, 150, 30);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(150, 100, 100, 25);

        usernameField = new JTextField();
        usernameField.setBounds(250, 100, 200, 25);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(150, 150, 100, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(250, 150, 200, 25);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 200, 100, 30);
        loginButton.addActionListener(e -> handleLogin());

        add(titleLabel);
        add(userLabel);
        add(usernameField);
        add(passLabel);
        add(passwordField);
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            client.getOut().println("LOGIN");
            client.getOut().println(username);
            client.getOut().println(password);

            String response = client.getIn().readLine();

            if("Login Successful".equals(response)) {
                if(username.startsWith("admin")) {
                    client.switchToAdmin();
                } else {
                    client.switchToStudent();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "Error communicating with server");
            ex.printStackTrace();
        }
    }
}