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
 *
 * Author: Luke Foxton and Daniel Carlier
 */
/*
We need to do an windows updater that
* Does a backup
* Checks if the db is wrong and if it is corrects the system tool table - tick
* Updates the 5 rows in the lams_configuration table - tick
* Updates the registry? The version is in there isn't it? - tick
* Updates the news.html file.
*/

# includes
!include "MUI.nsh"
!include "LogicLib.nsh"


# constants
!define VERSION "2.0.4"
!define PREVIOUS_VERSION "2.0.3"
!define LANGUAGE_PACK_VERSION "2007-06-01"
!define LANGUAGE_PACK_VERSION_INT "20070601"
!define DATE_TIME_STAMP "200706211047"
!define SERVER_VERSION_NUMBER "${VERSION}.${DATE_TIME_STAMP}"
!define BASE_VERSION "2.0"
!define SOURCE_JBOSS_HOME "D:\jboss-4.0.2"  ; location of jboss where lams was deployed
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"

# installer settings
!define MUI_ICON "..\graphics\lams2.ico"
!define MUI_UNICON "..\graphics\lams2.ico"
Name "LAMS ${VERSION}"
;BrandingText "LAMS ${VERSION} -- built on ${__TIMESTAMP__}"
BrandingText "LAMS ${PREVIOUS_VERSION} - ${VERSION} Updater"
OutFile "..\build\LAMS-${VERSION}.exe"
InstallDir "C:\lams"
InstallDirRegKey HKLM "${REG_HEAD}" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 
InstProgressFlags smooth

# set warning when cancelling install
!define MUI_ABORTWARNING

# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Update Wizard"
!define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the update from LAMS ${PREVIOUS_VERSION} - ${VERSION}.\r\n\r\n\
    Please ensure you have a copy of MySQL 5.x installed and running, and Java JDK version 1.5.x. or 1.6.x\r\n\r\n\
    NOTE: This updater works strictly for updates from LAMS 2.0.3 to LAMS 2.0.4. It is not a installer.\r\n\r\n\
    Click Next to continue."


# display finish page stuff
!define MUI_FINISHPAGE_RUN $INSTDIR\lams-start.exe
!define MUI_FINISHPAGE_RUN_TEXT "Start LAMS now"
!define MUI_FINISHPAGE_TEXT "The LAMS Server has been successfully updated to ${VERSION} on your computer."
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\readme.txt
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Open the readme file"
!define MUI_FINISHPAGE_LINK "Visit LAMS Community"
!define MUI_FINISHPAGE_LINK_LOCATION "http://www.lamscommunity.org"

# installer screen progression
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "..\license.txt"
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH 

# supported translations
!insertmacro MUI_LANGUAGE "English"


# variables
Var MYSQL_DIR           ; path to user's mysql directory
Var MYSQL_HOST          ; ip address for mysql
Var MYSQL_URL
Var DB_NAME             ; db name for lams
Var DB_USER             ; db user for lams
Var DB_PASS             ; db pass for lams
Var JDK_DIR             ; path to user's JDK directory  
Var LAMS_PORT           ; PORT for lams usually 8080
Var LAMS_LOCALE         ; default language locale on startup
Var LAMS_REPOSITORY     ; path to repository on user's box
Var WILDFIRE_DOMAIN     ; wildfire URL
Var WILDFIRE_USER       ; wildfire username
Var WILDFIRE_PASS
Var LAMS_DOMAIN
Var TIMESTAMP
Var BACKUP_DIR

