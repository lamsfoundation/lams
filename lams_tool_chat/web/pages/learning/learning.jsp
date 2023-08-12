<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>css/defaultHTML_learner.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript">
		var MODE = "${MODE}", TOOL_SESSION_ID = '<c:out value="${param.toolSessionID}" />', APP_URL = '<lams:WebAppURL />', LEARNING_ACTION = "<c:url value='learning/learning.do'/>", LAMS_URL = '<lams:LAMSURL/>';
		</script>
		<lams:JSImport src="includes/javascript/portrait.js" />
		<lams:JSImport src="includes/javascript/learning.js" relative="true" />
			
	
	</lams:head>

	<body class="stripes">
			
		<lams:Page type="learner" title="${chatDTO.title}">
			<div class="panel">
				<c:out value="${chatDTO.instructions}" escapeXml="false" />
			</div>
		
			<c:if test="${MODE == 'learner' || MODE == 'author'}">
		
				<!-- Announcements and advanced settings -->
				<c:if test="${chatDTO.lockOnFinish}">
					<lams:Alert id="lockWhenFinished" type="info" close="true">
						<c:choose>
							<c:when test="${chatUserDTO.finishedActivity}">
								<fmt:message key="message.activityLocked" />
							</c:when>
							<c:otherwise>
								<fmt:message key="message.warnLockOnFinish" />
							</c:otherwise>
						</c:choose>
					</lams:Alert>
				</c:if>
		
				<c:if test="${not empty submissionDeadline}">
		
					<lams:Alert id="submissionDeadline" type="info" close="true">
						<fmt:message key="authoring.info.teacher.set.restriction">
							<fmt:param>
								<lams:Date value="${submissionDeadline}" />
							</fmt:param>
						</fmt:message>
					</lams:Alert>
				</c:if>
			</c:if>
			<!-- End announcements and advanced settings -->
		
			<!-- chat UI -->
			<div class="row">
				<div class="col-xs-12 col-sm-9 col-md-9 col-lg-8">
					<div id="messages"></div>
				</div>
				<div class="col-xs-12 col-sm-3 col-md-3 col-lg-4">
					<div id="roster" class="d-none d-sm-block"></div>
				</div>
			</div>
			<c:if test="${MODE == 'teacher'}">
				<div class="row">
					<div class="col-xs-12 voffset5">
						<div id="sentTo">
							<fmt:message key="label.sendMessageTo" />
							&nbsp;<span id="sendToEveryone"><fmt:message key="label.everyone" /></span> <span id="sendToUser"
								style="display: none"></span>
						</div>
					</div>
				</div>
			</c:if>
		
			<c:if test="${MODE != 'learner' || !chatDTO.lockOnFinish || !chatUserDTO.finishedActivity}">
				<div class="row">
					<div class="col-xs-12 col-sm-9 col-md-9 col-lg-8">
						<div id="textArea" class="voffset5">
							<textarea id="sendMessageArea" rows="2" class="form-control" autofocus></textarea>
						</div>
					</div>
					<div class="col-xs-12 col-sm-3 col-md-3 col-lg-4">
						<div id="sendMessageButtonCell" class="voffset5">
							<input id="sendMessageButton" class="btn btn-autoresize btn-default" type="button" onclick="javascript:sendChatToolMessage()"
								value='<fmt:message key="button.send"/>' />
						</div>
					</div>
				</div>
			</c:if>
		
			<c:if test="${MODE == 'learner' || MODE == 'author'}">
				<%@ include file="parts/finishButton.jsp"%>
			</c:if>
		</lams:Page>

			
	</body>
</lams:html>