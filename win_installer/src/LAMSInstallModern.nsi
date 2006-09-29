;; Coded by C.K. Nimmagadda
;; Modified by Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
;; For LAMS Server Version 1.0.2
;; Copyright 2005 (c) LAMS International
;; http://www.lamsinternational.com/

;; Installer Version 0.21
;; Installer Status: BETA3

;; TODO
;  More user input validation
;  Better windows set environment (currently assuming the installer will only run once)
;  Indentation/style
;  Filewrites/file grouping
;  breaking up functions

;---------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
!include "LogicLib.nsh"
;!macro BIMAGE IMAGE PARMS
;	Push $0
;	GetTempFileName $0
;	File /oname=$0 "${IMAGE}"
;	SetBrandingImage ${PARMS} $0
;	Delete $0
;	Pop $0
;!macroend

;--------------------------------
; Add branding image to the installer (an image placeholder on the side).
; It is not enough to just add the placeholder, we must set the image too...
; We will later set the image in every pre-page function.
; We can also set just one persistent image in .onGUIInit
;AddBrandingImage left 200

;--------------------------------
;General
!define VERSION "1.0.2"
!define BUILD "20060424"

Name "LAMS Server Version ${VERSION}"
Caption "LAMS Server v${VERSION} Install"
OutFile "LAMS-Installer-v${VERSION}-build${BUILD}.exe"
InstallDir "c:\lams\"
;InstallDir "$PROGRAMFILES\LAMS\Server"
InstallDirRegKey HKLM "Software\LAMS\Server" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 
CheckBitmap "..\..\Contrib\Graphics\Checks\classic-cross.bmp"

BrandingText "LAMS International Free Installer"
LicenseText ""
LicenseData "..\..\LAMS\license.txt"
SetDateSave on
SetDatablockOptimize on
CRCCheck on
SilentInstall normal
;BGGradient 000000 800000 FFFFFF
;InstallColors FF8080 000030 
XPStyle on
;;LogSet on
;--------------------------------

!ifndef NOINSTTYPES ; only if not defined
  InstType "LAMS only"
  ;InstType /NOCUSTOM
  ;InstType /COMPONENTSONLYONCUSTOM
!endif

AutoCloseWindow true
ShowInstDetails hide

!include "Sections.nsh"
#!define SECTION_ON 0x80000000
#!define SECTION_OFF 0x7FFFFFFF

;;; Not as efficient as using bit flags - change in the next version
!define BASIC 0
!define ADVANCED 1
;;!define NSIS_CONFIG_LOG "install.log"
;--------------------------------
;Interface Settings

;  !define MUI_ABORTWARNING
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP  "..\..\LAMS\header.bmp"
!define MUI_HEADERIMAGE_RIGHT

!define MUI_COMPONENTSPAGE_NODESC ;No value


;!insertmacro MUI_DEFAULT MUI_ICON "..\..\LAMS\logo2.ico"


!define MUI_ICON "..\..\LAMS\lams2.ico"
!define MUI_UNICON "..\..\LAMS\lams2.ico"

;  !insertmacro MUI_DEFAULT MUI_UI "${NSISDIR}\Contrib\UIs\modern.exe"
;  !insertmacro MUI_DEFAULT MUI_UI_HEADERIMAGE "${NSISDIR}\Contrib\UIs\modern_headerbmp.exe"
;  !insertmacro MUI_DEFAULT MUI_UI_HEADERIMAGE_RIGHT "${NSISDIR}\Contrib\UIs\modern_headerbmpr.exe"
;  !insertmacro MUI_DEFAULT MUI_UI_COMPONENTSPAGE_SMALLDESC "${NSISDIR}\Contrib\UIs\modern_smalldesc.exe"
;  !insertmacro MUI_DEFAULT MUI_UI_COMPONENTSPAGE_NODESC "${NSISDIR}\Contrib\UIs\modern_nodesc.exe"

;  !insertmacro MUI_DEFAULT MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"
;  !insertmacro MUI_DEFAULT MUI_LICENSEPAGE_BGCOLOR "/windows"
  !insertmacro MUI_DEFAULT MUI_LICENSEPAGE_BGCOLOR "FFFFFF"
;  !insertmacro MUI_DEFAULT MUI_INSTFILESPAGE_COLORS "/windows"
;  !insertmacro MUI_DEFAULT MUI_INSTFILESPAGE_PROGRESSBAR "smooth"
;  !insertmacro MUI_DEFAULT MUI_BGCOLOR "FFFFFF"
;  !insertmacro MUI_DEFAULT MUI_WELCOMEFINISHPAGE_INI "${NSISDIR}\Contrib\Modern UI\ioSpecial.ini"
;  !insertmacro MUI_DEFAULT MUI_UNWELCOMEFINISHPAGE_INI "${NSISDIR}\Contrib\Modern UI\ioSpecial.ini"
;  !insertmacro MUI_DEFAULT MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\win.bmp"
;  !insertmacro MUI_DEFAULT MUI_UNWELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\win.bmp"

 ;--------------------------------
;Pages

  !define MUI_LICENSEPAGE_RADIOBUTTONS_TEXT_ACCEPT "I agree"
  !define MUI_LICENSEPAGE_RADIOBUTTONS_TEXT_DECLINE "I do not agree"

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; License for installer
;
;

  !define MUI_LICENSEPAGE_TEXT_TOP "Scroll down to see the rest of the agreement."
  !define MUI_PAGE_HEADER_TEXT "LAMS Installer License Agreement"
  !define MUI_PAGE_HEADER_SUBTEXT  "Please review the LAMS Installer license terms before continuing the installation."
  !insertmacro MUI_PAGE_LICENSE "..\..\LAMS\disclaimer.txt"




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; License for server
;
;

  !define MUI_LICENSEPAGE_TEXT_TOP "Scroll down to see the rest of the agreement."
  !define MUI_PAGE_HEADER_TEXT "LAMS Server License Agreement"
  !define MUI_PAGE_HEADER_SUBTEXT  "Please review the LAMS Server license terms before continuing the installation."
  !insertmacro MUI_PAGE_LICENSE "..\..\LAMS\license.txt"
  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE "CheckINSTDIR" 
  !insertmacro MUI_PAGE_DIRECTORY  

  Page custom SelectLevel LeaveSelectLevel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Components options
;
;
  !define MUI_COMPONENTSPAGE_TEXT_TOP "The default option is a full installation. For additional customisation select a different installation sequence select or deselect individual components. Click Next to continue."
  !define MUI_COMPONENTSPAGE_TEXT_COMPLIST "Individual installation components, tests or functions:"
  !define MUI_COMPONENTSPAGE_TEXT_INSTTYPE "Select installation sequence:"
  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE "LeaveComponentPage" 
  !insertmacro MUI_PAGE_COMPONENTS

  Page custom GeneralSettings LeaveGeneralSettings
  Page custom MySQLPage LeaveCustomPageB
  Page custom CustomPageA LeaveCustomPageA
#  Page custom FinalSettings

  !insertmacro MUI_PAGE_INSTFILES    
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Finish page setup
;
;

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Access LAMS Login page on your browser window."
!define MUI_FINISHPAGE_RUN_FUNCTION "OpenLamsConsole"
!define MUI_FINISHPAGE_TEXT "The LAMS Server has been successfully installed on your computer."


#!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\lams.html"   ;; ARE YOU SURE THIS WORKS????
#!define MUI_FINISHPAGE_SHOWREADME_TEXT "Display Online LAMS Manual"
#!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED

!define MUI_FINISHPAGE_LINK "Purchase LAMS support and contribute to the LAMS project"
!define MUI_FINISHPAGE_LINK_LOCATION "http://www.lamsinternational.com/"
!insertmacro MUI_PAGE_FINISH 
  



;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Reserve Files
  
  ;These files should be inserted before other files in the data block
  ;Keep these lines before any File command
  ;Only for solid compression (by default, solid compression is enabled for BZIP2 and LZMA)
  
  ReserveFile "lams-parameters.ini"
  ReserveFile "mysql-parameters.ini"
  ReserveFile "server-parameters.ini"
  ReserveFile "quick-parameters.ini"
  ReserveFile "install-select.ini"
  ReserveFile "quick-parameters.ini"
  ReserveFile "final-settings.ini"
  !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

;--------------------------------
;Variables

  Var InstallType
  Var JDKFlag
  Var MysqlFlag
  Var MysqlSetup
  Var MysqlPassword
  Var TestFlag
  Var JAVA_HOME
  Var MYSQL_HOME
  Var RETURN_VARIABLE
  Var EXEC_RETURN_STATUS
  Var EXEC_RETURN_ERROR  
  Var FILE_HANDLE
  Var NEW_FILE_HANDLE
  Var Copied
;--------------------------------
;Installer Sections

;--------------------------------
;Installer Functions

Function .onInit

  ;Extract InstallOptions INI files
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "install-select.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "server-parameters.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "lams-parameters.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "mysql-parameters.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "quick-parameters.ini"
  !insertmacro MUI_INSTALLOPTIONS_EXTRACT "final-settings.ini"

  File /oname=$PLUGINSDIR\splash.bmp "..\..\LAMS\logo.bmp"
  advsplash::show 1500 600 400 0x04025C $PLUGINSDIR\splash
  Pop $0

  ;; JDKFlag & MYSQLFlag = 1 - The installers will not be run

  StrCpy $JDKFlag 1
  StrCpy $MysqlFlag 1
  StrCpy $InstallType ${BASIC}
  StrCpy $MysqlSetup 100
  StrCpy $MysqlPassword ""
  StrCpy $JAVA_HOME ""
  StrCpy $MYSQL_HOME ""
  StrCpy $Copied 0

  ;; Here we check that JAVA 1.4.2 is installed.	
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome" 

  ${If} $2 == ""
      MessageBox MB_OK|MB_ICONSTOP  'Installation cannot continue as JAVA JDK 1.4.2 is not installed. Please download and install J2SE v 1.4.2_x SDK from http://java.sun.com/j2se/1.4.2/download.html'
      Abort -2       
  ${EndIf}

  ; sets JAVA_HOME and adds it to the system as an environment variable 
  StrCpy $JAVA_HOME "$2"

  ;; Checks that either MySQL 4.1 or 5.0 is installed.	
  ;ReadRegStr $2 HKLM "SOFTWARE\MySQL AB\MySQL Server 4.1" "Version" 
  ;ReadRegStr $3 HKLM "SOFTWARE\MySQL AB\MySQL Server 5.0" "Version" 

