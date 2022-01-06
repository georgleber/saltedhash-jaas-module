package de.meetwithfriends.security.jdbc;

import de.meetwithfriends.security.jdbc.data.ConfigurationData;
import de.meetwithfriends.security.jdbc.data.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationDao
{
    private final Logger log = LoggerFactory.getLogger(AuthenticationDao.class);

    private ConfigurationData configurationData;

    public AuthenticationDao(ConfigurationData configurationData)
    {
        this.configurationData = configurationData;
    }

    public UserData loadUserData(String username)
    {
        UserData result = new UserData();

        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try (Connection connection = getConnection())
        {
            statement = connection.prepareStatement(configurationData.getUserQuery());
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            String salt = null;
            String password = null;
            while (resultSet.next())
            {
                salt = resultSet.getString(1);
                password = resultSet.getString(2);
            }

            result.setSalt(salt);
            result.setPassword(password);
        }
        catch (SQLException e)
        {
            log.error("Error when loading user from the database", e);
        }
        finally
        {
            closeResource(resultSet);
            closeResource(statement);
        }

        return result;
    }

    public List<String> loadRoles(String username)
    {
        List<String> roles = new ArrayList<>();

        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try (Connection connection = getConnection())
        {
            statement = connection.prepareStatement(configurationData.getRoleQuery());
            statement.setString(1, username);

            resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                roles.add(resultSet.getString(1));
            }
        }
        catch (SQLException e)
        {
            log.error("Error when loading user from the database ", e);
        }
        finally
        {
            closeResource(resultSet);
            closeResource(statement);
        }

        return roles;
    }

    private Connection getConnection() throws SQLException
    {
        Connection connection = null;
        try
        {
            Class.forName(configurationData.getDbDriver()).newInstance();
            connection = DriverManager.getConnection(configurationData.getDbUrl(), configurationData.getDbUser(),
                    configurationData
                    .getDbPassword());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            log.error("Error when creating database connection", ex);
            throw new RuntimeException("Error when creating database connection: " + ex.getMessage());
        }

        return connection;
    }

    private void closeResource(AutoCloseable closeable)
    {
        try
        {
            if (closeable != null)
            {
                closeable.close();
            }
        }
        catch (Exception ex)
        {
            log.warn("Error closing resource", ex);
        }
    }

}
