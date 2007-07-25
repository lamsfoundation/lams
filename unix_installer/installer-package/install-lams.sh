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

# Installer shell script for LAMS 2.0.4

# Usage: sudo ./install.sh

LAMS_VERSION=2.0.4
MYSQL_VERSION_STR=5.
JAVA_REQ_VERSION=1.5
# Transform the required version string into a number that can be used in comparisons
JAVA_REQ_VERSION=`echo $JAVA_REQ_VERSION | sed -e 's;\.;0;g'`

# getting all the lams.properties variables into the variable space
. ./lams.properties

wrapper=""

# invoked when the install is failed
installfailed()
{
       	echo ""
	export JAVA_HOME=$ORIG_JAVA_HOME        
	exit 1
}

# Invoked when the install is exited
installexit()
{
	echo ""
        export JAVA_HOME=$ORIG_JAVA_HOME
	exit 0
}

checkJava()
{
	ORIG_JAVA_HOME=$JAVA_HOME
        export JAVA_HOME=$JDK_DIR
        JAVA_EXE=$JAVA_HOME/bin/java

	java -cp bin testJava	
	if [  "$?" -ne  "0" ]
        then
		echo "Install failed. Could not verify java installation, check your lams.properties file for the correct JDK_DIR entry, and that you have set your JAVA_HOME and PATH environment variables correctly."
		echo "Refer to the readme for help with setting up java for LAMS."
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
		installexit
	fi
}

checkMysql()
{
	if [ $SQL_HOST = localhost ]
        then

		$JDK_DIR/bin/java -cp .:bin/:assembly/lams.ear/mysql-connector-java-3.1.12-bin.jar checkmysql "$MYSQL_DB_URL" "root" "$DB_ROOT_PASSWORD" "$MYSQL_VERSION_STR"
		if [  "$?" -ne  "0" ]
        	then
        		printf "\nInstall Failed. MySql check did not pass. Please check that your DB_ROOT_PASSWORD in lams.properties is set to the root password of your MySql 5.x server\n\n"
        		installfailed
		fi
	fi
}

createDatabase()
{
	if [ $SQL_HOST = localhost ]
	then
		printf "\nCreating LAMS database.\n"
	
	        ant/bin/ant -buildfile ant-scripts/configure-database.xml -logfile log/create-database.log create-database
		if [  "$?" -ne  "0" ]
		then
        		echo "Install failed. Error creating the LAMS database. Check your MySql settings and try again."
        		installfailed
		fi
		printf "Database Created.\n \n"
	fi

	printf "Filling database with LAMS tables\n\n"
	ant/bin/ant -buildfile ant-scripts/filter-config.xml -logfile log/filter-config.log filter-config
        if [  "$?" -ne  "0" ]
        then
                        echo "Install failed. Error generating setLamsConfiguration.sql. Please check log/filter-config for errors.\n\n"
                        installfailed
        fi


	ant/bin/ant -buildfile ant-scripts/configure-database.xml -logfile log/fill-database.log fill-database
	if [  "$?" -ne  "0" ]
        then
                        printf "\nInstall failed. Could not fill the LAMS database. If you are installing with a remote MySql server, please check that your SQL_HOST in lams.properties points to a MySql 5.x installation and that you have created a database with the name DB_NAME as in lams.properties and that the DB_USER has remote access granted. Check the readme for instructions on how to do this.\n\n"
                        installfailed
        fi
	print "Done.\n\n"

}

