SaltedHash JAAS LoginModule
======================

[![Build Status](https://travis-ci.org/Develman/saltedhash-jaas-module.png?branch=master)](https://travis-ci.org/Develman/saltedhash-jaas-module)

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

#### Database structure example
```
create table users (id bigint primary key, username varchar(255), salt varchar(255), password varchar(255));
create table roles (id bigint primary key, name varchar(255));
create table roles_users (role_id bigint, user_id bigint, constraint roles_users_role_id foreign key (role_id) references roles (id), constraint roles_users_user_id foreign key (user_id) references roles (id));

insert into users values (0, 'hawkeye', '8117ec859d0cf945fa9b28075f590707', '13e96bc7ade230967057dd138198dbd02491a8f6871f09b97c351b31a35b5878');
insert into roles values (0, 'mohican');
insert into roles_users values (0, 0);
```

#### Running from maven 
```
mvn exec:java  -Dexec.args=MyVoiceIsMyPassword
```
