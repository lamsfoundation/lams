<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" scope="request" />
<c:set var="newtopic"><html:rewrite page="/learning/newTopic.do?sessionMapID=${sessionMapID}" /></c:set>		
<c:set var="refresh"><html:rewrite page="/learning/viewForum.do?mode=${sessionMap.mode}&toolSessionID=${sessionMap.toolSessionID}&sessionMapID=${sessionMapID}&hideReflection=${sessionMap.hideReflection}" /></c:set>
<c:set var="continue"><html:rewrite page="/learning/newReflection.do?sessionMapID=${sessionMapID}" /></c:set>
<c:set var="finish"><html:rewrite page="/learning/finish.do?sessionMapID=${sessionMapID}" /></c:set>

<script type="text/javascript">
	function finishForum() {
		document.getElementById("finishButton").disabled = true;
		location.href = '${finish}';
	};			
</script>

<div data-role="page" data-dom-cache="false">

	<div data-role="header" data-theme="b">
		<h1>
			<c:out value="${sessionMap.title}" escapeXml="false" />
		</h1>
	</div><!-- /header -->

	<div data-role="content">	
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
		
			<c:if
				test="${sessionMap.mode == 'author' || sessionMap.mode == 'learner'}">
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
							<fmt:param value="${sessionMap.minimumReply}"></fmt:param>
							<fmt:param value="${sessionMap.maximumReply}"></fmt:param>
						</fmt:message>
					</div>
				</c:if>
				
				<c:if test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply ne 0 and sessionMap.maximumReply eq 0)}">
					<div class="info">
						<fmt:message key="label.postingLimits.forum.reminder.min">
							<fmt:param value="${sessionMap.minimumReply}"></fmt:param>
						</fmt:message>
					</div>
				</c:if>
				
				<c:if test="${not sessionMap.allowNewTopics and (sessionMap.minimumReply eq 0 and sessionMap.maximumReply ne 0)}">
					<div class="info">
						<fmt:message key="label.postingLimits.forum.reminder.max">
							<fmt:param value="${sessionMap.maximumReply}"></fmt:param>
						</fmt:message>
					</div>
				</c:if>
		
			</c:if>
		
			<%@ include file="/common/messages.jsp"%>
		
			<%@ include file="/jsps/learning/mobile/message/topiclist.jsp"%>
		
			<div style="padding-top: 7px; padding-left: 5px;" data-role="controlgroup" data-type="horizontal">
				<c:set var="buttonClass" value="button" />
				<c:if test="${sessionMap.finishedLock}">
					<c:set var="buttonClass" value="disabled" />
				</c:if>
				
				<c:if test='${sessionMap.allowNewTopics}'>
				
						<a name="newtopic" href="${newtopic}" data-rel="dialog"  data-role="button"
								 data-theme="c" data-icon="plus" onclick="this.href += '&reqID=' + (new Date()).getTime();"> <!--class="${buttonClass}"  disabled="${sessionMap.finishedLock}" -->
							<fmt:message key="label.newtopic" />
						</a>
					
				</c:if>
		
				<a href="${refresh}" id="refresh" data-theme="c" data-icon="refresh" data-role="button" onclick="this.href += '&reqID=' + (new Date()).getTime();">
					<fmt:message key="label.refresh" />
				</a>
			</div>
		
			<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and !sessionMap.hideReflection}">
				<div class="small-space-top">
					<h2>
						${sessionMap.reflectInstructions}
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
		


	</div><!-- /content -->

	<div data-role="footer" data-theme="b" class="ui-bar">
		<span class="ui-finishbtn-right">
				<c:if test='${sessionMap.mode != "teacher"}'>
					<c:choose>
						<c:when
							test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">
							<button name="continue" onclick="javascript:location.href='${continue}';" data-icon="arrow-r">
								<fmt:message key="label.continue" />
							</button>
						</c:when>
		
						<c:otherwise>
							<button name="finishButton" id="finishButton" data-icon="arrow-r" onclick="finishForum();">
								<c:choose>
				 					<c:when test="${sessionMap.activityPosition.last}">
				 						<fmt:message key="label.submit" />
				 					</c:when>
				 					<c:otherwise>
				 		 				<fmt:message key="label.finish" />
				 					</c:otherwise>
				 				</c:choose>
							</button>
						</c:otherwise>
					</c:choose>
				</c:if>
		</span>
	</div><!-- /footer -->
</div><!-- /page -->

