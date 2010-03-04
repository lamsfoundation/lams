/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */

/**
 * updater.nsi is actually an updater/installer/language-pack. There are 2 
 * possibilites:
 * 1) If LAMS 2.x is installed and this is a newer version, the updater will be 
 *    run
 
 
 * 2) If there is no LAMS 2.x installation, a full install will take place
 *
 * Builds to win_installer\build\LAMS-updater-$VERSION.exe
 * You must change the $VERSION to comply with this version (Line 49 approx)
 * Change the LANGUAGE_PACK_VERSION To the date you compile YYYY-MM-DD
 * Change the DATE_TIME_STAMP to the same as can be found in lams_common\db\sql\insert_windows_config_data.sql
 */

# includes
!include "TextFunc.nsh"
!include "includes\Functions.nsh"
!include "MUI.nsh"
!include "LogicLib.nsh"

# functions from TextFunc.nsh
!insertmacro FileJoin
;!insertmacro LineFind

# constants
!define VERSION "2.3.4"
!define PREVIOUS_VERSION "2.3.3"
!define LANGUAGE_PACK_VERSION "2010-03-04"
!define LANGUAGE_PACK_VERSION_INT "20100304"
!define DATE_TIME_STAMP "20100300000"
######################## Added in the extra .0 for 2.1 for constitency 
!define SERVER_VERSION_NUMBER "${VERSION}.0.${DATE_TIME_STAMP}"
!define BASE_VERSION "2.0"
!define SOURCE_JBOSS_HOME "D:\jboss-4.0.2"  ; location of jboss where lams was deployed
!define SOURCE_LAMS_EAR "${SOURCE_JBOSS_HOME}\server\default\deploy\lams.ear\"
!define SOURCE_JBOSS_LIB "${SOURCE_JBOSS_HOME}\server\default\lib"
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
!define BUILD_REPOSITORY "D:\repository"

# project directories
!define BASE_DIR "..\..\"
!define BUILD_DIR "${BASE_DIR}\build\"
!define BASE_PROJECT_DIR "${BASE_DIR}\..\"
!define DOCUMENTS "..\documents"
!define GRAPHICS "..\graphics"
!define TEMPLATES "..\templates"
!define CONF "..\conf"
!define LIB "..\lib"
!define ANT "ant"
!define SQL "sql"
!define ASSEMBLY "..\assembly"
!define DATABASE "..\..\database\"


# installer settings
!define MUI_ICON "${GRAPHICS}\lams2.ico"
!define MUI_UNICON "${GRAPHICS}\lams2.ico"
Name "LAMS ${VERSION}"
;BrandingText "LAMS ${VERSION} -- built on ${__TIMESTAMP__}"
BrandingText "LAMS ${VERSION} -- built on ${__DATE__} ${__TIME__}"
OutFile "${BUILD_DIR}\LAMS-${VERSION}-patch.exe"
InstallDir "C:\lams"
InstallDirRegKey HKLM "${REG_HEAD}" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 
InstProgressFlags smooth


# set warning when cancelling install
!define MUI_ABORTWARNING

# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Install/Update Wizard"
!define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the installation/update to LAMS ${VERSION}.\r\n\r\n\
    Please ensure you have a copy of MySQL 5.x installed and running, and Java JDK version 1.5.x. or 1.6.x\r\n\r\n\
    Click Next to continue."


# set instfiles page to wait when done
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# display finish page stuff
!define MUI_FINISHPAGE_RUN $INSTDIR\lams-start.exe
!define MUI_FINISHPAGE_RUN_TEXT "Start LAMS now"
;!define MUI_FINISHPAGE_TEXT "The LAMS Server has been successfully installed on your computer."
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\readme.txt
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Open the readme file"
!define MUI_FINISHPAGE_LINK "Visit LAMS Community"
!define MUI_FINISHPAGE_LINK_LOCATION "http://www.lamscommunity.org"