;  ${If} $2 == "" 
;  ${AndIf} $3 == ""
;          MessageBox MB_OK|MB_ICONSTOP  'Installation cannot continue as MySQL Server is not installed. Please download and install MySQL 4.1.x from http://mysql.com'
;          Abort -2
;  ${EndIf}



FunctionEnd

LangString TEXT_IO_TITLE ${LANG_ENGLISH} "LAMS Install Tool 0.1"
LangString TEXT_IO_SUBTITLE ${LANG_ENGLISH} "Installing LAMS Server ${VERSION}"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Prints a report of the settings chosen

Function FinalSettings
  SetOutPath "$INSTDIR"
;   MessageBox MB_OK "$MYSQL_HOME"
  WriteINIStr  "$PLUGINSDIR\final-settings.ini" "Field 1" "Text" ""
  !insertmacro MUI_HEADER_TEXT "Configured Settings" "Check the configured settings."
  !insertmacro MUI_INSTALLOPTIONS_DISPLAY "final-settings.ini"
FunctionEnd

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Selects the Installation Type

Function FindExistingLAMS
   SetOutPath "$INSTDIR"
   StrCpy $1 ""
   ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_status"
;; DEBUG

   ${If} $1 == "successful"
        goto existing
   ${ElseIf} $1 == "completed"
        goto existing
   ${Else}
        goto end
   ${EndIf}

   existing:

           ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_dir"
           StrCpy $2 "$1\start-lams.exe"
           IfFileExists $2 present end
           present:
           MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION "The LAMS Server may already be installed at $1.$\nContinuing without uninstalling or backing up could overwrite parts of the existing LAMS installation." IDCANCEL quitit
           ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\LAMS" "DisplayName"
           ${If} $1 != ""
                 MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION "The existing LAMS service will be removed." IDCANCEL quitit
           ${EndIf}
           goto end
   quitit:
           Quit
   end:
FunctionEnd

;Function .onInstFailed
;        ${If} Copied = 1
;                Delete "$INSTDIR\*.*"
;                Quit
;        ${EndIf}
;FunctionEnd
        

Function SelectLevel

   call FindExistingLams
   SetOutPath "$INSTDIR"
      File /a "..\..\LAMS\newbie2.bmp"
      File /a "..\..\LAMS\advanced3.bmp"
      File /a "..\..\LAMS\basic-text.bmp"
      File /a "..\..\LAMS\advanced-text.bmp"
      File /a "..\..\LAMS\LocalPortScanner.class"
      File /a "..\..\LAMS\ValidateOrgId.class"
      File /a "..\..\LAMS\ValidateOrgIdCheckSum.class"
      
      File /a "..\..\LAMS\manual.html"
      File /r "..\..\LAMS\openlams_files"
;      File /a "..\..\LAMS\advancedguide.ppt"
;      File /a "..\..\LAMS\basicguide.ppt"
      File /a "..\..\LAMS\advancedguide.pdf"
      File /a "..\..\LAMS\basicguide.pdf"
      File /a "..\..\LAMS\teachersguide.pdf"
      File /a "..\..\LAMS\learnersguide.pdf"
 ;     File /r "..\..\LAMS\installer"
      File /r "..\..\LAMS\sc.exe"
      SetOutPath $TEMP
      File /a "..\..\LAMS\LocalPortScanner.class"
      SetOutPath $INSTDIR

      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 15" "State" "file:///$INSTDIR\manual.html#orgkey"
      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 16" "State" "file:///$INSTDIR\manual.html#serverip"
      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 17" "State" "file:///$INSTDIR\manual.html#email"
      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 18" "State" "file:///$INSTDIR\manual.html#isonline"
      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 19" "State" "file:///$INSTDIR\manual.html#os"
      WriteINIStr  "$PLUGINSDIR\server-parameters.ini" "Field 20" "State" "file:///$INSTDIR\manual.html#smtp"

      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 21" "State" "file:///$INSTDIR\manual.html#jboss"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 22" "State" "file:///$INSTDIR\manual.html#uploads"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 20" "State" "file:///$INSTDIR\manual.html#adminlogin"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 19" "State" "file:///$INSTDIR\manual.html#adminpassword"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 18" "State" "file:///$INSTDIR\manual.html#tomcat"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 17" "State" "file:///$INSTDIR\manual.html#chat"
      WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 16" "State" "file:///$INSTDIR\manual.html#jdk"

      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 14" "State" "file:///$INSTDIR\manual.html#mysqlhome"
      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 15" "State" "file:///$INSTDIR\manual.html#mysqldb"
      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 16" "State" "file:///$INSTDIR\manual.html#mysqluser"
      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 17" "State" "file:///$INSTDIR\manual.html#mysqlrootpasswd"
      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 18" "State" "file:///$INSTDIR\manual.html#mysqluserpasswd"
      WriteINIStr  "$PLUGINSDIR\mysql-parameters.ini" "Field 19" "State" "file:///$INSTDIR\manual.html#mysqlhost"

      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 18" "State" "file:///$INSTDIR\manual.html#basicorgkey"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 19" "State" "file:///$INSTDIR\manual.html#basicisonline"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 20" "State" "file:///$INSTDIR\manual.html#basicemail"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 21" "State" "file:///$INSTDIR\manual.html#basicsmtp"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 22" "State" "file:///$INSTDIR\manual.html#basicmysqlrootpasswd"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 24" "State" "file:///$INSTDIR\manual.html#basicadminlogin"
      WriteINIStr  "$PLUGINSDIR\quick-parameters.ini" "Field 25" "State" "file:///$INSTDIR\manual.html#basicadminpassword"

      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 7" "State" "file:///$INSTDIR\manual.html#guides"
      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 10" "State" "file:///$INSTDIR\manual.html#guides"
 
      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 1" "Text" "$INSTDIR\newbie2.bmp"
      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 3" "Text" "$INSTDIR\advanced3.bmp"
      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 5" "Text" "$INSTDIR\basic-text.bmp"
      WriteINIStr  "$PLUGINSDIR\install-select.ini" "Field 8" "Text" "$INSTDIR\advanced-text.bmp"

      !insertmacro MUI_HEADER_TEXT "Setting Up LAMS" "Installing and Configuring the LAMS Server"
      !insertmacro MUI_INSTALLOPTIONS_DISPLAY "install-select.ini"
      ReadINIStr $InstallType "$PLUGINSDIR\install-select.ini" "Field 4" "State"


FunctionEnd

Function LeaveSelectLevel
FunctionEnd

Function LeaveComponentPage
        SetOutPath "$INSTDIR"
  ${If} $InstallType = ${BASIC}
;        !insertmacro MUI_HEADER_TEXT "Interactive Package Install" "Installing 3rd Party packages - JDK 1.4.2"
       	call RunJavaInstaller
 
;        !insertmacro MUI_HEADER_TEXT "Interactive Package Install" "Installing 3rd Party packages - MySQL 4.1.11"
       	call RunMysqlInstaller
   ${Else} 
        call MiniJDKPage
        call RunMysqlInstaller
        ; nothing yet
  ${Endif}
;   MessageBox MB_OK "Install type $InstallType"
FunctionEnd


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Advanced install only
;
; Enter system settings

Function GeneralSettings
  SetOutPath "$INSTDIR"
  Push $INSTDIR
  call FixPath
  Pop  $INSTDIR
  ${If} $InstallType = ${ADVANCED}
      	!insertmacro MUI_HEADER_TEXT "General System Settings" "Provide details about the Windows server."
        ;CallInstDll $INSTDIR\ip.dll get_ip
        ;Pop $R0   - not needed if using getnextip       
        ;Call GetNextIp
        
        ;gets first IP address off the stack
        ;Pop $R0
        
  	!insertmacro MUI_INSTALLOPTIONS_DISPLAY "server-parameters.ini"

        ; do some pre-form filling
        WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 3" "State" "$INSTDIR\jboss"
        WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 5" "State" "$INSTDIR\lamsdata"
        ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome" 
        WriteINIStr  "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State" "$2"
  ${Else}
 		; Page not activated if basic installation option chosen
  ${Endif}

FunctionEnd

Function LeaveGeneralSettings
        SetOutPath "$INSTDIR"
        call CheckOrgId
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Selects the Installation Type

Function CustomPageA
   SetOutPath "$INSTDIR"
  !insertmacro MUI_HEADER_TEXT "Setting Up LAMS" "Installing and Configuring the LAMS Server"

  ${If} $InstallType = ${BASIC}
        !insertmacro MUI_INSTALLOPTIONS_DISPLAY "quick-parameters.ini"
  ${Else}
        !insertmacro MUI_INSTALLOPTIONS_DISPLAY "lams-parameters.ini"
        ;ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State"
        ;StrCpy $JAVA_HOME $R3
        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 3" "State"
        StrCpy $1 "The JBoss directory specified '$R3' has a space in it. Please select a directory without a space!"
        Push $1
        Push $R3
        call CheckForSpaces
        
        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 5" "State" 
        StrCpy $1 "The LAMS Data directory specified '$R3' has a space in it. Please select a directory without a space!"
        Push $1
        Push $R3
        call CheckForSpaces                                     
  ${Endif}
FunctionEnd


