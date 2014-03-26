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

<div id="content">

	<h1>
		<c:out value="${sessionMap.title}" escapeXml="true" />
	</h1>

	<div class="small-space-top">
		<c:out value="${sessionMap.instruction}" escapeXml="false" />
	</div>

	<c:if test="${not empty sessionMap.submissionDeadline}">
		<div class="info">
			<fmt:message key="authoring.info.teacher.set.restriction" >
				<fmt:param><lams:Date value="${sessionMap.submissionDeadline}" /></fmt:param>
			</fmt:message>
		</div>
	</c:if>
	<c:if test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
		<c:if test="${sessionMap.lockedWhenFinished}">
			<div class="info">
				<c:choose>
					<c:when test="${sessionMap.finishedLock}">
						<fmt:message key="label.responses.locked.reminder" />
					</c:when>
					<c:otherwise>
						<fmt:message key="label.responses.locked" />
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:if test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 and sessionMap.maximumReply ne 0)}">
			<div class="info">
				<fmt:message key="label.postingLimits.forum.reminder">
					<fmt:param value="${sessionMap.minimumReply}"/>
					<fmt:param value="${sessionMap.maximumReply}"/>
				</fmt:message>
			</div>
		</c:if>
		
		<c:if test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 and sessionMap.maximumReply eq 0)}">
			<div class="info">
				<fmt:message key="label.postingLimits.forum.reminder.min">
					<fmt:param value="${sessionMap.minimumReply}"/>
				</fmt:message>
			</div>
		</c:if>
		
		<c:if test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply eq 0 and sessionMap.maximumReply ne 0)}">
			<div class="info">
				<fmt:message key="label.postingLimits.forum.reminder.max">
					<fmt:param value="${sessionMap.maximumReply}"/>
				</fmt:message>
			</div>
		</c:if>
		
		<!-- Rating announcements -->
		<c:if test="${sessionMap.allowRateMessages}">
		
			<div class="info">
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
			</div>
			
		</c:if>	
		
	</c:if>

	<%@ include file="/common/messages.jsp"%>
	
	<div class="space-bottom-top">
		<c:set var="buttonClass" value="button" />
		<c:if test="${sessionMap.finishedLock}">
			<c:set var="buttonClass" value="disabled" />
		</c:if>

		<c:if test='${sessionMap.allowNewTopics}'>
			<html:button property="newtopic"
				onclick="javascript:location.href='${newtopic}';"
				disabled="${sessionMap.finishedLock}" styleClass="${buttonClass}">
				<fmt:message key="label.newtopic" />
			</html:button>
		</c:if>

		<html:button property="refresh" onclick="javascript:location.href='${refresh}';" styleClass="button space-left">
			<fmt:message key="label.refresh" />
		</html:button>
	</div>

	<%@ include file="/jsps/learning/message/topiclist.jsp"%>

	<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.hideReflection}">
		<div class="small-space-top">
			<h2>
				<c:out value="${sessionMap.reflectInstructions}" escapeXml="true"/>
			</h2>

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

			<c:if test='${sessionMap.mode != "teacher"}'>
				<html:button property="continue"
					onclick="javascript:location.href='${continue}';"
					styleClass="button">
					<fmt:message key="label.edit" />
				</html:button>
			</c:if>
		</div>
	</c:if>

	<div align="right" class="space-bottom-top">
		<c:if test='${(sessionMap.mode != "teacher") && sessionMap.isMinRatingsCompleted}'>
			<c:choose>
				<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
					<html:button property="continue"
						onclick="javascript:location.href='${continue}';"
						styleClass="button">
						<fmt:message key="label.continue" />
					</html:button>
				</c:when>

				<c:otherwise>
					<html:link href="#nogo" property="finish" styleId="finishButton" onclick="submitFinish();" styleClass="button">
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
	</div>
</div>
