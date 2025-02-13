package client.ui;

import client.Client;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentUI extends JFrame {
    private Client client;
    private JComboBox<String> examDropdown;
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
        setSize(600, 400);
        setLayout(null);

        JLabel selectLabel = new JLabel("Select Exam:");
        selectLabel.setBounds(50, 30, 100, 25);

        examDropdown = new JComboBox<>();
        examDropdown.setBounds(150, 30, 200, 25);

        JButton startButton = new JButton("Start Exam");
        startButton.setBounds(370, 30, 100, 25);

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setBounds(50, 80, 500, 60);

        options = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for(int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(50, 150 + (i * 40), 500, 30);
            optionsGroup.add(options[i]);
            add(options[i]);
        }

        nextButton = new JButton("Next Question");
        nextButton.setBounds(250, 310, 120, 30);
        nextButton.setEnabled(false);

        startButton.addActionListener(e -> handleStartExam());
        nextButton.addActionListener(e -> handleNextQuestion());

        add(selectLabel);
        add(examDropdown);
        add(startButton);
        add(questionArea);
        add(nextButton);

        setVisible(true);
    }

    private void loadExams() {
        try {
            client.getOut().println("GET_EXAMS");

            examDropdown.removeAllItems();
            String exam;
            while(!(exam = client.getIn().readLine()).equals("END")) {
                examDropdown.addItem(exam);
            }
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading exams");
            ex.printStackTrace();
        }
    }

    private void handleStartExam() {
        try {
            String selectedExam = (String) examDropdown.getSelectedItem();
            if(selectedExam == null) {
                JOptionPane.showMessageDialog(this, "Please select an exam");
                return;
            }

            client.getOut().println("GET_QUESTIONS");
            client.getOut().println("1");  // Using exam ID 1 for simplicity

            currentQuestions = new ArrayList<>();
            String line;
            while(!(line = client.getIn().readLine()).equals("END")) {
                currentQuestions.add(line.split("\\|"));
            }

            if(currentQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions available for this exam");
                return;
            }

            currentQuestionIndex = 0;
            displayCurrentQuestion();
            nextButton.setEnabled(true);

        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "Error starting exam");
            ex.printStackTrace();
        }
    }

    private void displayCurrentQuestion() {
        if(currentQuestionIndex < currentQuestions.size()) {
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
        for(int i = 0; i < options.length; i++) {
            if(options[i].isSelected()) {
                selectedAnswer = String.valueOf((char)('A' + i));
                break;
            }
        }

        if(selectedAnswer == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer");
            return;
        }

        try {
            client.getOut().println("SUBMIT_ANSWER");
            client.getOut().println("2");  // student ID
            client.getOut().println("1");  // exam ID
            client.getOut().println(currentQuestions.get(currentQuestionIndex)[0]); // question ID
            client.getOut().println(selectedAnswer);

            String response = client.getIn().readLine(); // Read the server's response

            currentQuestionIndex++;

            if(currentQuestionIndex >= currentQuestions.size()) {
                JOptionPane.showMessageDialog(this, "Exam completed!");
                nextButton.setEnabled(false);
            } else {
                displayCurrentQuestion();
            }

        } catch(IOException ex) {
            JOptionPane.showMessageDialog(this, "Error submitting answer");
            ex.printStackTrace();
        }
    }
}