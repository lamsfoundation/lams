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

# includes
!include "TextFunc.nsh"
!include "Functions.nsh"
!include "MUI.nsh"
!include "LogicLib.nsh"

# functions from TextFunc.nsh
!insertmacro FileJoin
!insertmacro LineFind

# constants
!define VERSION "2.0"
!define SOURCE_JBOSS_HOME "D:\jboss-4.0.2"  ; location of jboss where lams was deployed
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"

# installer settings
!define MUI_ICON "..\graphics\lams2.ico"
!define MUI_UNICON "..\graphics\lams2.ico"
Name "LAMS ${VERSION}"
;BrandingText "LAMS ${VERSION} -- built on ${__TIMESTAMP__}"
BrandingText "LAMS ${VERSION} -- built on ${__DATE__} ${__TIME__}"
OutFile "..\build\LAMS-${VERSION}.exe"
InstallDir "C:\lams"
InstallDirRegKey HKLM "${REG_HEAD}" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 

# set installer header image
;!define MUI_HEADERIMAGE
;!define MUI_HEADERIMAGE_BITMAP "..\graphics\header.bmp"
;!define MUI_HEADERIMAGE_RIGHT ; will switch to left side when using a RTL language

# set warning when cancelling install
!define MUI_ABORTWARNING

# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Install Wizard"
!define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the installation of LAMS ${VERSION}.\r\n\r\n\
    Please ensure you have a copy of MySQL 5.x installed and running, and Java JDK version 1.5.x.\r\n\r\n\
    Click Next to continue."

