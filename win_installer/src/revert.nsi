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
Name "LAMS Revert Utility"
BrandingText "LAMS Revert Utility"
OutFile "..\build\revert-lams.exe"
InstallDir "C:\lams"
Icon ..\graphics\lams2.ico
InstallDirRegKey HKLM "${REG_HEAD}" ""
VIProductVersion 2.0.0.0
VIAddVersionKey ProductName "LAMS Revert Utility"
VIAddVersionKey ProductVersion "1.0"
VIAddVersionKey CompanyName "LAMS International"
VIAddVersionKey CompanyWebsite "lamscommunity.org"
VIAddVersionKey FileVersion ""
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright ""

!define MUI_FINISHPAGE_TEXT "LAMS has been successfully reverted up on your computer" 

# set warning when cancelling install
!define MUI_ABORTWARNING
Page custom PreRevertDir PostRevertDir
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_LANGUAGE "English"


# variables
Var DB_NAME
Var DB_USER
Var DB_PASS
Var REPOSITORY_DIR
Var VERSION
Var MYSQL_DIR
Var BACKUP_DIR
Var SERVER_VERSION
Var LANG_VERSION
Var LANG_VERSION_INT

ReserveFile "revert-dir.ini"

Section Revert
    ;make the section compulsory
    SectionIn RO
    
    ; getting the required settings to revert from the register
    call readRegister
    
    call revertFiles
    
    detailprint "Reverting back to old database"
    call revertDatabase
    
    detailprint "Updating registry with current values"
    call updateRegistry 
SectionEnd

Function .onInit
    checklams:
        nsExec::ExecToStack "$1\bin\java LocalPortScanner $0"
        Pop $2
        ${If} $2 == 2
            MessageBox MB_YESNOCANCEL|MB_ICONQUESTION "LAMS appears to be running. Do you wish to continue with lams running? $\r$\n$\r$\nClick yes to continue or no to stop lams (will take a few seconds)" \
                IDNO stoplams \
                IDCANCEL quitrevert
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
    quitrevert:
        Delete "$TEMP\LocalPortScanner.class"
        Abort
    continue:
        Delete "$TEMP\LocalPortScanner.class"
    
    ReadRegStr $INSTDIR HKLM "${REG_HEAD}" "dir_inst"
    ${if} $INSTDIR == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Could not find LAMS installation in registry"
        Abort
    ${endif}
    
    ReadRegStr $DB_NAME HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $DB_USER HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $DB_PASS HKLM "${REG_HEAD}" "db_pass"
    ReadRegStr $MYSQL_DIR HKLM "${REG_HEAD}" "dir_mysql"
    call CheckMySQL

    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "revert-dir.ini"
FunctionEnd

Function PreRevertDir
    !insertmacro MUI_INSTALLOPTIONS_WRITE "revert-dir.ini" "Field 1" "State" "$BACKUP_DIR"
    !insertmacro MUI_HEADER_TEXT "Revert to Earlier LAMS Installation" "Enter the directory LAMS was backed up to and the reverter will do the rest"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "revert-dir.ini"
FunctionEnd

Function PostRevertDir
    !insertmacro MUI_INSTALLOPTIONS_READ $BACKUP_DIR "revert-dir.ini" "Field 2" "State"
    ${if} $BACKUP_DIR == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "The directory entered is not valid: $\r$\n$BACKUP_DIR"
        Abort
    ${endif}
    
    iffileexists $BACKUP_DIR continue notexists 
    iffileexists "$BACKUP_DIR\jboss-4.0.2" continue notexists 
    goto continue
    notexists:
          MessageBox MB_OK|MB_ICONEXCLAMATION "Could not find a LAMS backup: $\r$\n$BACKUP_DIR"
    continue:
FunctionEnd

Function CheckMySQL    
    # Checking if the given database name already exists in the mysql database list
    ifFileExists "$MYSQL_DIR\data\$DB_NAME\*.*" continue NoDatabaseNameExists
    NoDatabaseNameExists:
        MessageBox MB_OK|MB_ICONSTOP "Could not find database $DB_NAME. Please check your database settings and try again"
        quit   
    continue:
    
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

