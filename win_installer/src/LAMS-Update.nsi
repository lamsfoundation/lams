;; Coded by C.K. Nimmagadda
;; Recode by Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
;; For LAMS Server Version 1.0.1 Post-Yoichi
;; Copyright 2005 (c) LAMS International
;; http://www.lamsinternational.com/

;; TODO
;  More user input validation
;  Better windows set environment (currently assuming the installer will only run once)
;  Indentation/style
;  Filewrites/file grouping
;  breaking up functions


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 2006-04-24 (jliew)
; - updated to 1.0.2(20060424)
; 2006-03-21 (jliew)
; - updated to 1.0.2(20060223)
; - works on 1.0.1(launch cd, yoichi) and 1.0.2(20051019)
; - removed .jar files that weren't updated (cuts file size by approx. 33%)
; - cleaned up some community/orgid code and files
;
;; DEVELOPMENT NOTES (by ErnieG)
;
; This upgrader will *only* work if the previous version of LAMS is 1.0.1 of build (Yoichi / 20050501)
; 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;---------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
!include "LogicLib.nsh"
!define ALL_USERS
!include WriteEnvStr.nsh

;!macro BIMAGE IMAGE PARMS
;   Push $0
;   GetTempFileName $0
;   File /oname=$0 "${IMAGE}"
;   SetBrandingImage ${PARMS} $0
;   Delete $0
;   Pop $0
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
!define LIVERSION "1.0 Beta"
!define RESTORE_VERSION "1"
        
Name "LAMS ${VERSION} Update (${BUILD})"
Caption "LAMS International - Windows Update ${VERSION} (${BUILD})"
OutFile "LAMS-Update-${VERSION}-${BUILD}.exe"
;InstallDir "c:\lamsbackups"
InstallDir "$PROGRAMFILES\LAMS\Server\Restore"
InstallDirRegKey HKLM "Software\LAMS\Server" ""
LicenseForceSelection radiobuttons "I Agree" "I Do Not Agree" 
CheckBitmap "..\..\Contrib\Graphics\Checks\classic-cross.bmp"

BrandingText "http://www.lamsinternational.com/"
LicenseText ""
LicenseData "..\..\LAMS-Update\license.txt"
SetDateSave on
SetDatablockOptimize on
CRCCheck on
SilentInstall normal
;BGGradient 000000 800000 FFFFFF
;InstallColors FF8080 000030 
XPStyle on
;;;LogSet on
;--------------------------------

!ifndef NOINSTTYPES ; only if not defined
  InstType "Full Install (LAMS, JDK, MySQL)"
  InstType "LAMS, MySQL, No JDK"
  InstType "LAMS, JDK, No MySQL"
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

!define AYE 0
!define NAY 1

!define UPDATE 0
!define REVERT 1

!define BACKUP_BASIC 0
!define BACKUP_DB_DIR 1
!define BACKUP_LAMS_DIR 2
!define BACKUP_FULL 3


;;!define NSIS_CONFIG_LOG "update.log"
;--------------------------------
;Interface Settings

;  !define MUI_ABORTWARNING
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP  "..\..\LAMS-Update\images\header.bmp"
!define MUI_HEADERIMAGE_RIGHT

!define MUI_COMPONENTSPAGE_NODESC ;No value


;!insertmacro MUI_DEFAULT MUI_ICON "..\..\LAMS\logo2.ico"


!define MUI_ICON "..\..\LAMS-Update\images\lams2.ico"
!define MUI_UNICON "..\..\LAMS-Update\images\lams2.ico"

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
  !define MUI_PAGE_HEADER_TEXT "LAMS Server Update License Agreement"
  !define MUI_PAGE_HEADER_SUBTEXT  "Please review and accept the LAMS Server Update license terms."
  !insertmacro MUI_PAGE_LICENSE "..\..\LAMS-Update\license.txt"

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; License for server
;
;

;  !define MUI_LICENSEPAGE_TEXT_TOP "Scroll down to see the rest of the agreement."
;  !insertmacro MUI_PAGE_LICENSE "..\..\LAMS\license.txt"
  !define MUI_PAGE_HEADER_TEXT "Select Backups Storage Directory"
  !define MUI_PAGE_HEADER_SUBTEXT  ""
  !define MUI_DIRECTORYPAGE_TEXT_TOP "Please select the directory where a backup of the current LAMS installation will be stored. Backups are necessary to revert to the current installation if the update is unsuccessful."
         

  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE "ConfirmExistingLAMS" 
  !insertmacro MUI_PAGE_DIRECTORY  

;  Page custom SelectLevel LeaveSelectLevel
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Components options
;
;
;  !define MUI_COMPONENTSPAGE_TEXT_TOP "The default option is a full installation. For additional customisation select a different installation sequence select or deselect individual components. Click Next to continue."
;  !define MUI_COMPONENTSPAGE_TEXT_COMPLIST "Individual installation components, tests or functions:"
;  !define MUI_COMPONENTSPAGE_TEXT_INSTTYPE "Select installation sequence:"
;  !define MUI_PAGE_CUSTOMFUNCTION_LEAVE "LeaveComponentPage" 
;  !insertmacro MUI_PAGE_COMPONENTS

  Page custom InstallationSettings LeaveInstallationSettings
;  Page custom RegisterCommunity CheckOrgId
  Page custom UpdateSettings LeaveUpdateSettings

  
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

!define MUI_FINISHPAGE_TEXT "The LAMS Server has been successfully updated."
#!define MUI_FINISHPAGE_RUN_NOTCHECKED

