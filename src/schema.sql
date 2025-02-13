-- 1. Create the database
CREATE DATABASE IF NOT EXISTS online_exam_system;
USE online_exam_system;

-- 2. Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('student', 'admin') NOT NULL
);

-- 3. Create exams table
CREATE TABLE IF NOT EXISTS exams (
    exam_id INT AUTO_INCREMENT PRIMARY KEY,
    exam_name VARCHAR(255) NOT NULL,
    created_by INT,  -- User ID of the admin who created the exam
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 4. Create questions table
CREATE TABLE IF NOT EXISTS questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    exam_id INT,  -- Exam ID to which this question belongs
    question_text TEXT NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option CHAR(1) NOT NULL,  -- 'A', 'B', 'C', or 'D'
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id)
);

-- 5. Create student_answers table
CREATE TABLE IF NOT EXISTS student_answers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    exam_id INT,
    question_id INT,
    answer CHAR(1),  -- 'A', 'B', 'C', 'D'
    FOREIGN KEY (student_id) REFERENCES users(user_id),
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
);

-- 6. Add sample data (for testing)
-- Add sample users (1 student, 1 admin)
INSERT INTO users (username, password, role) VALUES ('admin1', 'admin1', 'admin');
INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'admin');

INSERT INTO users (username, password, role) VALUES ('s1', 's1', 'student');

-- Add a sample exam
INSERT INTO exams (exam_name, created_by) VALUES ('Java Programming', 1);  -- Admin ID is 1

-- Add sample questions for the Java Programming exam
INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option)
VALUES
(1, 'What is Java?', 'A programming language', 'A fruit', 'A website', 'A game', 'A'),
(1, 'Which of these is a data type in Java?', 'int', 'float', 'String', 'All of the above', 'D');