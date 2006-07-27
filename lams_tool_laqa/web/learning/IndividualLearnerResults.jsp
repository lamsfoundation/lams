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

	<script language="JavaScript" type="text/JavaScript">
		function submitLearningMethod(actionMethod) 
		{
			document.QaLearningForm.method.value=actionMethod; 
			document.QaLearningForm.submit();
		}
		
		function submitMethod(actionMethod) 
		{
			submitLearningMethod(actionMethod);
		}
	</script>	
</head>

<body>
	<div id="page-learner">

<h1 class="no-tabs-below">
	<c:out value="${sessionScope.activityTitle}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	  <html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
  		<html:hidden property="method"/>	 
		<html:hidden property="toolSessionID"/>						  		

				<table class="forms">
				  <tr><th scope="col">
					  	 <bean:message key="label.learning.reportMessage"/> 
		    	  	</th>
				  </tr>

			
					<tr> <td align=left>		
						<table align="left">
							<c:forEach var="questionEntry" items="${sessionScope.mapQuestionContentLearner}">
									  <tr>
										<td colspan=2 NOWRAP>
										<b> <bean:message key="label.question"/> <c:out value="${questionEntry.key}" escapeXml="false"/>:  </b>  
									  		<c:out value="${questionEntry.value}" escapeXml="false"/> 
									  	 </td>
									  </tr>
									  
				  			  		<c:forEach var="answerEntry" items="${sessionScope.mapAnswers}">
										<c:if test="${answerEntry.key == questionEntry.key}"> 						  			  		
										  <tr>
											<td colspan=2 NOWRAP> <b> <bean:message key="label.learning.yourAnswer"/>  </b>  
												  <c:out value="${answerEntry.value}" escapeXml="false" />						  																	
										  	</td>
										  </tr>
									  	</c:if> 				    
									</c:forEach>
									
									  <tr>
										<td colspan=2 NOWRAP >
											&nbsp
									  	 </td>
									  </tr>
									
							</c:forEach>
						</table>
					</td></tr>
					
	  	   		  <tr>
						<td colspan=2 NOWRAP>
			                                <html:submit property="viewAllResults" 
                                             styleClass="button" 
                                             onclick="submitMethod('viewAllResults');">
                                    <bean:message key="label.allResponses"/>
                                </html:submit>
					  	 </td>
				  </tr>
				  
				</table>
	</html:form>
</div>

<div id="footer-learner"></div>	

	</div>
</body>
</html:html>















	