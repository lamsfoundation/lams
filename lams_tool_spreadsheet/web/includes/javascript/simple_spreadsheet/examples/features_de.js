// visible cols, optional
cols = 13;
// visible rows, optional
rows = 30;

// Usage: [#col,#row,"value","style"],
dbCells = [
  // column headers
  [1,-1,"Spalte1","","Spalte1 langer Name"], // B-ColTitle
  [2,-1,"Spalte2","color: blue; width:100px;","Spalte2 langer Name"], // C-ColTitle
  [3,-1,"Spalte3","width:100px;"], // D-ColTitle

  // column group headers
  [0,-2,"Gruppe1","color:red;colspan:2;"],
  [3,-2,"Gruppe2","colspan:2;"],
  [5,-2,"Gruppe3","colspan:3;"],
  [8,-2,"Gruppe4","colspan:2;"],
  
  // row headers
  [-1,0,"Gruppe#1","color:blue;","Gruppe#1 langer Name"], // RowTitle 1
  [-1,1,"Zeile1",""], // RowTitle 2
  [-1,2,"Zeile2","height:50px;"], // RowTitle 3
  [-1,3,"Zeile3",""], // RowTitle 4
  [-1,4,"Gruppe2","color:blue;","Gruppe2 langer Name"], // RowTitle 5
  [-1,5,"Zeile1",""], // RowTitle 6
  [-1,6,"Zeile2",""], // RowTitle 7
  [-1,7,"Zeile3",""], // RowTitle 8

  [1,-1,"Spalte1","","Spalte1 langer Name"], // B-ColTitle
  [2,-1,"Spalte2","color: blue; width:100px;","Spalte2 langer Name"], // C-ColTitle
  [3,-1,"Spalte3","width:100px;"], // D-ColTitle

  // cells
  
  [0,0,"=C3","font-weight:bold;","example cell description"], // A1
  [2,2,"Test2","font-weight:bold;"], // C3
  [5,3,"html:Dies ist ein <b>html</b>-Text !<hr><ul><li>Punkt 1</li><li>Punkt 2</li><li>Punkt 3</li></ul>",""], // F4
  [5,5,"Dies ist ein mehrzeiliger Text !\\nZeile 2\\nZeile 3\\nEnde",""], // F6
  [1,2,"verbundene Zeilen","font-size:75%; rowspan:2;"], // B3
  [1,5,"verbundene Spalten","colspan:3;"], // B6

  [1,4,"gesperrt","readonly:true;height:50px;"], // B5
  [3,1,"10","format:dollar;"], // D2
  [2,1,"-2000000.999","width:100px; format:euro;"], // C2
  [1,1,"0.44321","format:percent;"], // B2
  
  [5,0,"=C3+'C\"3+C3'",""], // F1
  [5,1,"=C3+\"C'3-C3\"",""], // F2
  [5,2,"='C\"3-C3'+C3+\"C'3+C3\"",""], // F3
  [6,0,"=\"C'3+C3\"+C3+'C\"3-C3'",""], // G1
  [6,1,"='<t\"e\\'st'+100+\"te's\\\"t>\"","color:blue;"], // G2
  
  [4,0,"abc http://www.spiegel.de test http://www.heise.de",""], // E1
  [4,1,"http://www.spiegel.de","font-weight:bold;"], // E2
  [4,2,"firstname.lastname@invalid.local","font-weight:bold;"], // E3

  [0,6,"02/26/2006","format:date;"], // A7
  [1,6,"03/27/2007 9:11:12","format:datetime;"], // B7
  [2,6,"14:11:12","format:time;"], // C7
  [3,6,"1.2.2004","format:date;"], // D7
  [4,6,"03/27/2007 9:11:12","format:datefulltime;"], // E7
  [5,6,"02/26/2006","format:datefull;"], // F7
  
  [2,3,"=customCalc(D4+D5)+customCalc2(D4+D5)","color:blue;"], // C4
  [2,4,"=D2*D3","font-weight:bold;"], // C5
  [3,2,"20",""], // D3
  [3,3,"=D2+D3","font-weight:bold; text-decoration:underline;"], // D4
  [3,4,"=sum(D2:D4)","color:blue;"], // D5
  [4,4,"=E5","color:red; font-style:italic;"], // E5

// Usage: graph(type,title,data,keys)
  [4,3,"=graph('bar',E11,F12:F15,E12:E15,'Quartal','Wert')",""], // E4
  [4,5,"=graph('pie',F11,F12:F15,E12:E15)",""], // E6
  
  [4,16,"=graph('bar',E11,F12:F15,E12:E15,'Quartal','Wert')",""], // E17
  [4,18,"=graph('line',G11,F12:F15,E12:E15,'Quartal','Wert')",""], // E19
  
  [4,19,"=graph('linesteps','Stufendiagramm',F12:F15,E12:E15,'Quartal','Wert')",""], // E20
  [4,20,"=graph('scatter','Streuungsdiagramm',F12:F15,G12:G15)",""], // E21

  [5,16,"=graph2('bar','Balkendiagramm (2 Balken)',F12:F15,G12:G15,E12:E15)","colspan:2;"], // F17
  [5,17,"=graph2('baraccumulate','Balkendiagramm (kumuliert)',F12:F15,G12:G15,E12:E15)","colspan:2;"], // F18
  [5,18,"=graph2('line','Liniendiagramm (2 Linien)',F12:F15,G12:G15,E12:E15)","colspan:2;"], // F19
  [5,19,"=graph2('linesteps','Stufendiagramm (2 Linien)',F12:F15,G12:G15,E12:E15)","colspan:2;"], // F20

  [4,10,"Balkendiagramm",""], // F11
  [5,10,"Kuchendiagramm",""], // G11
  [6,10,"Liniendiagramm",""], // H11
  
  [4,11,"Q1",""], // E12
  [4,12,"Q2",""], // E13
  [4,13,"Q3",""], // E14
  [4,14,"Q4",""], // E15
  
  [5,11,"11",""], // F12
  [5,12,"2",""], // F13
  [5,13,"16",""], // F14
  [5,14,"22",""], // F15
  
  [6,11,"31",""], // G12
  [6,12,"37",""], // G13
  [6,13,"36",""], // G14
  [6,14,"43",""], // G15
  
  [1,7,"1",""], // B8
  [2,7,"2",""], // C8
  [3,7,"3",""], // D8
  [1,8,"6",""], // B9
  [2,8,"5",""], // C9
  [3,8,"5",""], // D9
  
  [1,10,"=\"sum(B8:D8)\"",""], // B11
  [2,10,"=sum(B8:D8)","readonly:true;"], // C11

  [1,11,"=\"min(B8:D8)\"",""], // B12
  [2,11,"=min(B8:D8)",""], // C12

  [1,12,"=\"max(B8:D8)\"",""], // B13
  [2,12,"=max(B8:D8)",""], // C13

  [1,13,"=\"avg(B8:D8)\"",""], // B14
  [2,13,"=avg(B8:D8)",""], // C14

  [1,14,"=\"count(B8:D8)\"",""], // B15
  [2,14,"=count(B8:D8)",""], // C15

  [1,15,"=\"sum([B8,D8])\"",""], // B16
  [2,15,"=sum([B8,D8])",""] // C16
];

// define a custom function to be used in the formulas, optional
registerFuncs = ["customCalc","customCalc2"];

function customCalc(num) {
  return num*2.5;
}

function customCalc2(num) {
  return num*3.0*showCell(1,1); // num * 2.5 * B2
}

// forbid paging ? optional
// allowPaging = false;
