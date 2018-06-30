package com.walltearer.phonecharge.desktop;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {

    private Label batteryLevelIndicator;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));
        primaryStage.setTitle("Phone Charge");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        /**
         * TODO:
         * - show app in a menu tray
         * - load phone charge of a specific device
         * - move this logic to a separate component/service
         */

        batteryLevelIndicator = (Label) root.lookup("#batteryLevel");

        listenToBatteryLevelChanges();
    }

    private void listenToBatteryLevelChanges() {
        if (!connectToFirebase()) {
            return;
        };

        String deviceId = "1234-5678-ABCD"; // TODO: provide auth mechanism for the user to enter device id
        String devicePath = "device/" + deviceId + "/batteryLevel";
        DatabaseReference deviceRef = FirebaseDatabase.getInstance().getReference(devicePath);

        deviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object batteryLevel = dataSnapshot.getValue();

                Platform.runLater( () -> {
                    batteryLevelIndicator.setText("" + batteryLevel);
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    private boolean connectToFirebase() {
        Properties config = new Properties();
        try {
            // TODO: move move this to some common place
            config.load(getClass().getResourceAsStream("/config/app.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        String firebaseUrl = config.getProperty("firebaseUrl");

        InputStream serviceAccount = getClass().getResourceAsStream("/config/firebaseAccountKey.json");
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(firebaseUrl)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        FirebaseApp.initializeApp(options);
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
