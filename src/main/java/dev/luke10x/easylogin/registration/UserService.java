package dev.luke10x.easylogin.registration;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Resource(lookup = "java:/jboss/PostgresqlDS")
    private DataSource dataSource;

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (final Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM registration";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()) {
                    int handleId = resultSet.getInt("handle_id");
                    String handle = resultSet.getString("handle");
                    String secret = resultSet.getString("secret");
                    int enabled = resultSet.getInt("enabled");

                    users.add(new User(handle));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error");
            System.err.println(e);
        }

        return users;
    }
}
