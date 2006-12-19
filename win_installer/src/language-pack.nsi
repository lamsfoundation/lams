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
#!include "TextFunc.nsh"

!include "Functions.nsh"
!include "MUI.nsh"
!include "LogicLib.nsh"
!define ArrayNoTemp4
!include "Array.nsh"


# constants
!define VERSION "2006-12-12" ; DATE of language pack in fromat YYYYMMDD
!define SOURCE_JBOSS_HOME "D:\jboss-4.0.2"  ; location of jboss where lams was deployed
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"

# installer settings
!define MUI_ICON "..\graphics\lams2.ico"

Name "LAMS ${VERSION} Language Pack Update"

# Installer attributes
OutFile "..\build\LAMSLanguagePack-${VERSION}.exe"
InstallDir "C:\lams"
Icon ..\graphics\lams2.ico
InstallDirRegKey HKLM "${REG_HEAD}" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 
VIProductVersion 2.0.0.0
VIAddVersionKey ProductName "LAMS Language Pack ${VERSION}"
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey CompanyName "${COMPANY}"
VIAddVersionKey CompanyWebsite "${URL}"
VIAddVersionKey FileVersion ""
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright ""

# set warning when cancelling install
!define MUI_ABORTWARNING

# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Install Wizard"
!define MUI_WELCOMEPAGE_TEXT 'This wizard will guide you through updating your LAMS 2.0 with the latest language packs available.\r\n\r\n\
                              This language pack requires that you have a working version of LAMS 2.0 installed\n\n\
                              It is recommended that you close all other programs before continuing with the installation\r\n\r\n\
                              Click next to continue'

# set components page type
#!define MUI_COMPONENTSPAGE_SMALLDESC
!define MUI_COMPONENTSPAGE_NODESC 
!define MUI_COMPONENTSPAGE_TEXT 'Existing language files will be backed up in:$\n\
                                 $\t "(lams install directory)\jboss-4.0.2\server\default\deploy\lams.ear\lams-dictionary.jar\lams-dictionary.zip"$\n$\n\
                                 Language Files will then be overwritten by the new LAMS Language Pack ${VERSION}'

# set instfiles page to wait when done
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# display finish page stuff
!define MUI_FINISHPAGE_RUN $INSTDIR\lams-start.exe
!define MUI_FINISHPAGE_RUN_TEXT "Start LAMS now"
!define MUI_FINISHPAGE_TEXT "The LAMS Server has been successfully installed on your computer."
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\readme.txt
;!define MUI_FINISHPAGE_SHOWREADME_TEXT "Open the readme file"
!define MUI_FINISHPAGE_LINK "Visit LAMS Community"
!define MUI_FINISHPAGE_LINK_LOCATION "http://www.lamscommunity.org"


# installer screen pages
;--------------------------------
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "..\license.txt"
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
;--------------------------------

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

# Variables
;--------------------------------
Var MYSQL_DIR
Var DB_NAME
Var DB_USER
Var DB_PASS
Var BACKUP_DIR
Var LAMS_DIR
Var VERSION_INT
Var FLASHXML_DIR
Var SQL_QUERY
Var FOLDER_FLAG
${Array} RF_FOLDERS
${Array} CS_FOLDERS
${Array} FS_FOLDERS
;--------------------------------


Section "LAMS Language Pack ${VERSION}" LanguagePack
    # write this language pack version to registry
    ###############UNCOMMENT BELOW LINE BEFORE DEPLOY
    #WriteRegStr HKLM "${REG_HEAD}" "language_pack" $VERSION_INT
    Detailprint 'Writing Language pack version ${VERSION} to registry: "${REG_HEAD}"'
    
    setoutpath $EXEDIR
    File /r "..\zip"

    ;backup existing language files
    call zipLanguages
    
    ; copy language files from LAMS projects to a folder in $INSTDIR
    call copyProjects
    
    ; get the language files locations specific to this server from the database
    ; unpack to $INSTDIR\library\llidx
    call copyllid
    
    ; copy the flash dictionary files from central/web/flashxml to: 
    ; <JBoss_dir>\server\default\lams.ear\lams-central.war\flashxml
    call copyFlashxml
    
    ; Finally, add rows in the database (lams_supported_locale) for all new language files
    call updateDatase
