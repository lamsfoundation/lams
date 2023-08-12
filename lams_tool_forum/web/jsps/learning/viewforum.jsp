<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="UPLOAD_FILE_MAX_SIZE"><%=Configuration.get(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE)%></c:set>
<c:set var="EXE_FILE_TYPES"><%=Configuration.get(ConfigurationKeys.EXE_EXTENSIONS)%></c:set>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="tool"><lams:WebAppURL /></c:set>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"	scope="request" />
<c:set var="newtopic">
	<lams:WebAppURL />learning/newTopic.do?sessionMapID=${sessionMapID}
</c:set>
<c:set var="continue">
	<lams:WebAppURL />learning/newReflection.do?sessionMapID=${sessionMapID}
</c:set>
<c:set var="finish">
	<lams:WebAppURL />learning/finish.do?sessionMapID=${sessionMapID}
</c:set>
<c:set var="refresh">
	<lams:WebAppURL />learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}&hideReflection=${sessionMap.hideReflection}
</c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>

		<!-- ********************  CSS ********************** -->

		<lams:css />
		<style media="screen,projection" type="text/css">
			.info {
				margin: 10px;
			}
		</style>
		
		<!-- ********************  javascript ********************** -->
		<lams:JSImport src="includes/javascript/common.js" />
		<script type="text/javascript" src="${lams}includes/javascript/tabcontroller.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jRating.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.treetable.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>		
		<script type="text/javascript" src="${lams}includes/javascript/jquery.jscroll.js"></script>
		<lams:JSImport src="includes/javascript/upload.js" />
		<lams:JSImport src="learning/includes/javascript/gate-check.js" />
		
		<script type="text/javascript">
			var removeItemAttachmentUrl = "<lams:WebAppURL />learning/deleteAttachment.do";
			var warning = '<fmt:message key="warn.minimum.number.characters" />';
			var LABEL_MAX_FILE_SIZE = '<fmt:message key="errors.maxfilesize"><param>{0}</param></fmt:message>';
			var LABEL_NOT_ALLOWED_FORMAT = '<fmt:message key="error.attachment.executable"/>';	
			var EXE_FILE_TYPES = '${EXE_FILE_TYPES}';
			var UPLOAD_FILE_MAX_SIZE = '${UPLOAD_FILE_MAX_SIZE}';

			checkNextGateActivity('finishButton', '${sessionMap.toolSessionID}', '', submitFinish);

			function submitFinish() {
				location.href = '${finish}';
			}
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/learner.js"></script>	
		
	</lams:head>
	
	<body class="stripes">

		<lams:Page type="learner" title="${sessionMap.title}">
		
			<div class="panel">
				<c:out value="${sessionMap.instruction}" escapeXml="false" />
			</div>
		
			<!-- Announcements and advanced settings -->
			<c:if test="${not empty sessionMap.submissionDeadline}">
				<lams:Alert id="submissionDeadline" type="info" close="true">
					<fmt:message key="authoring.info.teacher.set.restriction" >
						<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
					</fmt:message>
				</lams:Alert>
			</c:if>
		
			<c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
				<c:if test="${sessionMap.lockedWhenFinished}">
					<lams:Alert id="lockWhenFinished" type="info" close="true">
						<c:choose>
						<c:when test="${sessionMap.finishedLock}">
							<fmt:message key="label.responses.locked.reminder" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.responses.locked" />
						</c:otherwise>
						</c:choose>
					</lams:Alert>
				</c:if>
		
				<c:if test="${not sessionMap.allowNewTopics and ( sessionMap.minimumReply ne 0 or sessionMap.maximumReply ne 0)}">
					<lams:Alert id="postingLimits" type="info" close="true">
						<c:if test="${(sessionMap.minimumReply ne 0 and sessionMap.maximumReply ne 0)}">
								<fmt:message key="label.postingLimits.forum.reminder">
									<fmt:param value="${sessionMap.minimumReply}"/>
									<fmt:param value="${sessionMap.maximumReply}"/>
								</fmt:message>
						</c:if>
						<c:if test="${(sessionMap.minimumReply ne 0 and sessionMap.maximumReply eq 0)}">
							<fmt:message key="label.postingLimits.forum.reminder.min">
								<fmt:param value="${sessionMap.minimumReply}"/>
							</fmt:message>
						</c:if>			
						<c:if test="${(sessionMap.minimumReply eq 0 and sessionMap.maximumReply ne 0)}">
							<fmt:message key="label.postingLimits.forum.reminder.max">
								<fmt:param value="${sessionMap.maximumReply}"/>
							</fmt:message>
						</c:if>
					</lams:Alert>
				</c:if>
				
				<!-- Rating announcements -->
				<c:if test="${sessionMap.allowRateMessages}">
					<lams:Alert id="rateMessages" type="info" close="true">
						<c:choose>
							<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate ne 0}">
								<fmt:message key="label.rateLimits.forum.reminder">
									<fmt:param value="${sessionMap.minimumRate}"/>
									<fmt:param value="${sessionMap.maximumRate}"/>
								</fmt:message>						
								<br>
							</c:when>
							
							<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate eq 0}">
								<fmt:message key="label.rateLimits.forum.reminder.min">
									<fmt:param value="${sessionMap.minimumRate}"/>
								</fmt:message>					
								<br>
							</c:when>
							
							<c:when test="${sessionMap.minimumRate eq 0 and sessionMap.maximumRate ne 0}">
								<fmt:message key="label.rateLimits.forum.reminder.max">
									<fmt:param value="${sessionMap.maximumRate}"/>
								</fmt:message>					
								<br>
							</c:when>				
						</c:choose>
								
						<fmt:message key="label.rateLimits.topic.reminder">
							<fmt:param value="<span id='numOfRatings'>${sessionMap.numOfRatings}</span>"/>
						</fmt:message>
					</lams:Alert>
				</c:if>	
			</c:if>
			<!-- End announcements and advanced settings -->
		
			<lams:errors/>
		
			<!-- main UI -->
			<%@ include file="/jsps/learning/message/topiclist.jsp"%>
			<!--  end main UI -->
		
			<!-- Reflection -->
			<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.hideReflection}">
				<div class="panel panel-default">
					<div class="panel-heading panel-title">
						<fmt:message key="title.reflection" />
					</div>
					<div class="panel-body">
						<div class="reflectionInstructions">
							<lams:out value="${sessionMap.reflectInstructions}" escapeHtml="true" />
						</div>
						<div class="panel">
							<c:choose>
								<c:when test="${empty sessionMap.reflectEntry}">
									<p>
										<em> <fmt:message key="message.no.reflection.available" /> </em>
									</p>
								</c:when>
								<c:otherwise>
									<p>
										<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
									</p>
								</c:otherwise>
							</c:choose>
						</div>
						<c:if test='${sessionMap.mode != "teacher"}'>
							<button name="continue"
								onclick="javascript:location.href='${continue}';"
								class="btn btn-default float-start" id="editReflection">
								<fmt:message key="label.edit" />
							</button>
						</c:if>
					</div>
				</div>
			</c:if>
			<!-- End Reflection -->
		
			<c:if test='${(sessionMap.mode != "teacher") && sessionMap.isMinRatingsCompleted}'>
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
						<button name="continue"
							onclick="javascript:location.href='${continue}';"
							class="btn btn-primary voffset5 float-end">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
		
					<c:otherwise>
						<a href="#nogo" name="finish" id="finishButton" class="btn btn-primary voffset5 float-end na">
							<span class="nextActivity">
								<c:choose>
				 					<c:when test="${sessionMap.isLastActivity}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finish" />
				 					</c:otherwise>
				 				</c:choose>
							</span>
						</a>
					</c:otherwise>
		
				</c:choose>
			</c:if>
		
		</lams:Page>

	</body>
</lams:html>