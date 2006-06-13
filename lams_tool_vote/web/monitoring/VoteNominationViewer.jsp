<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

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


<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%@ page import="java.util.LinkedHashSet" %>
<%@ page import="java.util.Set" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<html:html locale="true">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:headItems/>
	<title> <bean:message key="label.learnersVoted"/> </title>
</head>
<body>

    <table width='80%' cellspacing='8' align='CENTER' class='forms'>
	    <tr> <th NOWRAP colspan=2>  <bean:message key="label.learnersVoted"/> </th> </tr>
	
		<tr> 
	       <td NOWRAP align=center valign=top><b> <bean:message key="label.user"/> </b></td>
		   <td NOWRAP align=center valign=top><b> <bean:message key="label.attemptTime"/> </b></td>
	   </tr>
	    
		<c:forEach var="userData" items="${mapStudentsVoted}">
			<tr>		 
					 <td NOWRAP valign=top align=center>   <font size=2> <c:out value="${userData.userName}"/> </font>  </td>  
					 <td NOWRAP valign=top align=center>   <font size=2> <c:out value="${userData.attemptTime}"/> </font> </td>
			</tr>				 
		</c:forEach>		  	
    </table>

</body>
</html:html>
