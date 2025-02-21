package client.ui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentUI extends JFrame {
    private Client client;
    private JComboBox<String> examDropdown;
    private JPanel examSelectionPanel;
    private JPanel questionPanel;
    private JTextArea questionArea;
    private ButtonGroup optionsGroup;
    private JRadioButton[] options;
    private JButton nextButton;
    private List<String[]> currentQuestions;
    private int currentQuestionIndex;

    public StudentUI(Client client) {
        this.client = client;
        initializeUI();
        loadExams();
    }

    private void initializeUI() {
        setTitle("Online Exam System - Student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLayout(null);
        getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Create exam selection panel
        examSelectionPanel = new JPanel();
        examSelectionPanel.setLayout(null);
        examSelectionPanel.setBounds(20, 20, 560, 100);
        examSelectionPanel.setBackground(Color.WHITE);
        examSelectionPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

        JLabel selectLabel = new JLabel("Select Exam:");
        selectLabel.setBounds(30, 30, 100, 25);
        selectLabel.setFont(new Font("Arial", Font.BOLD, 14));

        examDropdown = new JComboBox<>();
        examDropdown.setBounds(140, 30, 200, 30);
        examDropdown.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton startButton = new JButton("Start Exam");
        startButton.setBounds(360, 30, 140, 30);
        styleButton(startButton, new Color(46, 204, 113)); // Green button

        examSelectionPanel.add(selectLabel);
        examSelectionPanel.add(examDropdown);
        examSelectionPanel.add(startButton);

        // Create question panel (initially invisible)
        questionPanel = new JPanel();
        questionPanel.setLayout(null);
        questionPanel.setBounds(20, 130, 560, 280);
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        questionPanel.setVisible(false);

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setBounds(20, 20, 520, 60);
        questionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        questionArea.setBackground(new Color(245, 245, 245)); // Light gray background
        questionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        options = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(30, 90 + (i * 40), 500, 30);
            options[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionsGroup.add(options[i]);
            questionPanel.add(options[i]);
        }

        nextButton = new JButton("Next Question");
        nextButton.setBounds(200, 230, 160, 40);
        styleButton(nextButton, new Color(52, 152, 219)); // Blue button
        nextButton.setEnabled(false);

        questionPanel.add(questionArea);
        questionPanel.add(nextButton);

        // Add panels to frame
        add(examSelectionPanel);
        add(questionPanel);

        // Add action listeners
        startButton.addActionListener(e -> handleStartExam());
        nextButton.addActionListener(e -> handleNextQuestion());

        setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    private void loadExams() {
        try {
            client.getOut().println("GET_EXAMS");

            examDropdown.removeAllItems();
            String exam;
            while (!(exam = client.getIn().readLine()).equals("END")) {
                examDropdown.addItem(exam);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading exams");
            ex.printStackTrace();
        }
    }

    private void handleStartExam() {
        try {
            String selectedExam = (String) examDropdown.getSelectedItem();
            if (selectedExam == null) {
                JOptionPane.showMessageDialog(this, "Please select an exam");
                return;
            }

            client.getOut().println("GET_QUESTIONS");
            client.getOut().println("1"); // Using exam ID 1 for simplicity

            currentQuestions = new ArrayList<>();
            String line;
            while (!(line = client.getIn().readLine()).equals("END")) {
                currentQuestions.add(line.split("\\|"));
            }

            if (currentQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions available for this exam");
                return;
            }

            // Show question panel and hide exam selection panel
            examSelectionPanel.setVisible(false);
            questionPanel.setVisible(true);

            currentQuestionIndex = 0;
            displayCurrentQuestion();
            nextButton.setEnabled(true);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error starting exam");
            ex.printStackTrace();
        }
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex < currentQuestions.size()) {
            String[] question = currentQuestions.get(currentQuestionIndex);
            questionArea.setText("Question " + (currentQuestionIndex + 1) + ": " + question[1]);

            options[0].setText("A) " + question[2]);
            options[1].setText("B) " + question[3]);
            options[2].setText("C) " + question[4]);
            options[3].setText("D) " + question[5]);

            optionsGroup.clearSelection();
        }
    }

    private void handleNextQuestion() {
        String selectedAnswer = null;
        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selectedAnswer = String.valueOf((char) ('A' + i));
                break;
            }
        }

        if (selectedAnswer == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer");
            return;
        }

        try {
            client.getOut().println("SUBMIT_ANSWER");
            client.getOut().println("2"); // Student ID
            client.getOut().println("1"); // Exam ID
            client.getOut().println(currentQuestions.get(currentQuestionIndex)[0]); // Question ID
            client.getOut().println(selectedAnswer);

            String response = client.getIn().readLine(); // Read server response

            currentQuestionIndex++;

            if (currentQuestionIndex >= currentQuestions.size()) {
                JOptionPane.showMessageDialog(this, "Exam completed!");
                nextButton.setEnabled(false);
                examSelectionPanel.setVisible(true);
                questionPanel.setVisible(false);
            } else {
                displayCurrentQuestion();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error submitting answer");
            ex.printStackTrace();
        }
    }
}
