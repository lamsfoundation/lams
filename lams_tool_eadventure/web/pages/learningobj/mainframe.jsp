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
<!DOCTYPE html> 
		
<%@ include file="/common/taglibs.jsp" %>


<lams:html>
<lams:head>
<html:base />
	<%@ include file="/common/header.jsp"%>
	
	<c:set var="sessionMapID" value="${param.sessionMapID}"/>
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
	<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
	<c:set var="toolContentID" value="${sessionMap.toolContentID}" />
	<c:set var="userFName" value="${sessionMap.userFName}" />
	<c:set var="userLName" value="${sessionMap.userLName}" />
	<c:set var="user" value="${sessionMap.userID}" />

	
	
<script language="JavaScript" type="text/JavaScript">
	        <!--
	      		function setParams(){
	      	
				window.frames[1].document.getElementById('eadventure').setUserId("${user}");
				window.frames[1].document.getElementById('eadventure').setRunId("${toolSessionID}");
				window.frames[1].document.getElementById('eadventure').setUserFName("${userFName}");
				window.frames[1].document.getElementById('eadventure').setUserLName("${userLName}");
			

			window.frames[1].document.getElementById('eadventure').setAppletURL(window.frames[1].location);
	      		}

	        //-->
	  </script>
</lams:head>
<frameset cols="0%,100%" rows="*">
	<frame src="navigatortree.jsp?sessionMapID=${param.sessionMapID}" name="navigatorFrame" marginheight="0"  scrolling="YES">
	<frame src="defaultcontent.jsp" name="contentFrame" marginheight="0" scrolling="YES">
	
</frameset>
</lams:html>
