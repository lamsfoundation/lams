document.title = "Simple Groupware Survey Results";

loadStyleFile("styles_noline.css");
printstyle = "print_noline.css";
rows = 95;
isWriteable = false;
allowPaging = false;

col_min_width = "50px";

dbCells = [
  [3,1,"Which browser do you use ?","colspan:3; font-weight:bold;"], // D2

  [3,3,"Firefox 2","width:100px;"], // D4
  [4,3,"47",""], // E4
  [6,3,"=graph('pie',D2,E4:E11,D4:D11,'','',400,180)","rowspan:8;"], // G4

  [3,4,"IE 7",""], // D5
  [4,4,"4",""], // E5

  [3,5,"Opera 9.2",""], // D6
  [4,5,"3",""], // E6

  [3,6,"Firefox 1.5",""], // D7
  [4,6,"3",""], // E7

  [3,7,"IE 6",""], // D8
  [4,7,"3",""], // E8

  [3,8,"Opera 9.1",""], // D9
  [4,8,"1",""], // E9

  [3,9,"Konqueror\\n(3.5.6)",""], // D10
  [4,9,"1",""], // E10

  [3,10,"Others",""], // D11
  [4,10,"0",""], // E11

  [3,13,"Firefox",""], // D14
  [4,13,"50",""], // E14
  [6,13,"=graph('pie',D2,E14:E17,D14:D17,'','',400,130)","rowspan:4;"], // G14

  [3,14,"IE",""], // D15
  [4,14,"7",""], // E15

  [3,15,"Opera",""], // D16
  [4,15,"4",""], // E16

  [3,16,"Konqueror",""], // D17
  [4,16,"1",""], // E17

  [3,20,"Which database do you use ?","colspan:3; font-weight:bold;"], // D21

  [3,22,"MySQL 5.1",""], // D23
  [4,22,"18",""], // E23
  [6,22,"=graph('pie',D21,E23:E31,D23:D31,'','',430,185)","rowspan:9;"], // G23

  [3,23,"MySQL 5",""], // D24
  [4,23,"15",""], // E24
  [6,23,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G24

  [3,24,"MySQL 4.1",""], // D25
  [4,24,"11",""], // E25
  [6,24,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G25

  [3,25,"PostgreSQL 8.2",""], // D26
  [4,25,"10",""], // E26
  [6,25,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G26

  [3,26,"MySQL 4",""], // D27
  [4,26,"3",""], // E27
  [6,26,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G27

  [3,27,"PostgreSQL 8.1",""], // D28
  [4,27,"2",""], // E28
  [6,27,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G28

  [3,28,"Oracle 9i",""], // D29
  [4,28,"1",""], // E29
  [6,28,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G29

  [3,29,"PostgreSQL 8.0",""], // D30
  [4,29,"0",""], // E30
  [6,29,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G30

  [3,30,"Oracle 10g",""], // D31
  [4,30,"0",""], // E31
  [6,30,"=graph('pie',D2,E14:E22,D14:D22,'','',400,185)","rowspan:9;"], // G31

  [3,33,"MySQL",""], // D34
  [4,33,"=E27+E25+E24+E23",""], // E34
  [6,33,"=graph('pie',D21,E34:E36,D34:D36,'','',430,130)","rowspan:3;"], // G34

  [3,34,"PostgreSQL",""], // D35
  [4,34,"12",""], // E35

  [3,35,"Oracle",""], // D36
  [4,35,"1",""], // E36

  [3,39,"Which operating system do you use on the server ?","font-weight:bold; colspan:4;"], // D40

  [3,41,"Debian",""], // D42
  [4,41,"14",""], // E42
  [6,41,"=graph('pie',D40,E42:E54,D42:D54,'','',450,270)","rowspan:13;"], // G42

  [3,42,"CentOS",""], // D43
  [4,42,"9",""], // E43

  [3,43,"Ubuntu",""], // D44
  [4,43,"7",""], // E44

  [3,44,"SuSE E",""], // D45
  [4,44,"6",""], // E45

  [3,45,"Win2k3",""], // D46
  [4,45,"6",""], // E46

  [3,46,"BSD",""], // D47
  [4,46,"5",""], // E47

  [3,47,"WinXP",""], // D48
  [4,47,"5",""], // E48

  [3,48,"RedHat",""], // D49
  [4,48,"2",""], // E49

  [3,49,"SuSE",""], // D50
  [4,49,"2",""], // E50

  [3,50,"Fedora",""], // D51
  [4,50,"1",""], // E51

  [3,51,"Win2k",""], // D52
  [4,51,"1",""], // E52

  [3,52,"Gentoo",""], // D53
  [4,52,"1",""], // E53

  [3,53,"Solaris",""], // D54
  [4,53,"0",""], // E54

  [3,55,"Debian",""], // D56
  [4,55,"21",""], // E56
  [6,55,"=graph('pie',D40,E56:E60,D56:D60,'','',450,140)","rowspan:5;"], // G56

  [3,56,"RedHat",""], // D57
  [4,56,"12",""], // E57

  [3,57,"Windows",""], // D58
  [4,57,"12",""], // E58

  [3,58,"SuSE",""], // D59
  [4,58,"8",""], // E59

  [3,59,"BSD",""], // D60
  [4,59,"5",""], // E60

  [3,63,"Custom browser:",""], // D64
  [4,63,"IE 7 (1x), IE 6 (1x)","colspan:3;"], // E64

  [3,65,"Custom OS:",""], // D66
  [4,65,"Slackware Linux (2x), Ubuntu & Nokia N80 (1x), hosted linux (1x), Windows 2003/Fedora Core 6 (1x), windows 2003 (1x), Netware 5.1 (1x)","colspan:3;"], // E66

  [3,67,"New Features:",""], // D68
  [4,67,"Implemented",""], // E68
  [5,67,"document check in / check out","colspan:2;"], // F68

  [5,68,"posibility to add info to files mounted via CIFS (meta data)","colspan:2;"], // F69

  [5,69,"client with ssl certificates signed by CAcert.org root certificate","colspan:2;"], // F70

  [5,70,"An option to use a WYSIWYG editor in the text area. (configurable with sgsML)","colspan:2;"], // F71

  [4,72,"Roadmap",""], // E73
  [5,72,"short, simple URLs that refer to Companies, Contacts, Calendars, etc.","colspan:2;"], // F73

  [5,73,"Administrative workflow with attendance and time management","colspan:2;"], // F74

  [5,74,"support for other webservers like lighttpd","colspan:2;"], // F75

  [5,75,"ERP Module","colspan:2;"], // F76

  [5,76,"HR management module - leave management/application/approvals, employee records, etc.","colspan:2;"], // F77

  [5,77,"Bookmark syncing with Firefox","colspan:2;"], // F78

  [5,78,"iCalendar server via WebDAV for writing, CalDAV support","colspan:2;"], // F79

  [5,79,"Drag and drop of e-mails and files","colspan:2;"], // F80

  [4,81,"Rejected",""], // E82
  [5,81,"baum (not clear)","colspan:2;"], // F82

  [5,82,"SLA integration (not enough details)","colspan:2;"], // F83

  [5,83,"netmeeting portal (not enough details)","colspan:2;"], // F84

  [5,84,"Voip and messaging (not enough details)","colspan:2;"], // F85

  [5,85,"A \"nicer\" Frontend (not enough details)","colspan:2;"], // F86

  [5,86,"Forms Designer (not clear)","colspan:2;"], // F87

  [5,87,"Real integration of SyncML into Simple Groupware without Funambol Sync Server (why ?)","colspan:2;"], // F88

  [5,88,"Document management with tagging that is similar to del.icio.us (documents <> bookmarks)\\n","colspan:2;"], // F89

  [5,89,"a comprehensive implementation and customization guide (not enough details)","colspan:2;"], // F90

  [5,90,"Visually setup paths in the modules mount point, being able to setup URL's visually, instead of having to type them in (not clear)","colspan:2;"], // F91

  [5,91,"There is no mention on the website if and this application's API can be used to integrate into other applications (invalid)","colspan:2;"], // F92

  [5,92,"A reports/Form module/Wrapper:\\nI know sometimes creating a report or a form can be very different from person to person, so that's the reason why having wrapper module would help. I create php forms and reports using the PEAR packages I use the XOOPS CMS to provide users with the links to the report. I also use a XOOPS module to wrap the content withing the XOOPS CMS. (not clear)","colspan:2;"], // F93
];
