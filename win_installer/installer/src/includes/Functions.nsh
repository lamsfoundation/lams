!include "LogicLib.nsh"

# http://nsis.sourceforge.net/StrStr
#
!define StrStr2 "!insertmacro StrStr2"
 
!macro StrStr2 ResultVar String SubString
  Push `${String}`
  Push `${SubString}`
  Call StrStr2
  Pop `${ResultVar}`
!macroend
 
Function StrStr2
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


;This function will search in a file for the specified string, and return some values.
/*
USAGE
Push C:\temp1.txt
Push hello
 Call FileSearch
Pop $0 #Number of times found throughout
Pop $1 #Found at all? yes/no
Pop $2 #Number of lines found in
 
StrCmp $1 yes 0 +2
MessageBox MB_OK "$\"hello$\" was found in the file $0 times on $2 lines."
*/
Function FileSearch
Exch $0 ;search for
Exch
Exch $1 ;input file
Push $2
Push $3
Push $4
Push $5
Push $6
Push $7
Push $8
Push $9
Push $R0
  FileOpen $2 $1 r
  StrLen $4 $0
  StrCpy $5 0
  StrCpy $7 no
  StrCpy $8 0
  StrCpy $9 0
  ClearErrors
loop_main:
  FileRead $2 $3
  IfErrors done
 IntOp $R0 $R0 + $9
  StrCpy $9 0
  StrCpy $5 0
filter_top:
 IntOp $5 $5 - 1
  StrCpy $6 $3 $4 $5
  StrCmp $6 "" loop_main
  StrCmp $6 $0 0 filter_top
  StrCpy $3 $3 $5
  StrCpy $5 0
 StrCpy $7 yes
 StrCpy $9 1
 IntOp $8 $8 + 1
Goto filter_top
done:
  FileClose $2
  StrCpy $0 $8
  StrCpy $1 $7
  StrCpy $2 $R0
Pop $R0
Pop $9
Pop $8
Pop $7
Pop $6
Pop $5
Pop $4
Pop $3
Exch $2 ;output number of lines
Exch
Exch $1 ;output yes/no
Exch 2
Exch $0 ;output count found
FunctionEnd

;----------------------------------------------------------------------------
; Superseded by     : GetTime function.
;----------------------------------------------------------------------------
; Title             : Get Local Time
; Short Name        : GetLocalTime
; Last Changed      : 22/Feb/2005
; Code Type         : Function
; Code Sub-Type     : One-way Output
;----------------------------------------------------------------------------
; Required          : System plugin.
; Description       : Gets the current local time of the user's computer
;----------------------------------------------------------------------------
; Function Call     : Call GetLocalTime
;
;                     Pop "$Variable1"
;                       Day.
;
;                     Pop "$Variable2"
;                       Month.
;
;                     Pop "$Variable3"
;                       Year.
;
;                     Pop "$Variable4"
;                       Day of the week name.
;
;                     Pop "$Variable5"
;                       Hour.
;
;                     Pop "$Variable6"
;                       Minute.
;
;                     Pop "$Variable7"
;                       Second.
;----------------------------------------------------------------------------
; Author            : Diego Pedroso
; Author Reg. Name  : deguix
;----------------------------------------------------------------------------
 
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