#!define MUI_FINISHPAGE_SHOWREADME "$TEMP_DIR\lams.html"   ;; ARE YOU SURE THIS WORKS????
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

  ReserveFile "installation-settings.ini"
  ReserveFile "update-settings.ini"

  !insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

;--------------------------------
;Variables

 
  Var LAMS_FOUND
  Var LAMS_HOME
  Var LAMS_JBOSS_HOME
  Var BACKUP_TYPE
  Var BACKUP_DIR   

  Var TEST_FLAG

  Var RETURN_VARIABLE
  Var EXEC_RETURN_STATUS
  Var EXEC_RETURN_ERROR  

  Var ADMIN_USER                    
  Var INSTALL_STATUS 
  Var INSTALL_BUILD
  Var INSTALL_VERSION
  Var JBOSS_HOME
  Var LAMS_UPLOAD_DIRECTORY
  Var MYSQL_DBNAME
  Var MYSQL_HOST
  Var MYSQL_USER
  Var SERVICE
  Var TOMCAT_PORT
  Var CHAT_PORT
  Var JAVA_HOME
  Var JAVA_VERSION
  Var MYSQL_HOME
  Var MYSQL_SERVICE
  Var MYSQL_SETUP
  Var MYSQL_PASSWORD
  Var TEMP_DIR
  Var UPDATE_TYPE
  Var UPDATE_LOG
  Var UPDATE_BINARIES
  Var BACKUP_SCRIPT_NAME
  Var TIMESTAMP
  Var TIMEDISPLAY 
  Var BACKUP_CONF 
;--------------------------------
;Installer Sections

;--------------------------------
;Installer Functions

Function .onInit
        !insertmacro MUI_INSTALLOPTIONS_EXTRACT "installation-settings.ini"
        !insertmacro MUI_INSTALLOPTIONS_EXTRACT "update-settings.ini"

    ;; is LAMS installed ?
    ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_status"

    ${If} $1 != "successful"
               MessageBox MB_OK|MB_ICONSTOP  'Sorry, it seems that LAMS is not installed successfuly on your computer. Download the LAMS Installer instead from http://lamsinternational.com/downloads'
               Abort -2       
        ${EndIf}        


    ;; here we check that we are updating from 1.0.1 Yoichi or 20050501 
    ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_version"
    ReadRegStr $2 HKLM SOFTWARE\LAMS\Server "install_build"

        ${If} $1 == "1.0.2"
    
            ${If} $2 == "20051019"
            ${OrIf} $2 == "20051121"
        ${OrIf} $2 == "20060223"
        ${OrIf} $2 == "20060403"
               ; these are supported builds 
        ${ElseIf} $2 == ${Build}

           ;; If the updater is the same version as the LAMS installed, we let the user know that 
           ;; there's nothing for the installer to do. 

           MessageBox MB_OK|MB_ICONSTOP 'Your LAMS installation is currently same version as this updater. For further updates, see http://lamsinternational.com/downloads'
           Abort -2
        
            ${Else}
           ;; Although the installation is of 1.0.2 version, the build is not recognized. 

           MessageBox MB_OK|MB_ICONSTOP 'Sorry. Although your LAMS version is $1, your build ($2) is not supported by this updater. Contact the LAMS Community for further details'
           Abort -2
        
            ${EndIf}

        ${EndIf}

        StrCpy $BACKUP_TYPE ${BACKUP_BASIC}
        StrCpy $MYSQL_SETUP 100
        StrCpy $MYSQL_PASSWORD ""
        StrCpy $JAVA_HOME ""
        StrCpy $MYSQL_HOME ""
        StrCpy $UPDATE_BINARIES ${NAY}
        StrCpy $BACKUP_SCRIPT_NAME "lams-database-backup.sql"

        call GetLocalTime
        Pop $0
        Pop $1
        Pop $2
        Pop $3
        Pop $4
        Pop $5
        Pop $6
        StrCpy $TIMESTAMP "$2$1$0$4$5$6"
        StrCpy $TIMEDISPLAY "$3 $0-$1-$2 $4:$5:$6"
 FunctionEnd

LangString TEXT_IO_TITLE ${LANG_ENGLISH} "LAMS Update"
LangString TEXT_IO_SUBTITLE ${LANG_ENGLISH} "Installing LAMS Update to ${VERSION} Build ${BUILD}"

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;  Selects the Installation Type

Function CheckPath
        Push $INSTDIR
        call FixPath
        Pop  $INSTDIR
        StrCpy $BACKUP_DIR "$INSTDIR\$TIMESTAMP\backup"
        CreateDirectory "$BACKUP_DIR"
        CreateDirectory "$INSTDIR\$TIMESTAMP\updates"
        StrCpy $TEMP_DIR "$TEMP\lams$TIMESTAMP"
FunctionEnd

Function CheckUpdateVersion
       
        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_build"
        ${If} $1 != ""
                ReadRegStr $2 HKLM SOFTWARE\LAMS\Server\Restore "$1_status"
                ReadRegStr $3 HKLM SOFTWARE\LAMS\Server "install_build"
                
                ${If} $2 == "started"
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Your previous attempt at updating to this version may have failed. $\nIt is recommended to revert to the backup before trying again."
                        Abort
                ${ElseIf} ${BUILD} == $1
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Your version of LAMS has already been updated to the current version (${VERSION} ${BUILD})."
                        Abort                                           
                ${EndIf}
        ${EndIf}
FunctionEnd

Function ConfirmExistingLAMS      

        call CheckUpdateVersion
        StrCpy $1 ""


        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_status"
