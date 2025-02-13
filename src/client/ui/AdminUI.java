package client.ui;

import client.Client;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JFrame {
    private Client client;

    public AdminUI(Client client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Exam System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Add admin-specific components here
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create Exam Panel
        JPanel createExamPanel = new JPanel();
        // Add components for creating exams
        tabbedPane.addTab("Create Exam", createExamPanel);

        // Manage Questions Panel
        JPanel manageQuestionsPanel = new JPanel();
        // Add components for managing questions
        tabbedPane.addTab("Manage Questions", manageQuestionsPanel);

        // View Results Panel
        JPanel viewResultsPanel = new JPanel();
        // Add components for viewing results
        tabbedPane.addTab("View Results", viewResultsPanel);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }
}