# installer screen progression
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "${DOCUMENTS}\license.txt"
Page custom PreComponents PostComponents
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE DirectoryLeave
!define MUI_PAGE_CUSTOMFUNCTION_PRE skipPage
!insertmacro MUI_PAGE_DIRECTORY
Page custom PreLAMSConfig PostLAMSConfig
Page custom PreMySQLConfig PostMySQLConfig
Page custom PreLAMS2Config PostLAMS2Config
Page custom PreWildfireConfig PostWildfireConfig
Page custom PreFinal PostFinal
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH


# supported translations
!insertmacro MUI_LANGUAGE "English" # first language is the default language


# reserve files
#
ReserveFile "lams-update.ini"
ReserveFile "final.ini"
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
!insertmacro MUI_RESERVEFILE_LANGDLL

# variables
Var MYSQL_DIR           ; path to user's mysql directory
Var MYSQL_HOST          ; ip address for mysql
Var MYSQL_PORT          ; mysql port
Var DB_NAME             ; db name for lams
Var DB_USER             ; db user for lams
Var DB_PASS             ; db pass for lams
Var JDK_DIR             ; path to user's JDK directory  
Var LAMS_DOMAIN         ; server URL for lams
Var LAMS_PORT           ; PORT for lams usually 8080
Var LAMS_LOCALE         ; default language locale on startup
Var LAMS_REPOSITORY     ; path to repository on user's box
;Var LAMS_USER           ; user name for lams system administrater
;Var LAMS_PASS           ; password for lams system administrater
Var WILDFIRE_DOMAIN     ; wildfire URL
Var WILDFIRE_USER       ; wildfire username
Var WILDFIRE_PASS       ; wildfie password

;Var RETAIN_DIR          ; path to directory to retain files on uninstall
;Var RETAIN_FILES        ; bool value to devide whether to retain files
Var TIMESTAMP           ; timestamp
Var BACKUP              ; bool value to determine whether the updater will backup

#LANGUAGE PACK VARIABLES #####
Var LAMS_DIR            ; directory lams is installed at
Var VERSION_INT         ; version of the language pack
Var OLD_LANG_VERSION         ; previous version of language pack



# installer sections
SectionGroup "LAMS ${VERSION} Patch (Requires LAMS 2.0)" update
    
    Section "!LAMS ${VERSION}" LAMSPATCH
        Detailprint "Installing LAMS ${VERSION} patch"
        
        ; Backing up existing lams installation
        call backupLams
       
        ; removing temporary jboss files
        clearerrors
        Detailprint "Removing $INSTDIR\jboss-4.0.2\server\default\tmp "
        rmdir /r "$INSTDIR\jboss-4.0.2\server\default\tmp"
        Detailprint "Removing $INSTDIR\jboss-4.0.2\server\default\work\jboss.web\localhost"
        rmdir /r "$INSTDIR\jboss-4.0.2\server\default\work\jboss.web\localhost"
        iferrors error continue
        error:
            MessageBox MB_OKCANCEL|MB_ICONQUESTION "Could not remove all of LAMS temporary files. Check all other programs are closed and LAMS is not running $\r$\n$\r$\n If you have just shutdown LAMS then LAMS may still be shutting down - please cancel the installation and then wait for a minute and try to run the installer again. If this problem reoccurs, please delete the following directories manually:$\r$\n$INSTDIR\jboss-4.0.2\server\default\work\jboss.web\localhost $\r$\n$INSTDIR\jboss-4.0.2\server\default\tmp $\r$\n$\r$\nClick ok to continue or cancel to stop installation" IDOK continue IDCANCEL cancel
            cancel:
            Abort
        continue: 
        
        ; setting up ant
        call setupant
        
        ; Updating the the core lams jars/wars
        call updateJarsWars
        
        setoutpath $INSTDIR
        File /a "${DOCUMENTS}\license.txt"
        File /a "${DOCUMENTS}\license-wrapper.txt"
        File /a "${DOCUMENTS}\readme.txt"
     
        ; Update the registry
        call WriteRegEntries
    SectionEnd
   