# set components page type
;!define MUI_COMPONENTSPAGE_NODESC
!define MUI_COMPONENTSPAGE_SMALLDESC

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
!insertmacro MUI_PAGE_LICENSE "..\license.txt"
!insertmacro MUI_PAGE_COMPONENTS
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE DirectoryLeave
!insertmacro MUI_PAGE_DIRECTORY
Page custom PreMySQLConfig PostMySQLConfig
Page custom PreLAMSConfig PostLAMSConfig
Page custom PreLAMS2Config PostLAMS2Config
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
/*
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
ReserveFile "lams2.ini"
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
Var LAMS_USER
Var LAMS_PASS
Var WILDFIRE_DOMAIN
Var WILDFIRE_USER
Var WILDFIRE_PASS
Var WINTEMP
Var RETAIN_DIR
Var RETAIN_FILES



# installer sections
#
SectionGroup /e "!Install LAMS"
    Section "JBoss 4.0.2" jboss
        SectionIn RO
        SetOutPath $INSTDIR
        File /a /r /x all /x minimal /x robyn /x log /x tmp /x work /x jsMath.war ${SOURCE_JBOSS_HOME}
    SectionEnd

    Section "LAMS ${VERSION}" lams
        SectionIn RO
        
        SetOutPath $INSTDIR
        
        ${if} $RETAIN_FILES == "1"
            Createdirectory "$WINTEMP\lams"
            CopyFiles /silent $INSTDIR\backup "$WINTEMP\lams"
            DetailPrint "$WINTEMP\lams\backup"
        ${endif}
        
        Call DeployConfig
        Call ImportDatabase
        
        CreateDirectory "$INSTDIR\temp"
        CreateDirectory "$INSTDIR\dump"
        CreateDirectory "$LAMS_REPOSITORY"
        
        # Log mode is set to INFO in this template
        SetOutPath "$INSTDIR\jboss-4.0.2\server\default\conf"
        File /a "..\templates\log4j.xml"
        
        Call WriteRegEntries
        
        SetOutPath $INSTDIR
        File /a "..\build\lams-start.exe"
        File /a "..\build\lams-stop.exe"
        File /a "..\license.txt"
        File /a "..\readme.txt"
        Call SetupStartMenu
        
        ${if} $RETAIN_FILES == "1"
            Call OverWriteRetainedFiles
        ${endif}
        
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
            MessageBox MB_OK|MB_ICONEXCLAMATION "LAMSv2 was not installed as a service.  However you may start LAMS by double-clicking $INSTDIR\jboss-4.0.2\bin\run.bat."
        ${EndIf}            
    SectionEnd
SectionGroupEnd

SectionGroup /e "Optional"
    Section /o "jsMath (compressed)" jsmathc
        SetOutPath "$INSTDIR\jboss-4.0.2\server\default\deploy"
        File /a /r  "..\..\jsmath\build\lib\jsMath.war"
    SectionEnd
    
    Section /o "jsMath (expanded)" jsmathe
        SetOutPath "$TEMP"
        File /a  "..\..\jsmath\build\lib\jsMath.war"
        CreateDirectory "$INSTDIR\jboss-4.0.2\server\default\deploy\jsMath.war"
        SetOutPath "$INSTDIR\jboss-4.0.2\server\default\deploy\jsMath.war"
        DetailPrint "Expanding jsMath.war..."
        nsExec::ExecToStack "$JDK_DIR\bin\jar xf $TEMP\jsMath.war"
        Pop $0
        Pop $1
        ${If} $0 != 0
            DetailPrint "Failed to expand jsMath.war."
        ${EndIf}
        Delete "$TEMP\jsMath.war"
    SectionEnd
SectionGroupEnd


# descriptions
#
LangString DESC_jboss ${LANG_ENGLISH} "JBoss Application Server."
LangString DESC_lams ${LANG_ENGLISH} "LAMS Application."
LangString DESC_service ${LANG_ENGLISH} "Install as a Windows service."
LangString DESC_jsmathc ${LANG_ENGLISH} "Include a compressed jsMath.war to be able to write and display mathematical symbols and equations."
LangString DESC_jsmathe ${LANG_ENGLISH} "Include an expanded jsMath.war to be able to write and display mathematical symbols and equations."

!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${jboss} $(DESC_jboss)
    !insertmacro MUI_DESCRIPTION_TEXT ${lams} $(DESC_lams)
    !insertmacro MUI_DESCRIPTION_TEXT ${service} $(DESC_service)
    !insertmacro MUI_DESCRIPTION_TEXT ${jsmathc} $(DESC_jsmathc)
    !insertmacro MUI_DESCRIPTION_TEXT ${jsmathe} $(DESC_jsmathe)
!insertmacro MUI_FUNCTION_DESCRIPTION_END


# functions
#

Function .onInit
    # select language
    ;!insertmacro MUI_LANGDLL_DISPLAY
    
    # Abort install if already installed
    ReadRegStr $0 HKLM "${REG_HEAD}" "version"
    ${If} $0 != ""
        MessageBox MB_OK|MB_ICONSTOP "LAMS $0 is already installed.  Please uninstall before continuing."
        Abort
    ${EndIf}
    
    # extract custom page display config
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams2.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "mysql.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "wildfire.ini"
    
    # set jsmath exploded size (assumes 4KB cluster size on destination hdd)
    SectionSetSize ${jsmathe} 81816
    
    # init mutually exclusive checkbox status
    SectionGetFlags ${jsmathc} $8
    SectionGetFlags ${jsmathc} $9
FunctionEnd


Function .onSelChange
    # mutually exclusive checkboxes
    SectionGetFlags ${jsmathc} $0
    ;MessageBox MB_OK "old c: $8 e: $9 $\r$\n new c: $0"
    IntCmp $0 $8 next 0 0
    IntCmp $0 ${SF_SELECTED} 0 next next
    SectionGetFlags ${jsmathe} $1
    IntOp $1 $1 & ${SECTION_OFF}
    SectionSetFlags "${jsmathe}" $1
    Goto done
    
    next:
    SectionGetFlags ${jsmathe} $0
    ;MessageBox MB_OK "old c: $8 e: $9 $\r$\n new e: $0"
    IntCmp $0 $9 done 0 0
    IntCmp $0 ${SF_SELECTED} 0 done done
    SectionGetFlags ${jsmathc} $1
    IntOp $1 $1 & ${SECTION_OFF}
    SectionSetFlags "${jsmathc}" $1
    
    done:
        SectionGetFlags ${jsmathc} $8
        SectionGetFlags ${jsmathe} $9
        ;MessageBox MB_OK "c: $8 e: $9"
FunctionEnd


Function CheckJava
    # check for JDK
    ReadRegStr $JDK_DIR HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.6" "JavaHome"
    ${If} $JDK_DIR == ""
        ReadRegStr $JDK_DIR HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.5" "JavaHome"
        ${if} $JDK_DIR == ""
            MessageBox MB_OK|MB_ICONSTOP "Could not find a Java JDK 1.5 or 1.6 installation.  Please ensure you have JDK 1.5 or 1.6 installed."
        ${EndIf}
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


Function DirectoryLeave
    # check for spaces in instdir
    ${StrStr} $0 $INSTDIR " "
    ${If} $0 != ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Please choose a location without a space."
        Abort
    ${EndIf}
    # check LAMS 1
    ReadRegStr $0 HKLM "SOFTWARE\LAMS\Server" "install_dir"
    ReadRegStr $1 HKLM "SOFTWARE\LAMS\Server" "install_status"
    ReadRegStr $2 HKLM "SOFTWARE\LAMS\Server" "install_version"
    ${If} $1 == "successful"
        MessageBox MB_OK|MB_ICONINFORMATION "If you have LAMS $2 on your system, remember not to run it at the same time as LAMS ${VERSION} (unless you know what you're doing)."
        ${If} $0 == $INSTDIR
            MessageBox MB_OK|MB_ICONEXCLAMATION "There appears to be a LAMS $2 installation at $INSTDIR - please chose another location."
            Abort
        ${EndIf}
    ${EndIf}
    
    Strcpy $RETAIN_FILES "0"

    
    IfFileExists "$INSTDIR\backup\backup.zip" backupExists end
    backupExists:
        ; CHECK if there are files retained from a previous uninstall
        ; THEN after installation, overwrite retained files and free files from temp folder temp folder 
        strcpy $6 ""
        MessageBox MB_YESNO|MB_ICONQUESTION "Installer has detected database, repository and uploaded files retained from a previous install, do you wish to use them?" \
                    IDYES retainFiles \
                    IDNO noRetain
        noRetain:
            MessageBox MB_OK|MB_ICONEXCLAMATION "Files will be backed up as $INSTDIR\backup"
            goto end
        retainFiles:
            Strcpy $WINTEMP "C:\WINDOWS\Temp"
            Strcpy $RETAIN_FILES "1"
            #CopyFiles $INSTDIR $WINTEMP
            #MessageBox MB_OK|MB_ICONEXCLAMATION "$RETAIN_FILES \n $RETAIN_REP $\n $RETAIN_CONF $\n $RETAIN_DB"
    end:
FunctionEnd


Function PreMySQLConfig
    Call CheckMySQL
    !insertmacro MUI_INSTALLOPTIONS_WRITE "mysql.ini" "Field 3" "State" "$MYSQL_DIR"
    !insertmacro MUI_HEADER_TEXT "Setting Up MySQL Database Access (1/4)" "Choose a MySQL database and user account for LAMS.  If unsure, use the defaults."
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
    ${If} $1 == "" ; if mySQL install directory field is empty, do not continue
       MessageBox MB_OK|MB_ICONSTOP "Your MySQL directory does not appear to be valid, please enter a valid MySQL directory before continuing.$\r$\n$\r$\n$1"
       Abort
    ${EndIf}
    ${StrStr} $0 $1 "5.0"
    ${If} $0 == "" ; if not 5.0.x, check 5.1.x
        ${StrStr} $0 $1 "5.1"
        ${If} $0 == ""
            MessageBox MB_OK|MB_ICONSTOP "Your MySQL version does not appear to be compatible with LAMS (5.0.x or 5.1.x): $\r$\n$\r$\n$1"
            Abort
        ${EndIf}
    ${EndIf}
    
    # Checking if the given database name already exists in the mysql database list
    ifFileExists "$MYSQL_DIRdata\$DB_NAME\*.*" databaseNameExists continue
    databaseNameExists:
        MessageBox MB_OK|MB_ICONSTOP 'A database named "$DB_NAME" already exists, please try another name.$\n$MYSQL_DIRdata\$DB_NAME'
        Abort    
    continue:
        
    
    # check root password, server status
    StrLen $0 $MYSQL_ROOT_PASS
    ${If} $0 == 0
        nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin ping -u root'
    ${Else}
        nsExec::ExecToStack '$MYSQL_DIR\bin\mysqladmin ping -u root -p$MYSQL_ROOT_PASS'
    ${EndIf}
    Pop $0
    Pop $1
    # check root password is correct
    ${StrStr} $2 $1 "Access denied"
    ${If} $2 != ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "The MySQL root password appears to be incorrect - please re-enter your password."
        Abort
    ${EndIf}
    # check mysql is running
    ${StrStr} $2 $1 "mysqld is alive"
    ${If} $2 == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "MySQL does not appear to be running - please make sure it is running before continuing."
        Abort
    ${EndIf}
    
    
FunctionEnd


Function PreLAMSConfig
    Call CheckJava
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams.ini" "Field 2" "State" "$JDK_DIR"
    !insertmacro MUI_INSTALLOPTIONS_WRITE "lams.ini" "Field 4" "State" "$INSTDIR\repository"
    !insertmacro MUI_HEADER_TEXT "Setting Up LAMS (2/4)" "Configure the LAMS Server.  If unsure, use the defaults."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams.ini"
FunctionEnd


Function PostLAMSConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $JDK_DIR "lams.ini" "Field 2" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_REPOSITORY "lams.ini" "Field 4" "State"

    # check java version using given dir
    nsExec::ExecToStack '$JDK_DIR\bin\javac -version'
    Pop $0
    Pop $1
    ${StrStr} $0 $1 "1.6"
    ${If} $0 == ""
        ${StrStr} $0 $1 "1.5"
        ${If} $0 == ""
            MessageBox MB_OK|MB_ICONEXCLAMATION "Could not verify Java JDK 1.5, please check your JDK directory."
            Abort
        ${EndIf}
    ${EndIf}
FunctionEnd


Function PreLAMS2Config
    !insertmacro MUI_HEADER_TEXT "Setting Up LAMS (3/4)" "Configure the LAMS Server, and choose an admin username and password."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams2.ini"
FunctionEnd


Function PostLAMS2Config
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_DOMAIN "lams2.ini" "Field 8" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_PORT "lams2.ini" "Field 9" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_LOCALE "lams2.ini" "Field 12" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_USER "lams2.ini" "Field 2" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $LAMS_PASS "lams2.ini" "Field 5" "State"
FunctionEnd


Function PreWildfireConfig
    !insertmacro MUI_HEADER_TEXT "Setting Up Wildfire Chat Server (4/4)" "Configure Wildfire, chat server for LAMS.  If unsure, use the default."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "wildfire.ini"
FunctionEnd


Function PostWildfireConfig
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_DOMAIN "wildfire.ini" "Field 2" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_USER "wildfire.ini" "Field 5" "State"
    !insertmacro MUI_INSTALLOPTIONS_READ $WILDFIRE_PASS "wildfire.ini" "Field 7" "State"

    # check wildfire is running by checking client connection port 5222
    SetOutPath $TEMP
    File "..\build\LocalPortScanner.class"
    nsExec::ExecToStack '$JDK_DIR\bin\java LocalPortScanner 5222'
    Pop $0
    Pop $1
    ${If} $0 == 0
        MessageBox MB_OKCANCEL|MB_ICONQUESTION "Wildfire does not appear to be running - LAMS will be OK, but chat rooms will be unavailable." IDOK noWildfire IDCANCEL cancel
        cancel:
            Abort
        noWildfire:
    ${EndIf}
FunctionEnd


Function DeployConfig
    # extract support files to write configuration
    SetOutPath $INSTDIR
    File /r "..\apache-ant-1.6.5"
    File /r "..\zip"
    SetOutPath $TEMP
    File "build.xml"
    File "..\templates\mysql-ds.xml"
    File "..\templates\server.xml"
    File "..\templates\run.bat"
    File "..\templates\wrapper.conf"
    File "..\templates\index.html"
    File "..\templates\lamsauthentication.xml"
    File "..\templates\update_lams_configuration.sql"
    File "..\templates\login-config.xml"
      

      
    # create installer.properties
    ClearErrors
    FileOpen $0 $TEMP\installer.properties w
    IfErrors 0 +2
        goto error
        
    # convert '\' to '/' for Ant's benefit
    Push $TEMP
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "TEMP=$2$\r$\n"
            
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
    FileWrite $0 "LAMS_USER=$LAMS_USER$\r$\n"
    FileWrite $0 "LAMS_PASS=$LAMS_PASS$\r$\n"
    FileClose $0
    # for debugging purposes
    CopyFiles "$TEMP\installer.properties" $INSTDIR
        
    # use Ant to write config to files
    FileOpen $0 "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" w
    IfErrors 0 +2
        goto error
    FileWrite $0 "@echo off$\r$\nset JAVACMD=$JDK_DIR\bin\java$\r$\n"
    FileClose $0
    ${FileJoin} "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" "$INSTDIR\apache-ant-1.6.5\bin\ant.bat" ""
    DetailPrint '$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat configure-deploy'
    nsExec::ExecToStack '$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat configure-deploy'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    
    FileOpen $R0 "$INSTDIR\installer_ant.log" w
    IfErrors 0 +2
        goto error
    FileWrite $R0 $1
    FileClose $R0
    
    ${If} $0 == "error"
        goto error
    ${EndIf}
    ${StrStr} $0 $1 "BUILD SUCCESSFUL"
    ${If} $0 == ""
        goto error
    ${EndIf}
    
    # write my.ini if exists
    # TODO doesn't check if tx_isolation is already READ-COMMITTED
    # TODO doesn't take effect until mysql server is restarted
    DetailPrint "Setting MySQL transaction-isolation to READ-COMMITTED"
    ${LineFind} "$MYSQL_DIR\my.ini" "" "1" "WriteMyINI"
    IfErrors 0 +2
        MessageBox MB_OK|MB_ICONEXCLAMATION "Couldn't write to $MYSQL_DIR\my.ini.  Please write this text into your MySQL configuration file and restart MySQL:$\r$\n$\r$\n[mysqld]$\r$\ntransaction-isolation=READ-COMMITTED"
    DetailPrint "MySQL will need to be restarted for this to take effect."
    goto done
    error:
        DetailPrint "Ant configure-deploy failed."
        MessageBox MB_OK|MB_ICONSTOP "LAMS configuration failed.  Please check your LAMS configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "LAMS configuration failed."
    done:
FunctionEnd


Function WriteMyINI
    FileWrite $R4 "[mysqld]$\r$\n"
    FileWrite $R4 "transaction-isolation=READ-COMMITTED$\r$\n"
    Push $0
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
    
     ${if} $RETAIN_FILES == '1'      
        #unzip repository files
        setoutpath "$INSTDIR\backup"
        strcpy $4 '$INSTDIR\zip\7za.exe x -aoa "backup.zip"'
        nsExec::ExecToStack $4
        pop $5
        pop $6
        ${if} $5 != 0
            Detailprint  "7za Unzip error: $\r$\n$\r$\n$6"
            MessageBox MB_OK|MB_ICONSTOP "7za Unzip error:$\r$\nError:$\r$\n$\r$\n$6"
            Abort "Lams configuration failed"
        ${endif}
         
        #replace the install dump with the retained dump    
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Rebuilding datbase"   
        CopyFiles "$INSTDIR\backup\lamsDump.sql" "$TEMP\dump.sql"     
        DetailPrint "Using retained database: $INSTDIR\backup\lamsDump.sql"
        
        RMdir /r  "$INSTDIR\backup\jboss-4.0.2"
        RMdir /r  "$INSTDIR\backup\repository"
        delete  "$INSTDIR\backup\lamsDump.sql"
        
        Detailprint  "FINISHED COPYTING lamsdump.sql"
     ${endif}
    
    
    
    SetOutPath $TEMP
    # use Ant to import database
    DetailPrint '$INSTDIR\apache-ant-1.6.5\bin\ant.bat import-db'
    nsExec::ExecToStack '$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat import-db'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    DetailPrint "DB import status: $0"
    DetailPrint "DB import output: $1"
    
    FileWrite $R0 $1
    FileClose $R0
    
    ${StrStr} $0 $1 "BUILD SUCCESSFUL"
    ${If} $0 == ""
        goto error
    ${EndIf}
    
    /*${if} $RETAIN_FILES == '1'
        #replace the install dump with the retained dump
        #CopyFiles "$INSTDIR\backup\lamsDump.sql" "$TEMP\dump.sql"
        DetailPrint "Using retained database: $INSTDIR\backup\lamsDump.sql"
        strcpy $0 "$MYSQL_DIR\bin\mysql $DB_NAME -uroot - p$MYSQL_ROOT_PASS < $INSTDIR\backup\lamsDump.sql"
        nsExec::ExecToStack $0
        pop $1
        pop $2
        DetailPrint $0
        DetailPrint $1
        MessageBox MB_OK|MB_ICONEXCLAMATION "Rebuilding datbase $\n$0 $\n$1 $\n$2" 
    
        ${if} $0 != 0
            goto error
        ${endif}
    ${endif}
    */
    goto done
    
    error:
        # Just leave database if there is an error (don't want to remove someone's existing db)
        /*StrCpy $0 '$MYSQL_DIR\bin\mysql -e "drop database $DB_NAME" -u root'
        DetailPrint $0
        ${If} $9 != 0
            StrCpy $0 '$MYSQL_DIR\bin\mysql -e "drop database $DB_NAME" -u root -p$MYSQL_ROOT_PASS'
        ${EndIf}
        nsExec::ExecToStack $0
        Pop $1
        Pop $2
        ${If} $1 != 1
            DetailPrint "Undid create database action."
        ${Else}
            DetailPrint "Could not remove database '$DB_NAME'."
        ${EndIf}*/
        MessageBox MB_OK|MB_ICONSTOP "Database setup failed.  Please check your MySQL configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "Database setup failed."
    done:
    
