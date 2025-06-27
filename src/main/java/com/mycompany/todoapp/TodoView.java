package com.mycompany.todoapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;

public class TodoView {
    private TodoOperations todoOperations;
    private TableView<Todo> tableView;
    private ObservableList<Todo> todoList;

    public TodoView() {
        try {
            todoOperations = new TodoOperations();
            todoList = FXCollections.observableArrayList(todoOperations.getTodos());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error loading todos: " + e.getMessage());
        }
    }

    public VBox getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // ======== Tambahkan Judul di Tengah =========
        Label titleLabel = new Label("✨ Aplikasi Manajemen To-Do - Lita ✨");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #333366;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // Buat HBox supaya lebih teratur, jika mau
        HBox titleContainer = new HBox();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.getChildren().add(titleLabel);

        // =============================================

        // TableView
        tableView = new TableView<>();
        tableView.setItems(todoList);

        // Kolom
        TableColumn<Todo, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        TableColumn<Todo, String> titleColumn = new TableColumn<>("Judul");
        titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<Todo, String> descriptionColumn = new TableColumn<>("Deskripsi");
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());

        TableColumn<Todo, Boolean> isCompletedColumn = new TableColumn<>("Selesai");
        isCompletedColumn.setCellValueFactory(data -> data.getValue().isCompletedProperty());

        TableColumn<Todo, Void> actionColumn = new TableColumn<>("Aksi");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private Button editButton = new Button("✏️ Ubah");
            private Button deleteButton = new Button("🗑️ Hapus");
            private Button markCompletedButton = new Button("✅ Selesai");

            {
                editButton.setStyle("-fx-background-color: #f9c74f; -fx-text-fill: black;");
                deleteButton.setStyle("-fx-background-color: #f94144; -fx-text-fill: white;");
                markCompletedButton.setStyle("-fx-background-color: #90be6d; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editButton.setOnAction(event -> {
                        Todo selected = getTableView().getItems().get(getIndex());
                        showTodoModal(selected);
                    });

                    deleteButton.setOnAction(event -> {
                        Todo selected = getTableView().getItems().get(getIndex());
                        todoOperations.deleteTodo(selected.getId());
                        refreshTable();
                        showSuccess("Todo berhasil dihapus!");
                    });

                    markCompletedButton.setOnAction(event -> {
                        Todo selected = getTableView().getItems().get(getIndex());
                        todoOperations.markAsCompleted(selected.getId());
                        refreshTable();
                        showSuccess("Tugas ditandai selesai!");
                    });

                    HBox buttonContainer = new HBox(5);
                    buttonContainer.getChildren().addAll(editButton, deleteButton, markCompletedButton);
                    setGraphic(buttonContainer);
                }
            }
        });

        tableView.getColumns().addAll(idColumn, titleColumn, descriptionColumn, isCompletedColumn, actionColumn);

        // Tombol Tambah
        Button addButton = new Button("➕ Tambah Tugas Baru");
        addButton.setStyle("-fx-background-color: #577590; -fx-text-fill: white;");
        addButton.setOnAction(e -> showTodoModal(null));

        root.getChildren().addAll(titleContainer, addButton, tableView);
        return root;
    }

    private void showTodoModal(Todo todo) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle(todo == null ? "Tambah Tugas" : "Ubah Tugas");

        TextField titleField = new TextField(todo == null ? "" : todo.getTitle());
        TextField descriptionField = new TextField(todo == null ? "" : todo.getDescription());

        Button saveButton = new Button(todo == null ? "Tambah" : "Simpan");
        saveButton.setStyle("-fx-background-color: #43aa8b; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            if (todo == null) {
                todoOperations.addTodo(new Todo(0, titleField.getText(), descriptionField.getText(), false, null));
                showSuccess("Tugas berhasil ditambahkan!");
            } else {
                todoOperations.updateTodo(todo.getId(), titleField.getText(), descriptionField.getText());
                showSuccess("Tugas berhasil diupdate!");
            }
            refreshTable();
            modalStage.close();
        });

        VBox modalContent = new VBox(10);
        modalContent.setPadding(new Insets(10));
        modalContent.getChildren().addAll(new Label("Judul:"), titleField, new Label("Deskripsi:"), descriptionField, saveButton);

        Scene modalScene = new Scene(modalContent);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private void refreshTable() {
        todoList.setAll(todoOperations.getTodos());
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
