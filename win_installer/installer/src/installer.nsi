/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * along with this program; ifnot, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
# includes
!include "TextFunc.nsh"
!include "includes\Functions.nsh"
!include "MUI.nsh"
!include "LogicLib.nsh"
!include "x64.nsh"
!include "Ports.nsh"

# functions from TextFunc.nsh
!insertmacro FileJoin
!insertmacro LineFind
# constants
!define VERSION "2.4.0"
!define LANGUAGE_PACK_VERSION "2012-04-09"
!define LANGUAGE_PACK_VERSION_INT "20120409"
!define DATE_TIME_STAMP "201204051000"
!define SERVER_VERSION_NUMBER "2.4.0.201204091000"
!define BASE_VERSION "2.4"
!define JBOSS_VERSION "5.1"
!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
!define LAMS_PACKAGE_DIR "..\..\lams-package\"
!define INSTALLERTYPE "allinone"
!define LAMS24_7Z "lams2.4.7z"
!define BASE_DEV_DIR "..\..\"
!define BUILD "..\..\build\"
!define DATABASE "..\..\database\"
!define DEFAULT_REPOSITORY "..\..\repository\"
# installer settings
!define MUI_ICON "..\graphics\lams2.ico"
!define MUI_UNICON "..\graphics\lams2.ico"
Name "LAMS ${VERSION}"
;BrandingText "LAMS ${VERSION} -- built on ${__TIMESTAMP__}"
BrandingText "LAMS ${VERSION} -- built on ${__DATE__} ${__TIME__}"
OutFile "${BUILD}\LAMS-2.4.exe"
;InstallDir "C:\lams"
InstallDir "$LOCALAPPDATA\lams"
InstallDirRegKey HKCU "${REG_HEAD}" ""
LicenseForceSelection radiobuttons "I agree" "I do not agree" 
InstProgressFlags smooth
RequestExecutionLevel user
VIProductVersion 2.4.0.0
VIAddVersionKey ProductName "LAMS 2.4 Installer"
VIAddVersionKey ProductVersion "2.4.0"
VIAddVersionKey CompanyName "LAMS Foundation"
VIAddVersionKey CompanyWebsite "lamsfoundation.org"
VIAddVersionKey FileVersion ""
VIAddVersionKey FileDescription ""
VIAddVersionKey LegalCopyright "GNU General Public License, version 2"
/* 
In the future if we want to install LAMS as service, then enable this option to 
ensure that the user is admin before he/she tries to install LAMS
RequestExecutionLevel admin ;Require admin rights on NT6+ (When UAC is turned on)
*/
# set warning when cancelling install
!define MUI_ABORTWARNING
# set welcome page
!define MUI_WELCOMEPAGE_TITLE "LAMS ${VERSION} Install Wizard"
!define MUI_WELCOMEPAGE_TEXT "This wizard will guide you through the installation of LAMS ${VERSION}.\r\n\r\n\ 
    Click Next to continue."
# set instfiles page to wait when done
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
# display finish page stuff
!define MUI_FINISHPAGE_RUN $INSTDIR\lams-start.exe
!define MUI_FINISHPAGE_RUN_TEXT "Start LAMS now"
!define MUI_FINISHPAGE_SHOWREADME $INSTDIR\readme.txt
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Open the readme file"
!define MUI_FINISHPAGE_LINK "Visit LAMS Community"
!define MUI_FINISHPAGE_LINK_LOCATION "http://lamscommunity.org"
# installer screen progression
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "..\documents\license.txt"
;!insertmacro MUI_PAGE_COMPONENTS
;!define MUI_PAGE_CUSTOMFUNCTION_LEAVE DirectoryLeave
;!insertmacro MUI_PAGE_DIRECTORY
Page custom PreLAMS2Config PostLAMS2Config
Page custom PreFinal PostFinal
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
# uninstaller screens
!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH
# supported translations
!insertmacro MUI_LANGUAGE "English" # first language is the default language
# reserve files
#
ReserveFile "lams.ini"
ReserveFile "lams2.ini"
ReserveFile "mysql.ini"
ReserveFile "wildfire.ini"
ReserveFile "final.ini"
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
!insertmacro MUI_RESERVEFILE_LANGDLL
# variables
Var LAMS_DOMAIN         ; server URL for lams
Var LAMS_PORT           ; PORT for lams usually 8080
Var LAMS_LOCALE         ; default language locale on startup
Var LAMS_USER           ; user name for lams system administrator
Var LAMS_PASS           ; password for lams system administrator
Var WINTEMP             ; temp dir
Var TIMESTAMP           ; timestamp
##############################
SectionGroup /e "LAMS ${VERSION} Full Install" fullInstall
    Section "LAMS ${VERSION}" lams
		AddSize 365000 ; specify the LAMS full install size 
        SectionIn RO
        Detailprint "Installing LAMS ${VERSION}"
		
		Call PrepareInst
        Call DeployConfig
		Call ConfigureDatabase
		Call WriteRegEntries	
        SetOutPath $INSTDIR
        File /a "${BUILD}\lams-start.exe"
        File /a "${BUILD}\lams-stop.exe"
        File /a "..\documents\license.txt"
        File /a "..\documents\readme.txt"		
        Call SetupStartMenu
		
        WriteUninstaller "$INSTDIR\lams-uninstall.exe"
		
    SectionEnd
