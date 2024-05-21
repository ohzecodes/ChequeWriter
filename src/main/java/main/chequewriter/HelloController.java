package main.chequewriter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import static main.chequewriter.numberstoWords.convertDigitsToWords;

public class HelloController  {

    @FXML
    public ComboBox bankCombo;
    public Button PrintButton;
    // these are the DATA objects
    @FXML
    private TextField Ddate,Dpayee,Ddigits;
    @FXML
    private TextArea   Dwords;

    @FXML
    private TextField dateX ,dateY ,PayeeX ,PayeeY ,digitsX ,digitsY ,AwordsX ,AwordsY;

    @FXML
    public void initialize() {
        // Add a listener to the dDigits TextField to listen for changes
        Ddigits.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Convert the digits to words and set the text in Dwords TextArea
                Dwords.setText(convertDigitsToWords(newValue));
            }
        });
        PrintButton.setOnAction( e->printDocument());



    }

    private int cmToPoints(double cm) {
        // 1 cm = 0.3937 inch, 1 inch = 72 points
        return (int) (cm * 0.3937 * 72);
    }

    public void printDocument() {
        double dateXcm = Double.parseDouble(dateX.getText());
        double dateYcm = Double.parseDouble(dateY.getText());
        double payeeXcm = Double.parseDouble(PayeeX.getText());
        double payeeYcm = Double.parseDouble(PayeeY.getText());
        double digitsXcm = Double.parseDouble(digitsX.getText());
        double digitsYcm = Double.parseDouble(digitsY.getText());
        double wordsXcm = Double.parseDouble(AwordsX.getText());
        double wordsYcm = Double.parseDouble(AwordsY.getText());

        int dateXPoints = cmToPoints(dateXcm);
        int dateYPoints = cmToPoints(dateYcm);
        int payeeXPoints = cmToPoints(payeeXcm);
        int payeeYPoints = cmToPoints(payeeYcm);
        int digitsXPoints = cmToPoints(digitsXcm);
        int digitsYPoints = cmToPoints(digitsYcm);
        int wordsXPoints = cmToPoints(wordsXcm);
        int wordsYPoints = cmToPoints(wordsYcm);

//     TODO: Use these datapoints to print/save the cheque as pdf
    }

}