Function LeaveCustomPageA
  SetOutPath "$INSTDIR"
  ${If} $InstallType = ${BASIC}
        ; ReadINIStr $MysqlSetup "$PLUGINSDIR\quick-parameters.ini" "Field 26" "State"
        StrCpy $MysqlSetup 1

        ReadINIStr $MysqlPassword  "$PLUGINSDIR\quick-parameters.ini" "Field 13" "State"
        call CheckOrgId
        call CheckLAMSLogin
        call CheckLAMSPassword      
  ${Else}
        ; nothing yet
  ${Endif}
  call RunTests
FunctionEnd

Function CheckOrgId
  SetOutPath "$INSTDIR"
  ${If} $InstallType = ${BASIC}
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 2" "State" 
        ReadINIStr $R4 "$PLUGINSDIR\quick-parameters.ini" "Field 5" "State" 
  ${Else}
        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 4" "State" 
        ReadINIStr $R4 "$PLUGINSDIR\server-parameters.ini" "Field 3" "State"
  ${EndIf}      

        ${If} $R3 = 1
;                nsExec::ExecToStack '$JAVA_HOME\bin\java ValidateOrgId $R4'

                nsExec::ExecToStack '$JAVA_HOME\bin\java ValidateOrgIdCheckSum $R4'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR 
                ;MessageBox MB_OK "got here: $R4 $EXEC_RETURN_ERROR"
                ${If} $EXEC_RETURN_STATUS = 0
                        ;;

                ${ElseIf} $EXEC_RETURN_STATUS = 2
                        MessageBox MB_OK "Invalid Organisation Key. Please check your key or retrieve your valid organisation key.$\nTo continue without a valid organisation key, please deselect the option 'This will be an Online Server'"
                        Abort
                ${Else}
                        MessageBox MB_OK "Unable to verify your Organisation Key.$\nTo continue without a valid organisation key, please deselect the option 'This will be an Online Server'"
                        Abort
                ${EndIf}
        ${EndIf}



FunctionEnd

;; Only runs for the basic install

Function CheckLAMSLogin
        SetOutPath "$INSTDIR"
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 15" "State"
        ${If} $R3 == "admin"      

                goto quitit                
        ${ElseIf} $R3 == "author"      
                goto quitit
        ${ElseIf} $R3 == "teacher"      
                goto quitit
        ${ElseIf} $R3 == "learner"
                goto quitit
        ${Else}      
                goto end
        ${EndIf}
        quitit:
                MessageBox MB_OK|MB_ICONSTOP "The username 'admin' is reserved by the system. Pick an alternative name!"
                Abort
        end:
        StrCpy $1 "The username '$R3' has a space in it. Please select a login name without a space!"
        Push $1
        Push $R3
        call CheckForSpaces
FunctionEnd

Function CheckLAMSPassword
        SetOutPath "$INSTDIR"
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 17" "State"
        StrCpy $1 "The password '$R3' has a space in it. Please select a password without a space!"
        Push $1
        Push $R3
        call CheckForSpaces
FunctionEnd



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Advanced install only
;
; The mysql page

Function MySQLPage
   SetOutPath "$INSTDIR"
  ${If} $InstallType = ${ADVANCED}
        ; Advanced Installation only

    call FindMySQLInstallation

	${If} $MYSQL_HOME == ""
		WriteINIStr "$PLUGINSDIR\mysql-parameters.ini" "Field 3" "State" "c:\mysql\"
	${Else}
		WriteINIStr "$PLUGINSDIR\mysql-parameters.ini" "Field 3" "State" $MYSQL_HOME
	${Endif}

       !insertmacro MUI_HEADER_TEXT "MySQL Integration" "Configuring LAMS to the use the MySQL database."
       !insertmacro MUI_INSTALLOPTIONS_DISPLAY "mysql-parameters.ini"
  ${Else}
	redundant: 
		; Page not activated if basic installation option chosen
  ${Endif}

FunctionEnd

Function LeaveCustomPageB
  call LeaveMysqlPage
FunctionEnd

Function FixPath
        SetOutPath "$INSTDIR"
        Pop $1
        StrCpy $0 ""
        StrCpy $0 $1 1 -1
        ${If} $0 == "\"
                StrCpy $0 $1 -1
        ${Else}
                StrCpy $0 $1
        ${EndIf}
        Push $0
FunctionEnd

Function LeaveMysqlPage
        SetOutPath "$INSTDIR"
        ReadINIStr $MYSQL_HOME "$PLUGINSDIR\mysql-parameters.ini" "Field 3" "State"
        
        ; We now assume MySQL has been setup by the user.
        ; ReadINIStr $MysqlSetup  "$PLUGINSDIR\mysql-parameters.ini" "Field 21" "State"
        StrCpy $MysqlSetup 1  

        ReadINIStr $MysqlPassword  "$PLUGINSDIR\mysql-parameters.ini" "Field 9" "State"
        Push $MYSQL_HOME
        Call FixPath
        Pop $R3
        IfFileExists $R3 present notpresent
      
        notpresent:
              	MessageBox MB_OK|MB_ICONSTOP "Invalid MySQL Home specified (check if $R3 exists)!"
                Abort
        present:
		; changed from mysqld-nt.exe to mysqld.exe for compatibility with easyphp/xamp, etc.
                StrCpy $R3 "$MYSQL_HOME\bin\mysqld.exe"
              	IfFileExists "$MYSQL_HOME\bin\mysqld.exe" done notpresent
        done:


FunctionEnd


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Advanced install only
;
; The JDK page - enters only when JDK needs to be installed - needs to be relocated

Function FindJDKInstallation
        SetOutPath "$INSTDIR"
        StrCpy $2 0
	ClearErrors
	;ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion" 
	ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome" 
	ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "MicroVersion"
	IfErrors 0 setlocation

        ;; if errors again then looking at the enviornment variable
;     	ReadEnvStr $2 "JAVA_HOME"      
        ${If} $JAVA_HOME != ""
                StrCpy $2 "$2\"
        ${EndIf}
        
        ;; sets as blank (by default) if no environment variable either
        setlocation:
                StrCpy $JAVA_HOME $2         
FunctionEnd


Function MiniJDKPage
        SetOutPath "$INSTDIR"
       	call RunJavaInstaller       	
        call FindJDKInstallation

  	ReadEnvStr $R0 "JAVA_HOME"
	${If} $R0 == ""
	  	WriteINIStr "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State" "aasa"
        ${Else}
		WriteINIStr "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State" $R0
    ${Endif}
        done:
FunctionEnd

Function LeaveJDKPage
        SetOutPath "$INSTDIR"
        ReadINIStr $JAVA_HOME "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State"
        IfFileExists $JAVA_HOME present notpresent
       
        notpresent:
              	MessageBox MB_OK|MB_ICONSTOP "Invalid Java Home specified (check if $JAVA_HOME\bin\javac.exe exists)!"
                Abort
        present:
              	IfFileExists "$JAVA_HOME\bin\javac.exe" done notpresent
        done:

FunctionEnd


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Advanced install only
;
; Installing JDK and Mysql - potentially based on whether or not they are selected

Function "RunJavaInstaller"
        SetOutPath "$INSTDIR"

        call FindJDKInstallation
        ${If} $JDKFlag = 0
        
                
              ;;;; JDK Installer no longer run
              MessageBox MB_OK "Please down and installer J2SDK 1.4.2.x before continuing the installation."
              
              ;!insertmacro MUI_HEADER_TEXT "Interactive Package Install" "Installing 3rd Party packages - JDK 1.4.x"
              ;  MessageBox MB_OK|MB_ICONQUESTION "Launching the JDK installer may take a few seconds.$\nSelect Not to restart your computer after JDK has been installed."
              ; StrCpy $JDKFlag 2
	      ; ${If} $JAVA_HOME != ""
	       ;      MessageBox MB_YESNO|MB_ICONINFORMATION "Java may already be installed at $JAVA_HOME. Continue (recommended)?" IDNO continuefurther ; skipped if file doesn't exist
	       ;${Endif}
               ;
               ; SetOutPath $TEMP
               ; File /a "..\..\bundled\jdk-installer.exe"
               ;
               ; sleep 1000
               ;   ;SearchPath $1 jdk-installer.exe
               ;   ;MessageBox MB_OK "$INSTDIR\jdk-installer.exe=$1"
               ; ExecWait '"$TEMP\jdk-installer.exe"'
               ;
               ; nsExec::ExecToStack '$JAVA_HOME\bin\javac --version'
               ; Pop $EXEC_RETURN_STATUS
               ; Pop $EXEC_RETURN_ERROR 
               ;
               ; MessageBox MB_OK|MB_ICONEXCLAMATION "Windows may present an alert message when first using Java!$\nPlease select to unblock Java/Javac for the install to complete successfully."
               ; nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 3306'
               ; Pop $EXEC_RETURN_STATUS
               ; Pop $EXEC_RETURN_ERROR
               ; call FindJDKInstallation
               ; continuefurther:
               ;         bringtofront
        ${EndIf}
        SetOutPath $INSTDIR
FunctionEnd

