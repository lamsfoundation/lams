#!/bin/sh
#   Copyright (C) 2006-2007 LAMS Foundation (http://lamsfoundation.org)
#   License Information: http://lamsfoundation.org/licensing/lams/2.0/
#
#   This program is free software; you can redistribute it and/or modify
#   it under the terms of the GNU General Public License version 2.0 
#   as published by the Free Software Foundation.
# 
#   This program is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
# 
#   You should have received a copy of the GNU General Public License
#   along with this program; if not, write to the Free Software
#   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
#   USA
# 
#   http://www.gnu.org/licenses/gpl.txt 
#
#   Author: Luke Foxton 
#   Updater shell script for LAMS

# Usage: sudo ./update-lams.sh

LAMS_VERSION=2.0.4
REQ_LAMS_VERSION=2.0.2
JAVA_REQ_VERSION=1.5
# Transform the required version string into a number that can be used in comparisons
JAVA_REQ_VERSION=`echo $JAVA_REQ_VERSION | sed -e 's;\.;0;g'`

# backup the lams.properties
cp lams.properties docs/lams.properties.backup.orig

# Invoked when the install is failed
installfailed()
{
        echo ""
	cp lams.properties docs/lams.properties.backup.exec
	cp docs/lams.properties.backup.orig lams.properties
	export JAVA_HOME=$ORIG_JAVA_HOME
        exit 1
}

# Invoked when the install is exited
installexit()
{
	printf "\nLAMS update exited by user.\n\n"
	cp lams.properties docs/lams.properties.backup.exec
	cp docs/lams.properties.backup.orig lams.properties
	export JAVA_HOME=$ORIG_JAVA_HOME
        exit 0
}


# Detecting if there is an existing lams.properties file
if [ -r /etc/lams2/lams.properties ]
then
	useproperties=""
	clear
	printf "\nThe LAMS $LAMS_VERSION updater has detected a lams.properties file in /etc/lams2.\n"
	printf "Do you wish to use this file? (y)es (n)o (q)uit\n> "
	read useproperties

	case "$useproperties" in
        y)
		printf "\nUsing lams.properties from /etc/lams2.\n"
		cp /etc/lams2/lams.properties .		

		# backup the lams.properties
		cp lams.properties docs/lams.properties.backup.etc
		sleep 1
                ;;
        n)
		# do nothing
                ;;
        *)
		installexit
		;;
	esac	
fi

# getting all the lams.properties variables into the variable space
. ./lams.properties

# New variables introduced with 2.0.4 updater
SQL_HOST=localhost
SQL_URL=jdbc:mysql://$SQL_HOST/$DB_NAME?characterEncoding=utf8&autoReconnect=true

# Checking that the lams.properties points to 'a' lams installation
if [ ! -r "$JBOSS_DIR/server/default/deploy/lams.ear/lams.jar" ]
                then
                        printf "\nUpdate failed, Could not find lams.jar at JBOSS_DIR/server/default/deploy/lams.ear.\nPlease check that have LAMS $REQ_LAMS_VERSION is installed and your lams.properties file is correct before running this update.\n"
                        installfailed
fi


checkJava()
{
	ORIG_JAVA_HOME=$JAVA_HOME
        export JAVA_HOME=$JDK_DIR
        JAVA_EXE=$JAVA_HOME/bin/java

	$JDK_DIR/bin/java -cp bin testJava	
        if [  "$?" -ne  "0" ]
        then
		echo "Update failed. Could not verify java installation, check your lams.properties file for the correct JDK_DIR entry, and that you have set your JAVA_HOME and PATH environment variables correctly"
		echo "Refer to the readme for help with setting up java for LAMS"
        	installfailed
	fi
	
	# Check JAVA_HOME directory to see if Java version is adequate
	if [ $JAVA_HOME ]
	then
		JAVA_EXE=$JAVA_HOME/bin/java
		$JAVA_EXE -version 2> tmp.ver
		VERSION=`cat tmp.ver | grep "java version" | awk '{ print substr($3, 2, length($3)-2); }'`
		rm tmp.ver
		VERSION=`echo $VERSION | awk '{ print substr($1, 1, 3); }' | sed -e 's;\.;0;g'`
		if [ $VERSION ]
		then
			if [ $VERSION -ge $JAVA_REQ_VERSION ]
			then
				JAVA_HOME=`echo $JAVA_EXE | awk '{ print substr($1, 1, length($1)-9); }'`
			else
				JAVA_HOME=
			fi
		else
			JAVA_HOME=
		fi
	fi

	# If the existing JAVA_HOME directory is adequate, then leave it alone
	# otherwise, use 'locate' to search for other possible java candidates and
	# check their versions.
	if [ $JAVA_HOME ]
	then
		:
	else
		for JAVA_EXE in `locate bin/java | grep java$ | xargs echo`
		do
			if [ $JAVA_HOME ] 
			then
				:
			else
				$JAVA_EXE -version 2> tmp.ver 1> /dev/null
				VERSION=`cat tmp.ver | grep "java version" | awk '{ print substr($3, 2, length($3)-2); }'`
				rm tmp.ver
				VERSION=`echo $VERSION | awk '{ print substr($1, 1, 3); }' | sed -e 's;\.;0;g'`
				if [ $VERSION ]
				then
					if [ $VERSION -ge $JAVA_REQ_VERSION ]
					then
						JAVA_HOME=`echo $JAVA_EXE | awk '{ print substr($1, 1, length($1)-9); }'`
					fi
				fi
			fi
		done
	fi

	# If the correct Java version is detected, then export the JAVA_HOME environment variable
	if [ $JAVA_HOME ]
	then
		export JAVA_HOME
	else
		echo "No installation of java found, please set your JAVA_HOME to point to java 1.5 or newer, then run the installer."
		installfailed
	fi
}