chooseWrapper()
{
        isMac=`uname -a | grep Mac`
        if [ "$isMac" ]
        then
                printf "\nIt looks like you have a Macintosh. We can't install the wrapper automatically for Macs. If you want to use the wrapper, you will need to install it manually. \nSee http://wiki.lamsfoundation.org/display/lamsdocs/Setup+Java+Service+Wrapper+for+LAMS.\n\nPress enter to continue with installation."
                read temp
        else
                isMac=`uname -a | grep Darwin`
                if [ "$isMac" ]
                then
                        printf "\nIt looks like you have a Macintosh. We can't install the wrapper automatically for Macs. If you want to use the wrapper, you will need to install it manually. \nSee http://wiki.lamsfoundation.org/display/lamsdocs/Setup+Java+Service+Wrapper+for+LAMS.\n\nPress enter to continue with installation."
                        read temp
                else
                        arch=`uname -m`
                        if [ $arch = i386 ]
                        then
                                wrapper=wrapper-linux-x86-32-3.2.3
                        fi

                        if [ $arch = "ia64" ] || [ $arch = "IA64" ]
                        then
                                wrapper=wrapper-linux-x86-64-3.2.3
                        fi

                        if [ $arch = "ppc" ]|| [ $arch = "PPC" ]
                        then
                                wrapper=wrapper-linux-ppc-64-3.2.3
                        fi

                        if [ $wrapper ]
                        then
                                printf "\nThe installer has detected that you need this wrapper: $wrapper\n"
                                printf "If you wish to use one of the others please select it instead.\n"
                        else
                                printf "\nThe installer could not detect a suitable wrapper, please select one from below.\n"
                        fi
                        printf "(1) wrapper-linux-x86-32-3.2.3\n"
                        printf "(2) wrapper-linux-x86-64-3.2.3\n"
                        printf "(3) wrapper-linux-ppc-64-3.2.3\n"
                        printf "(n) No wrapper, continue with install\n"
                        printf "(q) quit\n"
                        printf "> "
                        input=""
                        read input

                        case "$input" in
                        1)
                                wrapper=wrapper-linux-x86-32-3.2.3
                                ;;
                        2)
                                wrapper=wrapper-linux-x86-64-3.2.3
                                ;;
                        3)
                                wrapper=wrapper-linux-ppc-64-3.2.3
                                ;;
                        q)
                                printf "\n\nBye!\n\n"
                               	installexit
				;;
			n)
				wrapper=""
				;;
			*)
				printf "\nPlease type 1, 2, 3, n or q and press enter.\n"
				chooseWrapper
				;;
			esac

		fi
	fi
}



installWrapper()
{
        printf "\nDo you wish to install LAMS as a Java Service Wrapper? (y)es, (n)o, (q)uit: "
        input=""
        read input

        case "$input" in
        q)
           	printf "\n\nBye!\n\n" 
                installexit
                ;;
        y)
		chooseWrapper
		;;
        n)
                ;;
        *)
                printf "\nPlease enter y, n or q.\n"
                installWrapper
                ;;
        esac
}

configureWrapper()
{
	if [ $wrapper ]
	then
		printf "\n\nInstalling and configuring Java Service Wrapper: $wrapper\n"
		cp "wrapper/$wrapper/bin/wrapper" "$JBOSS_DIR/bin"
		cp "wrapper/sh.script.in" "$JBOSS_DIR/bin/lams2"
		
		cp "wrapper/$wrapper/lib/libwrapper.so" "wrapper/$wrapper/lib/wrapper.jar" "$JBOSS_DIR/lib"
		#cp "wrapper/wrapper.conf.in" "$JBOSS_DIR/conf/wrapper.conf"
		ant/bin/ant -buildfile ant-scripts/configure-deploy.xml -logfile log/configure-wrapper.log configure-wrapper
		if [  "$?" -ne  "0" ]
  	    	then
        		echo "\nInstall Failed. Problem while configuring wrapper. Please configure-wrapper.log for details.\n\n"
        		installfailed
		fi


		if [ -f "$JBOSS_DIR/logs/" ]
		then
			printf " "
		else
			mkdir "$JBOSS_DIR/logs/"
		fi
		cp wrapper/run.sh "$JBOSS_DIR/bin"
		
		if [ -f /etc/init.d/lams2 ]
		then
			rm /etc/init.d/lams2
		fi
		ln -s "$JBOSS_DIR/bin/lams2" /etc/init.d/lams2
		printf "Done.\n\n"
	fi
}

checkJboss()
{
	if [ -f "$JBOSS_DIR/lib/jboss-common.jar" ]
	then
  		echo "JBoss Directory Found"
	else
		printf "\n\nInstall Failed. No installation of JBoss-4.0.2 was found at $JBOSS_DIR. Please check your lams.properties file and retry.\n\n"
		installfailed
	fi	
}