;Function FindMySQLService
;          SetOutPath "$TEMP"
;
;          nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 3306'
;          Pop $EXEC_RETURN_STATUS
;          Pop $EXEC_RETURN_ERROR
;          ;MessageBox MB_OK|MB_ICONEXCLAMATION "Status: $EXEC_RETURN_STATUS cmd:$JAVA_HOME\bin\java LocalPortScanner 3306"
;
;          ${If} $EXEC_RETURN_STATUS = 1
;                ${If} $InstallType = ${ADVANCED}
;                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
;                ${EndIf}
;          ${ElseIf} $EXEC_RETURN_STATUS = 0
;                ;;
;          ${Else}      
;                MessageBox MB_OK|MB_ICONSTOP "Error launching MySQL installer: MySQL is already running! Uninstall MySQL or use the existing MySQL server for LAMS!$\n$\nDe-select 'Launch MySQL Installer' to continue."
;                ${If} $InstallType = ${ADVANCED}
;                        Abort -2
;                ${EndIf}
;                Abort
;          ;; Mysql is running
;          ${EndIf}
;
;	      ${If} $MYSQL_HOME != ""
;               StrCpy $1 "$MYSQL_HOME\bin\mysqladmin.exe"
;               IfFileExists "$1" intactdatabase
;	                    MessageBox MB_YESNO|MB_ICONQUESTION "Detected an exising MySQL installation at $MYSQL_HOME. Continue?" IDNO quitit
;	                    goto continue
;               intactdatabase:
;	                    MessageBox MB_YESNO|MB_ICONQUESTION "Detected an exising MySQL installation at $MYSQL_HOME. Abort MySQL installation (recommended)?" IDYES quitit
;               continue:
;                   StrCpy $1 "$MYSQL_HOME\bin\mysqladmin.exe"
;               StrCpy $EXEC_RETURN_STATUS 2
;               StrCpy $EXEC_RETURN_ERROR "Existing MySQL!"
;	      ${Endif}
;
;         ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\MySQL" "DisplayName"
;          ${If} $1 != ""
;                goto foundservice
;          ${Else}
;                goto olddatabase
;          ${EndIf}
;          
;          foundservice:
;                  MessageBox MB_YESNO|MB_ICONEXCLAMATION "Found an existing (but not running) MySQL service component in Windows.$\nThis service component must be removed for the MySQL installation to continue!$\nClick Yes to remove and continue, No to abort and fix manually!" IDNO olddatabase
;                  SetOutPath "$TEMP"
;                  File /r "..\..\LAMS\removemysql"
;                  nsExec::ExecToStack '$TEMP\removemysql\bin\UninstallMySQLService.bat'
;                  Pop $EXEC_RETURN_STATUS
;                  Pop $EXEC_RETURN_ERROR
;                  ${If} $EXEC_RETURN_STATUS != 0
;                         MessageBox MB_OK|MB_ICONEXCLAMATION "Removing the service may have failed!"
;                  ${EndIf}
;          olddatabase:
;                  StrCpy $0 "C:\Program Files\MYSQL\MySQL Server 4.1"
;                  StrCpy $1 "C:\Program Files\MYSQL\MySQL Server 4.1\data\mysql\db.frm"
;                  IfFileExists "$1" yesdatabase nodatabase
;
;          yesdatabase:
;                        MessageBox MB_YESNO|MB_ICONEXCLAMATION "A dormant MySQL database at $0\data may interfere with the MySQL installation.$\nThis directory needs to be moved for the MySQL installation to complete successfully!$\nClick Yes to rename this directory to $0\data-backup.lams, or click No to leave directory unchanged!" IDNO end
;                        File /a "..\..\LAMS\move.bat"
;                        nsExec::ExecToStack '"$TEMP\move.bat" "$0\data" "$0\data-backup.lams"'
;                        Pop $EXEC_RETURN_STATUS
;                        Pop $EXEC_RETURN_ERROR
;                        ${If} $EXEC_RETURN_STATUS != 0
;                               MessageBox MB_OK|MB_ICONEXCLAMATION "Please move the existing database at $0\data before continuing."
;                        ${EndIf}                
;                  goto end
;         nodatabase:
;                  goto end
;         
;          quitit:
;                  StrCpy $1 "Existing MySQL!"
;                  Push $1        
;                  StrCpy $1 1
;                  Push $1 
;                  goto done
;          end:          
;                  StrCpy $1 "No error"
;                  Push $1        
;                  StrCpy $1 0
;                  Push $1 
;          done:                 
;                  SetOutPath "$INSTDIR"
;FunctionEnd

Function FindMySQLInstallation
        SetOutPath "$INSTDIR"
        StrCpy $1 0
        
        ClearErrors
        ReadRegStr $1 HKLM "SOFTWARE\MYSQL AB\MySQL Server 4.1" "Location"
        IfErrors 0 setlocation

        ;; if errors then trying again else "setlocation"
      	ClearErrors
        ReadRegStr $1 HKLM "SOFTWARE\MYSQL AB\MySQL Server 5.0" "Location"
	IfErrors 0 setlocation

        ;; if errors again then looking at the enviornment variable

;;       	ReadEnvStr $1 "MYSQL_HOME"
       
        ;; sets as blank if no environment variable either
        setlocation:
                Push $1
                call FixPath
                Pop $1
                StrCpy $MYSQL_HOME $1
;                MessageBox MB_OK "fixpath mysql home $MYSQL_HOME" 

; Check if Mysql Service is installer

               
FunctionEnd

Function "RunMysqlInstaller"
   SetOutPath "$INSTDIR"
  call FindMySQLInstallation
  ${If} $MysqlFlag = 0
        ; MySQL No longer installed
        
        MessageBox MB_OK "Please install MySQL before continuing with the LAMS installation."
       ;!insertmacro MUI_HEADER_TEXT "Interactive Package Install" "Installing 3rd Party packages - MySQL 4.1.11"
       ; ;; No intact MySQL found - try to look for poorly uninstalled bits
       ; call FindMySQLService
       ; pop $EXEC_RETURN_STATUS
       ; pop $EXEC_RETURN_ERROR
       ;
       ; ${If} $EXEC_RETURN_STATUS != 0
       ;         goto skipinstall
       ; ${EndIf}
       ;
       ; MessageBox MB_OK "Launching the MySQL Installer - this may take a few seconds!$\nPlease select and write down a password for MySQL configuration. This password will need to be entered again later."
       ; SetOutPath $TEMP
       ; StrCpy $MysqlFlag 2
       ; File /a "..\..\bundled\mysql-setup.exe"
;  SearchPath $1 mysql-setup.exe
;  MessageBox MB_OK "setup=$1"
       ; nsExec::ExecToStack '$TEMP\mysql-setup.exe'
        ;Pop $EXEC_RETURN_STATUS
        ;Pop $EXEC_RETURN_ERROR
        ;SetOutPath $INSTDIR        
        ;sleep 4000
        ;MessageBox MB_OK|MB_ICONINFORMATION "Press OK to continue after the MySQL installation process has been completed."
  ;skipinstall:  
        ;call FindMySQLInstallation
        ; done
  ${EndIf}
FunctionEnd

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;--------------------------------
;Descriptions

  ;Language strings

  ;Language strings
  LangString DESC_SecDummy ${LANG_ENGLISH} "LAMS Server ${VERSION}"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecDummy} $(DESC_SecDummy)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

Section "" ; empty string makes it hidden, so would starting with -

SetDetailsPrint textonly
DetailPrint "Status: Extracting packaged files to start the installation."
SetDetailsPrint listonly
;LogSet on

;  ;LogSet on

  ; write registry stuff
  DetailPrint "Writing to registry"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "install_dir" "$INSTDIR"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "install_version" "${VERSION}"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "install_build" "${BUILD}"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "install_status" "started"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "service" "lams"

  ; write uninstall strings
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LAMS Server" "DisplayName" "LAMS Server (remove only) - MySQL & JDK must be removed separately"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LAMS Server" "UninstallString" '"$INSTDIR\uninstall-lams.exe"'

  SetOutPath $INSTDIR
  File /r "..\..\LAMS\apache-ant"
  File /r "..\..\LAMS\wrapper"
  File /r "..\..\LAMS\lams-package"
  File /r "..\..\LAMS\jboss"
  File /a "..\..\LAMS\CallOS.class"
  File /a "..\..\LAMS\move.bat"
  File /a "..\..\LAMS\sed.exe"
  File /a "..\..\LAMS\setenv.exe"
  File /a "..\..\LAMS\cat.exe"
  File /a "..\..\LAMS\openlams.html"
  File /a "..\..\LAMS\showlams.html"
  File /a "..\..\LAMS\startlams.html"
  File /a "..\..\LAMS\manual.html"
  File /r "..\..\LAMS\openlams_files"
  ;File /r "..\..\LAMS\manual_files"
  File /a "..\..\LAMS\loading.png"
  File /a "..\..\LAMS\setenv.exe"
  File /a "..\..\LAMS\runh.exe"
  File /a "..\..\LAMS\start-lams.bat"
  File /a "..\..\LAMS\stop-lams.bat"
  File /a "..\..\LAMS\start-lams.exe"
  File /a "..\..\LAMS\stop-lams.exe"
  File /a "..\..\LAMS\update-lams.exe"
  File /a "..\..\LAMS\LocalPortScanner.class"
  ;File /a "..\..\LAMS\my.ini.chg"
  File /a "..\..\LAMS\my.ini"
  SetOutPath $TEMP
  File /a "..\..\LAMS\LocalPortScanner.class"
  SetOutPath $INSTDIR
  WriteUninstaller "uninstall-lams.exe"

  ;; now we add JAVA_HOME as a environment variable
  ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome"
  nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m JAVA_HOME "$2"'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR


  ;; Add LAMS install directory to classpath
  ReadEnvStr $R0 "CLASSPATH"
  StrCpy $R0 "$R0;$INSTDIR"
  System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i(CLASSPATH, $R0).r0'
SectionEnd

Section "Verify JDK Settings" JDKIDX
SetDetailsPrint listonly
;LogSet on
SectionIn RO


;AddSize 55000

${If} $JDKFlag = 2
;;; Adding JAVA_HOME

ReadEnvStr $R0 "PATH"

System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i(PATH, "$R0\bin;$R0").r0'
nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m JAVA_HOME "$JAVA_HOME"'
Pop $EXEC_RETURN_STATUS
Pop $EXEC_RETURN_ERROR

nsExec::ExecToStack '  "$INSTDIR\setenv.exe" -m PATH "$JAVA_HOME\bin;$R0"'
Pop $EXEC_RETURN_STATUS
Pop $EXEC_RETURN_ERROR
System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i(PATH, "$R0\bin;$R0").r0'

${EndIf}

SectionEnd

Section "Verify MySQL Settings" MYSQLIDX
SetDetailsPrint textonly
DetailPrint "Status: Configuring the LAMS server to work your with MySQL installation."
SetDetailsPrint listonly
;LogSet on
SectionIn RO
;AddSize 30000 