SectionEnd

;--------------------------------
;Descriptions
  ;Language strings
  LangString DESC_LanguagePack ${LANG_ENGLISH} "LAMS 2.0 Language pack update ${VERSION} "

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${LanguagePack} $(DESC_LanguagePack)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END
;--------------------------------

# Installer functions
Function .onInit
    InitPluginsDir
    # select language
    ;!insertmacro MUI_LANGDLL_DISPLAY
    
    #get the version in from the version date yyyy-mm-dd
    call getVersionInt
    
    ;set the location of the temp folder for this installation
    ;strcpy $TEMP '$INSTDIR\temp'
    
    # getting the mysql database details
    ReadRegStr $MYSQL_DIR HKLM "${REG_HEAD}" "dir_mysql"
    ReadRegStr $DB_NAME   HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $DB_USER   HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $DB_PASS   HKLM "${REG_HEAD}" "db_pass"
    
    # Abort install if already installed or if a newer version is installed
    ReadRegStr $0 HKLM "${REG_HEAD}" "language_pack"
    ${VersionCompare} "$VERSION_INT" "$0" $1
    ${If} $1 == "0"
        MessageBox MB_OK|MB_ICONSTOP "You already have LAMS-LanguagePack-${VERSION} installed"
        Abort
    ${EndIf}    
    ${if} $1 == "2"
        MessageBox MB_OK|MB_ICONSTOP "Your current language pack is a newer version than this version: LAMS-LanguagePack-$0"
        Abort
    ${EndIf}
    
    # Abort if there is no version of LAMS2 installed
    ReadRegStr $0 HKLM "${REG_HEAD}" "version"
    ${If} $0 = ""
        MessageBox MB_OK|MB_ICONSTOP "No version of LAMS 2.x is installed$\n$\n\
                                      Please install LAMS 2 before continuing"
        Abort
    ${EndIf}
    
    #set the installation directory
    ReadRegStr $0 HKLM "${REG_HEAD}" "dir_inst"
    strcpy $LAMS_DIR $0
    strcpy $INSTDIR "$0\jboss-4.0.2\server\default\deploy\lams.ear\lams-dictionary.jar\org\lamsfoundation\lams"
FunctionEnd

Function .onInstSuccess
    RMDir /r "$EXEDIR\zip"
    RMDir /r "$EXEDIR\build"
FunctionEnd

;backup existing language files 
;zip to $INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-dictionary.jar\lams-dictionary-bak.zip
Function zipLanguages
    strcpy $BACKUP_DIR "$LAMS_DIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-dictionary.jar\backup"
    
    detailprint 'Zipping existing files to "$BACKUP_DIR"' 
    
    #zip existing language files
    setoutpath $INSTDIR
    rmdir /r "$BACKUP_DIR"
    createdirectory "$BACKUP_DIR"
    Strcpy $4 '$EXEDIR\zip\7za.exe a -r -tzip "$BACKUP_DIR\lamsDictionaryBak-${VERSION}.zip" "*"'
    nsExec::ExecToStack $4 
    pop $8
    pop $9
    detailprint $8
    detailprint $9
    detailprint 'backupdir: $BACKUP_DIR'
    detailprint 'instdir: $INSTDIR'
    detailprint '$4'
    detailprint 'done'
FunctionEnd

Function getVersionInt
    push ${VERSION}
    push "-"
    push 0
    push 1
    call Strtok
    pop $VERSION_INT
    
    push ${VERSION}
    push "-"
    push 1
    push 1
    call Strtok
    pop $0
    strcpy $VERSION_INT "$VERSION_INT$0"
    
    push ${VERSION}
    push "-"
    push 2
    push 1
    call Strtok
    pop $0
    strcpy $VERSION_INT "$VERSION_INT$0"
FunctionEnd