checkMysql()
{
	printf "Checking LAMS database...\n"
        $JDK_DIR/bin/java -cp .:bin/:assembly/lams.ear/mysql-connector-java-3.1.12-bin.jar checkmysql "$SQL_URL" "$DB_USER" "$DB_PASS" "$REQ_LAMS_VERSION"

	if [  "$?" -ne  "0" ]
        then
		installfailed
        fi
}

getMysqlHost()
{
	SQL_HOST=""
        SQL_URL=""
        continue=""
        printf "Please enter the location of your MySql Host (Default: localhost)\n> "
        read SQL_HOST
        if [ -z "$SQL_HOST" ]
	then	
                SQL_HOST=localhost
	fi

        SQL_URL="jdbc:mysql://$SQL_HOST/$DB_NAME?characterEncoding=utf8&autoReconnect=true"

        printf "\nAll LAMS database update connections will be done through: \n$SQL_URL.\n"
        printf "Is this correct? (y)es, (n)o, (q)uit.\n> "
        read continue

        case $continue in
        q)
                printf "\nTo set up MySql on a separate server, follow the instructions in the readme.\n\n"
                printf "LAMS update aborted by user.\n\n"
                installexit
                ;;
        y)
	
		# append SQL_HOST and SQL_URL to lams.properties
               	echo "# Updater-generated properties, used instead of default localhost" >> lams.properties
		echo "SQL_HOST=$SQL_HOST" >> lams.properties
		echo "SQL_URL=jdbc:mysql://${SQL_HOST}/${DB_NAME}?characterEncoding=utf8&autoReconnect=true" >> lams.properties
		printf "\n"
		;;
	n)
                printf "\n"
                getMysqlHost
                ;;
        *)
                printf "\nPlease enter your MySql host and then enter (y).\n"
                getMysqlHost
                ;;
        esac
}

checklams()
{
	printf "Do you want to run the LAMS shutdown script before continuing? (Recommended)\n"
	printf "(y)es I want to run the JBOSS shutdown script.\n"
	printf "(n)o I have already shutdown LAMS. Continue with the upgrade.\n"
	printf "(q)uit.\n"
	printf "> "
	shutdown=""
	read shutdown

	case "$shutdown" in 
	q)
		echo "Update aborted by user"
		installexit
		;;
	y)
		printf "\nPlease wait a few moments while LAMS shuts down\n"
		if [ -x "$JBOSS_DIR/bin/lams2" ]
		then
			$JBOSS_DIR/bin/lams2 stop
		else
	        	$JBOSS_DIR/bin/shutdown.sh -S 	
		fi	
	
		printf "\nWaiting for shutdown to finish.\n"
		printf "..........."
		sleep 3
		printf "....................."
		sleep 3
		printf "..............................."
		sleep 4
		printf ".................\n"
		printf "Shutdown completed\n\n"
		;;
	n) 

		;;
	*)
		printf "\nPlease enter y, n or q\n"
		checklams
		;;
	esac
}

backup()
{
	printf "\nDo you wish to automatically backup lams before updating? (Recommended. NOTE: Requires MySql to be installed at localhost)\n"
	printf "The space required to backup your LAMS installation: \n"
	du -chs $LAMS_DIR $JBOSS_DIR
	printf "(y)es I wish to backup LAMS.\n"
	printf "(n)o I have already backed up LAMS, I am ready to update.\n"
	printf "(q)uit.\n"
	printf "> "
	backup=""	
	read backup

	case "$backup" in 
	q)
		printf "\nIf you would like manually backup LAMS, please complete the following steps:\n"
		printf "\n1) Backup $JBOSS_DIR\n"
		printf "2) Backup $LAMS_DIR\n"
		printf "3) Backup /etc/lams2\n"
		printf "4) Dump the database by executing the following command. Fill in your own backup \ndirectory.\n"
		printf "> $sqldir/mysqldump -u$dbuser -p$dbpass $dbname > (backup dir)/lams.dump\n"
		installexit 
		;;
	y)
		
		
		$JDK_DIR/bin/java -cp bin backup
		if [  "$?" -ne  "0" ]
                then
                        echo "Update failed, please check that LAMS 2.0 is installed and your lams.properties file is correct."
                        installfailed
                fi

		chmod 755 bin/lamsdump.sql
		bin/lamsdump.sql
		if [  "$?" -ne  "0" ]
                then
                        echo "Update failed, please check that LAMS 2.0 is installed and your lams.properties file is correct."
                        installfailed
                fi

		;;
	n) 

		;;
	*)
		printf "\nPlease enter y, n or q\n"
		backup
		;;
	esac
}


