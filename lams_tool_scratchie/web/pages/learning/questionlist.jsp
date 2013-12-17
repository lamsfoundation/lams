<%@ include file="/common/taglibs.jsp"%>

<%-- param has higher level for request attribute --%>
<c:if test="${not empty param.sessionMapID}">
	<c:set var="sessionMapID" value="${param.sessionMapID}" />
</c:if>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="scratchie" value="${sessionMap.scratchie}" />
<c:set var="isUserLeader" value="${sessionMap.isUserLeader}" />
<c:set var="isScratchingFinished" value="${sessionMap.isScratchingFinished}" />

<!-- isUserLeader=${sessionMap.isUserLeader} -->

<script type="text/javascript">
	//stop refreshing non-leaders in case scratching is finished
	if (${isScratchingFinished} && (refreshIntervalId != null)) {
		clearInterval(refreshIntervalId);
	}
</script>

		<c:forEach var="item" items="${sessionMap.itemList}" varStatus="status">
			<h3>${item.title}</h3>
			<div>${item.description}</div>
	
			<table id="scratches" class="alternative-color">
				<c:forEach var="answer" items="${item.answers}" varStatus="status">
					<tr id="tr${answer.uid}">
						<td style="width: 40px;">
							<c:choose>
								<c:when test="${answer.scratched && answer.correct}">
									<img src="<html:rewrite page='/includes/images/scratchie-correct.png'/>" class="scartchie-image">
								</c:when>
								<c:when test="${answer.scratched && !answer.correct}">
									<img src="<html:rewrite page='/includes/images/scratchie-wrong.png'/>" id="image-${item.uid}-${answer.uid}" class="scartchie-image">
								</c:when>
								<c:when test="${sessionMap.userFinished || item.unraveled || !isUserLeader || (mode == 'teacher')}">
									<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image">
								</c:when>
								<c:otherwise>
									<a href="#nogo" onclick="scratchItem(${item.uid}, ${answer.uid}); return false;" id="imageLink-${item.uid}-${answer.uid}">
										<img src="<html:rewrite page='/includes/images/answer-${status.index + 1}.png'/>" class="scartchie-image" id="image-${item.uid}-${answer.uid}" />
									</a>
								</c:otherwise>
							</c:choose>
							
							<c:if test="${(mode == 'teacher') && (answer.attemptOrder != -1)}">
								<div style="text-align: center; margin-top: 2px;">
									<fmt:message key="label.choice.number" >
										<fmt:param>${answer.attemptOrder}</fmt:param>
									</fmt:message>
								</div>
							</c:if>
						</td>
						
						<td style="vertical-align: middle;">
							${answer.description} 
						</td>
					</tr>
				</c:forEach>
			</table>
					
		</c:forEach>

		<%-- show reflection (only for teacher) --%>
		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn and (mode == 'teacher')}">
			<div class="small-space-top">
				<h2>
					${sessionMap.reflectInstructions}
				</h2>

				<c:choose>
					<c:when test="${empty sessionMap.reflectEntry}">
						<p>
							<em> <fmt:message key="message.no.reflection.available" />
							</em>
						</p>
					</c:when>
					<c:otherwise>
						<p>
							<lams:out escapeHtml="true" value="${sessionMap.reflectEntry}" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

		<c:if test="${(mode != 'teacher') &&  (isUserLeader || isScratchingFinished)}">
			<div class="space-bottom-top align-right">
				<c:choose>
					<c:when test="${sessionMap.reflectOn && isUserLeader}">
						<html:button property="finishButton" styleId="finishButton" onclick="return continueReflect()" styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:button property="finishButton" styleId="finishButton" onclick="return finish()" styleClass="button">
							<fmt:message key="label.submit" />
						</html:button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
		