FunctionEnd


Function WriteRegEntries
    WriteRegStr HKLM "${REG_HEAD}" "dir_jdk" $JDK_DIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_mysql" $MYSQL_DIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_inst" $INSTDIR
    WriteRegStr HKLM "${REG_HEAD}" "dir_repository" $LAMS_REPOSITORY
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

Function OverWriteRetainedFiles
    # overwriting retain files (moved to windows/temp) to install directory files
    ${if} $RETAIN_FILES == "1"
        #MessageBox MB_OK|MB_ICONSTOP "repository files to be retained"
        
        #unzip repository files
        setoutpath "$INSTDIR\backup"
        strcpy $4 '$INSTDIR\zip\7za.exe x -aoa "backup.zip"'
        nsExec::ExecToStack $4
        #pop $0
        #pop $1
        #MessageBox MB_OK|MB_ICONSTOP "Extracting retained files: $0$\n$1"
        
        #copy repository and uploaded files to install directory
        CopyFiles "$WINTEMP\lams\backup\repository" "$INSTDIR\" 
        DetailPrint "$INSTDIR\repository"
        CopyFiles "$WINTEMP\lams\backup\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war" "$INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear\"
        DetailPrint "Overwrite $INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war"
    ${endif}
    RMdir "$WINTEMP\lams"