;; DEBUG
        ${If} $1 == "successful"
                goto existing
        ${ElseIf} $1 == "completed"
                goto existing
        ${Else}
           ${If} $1 != ""
                 MessageBox MB_YESNO|MB_ICONEXCLAMATION "Cannot find details about your current LAMS installation. Continue anyway?" IDNO quitit
           ${Else}
                 MessageBox MB_YESNO|MB_ICONEXCLAMATION "Cannot find details about your current LAMS installation. Please continue (select 'Yes') if using a pre-installer version of LAMS." IDNO quitit
           ${EndIf}
        ${EndIf}
        goto end
   existing:
           ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_dir"
           StrCpy $LAMS_HOME $1

           StrCpy $2 "$LAMS_HOME\start-lams.exe"
           IfFileExists $2 present end

           present:

;           MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION "The LAMS Server may already be installed at $1.$\nContinuing without uninstalling or backing up could overwrite parts of the existing LAMS installation." IDCANCEL quitit
           ReadRegStr $1 HKLM "SYSTEM\ControlSet001\Services\lams" "DisplayName"
;
;           ${If} $1 == ""
;                 MessageBox MB_YESNO|MB_ICONEXCLAMATION "Windows cannot find the LAMS Service." IDYES quitit
;           ${EndIf}
           call GetRegistrySettings
           goto end
   quitit:
           Quit
   end:
           call CheckPath
FunctionEnd

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Update Settings Page
;

Function FindMySQLInstallation
        StrCpy $1 0

        ClearErrors
        ReadRegStr $1 HKLM "SOFTWARE\MYSQL AB\MySQL Server 4.0" "Location"
        IfErrors 0 setlocation
        
        ClearErrors
        ReadRegStr $1 HKLM "SOFTWARE\MYSQL AB\MySQL Server 4.1" "Location"
        IfErrors 0 setlocation

        ;; if errors then trying again else "setlocation"
        ClearErrors
        ReadRegStr $1 HKLM "SOFTWARE\MYSQL AB\MySQL Server 5.0" "Location"
    IfErrors 0 setlocation

        setlocation:
                Push $1
                call FixPath
                Pop $1
                StrCpy $MYSQL_HOME $1
              
FunctionEnd



Function FindJDKInstallation
        StrCpy $2 0
    ClearErrors
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion" 
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome" 
    ReadRegStr $3 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "MicroVersion"
    IfErrors 0 setlocation


        ;; if errors again then looking at the enviornment variable
;       ReadEnvStr $2 "JAVA_HOME"      
        ${If} $JAVA_HOME != ""
                StrCpy $2 "$2\"
        ${EndIf}
        
        ;; sets as blank (by default) if no environment variable either
        setlocation:
        ${If} $1 == "1.5"
                StrCpy $R1 ""
                StrCpy $JAVA_HOME ""
                ReadRegStr $R1 HKLM "SOFTWARE\JavaSoft\Java Development Kit\1.4" "JavaHome"
                    
                ${If} $R1 != ""
                        MessageBox MB_OK "The current version of Java (J2SDK) is set to 1.5! The LAMS Server is not compatible with this version. Using Version 1.4 located at: $R1 "
                        StrCpy $JAVA_HOME $R1
                ${Else}
                        MessageBox MB_OK "The current version of Java (J2SDK) is set to 1.5! The LAMS Server is not compatible with this version. $\n Please locate and set your correct Java Home Directory."
                ${EndIf}
                
        ${ElseIf} $1 == "1.4"
                StrCpy $JAVA_HOME $2
        ${Else}
                MessageBox MB_OK "LAMS requires J2SDK 1.4.x"
        ${EndIf}
        done:
FunctionEnd

Function GetRegistrySettings

        ${If} $LAMS_FOUND != ${NAY}   
        call FixRegistrySettings

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_dir"
        StrCpy $LAMS_HOME $1 

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "admin_user"
        StrCpy $ADMIN_USER $1      
              
        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_status"
        StrCpy $INSTALL_STATUS $1 

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_build"
        StrCpy $INSTALL_BUILD $1 

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_version"
        StrCpy $INSTALL_VERSION $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "jboss_home"
        StrCpy $JBOSS_HOME $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "lams_upload_directory"
        StrCpy $LAMS_UPLOAD_DIRECTORY $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "mysql_dbname"
        StrCpy $MYSQL_DBNAME $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "mysql_host"
        StrCpy $MYSQL_HOST $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "mysql_user"
        StrCpy $MYSQL_USER $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "service"
        StrCpy $SERVICE $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "tomcat_port"
        StrCpy $TOMCAT_PORT $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "chat_port"
        StrCpy $CHAT_PORT $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "mysql_service"
        StrCpy $MYSQL_SERVICE $1

        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "mysql_key"
        StrCpy $MYSQL_PASSWORD $1        
        ${EndIf}
        
FunctionEnd

;; This is a fix for a problem with the Basic Install Registry settings in v1.0.1
;;

