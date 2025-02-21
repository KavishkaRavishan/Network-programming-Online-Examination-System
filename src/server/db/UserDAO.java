package server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If a record is found, authentication is successful
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Authentication failed
    }

    // Get list of available exams
    public static List<String> getExams() {
        List<String> exams = new ArrayList<>();
        String query = "SELECT exam_name FROM exams";

        try (Connection connection = DatabaseConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                exams.add(rs.getString("exam_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    // Get questions for a selected exam
    public static List<String[]> getQuestions(int examId) {
        List<String[]> questions = new ArrayList<>();
        String query = "SELECT question_id, question_text, option_a, option_b, option_c, option_d FROM questions WHERE exam_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, examId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                questions.add(new String[]{
                        rs.getString("question_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Submit student answer
    public static void submitAnswer(int studentId, int examId, int questionId, String answer) {
        String query = "INSERT INTO student_answers (student_id, exam_id, question_id, answer) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            stmt.setInt(3, questionId);
            stmt.setString(4, answer);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add new exam
    public static void addExam(String examName, int adminId) {
        String query = "INSERT INTO exams (exam_name, created_by) VALUES (?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, examName);
            stmt.setInt(2, adminId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add question to an exam
    public static void addQuestion(int examId, String text, String a, String b, String c, String d, String correct) {
        String query = "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, examId);
            stmt.setString(2, text);
            stmt.setString(3, a);
            stmt.setString(4, b);
            stmt.setString(5, c);
            stmt.setString(6, d);
            stmt.setString(7, correct);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get student marks
    public static int getStudentMarks(int studentId, int examId) {
        String query = "SELECT COUNT(*) AS score FROM student_answers sa JOIN questions q ON sa.question_id = q.question_id WHERE sa.student_id = ? AND sa.exam_id = ? AND sa.answer = q.correct_option";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, examId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
