package dev.luke10x.easylogin.adapter;

import dev.luke10x.easylogin.community.User;
import dev.luke10x.easylogin.community.UserService;
import jakarta.annotation.Resource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

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
