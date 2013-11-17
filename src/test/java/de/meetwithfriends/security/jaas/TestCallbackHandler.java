package de.meetwithfriends.security.jaas;

import java.io.IOException;
import javax.security.auth.callback.*;

public class TestCallbackHandler implements CallbackHandler
{
    private String name;
    private String password;

    public TestCallbackHandler(String name, String password)
    {
        this.name = name;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException
    {
        for (Callback callback : callbacks)
        {
            if (callback instanceof NameCallback)
            {
                NameCallback nameCallback = (NameCallback) callback;
                nameCallback.setName(name);
            }
            else if (callback instanceof PasswordCallback)
            {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                passwordCallback.setPassword(password.toCharArray());
            }
            else
            {
                throw new UnsupportedCallbackException(callback, "The submitted Callback is unsupported");
            }
        }
    }
}
