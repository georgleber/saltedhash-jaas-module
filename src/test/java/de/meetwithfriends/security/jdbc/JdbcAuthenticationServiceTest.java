package de.meetwithfriends.security.jdbc;

import de.meetwithfriends.security.jdbc.data.UserData;
import javax.security.auth.login.LoginException;
import org.junit.*;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class JdbcAuthenticationServiceTest
{
    @Mock
    AuthenticationDao authenticationDao;

    private JdbcAuthenticationService authenticationService;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        authenticationService = new JdbcAuthenticationService(authenticationDao, true);

        initStubs();
    }

    @Test
    public void testCorrectAuthentication() throws LoginException
    {
        boolean authenticated = authenticationService.authenticate("testuser", "abcd");
        Assert.assertTrue(authenticated);
    }

    @Test
    public void testIncorrectAuthentication() throws LoginException
    {
        boolean authenticated = authenticationService.authenticate("testuser", "dcba");
        Assert.assertFalse(authenticated);
    }

    private void initStubs()
    {
        Mockito.when(authenticationDao.loadUserData(ArgumentMatchers.anyString())).thenAnswer(new Answer<UserData>()
        {

            @Override
            public UserData answer(InvocationOnMock invocation) throws Throwable
            {
                UserData userData = new UserData();
                userData.setSalt("1234");
                userData.setPassword("e9cee71ab932fde863338d08be4de9dfe39ea049bdafb342ce659ec5450b69ae");

                return userData;
            }
        });
    }
}