FunctionEnd

Function SetupStartMenu
    CreateDirectory "$SMPROGRAMS\LAMSv2"
    CreateShortCut "$SMPROGRAMS\LAMSv2\Access LAMS.lnk" "http://$LAMS_DOMAIN:$LAMS_PORT/lams/"
    CreateShortCut "$SMPROGRAMS\LAMSv2\LAMS Community.lnk" "http://www.lamscommunity.org"
    CreateShortCut "$SMPROGRAMS\LAMSv2\Start LAMS.lnk" "$INSTDIR\lams-start.exe"
    CreateShortCut "$SMPROGRAMS\LAMSv2\Stop LAMS.lnk" "$INSTDIR\lams-stop.exe"
    CreateShortCut "$SMPROGRAMS\LAMSv2\Uninstall LAMS.lnk" "$INSTDIR\lams-uninstall.exe"
FunctionEnd


# cleanup functions
Function RemoveTempFiles
    Delete "$TEMP\LocalPortScanner.class"
    Delete "$TEMP\mysql-ds.xml"
    Delete "$TEMP\server.xml"
    Delete "$TEMP\run.bat"
    Delete "$TEMP\wrapper.conf"
    Delete "$TEMP\dump.sql"
    Delete "$TEMP\build.xml"
    Delete "$TEMP\installer.properties"
    Delete "$INSTDIR\wrapper.conf"
    Delete "$TEMP\index.html"
    Delete "$TEMP\lamsauthentication.xml"
    Delete "$TEMP\update_lams_configuration.sql"
    Delete "$TEMP\login-config.xml"
    Delete "$INSTDIR\update_lams_configuration.sql"
    RMDIR /r "$WINTEMP\lams"
