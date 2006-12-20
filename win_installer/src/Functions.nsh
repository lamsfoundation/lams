!include "LogicLib.nsh"

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


# http://nsis.sourceforge.net/StrStr
#
!define StrStr "!insertmacro StrStr"
 
!macro StrStr ResultVar String SubString
  Push `${String}`
  Push `${SubString}`
  Call StrStr
  Pop `${ResultVar}`
!macroend
 
Function StrStr
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

/**
* Source - Afrow UK http://nsis.sourceforge.net/VersionCompare
*
* Compare version numbers.
*
* Syntax:
* ${VersionCompare} "[Version1]" "[Version2]" $var
*                     ;    $var=0  Versions are equal
*                     ;    $var=1  Version1 is newer
*                     ;    $var=2  Version2 is newer 
**/
Function VersionCompare
    !define VersionCompare `!insertmacro VersionCompareCall`
 
    !macro VersionCompareCall _VER1 _VER2 _RESULT
        Push `${_VER1}`
        Push `${_VER2}`
        Call VersionCompare
        Pop ${_RESULT}
    !macroend
 
    Exch $1
    Exch
    Exch $0
    Exch
    Push $2
    Push $3
    Push $4
    Push $5
    Push $6
    Push $7
 
    begin:
    StrCpy $2 -1
    IntOp $2 $2 + 1
    StrCpy $3 $0 1 $2
    StrCmp $3 '' +2
    StrCmp $3 '.' 0 -3
    StrCpy $4 $0 $2
    IntOp $2 $2 + 1
    StrCpy $0 $0 '' $2
 
    StrCpy $2 -1
    IntOp $2 $2 + 1
    StrCpy $3 $1 1 $2
    StrCmp $3 '' +2
    StrCmp $3 '.' 0 -3
    StrCpy $5 $1 $2
    IntOp $2 $2 + 1
    StrCpy $1 $1 '' $2
 
    StrCmp $4$5 '' equal
 
    StrCpy $6 -1
    IntOp $6 $6 + 1
    StrCpy $3 $4 1 $6
    StrCmp $3 '0' -2
    StrCmp $3 '' 0 +2
    StrCpy $4 0
 
    StrCpy $7 -1
    IntOp $7 $7 + 1
    StrCpy $3 $5 1 $7
    StrCmp $3 '0' -2
    StrCmp $3 '' 0 +2
    StrCpy $5 0
 
    StrCmp $4 0 0 +2
    StrCmp $5 0 begin newer2
    StrCmp $5 0 newer1
    IntCmp $6 $7 0 newer1 newer2
 
    StrCpy $4 '1$4'
    StrCpy $5 '1$5'
    IntCmp $4 $5 begin newer2 newer1
 
    equal:
    StrCpy $0 0
    goto end
    newer1:
    StrCpy $0 1
    goto end
    newer2:
    StrCpy $0 2
 
    end:
    Pop $7
    Pop $6
    Pop $5
    Pop $4
    Pop $3
    Pop $2
    Pop $1
    Exch $0
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
