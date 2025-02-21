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

### 📡 Server-Client Communication
- Uses **Java Sockets** for communication
- Handles multiple clients with **multithreading** (if implemented in server)
- Secure request-response model

## 🏗️ Tech Stack
- **Java Swing** for GUI
- **Java Socket Programming** for networking
- **Multithreading** (if enabled on the server)
- **JDBC / Database** (if database integration is added)

## 💻 Installation & Setup

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/yourusername/online-exam-system.git
cd online-exam-system
```

### 2️⃣ Start the Server
Ensure the server is running on a machine with **a static local IP address**.
```sh
javac Server.java
java Server
```

### 3️⃣ Run the Client
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

## 🛠️ How to Contribute
1. **Fork the repo** and create a new branch:
   ```sh
   git checkout -b feature-new-improvement
   ```
2. **Make changes & commit**:
   ```sh
   git commit -m "Added new feature"
   ```
3. **Push & create a Pull Request**:
   ```sh
   git push origin feature-new-improvement
   ```

---
**⭐ If you like this project, consider giving it a star on GitHub!**

