#!/bin/bash
# A simple uninstall script, good only for development testing on unix servers
# Possibly vamp this up in the future to be release-worthy
# Written by lfoxton

if [ -r /etc/init.d/lams2 ]
then
	/etc/init.d/lams2 stop
fi

if [ -r /etc/lams2/lams.properties ]
then
	. /etc/lams2/lams.properties
else
	. lams.properties
fi

if [ -d $EAR_DIR ]
then
	echo "Removing $EAR_DIR..."
	rm -rv $EAR_DIR
	rm -rv $DEPLOY_DIR/mysql-ds.xml
	
	echo "Removing LAMS libraries..."
	rm -rv $DEFAULT_DIR/lib/lams-session.jar
	rm -rv $DEFAULT_DIR/lib/lams-valve.jar
	
	echo "Removing wrapper..."
	rm -rv $JBOSS_DIR/bin/lams2
	rm -rv /etc/init.d/lams2
else 
	echo "$EAR_DIR does not exist. Continuing with uninstall."
fi

if [ -d /etc/lams2 ]
then
	echo "Removing /etc/lams2..."
	rm -rv /etc/lams2
else 
	echo "/etc/lams2 does not exist. Continuing with uninstall."
fi

if [ -d $LAMS_DIR ]
then
	echo "Removing $LAMS_DIR..."
	rm -rv $LAMS_DIR
else 
	echo "$LAMS_DIR does not exist. Continuing with uninstall."
fi

echo "Removing database $DB_NAME..."
$SQL_DIR/mysql -u$DB_USER -p$DB_PASS -e "drop database if exists $DB_NAME"

echo ""
echo "Uninstall Complete."