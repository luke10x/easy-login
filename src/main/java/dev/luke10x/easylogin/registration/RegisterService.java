package dev.luke10x.easylogin.registration;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ApplicationScoped
public class RegisterService {

    @Resource(lookup = "java:/jboss/PostgresqlDS")
    private DataSource dataSource;

    public void registerNewHandle(final Handle handle) throws
            HandleAlreadyTakenException,
            HandleDoesNotFitDatabaseFieldException, UnknownDatabaseErrorSavingHandle {
        try (final Connection connection = dataSource.getConnection()) {
            final String sql = "INSERT INTO registration (handle, secret, enabled) VALUES (?, ?, ?)";
            try (final PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, handle.getHandle());
                statement.setString(2, handle.getSecret());
                statement.setInt(3, 1);

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            final String sqlState = e.getSQLState();
            if (sqlState.startsWith("23505")) {
                throw new HandleAlreadyTakenException("Handle @" + handle.getHandle() + " is taken already");
            } else if (sqlState.startsWith("22001")) {
                throw new HandleDoesNotFitDatabaseFieldException("Cannot insert to DB, too much data");
            }
            throw new UnknownDatabaseErrorSavingHandle(e.getMessage());
        }
    }
}
