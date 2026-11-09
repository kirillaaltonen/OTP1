import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitDAOTest {

    @BeforeAll
    static void setup() {
        Database.initialize();
    }

    @Test
    void testEnglishUnit() {

        UnitInfo unit =
                UnitDAO.getUnit(
                        "CELSIUS",
                        "en"
                );

        assertNotNull(unit);

        assertEquals(
                "Celsius",
                unit.getName()
        );
    }

    @Test
    void testFinnishUnit() {

        UnitInfo unit =
                UnitDAO.getUnit(
                        "DISTANCE",
                        "fi"
                );

        assertNotNull(unit);

        assertEquals(
                "Kilometrit",
                unit.getName()
        );
    }

    @Test
    void testEnglishFallback() {

        UnitInfo unit =
                UnitDAO.getUnit(
                        "CELSIUS",
                        "xx"
                );

        assertNotNull(unit);

        assertEquals(
                "Celsius",
                unit.getName()
        );
    }
}