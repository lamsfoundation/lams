<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript">
	var MODE = "${MODE}", TOOL_SESSION_ID = '${param.toolSessionID}', APP_URL = '<lams:WebAppURL />', LEARNING_ACTION = "<c:url value='/learning.do'/>";
</script>
<script type="text/javascript" src="includes/javascript/learning.js"></script>


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
		<div class="col-xs-12 col-sm-9 col-md-10 col-lg-10">
			<div id="messages"></div>
		</div>
		<div class="col-xs-12 col-sm-3 col-md-2 col-lg-2">
			<div id="roster" class="hidden-xs"></div>
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
			<div class="col-xs-12 col-sm-9 col-md-10 col-lg-10">
				<div id="textArea" class="voffset5">
					<textarea id="sendMessageArea" rows="2" class="form-control"></textarea>
				</div>
			</div>
			<div class="col-xs-12 col-sm-3 col-md-2 col-lg-2 ">
				<div id="sendMessageButtonCell" class="voffset5">
					<input id="sendMessageButton" class="btn btn-sm btn-default" type="button" onclick="javascript:sendMessage()"
						value='<fmt:message key="button.send"/>' />
				</div>
			</div>
		</div>
	</c:if>

	<c:if test="${MODE == 'learner' || MODE == 'author'}">
		<%@ include file="parts/finishButton.jsp"%>
	</c:if>
</lams:Page>