Section updatelams
    MessageBox MB_YESNO|MB_ICONQUESTION "Do you wish to backup your LAMS installation? (Recommended) $\r$\nBackup dir: $BACKUP_DIR $\r$\n$\r$\nNOTE: You must have MySql installed on this machine to do this." IDNO continue IDYES backup
    backup:
        MessageBox MB_OK "LAMS ${PREVIOUS_VERSION} will be backed up at $BACKUP_DIR"
        call backupLams
    continue:
    
    ; Check the db an run the update script if neccessary
    call runUpdateScript

    ; Copying the news.html file
    SetoutPath "$INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war\"
    File "..\..\lams_www\conf\lams\news.html"
    
    ; Replace readme
    SetoutPath "$INSTDIR"
    File "..\readme.txt"
    File "..\license.txt"
    
    ; Update the registry with server version and version
    WriteRegStr HKLM "${REG_HEAD}" "version" "${VERSION}"
    WriteRegStr HKLM "${REG_HEAD}" "server_version" "${SERVER_VERSION_NUMBER}"
Sectionend

Function .onInit
        
    # Checking to see if LAMS is installed 
    call checkRegistry
    
    # check if LAMS is stopped
    SetOutPath $TEMP
    File "..\build\LocalPortScanner.class"
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
        
    call generateBackupDir
    
    # Reading the registry values
    Detailprint "Reading existing LAMS data from registry"
    call readRegistry
    
FunctionEnd

Function generateBackupDir
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
    strcpy $BACKUP_DIR $INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak
FunctionEnd

Function checkRegistry
    # Check the current version installed (if any)
    ReadRegStr $0 HKLM "${REG_HEAD}" "version" 
    
    ${if} $0 == ""
        MessageBox MB_OK|MB_ICONSTOP "LAMS ${PREVIOUS_VERSION} is not installed. You need to have LAMS ${PREVIOUS_VERSION} installed to run this update."   
        Abort
    ${elseif} $0 == "2.0.4"
        MessageBox MB_OK "LAMS ${VERSION} is already up to date."   
        Abort 
    ${elseif} $0 != "2.0.3"
        MessageBox MB_OK|MB_ICONSTOP "Your version of LAMS ($0) is not compatible with this update. $\r$\n$\r$\nThis update requires LAMS ${PREVIOUS_VERSION}."   
        Abort 
    ${endif}
FunctionEnd

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
    ReadRegStr $MYSQL_HOST HKLM "${REG_HEAD}" "mysql_host"
FunctionEnd

Function runUpdateScript    
    # check for MySQL   
    ${if} $MYSQL_HOST == ""
        strcpy $MYSQL_HOST "localhost"
        strcpy $MYSQL_URL "jdbc:mysql://$MYSQL_HOST/$DB_NAME?characterEncoding=utf8"
    ${endif}
       
    # check if LAMS is stopped
    SetOutPath $TEMP\lams
    File "..\build\Update204.class"
    File "..\mysql-connector-java-3.1.12-bin.jar"
    
    
    # Connect through a mysql URl using JDBC
    strcpy $MYSQL_URL "jdbc:mysql://$MYSQL_HOST/$DB_NAME?characterEncoding=utf8"
    nsExec::ExecToStack '$JDK_DIR\bin\java -cp ".;$TEMP\lams\mysql-connector-java-3.1.12-bin.jar;" Update204 $MYSQL_URL $DB_USER $DB_PASS ${VERSION} ${SERVER_VERSION_NUMBER}'
    pop $0
    pop $1
    ${if} $0 != '0'
        MessageBox MB_OK|MB_ICONSTOP "Problem updating database using: $\r$\n$\r$\nMySql url: $MYSQL_URL.$\r$\nDatabase name: $DB_NAME.$\r$\nDatabase password: $DB_PASS. $\r$\n$\r$\n$1" 
        Abort
    ${endif}
FunctionEnd

Function .onInstFailed
    rmdir "$TEMP\lams"
FunctionEnd

Function .onInstSuccess
    rmdir "$TEMP\lams"
FunctionEnd

