import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class TemperatureController {

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private TextField temperatureField;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private Button convertButton;

    @FXML
    private Label resultLabel;

    @FXML
    private Label speedTitleLabel;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField timeField;

    @FXML
    private Button calculateSpeedButton;

    @FXML
    private Label speedResultLabel;

    private ResourceBundle bundle;

    private int sessionId;

    private final TemperatureConverter converter =
            new TemperatureConverter();

    @FXML
    public void initialize() {

        languageComboBox.getItems().addAll(
                "English",
                "Finnish",
                "Swedish",
                "Japanese",
                "Arabic"
        );

        languageComboBox.setValue("English");

        languageComboBox.setOnAction(event -> changeLanguage());

        loadLanguage(new Locale("en", "US"));

        sessionId = Database.createSession();
    }

    private void changeLanguage() {

        String language = languageComboBox.getValue();

        switch (language) {

            case "Finnish":
                loadLanguage(new Locale("fi", "FI"));
                break;

            case "Swedish":
                loadLanguage(new Locale("sv", "SE"));
                break;

            case "Japanese":
                loadLanguage(new Locale("ja", "JP"));
                break;

            case "Arabic":
                loadLanguage(new Locale("ar"));
                break;

            default:
                loadLanguage(new Locale("en", "US"));
                break;
        }
    }

    private void loadLanguage(Locale locale) {

        bundle = ResourceBundle.getBundle(
                "MessagesBundle",
                locale
        );

        titleLabel.setText(
                bundle.getString("title")
        );

        temperatureField.setPromptText(
                bundle.getString("temperaturePrompt")
        );

        convertButton.setText(
                bundle.getString("convert")
        );

        resultLabel.setText(
                bundle.getString("result")
        );

        unitComboBox.getItems().clear();

        unitComboBox.getItems().addAll(
                bundle.getString("fahrenheitToCelsius"),
                bundle.getString("celsiusToFahrenheit"),
                bundle.getString("kelvinToCelsius")
        );

        unitComboBox.getSelectionModel().selectFirst();

        speedTitleLabel.setText(
                bundle.getString("speedTitle")
        );

        distanceField.setPromptText(
                bundle.getString("distance")
        );

        timeField.setPromptText(
                bundle.getString("time")
        );

        calculateSpeedButton.setText(
                bundle.getString("calculateSpeed")
        );

        speedResultLabel.setText(
                bundle.getString("speed")
        );
    }

    @FXML
    private void convertTemperature() {

        try {

            double value = Double.parseDouble(
                    temperatureField.getText()
            );

            int selectedIndex =
                    unitComboBox
                            .getSelectionModel()
                            .getSelectedIndex();

            double result;

            if (selectedIndex == 0) {

                result =
                        converter.fahrenheitToCelsius(value);

            } else if (selectedIndex == 1) {

                result =
                        converter.celsiusToFahrenheit(value);

            } else {

                result =
                        converter.kelvinToCelsius(value);
            }

            String conversionType =
                    unitComboBox.getValue();

            Database.saveConversion(
                    sessionId,
                    value,
                    conversionType,
                    result
            );

            resultLabel.setText(
                    bundle.getString("result")
                            + ": "
                            + String.format("%.2f", result)
            );

        } catch (NumberFormatException e) {

            resultLabel.setText(
                    bundle.getString("invalidNumbers")
            );
        }
    }

    @FXML
    private void calculateSpeed() {

        try {

            double distance =
                    Double.parseDouble(
                            distanceField.getText()
                    );

            double time =
                    Double.parseDouble(
                            timeField.getText()
                    );

            if (time <= 0) {

                speedResultLabel.setText(
                        bundle.getString("invalidTime")
                );

                return;
            }

            double speed = distance / time;

            speedResultLabel.setText(
                    bundle.getString("speed")
                            + " "
                            + String.format("%.2f", speed)
            );

            Database.saveSpeedCalculation(
                    distance,
                    time,
                    speed
            );

        } catch (NumberFormatException e) {

            speedResultLabel.setText(
                    bundle.getString("invalidNumbers")
            );
        }
    }
    @FXML
    private void showSpeedHistory() {

        Stage historyStage = new Stage();

        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        textArea.setText(
                Database.getSpeedHistory()
        );

        VBox root = new VBox(textArea);

        Scene scene = new Scene(root, 600, 400);

        historyStage.setTitle("Speed History");
        historyStage.setScene(scene);
        historyStage.show();
    }
    @FXML
    private void showHistory() {

        Stage historyStage =
                new Stage();

        TextArea textArea =
                new TextArea();

        textArea.setEditable(false);

        textArea.setText(
                Database.getHistory()
        );

        VBox root =
                new VBox(textArea);

        Scene scene =
                new Scene(root, 500, 400);

        historyStage.setTitle(
                "Conversion History"
        );

        historyStage.setScene(scene);

        historyStage.show();
    }
}