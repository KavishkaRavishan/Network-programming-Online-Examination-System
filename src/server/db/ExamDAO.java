package server.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamDAO {
    public static List<String> getAllExamNames() {
        List<String> exams = new ArrayList<>();
        String query = "SELECT exam_name FROM exams";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                exams.add(rs.getString("exam_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public static List<String[]> getExamQuestions(String examName) {
        List<String[]> questions = new ArrayList<>();
        String query = "SELECT question_text, option_a, option_b, option_c, option_d, correct_option FROM questions WHERE exam_name = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, examName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] questionData = {
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                };
                questions.add(questionData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static boolean checkAnswer(String examName, String questionText, String selectedAnswer) {
        String query = "SELECT correct_option FROM questions WHERE exam_name = ? AND question_text = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, examName);
            stmt.setString(2, questionText);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("correct_option").equalsIgnoreCase(selectedAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void storeStudentAnswer(String examName, String questionText, String selectedAnswer, boolean isCorrect) {
        String query = "INSERT INTO student_answers (exam_name, question_text, selected_answer, is_correct) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, examName);
            stmt.setString(2, questionText);
            stmt.setString(3, selectedAnswer);
            stmt.setBoolean(4, isCorrect);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean addQuestion(String examName, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        String query = "INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) " +
                "VALUES ((SELECT exam_id FROM exams WHERE exam_name = ?), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, examName);
            stmt.setString(2, questionText);
            stmt.setString(3, optionA);
            stmt.setString(4, optionB);
            stmt.setString(5, optionC);
            stmt.setString(6, optionD);
            stmt.setString(7, correctOption);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