; copies all the lams_blah project language files from lams_blah/conf/languages
; files are compresses then extracted to the jboss language directory:
; C:\lams\jboss-4.0.2\server\default\deploy\lams.ear\lams-dictionary.jar\org\lamsfoundation\lams
Function copyProjects
    
    ;copying COMMON project language files
    setoutpath "$INSTDIR"
    detailprint "Extracting language files for lams_common"
    file /a "..\..\lams_common\conf\language\*"
    
    ;copying ADMIN project language files
    setoutpath "$INSTDIR\admin"
    detailprint "Extracting language files for lams_admin"
    file /a "..\..\lams_admin\conf\language\*"
    
    ;copying CENTRAL project language files
    setoutpath "$INSTDIR\central"
    detailprint "Extracting language files for lams_central"
    file /a "..\..\lams_central\conf\language\*"
    
    ;copying CONTENTREPOSITORY project language files
    setoutpath "$INSTDIR\contentrepository"
    detailprint "Extracting language files for lams_contentrepository"
    file /a  "..\..\lams_contentrepository\conf\language\*"
    
    ;copying LEARNING project language files
    setoutpath "$INSTDIR\learning"
    detailprint "Extracting language files for lams_learning"
    file /a "..\..\lams_learning\conf\language\*"
     
    ;copying MONITORING project language files
    setoutpath "$INSTDIR\monitoring"
    detailprint "Extracting language files for lams_monitoring"
    file /a  "..\..\lams_monitoring\conf\language\*"
    
    ;copying TOOL_CHAT project language files
    setoutpath "$INSTDIR\tool\chat"
    detailprint "Extracting language files for lams_tool_chat"
    file /a "..\..\lams_tool_chat\conf\language\*"
    
    ;copying TOOL_FORUM project language files
    setoutpath "$INSTDIR\tool\forum"
    detailprint "Extracting language files for lams_tool_forum"
    file /a "..\..\lams_tool_forum\conf\language\*"
    
    ;copying TOOL_LAMC project language files
    setoutpath "$INSTDIR\tool\mc"
    detailprint "Extracting language files for lams_tool_lamc"
    file /a "..\..\lams_tool_lamc\conf\language\*"
    
    ;copying TOOL_LAQA project language files
    setoutpath "$INSTDIR\tool\qa"
    detailprint "Extracting language files for lams_tool_laqa"
    file /a "..\..\lams_tool_laqa\conf\language\*"
    
    ;copying TOOL_NOTEBOOK project language files
    setoutpath "$INSTDIR\tool\notebook"
    detailprint "Extracting language files for lams_tool_notebook"
    file /a "..\..\lams_tool_notebook\conf\language\*"
    
    ;copying TOOL_NB project language files
    setoutpath "$INSTDIR\tool\noticeboard"
    detailprint "Extracting language files for lams_tool_nb"
    file /a "..\..\lams_tool_nb\conf\language\*"
    
    ;copying TOOL_LARSRC project language files
    setoutpath "$INSTDIR\tool\rsrc"
    detailprint "Extracting language files for lams_tool_larsrc"
    file /a "..\..\lams_tool_larsrc\conf\language\*"
    
    ;copying TOOL_SBMT project language files
    setoutpath "$INSTDIR\tool\sbmt"
    detailprint "Extracting language files for lams_tool_sbmt"
    file /a "..\..\lams_tool_sbmt\conf\language\*"
    
    ;copying TOOL_SCRIBE project language files
    setoutpath "$INSTDIR\tool\scribe"
    detailprint "Extracting language files for lams_tool_scribe"
    file /a "..\..\lams_tool_scribe\conf\language\*"
    
    ;copying TOOL_SURVEY project language files
    setoutpath "$INSTDIR\tool\survey"
    detailprint "Extracting language files for lams_tool_survey"
    file /a "..\..\lams_tool_survey\conf\language\*"
    
    ;copying TOOL_VOTE project language files
    setoutpath "$INSTDIR\tool\vote"
    detailprint "Extracting language files for lams_tool_vote"
    file /a "..\..\lams_tool_vote\conf\language\*"
    
FunctionEnd

; copys the files from lams_central/web/flashxml to:
; "<JBOSS>/\server\default\lams.ear\lams-central.war\flashxml
Function copyFlashxml
    strcpy $FLASHXML_DIR "$LAMS_DIR\jboss-4.0.2\server\default\deploy\lams.ear\lams-central.war\flashxml"
    setoutpath $FLASHXML_DIR
    detailprint "Extracting language files for FLASH"
    file /a /r /x "CVS" "..\..\lams_central\web\flashxml\*"
    detailprint "DONE!"  