Function FixRegistrySettings
        
    ; correct missing 'install_build' variable in launch cd installer
        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_build"
        ${If} $1 == ""
                WriteRegStr HKLM SOFTWARE\LAMS\Server "install_build" "20050501"
        ${EndIf}

    ; correct missing 'install_version' variable in launch cd installer
        ReadRegStr $1 HKLM SOFTWARE\LAMS\Server "install_version"
        ${If} $1 == "" 
                StrCpy $INSTALL_VERSION "1.0.1"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "install_version" "1.0.1"
        ${EndIf}

    ; correct 1.0.1(yoichi) basic install bug
        ReadRegStr $2 HKLM SOFTWARE\LAMS\Server "tomcat_port"
        ReadRegStr $3 HKLM SOFTWARE\LAMS\Server "mysql_dbname"

        ${If} $2 == $3
                StrCpy $MYSQL_PASSWORD "$2"
                StrCpy $TOMCAT_PORT "8080"                    
                StrCpy $CHAT_PORT "9800"
                StrCpy $MYSQL_DBNAME "lamsone"
                StrCpy $MYSQL_HOST "localhost"
                StrCpy $MYSQL_USER "lamsone"
                StrCpy $SERVICE "lams"

                WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_key" "$MYSQL_PASSWORD"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "install_type" "basic"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_host" "localhost"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_user" "lamsone"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "service" "lams"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "chat_port" "9800"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "tomcat_port" "8080"
                WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_dbname" "lamsone"
        ${Else}
                WriteRegStr HKLM SOFTWARE\LAMS\Server "install_type" "advanced"
        ${EndIf}

    ; correct 1.0.2(20051019) basic install bug
    ReadRegStr $2 HKLM SOFTWARE\LAMS\Server "mysql_key"
        ReadRegStr $3 HKLM SOFTWARE\LAMS\Server "mysql_user"

    ${If} $2 == $3
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_key" "$MYSQL_PASSWORD"
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_user" "lams"
    ${EndIf}

FunctionEnd
           
Function InstallationSettings
        call FindJDKInstallation
        call FindMySQLInstallation
                       
        !insertmacro MUI_HEADER_TEXT "Installations Settings" "Please confirm details about your existing LAMS installation. These should not be changed unless necessary."
        WriteINIStr  "$PLUGINSDIR\installation-settings.ini" "Field 3" "State" "$LAMS_HOME"
        WriteINIStr  "$PLUGINSDIR\installation-settings.ini" "Field 5" "State" "$LAMS_UPLOAD_DIRECTORY"
        WriteINIStr  "$PLUGINSDIR\installation-settings.ini" "Field 7" "State" "$JBOSS_HOME"
        WriteINIStr  "$PLUGINSDIR\installation-settings.ini" "Field 9" "State" "$JAVA_HOME"
        WriteINIStr  "$PLUGINSDIR\installation-settings.ini" "Field 11" "State" "$MYSQL_HOME"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "installation-settings.ini"
FunctionEnd

Function LeaveInstallationSettings
        SetOutPath "$TEMP_DIR"
        File /r "..\..\LAMS-Update\utilities"
        ReadINIStr $LAMS_HOME "$PLUGINSDIR\installation-settings.ini" "Field 3" "State"
        WriteINIStr $LAMS_UPLOAD_DIRECTORY "$PLUGINSDIR\installation-settings.ini" "Field 5" "State"
        ReadINIStr $JBOSS_HOME "$PLUGINSDIR\installation-settings.ini" "Field 7" "State"
        ReadINIStr $JAVA_HOME "$PLUGINSDIR\installation-settings.ini" "Field 9" "State"
        ReadINIStr $MYSQL_HOME "$PLUGINSDIR\installation-settings.ini" "Field 11" "State"
FunctionEnd

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; Update Settings Page
;

Function UpdateSettings

        SetOutPath "$TEMP_DIR"

        !insertmacro MUI_HEADER_TEXT "Update Settings" "Select your update settings."
        WriteINIStr  "$PLUGINSDIR\update-settings.ini" "Field 4" "State" "$MYSQL_DBNAME"
        WriteINIStr  "$PLUGINSDIR\update-settings.ini" "Field 7" "State" "$MYSQL_PASSWORD"
    !insertmacro MUI_INSTALLOPTIONS_DISPLAY "update-settings.ini"
        ReadINIStr $MYSQL_DBNAME "$PLUGINSDIR\update-settings.ini" "Field 4" "State"
        ReadINIStr $MYSQL_PASSWORD "$PLUGINSDIR\update-settings.ini" "Field 7" "State"
        ReadINIStr $0 "$PLUGINSDIR\update-settings.ini" "Field 8" "State"
        ReadINIStr $1 "$PLUGINSDIR\update-settings.ini" "Field 9" "State"

        ${If} $0 == "1"
                ${If} $0 == "1"
                        StrCpy $BACKUP_TYPE ${BACKUP_FULL}
                ${Else} 
                        StrCpy $BACKUP_TYPE ${BACKUP_LAMS_DIR}
                ${EndIf}
        ${Else}
                ${If} $1 == "1"
                        StrCpy $BACKUP_TYPE ${BACKUP_DB_DIR} 
                ${Else} 
                        StrCpy $BACKUP_TYPE ${BACKUP_BASIC}
                ${EndIf}
        ${EndIf}                        

FunctionEnd

Function LeaveUpdateSettings
        SetOutPath "$TEMP_DIR"
        ReadINIStr $MYSQL_DBNAME "$PLUGINSDIR\update-settings.ini" "Field 4" "State"
        ReadINIStr $MYSQL_PASSWORD "$PLUGINSDIR\update-settings.ini" "Field 7" "State"
        call RunTests
FunctionEnd

Function "StopLAMS"
        nsExec::ExecToStack '$TEMP_DIR\utilities\sc.exe stop lams'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 
;        MessageBox MB_OK "zipping $EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
FunctionEnd

Function "StartLAMS"
        nsExec::ExecToStack '$TEMP_DIR\utilities\sc.exe start lams'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 
;        MessageBox MB_OK "zipping $EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
FunctionEnd