FunctionEnd

Function .onInstFailed
    
    
    ;remove all files from the instdir excluding the backed up files
    RMDir /r "$INSTDIR\jboss-4.0.2"
    RMDir /r "$INSTDIR\dump"
    RMDir /r "$INSTDIR\repository"
    RMDir /r "$INSTDIR\temp"
    RMDir /r "$INSTDIR\build"
    Delete "$INSTDIR\index.html"
    Delete "$INSTDIR\installer.properties"
    Delete "$INSTDIR\installer_ant.log"
    Delete "$INSTDIR\lams-start.exe"
    Delete "$INSTDIR\lams-stop.exe"
    Delete "$INSTDIR\lams_uninstall.exe"
    Delete "$INSTDIR\license.txt"
    Delete "$INSTDIR\readme.txt"
    
    ; remove the db 
    strcpy $0 $MYSQL_DIR
    strcpy $1 $DB_NAME
    strcpy $2 $DB_USER
    strcpy $3 $DB_PASS

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
        MessageBox MB_OK|MB_ICONEXCLAMATION "Couldn't remove LAMS database:$\r$\n$\r$\n$1"
        DetailPrint "Failed to remove LAMS database."
    ${EndIf}
    
    Call RemoveTempFiles
FunctionEnd

Function .onInstSuccess
    Call RemoveTempFiles
    RMDir /r "$INSTDIR\apache-ant-1.6.5"
    RMDir /r "$INSTDIR\zip"
    RMDir /r "$INSTDIR\backup\repository"
    RMDIR /r "$INSTDIR\backup\jboss-4.0.2"
    Delete "$INSTDIR\backup\lamsDump.sql"
