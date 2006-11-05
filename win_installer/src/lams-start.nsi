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
!include "Functions.nsh"

!define REG_HEAD "Software\LAMS Foundation\LAMSv2"
  
Name "Start LAMS"
OutFile "..\build\lams-start.exe"
!define MUI_ICON "..\graphics\favicon.ico"
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_LANGUAGE "English"

Function .onInit
    SetSilent silent
FunctionEnd

Section
    ReadRegStr $9 HKLM "${REG_HEAD}" "db_user"
    ReadRegStr $8 HKLM "${REG_HEAD}" "db_pass"
    ReadRegStr $7 HKLM "${REG_HEAD}" "dir_inst"
    ReadRegStr $6 HKLM "${REG_HEAD}" "dir_mysql"

    # check mysql password, mysql server status
    StrLen $0 $8
    ${If} $0 == 0
        nsExec::ExecToStack '$6\bin\mysqladmin ping -u $9'
    ${Else}
        nsExec::ExecToStack '$6\bin\mysqladmin ping -u $9 -p$8'
    ${EndIf}
    Pop $0
    Pop $1
    # check mysql password is correct
    ${StrStr} $2 $1 "Access denied"
    ${If} $2 != ""
        ; mysql password somehow changed - prompt user to update registry entry
    ${EndIf}
    # check mysql is running
    ${StrStr} $2 $1 "mysqld is alive"
    ${If} $2 == ""
        MessageBox MB_OK|MB_ICONEXCLAMATION "MySQL does not appear to be running - please make sure it is running before starting LAMS."
        Abort
    ${EndIf}

    nsExec::ExecToStack 'sc start LAMSv2'
    Pop $0
    Pop $1
    ${StrStr} $2 $1 "START_PENDING"
    ${If} $2 == ""
        MessageBox MB_OK|MB_ICONSTOP "Could not start LAMSv2 service: $\r$\n$1"
    ${Else}
        MessageBox MB_OK "Started LAMSv2 service.  Please wait a minute or two while LAMS starts up."
        ExecShell "open" '"$7\index.html"'
    ${EndIf}
SectionEnd

