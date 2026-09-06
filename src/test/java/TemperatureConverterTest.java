import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    TemperatureConverter converter = new TemperatureConverter();

    @Test
    void fahrenheitToCelsius() {
        assertEquals(0, converter.fahrenheitToCelsius(32));
        assertEquals(100, converter.fahrenheitToCelsius(212));
    }

    @Test
    void celsiusToFahrenheit() {
        assertEquals(32, converter.celsiusToFahrenheit(0));
        assertEquals(212, converter.celsiusToFahrenheit(100));
    }

    @Test
    void isExtremeTemperature() {
        assertTrue(converter.isExtremeTemperature(-41));
        assertTrue(converter.isExtremeTemperature(51));
        assertFalse(converter.isExtremeTemperature(25));
    }
}