${If} $MysqlSetup != 1

        ReadEnvStr $R0 "PATH"

        ;System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i(MYSQL_HOME, $R3).r0'
        DetailPrint '"$INSTDIR\setenv.exe" -m MYSQL_HOME "$MYSQL_HOME"'
        nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m MYSQL_HOME "$MYSQL_HOME"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        DetailPrint '"$INSTDIR\setenv.exe" -m PATH "$MYSQL_HOME\bin;$R0"'
        ;System::Call 'Kernel32::SetEnvironmentVariableA(t, t) i(PATH, "$R0\bin;$R0").r0'
        nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m PATH "$MYSQL_HOME\bin;$R0"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ;handle string
        ClearErrors
        FileOpen $4 "$INSTDIR\mysql-config.bat" w
        IfErrors done
        FileWrite $4 '"$INSTDIR\setenv.exe" -m PATH "$MYSQL_HOME\bin;$R0"'
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"
        FileWrite $4 'set MYSQL_HOME=$MYSQL_HOME'
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"
        FileWrite $4 '"$INSTDIR\setenv.exe" -m PATH=$MYSQL_HOME\bin;$R0'
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"
        FileWrite $4 'set PATH=$MYSQL_HOME\bin;%PATH%'
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"


        FileWrite $4 'cd "$MYSQL_HOME\bin"'
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"
        FileWrite $4 ''
        FileWriteByte $4 "13"
        FileWriteByte $4 "10"

;${If} MysqlSetup != 1
;
;        FileWrite $4 'call "$MYSQL_HOME\bin\mysqld-nt.exe" --remove'
;        FileWriteByte $4 "13"
;       FileWriteByte $4 "10"
;       FileWrite $4 'call "$MYSQL_HOME\bin\mysqld-nt.exe" --install'
;        FileWriteByte $4 "13"
;        FileWriteByte $4 "10"
;        FileWrite $4 'call sc start mysql'
;        FileWriteByte $4 "13"
;        FileWriteByte $4 "10"
;${EndIf}

        FileClose $4
        sleep 1000


        DetailPrint '"$INSTDIR\mysql-config.bat"'
        nsExec::ExecToStack '"$INSTDIR\mysql-config.bat"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        sleep 3000
${Endif}

        FileOpen $FILE_HANDLE "$MYSQL_HOME\my.ini" a
        FileSeek $FILE_HANDLE -1 END
        FileWriteByte $FILE_HANDLE "13"
        FileWriteByte $FILE_HANDLE "10"
        FileWrite $FILE_HANDLE 'transaction-isolation = READ-COMMITTED'
        FileWriteByte $FILE_HANDLE "13"
        FileWriteByte $FILE_HANDLE "10"
        FileClose $FILE_HANDLE


;;;;; Mysql Config stuff

        ${If} InstallType = ${BASIC}
                ReadINIStr $R7 "$PLUGINSDIR\quick-parameters.ini" "Field 13" "State"
        ${ElseIf} InstallType = ${ADVANCED}
                ReadINIStr $R7 "$PLUGINSDIR\mysql-parameters.ini" "Field 9" "State"        
        ${EndIf}      

        ${If} MysqlSetup != 1
                nsExec::ExecToStack  '"$MYSQL_HOME\bin\mysqladmin.exe" -u root reload'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR

                nsExec::ExecToStack  '"$MYSQL_HOME\bin\mysqladmin.exe" -u root flush-privileges password "$R7"'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR

        ${Else}
                nsExec::ExecToStack  '"$MYSQL_HOME\bin\mysqladmin.exe" -u root reload -p$R7'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR

        ${EndIf}



;
;MessageBox MB_OK " $R3"

done:
  BringToFront
SectionEnd

SubSection "Pre-Requisite Tests" 

Section "Testing Java " 
SetDetailsPrint listonly
;LogSet on
  BeginTestSection:
  SectionIn RO

SectionEnd

Function FailedTest
SetOutPath "$INSTDIR"
SetDetailsPrint listonly
;LogSet on
  MessageBox MB_YESNO|MB_ICONSTOP "One or more tests failed! Click 'Yes' to continue if the reported problem has been resolved or click 'No' to abort and re-configure." IDNO end
  goto continue
  end:
  Abort
  continue:
FunctionEnd

Function RunTests
SetDetailsPrint textonly
DetailPrint "Status: Testing your 3rd party packages."
SetDetailsPrint listonly
;LogSet on
  ;WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Policies\Explorer" NORUN 1  

  SetOutPath "$TEMP"
  nsExec::ExecToStack ' $JAVA_HOME\bin\java -version'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  ;MessageBox MB_OK|MB_ICONSTOP "$JAVA_HOME"

  ${If} $EXEC_RETURN_STATUS = 1
  MessageBox MB_OK|MB_ICONSTOP "JRE (java) appears not to be working as expected!"
  call FailedTest
  ${EndIf}

  nsExec::ExecToStack '$JAVA_HOME\bin\javac -version'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  ${If} $EXEC_RETURN_STATUS = 1
        MessageBox MB_OK|MB_ICONSTOP "JDK (javac) appears not to be working as expected!"
        call FailedTest
  ${EndIf}

  sleep 1000
  ;MessageBox MB_OK " hmm1 = $1 $INSTDIR\LocalPortScanner"
  ${If} $InstallType = ${BASIC}
        call FindMySQLInstallation
        nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 1099 1098 4444 4445 8009 8083 8080 8090 8092 8093 9800'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
  ${Else}
        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 11" "State"
        ReadINIStr $R4 "$PLUGINSDIR\lams-parameters.ini" "Field 13" "State"

        nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 1099 1098 4444 4445 8009 8083 8090 8092 8093 $R3 $R4'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
  ${EndIf}

  ${If} $EXEC_RETURN_STATUS = 1
        ${If} InstallType = ${ADVANCED}
                MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
        ${EndIf}
  ${ElseIf} $EXEC_RETURN_STATUS = 2
          MessageBox MB_OK|MB_ICONSTOP "Not all JBoss-tomcat server mandatory ports are open. $\n$EXEC_RETURN_ERROR"
          call FailedTest
  ${ElseIf} $EXEC_RETURN_STATUS = 0
	;;
  ${Else}
          MessageBox MB_OK|MB_ICONSTOP "Not all JBoss-tomcat server mandatory ports are open. $\n$EXEC_RETURN_ERROR"
          call FailedTest
  ${EndIf}


  ${If} $MysqlSetup = 1
        nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 3306'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 1
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                        call FailedTest
                ${EndIf}

        ${ElseIf} $EXEC_RETURN_STATUS = 2
	;; Mysql is running
        ${ElseIf} $EXEC_RETURN_STATUS = 0
                MessageBox MB_OK|MB_ICONSTOP "Checking port 3306: MySQL is not running! Check that the MySQL install process is completed and MySQL is running!"
                call FailedTest
        ${Else}
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                ${EndIf}
        ${EndIf}

          ;MessageBox MB_OK|MB_ICONEXCLAMATION "Query: $MYSQL_HOME\bin\mysqladmin -u root -p$MysqlPassword"
          nsExec::ExecToStack '"$MYSQL_HOME\bin\mysqladmin.exe" -u root status -p$MysqlPassword'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR
          ${If} $EXEC_RETURN_STATUS != 0
                  MessageBox MB_OK|MB_ICONEXCLAMATION "Please set your current and correct password if MySQL has already been configured!"
                  Abort
          ${EndIf}
   ${Else}

          nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 3306'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 1
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                ${EndIf}
        ${ElseIf} $EXEC_RETURN_STATUS = 2
                  MessageBox MB_OK|MB_ICONSTOP "MySQL is already running!$\nPlease select the 'Already configured MySQL server option' and set your existing password if MySQL has already been configured!"
                  call FailedTest
	;; Mysql is running
        ${ElseIf} $EXEC_RETURN_STATUS = 0
        ${Else}
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                ${EndIf}
        ${EndIf}

          nsExec::ExecToStack '"$MYSQL_HOME\bin\mysqld-nt.exe" --install'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR
          ;MessageBox MB_OK|MB_ICONEXCLAMATION "$MYSQL_HOME\bin\mysqld-nt.exe --install"

          nsExec::ExecToStack '$INSTDIR\move.bat "$INSTDIR\my.ini" "$MYSQL_HOME\my.ini"'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR
          ;MessageBox MB_OK|MB_ICONEXCLAMATION "$MYSQL_HOME\bin\mysqld-nt.exe --install"

          
          sleep 250
          nsExec::ExecToStack '"$INSTDIR\sc.exe" start mysql'
          Pop $EXEC_RETURN_STATUS

          Pop $EXEC_RETURN_ERROR
          ;MessageBox MB_OK|MB_ICONEXCLAMATION "sc start mysql"
          
          sleep 250
          nsExec::ExecToStack '$JAVA_HOME\bin\java LocalPortScanner 3306'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 1
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                ${EndIf}
        ${ElseIf} $EXEC_RETURN_STATUS = 2
	;; Mysql is running
        ${ElseIf} $EXEC_RETURN_STATUS = 0
                MessageBox MB_OK|MB_ICONSTOP "Checking port 3306: MySQL is not running! Check that the MySQL install process is completed and MySQL is running!"
                call FailedTest
        ${Else}
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem! Continuing anyway..."
                ${EndIf}
        ${EndIf}

          ;MessageBox MB_OK|MB_ICONEXCLAMATION "Query: $MYSQL_HOME\bin\mysqladmin -u root status"
          nsExec::ExecToStack '"$MYSQL_HOME\bin\mysqladmin.exe" -u root status'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR
          ${If} $EXEC_RETURN_STATUS != 0
                  MessageBox MB_OK|MB_ICONEXCLAMATION "MySQL may have already been configured or is not running as expected!$\nPlease select the 'Already configured MySQL server option' and set your existing password if MySQL has already been configured!"
                  Abort
          ${EndIf}
;; DEBUG
;          ${If} $EXEC_RETURN_STATUS != 0
;                MessageBox MB_OK|MB_ICONEXCLAMATION "The entered MySQL root password may be incorrect!$\nMySQL Reported Error: $EXEC_RETURN_ERROR!"
;                Abort
;          ${EndIf}

  ${Endif}        
  BringToFront
