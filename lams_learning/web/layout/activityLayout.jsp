<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html:html locale="true" xhtml="true">

	<tiles:useAttribute name="pageTitle" ignore="false" />
	<tiles:useAttribute name="title" ignore="false" />
	
	<head>
		<title><c:out value="${pageTitle}" /></title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link href="<c:url value="/css/learner.css" />" rel="stylesheet" type="text/css" />
		<script language="javascript" src="<c:url value="/javascript/learning.js" />"></script>
	</head>

	<body bgcolor="#9DC5EC">
		<div align="center">
		
			<table width="95%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
				<tr>
					<td valign="top"> 
					
						<table width="100%" border="0" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
							<tr> 
								<td width="136" height="10"></td>
								<td width="92%" height="10"></td>
							</tr>
							<tr bgcolor="#282871"> 
								<td width="50%" height="18" align="left">
									<html:img height="8" width="8" page="/images/spacer.gif" />
									<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif"><c:out value="${title}" /></font>
								</td>
								<td width="50%" height="18" align="right" > 
									<html:link page="/doc/LAMS_Learner_Guide_b60.pdf">
										<font color="#FFFFFF" size="1" face="Verdana, Arial, Helvetica, sans-serif">[HELP]</font>
									</html:link>
									<html:img height="8" width="8" page="/images/spacer.gif" />
								</td>
							</tr>
						</table>
						
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="100%" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" summary="This table is being used for layout purposes">
							<tr> 
								<td valign="top">
	
									<tiles:insert attribute="body" />
	
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" summary="This table is being used for layout purposes">
							<tr bgcolor="#282871"> 
								<td width="136" height="8"></td>
								<td width="92%" height="8" align="right"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
	</body>

</html:html>
