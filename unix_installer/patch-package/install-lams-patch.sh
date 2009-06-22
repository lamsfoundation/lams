#!/bin/bash
#   Copyright (C) 2006-2008 LAMS Foundation (http://lamsfoundation.org)
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

# Patch shell script for LAMS

LAMS_VERSION_UPDATE=2.3.1
LAMS_SERVER_VERSION=2.3.1.200806190000
REQ_LAMS_VERSION=2.3

USE_ETC_PROPERTIES=0;

# backup the lams.properties
if [ ! -r docs/lams.properties.backup.orig ]
then
	cp lams.properties docs/lams.properties.backup.orig
fi

if [ -r /etc/lams2/lams.properties ]
then
    useproperties=""
    clear
    printf "\nThe LAMS updater has detected a lams.properties file in /etc/lams2.\n"
    printf "Do you wish to use this file? (y)es (n)o (q)uit\n> "
    read useproperties

    case "$useproperties" in
        y)
        printf "\nUsing lams.properties from /etc/lams2.\n"
        . /etc/lams2/lams.properties 
        cp lams.properties docs/lams.properties.backup.orig
        cp /etc/lams2/lams.properties .
        USE_ETC_PROPERTIES=1; 
                ;;
        n)
        . lams.properties
                ;;
        *)
        installexit
        ;;
    esac  
else
	. lams.properties  
fi

#escaping & in jdbc url
VAR_SQL_URL=jdbc:mysql://${SQL_HOST}/${DB_NAME}?characterEncoding=utf8\&autoReconnect=true


# Invoked when the install is failed
installfailed()
{
    echo ""
    cp lams.properties docs/lams.properties.backup.exec
    cp docs/lams.properties.backup.orig lams.properties
    #export JAVA_HOME=$ORIG_JAVA_HOME
    exit 1
}

# Invoked when the install is exited
installexit()
{
    printf "\nLAMS update exited by user.\n\n"
    cp lams.properties docs/lams.properties.backup.exec
    cp docs/lams.properties.backup.orig lams.properties
    #export JAVA_HOME=$ORIG_JAVA_HOME
    exit 0
}

checklams()
{
	# Checking that the lams.properties points to a lams installation
	if [ ! -r "$JBOSS_DIR/server/default/deploy/lams.ear/lams.jar" ]
	then
	        printf "\nUpdate failed, Could not find lams.jar at $JBOSS_DIR/server/default/deploy/lams.ear.\nPlease check that have LAMS $REQ_LAMS_VERSION is installed and your lams.properties file is correct before running this update.\n"
	        installfailed
	fi
    
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

checkMysql()
{
    echo ""
    $JDK_DIR/bin/java -cp .:bin/:lib/mysql-connector-java-5.0.8-bin.jar checkmysql "$VAR_SQL_URL" "$DB_USER" "$DB_PASS" "$REQ_LAMS_VERSION"

    if [  "$?" -ne  "0" ]
    then
    	installfailed
    fi
}

backup()
{
    printf "\nDo you wish to automatically backup lams before updating? (Recommended. NOTE: Requires MySql to be installed at localhost)\n"
    printf "Please check the below directories are correct before running this backup. If they are not, quit the installer and backup LAMS manually.\n"
    printf "The space required to backup your LAMS installation:\n"
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
        printf "> $sqldir/mysqldump -u$dbuser -p$dbpass $dbname > (backup dir)/dump.sql\n"
        installexit 
        ;;
    y)
        
        
        $JDK_DIR/bin/java -cp bin backup
        if [  "$?" -ne  "0" ]
        then
                echo "Update failed, Failed to backup LAMS."
                installfailed
        fi

        chmod 755 bin/lamsdump.sql
        bin/lamsdump.sql
        if [  "$?" -ne  "0" ]
        then
                echo "Update failed, problem dumping database for backup."
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

clear
printf "\n--------------------------------------------------------------------------------\n\n"
printf "WELCOME to the LAMS $REQ_LAMS_VERSION to $LAMS_VERSION_UPDATE Unix Patch! \n\n"
printf "Please ensure you have read and accepted the licence agreement before continuing\n\n"
printf "This installer will prompt you to enter many configurations and locations\n"
printf "on your computer so please make sure you are ready.\n"
printf "You should read the installation guide before continuing.\n"
printf "\nJAVA_HOME = $JDK_DIR\n"
printf "\n--------------------------------------------------------------------------------\n\n"

printf "The patch can backup LAMS automatically for you, but if you would like to\n"
printf "backup LAMS manually before proceeding, please complete the following steps:\n"
printf "\n1) Backup $JBOSS_DIR\n"
printf "2) Backup $LAMS_DIR\n"
printf "3) Backup /etc/lams2\n"
printf "4) Dump the database by executing the following command. Fill in your own backup \ndirectory.\n"
printf "> $sqldir/mysqldump -u$DB_USER -p$DB_PASS $DB_NAME > (backup dir)/dump.sql\n"
printf "\n--------------------------------------------------------------------------------\n\n"

checklams
checkMysql
backup

printf "\nApplying $LAMS_VERSION_UPDATE patch, this may take a few moments...\n"


printf "\nRemoving jboss caches...\n"
rm -r $DEFAULT_DIR/work/*
rm -r $DEFAULT_DIR/tmp/*

################################################################################
# Running the 2.3.1 patch
################################################################################

printf "\nCopying files...\n"
cp -r assembly/* $EAR_DIR
printf "\nDone.\n"

printf "\nApplying language pack...\n"

# Just updating the combined copyllid task, the rest of the language files are copied with the jars and wars
if [ $USE_ETC_PROPERTIES -ne 1 ]
	then 
		ant/bin/ant -logfile language-pack.log -buildfile language-pack/language-pack.xml -Dpropertiesfile=../lams.properties copy-llid
	else
		ant/bin/ant -logfile language-pack.log -buildfile language-pack/language-pack.xml -Dpropertiesfile=/etc/lams2/lams.properties copy-llid
fi	

if [  "$?" -ne  "0" ]
then
	printf "\nLanguage-pack update failed, check language-pack.log."
	installfailed
else
	printf "\nDone.\n"
fi

printf "\nUpdating database...\n"
if [ $USE_ETC_PROPERTIES -ne 1 ]
then 
	ant/bin/ant -logfile update_db.log -buildfile ant-scripts/update_db.xml -Dpropertiesfile=../lams.properties update_db
else
	ant/bin/ant -logfile update_db.log -buildfile ant-scripts/update_db.xml -Dpropertiesfile=/etc/lams2/lams.properties update_db
fi

if [  "$?" -ne  "0" ]
then
	printf "\nDatabase update failed, check update_db.log."
	installfailed
else
	printf "\nDone.\n"
	printf "\n\nPatch update finished!\n"
	
	if [ $USE_ETC_PROPERTIES -ne 1 ]
	then 
		mkdir -p  /etc/lams2
		cp lams.properties /etc/lams2
	fi				
fi