checkJava
clear
printf "\n--------------------------------------------------------------------------------\n\n"
printf "WELCOME to the LAMS $LAMS_VERSION Unix updater! \n\n"
printf "Please ensure you have read and accepted the licence agreement before continuing\n\n"
printf "This installer will prompt you to enter many configurations and locations\n"
printf "on your computer so please make sure you are ready.\n"
printf "You should read the installation guide before continuing.\n"
printf "\nJAVA_HOME = $JAVA_HOME\n"
printf "\n--------------------------------------------------------------------------------\n\n"

printf "The updater can backup LAMS automatically for you, but if you would like to\n"
printf "backup LAMS manually before proceeding, please complete the following steps:\n"
printf "\n1) Backup $JBOSS_DIR\n"
printf "2) Backup $LAMS_DIR\n"
printf "3) Backup /etc/lams2\n"
printf "4) Dump the database by executing the following command. Fill in your own backup \ndirectory.\n"
printf "> $sqldir/mysqldump -u$dbuser -p$dbpass $dbname > (backup dir)/lams.dump\n"
printf "\n--------------------------------------------------------------------------------\n\n"

getMysqlHost
checkMysql
checklams
backup
printf "\nBeginning ant scripts, check log/install.log for install log. This may take a few minutes...\n"
ant/bin/ant -logfile log/install.log -buildfile ant-scripts/update-lams.xml update-lams
if [  "$?" -ne  "0" ]
        then
        echo "Update failed, check the log/install.log file for details."
       	installfailed
fi

# Checking the log to see if there was any failures from the tool deployer (invoked in update-lams.xml)
failed=`grep FAILED log/*`
if [ "$failed" ]
then
	printf "\nUpdate failed. There were failures during the updating process. You may have the wrong MySql host, database name, user or password in your lams.properties file.\nCheck the logs listed below for details.\n"
	printf "$failed\n"
	installfailed
fi


# creating the repository, dump and temp dirs if they dont already exist
if [ ! -d $LAMS_DIR ]
then
	mkdir $LAMS_DIR
fi
if [ ! -d $LAMS_DIR/repository ]
then
	mkdir $LAMS_DIR/repository
fi
if [ ! -d $LAMS_DIR/temp ]
then
        mkdir $LAMS_DIR/temp
fi
if [ ! -d $LAMS_DIR/dump ]
then
        mkdir $LAMS_DIR/dump
fi

##### 2.0.4 Update Specific Java Program, DELETE for later updates
$JDK_DIR/bin/java -cp  bin:bin/hibernate3.jar:tools/lib/mysql-connector-java-3.1.12-bin.jar:tools/lib/commons-logging.jar UpdateLAMS202Chat "$SQL_URL" "$DB_USER" "$DB_PASS" "$LAMS_DIR/repository" > log/update-chat-204.log
if [  "$?" -ne  "0" ]
then
        echo "Update failed, problem while updating chat tool to 2.0.4. Check log/update-chat-204.log for details.\n"
        installfailed
fi
#####

##### 2.0.4 Update specific, updating lamsauthentication.xml
if [ -d "$EAR_DIR/jbossweb-tomcat55.sar" ]
then 
	printf "\nRemoving $DEPLOY_DIR/jbossweb-tomcat55.sar\n"
	rm -r "$EAR_DIR/jbossweb-tomcat55.sar"
fi
cp conf/lamsauthentication.xml $DEFAULT_DIR/conf

ant/bin/ant -logfile log/update-conf.log -buildfile ant-scripts/update-204_jbossweb-tomcat55.sar.xml update-conf-204
if [  "$?" -ne  "0" ]
then
        echo "Update failed, problem while updating configuration files to 2.0.4. Check log/update-conf.log for details.\n"
        installfailed
fi
#####

# copying lams.properties to /etc/lams2
printf "\nCopying lams.properties to /etc/lams2"
if [ ! -d /etc/lams2 ]
then
        mkdir /etc/lams2
fi
cp lams.properties /etc/lams2/lams.properties

# changing JAVA_HOME back
export JAVA_HOME=$ORIG_JAVA_HOME

printf "\nLAMS $LAMS_VERSION Configuration completed!\n"
printf "Please view the README for instructions on how to run LAMS\n\n"