FunctionEnd

Section "Checking Mandatory Ports" 

  SectionIn RO

SectionEnd


Section "Testing Mysql"

  SectionIn RO
SectionEnd

SubSectionEnd


SubSection /e "LAMS Server Install"

Section "Install JBoss"
  SectionIn RO

SectionEnd

Section "Install Java Service Wrapper"
  SectionIn RO

SectionEnd

Section "Install LAMS"
SectionIn RO
AddSize 55000
        ${If} $InstallType = ${BASIC}
                call BasicInstallLAMS
        ${Else}
                call AdvancedInstallLAMS
        ${EndIf}

SectionEnd

SubSectionEnd

Function "BasicInstallLAMS"
        SetOutPath "$INSTDIR"
        SetDetailsPrint textonly
        DetailPrint "Status: Configuring the pre-installation settings of the LAMS server."
        SetDetailsPrint listonly
        ;LogSet on
        ;handle string

        ClearErrors
        FileOpen $5 "$INSTDIR\lams-package\ant\basic.properties" a
        FileSeek $5 0 SET
        IfErrors error

        ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\MySQL" "DisplayName"
        nsExec::ExecToStack '"$INSTDIR\setenv" -m MYSQL_SERVICE_NAME "$1"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

	FileWrite $5 'MYSQL_CONNECTOR_JAR_FILE=mysql-connector-java-3.1.10-bin.jar$\r$\n'
        FileWrite $5 'MYSQL_SERVICE_NAME=$1$\r$\n'
        
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        ; Adding LAMS_JBOSS_HOME to JBOSS_HOME - overwrites current setting!!!!!!!!!
        ; Also adding to path

        FileWrite $5 'LAMS_COMMUNITY_ONLINE=false$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "lams_community_online" "false"

        Push $INSTDIR
        Call ConvertBStoFS
        Pop $R3
        FileWrite $5 'LAMS_JBOSS_HOME_WINDOWS=$R3/jboss$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "jboss_home" "$INSTDIR\jboss"
                                                          
        nsExec::ExecToStack '"$INSTDIR\setenv" -m JBOSS_HOME "$INSTDIR\jboss"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        
        ReadEnvStr $R0 "PATH"

        nsExec::ExecToStack '"$INSTDIR\setenv" -m PATH "$R3\bin;$R0"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        SetOutPath $INSTDIR
        File /r "..\..\LAMS\jboss"

        Push $INSTDIR
        Call ConvertBStoFS
        Pop $R3
        FileWrite $5 'LAMS_HOME_WINDOWS=$R3/lamsdata$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "lams_upload_directory" "$INSTDIR\lamsdata"

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 15" "State"
        FileWrite $5 'LAMS_ADMIN_USER=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "admin_user" "$R3"
        
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 17" "State"
        FileWrite $5 'LAMS_ADMIN_PASSWORD=$R3$\r$\n'
        
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 13" "State"
        FileWrite $5 'LAMS_DB_ROOT_PASSWORD=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_key" "$R3"
                
        FileWrite $5 'LAMS_DB_PASSWORD=$R3$\r$\n'
        
        FileWrite $5 'LAMS_DB_NAME=lamsone$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_dbname" "lamsone"

        FileWrite $5 'LAMS_DB_USER=lams$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_user" "lams"
                                                                  
        FileWrite $5 'LAMS_MYSQL_HOST=localhost$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_host" "localhost"

        FileWrite $5 'LAMS_JBOSS_HOST=localhost$\r$\n'

        FileWrite $5 'LAMS_TOMCAT_PORT=8080$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "tomcat_port" "8080"

        FileWrite $5 'LAMS_CHAT_PORT=9800$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "chat_port" "9800"

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 9" "State"
        FileWrite $5 'LAMS_ADMIN_EMAIL_ADDRESS=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 11" "State"
        FileWrite $5 'LAMS_SMTP_SERVER=$R3$\r$\n'

        FileClose $5
        sleep 250        

        ClearErrors
        FileOpen $4 "$INSTDIR\lams-config.bat" w

        IfErrors error
        FileWrite $4 'cd "$INSTDIR\lams-package\ant" $\r$\n'

        ; Find that MYSQL Service!!!

        ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\MySQL" "DisplayName"
        nsExec::ExecToStack '"$INSTDIR\setenv" -m MYSQL_SERVICE_NAME "$1"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
 
        FileWrite $4 '"$INSTDIR\setenv.exe" -m MYSQL_SERVICE_NAME "$1"$\r$\n'
	;FileWrite $4 '"$INSTDIR\setenv.exe" -m JAVA_HOME "$JAVA_HOME"$\r$\n'
        
        FileWrite $4 'set MYSQL_SERVICE_NAME=$1$\r$\n'
        
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        ; Adding LAMS_JBOSS_HOME to JBOSS_HOME - overwrites current setting!!!!!!!!!
        ; Also adding to path

        FileWrite $4 'set LAMS_COMMUNITY_ONLINE=false$\r$\n'

        Push $INSTDIR
        Call ConvertBStoFS
        Pop $R3
        FileWrite $4 'set LAMS_JBOSS_HOME_WINDOWS=$R3/jboss$\r$\n'
                                                          
        nsExec::ExecToStack '"$INSTDIR\setenv" -m JBOSS_HOME "$INSTDIR\jboss"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        
        ReadEnvStr $R0 "PATH"

        nsExec::ExecToStack '"$INSTDIR\setenv" -m PATH "$R3\bin;$R0"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        SetOutPath $INSTDIR
        File /r "..\..\LAMS\jboss"

        Push $INSTDIR
        Call ConvertBStoFS
        Pop $R3
        FileWrite $4 'set LAMS_HOME_WINDOWS=$R3/lamsdata$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 15" "State"
        FileWrite $4 'set LAMS_ADMIN_USER=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 17" "State"
        FileWrite $4 'set LAMS_ADMIN_PASSWORD=$R3$\r$\n'
        
        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 13" "State"
        FileWrite $4 'set LAMS_DB_ROOT_PASSWORD=$R3$\r$\n'
        
        FileWrite $4 'set LAMS_DB_PASSWORD=$R3$\r$\n'
         
        FileWrite $4 'set LAMS_DB_NAME=lamsone$\r$\n'

        FileWrite $4 'set LAMS_DB_USER=lams$\r$\n'
                                                                  
        FileWrite $4 'set LAMS_MYSQL_HOST=localhost$\r$\n'

        FileWrite $4 'set LAMS_JBOSS_HOST=localhost$\r$\n'

        FileWrite $4 'set LAMS_TOMCAT_PORT=8080$\r$\n'

        FileWrite $4 'set LAMS_CHAT_PORT=9800$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 9" "State"
        FileWrite $4 'set LAMS_ADMIN_EMAIL_ADDRESS=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\quick-parameters.ini" "Field 11" "State"
        FileWrite $4 'set LAMS_SMTP_SERVER=$R3$\r$\n'

        FileWrite $4 'call "$INSTDIR\apache-ant\bin\ant.bat" prepare-db >> "$INSTDIR\lams-config.log" 2>&1 $\r$\n'

        FileWrite $4 'call "$INSTDIR\apache-ant\bin\ant.bat" install >> "$INSTDIR\lams-config.log" 2>&1 $\r$\n'

        ;;;;;;;;;;;
        ; Adding Java Service Wrapper
        
        ;; Basic install users cant change jboss home"
        
        StrCpy $R3 "$INSTDIR\jboss"

        FileWrite $4 'copy "$INSTDIR\wrapper\bin\wrapper.exe" "$R3\bin\wrapper.exe"$\r$\n'

        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\App.bat.in" "$R3\bin\JBoss.bat"$\r$\n'

        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\InstallApp-NT.bat.in" "$R3\bin\InstallLams.bat"$\r$\n'
        
        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\UnInstallApp-NT.bat.in" "$R3\bin\UnInstallLams.bat"$\r$\n'

        FileWrite $4 'copy "$INSTDIR\wrapper\lib\wrapper.dll" "$R3\lib\wrapper.dll"$\r$\n'
      
        FileWrite $4 'copy "$INSTDIR\wrapper\lib\wrapper.jar" "$R3\lib\wrapper.jar"$\r$\n'
        
        FileWrite $4 'mkdir $R3\conf$\r$\n'

        
        FileWrite $4 'copy "$INSTDIR\wrapper\src\conf\wrapper.conf.in" "$R3\conf\wrapper.conf"$\r$\n'

        FileWrite $4 'cd $R3\bin$\r$\n'

        FileWrite $4 'call "$INSTDIR\sc.exe" delete lams$\r$\n'

        FileWrite $4 'call InstallLams.bat >> lams-config.log 2>&1$\r$\n'

        FileWrite $4 '"$INSTDIR\sc.exe" start lams >> lams-config.log 2>&1$\r$\n'

        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\openlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:8080/g" > $INSTDIR\newlams.html"$\r$\n'

        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\showlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:8080/g" > $INSTDIR\menulams.html"$\r$\n'

        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\startlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:8080/g" > $INSTDIR\servicelams.html"$\r$\n'


        FileWrite $4 'REM Add -d and run again for debugging'

        FileClose $4
        sleep 250

        SetDetailsPrint textonly
        DetailPrint "Status: Configuring JBoss and installing the LAMS binary packages."
        SetDetailsPrint listonly
        ;LogSet on

        nsExec::ExecToStack '$INSTDIR\lams-config.bat'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        SetDetailsPrint textonly
        DetailPrint "Status: Installing the LAMS Windows service to work in Manual mode."
        sleep 5000  ; Lets wait a little while LAMS starts
        DetailPrint "Status: For 24/7 production servers please change your LAMS Windows service to Automatic."
        sleep 5000  ; Lets wait a little while LAMS starts
        SetDetailsPrint listonly
        ;LogSet on

        WriteRegStr HKLM SOFTWARE\LAMS\Server "install_status" "successful"
        call InstallStartMenu
        goto done
        error:
                MessageBox MB_OK "Error: Cannot open the LAMS configuration script file. Installation has failed!"
                Quit
        done:   
                BringToFront
