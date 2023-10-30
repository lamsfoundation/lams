<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"> <lams:LAMSURL /> </c:set>
<c:set var="tool"> <lams:WebAppURL /> </c:set>

<lams:PageLearner title="${chatDTO.title}" toolSessionID="${param.toolSessionID}" >
	<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '<c:out value="${param.toolSessionID}" />', '', function(){
			 submitForm('finishActivity');
		});
		
		function disableFinishButton() {
			var finishButton = document.getElementById("finishButton");
			if (finishButton != null) {
				finishButton.disabled = true;
			}
		}
	    function submitForm(metodName){
	        var f = document.getElementById("learningForm");
	        f.submit();
	    }

    	var MODE = "${MODE}", 
			TOOL_SESSION_ID = '<c:out value="${param.toolSessionID}" />', 
			APP_URL = '<lams:WebAppURL />', 
			LEARNING_ACTION = "<c:url value='learning/learning.do'/>";
	</script>
	<lams:JSImport src="includes/javascript/portrait5.js" />
	<lams:JSImport src="includes/javascript/learning.js" relative="true" />
	
	<div id="container-main">
		<div id="instructions" class="instructions">
			<c:out value="${chatDTO.instructions}" escapeXml="false" />
		</div>
		
		<!-- Announcements and advanced settings -->
		<c:if test="${chatDTO.lockOnFinish}">
			<lams:Alert5 id="lockWhenFinished" type="info" close="true">
				<c:choose>
					<c:when test="${chatUserDTO.finishedActivity}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>
		
		<c:if test="${not empty submissionDeadline}">
			<lams:Alert5 id="submissionDeadline" type="info" close="true">
				<fmt:message key="authoring.info.teacher.set.restriction">
					<fmt:param>
						<lams:Date value="${submissionDeadline}" />
					</fmt:param>
				</fmt:message>
			</lams:Alert5>
		</c:if>
		
		<!-- chat UI -->
		<div class="row">
			<div class="col-12 col-sm-3 col-md-3 col-lg-3">
				<div id="roster" class="d-none d-sm-block div-hover shadow rounded ${MODE == 'teacher' ? 'mode-teacher' : ''}"></div>
			</div>
			<div class="col-12 col-sm-9 col-md-9 col-lg-9">
				<div id="messages" class="shadow rounded p-3" aria-live="polite"></div>
				
				<c:if test="${MODE != 'learner' || !chatDTO.lockOnFinish || !chatUserDTO.finishedActivity}">
					<div class="d-flex align-items-center flex-row mt-2">
						<div id="textArea" class="flex-fill">
							<label for="sendMessageArea" class="visually-hidden">
								<fmt:message key="button.send"/>
							</label>
							<textarea id="sendMessageArea" rows="2" class="form-control shadow rounded" autofocus></textarea>
						</div>
						<div id="sendMessageButtonCell" class="ms-2">
							<button id="sendMessageButton" class="btn btn-secondary" type="button" onclick="javascript:sendChatToolMessage()">
								<fmt:message key="button.send"/>
								<i class="fa-regular fa-paper-plane ms-1"></i>
							</button>
						</div>
					</div>
				</c:if>
			</div>
		</div>
		
		<c:if test="${MODE == 'teacher'}">
			<div class="row">
				<div class="col-12 mt-2">
					<div id="sentTo" class="badge bg-info">
						<fmt:message key="label.sendMessageTo" />
						&nbsp;
						<span id="sendToEveryone">
							<fmt:message key="label.everyone" />
						</span> 
						<span id="sendToUser" style="display: none"></span>
					</div>
				</div>
			</div>
		</c:if>
		
		<c:if test="${MODE == 'learner' || MODE == 'author'}">
			<form action="openNotebook.do" method="post">
				<input type="hidden" name="chatUserUID" value="${chatUserDTO.uid}" />
			
				<c:if test="${chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
					<br class="mt-5">
					<lams:NotebookReedit
						reflectInstructions="${chatDTO.reflectInstructions}"
						reflectEntry="${chatUserDTO.notebookEntry}"
						isEditButtonEnabled="true"
						notebookHeaderLabelKey="button.reflect"
						editNotebookLabelKey="button.edit"/>
				</c:if>
			</form>

			<div class="activity-bottom-buttons" id="learner-submit">
				<c:choose>
					<c:when test="${!chatUserDTO.finishedActivity and chatDTO.reflectOnActivity}">
						<form:form action="openNotebook.do" method="post"
								onsubmit="disableFinishButton();" modelAttribute="learningForm" id="learningForm">
							<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />
							<button type="submit" class="btn btn-primary na mt-2">
								<fmt:message key="button.continue" />
							</button>
						</form:form>
					</c:when>

					<c:otherwise>
						<form:form action="finishActivity.do" method="post"
								onsubmit="disableFinishButton();"  modelAttribute="learningForm" id="learningForm">
							<form:hidden path="chatUserUID" value="${chatUserDTO.uid}" />
							<button type="button" class="btn btn-primary mt-2 na" id="finishButton">
								 <c:choose>
								 	<c:when test="${isLastActivity}">
								 		 <fmt:message key="button.submit" />
								 	</c:when>
								 	<c:otherwise>
								 		 <fmt:message key="button.finish" />
								 	</c:otherwise>
								 </c:choose>
							</button>
						</form:form>
					</c:otherwise>
				</c:choose>
			</div>

		</c:if>
	</div>
</lams:PageLearner>