; Backs up existing lams installation
Function backupLams
    clearerrors
    iffileexists "$BACKUP_DIR\*.*" backupExists continue
    backupExists:
        DetailPrint "Lams backup failed"
        MessageBox MB_OK|MB_ICONSTOP "Lams backup failed, please delete or change the name of the backup file before continuing with the update$\r$\n$INSTDIR-${PREVIOUS_VERSION}-$TIMESTAMP.bak"
        Abort "LAMS configuration failed"
    continue:
    
    DetailPrint "Backing up lams at: $BACKUP_DIR. This may take a few minutes"
    SetDetailsPrint listonly
    copyfiles /SILENT $INSTDIR  $BACKUP_DIR 86000
    SetDetailsPrint both
    iferrors error1 continue1
    error1:
        DetailPrint "Backup failed"
        MessageBox MB_OK|MB_ICONSTOP "LAMS backup to $BACKUP_DIR failed. Check that all other applications are closed and LAMS is not running." 
        Abort
    continue1: 
    
    DetailPrint 'Dumping database to: $BACKUP_DIR'
    setoutpath "$BACKUP_DIR"
    Strcpy $4 '"$MYSQL_DIR\bin\mysqldump" -r "$BACKUP_DIR\dump.sql" $DB_NAME -u $DB_USER -p$DB_PASS'
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
    
FunctionEnd

;----------------------------------------------------------------------------
; Superseded by     : GetTime function.
;----------------------------------------------------------------------------
; Title             : Get Local Time
; Short Name        : GetLocalTime
; Last Changed      : 22/Feb/2005
; Code Type         : Function
; Code Sub-Type     : One-way Output
;----------------------------------------------------------------------------
; Required          : System plugin.
; Description       : Gets the current local time of the user's computer
;----------------------------------------------------------------------------
; Function Call     : Call GetLocalTime
;
;                     Pop "$Variable1"
;                       Day.
;
;                     Pop "$Variable2"
;                       Month.
;
;                     Pop "$Variable3"
;                       Year.
;
;                     Pop "$Variable4"
;                       Day of the week name.
;
;                     Pop "$Variable5"
;                       Hour.
;
;                     Pop "$Variable6"
;                       Minute.
;
;                     Pop "$Variable7"
;                       Second.
;----------------------------------------------------------------------------
; Author            : Diego Pedroso
; Author Reg. Name  : deguix
;----------------------------------------------------------------------------
 
Function GetLocalTime
 
  # Prepare variables
  Push $0
  Push $1
  Push $2
  Push $3
  Push $4
  Push $5
  Push $6
 
  # Call GetLocalTime API from Kernel32.dll
  System::Call '*(&i2, &i2, &i2, &i2, &i2, &i2, &i2, &i2) i .r0'
  System::Call 'kernel32::GetLocalTime(i) i(r0)'
  System::Call '*$0(&i2, &i2, &i2, &i2, &i2, &i2, &i2, &i2)i \
  (.r4, .r5, .r3, .r6, .r2, .r1, .r0,)'
 
  # Day of week: convert to name
  StrCmp $3 0 0 +3
    StrCpy $3 Sunday
      Goto WeekNameEnd
  StrCmp $3 1 0 +3
    StrCpy $3 Monday
      Goto WeekNameEnd
  StrCmp $3 2 0 +3
    StrCpy $3 Tuesday
      Goto WeekNameEnd
  StrCmp $3 3 0 +3
    StrCpy $3 Wednesday
      Goto WeekNameEnd
  StrCmp $3 4 0 +3
    StrCpy $3 Thursday
      Goto WeekNameEnd
  StrCmp $3 5 0 +3
    StrCpy $3 Friday
      Goto WeekNameEnd
  StrCmp $3 6 0 +2
    StrCpy $3 Saturday
  WeekNameEnd:
 
  # Minute: convert to 2 digits format
    IntCmp $1 9 0 0 +2
      StrCpy $1 '0$1'
 
  # Second: convert to 2 digits format
    IntCmp $0 9 0 0 +2
      StrCpy $0 '0$0'
 
  # Return to user
  Exch $6
  Exch
  Exch $5
  Exch
  Exch 2
  Exch $4
  Exch 2
  Exch 3
  Exch $3
  Exch 3
  Exch 4
  Exch $2
  Exch 4
  Exch 5
  Exch $1
  Exch 5
  Exch 6
  Exch $0
  Exch 6
 
FunctionEnd




