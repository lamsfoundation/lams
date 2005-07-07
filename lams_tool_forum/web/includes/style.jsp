<%@ page language="java" contentType="text/css" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
body {
	behavior:url("<html:rewrite page="/includes/csshover.htc" />");
	font-family: Verdana, Helvetica;
	background-color:#F9F9F5;
	font-size:10px;
	color: #606060;
	margin: 0px 0px 0px 0px;
	padding: 0px 0px 0px 0px;
}

A {
	font-family: Verdana, Helvetica;
	color:#942500;
	text-decoration:none;
	font-weight : bold;
	font-size:10px;
}

A:hover {
	text-decoration:none;
	color:#CC0000;
}

td {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

th {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

b {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

p {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

li {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

div {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

img {
	border: 0px;
}

.mainTitle {
	font-family: Verdana, Helvetica;
	font-size:17px;
	color:#5A5A5A;
}

.subTitle {
	font-family: Verdana, Helvetica;
	font-size:14px;
	color:#444444;
}

.contentArea {
	padding: 15px 15px 15px 15px;
	font-family: Verdana, Helvetica;
	font-size:11px;
}

.searchBox {
	padding: 10px 10px 10px 10px;
	font-family: Verdana, Helvetica;
	font-size:11px;
}

.formElement {
	font-family: Verdana, Helvetica;
	font-size:11px;
}

.error, .message {
	font-family : Arial,Helvetica;
	font-size: 12px;
	color: #FF0000;
	font-weight:bold;
}

input, textarea, select  {
	padding-left:4px;
	margin:1px 1px 1px 1px;
	border:1px solid silver;
}
select option {
	background-color: #f8f8f8;
}
input[type=button], input[type=submit], input[type=reset] {
	background-color: #ECECDF;
	border: 1px outset #B2D2FB;
	-moz-border-radius:25px;
}
input:focus, select:focus, textarea:focus, input:hover, select:hover, textarea:hover {
	background-color: #f8f8f8;
	border:1px solid #f2d822;
}
.displayTable {
    background-color:#ffffff;
    border-spacing:0px;
    border-collapse:collapse;
    padding:0px;
    width:100%;
}
.displayTitleCell {
    font-weight:normal;
    font-size:14;
    border-top:1px;
    border-left:1px;
    border-right:1px;
    border-bottom:0px;
    border-style:solid;
    border-color:#cccccc;
    background-color:#E5ECF9;
    padding-top:2px;
    padding-bottom:2px;
    padding-left:5px;
    padding-right:5px;
    text-align:left;
}
.displaySearchResultsCell {
    font-weight:normal;
    font-size:9;
    border-top:1px;
    border-left:1px;
    border-right:1px;
    border-bottom:0px;
    border-style:solid;
    border-color:#cccccc;
    background-color:#E5ECF9;
    padding-top:2px;
    padding-bottom:2px;
    padding-left:5px;
    padding-right:5px;
    vertical-align:bottom;
    text-align:right;
}

.displayHeaderCell {
   	font-weight: bold;
   	background-color: #eee8dc;
}

.displayDataCell {
    border:1px;
    border-style:solid;
    border-color:#cccccc;
    padding-top:2px;
    padding-bottom:2px;
    padding-left:5px;
    padding-right:5px;
}
 .keyDisplayTableCell {
    background-color:#f0f0f0;
    vertical-align:top;
    text-align:right;
    width:125px;
    padding:5px;
    font-style: italic;
}
.exportlinks {
	border:1px solid #000000
	width: 90%;
	background-color: #F0F0F0;
	font-weight: bold;
}
tr.odd {
	background-color: #F0F0F0;
}
tr.odd:hover, tr.even:hover {
	background-color: #E0E0E0;
}
fieldset {
	width: 90%;
	border: 1px dotted gray;
}
fieldset legend {
	font-size:12px;
	font-weight:bold;
	font-style:italic;
	background-color: #F9F9F5;
}
.selectedTab {
	background-color: #ECECDF;
	padding: 2px;
}
.tab {
	background-color: #ECECDF;
	padding: 2px;
	border: 1px solid #f2d822;
}
td.selectedTab a, td.tab a {
	color: #942500;
}
a.popup img {
	cursor: help;
	vertical-align: middle;
	padding-bottom: 3px;
}
a.subMenu {
	background-color: #ECECDF;
	padding: 2px;
	border: 1px solid #f2d822;
}
a.subSubMenu {
	background-color: #ECECDF;
	padding: 1px;
	border: 1px solid #f2d822;
}
a.menuItem {
	background-color: #ECECDF;
	padding: 1px;
	border: 1px solid #f2d822;
	font-size: 9px;
}
a.subMenu:hover, a.subSubMenu:hover, a.menuItem:hover {
	background-color: #F9F9F5;
	color: #942500;
}
a.selectedSubMenu {
	background-color: black;
	color: white;
}
td.tab:hover {
	background-color: #F9F9F5;
}
.calendarMonth {
	font-weight:bold;
	color: #942500;
	text-align: left;
	width: 20%;
}
.calendarWeekDay {
	background-color: #ECECDF;
	font-weight:bold;
	text-align: center;
	vertical-align: middle;
	width: 50;
}
.calendarUnselectableDay {
	background-color: #F9F9F5;
	color: #808080;
	text-align: center;
	vertical-align: middle;
	width: 50;
}
.calendarDay {
	background-color: #F9F9F5;
	text-align: center;
	vertical-align: middle;
	width: 50;
	height: 30;
}
td.calendarDay:hover {
	background-color: #ECECDF;
}
.calendarHighlightedDay {
	background-color: #ECECDF;
	font-weight:bold;
	text-align: center;
	vertical-align: middle;
	width: 50;
} .forumTable {
    background-color:#ffffff;
    cellspacing="1";
    cellpadding="1";
    width="100%";
}