SectionGroupEnd

Function .onInit
    
    # Checking to see if LAMS is installed 
    call checkRegistry
    
    
    # check if LAMS is stopped
    SetOutPath $TEMP
    File "${BUILD_DIR}\LocalPortScanner.class"
    ReadRegStr $0 HKLM "${REG_HEAD}" "lams_port"
    ReadRegStr $1 HKLM "${REG_HEAD}" "dir_jdk"
    Goto checklams
    
    checklams:
        nsExec::ExecToStack "$1\bin\java LocalPortScanner $0"
        Pop $2
        ${If} $2 == 2
            MessageBox MB_YESNO|MB_ICONQUESTION "LAMS appears to be running.  Stop LAMS and continue with update? (Will take a few seconds)" \
                IDYES stoplams \
                IDNO quit
        ${EndIf}
        Goto continue
    stoplams:
        nsExec::ExecToStack 'sc stop LAMSv2'
        Pop $0
        Pop $1
        DetailPrint "Sent stop command to LAMS service."
        # sleep for 10s to ensure that JBoss closes properly
        sleep 10000
        Goto checklams
    quit:
        Delete "$TEMP\LocalPortScanner.class"
        MessageBox MB_OK|MB_ICONSTOP "LAMS ${VERSION} update cannot continue while LAMS is running."
        Abort
    continue:
        Delete "$TEMP\LocalPortScanner.class"
        
    # Reading the registry values
    Detailprint "Reading existing LAMS data from registry"
    call readRegistry
        
    
    # extract custom page display config
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams-update.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "final.ini"
    
    # set jsmath exploded size (assumes 4KB cluster size on destination hdd)
    ;SectionSetSize ${jsmathe} 81816
    
FunctionEnd


################################################################################
# USER INTERFACE CODE                                                          #
################################################################################

# Skips the directory page if this is an update
Function skipPage
    Abort
FunctionEnd

Function checkRegistry
    # Check the current version installed (if any)
    ReadRegStr $0 HKLM "${REG_HEAD}" "version" 
    
    ${if} $0 == ""
        MessageBox MB_OK|MB_ICONSTOP "No installation of LAMS found."
        Abort
    ${else}
        # LAMS is installed. Check if the current version is installed
        ${VersionCompare} $0 ${VERSION} $1
        ${if} $1 == 0
        ${orif} $1 == 1
            MessageBox MB_OK|MB_ICONSTOP "You already have LAMS $0 Installed on your computer."
            Abort
        ${elseif} $0 == ${PREVIOUS_VERSION}
            # This is the correct version to update to
        ${else}
            MessageBox MB_OK|MB_ICONSTOP "Your existing version of LAMS ($0) is not compatible with this update. $\r$\n$\r$\nPlease update to LAMS-${PREVIOUS_VERSION} before running this update."
            Abort 
        ${endif}
    ${endif}
FunctionEnd


Function CheckJava
    # check for JDK
    ${If} $JDK_DIR == ""
        ReadRegStr $JDK_DIR HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.6" "JavaHome"
        ${If} $JDK_DIR == ""
            ReadRegStr $JDK_DIR HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.5" "JavaHome"
            ${if} $JDK_DIR == ""
                MessageBox MB_OK|MB_ICONSTOP "Could not find a Java JDK 1.5 or 1.6 installation.  Please enter where you have java 5 or 6 installed."
            ${EndIf}
        ${EndIf}
    ${endif}

FunctionEnd

Function Checkjava2    
    # check java version using given dir
    nsExec::ExecToStack '$JDK_DIR\bin\javac.exe -version'
    Pop $0
    Pop $1
    ${StrStr} $0 $1 "1.6"
    ${If} $0 == ""
        ${StrStr} $0 $1 "1.5"
        ${If} $0 == ""
            MessageBox MB_OK|MB_ICONEXCLAMATION "Could not find a Java 5 or Java 6 installation in the given directory. $\r$\nPlease check your Java installation and try again.$\r$\n$\r$\n$JDK_DIR"
            Abort
        ${EndIf}
    ${EndIf}