Function readRegister
    ReadRegStr $INSTDIR HKLM "${REG_HEAD}" "dir_inst"
    ReadRegStr $DB_NAME HKLM "${REG_HEAD}" "db_name"
    ReadRegStr $DB_USER HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $DB_PASS HKLM "${REG_HEAD}" "db_pass"
    ReadRegStr $MYSQL_DIR HKLM "${REG_HEAD}" "dir_mysql"
    ReadRegStr $REPOSITORY_DIR HKLM "${REG_HEAD}" "dir_repository"
FunctionEnd

Function revertfiles
    rmdir "$INSTDIR\jboss-4.0.2"
    rmdir "$REPOSITORY_DIR"
    rmdir "$INSTDIR\dump"
    detailprint "Copying files back to $INSTDIR"
    createdirectory $REPOSITORY_DIR
    
    DetailPrint "Reverting LAMS files. This may take a few minutes"
    SetDetailsPrint listonly
    copyfiles /SILENT "$BACKUP_DIR\repository\*" $REPOSITORY_DIR
    copyfiles /SILENT "$BACKUP_DIR\*" "$INSTDIR"
    SetDetailsPrint listonly
Functionend

Function revertDatabase
    StrCpy $0 '"$MYSQL_DIR\bin\mysql" -u$DB_USER -p$DB_PASS -e'
    
    StrCpy $1 '$0 "SET FOREIGN_KEY_CHECKS=0"'
    DetailPrint $1
    nsExec::ExecToStack $1
    Pop $3
    Pop $4
    ${If} $3 == 1
        detailprint "Problem setting foreign key checks to 0"
        goto error
    ${EndIf}
    
    Detailprint "Removing LAMS database"
    StrCpy $1 '$0 "drop database if exists $DB_NAME"'
    DetailPrint $1
    nsExec::ExecToStack $1
    Pop $3
    Pop $4
    ${If} $3 == 1
        detailprint "Problem deleting database"
        goto error
    ${EndIf}
    
    Detailprint "Creating LAMS database"
    StrCpy $1 '$0 "create database $DB_NAME default character set utf8"'
    DetailPrint $1
    nsExec::ExecToStack $1
    Pop $3
    Pop $4
    ${If} $3 == 1
        detailprint "Problem creating database"
        goto error
    ${EndIf}
    
    StrCpy $1 '$0 "SET FOREIGN_KEY_CHECKS=1"'
    DetailPrint $1
    nsExec::ExecToStack $1
    Pop $3
    Pop $4
    ${If} $3 == 1
        detailprint "Problem setting foreign key checks to 1"
        goto error
    ${EndIf}
    
    
    # extract ant
    SetOutPath $TEMP
    File /r "..\apache-ant-1.6.5"
    File "..\templates\revert.xml"

    # create installer.properties
    ClearErrors
    FileOpen $0 "$TEMP\revert.properties" w
    IfErrors error +1
   
        
    # convert '\' to '/' for Ant's benefit
    Push "$INSTDIR\jboss-4.0.2\server\default\deploy\lams.ear"
    Push "\"
    Call StrSlash
    Pop $2
    
    Push "$BACKUP_DIR"
    Push "\"
    Call StrSlash
    Pop $3
    
    filewrite $0 "EARDIR=$2$\r$\n"
    IfErrors error +1
    
    filewrite $0 "BACKUP_DIR=$3$\r$\n"
    IfErrors error +1
    
    filewrite $0 "DB_NAME=$DB_NAME$\r$\n"
    IfErrors error +1
    
    filewrite $0 "DB_USER=$DB_USER$\r$\n"
    IfErrors error +1
    
    filewrite $0 "DB_PASS=$DB_PASS$\r$\n"
    IfErrors error +1
    
    FileClose $0
    IfErrors error +1

    

    strcpy $5 "$TEMP\apache-ant-1.6.5\bin\ant.bat -buildfile $TEMP\revert.xml -logfile $INSTDIR\revert.log revert-db"
    detailprint $5
    nsExec::ExecToStack $5
    Pop $3
    Pop $4
    ${If} $3 == 1
        detailprint "Problem reverting database"
        goto error
    ${EndIf}
    
    goto done
    error:
        MessageBox MB_OK|MB_ICONSTOP "Database setup failed.  Please check your MySQL configuration and try again.$\r$\nError:$\r$\n$\r$\n$4"
        rmdir "$TEMP\apache-ant-1.6.5"
        delete "$TEMP\revert.xml"
        delete "$TEMP\revert.properties"
        Abort "Database setup failed."
    done:
        rmdir "$TEMP\apache-ant-1.6.5"
        delete "$TEMP\revert.xml"
        delete "$TEMP\revert.properties"