FunctionEnd

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Advanced Install


Function "AdvancedInstallLAMS"
        SetOutPath "$INSTDIR"
        SetDetailsPrint textonly
        DetailPrint "Status: Configuring the pre-installation settings of the LAMS server."
        SetDetailsPrint listonly
        ;LogSet on
        ;handle string

        ClearErrors
        FileOpen $5 "$INSTDIR\lams-package\ant\basic.properties" a
        FileSeek $5 0 SET
        IfErrors error
        FileWrite $5 '"$INSTDIR\setenv.exe" -m MYSQL_SERVICE_NAME "$1"$\r$\n'

        FileWrite $5 'MYSQL_SERVICE_NAME=$1$\r$\n'
 
        FileWrite $5 'LAMS_COMMUNITY_ONLINE=false$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "lams_community_online" "$R3"

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        ; Adding LAMS_JBOSS_HOME to JBOSS_HOME - overwrites current setting!!!!!!!!!
        ; Also adding to path

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 3" "State"
        ; fix the path if needed
        Push $R3
        call FixPath
        Pop $R3

        ; fiddle with the path just for LAMS
        Push $R3
        Call ConvertBStoFS
        Pop $R3
        FileWrite $5 'LAMS_JBOSS_HOME_WINDOWS=$R3$\r$\n'
                                                                        
        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 5" "State"
        Push $R3
        call FixPath
        Pop $R3

        WriteRegStr HKLM SOFTWARE\LAMS\Server "lams_upload_directory" "$R3"
        Push $R3
        Call ConvertBStoFS
        Pop $R3
        FileWrite $5 'LAMS_HOME_WINDOWS=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 7" "State"
        FileWrite $5 'LAMS_ADMIN_USER=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "admin_user" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 9" "State"
        FileWrite $5 'LAMS_ADMIN_PASSWORD=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 9" "State"
        FileWrite $5 'LAMS_DB_ROOT_PASSWORD=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_key" "$R3"        
	
	FileWrite $5 'MYSQL_CONNECTOR_JAR_FILE=mysql-connector-java-3.1.10-bin.jar$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 5" "State"
        FileWrite $5 'LAMS_DB_NAME=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_dbname" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 7" "State"
        FileWrite $5 'LAMS_DB_USER=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_user" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 11" "State"
        FileWrite $5 'LAMS_DB_PASSWORD=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 13" "State"
        FileWrite $5 'LAMS_MYSQL_HOST=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_host" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 8" "State"
        FileWrite $5 'LAMS_JBOSS_HOST=$R3$\r$\n'
        StrCpy $R5 $R3

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 11" "State"
        FileWrite $5 'LAMS_TOMCAT_PORT=$R3$\r$\n'
        StrCpy $R6 $R3
        WriteRegStr HKLM SOFTWARE\LAMS\Server "tomcat_port" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 13" "State"
        FileWrite $5 'LAMS_CHAT_PORT=$R3$\r$\n'
        WriteRegStr HKLM SOFTWARE\LAMS\Server "chat_port" "$R3"

        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 12" "State"
        FileWrite $5 'LAMS_EMAIL_ADDRESS=$R3$\r$\n'

        ; XXX
        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 14" "State"
        FileWrite $5 'LAMS_SMTP_SERVER=$R3$\r$\n'     

        FileClose $5
        sleep 250

        ClearErrors
        FileOpen $4 "$INSTDIR\lams-config.bat" w
        IfErrors error
        FileWrite $4 'cd "$INSTDIR\lams-package\ant" $\r$\n'

        ; Find that MYSQL Service!!!

        ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\MySQL" "DisplayName"
        nsExec::ExecToStack '"$INSTDIR\setenv" -m MYSQL_SERVICE_NAME "$1"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        FileWrite $4 '"$INSTDIR\setenv.exe" -m MYSQL_SERVICE_NAME "$1"$\r$\n'
	;FileWrite $4 '"$INSTDIR\setenv.exe" -m JAVA_HOME "$JAVA_HOME"$\r$\n'

        FileWrite $4 'set MYSQL_SERVICE_NAME=$1$\r$\n'
 
        FileWrite $4 'set LAMS_COMMUNITY_ONLINE=false$\r$\n'

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        ; Adding LAMS_JBOSS_HOME to JBOSS_HOME - overwrites current setting!!!!!!!!!
        ; Also adding to path

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 3" "State"
        ; fix the path if needed
        Push $R3
        call FixPath
        Pop $R3

        ${If} $R3 != "$INSTDIR\jboss"
                ;MessageBox MB_OK '"$INSTDIR\move.bat "$INSTDIR\jboss" "$R3"'
                DetailPrint '"$INSTDIR\move.bat" "$INSTDIR\jboss" "$R3"'
                nsExec::ExecToStack '"$INSTDIR\move.bat" "$INSTDIR\jboss" "$R3"'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR
                DetailPrint '$EXEC_RETURN_ERROR'
                ;MessageBox MB_OK 'Status: $EXEC_RETURN_STATUS'
        ${EndIf}

        WriteRegStr HKLM SOFTWARE\LAMS\Server "jboss_home" "$R3"
        nsExec::ExecToStack '"$INSTDIR\setenv" -m JBOSS_HOME "$R3"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        ReadEnvStr $R0 "PATH"

        ; fiddle with the path just for LAMS
        Push $R3
        Call ConvertBStoFS
        Pop $R3
        FileWrite $4 'set LAMS_JBOSS_HOME_WINDOWS=$R3$\r$\n'                                                                        

        nsExec::ExecToStack '"$INSTDIR\setenv" -m PATH "$R3\bin;$R0"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        SetOutPath $INSTDIR
        File /r "..\..\LAMS\jboss"

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 5" "State"
        Push $R3
        call FixPath
        Pop $R3

        Push $R3
        Call ConvertBStoFS
        Pop $R3
        FileWrite $4 'set LAMS_HOME_WINDOWS=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 7" "State"
        FileWrite $4 'set LAMS_ADMIN_USER=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 9" "State"
        FileWrite $4 'set LAMS_ADMIN_PASSWORD=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 9" "State"
        FileWrite $4 'set LAMS_DB_ROOT_PASSWORD=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 5" "State"
        FileWrite $4 'set LAMS_DB_NAME=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 7" "State"
        FileWrite $4 'set LAMS_DB_USER=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 11" "State"
        FileWrite $4 'set LAMS_DB_PASSWORD=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\mysql-parameters.ini" "Field 13" "State"
        FileWrite $4 'set LAMS_MYSQL_HOST=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 8" "State"
        FileWrite $4 'set LAMS_JBOSS_HOST=$R3$\r$\n'
        StrCpy $R5 $R3

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 11" "State"
        FileWrite $4 'set LAMS_TOMCAT_PORT=$R3$\r$\n'
        StrCpy $R6 $R3

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 13" "State"
        FileWrite $4 'set LAMS_CHAT_PORT=$R3$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 15" "State"

        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 12" "State"
        FileWrite $4 'set LAMS_EMAIL_ADDRESS=$R3$\r$\n'

        ; XXX
        ReadINIStr $R3 "$PLUGINSDIR\server-parameters.ini" "Field 14" "State"
        FileWrite $4 'set LAMS_SMTP_SERVER=$R3$\r$\n'

        FileWrite $4 'call "$INSTDIR\apache-ant\bin\ant.bat" prepare-db >> "$INSTDIR\lams-config.log" 2>&1 $\r$\n'

        FileWrite $4 'call "$INSTDIR\apache-ant\bin\ant.bat" install >> "$INSTDIR\lams-config.log" 2>&1 $\r$\n'


        ;;;;;;;;;;;
        ; Adding Java Service Wrapper



        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 3" "State"

        FileWrite $4 'copy "$INSTDIR\wrapper\bin\wrapper.exe" "$R3\bin\wrapper.exe"$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\App.bat.in" "$R3\bin\JBoss.bat"$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\InstallApp-NT.bat.in" "$R3\bin\InstallLams.bat"$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\src\bin\UnInstallApp-NT.bat.in" "$R3\bin\UnInstallLams.bat"$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\lib\wrapper.dll" "$R3\lib\wrapper.dll"$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\lib\wrapper.jar" "$R3\lib\wrapper.jar"$\r$\n'
        FileWrite $4 'mkdir $R3\conf$\r$\n'
        FileWrite $4 'copy "$INSTDIR\wrapper\src\conf\wrapper.conf.in" "$R3\conf\wrapper.conf"$\r$\n'
        FileWrite $4 'cd $R3\bin$\r$\n'
        FileWrite $4 'REM call UnInstallLams.bat$\r$\n'
        FileWrite $4 'call InstallLams.bat >> lams-config.log 2>&1$\r$\n'
        FileWrite $4 '"$INSTDIR\sc.exe" start lams >> lams-config.log 2>&1$\r$\n'

        ReadINIStr $R3 "$PLUGINSDIR\lams-parameters.ini" "Field 11" "State"
        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\openlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:$R3/g" > $INSTDIR\newlams.html"$\r$\n'
        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\showlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:$R3/g" > $INSTDIR\menulams.html"$\r$\n'
        FileWrite $4 'call "$INSTDIR\cat.exe" "$INSTDIR\startlams.html" | "$INSTDIR\sed.exe" -e "s/LAMS_HOST_XYZ_PORT/LOCALHOST:$R3/g" > $INSTDIR\servicelams.html"$\r$\n'
        FileWrite $4 'REM Add -d and run again for debugging'
        FileClose $4
        sleep 250

        SetDetailsPrint textonly
        DetailPrint "Status: Configuring JBoss and installing the LAMS binary packages."
        SetDetailsPrint listonly

        nsExec::ExecToStack '$INSTDIR\lams-config.bat'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        SetDetailsPrint textonly
        DetailPrint "Status: Installing the LAMS Windows service to work in Manual mode."
        sleep 5000  ; Lets wait a little while LAMS starts
        DetailPrint "Status: For 24/7 production servers please change your LAMS Windows service to Automatic."
        sleep 5000  ; Lets wait a little while LAMS starts
        SetDetailsPrint listonly 


        WriteRegStr HKLM SOFTWARE\LAMS\Server "install_status" "successful"
        call InstallStartMenu
	goto done

        error:
                MessageBox MB_OK "Error: Cannot open the LAMS configuration script file. Installation has failed!"
                Quit
        done:
                BringToFront


