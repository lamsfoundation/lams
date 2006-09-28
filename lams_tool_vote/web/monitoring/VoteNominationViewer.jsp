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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

	<html:html locale="true">
	<head>
	<title> <bean:message key="label.learnersVoted"/> </title>

	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript" src="${lams}includes/javascript/prototype.js"></script>
	<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
	
</head>
<body class="stripes">
<div id="page">

    <table>
	    <tr> <th NOWRAP colspan=2 align=center>  <bean:message key="label.learnersVoted"/> </th> </tr>
	
		<tr> 
	       <td NOWRAP align=center valign=top><b> <bean:message key="label.user"/> </b></td>
		   <td NOWRAP align=center valign=top><b> <bean:message key="label.attemptTime"/> </b></td>
	   </tr>
	    
		<c:forEach var="userData" items="${voteGeneralMonitoringDTO.mapStudentsVoted}">
			<tr>		 
					 <td NOWRAP valign=top align=center>    <c:out value="${userData.userName}"/>   </td>  
					 <td NOWRAP valign=top align=center>    <lams:Date value="${userData.attemptTime}"/>  </td>					 
			</tr>				 
		</c:forEach>		  	
    </table>

</div>
</body>
</html:html>
