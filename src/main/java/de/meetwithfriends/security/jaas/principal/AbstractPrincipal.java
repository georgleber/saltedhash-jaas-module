package de.meetwithfriends.security.jaas.principal;

import java.io.Serializable;
import java.security.Principal;

public abstract class AbstractPrincipal implements Principal, Serializable
{
    private final String name;

    public AbstractPrincipal(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("name can not be null");
        }

        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj instanceof AbstractPrincipal)
        {
            return name.equals(((Principal) obj).getName());
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return getClass().getName() + ":" + name;
    }

}
