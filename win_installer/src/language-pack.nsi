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

# constants
!define VERSION "2.0"
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
Var BACKUP_DIR
Var LAMS_DIR
;--------------------------------


Section "LAMS Language Pack ${VERSION}" LanguagePack
    # write this language pack version to registry
    ##########################UNCOMMENT LATER
    #WriteRegStr HKLM "${REG_HEAD}" "language_pack" ${VERSION}
    #Detailprint 'Writing Language pack version ${VERSION} to registry: "${REG_HEAD}"'
    
    setoutpath $EXEDIR
    File /r "..\zip"

    ;backup existing language files
    call zipLanguages



    #lams_blah\conf\language\*.properties
    # lams_central\flashxml\*

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
    
    # Abort install if already installed or if a newer version is installed
    ReadRegStr $0 HKLM "${REG_HEAD}" "language_pack"
    ${VersionCompare} "${VERSION}" "$0" $1
    ${If} $1 == "0"
        MessageBox MB_OK|MB_ICONSTOP "You already have LAMS Language Pack ${VERSION} installed"
        Abort
    ${EndIf}    
    ${if} $1 == "2"
        MessageBox MB_OK|MB_ICONSTOP "Your current language pack is a newer version than this version"
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
    Strcpy $4 '$EXEDIR\zip\7za.exe a -r -tzip "$BACKUP_DIR\lamsDictionaryBak.zip" "*"'
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
