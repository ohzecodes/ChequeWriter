package main.chequewriter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
//import static
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static main.chequewriter.tools.showInvalidAlert;


public class MainController {

    @FXML
//    public TextField
    public ComboBox<Object> bankCombo;

    public Button ChooseDirectory;

    public Button SaveCoordinates, BtnSaveSettings,PrintButton;
    public DatePicker dDate;
    public AnchorPane rootPane;
    @FXML
    private TextField Dpayee, Ddigits;

    @FXML
    private TextArea Dwords;

    @FXML
    private TextField dateX, dateY, PayeeX, PayeeY, digitsX, digitsY, AwordsX, AwordsY,Profile_Name;

    public ComboBox<String> FontSize ,fontName;
    public TextField Cents,Currency;
    @FXML
    public void initialize() {
        ChooseDirectory.setOnAction(e -> handleChooseDirectory());
        SaveCoordinates.setOnAction(e -> DBhelper.addCoordinatesToSQLite(getCoordinates()));
        // Add a listener to the dDigits TextField to listen for changes
        Ddigits.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Convert the digits to words and set the text in Dwords TextArea
                Dwords.setText(numberstoWords.convertDigitsToWords(newValue));
            }
        });

        PrintButton.setOnAction(e -> printDocument());
        ArrayList<String> a= (ArrayList<String>) DBhelper.getProfileNamesFromSQLite();
        a.add("new");
        ObservableList<Object> options = FXCollections.observableArrayList(a);
        bankCombo.setItems(options);
        bankCombo.setValue(options.get(options.size() - 1));

        bankCombo.setOnAction(event -> {
            String selectedOption = (String) bankCombo.getValue().toString().replace("[", "").replace("]", "");;

            if (selectedOption.equals("new")) {
                clearCoordinates();
            } else {
                setCoordinates(DBhelper.getCoordinatesByProfileName(selectedOption));
                SaveCoordinates.setVisible(false);
            }
        });
        SaveCoordinates.setVisible(true);
