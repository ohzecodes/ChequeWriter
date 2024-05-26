module main.chequewriter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;


    opens main.chequewriter to javafx.fxml;
    exports main.chequewriter;

}