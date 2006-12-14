## Example for NSIS Array script header
##                           by Afrow UK

## Include Array & LogicLib script header
!include Array.nsh
!include LogicLib.nsh

## General settings
OutFile ArrayTest.exe
Name    Arraytest
Caption ArrayTest
ShowInstDetails show

## Array creation
${Array} myArray1
${Array} myArray2

Section

## Initialise both arrays before we use them!
${myArray1->Init}
${myArray2->Init}

## Develop myArray1
${myArray1->Shift} "1. Hello there!"
${myArray1->Shift} "2. This took a while"
${myArray1->Shift} "3. To let you know!"
${myArray1->Shift} "4. This is a cool example..."
${myArray1->Shift} "5. Hope it helps."
${myArray1->Shift} "6. Or not."
${myArray1->Push}  "0. This was Pushed on front (took a bit of time!)"

## Print contents of myArray1
DetailPrint "========================="
DetailPrint " Array: myArray1"
DetailPrint ""

Push $R0 # Save value of $R0 in case we need it later
Push $R1 # & $R1
 StrCpy $R0 "$myArray1_UBound"

${Do}
  ${myArray1->Get} $R0 $R1
  DetailPrint "$R0 is: $R1"
 IntOp $R0 $R0 - 1
${LoopUntil} $R0 < 0

Pop $R1 # Value of $R1 is back!
Pop $R0 # & $R0

## Copy myArray1 into myArray2
${myArray1->Copy} myArray2 1

## Do stuff with myArray2
## Comment/uncomment and see what happens!!!
 #${myArray2->Put} 3 "Put @ 3: Gay bear!"
 ${myArray2->Reverse}
 #${myArray2->Shift} "It got reversed!"
 #${myArray2->Delete} 5

## Print contents of myArray2
DetailPrint ""
DetailPrint "========================="
DetailPrint " Array: myArray2"
DetailPrint ""

Push $R0 # Save value of $R0 in case we need it later
Push $R1 # & $R1
 StrCpy $R0 "$myArray2_UBound"

${Do}
  ${myArray2->Get} $R0 $R1
  DetailPrint "$R0 is: $R1"
 IntOp $R0 $R0 - 1
${LoopUntil} $R0 < 0

## Pop and Unshift
DetailPrint ""
${myArray2->Pop} $R0
DetailPrint "Popped: $R0"
${myArray2->Unshift} $R0
DetailPrint "Unshifted: $R0"

Pop $R1 # Value of $R1 is back!
Pop $R0 # & $R0

## Don't need Arrays anymore. Lets DESTROY them!
${myArray2->Destroy}
${myArray1->Destroy}

SectionEnd