Function "StopMySQL"
        nsExec::ExecToStack '$TEMP_DIR\utilities\sc.exe stop mysql'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 
;        MessageBox MB_OK "zipping $EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
FunctionEnd

Function "CheckStopMySQL"

        nsExec::ExecToStack '"$JAVA_HOME\bin\java.exe" LocalPortScanner 3306'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 2
                nsExec::ExecToStack '$TEMP_DIR\utilities\sc.exe start mysql'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR
                sleep 1000                                       
        ${Else}
                goto end
        ${EndIf}

        ${If} $EXEC_RETURN_STATUS = 2
                MessageBox MB_ICONEXCLAMATION|MB_OKCANCEL "Please check if MySQL is shutdown. To manually shutdown MySQL and continue click OK, to abort update select CANCEL" IDOK continue

        ${EndIf}
        quit:
                Abort        
        continue:

                nsExec::ExecToStack '"$JAVA_HOME\bin\java.exe" LocalPortScanner 3306'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR

                MessageBox MB_ICONEXCLAMATION|MB_OKCANCEL "MySQL may still be running. To manually shutdown MySQL and continue click OK, to abort update select CANCEL" IDCANCEL quit
        end:
        
FunctionEnd


Function "StartMySQL"
        nsExec::ExecToStack '$TEMP_DIR\utilities\sc.exe start mysql'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 
FunctionEnd

Function "BackupLAMS"
       
        SetOutPath "$LAMS_UPLOAD_DIRECTORY"
        nsExec::ExecToStack '$TEMP_DIR\zip\7za.exe a -r -tzip "$BACKUP_DIR\lamsdata-archive.zip" "*.*"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 

        ${If} $BACKUP_TYPE == "${BACKUP_FULL}"
                goto dir
        ${ElseIf} $BACKUP_TYPE == "${BACKUP_LAMS_DIR}"
                goto dir
        ${Else}
                goto basic
        ${EndIf}

        dir:
        SetOutPath "$LAMS_HOME"        
        nsExec::ExecToStack '"$TEMP_DIR\zip\7za.exe" a -r -tzip "$BACKUP_DIR\lams-dir-backup.zip" "*.*"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        SetOutPath "$BACKUP_DIR"
        basic:

        CopyFiles "$JBOSS_HOME\server\default\deploy\lams-1.0.jar" "$BACKUP_DIR"
        CopyFiles "$JBOSS_HOME\server\default\deploy\lams.war" "$BACKUP_DIR"
        CopyFiles "$JBOSS_HOME\server\default\deploy\lams-hibernate.sar" "$BACKUP_DIR"
    ${If} $INSTALL_BUILD == "20051019"
        ${If} $INSTALL_VERSION == "1.0.2"
            CopyFiles "$JBOSS_HOME\server\default\conf\jboss-service.xml" "$BACKUP_DIR"
        ${EndIf}
    ${EndIf}

        CreateDirectory "$BACKUP_DIR\lib"
        nsExec::ExecToStack '"$TEMP_DIR\utilities\copy.bat" "$JBOSS_HOME\server\default\lib" "$BACKUP_DIR\lib"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR    
        
        SetOutPath "$BACKUP_DIR"        
;        MessageBox MB_OK "$EXEC_RETURN_ERROR"
FunctionEnd

Function "BackupLAMSDB"
        nsExec::ExecToStack '"$MYSQL_HOME\bin\mysqldump.exe" -c -a --add-drop-table -r "$BACKUP_DIR\$BACKUP_SCRIPT_NAME" -B $MYSQL_DBNAME -u root -p$MYSQL_PASSWORD'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR         
;        MessageBox MB_OK "zipping $EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"

FunctionEnd

Function "BackupMySQLDirectory"
        ${If} $BACKUP_TYPE == "${BACKUP_FULL}"
                goto dir
        ${ElseIf} $BACKUP_TYPE == "${BACKUP_DB_DIR}"
                goto dir
        ${Else}
                goto done
        ${EndIf}

        dir:
        SetOutPath "$MYSQL_HOME"
        nsExec::ExecToStack '$TEMP_DIR\zip\7za.exe a -r -tzip "$BACKUP_DIR\mysql-archive.zip" "*.*"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR 

        done:
        SetOutPath "$BACKUP_DIR"
FunctionEnd
Function "CleanUp"
        Delete "$TEMP_DIR\*.*"
        RMDir /r "$TEMP_DIR"
FunctionEnd

Function "OpenLAMSConsole"
        SetOutPath "$TEMP_DIR"
        SetDetailsPrint listonly
        ;;LogSet on
        ;MessageBox MB_OK "Opening the LAMS Login Console in your browser window!"
        ;ExecShell "open" '"$TEMP_DIR"'
        ExecShell "open" '"$LAMS_HOME\newlams.html"'
        BringToFront
FunctionEnd

Function CheckINSTDIR
        SetOutPath "$TEMP_DIR"
        StrCpy $1 "Error: The selected installaton directory has a space/s.$\nPlease remove all spaces."
        Push $1
        Push $TEMP_DIR
        call CheckForSpaces
FunctionEnd        

Function FixPath
        SetOutPath "$TEMP_DIR"
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