FunctionEnd



# Uninstaller
#
Var UNINSTALL_RETAIN
#Var UNINSTALL_DB
#Var UNINSTALL_RP
#Var UNINSTALL_CF

Function un.onInit
    ;!insertmacro MUI_LANGDLL_DISPLAY
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "uninstall.ini"
    
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
            MessageBox MB_YESNO|MB_ICONQUESTION "LAMS appears to be running.  Do you wish to stop LAMS?" \
                IDYES stoplams \
                IDNO quit
        ${EndIf}
        Goto continue
    stoplams:
        nsExec::ExecToStack 'sc stop LAMSv2'
        Pop $0
        Pop $1
        DetailPrint "Sent stop command to LAMS service."
        sleep 10000
        Goto checklams
        # sleep for 10s to ensure that JBoss closes properly
        
    quit:
        Delete "$TEMP\LocalPortScanner.class"
        MessageBox MB_OK|MB_ICONSTOP "Uninstall cannot continue while LAMS is running."
        Abort
    
    continue:
        Delete "$TEMP\LocalPortScanner.class"
FunctionEnd

Function un.PreUninstall
    !insertmacro MUI_HEADER_TEXT "Remove LAMS Database" "Choose whether to remove the LAMS database."
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "uninstall.ini"
FunctionEnd

