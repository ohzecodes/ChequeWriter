package main.chequewriter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {

    static {
        // macOS specific configuration to suppress secure state restoration warning
        System.setProperty("javafx.macos.nsapplication.delegates", "true");
        System.setProperty("apple.awt.application.name", "Cheque Writer");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.appearance", "system");
        System.setProperty("apple.awt.applicationSupportsSecureRestorableState", "true");
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 989);
        stage.setTitle("Cheque Writer!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (!new File(DBhelper.DBPATH).exists()) {
            System.out.println("Couldn't find the DB file, I am proceeding to create a new one.");
            createDB.CreateTheDB();
        }
        launch(args);
    }
}