FunctionEnd


Function CheckMySQL  
        # check mysql version is 5.0.x
        Setoutpath "$TEMP\lams\"
        
        #StrLen $9 $MYSQL_ROOT_PASS
        
        
        Strcpy $0 '$JDK_DIR\bin\java.exe -cp ".;$TEMP\lams\mysql-connector-java-5.0.8-bin.jar" checkmysqlversion "jdbc:mysql://$MYSQL_HOST/$DB_NAME?characterEncoding=utf8" "$DB_USER" "$DB_PASS"'
       
        
        File "${BUILD_DIR}\checkmysqlversion.class"
        File "${LIB}\mysql-connector-java-5.0.8-bin.jar"
        nsExec::ExecToStack $0
        Pop $0
        Pop $1
        ${If} $0 != 0
            ${StrStr} $3 $1 "UnknownHostException"
            ${if} $3 == "" 
                MessageBox MB_OK|MB_ICONEXCLAMATION "An error occurred whilst checking your mysql configuration $\r$\n$\r$\nError: $1"
            ${else}
                MessageBox MB_OK|MB_ICONEXCLAMATION "An error occurred whilst checking your mysql configuration $\r$\n$\r$\nError: Could not connect to MySql host: $MYSQL_HOST. Please check your database configurations and try again."
            ${endif}               
            Abort
        ${EndIf}
        Delete "$TEMP\lams\checkmysql.class"
        Delete "$TEMP\mysql-connector-java-5.0.8-bin.jar"  
FunctionEnd

Function PreComponents
FunctionEnd 

Function PostComponents

FunctionEnd

Function DirectoryLeave
FunctionEnd


Function PreMySQLConfig
   
FunctionEnd

Function PostMySQLConfig 
    call CheckMySQL 
FunctionEnd


Function PreLAMSConfig
    Call CheckJava
   
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams-update.ini" "Field 2" "State" "$JDK_DIR"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams-update.ini" "Field 6" "State" "$MYSQL_HOST"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams-update.ini" "Field 7" "State" "$MYSQL_PORT"
    !insertmacro MUI_HEADER_TEXT "Java setup" "If you have changed your java installation since installing LAMS ${PREVIOUS_VERSION}, please enter the new details."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams-update.ini"
FunctionEnd


Function PostLAMSConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $MYSQL_HOST "lams-update.ini" "Field 6" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $MYSQL_PORT "lams-update.ini" "Field 7" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $JDK_DIR "lams-update.ini" "Field 2" "State"
    #!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_REPOSITORY "lams-update.ini" "Field 4" "State"
    
    # check java version using given dir
    Call Checkjava2
    
    Setoutpath "$TEMP\lams\"
    File "${BUILD_DIR}\checkmysql.class"
    File "${LIB}\mysql-connector-java-5.0.8-bin.jar"
    nsExec::ExecToStack '$JDK_DIR\bin\java.exe -cp ".;$TEMP\lams\mysql-connector-java-5.0.8-bin.jar" checkmysql "jdbc:mysql://$MYSQL_HOST/$DB_NAME?characterEncoding=utf8" $DB_USER $DB_PASS ${PREVIOUS_VERSION}'
    Pop $0
    Pop $1
    ${If} $0 != 0
        ${StrStr} $3 $1 "UnknownHostException"
        
        ${if} $3 == "" 
            MessageBox MB_OK|MB_ICONEXCLAMATION "An error occurred whilst checking your mysql configuration $\r$\n$\r$\nError: $1"
        ${else}
            MessageBox MB_OK|MB_ICONEXCLAMATION "An error occurred whilst checking your mysql configuration $\r$\n$\r$\nError: Could not connect to MySql host: $MYSQL_HOST. Please check your database configurations and try again."
        ${endif}               
        Abort
    ${EndIf}
    
    Delete "$TEMP\lams\checkmysql.class"
    Delete "$TEMP\mysql-connector-java-5.0.8-bin.jar"
