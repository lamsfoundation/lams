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

/*
 * TODO: uninstaller option to keep database/lams.xml/repository
 *
 *
 */

# includes
!include "MUI.nsh"
!include "LogicLib.nsh"

# constants
!define VERSION "2.0 RC1"
!define SOURCE_JBOSS_HOME "D:\jboss-4.0.2"  ; location of jboss and lamsconf where lams was built
!define SOURCE_LAMS_CONF "C:\lamsconf"
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"

# installer variables
!define MUI_ICON "..\graphics\lams2.ico"
!define MUI_UNICON "..\graphics\lams2.ico"
Name "LAMS ${VERSION}"
;BrandingText "LAMS Foundation"
OutFile "..\build\LAMS-${VERSION}.exe"
InstallDir "C:\lams"
InstallDirRegKey HKLM "${REG_HEAD}" ""

# set installer header image
;!define MUI_HEADERIMAGE
;!define MUI_HEADERIMAGE_BITMAP "..\graphics\header.bmp"
;!define MUI_HEADERIMAGE_RIGHT ; will switch to left side when using a RTL language

# set warning when cancelling install
!define MUI_ABORTWARNING

# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Installer"

# set components page type
;!define MUI_COMPONENTSPAGE_NODESC
!define MUI_COMPONENTSPAGE_SMALLDESC

# set instfiles page to wait when done
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# installer screen progression
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "..\license.txt"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
Page custom PreMySQLConfig PostMySQLConfig
Page custom PreLAMSConfig PostLAMSConfig
Page custom PreWildfireConfig PostWildfireConfig
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

# uninstaller screens
!insertmacro MUI_UNPAGE_WELCOME
UninstPage custom un.PreUninstall un.PostUninstall
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH


# supported translations
  !insertmacro MUI_LANGUAGE "English" # first language is the default language
  !insertmacro MUI_LANGUAGE "French"
  !insertmacro MUI_LANGUAGE "German"
  !insertmacro MUI_LANGUAGE "Spanish"
  !insertmacro MUI_LANGUAGE "SimpChinese"
  !insertmacro MUI_LANGUAGE "Korean"
  !insertmacro MUI_LANGUAGE "Italian"
  !insertmacro MUI_LANGUAGE "Dutch"
  !insertmacro MUI_LANGUAGE "Danish"
  !insertmacro MUI_LANGUAGE "Norwegian"
  !insertmacro MUI_LANGUAGE "NorwegianNynorsk"  
  !insertmacro MUI_LANGUAGE "Portuguese"
  !insertmacro MUI_LANGUAGE "PortugueseBR"
  !insertmacro MUI_LANGUAGE "Greek"
  !insertmacro MUI_LANGUAGE "Russian"
  !insertmacro MUI_LANGUAGE "Polish"
  !insertmacro MUI_LANGUAGE "Bulgarian"  
  !insertmacro MUI_LANGUAGE "Thai"
  !insertmacro MUI_LANGUAGE "Arabic"
/*
  !insertmacro MUI_LANGUAGE "TradChinese"
  !insertmacro MUI_LANGUAGE "Japanese"
  !insertmacro MUI_LANGUAGE "Swedish"
  !insertmacro MUI_LANGUAGE "Finnish"
  !insertmacro MUI_LANGUAGE "Ukrainian"
  !insertmacro MUI_LANGUAGE "Czech"
  !insertmacro MUI_LANGUAGE "Slovak"
  !insertmacro MUI_LANGUAGE "Croatian"
  !insertmacro MUI_LANGUAGE "Hungarian"
  !insertmacro MUI_LANGUAGE "Romanian"
  !insertmacro MUI_LANGUAGE "Latvian"
  !insertmacro MUI_LANGUAGE "Macedonian"
  !insertmacro MUI_LANGUAGE "Estonian"
  !insertmacro MUI_LANGUAGE "Turkish"
  !insertmacro MUI_LANGUAGE "Lithuanian"
  !insertmacro MUI_LANGUAGE "Catalan"
  !insertmacro MUI_LANGUAGE "Slovenian"
  !insertmacro MUI_LANGUAGE "Serbian"
  !insertmacro MUI_LANGUAGE "SerbianLatin"
  !insertmacro MUI_LANGUAGE "Farsi"
  !insertmacro MUI_LANGUAGE "Hebrew"
  !insertmacro MUI_LANGUAGE "Indonesian"
  !insertmacro MUI_LANGUAGE "Mongolian"
  !insertmacro MUI_LANGUAGE "Luxembourgish"
  !insertmacro MUI_LANGUAGE "Albanian"
  !insertmacro MUI_LANGUAGE "Breton"
  !insertmacro MUI_LANGUAGE "Belarusian"
  !insertmacro MUI_LANGUAGE "Icelandic"
  !insertmacro MUI_LANGUAGE "Malay"
  !insertmacro MUI_LANGUAGE "Bosnian"
  !insertmacro MUI_LANGUAGE "Kurdish"
  !insertmacro MUI_LANGUAGE "Irish"
*/