/*  
	No longer register LAMS as service
	Section /O "Install as Service (optional)" service
		# checks user account type 
		UserInfo::GetAccountType
		pop $0		
		${If} $0 != "admin" 
			SectionIn RO
		${EndIf}
		
		DetailPrint "Setting up lams ${VERSION} as a service."
	
		Call RegisterLAMSService
		
		
	SectionEnd
*/	
SectionGroupEnd
# functions
#
Function .onInit
/*
	${If} $ISADMIN != "admin" ;Require admin rights on NT4+
		MessageBox mb_iconstop "Administrator rights required!"
		SetErrorLevel 740 ;ERROR_ELEVATION_REQUIRED
		Abort
	${EndIf}	
*/	
    # Checking to see ifLAMS is installed 
    call checkRegistry
    # extract custom page display config
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams2.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "mysql.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "wildfire.ini"
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "final.ini"
    
FunctionEnd
################################################################################
# USER INTERFACE CODE                                                          #
################################################################################
Function checkRegistry
    # Check the current version installed (if any)
    ReadRegStr $0 HKCU "${REG_HEAD}" "version" 
    
    ${if} $0 == ""
    ${else}
        MessageBox MB_OK|MB_ICONSTOP "You already have LAMS $0 Installed on your computer. You must uninstall before continuing or get the appropriate upgrader."
        Abort
    ${endif}
	
    # Check if previous versions are installed
    ReadRegStr $0 HKLM "${REG_HEAD}" "version" 
    
    ${if} $0 == ""
    ${else}
        MessageBox MB_OK|MB_ICONSTOP "You have LAMS $0 Installed on your computer."
        Abort
    ${endif}	
FunctionEnd
Function DirectoryLeave
/*
    # check for spaces in instdir
    ${StrStr2} $0 $INSTDIR " "
    ${If} $0 != ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "Please choose a location without a space."
        Abort
    ${EndIf}
*/    
FunctionEnd
Function PreLAMS2Config
        !insertmacro MUI_HEADER_TEXT "Setting Up LAMS" "Configure the LAMS Server, and choose an admin username and password."
        !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams2.ini"
FunctionEnd
Function PostLAMS2Config
	!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_DOMAIN "lams2.ini" "Field 8" "State"
	!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_PORT "lams2.ini" "Field 9" "State"
	!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_LOCALE "lams2.ini" "Field 12" "State"
	!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_USER "lams2.ini" "Field 2" "State"
	!insertmacro MUI_INSTALLOPTIONS_READ $LAMS_PASS "lams2.ini" "Field 5" "State"
	
	# check that there's no other app running on that port

	${If} ${TCPPortOpen} $LAMS_PORT
		MessageBox MB_OK "The port $LAMS_PORT is in used by another application. Please choose another one (ie: 8181)"
		Abort
	${EndIf}	
 		
FunctionEnd
Function PreFinal
    !insertmacro MUI_INSTALLOPTIONS_WRITE "final.ini" "Field 2" "Text" "Click 'Install' to commence installation of LAMS ${VERSION}"  
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
    
