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
</head>

<body>
	<div id="page-learner">
	
	<h1 class="no-tabs-below">
		<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
	</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	<html:form  action="/learning?method=displayMc&validate=false" method="POST" target="_self">
		<html:hidden property="toolContentID"/>						
		<html:hidden property="toolSessionID"/>								
		<html:hidden property="httpSessionID"/>										
		<html:hidden property="userOverPassMark"/>						
		<html:hidden property="passMarkApplicable"/>										
		<html:hidden property="learnerProgress"/>										
		<html:hidden property="learnerProgressUserId"/>										
		<html:hidden property="questionListingMode"/>												
	
				<table class="forms">
				  <tr>
				  	<th scope="col" valign=top colspan=2> 
					  	  <bean:message key="label.assessment"/> 
				  	</th>
				  </tr>
			
		 			<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 		
						  <tr>
						  	<td NOWRAP align=center valign=top colspan=2> 
							  	  <b> <bean:message key="label.withRetries.results.summary"/> </b>
						  	</td>
						  </tr>
  					</c:if> 			

					<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
					  <tr>
					  	<td NOWRAP align=center valign=top colspan=2> 
						  	<b> <bean:message key="label.withoutRetries.results.summary"/>  </b>
					  	</td>
					  </tr>
					</c:if> 			


				  <tr>
				  	<td NOWRAP align=left valign=top colspan=2> 
					  	  <c:out value="${mcGeneralLearnerFlowDTO.countSessionComplete}"/> 
					  	  <bean:message key="label.learnersFinished.simple"/> 
				  	</td>
				  </tr>	


					  <tr>
					  	<td NOWRAP align=left valign=top> 
						  	  <b> <bean:message key="label.topMark"/> </b>
						 </td> 
						 <td NOWRAP align=left>	
							  	 <c:out value="${mcGeneralLearnerFlowDTO.topMark}"/>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=left valign=top> 
						  	<b> <bean:message key="label.avMark"/> </b>
					  	</td>
					  	<td NOWRAP align=left>
							  	<c:out value="${mcGeneralLearnerFlowDTO.averageMark}"/>
					  	</td>
					  </tr>	

					  <tr>
					  	<td NOWRAP align=left valign=top> 
							  <b> <bean:message key="label.loMark"/> </b>
					  	</td>
					  	<td NOWRAP align=left>
							  	<c:out value="${mcGeneralLearnerFlowDTO.lowestMark}"/>
					  	</td>
					  </tr>	
					  


		 		<c:if test="${mcGeneralLearnerFlowDTO.retries == 'true'}"> 					  	   
	  	   		  <tr>
				  	<td NOWRAP colspan=2  valign=top> 
					  			<html:submit property="redoQuestions" styleClass="button">
									<bean:message key="label.redo.questions"/>
								</html:submit>	 		
				  	 </td>
				  </tr>
				</c:if> 																		

				<c:if test="${mcGeneralLearnerFlowDTO.retries != 'true'}"> 							  
	  	   		  <tr>
	  	   		    <td NOWRAP colspan=2 valign=top>
	  						<div class="right-buttons">	  	   		    
						  	   <html:submit property="learnerFinished" styleClass="button">
											<bean:message key="label.finished"/>
							   </html:submit>
					  	   </div>						   
				  	 </td>
				  </tr>
				</c:if> 																		
			</table>
</html:form>
</div>

<div id="footer-learner"></div>

</div>
</body>
</html:html>









	
	