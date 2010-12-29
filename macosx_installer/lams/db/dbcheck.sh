#!/bin/sh

cd /usr/local/lams/db/
# Let's check if the root password is blank
export blank_password=`java -cp .:/usr/local/lams/jboss-4.0.2/server/default/deploy/lams.ear/mysql-connector-java-5.0.8-bin.jar checkmysqlversion jdbc:mysql://localhost/ root ''`

echo "$blank_password" > database.log

if echo "$blank_password" |grep "is compatible" > /dev/null 2>&1; 
then   
    echo "db.root.password=" >> db.properties
    /usr/local/lams/db/dbscript.sh
else
    /usr/local/lams/db/addrootpass.sh
    /usr/local/lams/db/dbscript.sh
fi