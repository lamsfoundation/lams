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

	var noSelected = 0;
	var maxVotes = <c:out value="${VoteLearningForm.maxNominationCount}"/>; 
	function updateCount(clickedObj){
		var userEntry = 0;
		<c:if test="${VoteLearningForm.allowTextEntry == true}">	
			if(document.forms[0].userEntry.value != ""){
				userEntry = 1;
			}
		</c:if>
		if(clickedObj.checked){
			noSelected++;
		}else{
			noSelected--;
		}
		
		if((maxVotes != -1) && (noSelected + userEntry) > maxVotes){
			clickedObj.checked = false;
			noSelected--;
			alertTooManyVotes(maxVotes);
		}
	
	}

	function validate(){
		var error = "";
		var userEntry = 0;
		<c:if test="${VoteLearningForm.allowTextEntry == true}">	
			if(document.forms[0].userEntry.value != ""){
				userEntry = 1;
			}
		</c:if>

		if((maxVotes != -1) && (noSelected + userEntry) > maxVotes){
			alertTooManyVotes(maxVotes);
			return false;
		} else {
			return true;
		}
		 
	}

	function alertTooManyVotes(maxVotes) {
		var msg = "<bean:message key="error.maxNominationCount.reached"/> "+maxVotes+" <bean:message key="label.nominations"/>";
		alert(msg);
	}
	</script>
	
	<script language="JavaScript" type="text/JavaScript">
		function submitMethod(actionMethod) 
		{
			document.VoteLearningForm.dispatch.value=actionMethod; 
			document.VoteLearningForm.submit();
		}
	</script>
</head>

<body>
	<div id="page-learner">

<h1 class="no-tabs-below">
	<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="false" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">
	<html:form  onsubmit="return validate();" action="/learning?validate=false&dispatch=continueOptionsCombined" method="POST" target="_self">
	<html:hidden property="dispatch"/>
	<html:hidden property="toolSessionID"/>
	<html:hidden property="userID"/>	
	<html:hidden property="revisitingUser"/>		
	<html:hidden property="previewOnly"/>		
	<html:hidden property="maxNominationCount"/>		
	<html:hidden property="allowTextEntry"/>		
	<html:hidden property="voteChangable"/>	
	<html:hidden property="lockOnFinish"/>	
	
	
			<table>
					<c:if test="${voteGeneralLearnerFlowDTO.activityRunOffline == 'true'}"> 			
						<tr> <td class="error">
							<bean:message key="label.learning.forceOfflineMessage"/>
						</td></tr>
					</c:if> 		
					
					<c:if test="${voteGeneralLearnerFlowDTO.maxNominationCountReached == 'true'}"> 			
						<tr> <td class="error">
							<bean:message key="error.maxNominationCount.reached"/> 
								<c:out value="${voteGeneralLearnerFlowDTO.maxNominationCount}"/>	
							<bean:message key="label.nominations"/>
						</td></tr>
					</c:if> 		
					
	
					<c:if test="${voteGeneralLearnerFlowDTO.activityRunOffline != 'true'}"> 			
						  <tr>
						  	<td  NOWRAP align=left valign=top colspan=2> 
								  <c:out value="${voteGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />						  																
						  	</td>
						  </tr>
	

							  <tr>						 
								<td NOWRAP align=left>
								<table align=left>
										<c:forEach var="subEntry" varStatus="status" items="${requestScope.mapQuestionContentLearner}">
											<input type="checkbox" name="checkedVotes" value="${subEntry.key}" onClick="updateCount(this);"><c:out value="${subEntry.value}" escapeXml="false"/><BR>
										</c:forEach>
								</table>
								</td>
							</tr>
							  
							<c:if test="${VoteLearningForm.allowTextEntry == 'true'}"> 			
									<tr> 
										<td NOWRAP align=left valign=top colspan=2> 
											<table align=left> 
												<tr> <td>
								      			 <b>
											 		<bean:message key="label.other"/>: 
										      		</b> 
										 			<html:text property="userEntry" size="30" maxlength="100"/>
											 	</td> </tr>
											</table>									 			
								 		</td>
								  	</tr>
							</c:if> 									  	
				  	   
				  	<html:hidden property="donePreview"/>						   
		  	   		
		  	   		  <tr>
					  	<td NOWRAP valign=top colspan=2> 
		                            <html:submit property="continueOptionsCombined" 
		                                         styleClass="button">
										<bean:message key="label.submit.vote"/>
		                            </html:submit>
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

