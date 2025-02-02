package com.hc.proj9ledge;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.File;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            File rootFolder = new File("D:/COding/My Projects/9ledge/Files");
            if (!rootFolder.exists()) {
                rootFolder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.primaryStage = primaryStage;
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/hc/proj9ledge/thumbnail.png")));
        this.primaryStage.setTitle("9Ledge-Explorer");

        initMainView();
    }

    private void initMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/hc/proj9ledge/MainView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}