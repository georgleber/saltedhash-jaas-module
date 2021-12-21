package de.meetwithfriends.security.jdbc;

import de.meetwithfriends.security.jdbc.data.UserData;
import de.meetwithfriends.security.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.security.auth.login.LoginException;

public class JdbcAuthenticationService
{
    private final Logger log = LoggerFactory.getLogger(JdbcAuthenticationService.class);

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
            log.debug("Trying to authenticate user: " + username);
        }

        boolean authenticated = false;

        UserData result = authenticationDao.loadUserData(username);
        if (result.getSalt() != null && result.getPassword() != null)
        {
            if (debug)
            {
                log.debug("Salt and password loaded from database");
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
            String userPassword = getSaltedPasswordDigest(password, userData.getSalt(), mdAlgorithm);
            if (userPassword.equalsIgnoreCase(userData.getPassword()))
            {
                if (debug)
                {
                    log.debug("Passwords are equal, user authenticated");
                }

                matching = true;
            }

        }
        catch (NoSuchAlgorithmException ex)
        {
            log.error(mdAlgorithm + " not available, unable to check passwords", ex);
        }

        return matching;
    }

    public static String getSaltedPasswordDigest(String password, String salt, String mdAlgorithm) throws NoSuchAlgorithmException {
        String saltedPassword = password + salt;

        MessageDigest md = MessageDigest.getInstance(mdAlgorithm);
        md.update(saltedPassword.getBytes());

        byte byteData[] = md.digest();

        String userPassword = StringUtil.convertToHex(byteData);
        return userPassword;
    }

}