checkJava
checkMysql
checkJboss

clear
printf "\n--------------------------------------------------------------------------------\n\n"
printf "WELCOME to the LAMS $LAMS_VERSION unix Installer! \n\n"
printf "Please ensure you have read and accepted the license agreement before continuing\n\n"
printf "Make sure you have correctly configured the lams.properties file to your \n"
printf "preferred settings.\n\n"
printf "You should read the installation guide before continuing.\n"
printf "\nJAVA_HOME = $JAVA_HOME\n"
printf "\n--------------------------------------------------------------------------------\n\n"

if [ $SQL_HOST != localhost ]
then
	printf "You are installing LAMS with a remote MySQL server. You MUST first create the \nLAMS database and user with remote privileges before continuing. Refer to the\nreadme chapter 'Setting up MySql on a Different Server' for instructions on how\nto do this.\n\nDo you wish to Continue? (y)es or (n)o: " 
	continue=""
	read continue
	case "$continue" in
	y)
        	printf "\n"
		;;
	n)      
		printf "\nBye!\n\n"
        	installexit
        	;;
	*)      printf "\n\n"
        	installfailed
        	;;
		esac
fi

printf "LAMS requires about 117MB of space, continue with installation? (y)es, (n)o: "
continue=""
read continue
case "$continue" in
y)
	;;
n)	printf "\nBye!\n\n"
	installexit
	;;
*)  	printf "\n\n"
	installfailed
	;;
esac

# Choosing whether to install the wrapper
installWrapper


printf "\nUsing LAMS database with the following parameters...\n"
printf "Database name: $DB_NAME\n"
printf "Database user: $DB_USER\n"
printf "Database password: $DB_PASS\n"
createDatabase
printf "\nDatabase Created.\n\n"

# Copying the main lams files
printf "Copying lams.ear directory to ${DEFAULT_DIR}/deploy.\n"
cp -rv assembly/lams.ear ${DEFAULT_DIR}/deploy

printf "\nDone.\n\n"
if [  "$?" -ne  "0" ]
        then
        echo "Install Failed. Problem while copying the jboss-4.0.2 directory, please try again."
       	installfailed
fi

# UNCOMMENT FOR (2.1) Create news-unmodified.html and place it with news.html such that the upgrader can do a 
# diff between the two files to determine whether it should update news.html or not.
# cp assembly/lams.ear/lams-www.war/news.html ${DEFAULT_DIR}/deploy/lams.ear/lams-www.war/news-unmodified.html

# Configuring jboss with settings from lams.properties
printf "Configuring JBoss with your settings.\n"
ant/bin/ant -buildfile ant-scripts/configure-deploy.xml -logfile log/copy-conf.log copy-conf
if [  "$?" -ne  "0" ]
        then
        echo "Install Failed. Problem while copying the jboss-4.0.2 configuration files, check log/configure-deploy.log for details."
        installfailed
fi

chmod 755 $JBOSS_DIR/bin/run-lams.sh

# configure the wrapper
printf "\nConfiguring the java Wrapper\n"
configureWrapper

# configure jboss
mkdir ${DEFAULT_DIR}/lib
cp $JBOSS_DIR/server/all/lib/jgroups.jar $JBOSS_DIR/server/all/lib/jboss-cache.jar ${DEFAULT_DIR}/lib
cp assembly/lams-session.jar  assembly/lams-valve.jar ${DEFAULT_DIR}/lib
if [  "$?" -ne  "0" ]
        then
        echo "\nInstall Failed. Problem while configuring JBoss, please ensure you have the correct version of JBoss (4.0.2).\n\n"
        installfailed
fi


printf "Copying lams.properties to /etc.\n"	
mkdir -p  /etc/lams2
cp lams.properties /etc/lams2/

# changing JAVA_HOME back 
export JAVA_HOME=$ORIG_JAVA_HOME

printf "\n\nLAMS $LAMS_VERSION Configuration completed!\n"
printf "Please view the README for instructions on how to run LAMS.\n\n"


