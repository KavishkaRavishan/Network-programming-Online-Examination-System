package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class studentUI {
    private JFrame frame;
    private JComboBox<String> examDropdown;
    private JButton startExamButton;
    private JLabel questionLabel;
    private JRadioButton optionA, optionB, optionC, optionD;
    private ButtonGroup optionsGroup;
    private JButton nextButton;
    private List<String[]> questions;
    private int currentQuestionIndex = 0;
    private String selectedExam;
    private int studentId = 2;  // Assuming student ID 2 (you can modify this)

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                studentUI window = new studentUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public studentUI() {
        initialize();
        loadExams();
    }

    private void initialize() {
        frame = new JFrame("Student Exam System");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblSelectExam = new JLabel("Select Exam:");
        lblSelectExam.setBounds(50, 20, 100, 25);
        frame.getContentPane().add(lblSelectExam);

        examDropdown = new JComboBox<>();
        examDropdown.setBounds(150, 20, 200, 25);
        frame.getContentPane().add(examDropdown);

        startExamButton = new JButton("Start Exam");
        startExamButton.setBounds(360, 20, 100, 25);
        frame.getContentPane().add(startExamButton);

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setBounds(50, 80, 400, 25);
        frame.getContentPane().add(questionLabel);

        optionA = new JRadioButton();
        optionA.setBounds(50, 120, 400, 25);
        frame.getContentPane().add(optionA);

        optionB = new JRadioButton();
        optionB.setBounds(50, 150, 400, 25);
        frame.getContentPane().add(optionB);

        optionC = new JRadioButton();
        optionC.setBounds(50, 180, 400, 25);
        frame.getContentPane().add(optionC);

        optionD = new JRadioButton();
        optionD.setBounds(50, 210, 400, 25);
        frame.getContentPane().add(optionD);

        optionsGroup = new ButtonGroup();
        optionsGroup.add(optionA);
        optionsGroup.add(optionB);
        optionsGroup.add(optionC);
        optionsGroup.add(optionD);

        nextButton = new JButton("Next Question");
        nextButton.setBounds(180, 250, 150, 30);
        frame.getContentPane().add(nextButton);
        nextButton.setEnabled(false);

        startExamButton.addActionListener(e -> startExam());
        nextButton.addActionListener(e -> submitAnswerAndNext());
    }

    private void loadExams() {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET_EXAMS");

            String exam;
            while (!(exam = in.readLine()).equals("END")) {
                examDropdown.addItem(exam);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startExam() {
        selectedExam = (String) examDropdown.getSelectedItem();
        questions = new ArrayList<>();
        currentQuestionIndex = 0;

        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET_QUESTIONS");
            out.println(1);  // Assuming exam ID 1 (modify accordingly)

            String line;
            while (!(line = in.readLine()).equals("END")) {
                questions.add(line.split("\\|"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            String[] q = questions.get(currentQuestionIndex);
            questionLabel.setText(q[1]);
            optionA.setText("A. " + q[2]);
            optionB.setText("B. " + q[3]);
            optionC.setText("C. " + q[4]);
            optionD.setText("D. " + q[5]);
            nextButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Exam Completed!");
            nextButton.setEnabled(false);
        }
    }

    private void submitAnswerAndNext() {
        String selectedAnswer = "";
        if (optionA.isSelected()) selectedAnswer = "A";
        else if (optionB.isSelected()) selectedAnswer = "B";
        else if (optionC.isSelected()) selectedAnswer = "C";
        else if (optionD.isSelected()) selectedAnswer = "D";

        if (!selectedAnswer.isEmpty()) {
            try (Socket socket = new Socket("localhost", 12345);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                out.println("SUBMIT_ANSWER");
                out.println(studentId);
                out.println(1); // Assuming exam ID 1 (modify accordingly)
                out.println(questions.get(currentQuestionIndex)[0]); // Question ID
                out.println(selectedAnswer);

            } catch (IOException e) {
                e.printStackTrace();
            }

            currentQuestionIndex++;
            showQuestion();
        }
    }
}