Function un.PostUninstall
    !insertmacro MUI_INSTALLOPTIONS_READ $UNINSTALL_RETAIN "uninstall.ini" "Field 1" "State"
    
FunctionEnd


# http://nsis.sourceforge.net/StrStr
#
!define un.StrStr "!insertmacro un.StrStr"
 
!macro un.StrStr ResultVar String SubString
  Push `${String}`
  Push `${SubString}`
  Call un.StrStr
  Pop `${ResultVar}`
!macroend

Function un.StrStr
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


Section "Uninstall"
    

    strcpy $WINTEMP "C:\WINDOWS\Temp"
    RMDir /r "$WINTEMP\lams\backup"
    CreateDirectory "$WINTEMP\lams\backup"
       
    #strcpy $RETAIN_DIR "$INSTDIR\backup_$TIMESTAMP"
    strcpy $RETAIN_DIR "$INSTDIR\backup"
    CreateDirectory $RETAIN_DIR

    ;Now copy files that are to be retained to the temp folder
    ${If} $UNINSTALL_RETAIN == 1
        #MessageBox MB_OK|MB_ICONEXCLAMATION "retaining repository"
        ; KEEP repository and uploaded files
        ; Copy repository and jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war to backup
        ReadRegStr $6 HKLM "${REG_HEAD}" "dir_repository"
       
        CreateDirectory "$RETAIN_DIR\repository"
        CreateDirectory "$RETAIN_DIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war\"
        
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Copying files to $RETAIN_DIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war\"
        copyfiles /silent $6 "$RETAIN_DIR"
        copyfiles /silent "$INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war\*" "$RETAIN_DIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-www.war\"
        
        DetailPrint 'Saving repository and uploaded files to: $RETAIN_DIR'
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Copying files from instdir to temp"
        copyfiles /silent "$RETAIN_DIR\*" "$WINTEMP\lams\backup"
    ${EndIf}
    
    setoutpath $temp
    RMdir /r "$INSTDIR\jboss-4.0.2\"
    RMdir /r "$INSTDIR\"
    #MessageBox MB_OK|MB_ICONEXCLAMATION "INSTDIR DELETED FFS!"
    
    ; NOT SURE IF THIS SECTION OF CODE IS NECCESSARY
    ReadRegStr $0 HKLM "${REG_HEAD}" "dir_conf"
    RMDir /r $0
    
    
    
    
    ; RESTORE Retained folders to their original localtion then delete temp files
    ; DUMP database into backup folder
    ReadRegStr $0 HKLM "${REG_HEAD}" "dir_mysql"
    ReadRegStr $1 HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $2 HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $3 HKLM "${REG_HEAD}" "db_pass"
    ${If} $UNINSTALL_RETAIN == 1
        #Messagebox MB_OK|MB_ICONEXCLAMATION "retaining db"
        ; DUMP the database file into the retained install directory 
           
        CreateDirectory "$RETAIN_DIR" 
        Strcpy $4 "$0\bin\mysqldump -r $WINTEMP\lams\backup\lamsDump.sql $1 -u $2 -p$3"
        nsExec::ExecToStack $4
        Pop $8
        Pop $9
        DetailPrint 'Dumping database to: $RETAIN_DIR'
        
        Setoutpath $INSTDIR
        File /r "..\zip"
        
        #ZIP UP ALL RETAINED FILES 
        IfFileExists "$WINTEMP\lams\backup\backup.zip" removeZip leaveFolder
        removeZip:
            delete "$WINTEMP\lams\backup\backup.zip"
        leaveFolder:
        
        SetOutPath "$WINTEMP\lams\backup"
        Strcpy $4 '$INSTDIR\zip\7za.exe a -r -tzip "$RETAIN_DIR\backup.zip" "*"'
        nsExec::ExecToStack $4 
        rmdir /r "$INSTDIR\zip"
        #pop $5
        #pop $6
        #MessageBox MB_OK|MB_ICONEXCLAMATION "ZIP RESULT: $5$\n$6$\n"
        rmdir $RETAIN_DIR
    ${EndIf}
    RMDir /r "$WINTEMP\lams"
    
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
        MessageBox MB_OK|MB_ICONEXCLAMATION "Couldn't remove LAMS database:$\r$\n$\r$\n$1"
        DetailPrint "Failed to remove LAMS database."
    ${EndIf}
    
        
    
    
    ; batch file doesn't want to work when called with ExecToStack
    ; nsExec::ExecToStack '$INSTDIR\jboss-4.0.2\bin\UninstallLAMS-NT.bat'
    nsExec::ExecToStack 'sc delete LAMSv2'
    Pop $0
    Pop $1
    ; can't call StrStr from within uninstaller unless it's a un. function
    ${un.StrStr} $2 $1 "SUCCESS"
    ${If} $2 == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Couldn't remove LAMSv2 service.$\r$\n$\r$\n$1"
        DetailPrint "Failed to remove LAMSv2 service."
    ${Else}
        DetailPrint "Removed LAMSv2 service."
    ${EndIf}
    /*StrCmp $1 "[SC] Delete Service SUCCESS$\r$\n" 0 +3
    DetailPrint "Removed LAMSv2 service."
    Goto +3
    MessageBox MB_OK|MB_ICONSTOP "Couldn't remove LAMSv2 service.$\r$\n$\r$\n$1"
    DetailPrint "Failed to remove LAMSv2 service."*/
    
    DeleteRegKey HKLM "${REG_HEAD}"
    DetailPrint "Removed registry entries."
    RMDir /r "$SMPROGRAMS\LAMSv2"
    DetailPrint "Removed start menu entries."
    DetailPrint "Uninstall complete."
SectionEnd