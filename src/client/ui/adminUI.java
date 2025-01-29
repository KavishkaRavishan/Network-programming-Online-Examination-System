package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class adminUI {
    private JFrame frame;
    private JTextField examField, questionField, optionAField, optionBField, optionCField, optionDField, correctOptionField;
    private JButton addExamButton, addQuestionButton, viewMarksButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                adminUI window = new adminUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public adminUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Admin Panel");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblExam = new JLabel("Exam Name:");
        lblExam.setBounds(50, 20, 100, 25);
        frame.getContentPane().add(lblExam);

        examField = new JTextField();
        examField.setBounds(150, 20, 200, 25);
        frame.getContentPane().add(examField);

        addExamButton = new JButton("Add Exam");
        addExamButton.setBounds(360, 20, 100, 25);
        frame.getContentPane().add(addExamButton);

        addExamButton.addActionListener(e -> addExam());

        // Similarly, add fields and buttons for adding questions and viewing marks...
    }

    private void addExam() {
        String examName = examField.getText();

        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("ADD_EXAM");
            out.println(examName);
            JOptionPane.showMessageDialog(frame, "Exam Added!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
