package de.meetwithfriends.security.jdbc.data;

public class ConfigurationData
{
    private String dbUser;
    private String dbPassword;
    private String dbUrl;
    private String dbDriver;
    private String userQuery;
    private String roleQuery;

    public String getDbUser()
    {
        return dbUser;
    }

    public void setDbUser(String dbUser)
    {
        this.dbUser = dbUser;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

    public String getDbUrl()
    {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver)
    {
        this.dbDriver = dbDriver;
    }

    public String getUserQuery()
    {
        return userQuery;
    }

    public void setUserQuery(String userQuery)
    {
        this.userQuery = userQuery;
    }

    public String getRoleQuery()
    {
        return roleQuery;
    }

    public void setRoleQuery(String roleQuery)
    {
        this.roleQuery = roleQuery;
    }

}
