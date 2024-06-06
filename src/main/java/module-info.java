module main.chequewriter {
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires java.sql;
    opens main.chequewriter to javafx.fxml;
    exports main.chequewriter;

}