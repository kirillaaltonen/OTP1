import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose language:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");

        int choice = scanner.nextInt();

        Locale locale;

        switch (choice) {
            case 2:
                locale = new Locale("fi", "FI");
                break;
            case 3:
                locale = new Locale("sv", "SE");
                break;
            case 4:
                locale = new Locale("ja", "JP");
                break;
            default:
                locale = new Locale("en", "US");
                break;
        }

        ResourceBundle messages =
                ResourceBundle.getBundle("MessagesBundle", locale);

        TemperatureConverter converter = new TemperatureConverter();

        System.out.println(messages.getString("fahrenheitPrompt"));
        double fahrenheit = scanner.nextDouble();

        double celsius =
                converter.fahrenheitToCelsius(fahrenheit);

        System.out.println(
                messages.getString("fahrenheitResult")
                        + " "
                        + celsius
        );

        System.out.println(
                messages.getString("extreme")
                        + " "
                        + converter.isExtremeTemperature(celsius)
        );

        System.out.println(messages.getString("celsiusPrompt"));
        double celsiusInput = scanner.nextDouble();

        double fahrenheitResult =
                converter.celsiusToFahrenheit(celsiusInput);

        System.out.println(
                messages.getString("celsiusResult")
                        + " "
                        + fahrenheitResult
        );

        scanner.close();
    }
}