Function RunTests
SetDetailsPrint textonly
DetailPrint "Status: Testing your 3rd party packages."
SetDetailsPrint listonly
;;LogSet on
  ;WriteRegDWORD HKCU "Software\Microsoft\Windows\CurrentVersion\Policies\Explorer" NORUN 1  

        SetOutPath "$TEMP_DIR\utilities"

        nsExec::ExecToStack '"$JAVA_HOME\bin\java.exe" -help'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
 
        ${If} $EXEC_RETURN_STATUS = 1
                MessageBox MB_OK|MB_ICONSTOP "JDK (java) appears not to be working as expected. Check the Java Home Directory!"
                call FailedTest
        ${EndIf}

        nsExec::ExecToStack '"$JAVA_HOME\bin\javac.exe" -help'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 1
                MessageBox MB_OK|MB_ICONSTOP "JDK (javac) appears not to be working as expected!"
                call FailedTest
        ${EndIf}

        nsExec::ExecToStack '"$JAVA_HOME\bin\java.exe" LocalPortScanner 3306'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS = 1
                ${If} InstallType = ${ADVANCED}
                        MessageBox MB_OK|MB_ICONEXCLAMATION "Port scanning test encountered a problem!"
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
          nsExec::ExecToStack '"$MYSQL_HOME\bin\mysqladmin.exe" -u root status -p$MYSQL_PASSWORD'
          Pop $EXEC_RETURN_STATUS
          Pop $EXEC_RETURN_ERROR
          ${If} $EXEC_RETURN_STATUS != 0
                  MessageBox MB_OK|MB_ICONSTOP "Please set your current and correct password if MySQL has already been configured! $\n(This error may also occur if MySQL is not running)"
                  Abort
          ${EndIf}

  BringToFront
FunctionEnd

Function FailedTest
SetOutPath "$INSTDIR"
SetDetailsPrint listonly
;;LogSet on
  MessageBox MB_YESNO|MB_ICONSTOP "One or more tests failed! Click 'Yes' to continue if the reported problem has been resolved or click 'No' to abort and re-configure settings." IDNO end
  goto continue
  end:
  Abort
  continue:
FunctionEnd

Function "UpdateRegistry"

        ReadINIStr $R1 "$PLUGINSDIR\update-settings.ini" "Field 5" "State"
        ${If} $R1 != ""
                WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_key" "$MYSQL_PASSWORD"
        ${EndIf}        

        StrCpy $R1 ""
        ReadRegStr $R1 HKLM SOFTWARE\LAMS\Server "last_restore"        
        ${If} $R1 != ""
                WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$R1_next" "$TIMESTAMP"
                WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_previous" "$R1"               
        ${EndIf}   
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_dir" "$BACKUP_DIR"
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_time" "$TIMEDISPLAY"
;;        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_conf" "$BACKUP_DIR\update.conf"        
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_type" "UPDATE"
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_version" "$INSTALL_VERSION"
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_build" "$INSTALL_BUILD"
        
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_backup_script" "$BACKUP_SCRIPT_NAME"
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_restore_version" "${RESTORE_VERSION}"
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_restore_tag" "l"
                
        WriteRegStr HKLM SOFTWARE\LAMS\Server "last_restore" "$TIMESTAMP"
        WriteRegStr HKLM SOFTWARE\LAMS\Server "install_version" "${VERSION}"
        WriteRegStr HKLM SOFTWARE\LAMS\Server "install_build" "${BUILD}"

                        
;        ReadRegStr $UPDATE_LOG HKLM SOFTWARE\LAMS\Server "update_log" 
;        ${If} $UPDATE_LOG == ""
;                WriteRegStr HKLM SOFTWARE\LAMS\Server "update_log" "$LAMS_HOME\update.log"
;        ${Else}
;                ReadRegStr $UPDATE_LOG HKLM SOFTWARE\LAMS\Server "update_log"
;        ${EndIf}
        WriteRegStr HKLM SOFTWARE\LAMS\Server "update_dir" "$INSTDIR"
        WriteRegStr HKLM SOFTWARE\LAMS\Server "java_home" "$JAVA_HOME"
        WriteRegStr HKLM SOFTWARE\LAMS\Server "mysql_home" "$MYSQL_HOME"

FunctionEnd

Section ""
        SetDetailsPrint listonly
        ;;LogSet on
        AddSize 30000
        !insertmacro MUI_HEADER_TEXT "Updating LAMS" "Please wait while the LAMS Server is being updated."

        ;; Keep a copy in the update backup dir
        SetOutPath "$INSTDIR\$TIMESTAMP\updates"
        File /r "..\..\LAMS-Update\binaries"
        File /r "..\..\LAMS-Update\sql"
                       
        SetOutPath "$TEMP_DIR"
        File /r "..\..\LAMS-Update\zip"
        File /r "..\..\LAMS-Update\sql"
        File /r "..\..\LAMS-Update\binaries"
        File /r "..\..\LAMS-Update\libs"        
    File /r "..\..\LAMS-Update\conf"

        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_status" "started"
        call StopLAMS
        sleep 5000
        call BackupLAMSDB        
        sleep 1000
        call StopMySQL
        sleep 2500
        call CheckStopMySQL
        call BackupLAMS
        sleep 1000
        call BackupMySQLDirectory
        sleep 1000
        call StartMySQL
        sleep 2500
        call UpdateBinaries
        sleep 1000
        call UpdateDatabase
        sleep 1000
        call UpdateInstallation 
        sleep 1000
        call StartLAMS
        sleep 10000
        call CleanUp
        WriteRegStr HKLM SOFTWARE\LAMS\Server\Restore "$TIMESTAMP_status" "successful"
        call UpdateRegistry
        sleep 2000
        call SetJavaPath
SectionEnd


;;==========================================================================================================================================
;;==========================================================================================================================================
;;==========================================================================================================================================
;;==========================================================================================================================================
;;==========================================================================================================================================
;;  Updates for each version of LAMS
;;
;;

