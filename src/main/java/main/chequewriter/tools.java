package main.chequewriter;

import javafx.scene.control.Alert;

public class tools {
    public static void showInvalidAlert(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid");
        alert.setHeaderText(null);
        alert.setContentText(text );
        alert.show();
    }
}