FunctionEnd


; first, finds the location of the language files in the database
; then copy the required files to the dirname
Function copyllid

    ${CS_FOLDERS->Init}
    ${FS_FOLDERS->Init}
    ${RF_FOLDERS->Init}

    ; getting the rows for Chat and Scribe
    strcpy $SQL_QUERY '"SELECT learning_library_id FROM lams_learning_library WHERE title = $\'Chat and Scribe$\';"'
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $SQL_QUERY'
    strcpy $FOLDER_FLAG "0"
    call executeSQLScript
    pop $0
    detailprint "SQL script result for Chat and Scribe: $\n$0"
    
    ; getting the rows for Forum and Scribe
    strcpy $SQL_QUERY '"SELECT learning_library_id FROM lams_learning_library WHERE title = $\'Forum and Scribe$\';"'
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $SQL_QUERY'
    strcpy $FOLDER_FLAG "1"
    call executeSQLScript
    pop $0
    detailprint "SQL script result for Forum and Scribe: $\n$0"
    
    ; getting the rows for Resources and Forum
    strcpy $SQL_QUERY '"SELECT learning_library_id FROM lams_learning_library WHERE title = $\'Resources and Forum$\';"'
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $SQL_QUERY'
    strcpy $FOLDER_FLAG "2"
    call executeSQLScript
    pop $0
    detailprint "SQL script result for Resource and Forum: $\n$0"
    
    ; copy all the folders for llid Chat and Scribe
    IntOp $R0 "$CS_FOLDERS_UBound" + 1
    ${do}
        ${CS_FOLDERS->Get} $CS_FOLDERS_UBound $R1
        ${CS_FOLDERS->Delete} $CS_FOLDERS_UBound
        IntOp $R0 "$CS_FOLDERS_UBound" + 1
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Chat and Scribe: $R1 $\nElements $R0"
        
        setoutpath "$INSTDIR\library\llid$R1"
        detailprint "Copying language files for chat and scribe"
        file /a "..\..\lams_build\librarypackages\chatscribe\language\*"
        
    ${loopuntil} $R0 == "0"
    
    ; copy all the folders for llid Forum and Scribe
    IntOp $R0 "$FS_FOLDERS_UBound" + 1
    ${do}
        ${FS_FOLDERS->Get} $FS_FOLDERS_UBound $R1
        ${FS_FOLDERS->Delete} $FS_FOLDERS_UBound
        IntOp $R0 "$FS_FOLDERS_UBound" + 1
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Forum and Scribe: $R1 $\nElements $R0"
    
        setoutpath "$INSTDIR\library\llid$R1"
        detailprint "Copying language files for forum and scribe"
        file /a "..\..\lams_build\librarypackages\forumscribe\language\*"
    ${loopuntil} $R0 == "0"
    
    ; copy all the folders for llid Resource and Forum
    IntOp $R0 "$RF_FOLDERS_UBound" + 1
    ${do}
        ${RF_FOLDERS->Get} $RF_FOLDERS_UBound $R1
        ${RF_FOLDERS->Delete} $RF_FOLDERS_UBound
        IntOp $R0 "$RF_FOLDERS_UBound" + 1
        #MessageBox MB_OK|MB_ICONEXCLAMATION "Resource and Forum: $R1 $\nElements $R0"
        setoutpath "$INSTDIR\library\llid$R1"
        detailprint "Copying language files for resource and forum"
        file /a "..\..\lams_build\librarypackages\shareresourcesforum\language\*"
    ${loopuntil} $R0 == "0"
 
FunctionEnd