Function UpdateInstallation
        SetOutPath "$LAMS_HOME"
        File /r "..\..\LAMS-Update\binaries\restore-lams.exe"

        CreateShortCut "$SMPROGRAMS\LAMS\Restore LAMS From Backup.lnk" "$LAMS_HOME\restore-lams.exe" "..\..\LAMS-Update\images\lams2.ico"; use defaults for parameters, icon, etc.
        SetOutPath "$TEMP_DIR"
FunctionEnd

;;--------------------------------------------------
;; Update to Current Version

Function "UpdateLatestBinaries"
        StrCpy $1 "Update has failed. Reverting to the backup."
        
        ClearErrors
        CopyFiles "$TEMP_DIR\binaries\lams.war" "$JBOSS_HOME\server\default\deploy"        
        CopyFiles "$TEMP_DIR\binaries\lams-hibernate.sar" "$JBOSS_HOME\server\default\deploy"
        CopyFiles "$TEMP_DIR\binaries\lams-1.0.jar" "$JBOSS_HOME\server\default\deploy"

        IfErrors 0 next
        goto failed

        next:
        nsExec::ExecToStack '"$TEMP_DIR\utilities\move.bat" "$JBOSS_HOME\server\default\lib" "$JBOSS_HOME\server\default\lib.$TIMESTAMP"'
            Pop $EXEC_RETURN_STATUS
            Pop $EXEC_RETURN_ERROR
        
        StrCpy $2 "$JBOSS_HOME\server\default\lib\wddx.jar"
            IfFileExists $2 exists1 notfound1 


        exists1:
                goto failed

        notfound1:        
                nsExec::ExecToStack '"$TEMP_DIR\utilities\move.bat" "$TEMP_DIR\libs" "$JBOSS_HOME\server\default\lib"'
                Pop $EXEC_RETURN_STATUS
                Pop $EXEC_RETURN_ERROR        

        ; as of build 060223, non-updated libs are not included - manually copy them back in here.
        ClearErrors
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\activation.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\ant.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\autonumber-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\bcel.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\bindingservice-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\bsh-core-1.2b7.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\bsh-deployer.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\castor.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\counter-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\hsqldb.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\hsqldb-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss-common-jdbc-wrapper.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jbossha.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss-j2ee.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss-jaas.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss-jsr77.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jboss-management.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jbossmq.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jbossmx.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jbosssx.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jcert.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jmx-adaptor-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jnet.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jnpserver.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jpl-pattern.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jpl-util.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jsse.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\jts.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\log4j.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\mail.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\mail-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\process-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\properties-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\scheduler-plugin.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\scheduler-plugin-example.jar" "$JBOSS_HOME\server\default\lib"
        ;CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\servletapi-2.3.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\tomcat41-service.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\tyrex.jar" "$JBOSS_HOME\server\default\lib"
        CopyFiles "$JBOSS_HOME\server\default\lib.$TIMESTAMP\xalan.jar" "$JBOSS_HOME\server\default\lib"

        IfErrors 0 done
            goto failed

;                sleep 1000
;                StrCpy $2 "$JBOSS_HOME\server\default\lib\wddx.jar"
;                IfFileExists $2 exists2 notfound2 

;        exists2:
;                sleep 2500
;                goto done
                
;        notfound2:        
;                goto failed
        
        failed:
                MessageBox MB_OK "$1"
                call RevertBinaries
                sleep 2000
                MessageBox MB_OK "Your LAMS installation has been successfully restored. $\nPlease contact your LAMS support representative for further assistance."
                Quit                
        done:                  

FunctionEnd

Function "RevertBinaries"
        StrCpy $1 "Reverting to backups has failed. Please contact your LAMS support representative. (Error: B)"

        ClearErrors
        CopyFiles "$BACKUP_DIR\lams.war" "$JBOSS_HOME\server\default\deploy"
        CopyFiles "$BACKUP_DIR\lams-hibernate.sar" "$JBOSS_HOME\server\default\deploy"             
        CopyFiles "$BACKUP_DIR\lams-1.0.jar" "$JBOSS_HOME\server\default\deploy"
    CopyFiles "$BACKUP_DIR\jboss-service.xml" "$JBOSS_HOME\server\default\conf"

        nsExec::ExecToStack '"$TEMP_DIR\utilities\move.bat" "$JBOSS_HOME\server\default\lib" "$TEMP_DIR\lib.$TIMESTAMP"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        
        nsExec::ExecToStack '"$TEMP_DIR\utilities\move.bat" "$JBOSS_HOME\server\default\lib.$TIMESTAMP" "$JBOSS_HOME\server\default\lib"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        IfErrors 0 done
        goto failed

        failed:
                MessageBox MB_OK "$1"                
                Quit                
        done:                  

FunctionEnd

Function "RevertDatabase"
        StrCpy $1 "Reverting to backups has failed. Please contact your LAMS support representative. (Error: D)"

        DetailPrint '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $BACKUP_DIR\$BACKUP_SCRIPT_NAME"'
        nsExec::ExecToStack '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $BACKUP_DIR\$BACKUP_SCRIPT_NAME"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR
        sleep 5000

        ${If} $EXEC_RETURN_STATUS != 0
                DetailPrint "Error details: $EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
                MessageBox MB_OK "$1"
                sleep 2500
                Quit                       
        ${EndIf}

        done:                  

FunctionEnd


Function "UpdateBinaries20050501"
        ${If} $UPDATE_BINARIES != ${AYE}
                call UpdateLatestBinaries
                StrCpy $UPDATE_BINARIES ${AYE}
        ${EndIf}
