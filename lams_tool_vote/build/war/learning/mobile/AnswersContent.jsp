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
        

<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams">
	<lams:LAMSURL />
</c:set>
<c:set scope="request" var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<html:base />
	<title><fmt:message key="activity.title" />	</title>

	<link rel="stylesheet" href="${lams}css/jquery.mobile.css" />
	<link rel="stylesheet" href="${lams}css/defaultHTML_learner_mobile.css" />
	<script src="${lams}includes/javascript/jquery.js"></script>
	<script src="${lams}includes/javascript/jquery.mobile.js"></script>	
	<script language="JavaScript" type="text/JavaScript">

	var noSelected = 0;
	var maxVotes = <c:out value="${VoteLearningForm.maxNominationCount}"/>; 
	var minVotes = <c:out value="${VoteLearningForm.minNominationCount}"/>; 
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
		
		if((noSelected + userEntry) > maxVotes){
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

		var numberOfVotes = noSelected + userEntry;
		if(numberOfVotes > maxVotes) {
			alertTooManyVotes(maxVotes);
			return false;
		} else if(numberOfVotes < minVotes) {
			var msg = "<fmt:message key="error.minNominationCount.not.reached"/> " + minVotes + " <fmt:message key="label.nominations"/>";
			alert(msg);
			return false;			
		} else if(numberOfVotes == 0) {
			alert("<fmt:message key="error.empty.selection"/>");
			return false;
		} else {
			return true;
		}
	}

	function alertTooManyVotes(maxVotes) {
		var msg = "<fmt:message key="error.maxNominationCount.reached"/> "+maxVotes+" <fmt:message key="label.nominations"/>";
		alert(msg);
	}
	
	function submitMethod() {
		if (validate()) {
			document.VoteLearningForm.submit();	
		}
	}

	function checkLeaderProgress() {
		
        $.ajax({
        	async: false,
            url: '<c:url value="/learning.do"/>',
            data: 'dispatch=checkLeaderProgress&toolSessionID=' + $('[name="toolSessionID"]').val(),
            dataType: 'json',
            type: 'post',
            success: function (json) {
            	if (json.isLeaderResponseFinalized) {
            		location.reload();
            	}
            }
       	});
	}
	
	$(document).ready(function(){
		
		var mode = "${voteGeneralLearnerFlowDTO.learningMode}"; 
		var isUserLeader = ($('[name="userLeader"]').val() === "true");
		var isLeadershipEnabled = ($('[name="useSelectLeaderToolOuput"]').val() === "true");
		var hasEditRight = !isLeadershipEnabled || isLeadershipEnabled && isUserLeader;
		
		if (!hasEditRight && (mode != "teacher")) {
			setInterval("checkLeaderProgress();",60000);// Auto-Refresh every 1 minute for non-leaders
		}
		
		if (!hasEditRight) {
			$('[name="userEntry"]').prop('disabled', true);
			$('[name="checkedVotes"]').prop('disabled', true);
			$('#continue-options-combined-button').hide();
		}
	});	
	</script>
</lams:head>

<body>
<div data-role="page" data-cache="false">

	<div data-role="header" data-theme="b" data-nobackbtn="true">
		<h1>
			<c:out value="${voteGeneralLearnerFlowDTO.activityTitle}" escapeXml="true" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">

		<html:form action="/learning?validate=false&dispatch=continueOptionsCombined" method="POST" target="_self">
			<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
			<html:hidden property="toolSessionID" />
			<html:hidden property="userID" />
			<html:hidden property="revisitingUser" />
			<html:hidden property="previewOnly" />
			<html:hidden property="maxNominationCount" />
			<html:hidden property="minNominationCount" />
			<html:hidden property="allowTextEntry" />
			<html:hidden property="lockOnFinish" />
			<html:hidden property="reportViewOnly" />
			<html:hidden property="showResults" />
			<html:hidden property="userLeader" />
			<html:hidden property="groupLeaderName" />
			<html:hidden property="useSelectLeaderToolOuput" />
			
			<c:if test="${formBean.useSelectLeaderToolOuput}">
				<h4>
					<fmt:message key="label.group.leader" >
						<fmt:param>${formBean.groupLeaderName}</fmt:param>
					</fmt:message>
				</h4>
			</c:if>

			<c:if test="${not empty voteGeneralLearnerFlowDTO.submissionDeadline}">
				<div class="info">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${voteGeneralLearnerFlowDTO.submissionDeadline}" /></fmt:param>
					</fmt:message>	
				</div>
			</c:if>

			<c:if test="${voteGeneralLearnerFlowDTO.maxNominationCountReached == 'true'}">
				<div class="warning">
					<fmt:message key="error.maxNominationCount.reached" />
					<c:out value="${voteGeneralLearnerFlowDTO.maxNominationCount}" />
					<fmt:message key="label.nominations" />
				</div>
			</c:if>

			<p>
				<c:out value="${voteGeneralLearnerFlowDTO.activityInstructions}" escapeXml="false" />
			</p>

			<c:if test="${voteGeneralLearnerFlowDTO.maxNominationCount > 1}">
				<p>&nbsp;</p>
				<p>
					<fmt:message key="label.nominations.available">
						<fmt:param>
							<c:out value="${voteGeneralLearnerFlowDTO.maxNominationCount}" />
						</fmt:param>
					</fmt:message>
				</p>
			</c:if>

			<ul data-role="listview" data-inset="true">
				<table class="alternative-color" style="margin-top: 20px; margin-bottom: 10px;">
	
					<c:forEach var="subEntry" varStatus="status" items="${requestScope.mapQuestionContentLearner}">
	
						<tr>
							<td width="1px" style="font-size: 13px;">
								<input type="checkbox" name="checkedVotes" class="noBorder"
									value="${subEntry.key}" onClick="updateCount(this);">
							</td>
	
							<td>
								<c:out value="${subEntry.value}" escapeXml="false" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</ul>

			<c:if test="${VoteLearningForm.allowTextEntry == 'true'}">
				<strong> <fmt:message key="label.other" />: </strong>
				<html:text property="userEntry" size="30" maxlength="100" />
			</c:if>

			<html:hidden property="donePreview" />

			<div class="space-top button-inside" id="continue-options-combined-button">
				<button onclick="javascript:submitMethod();return false;" name="continueOptionsCombined" class="button" data-icon="arrow-r" data-theme="b">
					<fmt:message key="label.submit.vote" />
				</button>
			</div>

		</html:form>
	</div>
	
	<div data-role="footer" data-theme="b">
		<h2>&nbsp;</h2>
	</div><!-- /footer -->

</div>
</body>
</lams:html>