//        set
        ObservableList<String> numbers = FXCollections.observableArrayList();
        for (int i = 8; i <= 16; i++) {
            numbers.add(String.valueOf(i));
        }
        FontSize.setItems(numbers);
        fontName.setItems(FXCollections.observableArrayList("Times-Roman", "Helvetica", "Courier", "Symbol", "ZapfDingbats"));
        fontName.setOnAction(e->{
            String selectedOption =  fontName.getValue().replace("[", "").replace("]", "");;
            PDFHelper.setFontName(selectedOption);
        });
        PDFHelper.setFontName(fontName.getValue());
        FontSize.setOnAction(e->{
            String selectedOption =  FontSize.getValue().replace("[", "").replace("]", "");;
            PDFHelper.setFontSize(selectedOption);
        });
        PDFHelper.setFontSize(FontSize.getValue());
        BtnSaveSettings.setOnAction(e->saveOtherSetting());
        setOtherSettings(DBhelper.getOtherSettings());

    }
    private void handleChooseDirectory() {

        Stage stage = (Stage) ChooseDirectory.getScene().getWindow();
        File selectedDirectory = openDirectoryChooser(stage);
        if (selectedDirectory != null) {
            ChooseDirectory.setText(selectedDirectory.getAbsolutePath());
        }
        assert selectedDirectory != null;
        PDFHelper.setSaveLocation(selectedDirectory.getAbsolutePath());
    }

    private File openDirectoryChooser(Stage ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        // Show the directory chooser
        return directoryChooser.showDialog(ownerWindow);
    }
    private void setOtherSettings(Map<String, String> otherSettings){

        Currency.setText(otherSettings.getOrDefault("CURRENCY","dollar"));
        Cents.setText(otherSettings.getOrDefault("CENTS","cent"));
        fontName.setValue(otherSettings.getOrDefault("FONT", "Times-Roman"));
        FontSize.setValue(otherSettings.getOrDefault("FONTSIZE",10+""));
        ChooseDirectory.setText(otherSettings.getOrDefault("DIRECTORY","Select..."));
        numberstoWords.setDOLLAR(Currency.getText());
        numberstoWords.setCENT(Cents.getText());
        assert !isDirectoryChooserEmpty(ChooseDirectory);
        PDFHelper.setSaveLocation(ChooseDirectory.getText());

    }
    private void saveOtherSetting() {


        if (!isOtherSettingsValid()){
            showInvalidAlert("something is invalid, please check");
            checkAndHighlightEmptyFields(Currency,Cents);
            checkAndHighlightEmptyFields(fontName,FontSize);
           checkAndHighlightDirectoryChooser(ChooseDirectory);

            return;
        }
        Map<String, String> map=new HashMap<>();
        map.put("CURRENCY", Currency.getText());
        map.put("CENTS", Cents.getText());
        map.put("FONTNAME",fontName.getValue());
        map.put("FONTSIZE",FontSize.getValue());
        map.put("DIRECTORY", ChooseDirectory.getText());
        DBhelper.addOtherSettings(map);
        numberstoWords.setDOLLAR(Currency.getText());
        numberstoWords.setCENT(Cents.getText());

    }
    private void clearCoordinates() {
        Profile_Name.setText("");
        dateX.setText("");
        dateY.setText("");
        PayeeX.setText("");
        PayeeY.setText("");
        digitsX.setText("");
        digitsY.setText("");
        AwordsX.setText("");
        AwordsY.setText("");
        SaveCoordinates.setVisible(true);
    }
    private void setCoordinates(Map<String, Object> coordinatesByProfileName) {

        // Retrieve values from the map
        String profileName = (String) coordinatesByProfileName.get("name");
        Profile_Name.setText(profileName);
        Map<String, Map<String, Double>> coordinates = new HashMap<>();
        coordinates.put("date", (Map<String, Double>) coordinatesByProfileName.get("date"));
        coordinates.put("payee", (Map<String, Double>) coordinatesByProfileName.get("payee"));
        coordinates.put("digits", (Map<String, Double>) coordinatesByProfileName.get("digits"));
        coordinates.put("words", (Map<String, Double>) coordinatesByProfileName.get("words"));

        // Update the TextFields with the retrieved coordinates
        dateX.setText(String.valueOf(coordinates.get("date").get("x")));
        dateY.setText(String.valueOf(coordinates.get("date").get("y")));
        PayeeX.setText(String.valueOf(coordinates.get("payee").get("x")));
        PayeeY.setText(String.valueOf(coordinates.get("payee").get("y")));
        digitsX.setText(String.valueOf(coordinates.get("digits").get("x")));
        digitsY.setText(String.valueOf(coordinates.get("digits").get("y")));
        AwordsX.setText(String.valueOf(coordinates.get("words").get("x")));
        AwordsY.setText(String.valueOf(coordinates.get("words").get("y")));
    }
    private int cmToPoints(double cm) {
        // 1 cm = 0.3937 inch, 1 inch = 72 points
        return (int) (cm * 0.3937 * 72);
    }
    private Map<String, Object> getCoordinates() {
        Map<String, Object> cheque = new HashMap<>();
        cheque.put("name", Profile_Name.getText());
        double dateXcm = Double.parseDouble(dateX.getText());
        double dateYcm = Double.parseDouble(dateY.getText());
        double payeeXcm = Double.parseDouble(PayeeX.getText());
        double payeeYcm = Double.parseDouble(PayeeY.getText());
        double digitsXcm = Double.parseDouble(digitsX.getText());
        double digitsYcm = Double.parseDouble(digitsY.getText());
        double wordsXcm = Double.parseDouble(AwordsX.getText());
        double wordsYcm = Double.parseDouble(AwordsY.getText());


        Map<String, Object> date = new HashMap<>();
        date.put("x", dateXcm);
        date.put("y", dateYcm);
        cheque.put("date", date);

        Map<String, Object> payee = new HashMap<>();
        payee.put("x", payeeXcm);
        payee.put("y", payeeYcm);
        cheque.put("payee", payee);

        Map<String, Object> digits = new HashMap<>();
        digits.put("x", digitsXcm);
        digits.put("y", digitsYcm);
        cheque.put("digits", digits)
        ;
        Map<String, Object> words = new HashMap<>();

        words.put("x", wordsXcm);
        words.put("y", wordsYcm);
        cheque.put("words", words);

        return cheque;
    }
    private void checkAndHighlightEmptyFields(DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            if (datePicker.getValue() == null) {
                // If the date picker is empty, apply a red border
                datePicker.setStyle("-fx-border-color: red;");
            } else {
                // If not empty, remove any custom styling
                datePicker.setStyle("");
            }
        }
    }

    private void checkAndHighlightEmptyFields(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.getText().trim().isEmpty()) {
                // If the text field is empty, apply a red border
                textField.setStyle("-fx-border-color: red;");
            } else {
                // If not empty, remove any custom styling
                textField.setStyle("");
            }
        }
    }
    private void checkAndHighlightEmptyFields(TextArea... textAreas) {
        for (TextArea textArea : textAreas) {
            if (textArea.getText().trim().isEmpty()) {
                // If the text area is empty, apply a red border
                textArea.setStyle("-fx-border-color: red;");
            } else {
                // If not empty, remove any custom styling
                textArea.setStyle("");
            }
        }
    }

    private Map<String, Object> toPT(Map<String, Object> coordinates) {
        Map<String, Object> coordinatesInPoints = new HashMap<>();

        for (Map.Entry<String, Object> entry : coordinates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Check if the value is a map
            if (value instanceof Map) {
                Map<String, Double> coordinatesInCm = (Map<String, Double>) value;
                double xCm = coordinatesInCm.get("x");
                double yCm = coordinatesInCm.get("y");


                // Convert x and y coordinates from centimeters to points
                double xPoints = cmToPoints(xCm);
                double yPoints = cmToPoints(yCm);

                // Put the converted coordinates into the new map
                Map<String, Double> convertedCoordinates = new HashMap<>();
                convertedCoordinates.put("x", xPoints);
                convertedCoordinates.put("y", yPoints);
                coordinatesInPoints.put(key, convertedCoordinates);
            } else {
                // Handle unexpected value types
                System.err.println("Unexpected value type for key " + key + ": " + value.getClass());
            }
        }

        return coordinatesInPoints;
    }

    private boolean isDirectoryChooserEmpty(Button ChooseDirectory) {
        return ChooseDirectory.getText().contains("Select...");
    }
    private void checkAndHighlightDirectoryChooser(Button b){

        if (isDirectoryChooserEmpty(b)){
            b.setStyle("-fx-border-color: red;");
        }else {
            b.setStyle("");
        }
    }
    private void checkAndHighlightEmptyFields(ComboBox<?>... comboBoxes) {
        for (ComboBox<?> comboBox : comboBoxes) {
            if (comboBox.getSelectionModel().isEmpty()) {
                // If the combo box is empty, apply a red border
                comboBox.setStyle("-fx-border-color: red;");
            } else {
                // If not empty, remove any custom styling
                comboBox.setStyle("");
            }
        }
    }
    private boolean isCoordinatesValid(){
      return  ! (dateX.getText().isEmpty()|| dateY.getText().isEmpty()|| PayeeX.getText().isEmpty()||PayeeY.getText().isEmpty()||
                digitsX.getText().isEmpty() ||digitsY.getText().isEmpty()|| AwordsX.getText().isEmpty()|| AwordsY.getText().isEmpty()||
                Profile_Name.getText().isEmpty());
    }
    private boolean isOtherSettingsValid(){
        return !(isDirectoryChooserEmpty(ChooseDirectory)||
                FontSize.getValue().isEmpty() ||
                fontName.getValue().isEmpty() ||
                Cents.getText().isEmpty() ||
                Currency.getText().isEmpty());
    }
    private boolean isDataValid(){
        return !(dDate.getValue() == null||Dpayee.getText().isEmpty()||
                Ddigits.getText().isEmpty()||Dwords.getText().isEmpty());
    }
    private void printDocument() {
        double width = 17.3, height = 7.3;
        boolean b=(isDataValid()&&isCoordinatesValid());
        if(!b){
            checkAndHighlightEmptyFields(dDate);
            checkAndHighlightEmptyFields(Dwords);
            checkAndHighlightEmptyFields( dateX, dateY, PayeeX, PayeeY, digitsX, digitsY, AwordsX, AwordsY,Profile_Name,Dpayee, Ddigits);
            showInvalidAlert("Something is not filled that was expect to be filled ");
            return;
        }

        Map<String, Object> chequeData = new HashMap<>();
        chequeData.put("date", dDate.getValue());
        chequeData.put("payee", Dpayee.getText());
        chequeData.put("amountDigits", Ddigits.getText());
        chequeData.put("amountWords", Dwords.getText());

        PDFHelper.saveAsPDF(cmToPoints(width), cmToPoints(height), toPT(getCoordinates()), chequeData, Profile_Name.getText());
        ;
    }

}

