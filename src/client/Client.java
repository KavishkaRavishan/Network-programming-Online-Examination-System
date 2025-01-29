package client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> examDropdown;
    private JTextArea questionArea;
    private ButtonGroup optionsGroup;
    private JRadioButton[] options;
    private JButton nextButton;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private List<String[]> currentQuestions;
    private int currentQuestionIndex;

    public Client() {
        initializeUI();
        connectToServer();
    }

    private void initializeUI() {
        frame = new JFrame("Online Exam System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createExamPanel();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Could not connect to server");
            System.exit(1);
        }
    }

    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(null);

        JLabel titleLabel = new JLabel("Student Login");
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

        loginPanel.add(titleLabel);
        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        mainPanel.add(loginPanel, "LOGIN");
    }

    private void createExamPanel() {
        JPanel examPanel = new JPanel(null);

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
            examPanel.add(options[i]);
        }

        nextButton = new JButton("Next Question");
        nextButton.setBounds(250, 310, 120, 30);
        nextButton.setEnabled(false);

        startButton.addActionListener(e -> handleStartExam());
        nextButton.addActionListener(e -> handleNextQuestion());

        examPanel.add(selectLabel);
        examPanel.add(examDropdown);
        examPanel.add(startButton);
        examPanel.add(questionArea);
        examPanel.add(nextButton);

        mainPanel.add(examPanel, "EXAM");
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            out.println("LOGIN");
            out.println(username);
            out.println(password);

            String response = in.readLine();

            if("Login Successful".equals(response)) {
                loadExams();
                cardLayout.show(mainPanel, "EXAM");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password");
            }
        } catch(IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error communicating with server");
            ex.printStackTrace();
        }
    }

    private void loadExams() throws IOException {
        out.println("GET_EXAMS");

        examDropdown.removeAllItems();
        String exam;
        while(!(exam = in.readLine()).equals("END")) {
            examDropdown.addItem(exam);
        }
    }

    private void handleStartExam() {
        try {
            String selectedExam = (String) examDropdown.getSelectedItem();
            if(selectedExam == null) {
                JOptionPane.showMessageDialog(frame, "Please select an exam");
                return;
            }

            out.println("GET_QUESTIONS");
            out.println("1");  // Using exam ID 1 for simplicity

            currentQuestions = new ArrayList<>();
            String line;
            while(!(line = in.readLine()).equals("END")) {
                currentQuestions.add(line.split("\\|"));
            }

            if(currentQuestions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No questions available for this exam");
                return;
            }

            currentQuestionIndex = 0;
            displayCurrentQuestion();
            nextButton.setEnabled(true);

        } catch(IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error starting exam");
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
            JOptionPane.showMessageDialog(frame, "Please select an answer");
            return;
        }

        try {
            out.println("SUBMIT_ANSWER");
            out.println("2");  // student ID
            out.println("1");  // exam ID
            out.println(currentQuestions.get(currentQuestionIndex)[0]); // question ID
            out.println(selectedAnswer);

            String response = in.readLine(); // Read the server's response

            currentQuestionIndex++;

            if(currentQuestionIndex >= currentQuestions.size()) {
                JOptionPane.showMessageDialog(frame, "Exam completed!");
                nextButton.setEnabled(false);
            } else {
                displayCurrentQuestion();
            }

        } catch(IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error submitting answer");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client());
    }
}