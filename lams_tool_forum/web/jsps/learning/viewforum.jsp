<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"	scope="request" />
<c:set var="newtopic">
	<html:rewrite page="/learning/newTopic.do?sessionMapID=${sessionMapID}" />
</c:set>
<c:set var="continue">
	<html:rewrite page="/learning/newReflection.do?sessionMapID=${sessionMapID}" />
</c:set>
<c:set var="finish">
	<html:rewrite page="/learning/finish.do?sessionMapID=${sessionMapID}" />
</c:set>
<c:set var="refresh">
	<html:rewrite page="/learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}&hideReflection=${sessionMap.hideReflection}" />
</c:set>

<script type="text/javascript">
	function submitFinish() {
		document.getElementById("finishButton").disabled = true;
		location.href = '${finish}';
	}		
</script>

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
					</c:when>
					
					<c:when test="${sessionMap.minimumRate ne 0 and sessionMap.maximumRate eq 0}">
						<fmt:message key="label.rateLimits.forum.reminder.min">
							<fmt:param value="${sessionMap.minimumRate}"/>
						</fmt:message>					
					</c:when>
					
					<c:when test="${sessionMap.minimumRate eq 0 and sessionMap.maximumRate ne 0}">
						<fmt:message key="label.rateLimits.forum.reminder.max">
							<fmt:param value="${sessionMap.maximumRate}"/>
						</fmt:message>					
					</c:when>				
				</c:choose>
				<br>
						
				<fmt:message key="label.rateLimits.topic.reminder">
					<fmt:param value="<span id='numOfRatings'>${sessionMap.numOfRatings}</span>"/>
				</fmt:message>
			</lams:Alert>
		</c:if>	
	</c:if>
	<!-- End announcements and advanced settings -->

	<%@ include file="/common/messages.jsp"%>

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
					<html:button property="continue"
						onclick="javascript:location.href='${continue}';"
						styleClass="btn btn-default pull-left" styleId="editReflection">
						<fmt:message key="label.edit" />
					</html:button>
				</c:if>
			</div>
		</div>
	</c:if>
	<!-- End Reflection -->

	<c:if test='${(sessionMap.mode != "teacher") && sessionMap.isMinRatingsCompleted}'>
		<c:choose>
			<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
				<html:button property="continue"
					onclick="javascript:location.href='${continue}';"
					styleClass="btn btn-primary voffset5 pull-right">
					<fmt:message key="label.continue" />
				</html:button>
			</c:when>

			<c:otherwise>
				<html:link href="#nogo" property="finish" styleId="finishButton" onclick="submitFinish();" styleClass="btn btn-primary voffset5 pull-right na">
					<span class="nextActivity">
						<c:choose>
		 					<c:when test="${sessionMap.activityPosition.last}">
		 						<fmt:message key="label.submit" />
		 					</c:when>
		 					<c:otherwise>
		 		 				<fmt:message key="label.finish" />
		 					</c:otherwise>
		 				</c:choose>
					</span>
				</html:link>
			</c:otherwise>

		</c:choose>
	</c:if>

</lams:Page>
