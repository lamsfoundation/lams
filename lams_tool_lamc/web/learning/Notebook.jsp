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

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>
</head>

<body class="stripes">
	
	<h1>
		<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false"/> 		
	</h1>
	
	
	
	
	<html:form  action="/learning?method=displayMc&validate=false" enctype="multipart/form-data" method="POST" target="_self">
		<html:hidden property="toolContentID"/>						
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="httpSessionID"/>			
		<html:hidden property="userID"/>								
		
		<div id="content">
		
		<table>
			<tr>
				<td>
					<c:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeXml="false"/> 
				</td>
			</tr>

			<tr>
				<td>
					<html:textarea cols="66" rows="8" property="entryText"></html:textarea>
				</td>
			</tr>

			<tr>
				<td class="right-buttons">
					<html:submit property="submitReflection" styleClass="button">					
						<bean:message key="button.endLearning"/>
					</html:submit>
				</td>
			</tr>
		</table>

		
		</div>
	</html:form>	
	
	<div id="footer"></div>


</body>
</html:html>