; Executing sql scripts
; Puts the result of sql script on the stack
Function executeSQLScript
    nsExec::ExecToStack $SQL_QUERY
    detailprint $SQL_QUERY  
    pop $0 
    pop $1
    strcpy $1 $1 -2
    push $1
    
    ; Getting the muliple entries out
    ${while} $1 != ""
        push "$\n"
        push $1
        call SplitFirstStrPart
        pop $R0
        pop $1
        
        ${if} $FOLDER_FLAG == "0"
            ${CS_FOLDERS->Push} $R0
            #MessageBox MB_OK|MB_ICONEXCLAMATION "Chat and scribe: $R0 $\n"
        ${endif} 
        ${if} $FOLDER_FLAG == "1"
            ${FS_FOLDERS->Push} $R0
            #MessageBox MB_OK|MB_ICONEXCLAMATION "Forum and Scribe: $R0"
        ${endif}
        ${if} $FOLDER_FLAG == "2"
            ${RF_FOLDERS->Push} $R0
            #MessageBox MB_OK|MB_ICONEXCLAMATION "Resource and Forum: $R0"
        ${endif} 
    ${endwhile}
    
    
    
    #check for errors and write result to install window
    ${if} $0 != 0 
        goto Errors
    ${endif}
    
    goto Finish
    
    Errors:
        DetailPrint "Can't read from $MYSQL_DIR\$DB_NAME database"
    Finish:
        clearerrors
    
FunctionEnd

Function SplitFirstStrPart
  Exch $R0
  Exch
  Exch $R1
  Push $R2
  Push $R3
  StrCpy $R3 $R1
  StrLen $R1 $R0
  IntOp $R1 $R1 + 1
  loop:
    IntOp $R1 $R1 - 1
    StrCpy $R2 $R0 1 -$R1
    StrCmp $R1 0 exit0
    StrCmp $R2 $R3 exit1 loop
  exit0:
  StrCpy $R1 ""
  Goto exit2
  exit1:
    IntOp $R1 $R1 - 1
    StrCmp $R1 0 0 +3
     StrCpy $R2 ""
     Goto +2
    StrCpy $R2 $R0 "" -$R1
    IntOp $R1 $R1 + 1
    StrCpy $R0 $R0 -$R1
    StrCpy $R1 $R2
  exit2:
  Pop $R3
  Pop $R2
  Exch $R1 ;rest
  Exch
  Exch $R0 ;first
FunctionEnd

