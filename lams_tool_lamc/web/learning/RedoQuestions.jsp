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
		<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />		
	</h1>
	
	
	
	
		<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
			<html:hidden property="toolContentID"/>						
			<html:hidden property="toolSessionID"/>								
			<html:hidden property="httpSessionID"/>											
			<html:hidden property="userID"/>											
			<html:hidden property="userOverPassMark"/>						
			<html:hidden property="passMarkApplicable"/>										
			<html:hidden property="learnerProgress"/>										
			<html:hidden property="learnerProgressUserId"/>										
			<html:hidden property="questionListingMode"/>													

			<div id="content">		
				<table class="forms">
					  <tr>
					  	<th scope="col" valign=top colspan=2> 
						    <bean:message key="label.assessment"/> 
					  	</th>
					  </tr>
				
			 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	 <b> <bean:message key="label.individual.results.withRetries"/> </b>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${mcGeneralLearnerFlowDTO.retries == 'false'}"> 							  
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
								  <b>  <bean:message key="label.individual.results.withoutRetries"/> </b>
						  	</td>
						  </tr>
					</c:if> 			


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <bean:message key="label.learner.redo"/> 
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=right valign=top colspan=2> 
							&nbsp
					  	</td>
					  </tr>	


					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	 <bean:message key="label.learner.bestMark"/>
						  	   <c:out value="${mcGeneralLearnerFlowDTO.learnerBestMark}"/> 
						  	<bean:message key="label.outof"/> 
						  	<c:out value="${mcGeneralLearnerFlowDTO.totalQuestionCount}"/> 
					  	</td>
					  </tr>	
					  
					  
				  <tr>
				  	<td NOWRAP colspan=2 align=left valign=top> 
				  			<html:submit property="viewAnswers" styleClass="button">
								<bean:message key="label.view.answers"/>
							</html:submit>	 		

	   						<html:submit property="redoQuestionsOk" styleClass="button">
								<bean:message key="label.redo.questions"/>
							</html:submit>	 				 		  					
				  	 </td>
				  </tr>
			 </table>

			
		 	</div>

	
		</html:form>	
	
	<div id="footer"></div>


</body>
</html:html>








	