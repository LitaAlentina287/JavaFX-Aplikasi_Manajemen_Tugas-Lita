# 📋 JavaFX - Aplikasi Manajemen Tugas

Aplikasi ini dibuat menggunakan JavaFX sebagai bagian dari tugas mata kuliah PBO2. Tujuannya adalah untuk membantu pengguna dalam mencatat, mengelola, dan menyelesaikan tugas harian mereka dengan antarmuka yang sederhana dan interaktif.

## ✨ Fitur Utama
- Tambah tugas baru
- Hapus dan edit tugas
- Tandai tugas yang sudah selesai
- Antarmuka menggunakan JavaFX (FXML)

## 💻 Teknologi yang Digunakan
- Java
- JavaFX
- Scene Builder
- Apache NetBeans IDE
- Git & GitHub

## 📽️ Demo Aplikasi
Tonton demo aplikasi di YouTube:  
🔗 [https://www.youtube.com/watch?v=MASUKKAN_LINK_KAMU_DI_SINI](https://youtu.be/klz2-AVIM1Q?si=6h_i4sEySB_GH3p2)


## 👩‍💻 Pengembang
**Lita Alentina**

---
---

## 🧩 Kode Program Utama

### 📁 File: App.java
```java
package com.mycompany.helloworldtodo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();
            TodoView todoView = new TodoView();
            root.setCenter(todoView.getView());

            Scene scene = new Scene(root, 800, 600);

            primaryStage.setTitle("To-Do Manager");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

### 📁 File: DatabaseConnection.java
```java
package com.mycompany.todoapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/todo_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Failed to close database connection.");
                e.printStackTrace();
            }
        }
    }
}
```

### 📁 File: Todo.java
```java
package com.mycompany.todoapp;

import javafx.beans.property.*;

public class Todo {
    private IntegerProperty id;
    private StringProperty title;
    private StringProperty description;
    private BooleanProperty isCompleted;
    private StringProperty createdAt;

    public Todo(int id, String title, String description, boolean isCompleted, String createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.isCompleted = new SimpleBooleanProperty(isCompleted);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getTitle() { return title.get(); }
    public StringProperty titleProperty() { return title; }
    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public boolean isIsCompleted() { return isCompleted.get(); }
    public BooleanProperty isCompletedProperty() { return isCompleted; }
    public String getCreatedAt() { return createdAt.get(); }
    public StringProperty createdAtProperty() { return createdAt; }
}
```

### 📁 File: TodoOperations.java
```java
package com.mycompany.todoapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoOperations {
    private Connection connection;

    public TodoOperations() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    public void addTodo(Todo todo) {
        String query = "INSERT INTO todos (title, description) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, todo.getTitle());
            stmt.setString(2, todo.getDescription());
            stmt.executeUpdate();
            System.out.println("To-Do added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Todo> getTodos() {
        List<Todo> todos = new ArrayList<>();
        String query = "SELECT * FROM todos";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                todos.add(new Todo(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getBoolean("is_completed"),
                    rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    public void updateTodo(int id, String newTitle, String newDescription) {
        String query = "UPDATE todos SET title = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newDescription);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            System.out.println("To-Do updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTodo(int id) {
        String query = "DELETE FROM todos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("To-Do deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void markAsCompleted(int id) {
        String query = "UPDATE todos SET is_completed = TRUE WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("To-Do marked as completed!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### 📁 File: TodoView.java
(Paste lengkap di bagian selanjutnya karena terlalu panjang untuk satu sel)

---

## 🗃️ Struktur Database (MySQL)
```sql
CREATE DATABASE todo_system;

USE todo_system;

CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS todos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

