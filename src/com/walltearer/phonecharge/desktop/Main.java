package com.walltearer.phonecharge.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Phone Charge");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        /**
         * TODO:
         * - show phone charge inside the app
         * - show app in a menu tray
         * - integrate gradle?
         */

    }


    public static void main(String[] args) {
        launch(args);
    }
}