FunctionEnd

;--------------------------------

Function "OpenLAMSConsole"
SetOutPath "$INSTDIR"
SetDetailsPrint listonly
;LogSet on
;MessageBox MB_OK "Opening the LAMS Login Console in your browser window!"
;ExecShell "open" '"$INSTDIR"'
ExecShell "open" '"$INSTDIR\newlams.html"'
BringToFront

FunctionEnd

Function "InstallStartMenu"
  SetOutPath "$INSTDIR"
  SetDetailsPrint listonly
  ;LogSet on
  CreateDirectory "$SMPROGRAMS\LAMS"
  SetOutPath $INSTDIR ; for working directory
  ; this one will use notepad's icon, start it minimized, and give it a hotkey (of Ctrl+Shift+Q)
  Delete "$SMPROGRAMS\LAMS\Uninstall LAMS.lnk"
  Delete "$SMPROGRAMS\LAMS\Stop LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\Start LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\Update LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\LAMS Server Login Page.lnk"
  CreateShortCut "$SMPROGRAMS\LAMS\LAMS Server Login Page.lnk" "$INSTDIR\menulams.html" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$SMPROGRAMS\LAMS\Start LAMS Server.lnk" "$INSTDIR\start-lams.exe" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$SMPROGRAMS\LAMS\Stop LAMS Server.lnk" "$INSTDIR\stop-lams.exe" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$SMPROGRAMS\LAMS\Update LAMS Server.lnk" "$INSTDIR\update-lams.exe" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$SMPROGRAMS\LAMS\Uninstall LAMS.lnk" "$INSTDIR\uninstall-lams.exe" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$DESKTOP\Start LAMS Server.lnk" "$INSTDIR\start-lams.exe" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
  CreateShortCut "$DESKTOP\LAMS Server Login Page.lnk" "$INSTDIR\menulams.html" "..\..\LAMS\lams2.ico"; use defaults for parameters, icon, etc.
FunctionEnd


;--------------------------------

; Uninstaller

UninstallText "The LAMS Server will be uninstalled. Click next to continue."
;UninstallIcon "..\..\LAMS\logo.ico"

Section "Uninstall"

  nsExec::ExecToStack '"$INSTDIR\sc.exe" stop lams'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  ;MessageBox MB_OK "Removing JBoss at $1"
  ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "jboss_home"

  nsExec::ExecToStack '"$1\bin\UnInstallLams.bat"'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  ${If} $1 != ""
        Delete "$1\*.*"
        RMDir /r "$1"
  ${EndIf}

  ;; gets ride of all the environment variables that we set

  nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m JAVA_HOME -delete'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m MYSQL_SERVICE_NAME -delete'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  nsExec::ExecToStack '"$INSTDIR\setenv.exe" -m JBOSS_HOME -delete'
  Pop $EXEC_RETURN_STATUS
  Pop $EXEC_RETURN_ERROR

  ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "lams_upload_directory"
  ;MessageBox MB_OK "Removing LAMS uploads at $1"
  ${If} $1 != ""
        DetailPrint "Moving the lams upload directory $1 to $TEMP\$1"
        sleep 250
        nsExec::ExecToStack '"$INSTDIR\move.bat" "$1" "$TEMP\$1"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        DetailPrint '$EXEC_RETURN_ERROR'
  ${EndIf}
  
  Delete "$INSTDIR\apache-ant"
  Delete "$INSTDIR\wrapper"
  Delete "$INSTDIR\lams-package"
  Delete "$INSTDIR\LocalPortScanner.class"
  Delete "$INSTDIR\CallOS.class"
  Delete "$INSTDIR\sed.exe"
  Delete "$INSTDIR\setenv.exe"
  Delete "$INSTDIR\cat.exe"
  Delete "$INSTDIR\openlams.html"
  Delete "$INSTDIR\openlams_files"
  Delete "$INSTDIR\*.*"
  Delete "$SMPROGRAMS\LAMS\Uninstall LAMS.lnk"
  Delete "$SMPROGRAMS\LAMS\Stop LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\Start LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\Update LAMS Server.lnk"
  Delete "$SMPROGRAMS\LAMS\LAMS Server Login Page.lnk"
  Delete "$DESKTOP\LAMS Server Login Page.lnk"
  Delete "$SMPROGRAMS\LAMS\*Server*.lnk"
  Delete "$DESKTOP\Start LAMS Server.lnk"
;;  Delete "$SMPROGRAMS\LAMS"
  RMDir /r "$INSTDIR"

  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\LAMS Server"
  DeleteRegKey HKLM "SOFTWARE\LAMS\Server"
  WriteRegStr HKLM SOFTWARE\LAMS\Server "install_status" "uninstalled"

;DeleteRegKey HKLM "SOFTWARE\LAMS"
;; This needs to be fixed?


SectionEnd

Function CheckINSTDIR
        SetOutPath "$INSTDIR"
        StrCpy $1 "Error: The selected installaton directory has a space/s.$\nPlease remove all spaces."
        Push $1
        Push $INSTDIR
        call CheckForSpaces
FunctionEnd        


Function CheckForSpaces
 Pop $0
 Pop $1
 Push $R1
 Push $R2
 Push $R3
 Push $R4
 StrCpy $R1 -1
 StrCpy $R3 0
 loop:
   StrCpy $R2 $0 1 $R1
   IntOp $R1 $R1 - 1
   StrCmp $R2 "" done
   StrCmp $R2 " " found
   StrCpy $R4 "$R2$R4"
 Goto loop
 found:
   IntOp $R3 $R3 + 1
 Goto loop
 done:
   StrCmp $R3 0 +3
   MessageBox MB_OK|MB_ICONEXCLAMATION "$1"
   Abort
 Pop $R4
 Pop $R3
 Pop $R2
 Pop $R1
FunctionEnd

Function GetNextIp
  Exch $0
  Push $1
  Push $2
  Strcpy $2 0             ; Counter
  Loop:
    IntOp $2 $2 + 1
    StrCpy $1 $0 1 $2
    StrCmp $1 '' ExitLoop
    StrCmp $1 ';' '' Loop
    StrCpy $1 $0 $2       ; IP-address
    IntOp $2 $2 + 1
    StrCpy $0 $0 '' $2    ; Remaining string
  ExitLoop:
  Pop $2
  Push $0
  Exch $2
  Pop $0
  Exch $1
FunctionEnd

Function ConvertBStoFS
 Exch $R0 ;input string
 Push $R1
 Push $R2
 StrCpy $R1 0
loop:
  IntOp $R1 $R1 - 1
  StrCpy $R2 $R0 1 $R1
  StrCmp $R2 "" done
 StrCmp $R2 "\" 0 loop
  StrCpy $R2 $R0 $R1 ;part before
   Push $R1
  IntOp $R1 $R1 + 1
  StrCpy $R1 $R0 "" $R1 ;part after
 StrCpy $R0 "$R2/$R1"
   Pop $R1
  IntOp $R1 $R1 - 1
Goto loop
done:
   Pop $R2
   Pop $R1
   Exch $R0 ;output string
FunctionEnd


Function .onSelChange


;;;;;;;;;;;;;;;;;;;;
;; Commented out the selection change code as MYSQL and JDK no longer run in the code

;  SectionGetText ${JDKIDX} $0
;  SectionGetFlags ${JDKIDX} $1
;  !insertmacro SectionFlagIsSet ${JDKIDX} ${SF_SELECTED} "" e
;  ${If} $JDKFlag = 2
;        goto e2
;  ${EndIf}
;        StrCpy $JDKFlag 0
;        SectionSetText ${JDKIDX} "Launch JDK Installer"
;
;  goto e2
;  e:
;  StrCpy $JDKFlag 1
;  SectionSetText ${JDKIDX} "Skip JDK Installer"
;  e2:
;  
;  SectionGetText ${MYSQLIDX} $0
;  SectionGetFlags ${MYSQLIDX} $1
;  ${If} $MysqlFlag = 2
;        goto e4
;  ${EndIf}
; !insertmacro SectionFlagIsSet ${MYSQLIDX} ${SF_SELECTED} "" e3
; StrCpy $MysqlFlag 0
;  SectionSetText ${MYSQLIDX} "Launch MySQL Installer"
;  goto e4
;  e3:
;  StrCpy $MysqlFlag 1
;  SectionSetText ${MYSQLIDX} "Skip MySQL Installer"
;  e4:

FunctionEnd

 ; GetParent
 ; input, top of stack  (e.g. C:\Program Files\Poop)
 ; output, top of stack (replaces, with e.g. C:\Program Files)
 ; modifies no other variables.
 ;
 ; Usage:
 ;   Push "C:\Program Files\Directory\Whatever"
 ;   Call GetParent
 ;   Pop $R0
 ;   ; at this point $R0 will equal "C:\Program Files\Directory"

 Function GetParent
 
   Exch $R0
   Push $R1
   Push $R2
   Push $R3
   
   StrCpy $R1 0
   StrLen $R2 $R0
   
   loop:
     IntOp $R1 $R1 + 1
     IntCmp $R1 $R2 get 0 get
     StrCpy $R3 $R0 1 -$R1
     StrCmp $R3 "\" get
     Goto loop
   
   get:
     StrCpy $R0 $R0 -$R1
     
     Pop $R3
     Pop $R2
     Pop $R1
     Exch $R0
     
 FunctionEnd
