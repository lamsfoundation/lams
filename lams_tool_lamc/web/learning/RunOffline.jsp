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
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css/>
	<title><fmt:message key="activity.title" /></title>
</head>

<body class="stripes">
	
	<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
		<html:hidden property="toolContentID"/>						
		<html:hidden property="toolSessionID"/>						
		<html:hidden property="httpSessionID"/>			
		<html:hidden property="userID"/>								

	
		<div id="content">
			&nbsp&nbsp&nbsp&nbsp&nbsp <fmt:message key="label.learning.forceOfflineMessage" />
			
			<table>	
			<tr>	
			<td>
				<div class="right-buttons">

					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}"> 						  			  		
						<html:submit property="learnerFinished" styleClass="button">
							<fmt:message key="label.finished"/>
						</html:submit>	 				
				  	</c:if> 				    					
	
					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}"> 						  			  		
						<html:submit property="forwardtoReflection"  styleClass="button">
							<fmt:message key="label.continue"/>
						</html:submit>	 				
				  	</c:if> 				    					
			  	
				</div> 		  															 		  					
			</td> 
			</tr>
			</table>
		</div>
	</html:form>	
	
	<div id="footer"></div>


</body>
</html:html>








