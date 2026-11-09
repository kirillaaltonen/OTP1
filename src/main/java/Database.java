import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
                input_value REAL NOT NULL,
                from_unit TEXT NOT NULL,
                to_unit TEXT NOT NULL,
                result_value REAL NOT NULL,
                language TEXT NOT NULL,
                created_at TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(session_id) REFERENCES conversion_sessions(id)
            );
            """;

        try (
                Connection connection = connect();
                Statement statement = connection.createStatement()
        ) {

            statement.execute(createCalculationsTable);
            statement.execute(createCalculationValuesTable);
            statement.execute(createSessions);
            statement.execute(createHistory);

            runInitSql(connection);

            System.out.println("Database initialized");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void runInitSql(Connection connection) {

        try {

            InputStream inputStream =
                    Database.class.getResourceAsStream("/init.sql");

            if (inputStream == null) {
                System.out.println("init.sql not found");
                return;
            }

            String sql = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String[] statements = sql.split(";");

            for (String statement : statements) {

                if (!statement.trim().isEmpty()) {

                    try (
                            Statement sqlStatement =
                                    connection.createStatement()
                    ) {
                        sqlStatement.execute(
                                statement.trim()
                        );
                    }
                }
            }

            System.out.println(
                    "Database localization initialized"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int createSession() {

        String sql = """
            INSERT INTO conversion_sessions DEFAULT VALUES
            """;

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void saveConversion(
            int sessionId,
            double inputValue,
            String fromUnit,
            String toUnit,
            double resultValue,
            String language
    ) {

        String sql = """
            INSERT INTO conversion_history
            (
                session_id,
                input_value,
                from_unit,
                to_unit,
                result_value,
                language
            )
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, sessionId);
            statement.setDouble(2, inputValue);
            statement.setString(3, fromUnit);
            statement.setString(4, toUnit);
            statement.setDouble(5, resultValue);
            statement.setString(6, language);

            statement.executeUpdate();

            System.out.println("Conversion saved");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveSpeedCalculation(
            double distance,
            double time,
            double speed
    ) {

        String insertCalculation =
                "INSERT INTO calculations DEFAULT VALUES";

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

                try (
                        ResultSet keys =
                                calculationStatement.getGeneratedKeys()
                ) {

                    if (keys.next()) {

                        int calculationId =
                                keys.getInt(1);

                        try (
                                PreparedStatement valuesStatement =
                                        connection.prepareStatement(
                                                insertValues
                                        )
                        ) {

                            valuesStatement.setInt(
                                    1,
                                    calculationId
                            );

                            valuesStatement.setDouble(
                                    2,
                                    distance
                            );

                            valuesStatement.setDouble(
                                    3,
                                    time
                            );

                            valuesStatement.setDouble(
                                    4,
                                    speed
                            );

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
            SELECT
                input_value,
                from_unit,
                to_unit,
                result_value,
                language,
                created_at
            FROM conversion_history
            ORDER BY id DESC
            LIMIT 5
            """;

        StringBuilder history =
                new StringBuilder();

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                history.append(
                        resultSet.getDouble("input_value")
                );

                history.append(" ");

                history.append(
                        resultSet.getString("from_unit")
                );

                history.append(" → ");

                history.append(
                        resultSet.getDouble("result_value")
                );

                history.append(" ");

                history.append(
                        resultSet.getString("to_unit")
                );

                history.append(" | ");

                history.append(
                        resultSet.getString("language")
                );

                history.append(" | ");

                history.append(
                        resultSet.getString("created_at")
                );

                history.append("\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history.toString();
    }

    public static String getSpeedHistory() {

        String sql = """
            SELECT
                cv.distance,
                cv.time,
                cv.speed,
                c.created_at
            FROM calculation_values cv
            JOIN calculations c
                ON cv.calculation_id = c.id
            ORDER BY cv.id DESC
            LIMIT 20
            """;

        StringBuilder history =
                new StringBuilder();

        try (
                Connection connection = connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                double distance =
                        resultSet.getDouble("distance");

                double time =
                        resultSet.getDouble("time");

                double speed =
                        resultSet.getDouble("speed");

                String createdAt =
                        resultSet.getString("created_at");

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
}