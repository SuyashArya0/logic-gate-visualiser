package com.visualiser;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 768);

        primaryStage.setTitle("Logic Gate Visualiser");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}