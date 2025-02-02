package com.hc.proj9ledge;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.Desktop;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.util.Stack;

public class MainViewController {
    @FXML
    private TreeView<Object> folderTreeView;

    @FXML
    private TextField searchField;

    @FXML
    private Button addFolderButton;

    @FXML
    private Button addFileButton;

    @FXML
    private Button searchButton;

    @FXML
    private StackPane contentArea;

    public void initialize() {

        FolderTreeViewService.populateFolderTreeView(folderTreeView);

        setupEventHandlers();

        folderTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showFolderContents(newValue);
            }
        });

    }

    private void showFolderContents(TreeItem<Object> selectedItem) {
        contentArea.getChildren().clear();

        Folder folder = selectedItem.getValue() instanceof Folder ? (Folder) selectedItem.getValue() : null;
        if (folder == null) return;

        List<com.hc.proj9ledge.File> files = FileDAO.getFilesByFolderId(folder.getFolderId());

        VBox fileContainer = new VBox(10);

        for (com.hc.proj9ledge.File file : files) {
            HBox fileItem = new HBox(10);

            ImageView fileIcon = new ImageView(getClass().getResource("/com/hc/proj9ledge/fileIcon.png").toString());
            fileIcon.setFitWidth(20);
            fileIcon.setFitHeight(20);

            Label fileLabel = new Label(file.getFileName());
            fileLabel.setFont(new Font(14));
            fileLabel.setTextFill(Color.BLACK);

            fileLabel.setOnMouseClicked(event -> openWordFile(file.getFilePath()));

            fileItem.getChildren().addAll(fileIcon, fileLabel);

            fileContainer.getChildren().add(fileItem);
        }
    }

    private void openWordFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                showAlert("Error", "File not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open file.");
        }
    }


    private void loadFilesIntoTreeView(TreeItem<Object> folderItem) {
        if (folderItem == null) return;

        Folder folder = folderItem.getValue() instanceof Folder ? (Folder) folderItem.getValue() : null;
        if (folder == null) return;

        folderItem.getChildren().clear();

        List<com.hc.proj9ledge.File> files = FileDAO.getFilesByFolderId(folder.getFolderId());

        for (com.hc.proj9ledge.File file : files) {
            TreeItem<Object> fileItem = new TreeItem<>(file);
            folderItem.getChildren().add(fileItem);
        }
    }

    private void setupEventHandlers() {

        addFolderButton.setOnAction(event -> createFolder());

        addFileButton.setOnAction(event -> createFile());

        searchButton.setOnAction(event -> searchFoldersAndFiles());
    }

    private void createFolder() {

        String folderName = getFolderNameFromUser();

        if (folderName != null && !folderName.trim().isEmpty()) {
            TreeItem<Object> selectedItem = folderTreeView.getSelectionModel().getSelectedItem();
            int parentId = FolderDAO.getParentId();

            if (selectedItem != null) {
                Folder parentFolder = selectedItem.getValue() instanceof Folder ? (Folder) selectedItem.getValue() : null;
                if (parentFolder != null) {
                    parentId = parentFolder.getFolderId();
                    System.out.println("Parent folder: " + parentFolder.getFolderName() + " (ID: " + parentId + ")");
                }
            }

            Folder newFolder = new Folder(folderName,parentId);
            FolderService.createFolder(newFolder);
            TreeItem<Object> newFolderItem = new TreeItem<>(newFolder);

            if (selectedItem == null) {
                folderTreeView.getRoot().getChildren().add(newFolderItem);
            } else {
                selectedItem.getChildren().add(newFolderItem);
                selectedItem.setExpanded(true);
            }
            loadFilesIntoTreeView(newFolderItem);
        }
    }
    private void createFile() {
        String fileName = getFileNameFromUser();

        if (fileName != null && !fileName.trim().isEmpty()) {
            TreeItem<Object> selectedItem = folderTreeView.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                showAlert("Error", "Please select a folder before adding a file.");
                return;
            }

            Folder parentFolder = selectedItem.getValue() instanceof Folder ? (Folder) selectedItem.getValue() : null;
            if (parentFolder == null) {
                showAlert("Error", "Selected folder is invalid.");
                return;
            }

            int folderId = parentFolder.getFolderId();

            String folderPath = buildFolderPath(parentFolder);
            String filePath = folderPath + File.separator + fileName + ".docx";

            com.hc.proj9ledge.File newFile = new com.hc.proj9ledge.File(0, fileName, folderId, filePath);

            FileService.createFile(newFile);

            TreeItem<Object> fileItem = new TreeItem<>(newFile);

            selectedItem.getChildren().add(fileItem);

            selectedItem.setExpanded(false);
            selectedItem.setExpanded(true);

            showFolderContents(selectedItem);
        }
    }

    private String buildFolderPath(Folder folder) {
        String rootPath = "D:/COding/My Projects/9ledge/Files";
        Stack<String> pathStack = new Stack<>();

        while (folder != null) {
            pathStack.push(folder.getFolderName());
            folder = FolderDAO.getFolderById(folder.getParentFolderId());
        }

        StringBuilder fullPath = new StringBuilder(rootPath);
        while (!pathStack.isEmpty()) {
            fullPath.append(File.separator).append(pathStack.pop());
        }

        return fullPath.toString();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void searchFoldersAndFiles() {
        String searchTerm = searchField.getText().trim();

        if (!searchTerm.isEmpty()) {
            List<Folder> matchingFolders = FolderDAO.searchFolders(searchTerm);
            List<com.hc.proj9ledge.File> matchingFiles = FileDAO.searchFiles(searchTerm);

            updateSearchResults(matchingFolders, matchingFiles);
        }
    }

    private void updateSearchResults(List<Folder> folders, List<com.hc.proj9ledge.File> files) {
        contentArea.getChildren().clear();


        VBox searchResultsContainer = new VBox(10);
        searchResultsContainer.setPadding(new Insets(10,10,10,10));

        for (Folder folder : folders) {
            HBox folderContainer = new HBox(10);
            folderContainer.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-border-radius: 5; -fx-effect: innershadow(gaussian, #000, 0.5, 0.5, 0, 0);");

            Label folderIcon = new Label("📁");
            folderIcon.setFont(new Font(18));
            folderIcon.setTextFill(Color.DARKGRAY);

            Label folderLabel = new Label(" " + folder.getFolderName());
            folderLabel.setFont(new Font(16));
            folderLabel.setTextFill(Color.DARKBLUE);
            folderLabel.setStyle("-fx-font-weight: bold;");

            folderContainer.getChildren().addAll(folderIcon, folderLabel);
            searchResultsContainer.getChildren().add(folderContainer);
        }

        for (com.hc.proj9ledge.File file : files) {
            HBox fileContainer = new HBox(10);
            fileContainer.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-border-radius: 5; -fx-effect: innershadow(gaussian, #000, 0.5, 0.5, 0, 0);");

            Label fileIcon = new Label("📄");
            fileIcon.setFont(new Font(18));
            fileIcon.setTextFill(Color.DARKGRAY);

            Label fileLabel = new Label(" " + file.getFileName());
            fileLabel.setFont(new Font(16));
            fileLabel.setTextFill(Color.BLACK);

            fileLabel.setOnMouseEntered(event -> {
                fileLabel.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: #007bff;");
            });

            fileLabel.setOnMouseExited(event -> {
                fileLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            });

            fileLabel.setOnMouseClicked(event -> openWordFile(file.getFilePath()));

            fileContainer.getChildren().addAll(fileIcon, fileLabel);
            searchResultsContainer.getChildren().add(fileContainer);
        }

        ScrollPane scrollPane = new ScrollPane(searchResultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        contentArea.getChildren().add(scrollPane);
    }


    private String getFolderNameFromUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Folder");
        dialog.setHeaderText("Enter the folder name");
        dialog.setContentText("Folder Name:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private String getFileNameFromUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New File");
        dialog.setHeaderText("Enter the file name");
        dialog.setContentText("File Name:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
