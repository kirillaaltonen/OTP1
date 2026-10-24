import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    TemperatureConverter converter = new TemperatureConverter();

    @Test
    void fahrenheitToCelsius() {
        assertEquals(0, converter.fahrenheitToCelsius(32), 0.001);
        assertEquals(100, converter.fahrenheitToCelsius(212), 0.001);
        assertEquals(-40, converter.fahrenheitToCelsius(-40), 0.001);
    }

    @Test
    void celsiusToFahrenheit() {
        assertEquals(32, converter.celsiusToFahrenheit(0), 0.001);
        assertEquals(212, converter.celsiusToFahrenheit(100), 0.001);
        assertEquals(-40, converter.celsiusToFahrenheit(-40), 0.001);
    }

    @Test
    void isExtremeTemperature() {
        assertFalse(converter.isExtremeTemperature(-40));
        assertFalse(converter.isExtremeTemperature(50));
        assertTrue(converter.isExtremeTemperature(-41));
        assertTrue(converter.isExtremeTemperature(51));
        assertFalse(converter.isExtremeTemperature(25));
    }

    @Test
    void kelvinToCelsius() {
        assertEquals(26.85, converter.kelvinToCelsius(300), 0.001);
        assertEquals(0, converter.kelvinToCelsius(273.15), 0.001);
    }
}