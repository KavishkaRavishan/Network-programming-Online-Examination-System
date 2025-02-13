package client;

import client.ui.AdminUI;
import client.ui.LoginUI;
import client.ui.StudentUI;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private LoginUI loginUI;
    private StudentUI studentUI;
    private AdminUI adminUI;

    public Client() {
        connectToServer();
        loginUI = new LoginUI(this);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server");
            System.exit(1);
        }
    }

    public PrintWriter getOut() { return out; }
    public BufferedReader getIn() { return in; }

    public void switchToStudent() {
        loginUI.dispose();
        studentUI = new StudentUI(this);
    }

    public void switchToAdmin() {
        loginUI.dispose();
        adminUI = new AdminUI(this);
    }

    public void logout() {
        if (studentUI != null) {
            studentUI.dispose();
            studentUI = null;
        }
        if (adminUI != null) {
            adminUI.dispose();
            adminUI = null;
        }
        loginUI = new LoginUI(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client());
    }
}