Function updateDatase
    SetOutPath $INSTDIR
    file /a "languages.txt"
    
    Fileopen $R0 "$INSTDIR\languages.txt" r
    Fileread $R0 $R1
    ${while} $R1 != ""
        
        push "-"
        push $R1
        call SplitFirstStrPart
        pop $0
        pop $R1
        
        push "-"
        push $R1
        call SplitFirstStrPart
        pop $1
        pop $R1
        
        push "-"
        push $R1
        call SplitFirstStrPart
        pop $2
        pop $R1
        
        push "-"
        push $R1
        call SplitFirstStrPart
        pop $3
        pop $R1
        
        Fileread $R0 $R1
        strlen $8 R1
        ${if} $8 > 3
            strcpy $3 $3 -2
        ${endif}

        strcpy $9 'select count(*) from lams_supported_locale where language_iso_code = \"$0\" and country_iso_code = \"$1\"'
        strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B "$DB_NAME" -e "$9"'
        nsExec::ExecToStack $SQL_QUERY
        
        pop $4
        pop $5
        strcpy $5 $5 -2
        
        Detailprint "RESULT: $4"
        Detailprint "OUTPUT: $5"
        Detailprint 'TEXT: $0 - $1 - $2 - $3 '
        Detailprint 'QUERY: $SQL_QUERY'
        ${if} $5 == 0
            Detailprint "INSERTING ROWS!"
            strcpy $9 "insert into lams_supported_locale(language_iso_code, country_iso_code, description, direction) values ($\'$0$\',$\'$1$\',$\'$2$\',$\'$3$\')"
            strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B "$DB_NAME" -e "$9"'
            nsExec::ExecToStack $SQL_QUERY
            pop $7
            pop $8
            Detailprint 'QUERY: $SQL_QUERY'
            Detailprint "INSERT RESULT: $7"
            Detailprint "INSERT OUTPUT: $8"
        ${endif} 
    ${endwhile} 
    

    /*
    ; get the procedure scripts required
    setoutpath "$INSTDIR"
    File /a insertlocale.sql
    File /a LanguagePack.xml
    File /r "..\apache-ant-1.6.5"
    #File /a "..\apache-ant-1.6.5\bin\ant.bat"
    #File /a "..\apache-ant-1.6.5\lib\ant.jar"
    
    
    ; update locals must be stored as a procedure first
    ; nsExec wont let me do "mysql < insertLocale.sql"  so i had to use ant
    /*
    detailprint "Attempting to store procedure"
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -B $DB_NAME \< "$INSTDIR\sqlscripts\insertLocale.sql"'
    detailprint $SQL_QUERY
    nsExec::ExecToStack $SQL_QUERY
    pop $0
    pop $1
    ${If} $0 == 0
        DetailPrint "Procedure successfully stored in database"
    ${Else}
        DetailPrint "ERROR: DB001 - Unable to store procedure: $1" 
    ${EndIf} 
    */
    
    /*
    ; create installer.properties
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
    FileWrite $0 "INSTDIR=$2/$\r$\n"
    ;FileWrite $0 "URL=http://$LAMS_DOMAIN:$LAMS_PORT/lams/$\r$\n"
    FileWrite $0 "DB_NAME=$DB_NAME$\r$\n"
    FileWrite $0 "DB_USER=$DB_USER$\r$\n"
    FileWrite $0 "DB_PASS=$DB_PASS$\r$\n"
    Push $LAMS_DIR
    Push "\"
    Call StrSlash
    Pop $2
    FileWrite $0 "EARDIR=$2/jboss-4.0.2/server/default/deploy/lams.ear$\r$\n"

    ; For debugging purposes
    copyfiles "$TEMP\installer.properties" $INSTDIR
    
    SetOutPath $TEMP
    File /a "LanguagePack.xml"
    
    
    ; update locals must be stored as a procedure first
    ; use ANT to store procedures
    DetailPrint '$INSTDIR\apache-ant-1.6.5\bin\ant.bat insertLocale-db'
    nsExec::ExecToStack '$INSTDIR\apache-ant-1.6.5\bin\ant.bat -buildfile $TEMP\LanguagePack.xml execute-sql'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    DetailPrint "Database insert status: $0"
    DetailPrint "Database insert output: $1"
    

    
    ; execute the procedures
    detailprint "Attempting to execute database procedure"
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B "$DB_NAME" -e "call updateLocale()"'
    detailprint $SQL_QUERY
    Execwait $SQL_QUERY
    pop $0
    pop $1
    ${If} $0 == 0
        DetailPrint "Procedure successfully exectuted"
    ${Else}
        DetailPrint "ERROR: DB002 - Unable to execute procedure: $1" 
    ${EndIf}
    
    ; remove the procedures
    detailprint "Attempting to remove the procedure from the Database"
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B "$DB_NAME" -e "DROP PROCEDURE IF EXISTS updateLocales"'
    detailprint $SQL_QUERY
    nsExec::ExecToStack $SQL_QUERY
    pop $0
    pop $1
    ${If} $0 == 0
        DetailPrint "Procedure successfully removed from database"
    ${Else}
        DetailPrint "ERROR: DB003 - Unable to remove procedure: $1" 
    ${EndIf} 
    
    ; remove the procedures
    detailprint "Attempting to remove the procedure from the Database"
    strcpy $SQL_QUERY '"$MYSQL_DIRbin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B "$DB_NAME" -e "DROP PROCEDURE IF EXISTS insertlocale"'
    detailprint $SQL_QUERY
    nsExec::ExecToStack $SQL_QUERY
    pop $0
    pop $1
    ${If} $0 == 0
        DetailPrint "Procedure successfully removed from database"
    ${Else}
        DetailPrint "ERROR: DB003 - Unable to remove procedure: $1" 
    ${EndIf} 
    
    
    
    goto done
    error:
        DetailPrint "Ant configure-deploy failed."
        MessageBox MB_OK|MB_ICONSTOP "LAMS configuration failed.  Please check your LAMS configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        #Abort "LAMS configuration failed."
    
    done: 
        ; remove the sql scripts
        delete "$INSTDIR\insertlocale.sql"
        delete "LanguagePack.xml"
        rmdir /r $TEMP
        rmdir /r "$INSTDIR\apache-ant-1.6.5"
    */
FunctionEnd


