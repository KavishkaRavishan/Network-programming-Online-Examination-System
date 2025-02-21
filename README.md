# Online Exam System

An advanced **Online Exam System** built with **Java Swing (Client-side)** and **Java Socket Programming (Server-side)**, allowing students to take exams and administrators to manage questions and results.

## 🚀 Features

### 🧑‍🎓 Student Module
- Login authentication
- Select an available exam
- Answer multiple-choice questions
- Submit answers in real-time

### 🛠️ Admin Module
- Create and manage exams
- Add and edit questions
- View students' results

### 🎼 Server-Client Communication
- Uses **Java Sockets** for communication
- Handles multiple clients with **multithreading**
- Secure request-response model

## 🏷️ Tech Stack
- **Java Swing** for GUI
- **Java Socket Programming** for networking
- **Multithreading**
- **JDBC / Database**

## 💻 Installation & Setup

### 1⃣ Clone the Repository
```sh
git clone https://github.com/yourusername/online-exam-system.git
cd online-exam-system
```

### 2⃣ Configure the Database
1. Open `DatabaseConfig.java` and update the following details:
   ```java
   String url = "jdbc:mysql://localhost:3306/online_exam_system";
   String user = "root";
   String password = "yourpassword";
   ```
2. Ensure you have created the `online_exam_system` database in MySQL.
3. Import the required tables using the provided SQL script.

### 3⃣ Download & Add MySQL Connector
1. Download the **MySQL Connector/J** from:
   [https://dev.mysql.com/downloads/connector/j/](https://dev.mysql.com/downloads/connector/j/)
2. Add the JAR file to the project:
   - If using an IDE like **Eclipse** or **IntelliJ**, add it to the classpath.
   - If compiling manually, include it using:
     ```sh
     javac -cp .:mysql-connector-java-x.x.x.jar *.java
     java -cp .:mysql-connector-java-x.x.x.jar Server
     ```

### 4⃣ Start the Server
Ensure the server is running on a machine with **a static local IP address**.
```sh
javac Server.java
java Server
```

### 5⃣ Run the Client
On any machine within the same network:
1. Update the client’s `connectToServer()` method in `Client.java`:
   ```java
   socket = new Socket("192.168.X.X", 12345); // Replace with server’s local IP
   ```
2. Compile & run the client:
   ```sh
   javac Client.java
   java Client
   ```

## 🌐 Running on Two Computers (Same Router)
1. **Find the Server's IP Address**:
   - On the server machine, open **Command Prompt** and type:
     ```sh
     ipconfig
     ```
   - Note the **IPv4 Address** (e.g., `192.168.1.100`).

2. **Update Client’s Connection Details**:
   - Modify `Client.java` to use the **server’s IP address** instead of `localhost`.

3. **Run the Client on Another PC**:
   - Ensure both computers are **connected to the same router**.
   - Run the client as mentioned earlier.

---
**⭐ If you like this project, consider giving it a star on GitHub!**

