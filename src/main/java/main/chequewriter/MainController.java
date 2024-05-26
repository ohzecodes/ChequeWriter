package main.chequewriter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainController {
    @FXML
    public TextField Profile_Name;
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
                Dwords.setText(numberstoWords.convertDigitsToWords(newValue));
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

        ArrayList<Map<String, Object>> fieldMaps = new ArrayList<>();

        Map<String, Object> date = new HashMap<>();
        date.put("name", Ddate.getText());
        date.put("x", dateXPoints);
        date.put("y", dateYPoints);
        fieldMaps.add(date);

        Map<String, Object> payee = new HashMap<>();
        payee.put("name", Dpayee.getText());
        payee.put("x", payeeXPoints);
        payee.put("y", payeeYPoints);
        fieldMaps.add(payee);

        Map<String, Object> digits = new HashMap<>();
        digits.put("name", Ddigits.getText());
        digits.put("x", digitsXPoints);
        digits.put("y", digitsYPoints);
        fieldMaps.add(digits);

        Map<String, Object> words = new HashMap<>();
        words.put("name", Dwords.getText());
        words.put("x", wordsXPoints);
        words.put("y", wordsYPoints);
        fieldMaps.add(words);

        double width=17.3 ,height=7.3;
        SaveHelper.saveAsPDF(cmToPoints(width),cmToPoints(height),fieldMaps);



    }

}