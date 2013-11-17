SaltedHash JAAS LoginModule
======================

JAAS LoginModule for checking login with salted hashes


This JAAS LoginModule is used for login with a salted user password. The configuration of this module is very flexible, all SQL statements can be defined in the JAAS configuration file: 

```
SaltedHashLoginModule {
  de.meetwithfriends.security.jaas.SaltedHashLoginModule required 
    dbDriver="org.h2.Driver"
    dbURL="jdbc:h2:mem:test"
    dbUser="sa"
    dbPassword="sa"
    mdAlgorithm="SHA-256"
    userQuery="select salt, password from user where username = ?"
    roleQuery="select ro.name from role ro inner join roles_users rous on ro.id = rous.roles_id inner join user us on rous.users_id = us.id where us.username = ?"
    debug=true;
};
```

Following list shows all configuration parameters: 
* dBDriver - database driver class
* dbURL - connection URL 
* dbUser - user for login
* dbPassword - password for login
* mdAlgorithm - MessageDigest algorithm used for encryption (optional: if not set, SHA-256 is used)
* userQuery - SQL query for loading user related data from database (first column is used as salt, second column as password)
* roleQuery - SQL query for loading the associated roles of the authenticated user
* debug - activate / deactivate debugging information (optional: default is false) 