FunctionEnd


Function PreLAMS2Config
FunctionEnd


Function PostLAMS2Config
FunctionEnd


Function PreWildfireConfig
FunctionEnd


Function PostWildfireConfig
FunctionEnd

Function PreFinal
    !insertmacro MUI_INSTALLOPTIONS_WRITE "final.ini" "Field 2" "Text" "Click 'Install' to commence update to LAMS ${VERSION}" 
    !insertmacro MUI_HEADER_TEXT "LAMS ${VERSION}" "Configuration Completed"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "final.ini"
FunctionEnd

Function PostFinal

    Call GetLocalTime
    Pop "$0" ;Variable (for day)
    Pop "$1" ;Variable (for month)
    Pop "$2" ;Variable (for year)
    Pop "$3" ;Variable (for day of week name)
    Pop "$4" ;Variable (for hour)
    Pop "$5" ;Variable (for minute)
    Pop "$6" ;Variable (for second)
    
    strlen $7 $0
    ${if} $7 == 1
        strcpy $0 "0$0"
    ${endif}
    
    strlen $7 $1
    ${if} $7 == 1
        strcpy $1 "0$1"
    ${endif}
    
    strlen $7 $4
    ${if} $7 == 1
        strcpy $4 "0$4"
    ${endif}
    
    strlen $7 $5
    ${if} $7 == 1
        strcpy $1 "0$5"
    ${endif}
    
    
    strcpy $TIMESTAMP "$2$1$0$4$5"

    strcpy $BACKUP "0"
    
    MessageBox MB_YESNOCANCEL|MB_ICONQUESTION "Do you wish to backup your LAMS installation? (Recommended) $\r$\nBackup dir: $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak $\r$\n$\r$\nNOTE: You must have MySql installed on this machine to do this." IDNO continue IDCANCEL cancel

    call CheckMySQL

    strcpy $BACKUP "1"
    goto continue
    cancel:
        Abort
    continue:
FunctionEnd

################################################################################
# END USER INTERFACE CODE                                                      #
################################################################################


Function readRegistry
    ReadRegStr $INSTDIR HKLM "${REG_HEAD}" "dir_inst"
    ReadRegStr $DB_NAME HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $DB_PASS HKLM "${REG_HEAD}" "db_pass"
    ReadRegStr $DB_USER HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $JDK_DIR HKLM "${REG_HEAD}" "dir_jdk"
    ReadRegStr $MYSQL_DIR HKLM "${REG_HEAD}" "dir_mysql"
    ReadRegStr $LAMS_REPOSITORY HKLM "${REG_HEAD}" "dir_repository"
    ReadRegStr $LAMS_DOMAIN HKLM "${REG_HEAD}" "lams_domain"
    ReadRegStr $LAMS_LOCALE HKLM "${REG_HEAD}" "lams_locale"
    ReadRegStr $LAMS_PORT HKLM "${REG_HEAD}" "lams_port"
    #ReadRegStr $VERSION HKLM "${REG_HEAD}" "version"
    ReadRegStr $WILDFIRE_DOMAIN HKLM "${REG_HEAD}" "wildfire_domain"
    ReadRegStr $WILDFIRE_PASS HKLM "${REG_HEAD}" "wildfire_pass"
    ReadRegStr $WILDFIRE_USER HKLM "${REG_HEAD}" "wildfire_user"
    ReadRegStr $OLD_LANG_VERSION HKLM "${REG_HEAD}" "language_pack"
    ; Getting the mysql_ip from the registry (2.0.3)
    ReadRegStr $MYSQL_HOST HKLM "${REG_HEAD}" "mysql_host"
    ${if} $MYSQL_HOST == ""
        strcpy $MYSQL_HOST "localhost"
    ${endif}
    
    ReadRegStr $MYSQL_PORT HKLM "${REG_HEAD}" "mysql_port"
    ;strcpy $MYSQL_PORT 3306
    
