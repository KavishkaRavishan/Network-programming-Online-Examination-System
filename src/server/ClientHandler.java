package server;

import server.db.UserDAO;
import java.io.*;
import java.net.*;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String command = in.readLine();
                if (command == null) break;

                switch (command) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "GET_EXAMS":
                        handleGetExams();
                        break;
                    case "GET_QUESTIONS":
                        handleGetQuestions();
                        break;
                    case "SUBMIT_ANSWER":
                        handleSubmitAnswer();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleLogin() throws IOException {
        String username = in.readLine();
        String password = in.readLine();
        boolean isAuthenticated = UserDAO.authenticateUser(username, password);
        out.println(isAuthenticated ? "Login Successful" : "Login Failed");
    }

    private void handleGetExams() {
        List<String> exams = UserDAO.getExams();
        for (String exam : exams) {
            out.println(exam);
        }
        out.println("END");
    }

    private void handleGetQuestions() throws IOException {
        int examId = Integer.parseInt(in.readLine());
        List<String[]> questions = UserDAO.getQuestions(examId);
        for (String[] question : questions) {
            out.println(String.join("|", question));
        }
        out.println("END");
    }

    private void handleSubmitAnswer() throws IOException {
        int studentId = Integer.parseInt(in.readLine());
        int examId = Integer.parseInt(in.readLine());
        int questionId = Integer.parseInt(in.readLine());
        String answer = in.readLine();
        UserDAO.submitAnswer(studentId, examId, questionId, answer);
        out.println("Answer submitted");
    }
}