FunctionEnd

Function "UpdateBinaries20050901"
        ${If} $UPDATE_BINARIES != ${AYE}
                call UpdateLatestBinaries
                StrCpy $UPDATE_BINARIES ${AYE}
        ${EndIf}         
FunctionEnd

Function "UpdateBinaries"
        ${If} $INSTALL_VERSION == "1.0.1"
                ${If} $INSTALL_BUILD == "20050501"
                        goto sep01
                ${Else}
                        goto may01
                ${EndIf}
    ${ElseIf} $INSTALL_BUILD == "20051019"
        CopyFiles "$TEMP_DIR\conf\jboss-service.xml" "$JBOSS_HOME\server\default\conf"
        ${Else} 
                ${If} $SERVICE == "lams"
                        goto may01                        
                ${Else}
                        MessageBox MB_OK "Error! Could not find binaries update for this version $INSTALL_VERSION!! "
                        call UpdateFailed
                ${EndIf}                
                MessageBox MB_OK "Update Error! Installation in an undefined state!"
                Abort
        
        ${EndIf}

        may01:        
                call UpdateBinaries20050501

        sep01:        
                call UpdateBinaries20050901
        done:
        
FunctionEnd

Function "UpdateDatabase"

        ${If} $INSTALL_VERSION == "1.0.1"
                ${If} $INSTALL_BUILD == "20050501"
                        goto may01
                ${Else}
                        goto sep01
                ${EndIf}                
        ${Else}
                ${If} $SERVICE == "lams"
                        goto feb23
                ${Else}
                        MessageBox MB_OK "Error! Could not find binaries update for this version $INSTALL_VERSION!! "
                        Abort
                ${EndIf}                        
        ${EndIf}
                MessageBox MB_OK "Update Error! Installation in an undefined state!"
                Abort                
        may01:  
                call UpdateDatabase20050501
        sep01:        
                call UpdateDatabase20050901
    feb23:
        call UpdateDatabase20060424
        done:
                DetailPrint "Passed UpdateDatabase"
FunctionEnd

Function SetJavaPath
        StrCpy $R0 "JAVA_HOME"
        StrCpy $R1 $JAVA_HOME
        Push $R0
        Push $R1
        Call WriteEnvStr

        StrCpy $R0 "LAMS_JAVA_HOME"
        StrCpy $R1 $JAVA_HOME
        Push $R0
        Push $R1
        Call WriteEnvStr

        StrCpy $R0 "MYSQL_HOME"
        StrCpy $R1 $MYSQL_HOME
        Push $R0
        Push $R1
        Call WriteEnvStr
FunctionEnd

Function RevertAll
        call RevertBinaries
        call RevertDatabase
        sleep 2000
        MessageBox MB_OK "Your LAMS installation has been successfully restored. $\nPlease contact your LAMS support representative for further assistance."
FunctionEnd

Function "UpdateDatabase20050501"
        ;handle string
        DetailPrint "Started UpdateDatabase20050501"         
        DetailPrint '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20050501.txt"'
        nsExec::ExecToStack '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20050501.txt"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        ${If} $EXEC_RETURN_STATUS != 0
                DetailPrint "$EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
                MessageBox MB_OK "Updating the LAMS database has failed! Reverting to backups."
                call RevertAll
                Quit                        
        ${EndIf}
         
        done:
        DetailPrint "Passed UpdateDatabase20050501"

FunctionEnd

Function "UpdateDatabase20050901"
        ;handle string

        DetailPrint "Started UpdateDatabase20050901"
        DetailPrint '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20050901.txt"'
        nsExec::ExecToStack '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20050901.txt"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        DetailPrint "$EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
        ${If} $EXEC_RETURN_STATUS != 0
                MessageBox MB_OK "Update Failed! Reverting to backup is not necessary."
                MessageBox MB_OK "STATUS - $EXEC_RETURN_STATUS ERROR - $EXEC_RETURN_ERROR"
                call RevertAll
                Quit
        ${EndIf}
         
        done:
             DetailPrint "Passed UpdateDatabase20050901"
FunctionEnd

Function "UpdateDatabase20060424"
        ;handle string

        DetailPrint "Started UpdateDatabase20060424"
        DetailPrint '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20060424.txt"'
        nsExec::ExecToStack '"$MYSQL_HOME\bin\mysql.exe" -B -u root -p$MYSQL_PASSWORD $MYSQL_DBNAME -e "\. $TEMP_DIR\sql\update.20060424.txt"'
        Pop $EXEC_RETURN_STATUS
        Pop $EXEC_RETURN_ERROR

        DetailPrint "$EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
        ${If} $EXEC_RETURN_STATUS != 0
                MessageBox MB_OK "Update Failed! Reverting to backup is not necessary."
                MessageBox MB_OK "STATUS - $EXEC_RETURN_STATUS ERROR - $EXEC_RETURN_ERROR"
                call RevertAll
                Quit
        ${EndIf}
         
        done:
             DetailPrint "Passed UpdateDatabase20060424"
FunctionEnd

Function UpdateFailed
        DetailPrint "$EXEC_RETURN_STATUS $EXEC_RETURN_ERROR"
        MessageBox MB_OK "Update Failed! Please restore your LAMS Server to the previous backup!"
        Abort                        
FunctionEnd

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

    IntCmp $5 9 0 0 +2
      StrCpy $5 '0$5'

    IntCmp $6 9 0 0 +2
      StrCpy $6 '0$6'
      
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

