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

!include "MUI.nsh"
!include "LogicLib.nsh"
!include "includes\Functions.nsh"
!include "x64.nsh"

!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
  
Name "Start LAMS"
RequestExecutionLevel user
OutFile "..\..\build\lams-start.exe"
!define MUI_ICON "..\graphics\favicon.ico"
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

# variables
Var JBOSSDIR		; JBOSS_HOME
Var LAMSDIR			; LAMS_HOME
Var LAMSPORT		; LAMS_PORT
Var JREDIR			; JREDIR

Function .onInit
    SetSilent silent
FunctionEnd

Section

	ReadRegStr $4 HKCU "${REG_HEAD}" "jre_dir"
	ReadRegStr $3 HKCU "${REG_HEAD}" "lams_port"
	ReadRegStr $2 HKCU "${REG_HEAD}" "jboss_dir"
	ReadRegStr $1 HKCU "${REG_HEAD}" "dir_inst"
	StrCpy $JREDIR $4
	StrCpy $LAMSPORT $3
	StrCpy $JBOSSDIR $2
	StrCpy $LAMSDIR $1
	
    # check if LAMS is running
    SetOutPath $TEMP
    File "..\..\build\LocalPortScanner.class"
    nsExec::ExecToStack '"$JREDIR\bin\java" LocalPortScanner $LAMSPORT'
    Pop $0
    Pop $1
    ${If} $0 == 2
        MessageBox MB_YESNO|MB_ICONQUESTION "LAMS appears to be running. Do you want to open the browser and access it now?" \
            IDYES openbrowser  \
            IDNO quit
    ${EndIf}	

	# Is LAMS installed as service?
    ReadRegStr $0 HKCU "${REG_HEAD}" "is_service"
	# if so, start as service
	${If} $0 == "1"
	    nsExec::ExecToStack 'sc start LAMS Server'
		Pop $0
		Pop $1
		${StrStr} $2 $1 "START_PENDING"
		${If} $2 == ""
			MessageBox MB_OK|MB_ICONSTOP "Could not start LAMSv2 service: $\r$\n$1"
		${Else}
			MessageBox MB_OK "Started LAMSv2 service.  Please wait a minute or two while it starts up."
			ExecShell "open" '$LAMSDIR\slides\index.html'
		${EndIf}
		goto quit
	${Else} 
		# We start LAMS Manually
		MessageBox MB_OK "The LAMS Server is being started.  Please wait a minute or two while it starts up."
		ExecShell "open" '$LAMSDIR\slides\index.html'	
		${If} ${RunningX64}
			nsExec::ExecToStack '"$JBOSSDIR\bin\service64.bat" start'
		${Else} 
			nsExec::ExecToStack '"$JBOSSDIR\bin\service.bat" start'	
			${EndIf}
	${EndIf}
	
	Abort
	openbrowser:
		ExecShell "open" '$LAMSDIR\slides\index.html'
		Quit
	quit:
 SectionEnd

