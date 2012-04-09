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
!include "x64.nsh"

!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
  
Name "Start LAMS"
RequestExecutionLevel user
OutFile "..\..\build\lams-stop.exe"
!define MUI_ICON "..\graphics\favicon.ico"
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

# variables
Var JBOSSDIR		; JBOSS_HOME
Var LAMSDIR			; LAMS_HOME

Function .onInit
    SetSilent silent
FunctionEnd

Section

	ReadRegStr $2 HKCU "${REG_HEAD}" "jboss_dir"
	ReadRegStr $1 HKCU "${REG_HEAD}" "dir_inst"
	StrCpy $JBOSSDIR $2
	StrCpy $LAMSDIR $1

	# Is LAMS installed as service?
    ReadRegStr $0 HKCU "${REG_HEAD}" "is_service"
	# if so, stop the service
	${If} $0 == "1"	


	${Else} 
		# Stop LAMS Manually
		${If} ${RunningX64}
			nsExec::ExecToStack '"$JBOSSDIR\bin\service64.bat" stop'
		${Else} 
			nsExec::ExecToStack '"$JBOSSDIR\bin\service.bat" stop'
		${EndIf}
		MessageBox MB_OK "LAMS Server has been stopped now. Thank you for using LAMS!"
	${EndIf}
	Abort
SectionEnd