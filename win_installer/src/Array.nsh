
 ###############################################################################################
 ## Array.nsh
 ## NSIS Array & script header
 ##
 ##  Writted by Afrow UK (Stuart Welch)
 ##  Last modified: 6th September 2005
 ###############################################################################################

 ## See Array.txt for usage information.
 ## Please don't modify this file!

 ## Current Array methods:
 ## Array, Set, Get, Shift, Unshift, Push, Pop, Put, Slice, Delete, Reverse, Copy, Init, Clear,
 ## ValExists

 !ifndef ArrayIncluded
 !define ArrayIncluded

 !ifndef ArrayNoTemp1
  Var Array_Temp1
 !endif
 !ifndef ArrayNoTemp2
  Var Array_Temp2
 !endif
 !ifndef ArrayNoTemp3
  Var Array_Temp3
 !endif
 !ifndef ArrayNoTemp4
  Var Array_Temp4
 !endif

 ###############################################################################################
 ## Array: Creates an Array object
 ###############################################################################################
 !macro Array Array
  !ifndef "${Array}-Used"
  !define "${Array}-Used"

   !define "${Array}-Store" "$PLUGINSDIR\${Array}.tmp"
   !define "${Array}-Section" "0"

  !ifdef Array-NoErrors
   !define "${Array}-NoErrors"
   !undef Array-NoErrors
  !endif

  Var "${Array}_UBound"

  !define "${Array}->Set"      '!insertmacro "Array-Set"     "${Array}"'
  !define "${Array}->Get"      '!insertmacro "Array-Get"     "${Array}"'
  !define "${Array}->Shift"    '!insertmacro "Array-Shift"   "${Array}"'
  !define "${Array}->Unshift"  '!insertmacro "Array-Unshift" "${Array}"'
  !define "${Array}->Push"     '!insertmacro "Array-Push"    "${Array}"'
  !define "${Array}->Pop"      '!insertmacro "Array-Pop"     "${Array}"'
  !define "${Array}->Put"      '!insertmacro "Array-Put"     "${Array}"'
  !define "${Array}->Slice"    '!insertmacro "Array-Slice"   "${Array}"'
  !define "${Array}->Delete"   '!insertmacro "Array-Delete"  "${Array}"'
  !define "${Array}->Reverse"  '!insertmacro "Array-Reverse" "${Array}"'
  !define "${Array}->Copy"     '!insertmacro "Array-Copy"    "${Array}"'
  !define "${Array}->Exists"   '!insertmacro "Array-Exists"  "${Array}"'
  !define "${Array}->Clear"    '!insertmacro "Array-Clear"   "${Array}"'
  !define "${Array}->Init"     '!insertmacro "Array-Init"    "${Array}"'
  !define "${Array}->Destroy"  '!insertmacro "Array-Destroy" "${Array}"'

  !else
   !error "Array: ${Array} has already been declared once!"
  !endif
 !macroend
 !define Array "!insertmacro Array"
 !define ArrayNoErrors "!define Array-NoErrors"

 ###############################################################################################
 ## Array-CheckForError: Internal macro used for error message
 ###############################################################################################
 !macro Array-CheckForError ArrayUBound Index
    IntCmp "${ArrayUBound}" "${Index}" +3 0 +3
    MessageBox MB_OK|MB_ICONSTOP "Index: ${Index} is out of range in Array: Array"
     Abort
 !macroend

 ###############################################################################################
 ## Array-Set: Sets value at Array Index
 ###############################################################################################
 !macro "Array-Set" Array Index Value
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifndef "${Array}-NoErrors"
   !insertmacro Array-CheckForError "$${Array}_UBound" "${Index}"
  !endif
  WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "${Index}" "${Value}"
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Set" "!insertmacro Array-Set"

 ###############################################################################################
 ## Array-Get: Gets value at Array Index
 ###############################################################################################
 !macro "Array-Get" Array Index Var
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifndef "${Array}-NoErrors"
   !insertmacro Array-CheckForError "$${Array}_UBound" "${Index}"
  !endif
  ReadINIStr "${Var}" "${${Array}-Store}" "${${Array}-Section}" "${Index}"
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Get" "!insertmacro Array-Get"

 ###############################################################################################
 ## Array-Push: Puts element at front of array
 ###############################################################################################
 !macro "Array-Push" Array Value
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
   StrCpy $Array_Temp2 "$${Array}_UBound"
   IntCmp $Array_Temp2 -1 +6 +6
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
     IntOp $Array_Temp3 $Array_Temp2 + 1
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp3" "$Array_Temp1"
    IntOp $Array_Temp2 $Array_Temp2 - 1
   Goto -5
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "0" "${Value}"
    IntOp "$${Array}_UBound" "$${Array}_UBound" + 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Push" "!insertmacro Array-Push"

 ###############################################################################################
 ## Array-Pop: Takes element from front of Array
 ###############################################################################################
 !macro "Array-Pop" Array Var
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  ReadINIStr "${Var}" "${${Array}-Store}" "${${Array}-Section}" "0"
   StrCpy $Array_Temp2 1
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
     IntOp $Array_Temp3 $Array_Temp2 - 1
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp3" "$Array_Temp1"
   StrCmp $Array_Temp2 "$${Array}_UBound" +3
    IntOp $Array_Temp2 $Array_Temp2 + 1
   Goto -5
   DeleteINIStr "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound"
   IntOp "$${Array}_UBound" "$${Array}_UBound" - 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Pop" "!insertmacro Array-Pop"

 ###############################################################################################
 ## Array-Put: Puts element at Index in Array
 ###############################################################################################
 !macro "Array-Put" Array Index Value
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifndef "${Array}-NoErrors"
   !insertmacro Array-CheckForError "$${Array}_UBound" "${Index}"
  !endif
   StrCpy $Array_Temp2 "$${Array}_UBound"
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
     IntOp $Array_Temp3 $Array_Temp2 + 1
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp3" "$Array_Temp1"
    IntOp $Array_Temp2 $Array_Temp2 - 1
   IntCmp $Array_Temp2 "${Index}" -4 0 -4
   WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "${Index}" "${Value}"
   IntOp "$${Array}_UBound" "$${Array}_UBound" + 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Put" "!insertmacro Array-Put"

 ###############################################################################################
 ## Array-Slice: Cuts element from Array
 ###############################################################################################
 !macro "Array-Slice" Array Index Var
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifndef "${Array}-NoErrors"
   !insertmacro Array-CheckForError "$${Array}_UBound" "${Index}"
  !endif
  ReadINIStr "${Var}" "${${Array}-Store}" "${${Array}-Section}" "${Index}"
   StrCpy $Array_Temp2 ${Index}
    IntOp $Array_Temp2 $Array_Temp2 + 1
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
     IntOp $Array_Temp3 $Array_Temp2 - 1
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp3" "$Array_Temp1"
   StrCmp $Array_Temp2 "$${Array}_UBound" -4
   DeleteINIStr "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound"
   IntOp "$${Array}_UBound" "$${Array}_UBound" - 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Slice" "!insertmacro Array-Slice"

 ###############################################################################################
 ## Array-Delete: Deletes element in Array
 ###############################################################################################
 !macro "Array-Delete" Array Index
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifndef "${Array}-NoErrors"
   !insertmacro Array-CheckForError "$${Array}_UBound" "${Index}"
  !endif
   StrCpy $Array_Temp2 ${Index}
    IntOp $Array_Temp2 $Array_Temp2 + 1
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
     IntOp $Array_Temp3 $Array_Temp2 - 1
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp3" "$Array_Temp1"
   IntCmp $Array_Temp2 "$${Array}_UBound" -4
   DeleteINIStr "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound"
   IntOp "$${Array}_UBound" "$${Array}_UBound" - 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Delete" "!insertmacro Array-Delete"

 ###############################################################################################
 ## Array-Reverse: Reverses elements in Array
 ###############################################################################################
 !macro "Array-Reverse" Array
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
   IntCmp "$${Array}_UBound" 0 +10 +10
   StrCpy $Array_Temp2 "$${Array}_UBound"
   StrCpy $Array_Temp4 "0"
    ReadINIStr "$Array_Temp1" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2"
    ReadINIStr "$Array_Temp3" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp4"
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp2" "$Array_Temp3"
    WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp4" "$Array_Temp1"
    IntOp $Array_Temp2 $Array_Temp2 - 1
    IntOp $Array_Temp4 $Array_Temp4 + 1
   IntCmp $Array_Temp2 $Array_Temp4 0 0 -6
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Reverse" "!insertmacro Array-Reverse"

 ###############################################################################################
 ## Array-Shift: Puts element on the end of Array
 ###############################################################################################
 !macro "Array-Shift" Array Value
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  IntOp "$${Array}_UBound" "$${Array}_UBound" + 1
  WriteINIStr "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound" "${Value}"
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Shift" "!insertmacro Array-Shift"

 ###############################################################################################
 ## Array-Unshift: Removes element from end of Array
 ###############################################################################################
 !macro "Array-Unshift" Array Var
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  ReadINIStr "${Var}" "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound"
  DeleteINIStr "${${Array}-Store}" "${${Array}-Section}" "$${Array}_UBound"
  IntOp "$${Array}_UBound" "$${Array}_UBound" - 1
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Unshift" "!insertmacro Array-Unshift"

 ###############################################################################################
 ## Array-Copy: Copies one Array to another
 ###############################################################################################
 !macro Array-Copy Array ToArray Mode
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  !ifdef "${ToArray}-Used"
  !ifdef "${ToArray}-Inited"
  !define "Mode-${Mode}"
   StrCpy $Array_Temp1 "-1"
  !ifdef "Mode-w"
   StrCpy $Array_Temp2 "-1"
   StrCpy "$${ToArray}_UBound" "$${Array}_UBound"
  !else
   StrCpy $Array_Temp2 "$${ToArray}_UBound"
   StrCmp "$${ToArray}_UBound" -1 0 +2
    StrCpy "$${ToArray}_UBound" 0
   IntOp "$${ToArray}_UBound" "$${Array}_UBound" + "$${ToArray}_UBound"
  !endif
   IntOp $Array_Temp1 $Array_Temp1 + 1
   IntOp $Array_Temp2 $Array_Temp2 + 1
   ReadINIStr "$Array_Temp3" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp1"
   WriteINIStr "${${ToArray}-Store}" "${${ToArray}-Section}" "$Array_Temp2" "$Array_Temp3"
   StrCmp $Array_Temp1 "$${Array}_UBound" 0 -4
  !else
   !error "Array: ${ToArray} not initialised!"
  !endif
  !else
   !error "Array: ${ToArray} not declared!"
  !endif
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Copy" "!insertmacro Array-Copy"

 ###############################################################################################
 ## Array-Exists: Checks if Array element exists
 ###############################################################################################
 !macro Array-Exists Array Value TrueGo FalseGo
  !ifdef "${Array}-Used"
  !ifdef "${Array}-Inited"
  StrCpy $Array_Temp1 0
   ReadINIStr "$Array_Temp2" "${${Array}-Store}" "${${Array}-Section}" "$Array_Temp1"
   StrCmp $Array_Temp2 "${Value}" "${TrueGo}"
   IntOp $Array_Temp1 $Array_Temp1 + 1
  StrCmp $Array_Temp1 "$${Array}_UBound" "${FalseGo}" -3
  !else
   !error "Array: ${Array} not initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Exists" "!insertmacro Array-Exists"

 ###############################################################################################
 ## Array-Clear: Clears Array elements
 ###############################################################################################
 !macro Array-Clear Array
  !ifdef "${Array}-Used"
   StrCpy "$${Array}_UBound" -1
   DeleteINISec "${${Array}-Store}" "${${Array-Section}"
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Clear" "!insertmacro Array-Clear"

 ###############################################################################################
 ## Array-Init: Initialises Array for use
 ###############################################################################################
 !macro Array-Init Array
  !ifdef "${Array}-Used"
  !ifndef "${Array}-Inited"
   !define "${Array}-Inited"
   StrCpy "$${Array}_UBound" -1
  !else
   !error "Array: ${Array} already initialised!"
  !endif
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Init" "!insertmacro Array-Init"

 ###############################################################################################
 ## Array-Destroy: Deletes Array object
 ###############################################################################################
 !macro Array-Destroy Array
  !ifdef "${Array}-Used"
   StrCpy "$${Array}_UBound" -1
   SetDetailsPrint none
    FlushINI "${${Array}-Store}"
    Delete "${${Array}-Store}"
   SetDetailsPrint both
   !undef "${Array}-Store"
   !undef "${Array}-Section"
   !undef "${Array}-Used"
  !else
   !error "Array: ${Array} not declared!"
  !endif
 !macroend
 !define "Array-Destroy" "!insertmacro Array-Destroy"

 !endif # ArrayIncluded

 # eof