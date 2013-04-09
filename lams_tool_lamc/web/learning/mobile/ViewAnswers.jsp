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

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" /></title>
	
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script type="text/javascript">
		function disableFinishButton() {
			var elem = document.getElementById("finishButton");
			if (elem != null) {
				elem.disabled = true;
			}
		}
	         function submitForm(methodName){
        	        var f = document.getElementById('Form1');
	                f.submit();
	        }
	</script>
</lams:head>

<body class="large-font">
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${mcGeneralLearnerFlowDTO.activityTitle}"	escapeXml="false" />
		</h1>
	</div>

	<html:form action="/learning?method=displayMc&validate=false"
		method="POST" target="_self" onsubmit="disableFinishButton();"  styleId="Form1">
		<div data-role="content">	
			<html:hidden property="toolContentID" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="httpSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="userOverPassMark" />
			<html:hidden property="passMarkApplicable" />
			<html:hidden property="learnerProgress" />
			<html:hidden property="learnerProgressUserId" />
			<html:hidden property="questionListingMode" />


			<h2>
				<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}">
					<fmt:message key="label.viewAnswers" />
				</c:if>

				<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress == 'true'}">
					<fmt:message key="label.learner.viewAnswers" />
				</c:if>
			</h2>

			<!--  QUESTIONS  -->
			<ul data-role="listview" data-inset="true" data-theme="d" >
			<c:set var="mainQueIndex" scope="request" value="0" />
			<c:forEach var="questionEntry"
				items="${mcGeneralLearnerFlowDTO.mapQuestionsContent}">
				<c:set var="mainQueIndex" scope="request" value="${mainQueIndex +1}" />

				<li>
					<div class="regular-li">
					<div style="overflow: auto;">									  	
						<c:out value="${questionEntry.value}" escapeXml="false" />
					</div>																			  								


					<!--  CANDIDATE ANSWERS  -->
					<c:set var="queIndex" scope="request" value="0" />
					<c:forEach var="mainEntry"
						items="${mcGeneralLearnerFlowDTO.mapGeneralOptionsContent}">
						<c:set var="queIndex" scope="request" value="${queIndex +1}" />

						<c:if test="${requestScope.mainQueIndex == requestScope.queIndex}">
							<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
								<ul>
									<c:forEach var="subEntry" items="${mainEntry.value}">
										<li>
											<c:out value="${subEntry.value}" escapeXml="false" /> 
										</li>
									</c:forEach>
								</ul>
							</c:if>
							
							<p>
								<c:forEach var="attemptEntryFinal"
									items="${mcGeneralLearnerFlowDTO.mapFinalAnswersContent}">
									<c:if test="${requestScope.mainQueIndex == attemptEntryFinal.key}">
										<c:out value="${attemptEntryFinal.value}" />
									</c:if>
								</c:forEach>
	
								<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
									<c:forEach var="attemptEntryCorrect"
										items="${mcGeneralLearnerFlowDTO.mapFinalAnswersIsContent}">
										<c:if test="${requestScope.mainQueIndex == attemptEntryCorrect.key}">
											<c:if test="${attemptEntryCorrect.value == 'true'}">
												<img src="<c:out value="${tool}"/>images/tick.gif" border="0" class="middle">									
											</c:if>
											<c:if test="${attemptEntryCorrect.value == 'false'}">
												<img src="<c:out value="${tool}"/>images/cross.gif" border="0" class="middle">
											</c:if>									
										</c:if>
									</c:forEach>
								</c:if>
							</p>
						</c:if>
					</c:forEach>

					<c:if test="${mcGeneralLearnerFlowDTO.displayAnswers == 'true'}">
						<c:forEach var="feedbackEntry"
							items="${mcGeneralLearnerFlowDTO.mapFeedbackContent}">
							<c:if test="${requestScope.mainQueIndex == feedbackEntry.key}">
								<c:if test="${(feedbackEntry.value != null) && (feedbackEntry.value != '')}">
									<div style="overflow: auto;">									  							
										<strong> <fmt:message key="label.feedback.simple" /> </strong> <c:out value="${feedbackEntry.value}" escapeXml="false" />
									</div>								
								</c:if>																			
							</c:if>
						</c:forEach>
					</c:if>
				</div>
				</li>
			</c:forEach>
			</ul>
			<!-- END QUESTION  -->


			<c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">
					<h4>
						<fmt:message key="label.group.results" />
					</h4>
					
					<table class="alternative-color" cellspacing="0">
					  <tr>
					  	<td width="30%"> 
						  	  <strong> <fmt:message key="label.topMark"/> </strong>
						 </td> 
						 <td>	
							  	 <c:out value="${mcGeneralLearnerFlowDTO.topMark}"/>
					  	</td>
					  </tr>	
		
					  <tr>
					  	<td> 
						  	 <strong><fmt:message key="label.avMark"/> </strong>
					  	</td>
					  	<td>
							  	<c:out value="${mcGeneralLearnerFlowDTO.averageMark}"/>
					  	</td>
					  </tr>	
					</table>
				</c:if>				

			<c:if test="${(mcGeneralLearnerFlowDTO.learnerProgress != 'true') && (mcGeneralLearnerFlowDTO.retries == 'true')}">
				<div class="small-space-top button-inside" >
					<button type="submit" name="redoQuestions" data-icon="back">
						<fmt:message key="label.redo.questions" />
					</button>
				</div>
			</c:if>
									
			<c:if test="${mcGeneralLearnerFlowDTO.reflection}">
				<h2>
							<c:out value="${mcGeneralLearnerFlowDTO.reflectionSubject}" escapeXml="false" />											
				</h2>

				<p>
					<c:choose>
						<c:when test="${not empty mcGeneralLearnerFlowDTO.notebookEntry}">
							<c:out value="${mcGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false"/>
						</c:when>
						<c:otherwise>
							<em><fmt:message key="message.no.reflection.available" /></em>  
						</c:otherwise>
					</c:choose>
				</p>
				
			</c:if>
		</div>

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
			<c:if test="${mcGeneralLearnerFlowDTO.learnerProgress != 'true'}">
				<c:if test="${(mcGeneralLearnerFlowDTO.retries != 'true') || (mcGeneralLearnerFlowDTO.retries == 'true') && (mcGeneralLearnerFlowDTO.passMarkApplicable == 'true') && (mcGeneralLearnerFlowDTO.userOverPassMark == 'true')}">

					<c:if test="${mcGeneralLearnerFlowDTO.reflection != 'true'}">
						<html:hidden property="learnerFinished" value="Finished" />
									
						<button id="finishButton" name="finishButton" onclick="submitForm('finish');return false" data-icon="arrow-r" data-theme="b">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finished" />
				 					</c:otherwise>
					 			</c:choose>
							</span>
						</button>
					</c:if>

					<c:if test="${mcGeneralLearnerFlowDTO.reflection == 'true'}">
						<button name="forwardtoReflection" type="submit" data-icon="arrow-r" data-theme="b">
							<fmt:message key="label.continue" />
						</button>
					</c:if>
				</c:if>
			</c:if>

			<html:hidden property="doneLearnerProgress" />
		</span>
	</div>			
	</html:form>

	</div>
</body>
</lams:html>
