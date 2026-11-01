import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:temperature.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String createCalculationsTable = """
        CREATE TABLE IF NOT EXISTS calculations (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            created_at TEXT DEFAULT CURRENT_TIMESTAMP
        );
        """;

        String createCalculationValuesTable = """
        CREATE TABLE IF NOT EXISTS calculation_values (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            calculation_id INTEGER NOT NULL,
            distance REAL NOT NULL,
            time REAL NOT NULL,
            speed REAL NOT NULL,
            FOREIGN KEY(calculation_id) REFERENCES calculations(id)
        );
        """;

        String createSessions = """
                CREATE TABLE IF NOT EXISTS conversion_sessions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
                """;

        String createHistory = """
                CREATE TABLE IF NOT EXISTS conversion_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    session_id INTEGER,
                    input_value REAL,
                    conversion_type TEXT,
                    result_value REAL,
                    FOREIGN KEY(session_id)
                        REFERENCES conversion_sessions(id)
                );
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.execute(createCalculationsTable);
            statement.execute(createCalculationValuesTable);

            statement.execute(createSessions);
            statement.execute(createHistory);

            System.out.println("Database initialized");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getSpeedHistory() {

        String sql = """
        SELECT cv.distance, cv.time, cv.speed, c.created_at
        FROM calculation_values cv
        JOIN calculations c
            ON cv.calculation_id = c.id
        ORDER BY cv.id DESC
        LIMIT 20
        """;

        StringBuilder history = new StringBuilder();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                double distance = resultSet.getDouble("distance");
                double time = resultSet.getDouble("time");
                double speed = resultSet.getDouble("speed");
                String createdAt = resultSet.getString("created_at");

                history.append("Distance: ")
                        .append(distance)
                        .append(" | Time: ")
                        .append(time)
                        .append(" | Speed: ")
                        .append(speed)
                        .append(" | ")
                        .append(createdAt)
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history.toString();
    }
    public static void saveConversion(
            int sessionId,
            double inputValue,
            String conversionType,
            double resultValue
    ) {

        String sql = """
            INSERT INTO conversion_history
            (session_id, input_value, conversion_type, result_value)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = connect();
             var statement = connection.prepareStatement(sql)) {

            statement.setInt(1, sessionId);
            statement.setDouble(2, inputValue);
            statement.setString(3, conversionType);
            statement.setDouble(4, resultValue);

            statement.executeUpdate();

            System.out.println("Conversion saved");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static int createSession() {

        String sql = """
            INSERT INTO conversion_sessions DEFAULT VALUES
            """;

        try (Connection connection = connect();
             var statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();

            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    public static void saveSpeedCalculation(double distance, double time, double speed) {
        String insertCalculation = "INSERT INTO calculations DEFAULT VALUES";
        String insertValues = """
            INSERT INTO calculation_values
            (calculation_id, distance, time, speed)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);

            try (
                    PreparedStatement calculationStatement =
                            connection.prepareStatement(
                                    insertCalculation,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {
                calculationStatement.executeUpdate();

                try (ResultSet keys = calculationStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        int calculationId = keys.getInt(1);

                        try (
                                PreparedStatement valuesStatement =
                                        connection.prepareStatement(insertValues)
                        ) {
                            valuesStatement.setInt(1, calculationId);
                            valuesStatement.setDouble(2, distance);
                            valuesStatement.setDouble(3, time);
                            valuesStatement.setDouble(4, speed);

                            valuesStatement.executeUpdate();
                        }
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getHistory() {

        String sql = """
            SELECT input_value, conversion_type, result_value
            FROM conversion_history
            ORDER BY id DESC
            LIMIT 20
            """;

        StringBuilder history = new StringBuilder();

        try (Connection connection = connect();
             var statement = connection.prepareStatement(sql);
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                double input = resultSet.getDouble("input_value");
                String type = resultSet.getString("conversion_type");
                double result = resultSet.getDouble("result_value");

                history.append(input)
                        .append(" | ")
                        .append(type)
                        .append(" | ")
                        .append(result)
                        .append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history.toString();
    }

}
