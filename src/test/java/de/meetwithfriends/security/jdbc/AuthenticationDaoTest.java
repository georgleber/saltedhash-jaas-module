package de.meetwithfriends.security.jdbc;

import de.meetwithfriends.security.jdbc.data.ConfigurationData;
import de.meetwithfriends.security.jdbc.data.UserData;
import java.sql.*;
import org.junit.*;

public class AuthenticationDaoTest
{
    private AuthenticationDao authDao;
    private Connection connection;

    @Before
    public void setUp() throws SQLException
    {
        ConfigurationData connectionData = createConfigurationData();
        authDao = new AuthenticationDao(connectionData);

        connection = DriverManager.getConnection(connectionData.getDbUrl(), connectionData.getDbUser(),
                connectionData.getDbPassword());
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE USER (ID INT PRIMARY KEY, USERNAME VARCHAR(255), SALT VARCHAR(255), "
                + "PASSWORD VARCHAR(255))");

        statement.execute("INSERT INTO USER (ID, USERNAME, SALT, PASSWORD) VALUES "
                + "(1, 'testuser', '1234', 'e9cee71ab932fde863338d08be4de9dfe39ea049bdafb342ce659ec5450b69ae')");
    }

    @After
    public void tearDown() throws SQLException
    {
        connection.close();
    }

    @Test
    public void testLoadUserData() throws SQLException
    {
        UserData result = authDao.loadUserData("testuser");

        String savedSalt = result.getSalt();
        String savedPassword = result.getPassword();

        Assert.assertNotNull(savedSalt);
        Assert.assertNotNull(savedPassword);
        Assert.assertEquals("1234", savedSalt);
        Assert.assertEquals("e9cee71ab932fde863338d08be4de9dfe39ea049bdafb342ce659ec5450b69ae", savedPassword);
    }

    @Test
    public void testUserNotFound() throws SQLException
    {
        UserData result = authDao.loadUserData("wronguser");

        String savedSalt = result.getSalt();
        String savedPassword = result.getPassword();

        Assert.assertNull(savedSalt);
        Assert.assertNull(savedPassword);
    }

    private ConfigurationData createConfigurationData()
    {
        ConfigurationData configurationData = new ConfigurationData();
        configurationData.setDbDriver("org.h2.Driver");
        configurationData.setDbUrl("jdbc:h2:mem:test");
        configurationData.setDbUser("sa");
        configurationData.setDbPassword("sa");
        configurationData.setUserQuery("select salt, password from user where username = ?");
        configurationData.setRoleQuery("select ro.name from role ro inner join roles_users rous on ro.id = "
                + "rous.roles_id inner join user us on rous.users_id = us.id where us.username = ?");

        return configurationData;
    }
}
