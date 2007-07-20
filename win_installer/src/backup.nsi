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
 * Coded by Luke Foxton (lfoxton@melcoe.mq.edu.au)
 */

# includes
!include "MUI.nsh"
!include "LogicLib.nsh"
!include "backup.nsh"

#defines
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
!define MUI_ICON "..\graphics\lams2.ico"

# Installer attributes
Name "LAMS Backup Utility"
BrandingText "LAMS Backup Utility"
OutFile "..\build\lams-backup.exe"
InstallDir "C:\lams"
Icon ..\graphics\lams2.ico
InstallDirRegKey HKLM "${REG_HEAD}" ""
VIProductVersion 2.0.0.0
VIAddVersionKey ProductName "LAMS Backup Utility"
VIAddVersionKey ProductVersion "1.0"
VIAddVersionKey CompanyName "LAMS International"
VIAddVersionKey CompanyWebsite "lamscommunity.org"
VIAddVersionKey FileVersion ""
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright ""



# set warning when cancelling install
!define MUI_ABORTWARNING
Page custom PreBackupDir PostBackupDir
!define MUI_INSTFILESPAGE_FINISHHEADER_TEXT "LAMS backup complete"
!define MUI_INSTFILESPAGE_FINISHHEADER_SUBTEXT ""
!insertmacro MUI_PAGE_INSTFILES
Page custom PreFinish PostFinish

;!define MUI_FINISHPAGE_TEXT "LAMS has been successfully backed up on your computer" 
;!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_LANGUAGE "English"



# variables
Var CURR_VERSION
Var DB_NAME
Var DB_USER
Var DB_PASS
Var MYSQL_DIR
Var BACKUP_DIR


ReserveFile "backup.ini"
ReserveFile "backup-finish.ini"


Section backup
    ;make the section compulsory
    SectionIn RO
    setoutpath $BACKUP_DIR
    file "..\backup.txt"
    clearerrors
    DetailPrint "Backing up LAMS at: $BACKUP_DIR. This may take a few minutes"
    SetDetailsPrint listonly
    createdirectory $BACKUP_DIR
    copyfiles /SILENT "$INSTDIR\*" $BACKUP_DIR
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
    
SectionEnd

Function .onInit
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
            MessageBox MB_YESNOCANCEL|MB_ICONQUESTION "LAMS appears to be running. Do you wish to continue with LAMS running? $\r$\n$\r$\nClick yes to continue or no to stop LAMS (will take a few seconds)" \
                IDNO stoplams \
                IDCANCEL quitbackup
        ${EndIf}
        goto continue
    stoplams:
        nsExec::ExecToStack 'sc stop LAMSv2'
        Pop $0
        Pop $1
        DetailPrint "Sent stop command to LAMS service."
        # sleep for 10s to ensure that JBoss closes properly
        sleep 10000
        Goto checklams
    quitbackup:
        Delete "$TEMP\LocalPortScanner.class"
        Abort
    continue:
        Delete "$TEMP\LocalPortScanner.class"
    
    
    
    
    
    
    
    
    
    
    ReadRegStr $INSTDIR HKLM "${REG_HEAD}" "dir_inst"
    
    ${if} $INSTDIR == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "You do not have LAMS 2 installed"
        Abort
    ${endif}
    
    ReadRegStr $CURR_VERSION HKLM "${REG_HEAD}" "version"
    ReadRegStr $DB_NAME HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $DB_USER HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $DB_PASS HKLM "${REG_HEAD}" "db_pass"
    ReadRegStr $MYSQL_DIR HKLM "${REG_HEAD}" "dir_mysql"
    call CheckMySQL
    
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
    
    
    strcpy $BACKUP_DIR "$INSTDIR-$CURR_VERSION-$2$1$0$4$5.bak"
   
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "backup.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "backup-finish.ini"
FunctionEnd

Function PreBackupDir
    !insertmacro MUI_INSTALLOPTIONS_WRITE "backup.ini" "Field 1" "State" "$BACKUP_DIR"
    !insertmacro MUI_HEADER_TEXT "LAMS Backup Utility" "Enter a directory in the space provided to back up your LAMS installation"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "backup.ini"
FunctionEnd

Function PostBackupDir
    !insertmacro MUI_INSTALLOPTIONS_READ $BACKUP_DIR "backup.ini" "Field 1" "State"
    ${if} $BACKUP_DIR == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "The directory entered is not valid: $\r$\n$BACKUP_DIR"
        Abort
    ${endif}
    
    iffileexists $BACKUP_DIR exists continue
    iffileexists "$BACKUP_DIR\*" exists continue
    
    goto continue
    exists:
          MessageBox MB_OK|MB_ICONEXCLAMATION "The file name for the directory entered is already in use: $\r$\n$BACKUP_DIR"
    continue:
FunctionEnd

Function PreFinish
    !insertmacro MUI_INSTALLOPTIONS_WRITE "backup-finish.ini" "Field 1" "Text" "Congratulations! \r\n\r\nLAMS has backed successfully backed up to: $BACKUP_DIR"
    !insertmacro MUI_HEADER_TEXT "LAMS Backup Utility" "Your LAMS backup is complete"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "backup-finish.ini"
FunctionEnd

Function PostFinish
    ; DO NOTHING!
FunctionEnd

Function CheckMySQL    
    # Checking if the given database name already exists in the mysql database list
    nsExec::ExecToStack '$MYSQL_DIR\bin\mysql -u$DB_USER -p$DB_PASS $DB_NAME -e "SELECT * FROM lams_configuration"'
    Pop $0
    Pop $1
    ${If} $0 != 0 ; if mySQL install directory field is empty, do not continue
        MessageBox MB_OK|MB_ICONSTOP "Could not find database $DB_NAME. Please check your database settings and try again"
        Abort
    ${EndIf}
    
    
    #ifFileExists "$MYSQL_DIR\data\$DB_NAME\*.*" continue NoDatabaseNameExists
    #NoDatabaseNameExists:
    #    MessageBox MB_OK|MB_ICONSTOP "Could not find database $DB_NAME. Please check your database settings and try again"
    #    quit   
    #continue:
    
    # check mysql version is 5.0.x
    nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin --version'
    Pop $0
    Pop $1
    ${If} $1 == "" ; if mySQL install directory field is empty, do not continue
        MessageBox MB_OK|MB_ICONSTOP "Your MySQL directory does not appear to be valid, please enter a valid MySQL directory before continuing.$\r$\n$\r$\n$1"
        Abort
    ${EndIf}
    
    ${StrStr} $0 $1 "5.0"
    
    ${If} $0 == "" ; if not 5.0.x, check 5.1.x
        ${StrStr} $0 $1 "5.1"
        ${If} $0 == ""
            MessageBox MB_OK|MB_ICONSTOP "Your MySQL version does not appear to be compatible with LAMS (5.0.x or 5.1.x): $\r$\n$\r$\n$1"
            MessageBox MB_OK|MB_ICONSTOP "Your MySQL directory does not appear to be valid, please enter a valid MySQL directory before continuing.$\r$\n$\r$\n$1"
            Abort
        ${endif}
    ${EndIf}
FunctionEnd