FunctionEnd
################################################################################
# END USER INTERFACE CODE                                                      #
################################################################################
################################################################################
# CODE USED FOR INSTALLER                                                      #
################################################################################    
Function PrepareInst
		Detailprint "Creating installation directory $INSTDIR"
        SetOutPath $INSTDIR
        CreateDirectory "$INSTDIR"
		
		#unzip lams -package files
		setoutpath "$INSTDIR"
		Detailprint "Installing LAMS ${VERSION}"
		File ${LAMS_PACKAGE_DIR}${LAMS24_7Z}
		File /r "${BASE_DEV_DIR}\zip"
		
		DetailPrint 'Extracting LAMS ${VERSION}: this might take a little while...'
		
		strcpy $4 '$INSTDIR\zip\7za.exe x -aoa ${LAMS24_7Z}'
		nsExec::ExecToStack $4
		pop $5
		pop $6
		FileWrite $R0 "$6$\n"
		${if} $5 != 0
			Detailprint  "7za Unzip error: $\r$\n$\r$\n$6"
			MessageBox MB_OK|MB_ICONSTOP "7za Unzip error:$\r$\nError:$\r$\n$\r$\n$6"
			Abort "Lams configuration failed"
		${endif}
FunctionEnd
Function DeployConfig
	DetailPrint 'Configuring LAMS with new settings'
    # extract support files to write configuration
    SetOutPath $INSTDIR
    File /r "${BASE_DEV_DIR}\apache-ant-1.6.5"
    File /r "${BASE_DEV_DIR}\zip"
		
    SetOutPath $TEMP
    File "build.xml"
    File "..\templates\mysql-ds.xml"
    File "..\templates\server.xml"
    File "..\templates\run.bat"
	File "..\templates\shutdown.bat"
	File "..\templates\service.bat"
    File "..\templates\service64.bat"
    File "..\templates\index.html"
    File "..\templates\update_lams_configuration.sql"
	File "..\templates\update-script.bat"	
      
    # create installer.properties
    ClearErrors
    FileOpen $0 $TEMP\installer.properties w
    IfErrors 0 +2
        goto error
        
    # convert '\' to '\\' for Ant's benefit
    Push $TEMP
    Push "\"
    Call StrSlash2
    Pop $2
    FileWrite $0 "TEMP=$2$\r$\n"
            
    Push $INSTDIR
    Push "\"
    Call StrSlash2
    Pop $2
	
    FileWrite $0 "URL=http://$LAMS_DOMAIN:$LAMS_PORT/lams/$\r$\n"
    FileWrite $0 "INSTDIR=$2\\\\$\r$\n"
	FileWrite $0 "JREDIR=$2\\\\jre$\r$\n"
	FileWrite $0 "MYSQLDIR=$2\\\\data\\\\db$\r$\n"
    FileWrite $0 "TEMPDIR=$2\\\\temp$\r$\n"
    FileWrite $0 "DUMPDIR=$2\\\\dump$\r$\n"
    FileWrite $0 "EARDIR=$2\\\\jboss-${JBOSS_VERSION}\\\\server\\\\default\\\\deploy\\\\lams.ear$\r$\n"
    FileWrite $0 "DEPLOYDIR=$2\\\\jboss-${JBOSS_VERSION}\\\\server\\\\default\\\\deploy$\r$\n"
    FileWrite $0 "TOMCATDIR=$2\\\\jboss-${JBOSS_VERSION}\\\\server\\\\default\\\\deploy\\\\jbossweb.sar$\r$\n"
    FileWrite $0 "BINDIR=$2\\\\jboss-${JBOSS_VERSION}\\\\bin$\r$\n"
    FileWrite $0 "REPOSITORYDIR=$2\\\\data\\\\repository$\r$\n"   
    StrCpy $LAMS_LOCALE $LAMS_LOCALE 5
    FileWrite $0 "LOCALE=$LAMS_LOCALE$\r$\n"
    ${If} $LAMS_LOCALE == "ar_JO"
        FileWrite $0 "LOCALE_DIRECTION=RTL$\r$\n"
    ${Else}
        FileWrite $0 "LOCALE_DIRECTION=LTR$\r$\n"
    ${EndIf}
      
    FileWrite $0 "LAMS_PORT=$LAMS_PORT$\r$\n"
    FileWrite $0 "LAMS_USER=$LAMS_USER$\r$\n"
    FileWrite $0 "LAMS_PASS=$LAMS_PASS$\r$\n"
    FileClose $0
    # for debugging purposes
    CopyFiles "$TEMP\installer.properties" $INSTDIR
        
    # use Ant to write config to files
	ClearErrors
    FileOpen $0 "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" w
    IfErrors 0 +2
        goto error
	
    FileWrite $0 "@echo off$\r$\nset JAVACMD=$2\jre\bin\java$\r$\nset JAVA_HOME=$2\jre$\r$\n"
    FileClose $0
    ${FileJoin} "$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" "$INSTDIR\apache-ant-1.6.5\bin\ant.bat" ""
    DetailPrint '"$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat" -buildfile "$TEMP\build.xml" configure-deploy'
   
    nsExec::ExecToStack '"$INSTDIR\apache-ant-1.6.5\bin\newAnt.bat"  -buildfile "$TEMP\build.xml" configure-deploy'
    Pop $0 ; return code, 0=success, error=fail
    Pop $1 ; console output
    CreateDirectory "$INSTDIR\install_logs"
    
    FileOpen $R0 "$INSTDIR\install_logs\installer_ant.log" w
    IfErrors 0 +2
        goto error
    FileWrite $R0 $1
    FileClose $R0	
	
    ${If} $0 == "error"
		goto error
    ${EndIf}
    goto done
	
    /*
	${StrStr2} $0 $1 "BUILD SUCCESSFUL"
    ${If} $0 == ""
        goto error
    ${EndIf}
	*/
	
    error:
        DetailPrint "Ant configure-deploy failed."
        MessageBox MB_OK|MB_ICONSTOP "LAMS configuration failed.  Please check your LAMS configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "LAMS configuration failed."
    done:
