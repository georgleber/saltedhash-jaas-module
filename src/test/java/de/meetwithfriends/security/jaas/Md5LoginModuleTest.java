package de.meetwithfriends.security.jaas;

import de.meetwithfriends.security.jaas.principal.UserPrincipal;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Principal;
import java.sql.*;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.junit.*;

public class Md5LoginModuleTest
{
    private Connection connection;

    @Before
    public void setUp() throws Exception
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource("jaas.config");
        String p = URLDecoder.decode(url.toExternalForm(), "UTF - 8");

        System.setProperty("java.security.auth.login.config", p);

        connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "sa");
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE USER (ID INT PRIMARY KEY, USERNAME VARCHAR(255), SALT VARCHAR(255), "
                + "PASSWORD VARCHAR(255))");

        statement.execute("INSERT INTO USER (ID, USERNAME, SALT, PASSWORD) VALUES "
                + "(1, 'testuser', '1234', 'e19d5cd5af0378da05f63f891c7467af')");
    }

    @After
    public void tearDown() throws Exception
    {
        connection.close();
    }

    @Test
    public void testCorrectLogin()
    {
        try
        {
            TestCallbackHandler callbackHandler = new TestCallbackHandler("testuser", "abcd");
            LoginContext lc = new LoginContext("SaltedHashLoginModule-MD5", callbackHandler);
            lc.login();

            Subject subject = lc.getSubject();
            Set<Principal> principals = subject.getPrincipals();

            Principal userPrincipal = principals.iterator().next();
            Assert.assertTrue(userPrincipal instanceof UserPrincipal);
            Assert.assertEquals("testuser", userPrincipal.getName());
        }
        catch (LoginException ex)
        {
            System.out.println(ex.getMessage());
            Assert.fail();
        }
    }

    @Test
    public void testIncorrectLogin()
    {
        try
        {
            TestCallbackHandler callbackHandler = new TestCallbackHandler("testuser", "falsepw");
            LoginContext lc = new LoginContext("SaltedHashLoginModule-MD5", callbackHandler);
            lc.login();

            Assert.fail();
        }
        catch (LoginException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
