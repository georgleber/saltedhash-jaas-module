#### Activemq

Copy the saltedhash-jaas-module.jar to the activemq/lib directory

##### Database structure example

For postgresql, create 'users' and 'roles' tables as postgresql doesn't allow a table to be named 'user'.

```
create table users (id bigint primary key, username varchar(255), salt varchar(255), password varchar(255));
create table roles (id bigint primary key, name varchar(255));
create table roles_users (role_id bigint, user_id bigint, constraint roles_users_role_id foreign key (role_id) references roles (id), constraint roles_users_user_id foreign key (user_id) references users (id));

insert into users values (0, 'hawkeye', '8117ec859d0cf945fa9b28075f590707', '13e96bc7ade230967057dd138198dbd02491a8f6871f09b97c351b31a35b5878');
insert into roles values (0, 'mohican');
insert into roles_users values (0, 0);
```

##### login.config

The jaas configuration is contained in login.conf  
Note the use of the role-principal-class module option to use the internal GroupPrincipal principal role class required by activemq.  
If using postgresql, use the user/role query defined below, and copy the driver jar into the activemq/lib directory.

```
SaltedHashLoginModule {
    de.meetwithfriends.security.jaas.SaltedHashLoginModule required
      dbDriver="org.postgresql.Driver"
      dbURL="jdbc:postgresql://db.server:5432/amq_db"
      dbUser="amq_user"
      dbPassword="amq_passcode"
      mdAlgorithm="SHA-256"
      userQuery="select salt, password from users where username = ?"
      roleQuery="select ro.name from roles ro inner join roles_users rous on ro.id = rous.role_id inner join users us on rous.user_id = us.id where us.username = ?"
      role-principal-class=org.apache.activemq.jaas.GroupPrincipal
      debug=true;
};
```


##### activemq.xml

Add the following example configuration snippet to the activemq.xml plugins element.  
Customise as required.

```
<jaasAuthenticationPlugin configuration="SaltedHashLoginModule" />
<authorizationPlugin>
    <map>
        <authorizationMap>
            <authorizationEntries>
                <authorizationEntry queue="listener.example"  read="mohican,admins" write="mohican,admins" admin="mohican,admins"/>
                <authorizationEntry topic="ActiveMQ.Advisory.>"  read="mohican,admins" write="mohican,admins" admin="mohican,admins"/>
            </authorizationEntries>
            <tempDestinationAuthorizationEntry>
                <tempDestinationAuthorizationEntry read="tmp" write="tmp" admin="tmp"/>
            </tempDestinationAuthorizationEntry>
        </authorizationMap>
    </map>
</authorizationPlugin>
```