# reserve files
#
ReserveFile "lams.ini"
ReserveFile "mysql.ini"
ReserveFile "wildfire.ini"
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
!insertmacro MUI_RESERVEFILE_LANGDLL


# variables
#
Var MYSQL_DIR
Var MYSQL_ROOT_PASS
Var DB_NAME
Var DB_USER
Var DB_PASS
Var JDK_DIR
Var LAMS_DOMAIN
Var LAMS_PORT
Var LAMS_LOCALE
Var LAMS_REPOSITORY
Var LAMS_CONF
Var WILDFIRE_DOMAIN
Var WILDFIRE_USER
Var WILDFIRE_PASS


# functions
#

# http://nsis.sourceforge.net/Another_String_Replace_%28and_Slash/BackSlash_Converter%29
#
; Push $filenamestring (e.g. 'c:\this\and\that\filename.htm')
; Push "\"
; Call StrSlash
; Pop $R0
; ;Now $R0 contains 'c:/this/and/that/filename.htm'
Function StrSlash
  Exch $R3 ; $R3 = needle ("\" or "/")
  Exch
  Exch $R1 ; $R1 = String to replacement in (haystack)
  Push $R2 ; Replaced haystack
  Push $R4 ; $R4 = not $R3 ("/" or "\")
  Push $R6
  Push $R7 ; Scratch reg
  StrCpy $R2 ""
  StrLen $R6 $R1
  StrCpy $R4 "\"
  StrCmp $R3 "/" loop
  StrCpy $R4 "/"  
loop:
  StrCpy $R7 $R1 1
  StrCpy $R1 $R1 $R6 1
  StrCmp $R7 $R3 found
  StrCpy $R2 "$R2$R7"
  StrCmp $R1 "" done loop
found:
  StrCpy $R2 "$R2$R4"
  StrCmp $R1 "" done loop
done:
  StrCpy $R3 $R2
  Pop $R7
  Pop $R6
  Pop $R4
  Pop $R2
  Pop $R1
  Exch $R3
FunctionEnd


# http://nsis.sourceforge.net/StrStr
#
!define StrStr "!insertmacro StrStr"
 
!macro StrStr ResultVar String SubString
  Push `${String}`
  Push `${SubString}`
  Call StrStr
  Pop `${ResultVar}`
!macroend
 
Function StrStr
/*After this point:
  ------------------------------------------
  $R0 = SubString (input)
  $R1 = String (input)
  $R2 = SubStringLen (temp)
  $R3 = StrLen (temp)
  $R4 = StartCharPos (temp)
  $R5 = TempStr (temp)*/
 
  ;Get input from user
  Exch $R0
  Exch
  Exch $R1
  Push $R2
  Push $R3
  Push $R4
  Push $R5
 
  ;Get "String" and "SubString" length
  StrLen $R2 $R0
  StrLen $R3 $R1
  ;Start "StartCharPos" counter
  StrCpy $R4 0
 
  ;Loop until "SubString" is found or "String" reaches its end
  ${Do}
    ;Remove everything before and after the searched part ("TempStr")
    StrCpy $R5 $R1 $R2 $R4
 
    ;Compare "TempStr" with "SubString"
    ${IfThen} $R5 == $R0 ${|} ${ExitDo} ${|}
    ;If not "SubString", this could be "String"'s end
    ${IfThen} $R4 >= $R3 ${|} ${ExitDo} ${|}
    ;If not, continue the loop
    IntOp $R4 $R4 + 1
  ${Loop}
 
/*After this point:
  ------------------------------------------
  $R0 = ResultVar (output)*/
 
  ;Remove part before "SubString" on "String" (if there has one)
  StrCpy $R0 $R1 `` $R4
 
  ;Return output to user
  Pop $R5
  Pop $R4
  Pop $R3
  Pop $R2
  Pop $R1
  Exch $R0
FunctionEnd


Function .onInit
    # select language
    !insertmacro MUI_LANGDLL_DISPLAY
    
    # extract custom page display config
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "mysql.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "wildfire.ini"
FunctionEnd


Function CheckJava
    # check for JDK
    ReadRegStr $JDK_DIR HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.5" "JavaHome"
    ;MessageBox MB_OK|MB_ICONSTOP "$JDK_DIR"
    ${If} $JDK_DIR == ""
        MessageBox MB_OK|MB_ICONSTOP "Could not find a Java JDK 1.5 installation.  Please ensure you have JDK 1.5 installed."
    ${EndIf}
FunctionEnd


Function CheckMySQL    
    # check for MySQL
    ReadRegStr $MYSQL_DIR HKLM "SOFTWARE\MySQL AB\MySQL Server 5.0" "Location"
    ${If} $MYSQL_DIR == ""
        ReadRegStr $MYSQL_DIR HKLM "SOFTWARE\MySQL AB\MySQL Server 5.1" "Location"
        ${If} $MYSQL_DIR == ""
            MessageBox MB_OK|MB_ICONSTOP "Could not find a MySQL installation.  Please ensure you have MySQL 5.0 or 5.1 installed."
        ${EndIf}
    ${EndIf}
FunctionEnd


Function PreMySQLConfig
    Call CheckMySQL
    !insertmacro MUI_INSTALLOPTIONS_WRITE "mysql.ini" "Field 3" "State" "$MYSQL_DIR"
    !insertmacro MUI_HEADER_TEXT "Setting Up MySQL Database Access" "Choose a MySQL database and user account for LAMS.  If unsure, use the default."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "mysql.ini"
FunctionEnd


Function PostMySQLConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $MYSQL_DIR "mysql.ini" "Field 3" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $MYSQL_ROOT_PASS "mysql.ini" "Field 5" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $DB_NAME "mysql.ini" "Field 7" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $DB_USER "mysql.ini" "Field 9" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $DB_PASS "mysql.ini" "Field 10" "State"
    
    # check mysql version is 5.0.x
    nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin --version'
    Pop $0
    Pop $1
    ${StrStr} $0 $1 "5.0"
    ${If} $0 == "" ; if not 5.0.x, check 5.1.x
        nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin --version'
        Pop $2
        Pop $3
        ${StrStr} $2 $3 "5.1"
        ${If} $2 == ""
            MessageBox MB_OK|MB_ICONSTOP "Your MySQL version does not appear to be compatible with LAMS (5.0.x or 5.1.x): $\r$\n$\r$\n$1"
        ${EndIf}
    ${EndIf}
    
    # check mysql is running
    StrLen $0 $MYSQL_ROOT_PASS
    ${If} $0 == 0
        nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin ping -u root'
    ${Else}
        nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin ping -u root -p$MYSQL_ROOT_PASS'
    ${EndIf}
    Pop $0
    Pop $1
    ${StrStr} $0 $1 "mysqld is alive"
    ${If} $0 == ""
        MessageBox MB_OK|MB_ICONSTOP "MySQL does not appear to be running - please make sure it is running before continuing."
        Abort
    ${EndIf}
FunctionEnd


Function PreLAMSConfig
    Call CheckJava
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams.ini" "Field 2" "State" "$JDK_DIR"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams.ini" "Field 10" "State" "$INSTDIR\repository"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams.ini" "Field 12" "State" "C:\lamsconf"
    !insertmacro MUI_HEADER_TEXT "Setting Up LAMS" "Configure the LAMS Server.  If unsure, use the default."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams.ini"
FunctionEnd


Function PostLAMSConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $JDK_DIR "lams.ini" "Field 2" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_DOMAIN "lams.ini" "Field 4" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_PORT "lams.ini" "Field 6" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_LOCALE "lams.ini" "Field 8" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_REPOSITORY "lams.ini" "Field 10" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_CONF "lams.ini" "Field 12" "State"

    # check java version using given dir
    nsExec::ExecToStack '$JDK_DIR\bin\javac -version'
    Pop $0
    Pop $1
    ${StrStr} $0 $1 "1.5"
    ${If} $0 == ""
        MessageBox MB_OK|MB_ICONSTOP "Could not verify Java JDK 1.5, please check your JDK directory."
        Abort
    ${EndIf}
FunctionEnd


Function PreWildfireConfig
    !insertmacro MUI_HEADER_TEXT "Setting Up Wildfire Chat Server" "Configure Wildfire, chat server for LAMS.  If unsure, use the default."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "wildfire.ini"
FunctionEnd


Function PostWildfireConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_DOMAIN "wildfire.ini" "Field 2" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_USER "wildfire.ini" "Field 5" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_PASS "wildfire.ini" "Field 7" "State"

    # check wildfire is running
    SetOutPath $TEMP
    File "..\build\LocalPortScanner.class"
    nsExec::ExecToStack '$JDK_DIR\bin\java LocalPortScanner 9090'
    Pop $0
    Pop $1
    ${If} $0 == 0
        MessageBox MB_OK|MB_ICONSTOP "Wildfire does not appear to be running - LAMS will be OK, but chat rooms will be unavailable."
    ${EndIf}
FunctionEnd


Function DeployConfig
    # extract support files to write configuration
    SetOutPath $INSTDIR
    File /r "..\apache-ant-1.6.5"
    SetOutPath $TEMP
    ;File "..\build\Switch.class"
    File "build.xml"
    File "..\templates\lams.xml"
    File "..\templates\mysql-ds.xml"
    File "..\templates\server.xml"
    File "..\templates\run.bat"
    File "..\templates\wrapper.conf"
        
    # create build.properties
    ClearErrors
    FileOpen $0 $TEMP\installer.properties w
    IfErrors 0 +2
        goto error
        
    # convert '\' to '/' for Ant's benefit
    ;nsExec::ExecToStack 'java -cp "$TEMP" Switch "$TEMP"'
    ;Pop $1 ; return code, 0=success
    ;Pop $2 ; output
    Push $TEMP
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "TEMP=$2$\r$\n"
    
    ;nsExec::ExecToStack 'java -cp "$TEMP" Switch "$LAMS_CONF"'
    ;Pop $1 ; return code, 0=success
    ;Pop $2 ; output
    Push $LAMS_CONF
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "LAMS_CONF=$2$\r$\n"
        
    ;nsExec::ExecToStack 'java -cp "$TEMP" Switch "$INSTDIR"'
    ;Pop $1 ; return code, 0=success
    ;Pop $2 ; output
    Push $INSTDIR
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "URL=http://$LAMS_DOMAIN:$LAMS_PORT/lams/$\r$\n"
    FileWrite $0 "INSTDIR=$2/$\r$\n"
    FileWrite $0 "TEMPDIR=$2/temp$\r$\n"
    FileWrite $0 "DUMPDIR=$2/dump$\r$\n"
    FileWrite $0 "EARDIR=$2/jboss-4.0.2/server/default/deploy/lams.ear$\r$\n"
    FileWrite $0 "DEPLOYDIR=$2/jboss-4.0.2/server/default/deploy$\r$\n"
    FileWrite $0 "TOMCATDIR=$2/jboss-4.0.2/server/default/deploy/jbossweb-tomcat55.sar$\r$\n"
    FileWrite $0 "BINDIR=$2/jboss-4.0.2/bin$\r$\n"
    
    # Use unix slashes for config in wrapper.conf
    Push $JDK_DIR
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "JDK_DIR_UNIX_SLASH=$2$\r$\n"
    
    ;nsExec::ExecToStack 'java -cp "$TEMP" Switch "$LAMS_REPOSITORY"'
    ;Pop $1 ; return code, 0=success
    ;Pop $2 ; output
    Push $LAMS_REPOSITORY
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "REPOSITORYDIR=$2$\r$\n"
        
    StrCpy $LAMS_LOCALE $LAMS_LOCALE 5
    FileWrite $0 "LOCALE=$LAMS_LOCALE$\r$\n"
    ${If} $LAMS_LOCALE == "ar_JO"
        FileWrite $0 "LOCALE_DIRECTION=RTL$\r$\n"
    ${Else}
        FileWrite $0 "LOCALE_DIRECTION=LTR$\r$\n"
    ${EndIf}
    FileWrite $0 "WILDFIRE_DOMAIN=$WILDFIRE_DOMAIN$\r$\n"
    FileWrite $0 "WILDFIRE_CONFERENCE=conference.$WILDFIRE_DOMAIN$\r$\n"
    FileWrite $0 "WILDFIRE_USER=$WILDFIRE_USER$\r$\n"
    FileWrite $0 "WILDFIRE_PASS=$WILDFIRE_PASS$\r$\n"
    
    FileWrite $0 "DB_NAME=$DB_NAME$\r$\n"
    FileWrite $0 "DB_USER=$DB_USER$\r$\n"
    FileWrite $0 "DB_PASS=$DB_PASS$\r$\n"
    
    FileWrite $0 "LAMS_PORT=$LAMS_PORT$\r$\n"
    FileClose $0
    # for debugging purposes
    CopyFiles "$TEMP\installer.properties" $INSTDIR
        
    # use Ant to copy configuration to destination LAMS_CONF
    DetailPrint '$INSTDIR\apache-ant-1.6.5\bin\ant.bat configure-deploy'
    nsExec::ExecToStack '$INSTDIR\apache-ant-1.6.5\bin\ant.bat configure-deploy'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    DetailPrint "LAMS configure status: $0"
    DetailPrint "LAMS configure output: $1"
    ${If} $0 == "error"
        goto error
    ${EndIf}
    ${StrStr} $0 $1 "BUILD SUCCESSFUL"
    ${If} $0 == ""
        goto error
    ${EndIf}
    
    goto done
    
    error:
        MessageBox MB_OK|MB_ICONSTOP "LAMS configuration failed.  Please check your LAMS configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "LAMS configuration failed."
    done:
FunctionEnd


Function ImportDatabase
    SetOutPath $TEMP
    File "..\build\dump.sql"
    
    # $9 == 0 for empty password
    StrLen $9 $MYSQL_ROOT_PASS
    
    StrCpy $0 '$MYSQL_DIR\bin\mysql -e "CREATE DATABASE $DB_NAME DEFAULT CHARACTER SET utf8" -u root'
    DetailPrint $0
    ${If} $9 != 0
        StrCpy $0 '$MYSQL_DIR\bin\mysql -e "CREATE DATABASE $DB_NAME DEFAULT CHARACTER SET utf8" -u root -p$MYSQL_ROOT_PASS' 
    ${EndIf}
    nsExec::ExecToStack $0
    Pop $0
    Pop $1
    ${If} $0 == 1
        goto error
    ${EndIf}
    
    StrCpy $0 `$MYSQL_DIR\bin\mysql -e "GRANT ALL PRIVILEGES ON *.* TO $DB_USER@localhost IDENTIFIED BY '$DB_PASS'" -u root`
    DetailPrint $0
    ${If} $9 != 0
        StrCpy $0 `$MYSQL_DIR\bin\mysql -e "GRANT ALL PRIVILEGES ON *.* TO $DB_USER@localhost IDENTIFIED BY '$DB_PASS'" -u root -p$MYSQL_ROOT_PASS`
    ${EndIf}
    nsExec::ExecToStack $0
    Pop $0
    Pop $1
    ${If} $0 == 1
        goto error
    ${EndIf}
    
    StrCpy $0 '$MYSQL_DIR\bin\mysql -e "REVOKE PROCESS,SUPER ON *.* from $DB_USER@localhost" -u root'
    DetailPrint $0
    ${If} $9 != 0
        StrCpy $0 '$MYSQL_DIR\bin\mysql -e "REVOKE PROCESS,SUPER ON *.* from $DB_USER@localhost" -u root -p$MYSQL_ROOT_PASS' 
    ${EndIf}
    nsExec::ExecToStack $0
    Pop $0
    Pop $1
    ${If} $0 == 1
        goto error
    ${EndIf}
    
    StrCpy $0 '$MYSQL_DIR\bin\mysqladmin flush-privileges -u root'
    DetailPrint $0
    ${If} $9 != 0
        StrCpy $0 '$MYSQL_DIR\bin\mysqladmin flush-privileges -u root -p$MYSQL_ROOT_PASS' 
    ${EndIf}
    nsExec::ExecToStack $0
    Pop $0
    Pop $1
    ${If} $0 == 1
        goto error
    ${EndIf}
    
    /* mysql keeps rejecting 'mysql -e...' and 'mysql db_name < dump.sql' commands
    StrCpy $0 `$MYSQL_DIR\bin\mysql -e "use $DB_NAME; source '$TEMP\dump.sql'" -u $DB_USER`
    DetailPrint $0
    StrLen $9 $DB_PASS
    ${If} $9 != 0
        StrCpy $0 `$MYSQL_DIR\bin\mysql -e "use $DB_NAME; source '$TEMP\dump.sql'" -u $DB_USER -p$DB_PASS`
    ${EndIf}
    nsExec::ExecToStack $0
    Pop $0
    Pop $1
    ${If} $0 == 1
        goto error
    ${EndIf}
    */
    
    # use Ant to import database
    DetailPrint '$INSTDIR\apache-ant-1.6.5\bin\ant.bat import-db'
    nsExec::ExecToStack '$INSTDIR\apache-ant-1.6.5\bin\ant.bat import-db'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    DetailPrint "DB import status: $0"
    DetailPrint "DB import output: $1"
    ${StrStr} $0 $1 "BUILD SUCCESSFUL"
    ${If} $0 == ""
        goto error
    ${EndIf}
    
    goto done
    
    error:
        MessageBox MB_OK|MB_ICONSTOP "Database setup failed.  Please check your MySQL configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "Database setup failed."
    done:
    
FunctionEnd


Function DeployLAMSConfigAlt
    # failed attempt to not use Ant
    ;File "..\build\Filter.class"
    ;File "lams.xml.template"
    ;nsExec::ExecToStack 'java -cp "$TEMP" Filter lams.xml.template @URL@ "$LAMS_DOMAIN"'
    ;nsExec::Exec 'copy "$TEMP\lams.xml.tmp" "$LAMS_CONF\lams.xml"'
FunctionEnd


Function WriteRegEntries
    WriteRegStr HKLM "${REG_HEAD}" "dir_jdk" $JDK_DIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_mysql" $MYSQL_DIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_inst" $INSTDIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_repository" $LAMS_REPOSITORY
    WriteRegStr HKLM "${REG_HEAD}" "dir_conf" $LAMS_CONF
    WriteRegStr HKLM "${REG_HEAD}" "version" "${VERSION}"
    WriteRegStr HKLM "${REG_HEAD}" "db_name" $DB_NAME
    WriteRegStr HKLM "${REG_HEAD}" "db_user" $DB_USER
    WriteRegStr HKLM "${REG_HEAD}" "db_pass" $DB_PASS
    WriteRegStr HKLM "${REG_HEAD}" "lams_domain" $LAMS_DOMAIN
    WriteRegStr HKLM "${REG_HEAD}" "lams_port" $LAMS_PORT
    WriteRegStr HKLM "${REG_HEAD}" "lams_locale" $LAMS_LOCALE
    WriteRegStr HKLM "${REG_HEAD}" "wildfire_domain" $WILDFIRE_DOMAIN
    WriteRegStr HKLM "${REG_HEAD}" "wildfire_user" $WILDFIRE_USER
    WriteRegStr HKLM "${REG_HEAD}" "wildfire_pass" $WILDFIRE_PASS
FunctionEnd


Function SetupStartMenu
	CreateDirectory "$SMPROGRAMS\LAMSv2"
	CreateShortCut "$SMPROGRAMS\LAMSv2\Start LAMS.lnk" "$INSTDIR\lams-start.exe"
	CreateShortCut "$SMPROGRAMS\LAMSv2\Stop LAMS.lnk" "$INSTDIR\lams-stop.exe"
	CreateShortCut "$SMPROGRAMS\LAMSv2\Uninstall LAMS.lnk" "$INSTDIR\lams-uninstall.exe"
FunctionEnd


# cleanup functions
Function RemoveTempFiles
    Delete "$TEMP\lams.xml"
    Delete "$TEMP\mysql-ds.xml"
    Delete "$TEMP\server.xml"
    Delete "$TEMP\run.bat"
    Delete "$TEMP\wrapper.conf"
    Delete "$TEMP\dump.sql"
    Delete "$TEMP\build.xml"
    Delete "$TEMP\installer.properties"
    Delete "$INSTDIR\wrapper.conf"
FunctionEnd

Function .onInstFailed
    Call RemoveTempFiles
    RMDir /r $INSTDIR
    RMDir /r $LAMS_CONF
FunctionEnd

Function .onInstSuccess
    Call RemoveTempFiles
    RMDir /r "$INSTDIR\apache-ant-1.6.5"
FunctionEnd


# installer sections
#
SectionGroup /e "Install LAMS ${VERSION}"
    Section "JBoss 4.0.2" jboss
        SectionIn RO
    SectionEnd

    Section "LAMS ${VERSION}" lams
        SectionIn RO
        
        SetOutPath $INSTDIR
        File /a /r /x all /x minimal /x log /x tmp /x work /x jsMath.war ${SOURCE_JBOSS_HOME}
        
        Call DeployConfig
        Call ImportDatabase
        
        CreateDirectory $INSTDIR\temp
        CreateDirectory $INSTDIR\dump
        CreateDirectory $LAMS_REPOSITORY
        
        SetOutPath $LAMS_CONF
        File /a ${SOURCE_LAMS_CONF}\*.dtd
        File /a ${SOURCE_LAMS_CONF}\authentication.xml

        # Log mode is set to INFO in this template
        SetOutPath "$INSTDIR\jboss-4.0.2\server\default\conf"
        File /a "..\templates\log4j.xml"
        
        Call WriteRegEntries
        
        SetOutPath $INSTDIR
        File /a "..\build\lams-start.exe"
        File /a "..\build\lams-stop.exe"
        Call SetupStartMenu
        
        WriteUninstaller "$INSTDIR\lams-uninstall.exe"
    SectionEnd
    
    Section "Install as Service" service
        SectionIn RO
        SetOutPath "$INSTDIR\jboss-4.0.2\bin"
        File /a "..\wrapper-windows-x86-32-3.2.1\bin\wrapper.exe"
        File /a "/oname=$INSTDIR\jboss-4.0.2\bin\InstallLAMS-NT.bat" "..\wrapper-windows-x86-32-3.2.1\bin\InstallTestWrapper-NT.bat"
        File /a "/oname=$INSTDIR\jboss-4.0.2\bin\UninstallLAMS-NT.bat" "..\wrapper-windows-x86-32-3.2.1\bin\UninstallTestWrapper-NT.bat"
        SetOutPath "$INSTDIR\jboss-4.0.2\lib"
        File /a "..\wrapper-windows-x86-32-3.2.1\lib\wrapper.dll"
        File /a "..\wrapper-windows-x86-32-3.2.1\lib\wrapper.jar"
        CreateDirectory "$INSTDIR\jboss-4.0.2\conf"
        CopyFiles "$INSTDIR\wrapper.conf" "$INSTDIR\jboss-4.0.2\conf\wrapper.conf"
        CreateDirectory "$INSTDIR\jboss-4.0.2\logs"
        nsExec::ExecToStack '$INSTDIR\jboss-4.0.2\bin\InstallLAMS-NT.bat'
        Pop $0
        ${If} $0 == 0
            DetailPrint "LAMSv2 successfully setup as a service. ($0)"
        ${Else}
            DetailPrint "LAMSv2 was not setup as a service. ($0)"
            MessageBox MB_OK|MB_ICONSTOP "LAMSv2 was not installed as a service.  However you may start LAMS by double-clicking $INSTDIR\jboss-4.0.2\bin\run.bat."
        ${EndIf}            
    SectionEnd
SectionGroupEnd

SectionGroup /e "Optional"
    Section /o "jsMath" jsmath
        SetOutPath "$INSTDIR\jboss-4.0.2\server\default\deploy"
        File /a /r "${SOURCE_JBOSS_HOME}\server\default\deploy\jsMath.war"
    SectionEnd
SectionGroupEnd


# descriptions
#
LangString DESC_jboss ${LANG_ENGLISH} "JBoss Application Server."
LangString DESC_lams ${LANG_ENGLISH} "LAMS Application."
LangString DESC_service ${LANG_ENGLISH} "Install as a Windows service."
LangString DESC_jsmath ${LANG_ENGLISH} "Include jsMath to be able to write and display mathematical symbols and equations."

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${jboss} $(DESC_jboss)
    !insertmacro MUI_DESCRIPTION_TEXT ${lams} $(DESC_lams)
    !insertmacro MUI_DESCRIPTION_TEXT ${service} $(DESC_service)
    !insertmacro MUI_DESCRIPTION_TEXT ${jsmath} $(DESC_jsmath)
!insertmacro MUI_FUNCTION_DESCRIPTION_END


# Uninstaller
#
Var UNINSTALL_DB

Function un.onInit
    !insertmacro MUI_LANGDLL_DISPLAY
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "uninstall.ini"
FunctionEnd

Function un.PreUninstall
    !insertmacro MUI_HEADER_TEXT "Remove LAMS Database" "Choose whether to remove the LAMS database."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "uninstall.ini"
FunctionEnd


Function un.PostUninstall
    !insertmacro MUI_INSTALLOPTIONS_READ $UNINSTALL_DB "uninstall.ini" "Field 1" "State"
FunctionEnd


Section "Uninstall"
    # need to check lams is stopped
    RMDir /r $INSTDIR
    ReadRegStr $0 HKLM "${REG_HEAD}" "dir_conf"
    DetailPrint "Removing $0..."
    RMDir /r $0
    
    ${If} $UNINSTALL_DB == 1
        ReadRegStr $0 HKLM "${REG_HEAD}" "dir_mysql"
        ReadRegStr $1 HKLM "${REG_HEAD}" "db_name"
        ReadRegStr $2 HKLM "${REG_HEAD}" "db_user"
        ReadRegStr $3 HKLM "${REG_HEAD}" "db_pass"
        StrLen $9 $3
        StrCpy $4 '$0\bin\mysql -e "DROP DATABASE $1" -u $2'
        DetailPrint $4
        ${If} $9 != 0
            StrCpy $4 '$0\bin\mysql -e "DROP DATABASE $1" -u $2 -p$3' 
        ${EndIf}
        nsExec::ExecToStack $4
        Pop $0
        Pop $1
        ${If} $0 == 1
            MessageBox MB_OK|MB_ICONSTOP "Couldn't remove LAMS database:$\r$\n$\r$\n$1"
            DetailPrint "Failed to remove LAMS database."
        ${EndIf}
    ${EndIf}
    
    ; batch file doesn't want to work when called with ExecToStack
    ;nsExec::ExecToStack '$INSTDIR\jboss-4.0.2\bin\UninstallLAMS-NT.bat'
    nsExec::ExecToStack 'sc delete LAMSv2'
    Pop $0
    Pop $1
    ${StrStr} $2 $1 "SUCCESS"
    ${If} $2 == ""
        MessageBox MB_OK|MB_ICONSTOP "Couldn't remove LAMSv2 service.$\r$\n$\r$\n$1"
        DetailPrint "Failed to remove LAMSv2 service."
    ${Else}
        DetailPrint "Removed LAMSv2 service."
    ${EndIf}
    DeleteRegKey HKLM "${REG_HEAD}"
SectionEnd