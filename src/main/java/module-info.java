module main.chequewriter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires pdfbox.app;

    opens main.chequewriter to javafx.fxml;
    exports main.chequewriter;

}