Functionend

Function updateRegistry
    strcpy $0 '"SELECT config_value FROM lams_configuration WHERE config_key = $\'Version$\';"'
    strcpy $0 '"$MYSQL_DIR\bin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $0'
    nsExec::ExecToStack $0
    pop $1
    pop $VERSION
    ${If} $0 == 1
        detailprint "Problem reading database"
        goto error
    ${EndIf}
    
    strcpy $0 '"SELECT config_value FROM lams_configuration WHERE config_key = $\'ServerVersionNumber$\';"'
    strcpy $0 '"$MYSQL_DIR\bin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $0'
    nsExec::ExecToStack $0
    pop $1
    pop $SERVER_VERSION
    ${If} $0 == 1
        detailprint "Problem reading database"
        goto error
    ${EndIf}
    
    
    strcpy $0 '"SELECT config_value FROM lams_configuration WHERE config_key = $\'DictionaryDateCreated$\';"'
    strcpy $0 '"$MYSQL_DIR\bin\mysql.exe" -u"$DB_USER" -p"$DB_PASS" -s -i -B $DB_NAME -e $0'
    nsExec::ExecToStack $0
    pop $1
    pop $LANG_VERSION
    ${If} $0 == 1
        detailprint "Problem reading database"
        goto error
    ${EndIf}
    
    call getVersionInt

    
    strcpy $VERSION $VERSION -2
    strcpy $LANG_VERSION_INT $LANG_VERSION_INT -2
    strcpy $SERVER_VERSION $SERVER_VERSION -2
    
    WriteRegStr HKLM "${REG_HEAD}" "version" $VERSION
    WriteRegStr HKLM "${REG_HEAD}" "language_pack" $LANG_VERSION_INT
    WriteRegStr HKLM "${REG_HEAD}" "server_version" $SERVER_VERSION
    
    goto done
    error:
        MessageBox MB_OK|MB_ICONSTOP "Registryy setup failed.  Please check your MySQL configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
    done:    
Functionend

; Convert version string into a integer
Function getVersionInt
    push $LANG_VERSION
    push "-"
    push 0
    push 1
    call Strtok
    pop $LANG_VERSION_INT
    
    push $LANG_VERSION
    push "-"
    push 1
    push 1
    call Strtok
    pop $0
    strcpy $LANG_VERSION_INT "$LANG_VERSION_INT$0"
    
    push $LANG_VERSION
    push "-"
    push 2
    push 1
    call Strtok
    pop $0
    strcpy $LANG_VERSION_INT "$LANG_VERSION_INT$0"
    
FunctionEnd


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

!define StrTok "!insertmacro StrTok"
 
!macro StrTok ResultVar String Separators ResultPart SkipEmptyParts
  Push "${String}"
  Push "${Separators}"
  Push "${ResultPart}"
  Push "${SkipEmptyParts}"
  Call StrTok
  Pop "${ResultVar}"
!macroend
 
