package de.meetwithfriends.security.jdbc;

import de.meetwithfriends.security.jdbc.data.UserData;
import de.meetwithfriends.security.util.StringUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.security.auth.login.LoginException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class JdbcAuthenticationService
{
    private static final Logger LOG = LogManager.getLogger(JdbcAuthenticationService.class);

    private String mdAlgorithm = "SHA-256";
    private AuthenticationDao authenticationDao;
    private boolean debug;

    public JdbcAuthenticationService(AuthenticationDao authenticationDao, boolean debug)
    {
        this.authenticationDao = authenticationDao;
        this.debug = debug;
    }

    public void setMdAlgorithm(String mdAlgorithm)
    {
        this.mdAlgorithm = mdAlgorithm;
    }

    public boolean authenticate(String username, String password) throws LoginException
    {
        if (debug)
        {
            LOG.debug("Trying to authenticate user: " + username);
        }

        boolean authenticated = false;

        UserData result = authenticationDao.loadUserData(username);
        if (result.getSalt() != null && result.getPassword() != null)
        {
            if (debug)
            {
                LOG.debug("Salt and password loaded from database");
            }

            authenticated = checkPasswordMatching(password, result);
        }

        return authenticated;
    }

    private boolean checkPasswordMatching(String password, UserData userData)
    {
        boolean matching = false;

        try
        {
            String saltedPassword = password + userData.getSalt();

            MessageDigest md = MessageDigest.getInstance(mdAlgorithm);
            md.update(saltedPassword.getBytes());

            byte byteData[] = md.digest();

            String userPassword = StringUtil.convertToHex(byteData);
            if (userPassword.equalsIgnoreCase(userData.getPassword()))
            {
                if (debug)
                {
                    LOG.debug("Passwords are equal, user authenticated");
                }

                matching = true;
            }

        }
        catch (NoSuchAlgorithmException ex)
        {
            LOG.error(mdAlgorithm + " not available, unable to check passwords", ex);
        }

        return matching;
    }

}