FunctionEnd



Function setupant
    
    # extract support files to write configuration
    SetOutPath $INSTDIR
    File /r "${BASE_DIR}\apache-ant-1.6.5"
    
    # use Ant to write config to files
    clearerrors
    FileOpen $0 "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" w
    IfErrors 0 +2
        goto error
    FileWrite $0 "@echo off$\r$\nset JAVACMD=$JDK_DIR\bin\java$\r$\n"
    FileClose $0
    ${FileJoin} "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" "$INSTDIR\apache-ant-1.6.5\bin\ant.bat" ""

    goto done
    error:
        DetailPrint "Error setting up ant"
        MessageBox MB_OK|MB_ICONSTOP "Error setting up ant "
        Abort "Lams configuration failed"
    done:
Functionend


; Backs up existing lams installation
Function backupLams
    ${if} $BACKUP == "1"
        ${if} $MYSQL_HOST == "localhost"
            clearerrors
            iffileexists "$INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak\*.*" backupExists continue
            backupExists:
                DetailPrint "Lams backup failed"
                MessageBox MB_OK|MB_ICONSTOP "Lams backup failed, please delete or change the name of the backup file before continuing with the update$\r$\n$INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak"
                Abort "LAMS configuration failed"
            continue:
            
            DetailPrint "Backing up lams at: $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak. This may take a few minutes"
            SetDetailsPrint listonly
            copyfiles /SILENT $INSTDIR  $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak 86000
            SetDetailsPrint both
            iferrors error1 continue1
            error1:
                DetailPrint "Backup failed"
                MessageBox MB_OK|MB_ICONSTOP "LAMS backup to $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak failed. Check that all other applications are closed and LAMS is not running." 
                Abort
            continue1: 
            
            DetailPrint 'Dumping database to: $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak'
            setoutpath "$INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak"
            Strcpy $4 '"$MYSQL_DIR\bin\mysqldump" -r "$INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak\dump.sql" $DB_NAME -u $DB_USER -p$DB_PASS'
            DetailPrint $4
            nsExec::ExecToStack $4
            Pop $0
            Pop $1
            ${If} $0 == "yes"
                goto error
            ${EndIf}
              
            goto done
            error:
                DetailPrint "Database dump failed"
                MessageBox MB_OK|MB_ICONSTOP "Database dump failed $\r$\nError:$\r$\n$\r$\n$1"
                Abort "Database dump failed"
            done:
        ${else}
            MessageBox MB_YESNO|MB_ICONQUESTION "Unable to backup LAMS. MYSQL_HOST is not set to localhost. Manual backup required. $\r$\n$\r$\nWould you like to quit the update and backup LAMS manually? If you choose 'No', the update will proceed and your existing LAMS installation will be overwritten." IDYES quit IDNO continue2
                quit:
                    Abort
                continue2:
        ${endif}
    ${endif}
FunctionEnd


; Updating the the core lams jars/wars
Function updateJarsWars
    SetoutPath "$INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear"
    File /r /x CVS ${ASSEMBLY}\*
    
    SetoutPath "$INSTDIR\jboss-4.0.2\server\default\deploy\jbossweb-tomcat55.sar\ROOT.war"
    File ${DOCUMENTS}\index.jsp
FunctionEnd


Function WriteRegEntries
    WriteRegStr HKLM "${REG_HEAD}" "dir_jdk" $JDK_DIR
    WriteRegStr HKLM "${REG_HEAD}" "mysql_host" "$MYSQL_HOST"
    WriteRegStr HKLM "${REG_HEAD}" "mysql_port" "$MYSQL_PORT"
    WriteRegStr HKLM "${REG_HEAD}" "version" "${VERSION}"
    WriteRegStr HKLM "${REG_HEAD}" "language_pack" "${VERSION}"
FunctionEnd

