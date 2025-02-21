//package client.ui;
//
//import client.Client;
//import server.db.ExamDAO;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class AdminUI extends JFrame {
//    private Client client;
//    private JTextField examField, questionField, optionAField, optionBField, optionCField, optionDField, correctOptionField;
//    private JButton submitButton;
//
//    public AdminUI(Client client) {
//        this.client = client;
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        setTitle("Online Exam System - Admin");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(600, 500);
//        setLocationRelativeTo(null);  // Center the window
//        setLayout(new BorderLayout(10, 10));
//
//        JTabbedPane tabbedPane = new JTabbedPane();
//        JPanel addQuestionPanel = new JPanel(new BorderLayout());
//
//        // Panel to hold form inputs
//        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
//        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        formPanel.add(new JLabel("Exam Name:"));
//        examField = new JTextField();
//        formPanel.add(examField);
//
//        formPanel.add(new JLabel("Question:"));
//        questionField = new JTextField();
//        formPanel.add(questionField);
//
//        formPanel.add(new JLabel("Option A:"));
//        optionAField = new JTextField();
//        formPanel.add(optionAField);
//
//        formPanel.add(new JLabel("Option B:"));
//        optionBField = new JTextField();
//        formPanel.add(optionBField);
//
//        formPanel.add(new JLabel("Option C:"));
//        optionCField = new JTextField();
//        formPanel.add(optionCField);
//
//        formPanel.add(new JLabel("Option D:"));
//        optionDField = new JTextField();
//        formPanel.add(optionDField);
//
//        formPanel.add(new JLabel("Correct Option (A/B/C/D):"));
//        correctOptionField = new JTextField();
//        formPanel.add(correctOptionField);
//
//        addQuestionPanel.add(formPanel, BorderLayout.CENTER);
//
//        // Button Panel with styling
//        JPanel buttonPanel = new JPanel();
//        submitButton = new JButton("➕ Add Question");
//
//        // Style the button
//        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
//        submitButton.setForeground(Color.WHITE);
//        submitButton.setBackground(new Color(30, 144, 255)); // DodgerBlue
//        submitButton.setOpaque(true);
//        submitButton.setBorderPainted(false);
//        submitButton.setFocusPainted(false);
//        submitButton.setPreferredSize(new Dimension(200, 40)); // Set button size
//
//        // Add hover effect
//        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                submitButton.setBackground(new Color(0, 102, 204)); // Darker blue
//            }
//
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                submitButton.setBackground(new Color(30, 144, 255)); // Back to original color
//            }
//        });
//
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addNewQuestion();
//            }
//        });
//
//        buttonPanel.add(submitButton);
//        addQuestionPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        tabbedPane.addTab("Add Question", addQuestionPanel);
//        add(tabbedPane, BorderLayout.CENTER);
//
//        setVisible(true);
//    }
//
//    private void addNewQuestion() {
//        String examName = examField.getText().trim();
//        String questionText = questionField.getText().trim();
//        String optionA = optionAField.getText().trim();
//        String optionB = optionBField.getText().trim();
//        String optionC = optionCField.getText().trim();
//        String optionD = optionDField.getText().trim();
//        String correctOption = correctOptionField.getText().trim().toUpperCase();
//
//        if (examName.isEmpty() || questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
//                optionC.isEmpty() || optionD.isEmpty() || correctOption.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        if (!correctOption.matches("[ABCD]")) {
//            JOptionPane.showMessageDialog(this, "Correct option must be A, B, C, or D", "Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        boolean success = ExamDAO.addQuestion(examName, questionText, optionA, optionB, optionC, optionD, correctOption);
//        if (success) {
//            JOptionPane.showMessageDialog(this, "Question added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
//            resetForm();  // Reset form after successful addition
//        } else {
//            JOptionPane.showMessageDialog(this, "Failed to add question", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void resetForm() {
//        examField.setText("");
//        questionField.setText("");
//        optionAField.setText("");
//        optionBField.setText("");
//        optionCField.setText("");
//        optionDField.setText("");
//        correctOptionField.setText("");
//    }
//}

package client.ui;

import client.Client;
import server.db.ExamDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI extends JFrame {
    private Client client;
    private JTextField examField, questionField, optionAField, optionBField, optionCField, optionDField, correctOptionField;
    private JButton submitButton;

    public AdminUI(Client client) {
        this.client = client;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Online Exam System - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Panel with padding
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); // Top padding added

        JLabel titleLabel = new JLabel("Add Question", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Exam Name:"));
        examField = new JTextField();
        formPanel.add(examField);

        formPanel.add(new JLabel("Question:"));
        questionField = new JTextField();
        formPanel.add(questionField);

        formPanel.add(new JLabel("Option A:"));
        optionAField = new JTextField();
        formPanel.add(optionAField);

        formPanel.add(new JLabel("Option B:"));
        optionBField = new JTextField();
        formPanel.add(optionBField);

        formPanel.add(new JLabel("Option C:"));
        optionCField = new JTextField();
        formPanel.add(optionCField);

        formPanel.add(new JLabel("Option D:"));
        optionDField = new JTextField();
        formPanel.add(optionDField);

        formPanel.add(new JLabel("Correct Option (A/B/C/D):"));
        correctOptionField = new JTextField();
        formPanel.add(correctOptionField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        submitButton = new JButton("➕ Add Question");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(30, 144, 255));
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(false);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 40));

        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(0, 102, 204));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setBackground(new Color(30, 144, 255));
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewQuestion();
            }
        });

        buttonPanel.add(submitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void addNewQuestion() {
        String examName = examField.getText().trim();
        String questionText = questionField.getText().trim();
        String optionA = optionAField.getText().trim();
        String optionB = optionBField.getText().trim();
        String optionC = optionCField.getText().trim();
        String optionD = optionDField.getText().trim();
        String correctOption = correctOptionField.getText().trim().toUpperCase();

        if (examName.isEmpty() || questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || correctOption.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!correctOption.matches("[ABCD]")) {
            JOptionPane.showMessageDialog(this, "Correct option must be A, B, C, or D", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = ExamDAO.addQuestion(examName, questionText, optionA, optionB, optionC, optionD, correctOption);
        if (success) {
            JOptionPane.showMessageDialog(this, "Question added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add question", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        examField.setText("");
        questionField.setText("");
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        correctOptionField.setText("");
    }
}
