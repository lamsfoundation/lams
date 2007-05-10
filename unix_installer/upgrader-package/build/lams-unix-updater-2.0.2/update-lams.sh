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

# Installer shell script for LAMS 2.0.2

# Usage: sudo ./install.sh


lams_dir=''
JAVA_REQ_VERSION=1.5
# Transform the required version string into a number that can be used in comparisons
JAVA_REQ_VERSION=`echo $JAVA_REQ_VERSION | sed -e 's;\.;0;g'`

lamsdir=`java -cp bin readProperty LAMS_DIR`
jbossdir=`java -cp bin readProperty JBOSS_DIR`
sqldir=`java -cp bin readProperty SQL_DIR`
dbname=`java -cp bin readProperty DB_NAME`
dbuser=`java -cp bin readProperty DB_USER`
dbpass=`java -cp bin readProperty DB_PASS`	

checkJava()
{
	
	
	jdkdir=`java -cp bin readProperty JDK_DIR`
	$jdkdir/bin/java -cp bin testJava	
        if [  "$?" -ne  "0" ]
        then
		echo "Update failed. Could not verify java installation, check your lams.properties file for the correct JDK_DIR entry, and that you have set your JAVA_HOME and PATH environment variables correctly"
		echo "Refer to the readme for help with setting up java for LAMS"
       		exit 1
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
		exit 0
	fi
}

checkmysql()
{
	mysqldir=`java -cp bin readProperty SQL_DIR`
		
        if [ -x  "$mysqldir/mysql" ]
        then
		echo "Found mysql executable"
	else
		echo "Update failed, could not fine sql executable at $mysqldir. Check your lams.properties file for correct configurations."
       		exit 1	
        fi

	java -cp bin:tools/lib/mysql-connector-java-3.1.12-bin.jar checkmysql
        if [  "$?" -ne  "0" ]
        then
                "Update failed, The version of lams installed is not compatible with this upgrader"
		exit 1
        fi
}

checklams()
{
	printf "Do you want to run the LAMS shutdown script before continuing? (Recommended)\n"
	printf "(y)es I want to run the JBOSS shutdown script\n"
	printf "(n)o I have already shutdown LAMS. Continue with the upgrade:\n"
	printf "(q)uit\n"
	printf "> "
	shutdown=""
	read shutdown

	case "$shutdown" in 
	q)
		exit
		;;
	y)
		java -cp bin getJbossDir
		if [  "$?" -ne  "0" ]
		then
			va -cp bin:tools/lib/mysql-connector-java-3.1.12-bin.jar checkmysql
echo "Update failed, please check that LAMS 2.0 is installed and you lams.properties file is correct."
			exit 1
		fi
		chmod 755 bin/shutdown.sh
		printf "\nPlease wait a few moments while LAMS shuts down\n"
		bin/shutdown.sh
		printf "Waiting for shutdown to finish\n"
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
	printf "Do you wish to backup lams before updating? (Recommended) (y)es, (n)o, (q)uit): "
	printf "The space required to backup your LAMS installation: \n"
	du -chs $lamsdir $jbossdir
	printf "(y)es, (n)o, (q)uit "
	printf "\n> "
	backup=""	
	read backup

	case "$backup" in 
	q)
		printf "\nTo backup your LAMS installation manually, simply follow the follwing steps..."
		printf "\n1) Backup $jbossdir\n"				      
		printf "2) Backup $lamsdir\n"					      
		printf "3) Backup /etc/lams2\n"					      
		printf "4) Dump the database by executing the following command. Fill in your own backup direcory\n"
		printf "> $sqldir/mysqldump -u$dbuser -p$dbpass $dbname > (backup dir)/lams.dump\n\n" 
		
		exit 0 
		;;
	y)
		checkmysql
		
		
		java -cp bin backup
		if [  "$?" -ne  "0" ]
                then
                        echo "Update failed, please check that LAMS 2.0 is installed and your lams.properties file is correct."
                        exit 1
                fi

		chmod 755 bin/lamsdump.sql
		bin/lamsdump.sql
		if [  "$?" -ne  "0" ]
                then
                        echo "Update failed, please check that LAMS 2.0 is installed and your lams.properties file is correct."
                        exit 1
                fi

		;;
	n) 
		checkmysql

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
printf "WELCOME to the LAMS 2.0.2 Unix updater! \n\n"
printf "Please ensure you have read and accepted the licence agreement before continuing\n\n"
printf "This installer will prompt you to enter many configurations and locations\n"
printf "on your computer so please make sure you are ready.\n"
printf "You should read the installation guide before continuing.\n"
printf "\nJAVA_HOME = $JAVA_HOME\n"
printf "\n--------------------------------------------------------------------------------\n\n"


checklams
backup
printf "\nBeginning ant scripts, check log/install.log for install log\n"
ant/bin/ant -logfile log/install.log -buildfile ant-scripts/update-lams-2.0.2.xml update-lams
if [  "$?" -ne  "0" ]
        then
        echo "Update failed, check the log/install.log file for details."
       	exit 1
fi


printf "\nLAMS 2.0.2 Configuration completed!\n"
printf "Please view the README for instructions on how to run LAMS\n\n"

printf "\nTo backup your LAMS installation manually, simply follow the follwing steps..."
printf "\n1) Backup $jbossdir\n"
printf "2) Backup $lamsdir\n"
printf "3) Backup /etc/lams2\n"
printf "4) Dump the database by executing the following command. Fill in your own backup direcory\n"
printf "> $sqldir/mysqldump -u$dbuser -p$dbpass $dbname > (backup dir)/lams.dump\n\n"