FunctionEnd
Function ConfigureDatabase
    DetailPrint "Updating Database configuration."
	MessageBox MB_OK|MB_ICONEXCLAMATION "In order to finishing configuring LAMS, make sure that you unblock or allow any services when your Windows system requests it."
    nsExec::ExecToStack '$INSTDIR\jboss-${JBOSS_VERSION}\bin\update-script.bat'
	
   ${If} $0 == "error"
		goto error
    ${EndIf}
    goto done
	
    error:
        DetailPrint "Updating database configuration failed."
        MessageBox MB_OK|MB_ICONSTOP "LAMS configuration failed.  Please check your LAMS configuration and try again.$\r$\nError:$\r$\n$\r$\n$1"
        Abort "LAMS configuration failed."
    done:	
FunctionEnd 
Function RegisterLAMSService
	${If} ${RunningX64}
			DetailPrint "Registering LAMS as service in 64bit system"
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service64.bat" install'
	${Else} 
			DetailPrint "Registering LAMS as service in 32bits system"	
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service.bat" install'
    ${EndIf}
	
	Pop $0
	${If} $0 == 0
		DetailPrint "LAMS successfully setup as a service. ($0)"
		WriteRegStr HKCU "${REG_HEAD}" "is_service" "1"
	${Else}
		DetailPrint "LAMS was not setup as a service. ($0)"
		MessageBox MB_OK|MB_ICONEXCLAMATION "LAMS was not installed as a service.  However you may start LAMS by double-clicking $INSTDIR\jboss-${JBOSS_VERSION}\bin\run.bat."
	${EndIf}   
FunctionEnd 
Function WriteRegEntries
    WriteRegStr HKCU "${REG_HEAD}" "installer_type" "${INSTALLERTYPE}"
    WriteRegStr HKCU "${REG_HEAD}" "is_service" "0"
    WriteRegStr HKCU "${REG_HEAD}" "dir_inst" $INSTDIR
	WriteRegStr HKCU "${REG_HEAD}" "jre_dir" "$INSTDIR\jre"
	WriteRegStr HKCU "${REG_HEAD}" "jboss_dir" "$INSTDIR\jboss-${JBOSS_VERSION}"
	WriteRegStr HKCU "${REG_HEAD}" "mysql_dir" "$INSTDIR\data\db"	
    WriteRegStr HKCU "${REG_HEAD}" "version" "${VERSION}"
    WriteRegStr HKCU "${REG_HEAD}" "server_version" "${SERVER_VERSION_NUMBER}"
    WriteRegStr HKCU "${REG_HEAD}" "lams_domain" $LAMS_DOMAIN
    WriteRegStr HKCU "${REG_HEAD}" "lams_port" $LAMS_PORT
    WriteRegStr HKCU "${REG_HEAD}" "lams_locale" $LAMS_LOCALE