Function StrTok
/*After this point:
  ------------------------------------------
  $0 = SkipEmptyParts (input)
  $1 = ResultPart (input)
  $2 = Separators (input)
  $3 = String (input)
  $4 = SeparatorsLen (temp)
  $5 = StrLen (temp)
  $6 = StartCharPos (temp)
  $7 = TempStr (temp)
  $8 = CurrentLoop
  $9 = CurrentSepChar
  $R0 = CurrentSepCharNum
  */
 
  ;Get input from user
  Exch $0
  Exch
  Exch $1
  Exch
  Exch 2
  Exch $2
  Exch 2
  Exch 3
  Exch $3
  Exch 3
  Push $4
  Push $5
  Push $6
  Push $7
  Push $8
  Push $9
  Push $R0
 
  ;Parameter defaults
  ${IfThen} $2 == `` ${|} StrCpy $2 `|` ${|}
  ${IfThen} $1 == `` ${|} StrCpy $1 `L` ${|}
  ${IfThen} $0 == `` ${|} StrCpy $0 `0` ${|}
 
  ;Get "String" and "Separators" length
  StrLen $4 $2
  StrLen $5 $3
  ;Start "StartCharPos" and "ResultPart" counters
  StrCpy $6 0
  StrCpy $8 -1
 
  ;Loop until "ResultPart" is met, "Separators" is found or
  ;"String" reaches its end
  ResultPartLoop: ;"CurrentLoop" Loop
 
  ;Increase "CurrentLoop" counter
  IntOp $8 $8 + 1
 
  StrSearchLoop:
  ${Do} ;"String" Loop
    ;Remove everything before and after the searched part ("TempStr")
    StrCpy $7 $3 1 $6
 
    ;Verify if it's the "String" end
    ${If} $6 >= $5
      ;If "CurrentLoop" is what the user wants, remove the part
      ;after "TempStr" and itself and get out of here
      ${If} $8 == $1
      ${OrIf} $1 == `L`
        StrCpy $3 $3 $6
      ${Else} ;If not, empty "String" and get out of here
        StrCpy $3 ``
      ${EndIf}
      StrCpy $R0 `End`
      ${ExitDo}
    ${EndIf}
 
    ;Start "CurrentSepCharNum" counter (for "Separators" Loop)
    StrCpy $R0 0
 
    ${Do} ;"Separators" Loop
      ;Use one "Separators" character at a time
      ${If} $R0 <> 0
        StrCpy $9 $2 1 $R0
      ${Else}
        StrCpy $9 $2 1
      ${EndIf}
 
      ;Go to the next "String" char if it's "Separators" end
      ${IfThen} $R0 >= $4 ${|} ${ExitDo} ${|}
 
      ;Or, if "TempStr" equals "CurrentSepChar", then...
      ${If} $7 == $9
        StrCpy $7 $3 $6
 
        ;If "String" is empty because this result part doesn't
        ;contain data, verify if "SkipEmptyParts" is activated,
        ;so we don't return the output to user yet
 
        ${If} $7 == ``
        ${AndIf} $0 = 1 ;${TRUE}
          IntOp $6 $6 + 1
          StrCpy $3 $3 `` $6
          StrCpy $6 0
          Goto StrSearchLoop
        ${ElseIf} $8 == $1
          StrCpy $3 $3 $6
          StrCpy $R0 "End"
          ${ExitDo}
        ${EndIf} ;If not, go to the next result part
        IntOp $6 $6 + 1
        StrCpy $3 $3 `` $6
        StrCpy $6 0
        Goto ResultPartLoop
      ${EndIf}
 
      ;Increase "CurrentSepCharNum" counter
      IntOp $R0 $R0 + 1
    ${Loop}
    ${IfThen} $R0 == "End" ${|} ${ExitDo} ${|}
          
    ;Increase "StartCharPos" counter
    IntOp $6 $6 + 1
  ${Loop}
 
/*After this point:
  ------------------------------------------
  $3 = ResultVar (output)*/
 
  ;Return output to user
 
  Pop $R0
  Pop $9
  Pop $8
  Pop $7
  Pop $6
  Pop $5
  Pop $4
  Pop $0
  Pop $1
  Pop $2
  Exch $3
FunctionEnd

