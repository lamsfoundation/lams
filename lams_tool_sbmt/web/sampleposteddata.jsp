<%@ page language="java" import="java.util.*" %>
<!--
 * FCKeditor - The text editor for internet
 * Copyright (C) 2003-2005 Frederico Caldeira Knabben
 * 
 * Licensed under the terms of the GNU Lesser General Public License:
 * 		http://www.opensource.org/licenses/lgpl-license.php
 * 
 * For further information visit:
 * 		http://www.fckeditor.net/
 * 
 * File Name: sampleposteddata.jsp
 * 	FCKeditor sample.
 * 
 * Version:  2.1
 * Modified: 2005-03-29 21:30:00
 * 
 * File Authors:
 * 		Simone Chiaretta (simo@users.sourceforge.net)
-->
<%
	Enumeration params = request.getParameterNames();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
	<head>
		<title>FCKeditor - Samples - Posted Data</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="robots" content="noindex, nofollow">
		<link href="../sample.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<h1>FCKeditor - Samples - Posted Data</h1>
		This page lists all data posted by the form.
		<hr>
		<table width="100%" border="1" cellspacing="0" bordercolor="#999999">
			<tr style="FONT-WEIGHT: bold; COLOR: #dddddd; BACKGROUND-COLOR: #999999">
				<td noWrap>Field Name&nbsp;&nbsp;</td>
				<td>Value</td>
			</tr>
			<%
			String parameter = null ;
			while( params.hasMoreElements() )
			{
				parameter = (String) params.nextElement() ;
			%>
			<tr>
				<td valign="top" nowrap><b><%=parameter%></b></td>
				<td width="100%"><%=request.getParameter(parameter)%></td>
			</tr>
			<%
			}
			%>
		</table>
	</body>
</html>