FunctionEnd
Function SetupStartMenu
    CreateDirectory "$SMPROGRAMS\LAMS"
    CreateShortCut "$SMPROGRAMS\LAMS\Access LAMS.lnk" "http://$LAMS_DOMAIN:$LAMS_PORT/lams/"
    CreateShortCut "$SMPROGRAMS\LAMS\LAMS Community.lnk" "http://lamscommunity.org"
	CreateShortCut "$SMPROGRAMS\LAMS\LAMS Documentation.lnk" "http://wiki.lamsfoundation.org"
    CreateShortCut "$SMPROGRAMS\LAMS\Start LAMS.lnk" "$INSTDIR\lams-start.exe"
    CreateShortCut "$SMPROGRAMS\LAMS\Stop LAMS.lnk" "$INSTDIR\lams-stop.exe"
    ;CreateShortCut "$SMPROGRAMS\LAMS\Backup LAMS.lnk" "$INSTDIR\lams-backup.exe"
    CreateShortCut "$SMPROGRAMS\LAMS\Uninstall LAMS.lnk" "$INSTDIR\lams-uninstall.exe"
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
    RMDIR /r "$WINTEMP\lams"
FunctionEnd
Function .onInstFailed 
    ;remove all files from the instdir excluding the backed up files
    RMDir /r "$INSTDIR"
    
    Call RemoveTempFiles
    DeleteRegKey HKCU "${REG_HEAD}"
FunctionEnd
Function .onInstSuccess
    DetailPrint "LAMS ${VERSION} configuration successful."
    Call RemoveTempFiles
	Delete "$INSTDIR\${LAMS24_7Z}"
    RMDir /r "$INSTDIR\apache-ant-1.6.5"
    RMDir /r "$INSTDIR\zip"
    RMDir /r "$INSTDIR\backup\repository"
    RMDIR /r "$INSTDIR\backup\jboss-${JBOSS_VERSION}"
    Delete "$INSTDIR\backup\lamsDump.sql"
FunctionEnd
################################################################################
# END CODE USED FOR INSTALLER                                                  #
################################################################################
################################################################################
# CODE USED FOR UNINSTALLER                                                    #
################################################################################
# Uninstaller
Function un.onInit
    !insertmacro MUI_INSTALLOPTIONS_EXTRACT "uninstall.ini"
  
    # check ifLAMS is stopped
    SetOutPath $TEMP
    File "${BUILD}\LocalPortScanner.class"
    ReadRegStr $0 HKCU "${REG_HEAD}" "lams_port"
    ReadRegStr $1 HKCU "${REG_HEAD}" "jre_dir"
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
		${If} ${RunningX64}
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service64.bat" stop'
		${Else} 
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service.bat" stop'
		${EndIf}
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
    ;ifnot "SubString", this could be "String"'s end
    ${IfThen} $R4 >= $R3 ${|} ${ExitDo} ${|}
    ;ifnot, continue the loop
    IntOp $R4 $R4 + 1
  ${Loop}
 
/*After this point:
  ------------------------------------------
  $R0 = ResultVar (output)*/
 
  ;Remove part before "SubString" on "String" (ifthere has one)
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
    # Has it being installed as Service?    
    ReadRegStr $0 HKCU "${REG_HEAD}" "is_service"	
	
	${If} $0 == 1
		${If} ${RunningX64}
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service64.bat" uninstall'
		${Else} 
			nsExec::ExecToStack '"$INSTDIR\jboss-${JBOSS_VERSION}\bin\service.bat" uninstall'
		${EndIf}
		Pop $0
		Pop $1
		; can't call StrStr from within uninstaller unless it's a un. function
		${un.StrStr} $2 $1 "SUCCESS"
		${If} $0 == "error"
			MessageBox MB_OK|MB_ICONEXCLAMATION "Couldn't remove LAMS service.$\r$\n$\r$\n$1"
			DetailPrint "Failed to remove LAMS service."
		${Else}
			DetailPrint "Removed LAMS service."
		${EndIf}
	${EndIf}
	
    DetailPrint "Removing LAMS files."    
	RMDIR /r '$INSTDIR'
    DeleteRegKey HKCU "${REG_HEAD}"
    DetailPrint "Removed registry entries."
    RMDir /r "$SMPROGRAMS\LAMS"
    DetailPrint "Removed start menu entries."
    DetailPrint "Uninstall complete."
SectionEnd
################################################################################
# END CODE USED FOR UNINSTALLER                                                #
################################################################################
################################################################################
# UTIL Functions                                              #
################################################################################
; Push $filenamestring (e.g. 'c:\this\and\that\filename.htm')
; Push "\"
; Call StrSlash
; Pop $R0
; ;Now $R0 contains 'c:/this/and/that/filename.htm'
;; this is a slight variation of StrSlash use for Ant
Function StrSlash2
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
  StrCmp $R3 "\\\\" loop
  StrCpy $R4 "\\\\"  
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
