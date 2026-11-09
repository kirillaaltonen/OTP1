import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitDAO {

    public static UnitInfo getUnit(
            String code,
            String language
    ) {

        String sql = """
                SELECT
                    u.code,
                    COALESCE(t.name, en.name) AS name,
                    COALESCE(t.description, en.description) AS description
                FROM unit u

                LEFT JOIN unit_translation t
                    ON t.unit_id = u.id
                    AND t.lang = ?

                LEFT JOIN unit_translation en
                    ON en.unit_id = u.id
                    AND en.lang = 'en'

                WHERE u.code = ?
                """;

        try (
                Connection connection = Database.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, language);
            statement.setString(2, code);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return new UnitInfo(
                        resultSet.getString("code"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}